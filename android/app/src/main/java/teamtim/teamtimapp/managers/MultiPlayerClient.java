package teamtim.teamtimapp.managers;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

        try {
            switch (data.get("METHOD")) {


                case "NEW_QUESTION":
                    WordQuestion currentQuestion = NetworkUtil.decodeQuestion(data.get("QUESTION"));
                    System.out.println(clientName + ": received new question: " + data + ", i.e., " + currentQuestion.getWord());

                    System.out.println(clientName + ": answering question...");
                    Thread.sleep(1000);
                    int currentQuestionResult = 10 + new Random().nextInt(10);

                    System.out.println(clientName + ": sending question results (" + currentQuestionResult + ")!");
                    Map<String, String> resultData = new HashMap<>();
                    resultData.put("QUESTION_RESULT", String.valueOf(currentQuestionResult));
                    clientThread.addDataToSendQueue(resultData);
                    break;

                case "GAME_RESULTS":
                    System.out.println(clientName + ": received game results: " + data);
                    // TODO: Use data!
                    updateScore(0, 0);
                    break;

                default:
                    System.err.println(clientName + ": got some unknown packet?!" + data);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPlayActivityCreated(PlayActivity currentPlayActivity) {
        this.currentPlayActivity = currentPlayActivity;
    }

    @Override
    public void onQuestionResult(int result) {
        System.out.println("MultiPlayerClient: got some question results!");

        Map<String, String> resultData = new HashMap<>();
        resultData.put("QUESTION_RESULT", String.valueOf(result));
        clientThread.addDataToSendQueue(resultData);
    }

    public void updateScore(int player1, int player2){
        //Something
    }

}
