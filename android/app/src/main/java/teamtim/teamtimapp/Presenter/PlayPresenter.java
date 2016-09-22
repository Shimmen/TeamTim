package teamtim.teamtimapp.Presenter;

public class PlayPresenter {


    public PlayPresenter(){

    }

    public boolean checkAnswer(String answer, String question){
        if(answer.equalsIgnoreCase(question)){
            return false;
        }
    }
    /*
    public WordQuestion getNextQuestion(){

    }
    */
}
