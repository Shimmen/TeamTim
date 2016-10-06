package teamtim.teamtimapp.managers;

import teamtim.teamtimapp.activities.PlayActivity;
import teamtim.teamtimapp.database.MockDatabase;

public class MultiPlayerClient implements OnResultCallback {
    GameState state;
    PlayActivity game;

    public MultiPlayerClient(){
        state = GameState.WAIT_INPUT;
    }

    //TODO override something
    private void onReceive(String key, String value){
        String[] args = value.split(";");
        switch(key){
            case "NEW_QUESTION":
                updateScore(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
                newQuestion(Integer.parseInt(args[2]));
                break;
            case "END_GAME":
                //TODO: End game in a multiplayer manner
                game.endGame(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
                break;
        }
    }

    private void send(String sting){

    }

    @Override
    public void onResult(ResultKey key, int value) {
        switch (key){
            case READY:
                game = PlayActivity.getInstance();
                send("READY");
                break;
        }
    }

    public void updateScore(int player1, int player2){
        //Something
    }

    private void newQuestion(int id){
        game.newQuestion(MockDatabase.getInstance().getQuestion(id));
    }
}
