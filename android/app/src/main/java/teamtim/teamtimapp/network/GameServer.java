package teamtim.teamtimapp.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import teamtim.teamtimapp.BuildConfig;
import teamtim.teamtimapp.network.NetworkUtil;
import teamtim.teamtimapp.database.WordQuestion;

public class GameServer extends Thread {

    private class Scores {
        public int score_p1;
        public int score_p2;
    }
    // Data types

    enum State {
        WAIT_FOR_CLIENT_DATA,
        SEND_NEXT_QUESTION,
        WAIT_FOR_QUESTION_RESULTS,
        SEND_GAME_RESULTS,
        END_OF_GAME
    }

    // Static

    public static final int PORT = 61992;

    // State

    private InetAddress hostAddress;

    private State currentState = State.WAIT_FOR_CLIENT_DATA;

    private List<WordQuestion> wordQuestions = new ArrayList<>();
    private int currentQuestionIndex = 0;

    private String c1Name;
    private String c2Name;


    public GameServer(InetAddress hostAddress) {
        super("TeamTimServerThread");
        this.hostAddress = hostAddress;
    }

    @Override
    public void run() {
        try {

            System.out.println("Server: starting up!");

            ServerSocket serverSocket = new ServerSocket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(hostAddress, PORT));

            System.out.println("Server: waiting for client connections on " + serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort());

            // Connect to the two clients. Will block until satisfied
            Socket client1 = serverSocket.accept();
            Socket client2 = serverSocket.accept();

            int client1Score = 0;
            int client2Score = 0;

            if (BuildConfig.DEBUG && !client1.isConnected()) throw new AssertionError();
            if (BuildConfig.DEBUG && !client2.isConnected()) throw new AssertionError();
            System.out.println("Server: successfully connected to client 1 (" + client1.getInetAddress() + ":" + client1.getPort() + ")");
            System.out.println("Server: successfully connected to client 2 (" + client2.getInetAddress() + ":" + client2.getPort() + ")");

            runLoop:
            while (currentState != State.END_OF_GAME) {
                switch (currentState) {

                    case WAIT_FOR_CLIENT_DATA: {

                        System.out.println("Server: waiting for client data!");

                        Map<String, String> c1Data = NetworkUtil.waitForAndReadData(client1);
                        System.out.println("Server: got packet from client 1: " + c1Data);
                        c1Name = c1Data.get("NAME");

                        Map<String, String> c2Data = NetworkUtil.waitForAndReadData(client2);
                        System.out.println("Server: got packet from client 2: " + c2Data);
                        c2Name = c2Data.get("NAME");

                        // Locate the questions data
                        String encodedQuestions = c1Data.containsKey("QUESTIONS") ? c1Data.get("QUESTIONS") : c2Data.get("QUESTIONS");
                        assert encodedQuestions != null;

                        // Collect all questions
                        wordQuestions = NetworkUtil.decodeWordQuestions(encodedQuestions);
                        System.out.println("Server: found questions: " + wordQuestions);

                        // Shuffle questions
                        Collections.shuffle(wordQuestions);

                        currentState = State.SEND_NEXT_QUESTION;
                    }
                    break;

                    case SEND_NEXT_QUESTION: {

                        if (BuildConfig.DEBUG && currentQuestionIndex >= wordQuestions.size()) throw new AssertionError();
                        System.out.println("Server: sending question!");

                        WordQuestion question = wordQuestions.get(currentQuestionIndex);
                        Map<String, String> newQuestionData = new HashMap<>();
                        newQuestionData.put("METHOD", "NEW_QUESTION");
                        newQuestionData.put("QUESTION", NetworkUtil.encodeQuestion(question));
                        newQuestionData.put(c1Name, String.valueOf(client1Score));
                        newQuestionData.put(c2Name, String.valueOf(client2Score));

                        NetworkUtil.sendData(newQuestionData, client1);
                        NetworkUtil.sendData(newQuestionData, client2);

                        currentState = State.WAIT_FOR_QUESTION_RESULTS;
                    }
                    break;

                    case WAIT_FOR_QUESTION_RESULTS: {

                        System.out.println("Server: waiting for current question results!");

                        Map<String, String> c1ResultData  = NetworkUtil.waitForAndReadData(client1);
                        System.out.println("Server: got question result from client 1: " + c1ResultData);

                        Map<String, String> c2ResultData  = NetworkUtil.waitForAndReadData(client2);
                        System.out.println("Server: got question result from client 2: " + c2ResultData);

                        Scores score = calculateScores(
                                Integer.parseInt(c1ResultData.get("QUESTION_RESULT")),
                                Integer.parseInt(c2ResultData.get("QUESTION_RESULT")),
                                Integer.parseInt(c1ResultData.get("QUESTION_TIME")),
                                Integer.parseInt(c2ResultData.get("QUESTION_TIME")));
                        client1Score += score.score_p1;
                        client2Score += score.score_p2;

                        // Next question or end of game
                        currentQuestionIndex += 1;
                        if (currentQuestionIndex < wordQuestions.size()) {
                            currentState = State.SEND_NEXT_QUESTION;
                        } else {
                            System.out.println("Server: no more questions!");
                            currentState = State.SEND_GAME_RESULTS;
                        }
                    }
                    break;

                    case SEND_GAME_RESULTS: {
                        System.out.println("Server: sending total game results!");

                        Map<String, String> gameResultData = new HashMap<>();
                        gameResultData.put("METHOD", "GAME_RESULTS");
                        gameResultData.put("C1SCORE", String.valueOf(client1Score));
                        gameResultData.put("C2SCORE", String.valueOf(client2Score));

                        NetworkUtil.sendData(gameResultData, client1);
                        NetworkUtil.sendData(gameResultData, client2);

                        currentState = State.END_OF_GAME;
                    }
                    break;

                    case END_OF_GAME:
                    default: {
                        break runLoop;
                    }
                }
            }

            System.out.println("Server: end of game, shutting down!");

        } catch (IOException e) {
            // TODO: Handle somehow!
            e.printStackTrace();
        }
    }

    private Scores calculateScores(int score_p1, int score_p2, int time_p1, int time_p2){
        Scores score = new Scores();
        score.score_p1 = score_p1 * 10;
        score.score_p2 = score_p2 * 10;
        if (time_p1 < time_p2){
            score.score_p2 *= 0.5f;
        } else if (time_p1 > time_p2){
            score.score_p1 *= 0.5f;
        }
        return score;
    }
}