package teamtim.teamtimapp.presenter;

import java.util.List;

import teamtim.teamtimapp.activities.PlayActivity;
import teamtim.teamtimapp.database.DatabaseInterface;
import teamtim.teamtimapp.database.MockDatabase;
import teamtim.teamtimapp.database.WordDifficulty;
import teamtim.teamtimapp.database.WordQuestion;

public class PlayPresenter {
    private List<WordQuestion> questions;
    private PlayActivity playActivity;
    private DatabaseInterface db = new MockDatabase();
    private int currentQ;
    private int score;

    public PlayPresenter(PlayActivity p){
        this.playActivity = p;
        questions = db.getQuestions(WordDifficulty.EASY, -1);
        currentQ = 0;

        playActivity.newQuestion(questions.get(currentQ));
    }

    public void checkAnswer(String answer) {
        WordQuestion currentQuestion = questions.get(currentQ);

        if (answer.equalsIgnoreCase(currentQuestion.getWord())){

            // Get next question index. Cycle for now, so we don't access an index outside the array!
            currentQ++;
            currentQ %= questions.size();
            playActivity.newQuestion(questions.get(currentQ));

        } else {
            // TODO: Handle incorrect word!
        }

    }
    /*
    public WordQuestion getNextQuestion(){

    }
    */
}
