package teamtim.teamtimapp.managers;

import java.util.List;

import teamtim.teamtimapp.activities.PlayActivity;
import teamtim.teamtimapp.database.MockDatabase;
import teamtim.teamtimapp.database.WordQuestion;

public class SinglePlayerClient extends OnResultListener {

    private PlayActivity game;

    private List<WordQuestion> questions;
    private int currentQ = 0;
    private int rightAnswers = 0;

    public SinglePlayerClient(String category) {
        OnResultListener.setGlobalListener(this);
        questions = MockDatabase.getInstance().getQuestions(category, -1);
    }

    @Override
    public void onResult(ResultKey key, int value) {
        System.out.println("SinglePlayerClient: got some result!");

        switch (key){
            case READY:
                start();
                break;
            case SUBMIT:
                finnishRound(value);
                break;
        }
    }

    private void start(){
        game = PlayActivity.getInstance();
        game.newQuestion(questions.get(currentQ));
    }

    private void finnishRound(int value){
        rightAnswers+=value;

        currentQ++;
        if (currentQ >= questions.size()) {
            game.endGame(rightAnswers, questions.size());
            return;
        }
        game.newQuestion(questions.get(currentQ));
    }
}
