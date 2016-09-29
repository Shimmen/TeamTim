package teamtim.teamtimapp.presenter;

import android.content.Context;
import java.util.List;

import teamtim.teamtimapp.activities.PlayActivity;
import teamtim.teamtimapp.database.DatabaseInterface;
import teamtim.teamtimapp.database.MockDatabase;
import teamtim.teamtimapp.database.WordDifficulty;
import teamtim.teamtimapp.database.WordQuestion;
import teamtim.teamtimapp.speechSynthesizer.ISpeechSynthesizer;
import teamtim.teamtimapp.speechSynthesizer.SoundPlayer;

public class PlayPresenter {
    private List<WordQuestion> questions;
    private PlayActivity playActivity;
    private DatabaseInterface db = new MockDatabase();
    private int currentQ;
    private int score;
    private ISpeechSynthesizer speaker;

    public PlayPresenter(PlayActivity p){
        this.playActivity = p;
        questions = db.getQuestions(WordDifficulty.EASY, -1);
        currentQ = 0;

        playActivity.newQuestion(questions.get(currentQ));

        this.speaker = new SoundPlayer();
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

    public void speakWord(Context c) {
        speaker.speak(c, questions.get(currentQ));
    }
    /*
    public WordQuestion getNextQuestion(){

    }
    */
}
