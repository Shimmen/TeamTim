package teamtim.teamtimapp.managers;

import java.util.List;

import teamtim.teamtimapp.activities.PlayActivity;
import teamtim.teamtimapp.database.MockDatabase;
import teamtim.teamtimapp.database.WordQuestion;

public class SinglePlayerClient extends QuestionResultListener {

    private PlayActivity currentPlayActivity;

    private String category;
    private int score = 0;

    private List<WordQuestion> questions;
    private int currentQ = 0;
    private GameData gameData;

    public SinglePlayerClient(String category) {
        gameData = new GameData();
        QuestionResultListener.setGlobalListener(this);
        this.category = category;
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
            MockDatabase.getInstance().updateCategorySuccessRatio(category, score, questions.size());
            currentPlayActivity.endSingleGame(gameData);
        }

        currentPlayActivity.setPlayerOneScore(score);

    }
}
