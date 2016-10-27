package teamtim.teamtimapp.managers;

import android.os.Build;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import teamtim.teamtimapp.application.TeamTimApp;
import teamtim.teamtimapp.network.NetworkUtil;
import teamtim.teamtimapp.activities.PlayActivity;
import teamtim.teamtimapp.database.WordQuestion;
import teamtim.teamtimapp.network.ClientThread;

public final class MultiPlayerClient extends QuestionResultListener implements ClientThread.OnDataListener {

    private ClientThread clientThread;
    private PlayActivity currentPlayActivity;
    private GameData gameData;
    private boolean isHosting;
    static final long serialVersionUID = 4;

    public MultiPlayerClient(String clientName, InetAddress serverAddress, List<WordQuestion> questions) {

        // Make this the global globalListener for all question result events
        QuestionResultListener.setGlobalListener(this);

        isHosting = clientName.equals("InitiatingClient");

        gameData = new GameData();

        // Start the client thread
        clientThread = new ClientThread(clientName, serverAddress);
        clientThread.setOnDataListener(this);
        clientThread.start();

        Map<String, String> readyData = new HashMap<>();
        readyData.put("METHOD", "READY");
        readyData.put("NAME", clientName);

        if (questions != null) {
            // Add questions to ready packet
            readyData.put("QUESTIONS", NetworkUtil.encodeQuestions(questions));
        }

        System.out.println(clientThread.getName() + ": enqueueing ready packet to send queue! " + readyData);
        clientThread.addDataToSendQueue(readyData);
    }

    @Override
    public void onData(Map<String, String> data) {
        String clientName = clientThread.getName();

        switch (data.get("METHOD")) {


            case "NEW_QUESTION":
                WordQuestion currentQuestion = NetworkUtil.decodeQuestion(data.get("QUESTION"));
                System.out.println(clientName + ": received new question: " + data + ", i.e., " + currentQuestion.getWord());
                if(isHosting) {
                    updateScore(Integer.parseInt(data.get("InitiatingClient")), Integer.parseInt(data.get("ExternalClient")));
                } else {
                    updateScore(Integer.parseInt(data.get("ExternalClient")), Integer.parseInt(data.get("InitiatingClient")));
                }
                //Add question to gameData
                gameData.addQuestion(currentQuestion);
                // Load next question
                if (currentPlayActivity != null) {
                    currentPlayActivity.newQuestion(currentQuestion);
                }
                break;
            case "GAME_RESULTS":
                System.out.println(clientName + ": received game results: " + data);
                if(isHosting) {
                    updateScore(Integer.parseInt(data.get("InitiatingClient")), Integer.parseInt(data.get("ExternalClient")));
                } else {
                    updateScore(Integer.parseInt(data.get("ExternalClient")), Integer.parseInt(data.get("InitiatingClient")));
                }
                clientThread.closeSocket();
                ((TeamTimApp)currentPlayActivity.getApplication()).becomeActiveOnConnectedListener();
                currentPlayActivity.endMultiGame(gameData);
                break;

            default:
                System.err.println(clientName + ": got some unknown packet?!" + data);

        }
    }

    @Override
    public void onPlayActivityCreated(PlayActivity currentPlayActivity) {
        this.currentPlayActivity = currentPlayActivity;
        currentPlayActivity.setPlayerOneScore(0);
        currentPlayActivity.setPlayerTwoScore(0);
    }

    @Override
    public void onQuestionResult(int result, int time, String answer) {
        System.out.println("MultiPlayerClient: got some question results!");

        gameData.addAnswer(answer);

        System.out.println(clientThread.getName() + ": sending question results (" + result + ")!");
        Map<String, String> resultData = new HashMap<>();
        resultData.put("QUESTION_RESULT", String.valueOf(result));
        resultData.put("QUESTION_TIME", String.valueOf(time));
        clientThread.addDataToSendQueue(resultData);
    }

    @Override
    public void onPause() {
        Map<String, String> resultData = new HashMap<>();
        resultData.put("PAUSE", "");
        clientThread.addDataToSendQueue(resultData);
        //Do something
    }

    @Override
    public void onResume() {
        //Do nothing...
    }

    public void updateScore(int player1, int player2){
        if (currentPlayActivity != null) {
            currentPlayActivity.setPlayerOneScore(player1);
            gameData.setP1Score(player1);

            currentPlayActivity.setPlayerTwoScore(player2);
            gameData.setP2Score(player2);
        }
    }

}
