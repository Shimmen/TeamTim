package teamtim.teamtimapp.managers;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import teamtim.teamtimapp.activities.PlayActivity;
import teamtim.teamtimapp.database.MockDatabase;
import teamtim.teamtimapp.network.ClientThread;

public class MultiPlayerClient extends OnResultListener implements ClientThread.OnDataListener {
    GameState state;
    PlayActivity game;
    ClientThread clientThread;

    public MultiPlayerClient(InetAddress ip){
        super.setListener(this);
        clientThread = new ClientThread(ip, this);
        clientThread.start();
    }

    private void send(Map<String, String> data){
        clientThread.addDataToSendQueue(data);
    }

    @Override
    public void onData(Map<String, String> data) {
        switch (data.get("METHOD")){
            case "NEW_QUESTION":
                updateScore(Integer.parseInt(data.get("p1Score")), Integer.parseInt(data.get("p2Score")));
                newQuestion(Integer.parseInt(data.get("questionId")));
                break;
            case "END_GAME":
                //TODO: End game in a multiplayer manner
                game.endGame(Integer.parseInt(data.get("p1Score")), Integer.parseInt(data.get("p2Score")));
                break;
        }
    }

    @Override
    public void onResult(ResultKey key, int value) {
        System.out.println("MultiPlayerClient: got some result!");

        Map<String, String> data = new HashMap<>();
        switch (key){
            case READY:
                data.put("METHOD", "READY");
                break;
            case SUBMIT:
                data.put("METHOD", "SUBMIT");
                data.put("SCORE", ""+value);
                break;
        }
        send(data);
    }

    public void updateScore(int player1, int player2){
        //Something
    }

    private void newQuestion(int id){
        game.newQuestion(MockDatabase.getInstance().getQuestion(id));
    }

}
