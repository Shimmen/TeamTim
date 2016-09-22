package teamtim.teamtimapp.database;

import android.graphics.Bitmap;
import android.media.Image;

import java.util.ArrayList;
import java.util.List;

public class WordQuestion {
    private final String word;
    private final List<String> categories;
    private final WordDifficulty difficulty;
    private final Bitmap wordImage;

    public WordQuestion(String word, List<String> categories, WordDifficulty difficulty, Bitmap wordImage){
        this.word = word;
        this.categories = categories;
        this.difficulty = difficulty;
        this.wordImage = wordImage;
    }

    public List<String> getCategories() {
        return new ArrayList<>(categories);
    }

    public Bitmap getImageURI() {
        return wordImage;
    }

    public String getWord() {
        return word;
    }

    public WordDifficulty getDifficulty() {
        return difficulty;
    }
}
