package teamtim.teamtimapp.network;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import teamtim.teamtimapp.BuildConfig;

public class ClientThread extends Thread {

    // Data types

    public interface OnDataListener {
        /**
         * Callback/listener for data from the server.
         *
         * @param data the incoming data as key-value pairs
         */
        void onData(Map<String, String> data);
    }

    public enum State {
        WAIT_FOR_AND_SEND_OUTGOING_DATA,
        WAIT_FOR_INCOMING_DATA
    }

    // Data

    private final InetAddress serverAddress;
    private final int serverPort;

    private final Object socketLock = new Object();
    private volatile boolean socketShouldClose = false;

    private State currentState;
    private OnDataListener onDataListener;
    private Queue<Map<String, String>> sendQueue = new ConcurrentLinkedQueue<>();

    public ClientThread(InetAddress serverAddress, int serverPort, OnDataListener onDataListener) {
        this.serverAddress= serverAddress;
        this.serverPort = serverPort;
        this.onDataListener = onDataListener;

        // The first thing a client does is send the 'connection packet'
        currentState = State.WAIT_FOR_AND_SEND_OUTGOING_DATA;
    }

    /**
     * Data submitted here will be sent to the server as soon as possible!
     *
     * @param data the data as key-value pairs
     */
    public void addDataToSendQueue(Map<String, String> data) {
        if (data != null) {
            synchronized (socketLock) {
                sendQueue.add(data);

                // Make sure the thread will send the newly added data
                currentState = State.WAIT_FOR_AND_SEND_OUTGOING_DATA;
            }
        }
/*
    FIXME: Implement this in a proper wait/notify way, without creating any deadlocks!
        synchronized (socketLock) {
            sendQueue.add(data);
            socketLock.notifyAll();
        }
*/
    }

    public void closeSocket() {
        synchronized (socketLock) {
            socketShouldClose = true;
        }
    }

    private boolean socketShouldClose() {
        synchronized (socketLock) {
            return socketShouldClose;
        }
    }

    @Override
    public void run() {
        try {

            Socket socket = new Socket(serverAddress, serverPort);

            // Buffer for incoming data (note: never receive more than 1024*10 bytes of data at once!)
            byte[] buffer = new byte[1024 * 10];

            if (!socket.isConnected()) {
                System.out.println("Client (this device) could not connect to the server! Closing down client.");
                return;
            }

            while (!socketShouldClose()) {
                switch (currentState) {

                    case WAIT_FOR_INCOMING_DATA: {

                        // The buffer is 'big enough'. Assume all is read in one go. (Will block.)
                        int bytesRead = socket.getInputStream().read(buffer, 0, buffer.length);

                        // We've read a packet that is bigger than out buffer. This should be considered an error!
                        if (BuildConfig.DEBUG && bytesRead >= buffer.length) throw new AssertionError();

                        // Convert raw data to a nice map
                        Map<String, String> data = fromRawData(buffer, bytesRead);

                        onDataListener.onData(data);

                        // Set to the outgoing data stage.
                        // NOTE: This only works because the app always alternates between sending and receiving.
                        // It will never send twice or more in a row or receive twice or more in a row.
                        currentState = State.WAIT_FOR_AND_SEND_OUTGOING_DATA;

                    } break;

                    case WAIT_FOR_AND_SEND_OUTGOING_DATA: {

                        if (sendQueue.isEmpty()) {
                            // FIXME: Implement this in a proper wait/notify way, without creating any deadlocks!

                            // Just sleep for a while...
                            Thread.sleep(200);

                            // Wait until there is data in the send queue! We will be notified when there is data
                            //socketLock.wait();
                        } else {

                            OutputStream socketOut = socket.getOutputStream();

                            for (Map<String, String> data : sendQueue) {
                                // Convert data to byte array and write unbuffered
                                byte[] rawData = asByteArray(data);
                                socketOut.write(rawData);
                            }

                            // Flush the stream to make sure everything is sent
                            socketOut.flush();
                        }

                        // Set to the incoming data stage.
                        // NOTE: This only works because the app always alternates between sending and receiving.
                        // It will never send twice or more in a row or receive twice or more in a row.
                        currentState = State.WAIT_FOR_INCOMING_DATA;

                    } break;

                }
            }

            socket.close();

        } catch (IOException | InterruptedException e) {
            // TODO: Handle these a bit more gracefully!
            e.printStackTrace();
        }
    }

    private Map<String, String> fromRawData(byte[] buffer, int numBytes) {
        Map<String, String> data = new HashMap<>();

        // TODO: The source says it's unsupported?! But I can't find the StringFactory class..?
        String stringData = new String(buffer, 0, numBytes, Charset.forName("UTF-8"));

        // TODO: Make it a bit more safe maybe? Probably.
        String[] keyValuePairs = stringData.split(";");
        for (String pair: keyValuePairs) {
            String[] s = pair.split("=");
            data.put(s[0], s[1]);
        }

        return data;
    }

    private byte[] asByteArray(Map<String, String> data) {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            String stringEntry = entry.getKey() + "=" + entry.getValue() + ";";
            builder.append(stringEntry);
        }

        String stringData = builder.toString();
        return stringData.getBytes(Charset.forName("UTF-8"));
    }

}
