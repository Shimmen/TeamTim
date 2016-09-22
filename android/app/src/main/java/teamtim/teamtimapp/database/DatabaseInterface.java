package teamtim.teamtimapp.database;

import java.util.List;

public interface DatabaseInterface {
    public List<WordQuestion> getQuestions(WordDifficulty difficulty, int maxAmount);
    public List<WordQuestion> getQuestions(String category, int maxAmount);
    public List<WordQuestion> getQuestions(WordDifficulty difficulty, String category, int maxAmount);
}
