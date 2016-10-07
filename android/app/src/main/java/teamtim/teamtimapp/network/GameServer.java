package teamtim.teamtimapp.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import teamtim.teamtimapp.BuildConfig;
import teamtim.teamtimapp.database.WordQuestion;

public class GameServer extends Thread {

    // Data types

    enum State {
        WAIT_FOR_CLIENT_DATA,
        SEND_NEXT_QUESTION,
        WAIT_FOR_QUESTION_RESULTS,
        SEND_GAME_RESULTS,
        END_OF_GAME
    }

    // Static

    public static final int PORT = 1342;
    public static final int BACKLOG_SIZE = 50; // (The value used in the default constructor)

    // State

    private InetAddress hostAddress;

    private State currentState = State.WAIT_FOR_CLIENT_DATA;

    private List<WordQuestion> wordQuestions;
    private int currentQuestionIndex = 0;


    public GameServer(InetAddress hostAddress, List<WordQuestion> wordQuestions) {
        this.hostAddress = hostAddress;
        this.wordQuestions = wordQuestions;
    }

    @Override
    public void run() {
        try {

            ServerSocket serverSocket = new ServerSocket(PORT, BACKLOG_SIZE, hostAddress);

            // Connect to the two clients. Will block until satisfied
            Socket client1 = serverSocket.accept();
            //Socket client2 = serverSocket.accept();

            // They should be connected by now, right?
            if (BuildConfig.DEBUG && !client1.isConnected()) throw new AssertionError();
            //if (BuildConfig.DEBUG && !client2.isConnected()) throw new AssertionError();

            runLoop:
            while (currentState != State.END_OF_GAME) {
                switch (currentState) {

                    case WAIT_FOR_CLIENT_DATA: {
                        // TODO (use buffered reader to read data from sockets)!
                        int value = client1.getInputStream().read();
                        System.out.println("Server got a packet! DATA: " + value);
                    }
                    break;

                    case SEND_NEXT_QUESTION: {

                        if (BuildConfig.DEBUG && currentQuestionIndex >= wordQuestions.size()) throw new AssertionError();

                        WordQuestion question = wordQuestions.get(currentQuestionIndex);
                        // TODO: Send question to both clients!

                        incrementQuestionAndSetState();
                    }
                    break;

                    case WAIT_FOR_QUESTION_RESULTS: {
                        // TODO!
                    }
                    break;

                    case SEND_GAME_RESULTS: {
                        // TODO!

                        currentState = State.END_OF_GAME;
                    }
                    break;

                    case END_OF_GAME:
                    default: {
                        break runLoop;
                    }
                }
            }

        } catch (IOException e) {
            // TODO: Handle somehow!
            e.printStackTrace();
        }
    }

    private void incrementQuestionAndSetState() {
        currentQuestionIndex += 1;

        if (currentQuestionIndex < wordQuestions.size()) {
            currentState = State.WAIT_FOR_QUESTION_RESULTS;
        } else {
            // If all word questions are played
            currentState = State.SEND_GAME_RESULTS;
        }
    }

}
