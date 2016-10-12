package teamtim.teamtimapp.managers;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import teamtim.teamtimapp.NetworkUtil;
import teamtim.teamtimapp.activities.PlayActivity;
import teamtim.teamtimapp.database.WordQuestion;
import teamtim.teamtimapp.network.ClientThread;

public class MultiPlayerClient extends QuestionResultListener implements ClientThread.OnDataListener {

    private ClientThread clientThread;
    private PlayActivity currentPlayActivity;

    public MultiPlayerClient(String clientName, InetAddress serverAddress, List<WordQuestion> questions) {

        // Make this the global globalListener for all question result events
        QuestionResultListener.setGlobalListener(this);

        // Start the client thread
        clientThread = new ClientThread(clientName, serverAddress);
        clientThread.setOnDataListener(this);
        clientThread.start();

        Map<String, String> readyData = new HashMap<>();
        readyData.put("METHOD", "READY");

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

                // Load next question
                currentPlayActivity.newQuestion(currentQuestion);
                break;
            case "GAME_RESULTS":
                System.out.println(clientName + ": received game results: " + data);
                // TODO: Use data!
                updateScore(Integer.parseInt(data.get("C1SCORE")), Integer.parseInt(data.get("C2SCORE")));
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
    public void onQuestionResult(int result) {
        System.out.println("MultiPlayerClient: got some question results!");

        System.out.println(clientThread.getName() + ": sending question results (" + result + ")!");
        Map<String, String> resultData = new HashMap<>();
        resultData.put("QUESTION_RESULT", String.valueOf(result));
        clientThread.addDataToSendQueue(resultData);
    }

    public void updateScore(int player1, int player2){
        currentPlayActivity.setPlayerOneScore(player1);
        currentPlayActivity.setPlayerTwoScore(player2);
    }

}
