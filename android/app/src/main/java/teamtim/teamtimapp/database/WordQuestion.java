package teamtim.teamtimapp.database;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class WordQuestion implements Serializable {

    private final String word;
    private final Prefix prefix;
    private final Set<String> categories;
    private final WordDifficulty difficulty;
    private final int imageId;
    static final long serialVersionUID = 2;

    private int questionId;


    public WordQuestion(String word, Prefix prefix, List<String> categories, WordDifficulty difficulty, int imageId){


        assert word != null;
        this.word = word.toLowerCase(new Locale("swe"));

        assert prefix != null;
        this.prefix = prefix;

        assert categories != null;
        this.categories = new HashSet<String>();
        for (String category: categories) {
            this.categories.add(category.toLowerCase(new Locale("swe")));
        }

        assert difficulty != null;
        this.difficulty = difficulty;

        this.imageId = imageId;
    }

    public String getPrefix(){
        return prefix.toString();
    }

    public Set<String> getCategories() {
        return new HashSet<>(categories);
    }

    public int getImage() {
        return imageId;
    }

    public String getWord() {
        return word;
    }

    public WordDifficulty getDifficulty() {
        return difficulty;
    }

    @Override
    public String toString() {
        return "word=" + word + ", categories=" + categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WordQuestion question = (WordQuestion) o;

        if (imageId != question.imageId) return false;
        if (!word.equals(question.word)) return false;
        if (!categories.equals(question.categories)) return false;
        return difficulty == question.difficulty;

    }

    @Override
    public int hashCode() {
        int result = word.hashCode();
        result = 31 * result + categories.hashCode();
        result = 31 * result + difficulty.hashCode();
        result = 31 * result + imageId;
        return result;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getQuestionId() {
        return questionId;
    }
}
