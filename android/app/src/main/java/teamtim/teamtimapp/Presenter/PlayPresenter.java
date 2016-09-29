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
    private PlayActivity playActivity;

    private DatabaseInterface db = MockDatabase.getInstance();

    private List<WordQuestion> questions;
    private int currentQ;
    private int correctAnswers;
    private ISpeechSynthesizer speaker;

    public PlayPresenter(PlayActivity p, String category){
        this.playActivity = p;
        questions = db.getQuestions(category, -1);
        currentQ = 0;
        correctAnswers = 0;

        playActivity.newQuestion(questions.get(currentQ));

        this.speaker = new SoundPlayer();
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

    public void speakWord(Context c) {
        speaker.speak(c, questions.get(currentQ));
    }
    /*
    public WordQuestion getNextQuestion(){

    }
    */
}
