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

    //Put into another class or something later
    //And write better methods
    public char[] shuffle(char[] wordLetters) {
        for (int i = 0; i < wordLetters.length; i ++) {
            int newPos = (int)((Math.random() * wordLetters.length)-1);
            char tempChar = wordLetters[newPos];
            wordLetters[newPos] = wordLetters[i];
            wordLetters[i] = tempChar;
        }
        return wordLetters;
    }

    public char[] splitString(String word) {
        char[] splitString = new char[word.length()];
        for (int i = 0; i < word.length(); i++) {
            splitString[i] = word.charAt(i);
        }
        return splitString;
    }

    public void speakWord(String word, View v) {
        speaker.speak(word);
    }
    /*
    public WordQuestion getNextQuestion(){

    }
    */
}
