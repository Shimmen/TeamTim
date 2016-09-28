package teamtim.teamtimapp.presenter;

import android.view.View;

import java.util.List;

import teamtim.teamtimapp.activities.PlayActivity;
import teamtim.teamtimapp.database.DatabaseInterface;
import teamtim.teamtimapp.database.MockDatabase;
import teamtim.teamtimapp.database.WordDifficulty;
import teamtim.teamtimapp.database.WordQuestion;
import teamtim.teamtimapp.speechSynthesizer.ISpeechSynthesizer;
import teamtim.teamtimapp.speechSynthesizer.SpeechSynthesizer;

public class PlayPresenter {
    private List<WordQuestion> questions;
    private PlayActivity playActivity;
    private DatabaseInterface db = new MockDatabase();
    private int currentQ;
    private int correctAnswers;
    private ISpeechSynthesizer speaker;

    public PlayPresenter(PlayActivity p){
        this.playActivity = p;
        questions = db.getQuestions(WordDifficulty.EASY, -1);
        currentQ = 0;
        correctAnswers = 0;

        playActivity.newQuestion(questions.get(currentQ));

        this.speaker = new SpeechSynthesizer(p.getApplicationContext());
    }

    public void checkAnswer(String answer) {
        WordQuestion currentQuestion = questions.get(currentQ);
        currentQ++;
        if (answer.equalsIgnoreCase(currentQuestion.getWord())) correctAnswers++;
        if (currentQ < questions.size()) playActivity.newQuestion(questions.get(currentQ));
        else playActivity.endGame(correctAnswers, questions.size());
    }

    public void speakWord(String word, View v) {
        speaker.speak(word);
    }
    /*
    public WordQuestion getNextQuestion(){

    }
    */
}
