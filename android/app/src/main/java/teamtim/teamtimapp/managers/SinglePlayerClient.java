package teamtim.teamtimapp.managers;

import java.util.List;

import teamtim.teamtimapp.activities.PlayActivity;
import teamtim.teamtimapp.database.MockDatabase;
import teamtim.teamtimapp.database.WordQuestion;

public class SinglePlayerClient extends QuestionResultListener {

    private PlayActivity currentPlayActivity;

    private int score = 0;

    private List<WordQuestion> questions;
    private int currentQ = 0;
    private GameData gameData;

    public SinglePlayerClient(String category) {
        QuestionResultListener.setGlobalListener(this);
        questions = MockDatabase.getInstance().getQuestions(category, -1);
        gameData.setQuestions(questions);
    }

    @Override
    public void onPlayActivityCreated(PlayActivity currentPlayActivity) {
        this.currentPlayActivity = currentPlayActivity;
        currentPlayActivity.newQuestion(questions.get(currentQ));
        currentPlayActivity.setPlayerOneScore(score);
    }

    @Override
    public void onQuestionResult(int result, int time, String answer) {
        System.out.println("SinglePlayerClient: got some result! (" + result + " points)");
        score += result;
        currentQ++;

        gameData.addAnswer(answer);

        if (currentQ < questions.size()) {
            currentPlayActivity.newQuestion(questions.get(currentQ));
        } else {
            gameData.setP1Score(score);
            currentPlayActivity.endGame(score, questions.size(), questions);
        }

        currentPlayActivity.setPlayerOneScore(score);

    }
}
