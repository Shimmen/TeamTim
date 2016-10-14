package teamtim.teamtimapp.managers;

import java.io.Serializable;
import java.util.List;

import teamtim.teamtimapp.database.WordQuestion;

public class GameData implements Serializable{

    private List<WordQuestion> questions;
    private List<String> answers;
    private int p1Score;
    private int p2Score;

    public GameData(){

    }

    public void setQuestions(List<WordQuestion> w){
        questions = w;
    }

    public void addQuestion(WordQuestion w){
        questions.add(w);
    }

    public void setP1Score(int score){
        p1Score = score;
    }

    public void setP2Score(int score){
        p2Score = score;
    }

    public void setAnswers(List<String> answers){
        this.answers = answers;
    }

    public void addAnswer(String answer){
        answers.add(answer);
    }



    public List<WordQuestion> getQuestions(){
        return questions;
    }

    public List<String> getAnswers(){
        return answers;
    }

    public int getP1Score(){
        return p1Score;
    }

    public int getP2Score(){
        return p2Score;
    }

}
