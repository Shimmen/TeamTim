package teamtim.teamtimapp.database;

import java.util.List;
import java.util.Set;

public interface DatabaseInterface {
    public List<WordQuestion> getQuestions(WordDifficulty difficulty, int maxAmount);
    public List<WordQuestion> getQuestions(String category, int maxAmount);
    public List<WordQuestion> getQuestions(WordDifficulty difficulty, String category, int maxAmount);
    public WordQuestion getQuestion(int id);

    public List<CategoryWrapper> getCategories();
}
