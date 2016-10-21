package teamtim.teamtimapp.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import teamtim.teamtimapp.database.MockDatabase;
import teamtim.teamtimapp.database.WordQuestion;

public class NetworkUtil {

    private NetworkUtil() {

    }

    public static Map<String, String> waitForAndReadData(Socket socket) {
        try {

            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader in = new BufferedReader(inputStreamReader);

            // Read packet as a line
            String line = in.readLine();

            // Convert raw data to a nice map
            return NetworkUtil.dataFromString(line);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return empty map if there was an error
        return new HashMap<>();
    }

    public static void sendData(Map<String, String> data, Socket socket) {
        try {

            byte[] rawData = NetworkUtil.asByteArray(data);
            socket.getOutputStream().write(rawData);

            // Not optimal to write this every time but easier to handle
            socket.getOutputStream().flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> dataFromString(String packet) {
        Map<String, String> data = new HashMap<>();

        String[] keyValuePairs = packet.split(";");
        for (String pair: keyValuePairs) {
            String[] s = pair.split("=");
            data.put(s[0], s[1]);
        }

        return data;
    }

    private static byte[] asByteArray(Map<String, String> data) {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            String stringEntry = entry.getKey() + "=" + entry.getValue() + ";";
            builder.append(stringEntry);
        }

        // Since we now use BufferedReader.readLine() it requires line breaks. This works well, though, since a line
        // break can signify a packet boundary.
        builder.append('\n');

        String stringData = builder.toString();
        return stringData.getBytes(Charset.forName("UTF-8"));
    }

    public static String encodeQuestion(WordQuestion wordQuestion) {
        return String.valueOf(wordQuestion.getQuestionId());
    }

    public static String encodeQuestions(List<WordQuestion> wordQuestions) {
        StringBuilder s = new StringBuilder("[");

        for (int i = 0; i < wordQuestions.size(); i++) {
            s.append(encodeQuestion(wordQuestions.get(i)));
            if (i < wordQuestions.size() - 1) {
                s.append(',');
            }
        }

        return s.append(']').toString();
    }

    public static WordQuestion decodeQuestion(String encodedQuestion) {
        int id = Integer.parseInt(encodedQuestion);
        if (id != -1) {
            return MockDatabase.getInstance().getQuestion(id);
        } else {
            return null;
        }
    }

    public static List<WordQuestion> decodeWordQuestions(String encodedQuestions) {
        List<WordQuestion> wordQuestions = new ArrayList<>();

        String list = encodedQuestions.substring(1, encodedQuestions.length() - 1);
        for (String encodedQuestion: list.split(",")) {
            WordQuestion question = decodeQuestion(encodedQuestion);
            if (question != null) {
                wordQuestions.add(question);
            }
        }

        return wordQuestions;
    }

}
