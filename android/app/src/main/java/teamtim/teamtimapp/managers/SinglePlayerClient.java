package teamtim.teamtimapp.managers;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;

import java.io.Serializable;
import java.util.List;

import teamtim.teamtimapp.activities.MainMenuActivity;
import teamtim.teamtimapp.activities.MultiplayerActivity;
import teamtim.teamtimapp.activities.PlayActivity;
import teamtim.teamtimapp.database.DatabaseInterface;
import teamtim.teamtimapp.database.MockDatabase;
import teamtim.teamtimapp.database.WordQuestion;

/*TODO:
    Include endgame in this "controller"
    ...or redo everything if serializable is bad
 */


public class SinglePlayerClient implements OnResultCallback, Serializable {

    private GameState state; //lol
    private DatabaseInterface db = MockDatabase.getInstance();

    private PlayActivity game;

    private List<WordQuestion> questions;
    private int currentQ = 0;
    private int rightAnswers = 0;

    public SinglePlayerClient(String category) {
        questions = db.getQuestions(category, -1);
    }

    public void start(){
        game = PlayActivity.getInstance();
        game.newQuestion(questions.get(currentQ));
    }

    @Override
    public void onResult(boolean rightAnswer) {
        if (rightAnswer) rightAnswers++;

        currentQ++;
        if (currentQ >= questions.size()) {
            game.endGame(rightAnswers, questions.size());
            return;
        }
        game.newQuestion(questions.get(currentQ));
    }
}
