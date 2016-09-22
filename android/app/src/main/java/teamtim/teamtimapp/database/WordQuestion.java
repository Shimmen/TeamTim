package teamtim.teamtimapp.database;

import android.graphics.Bitmap;
import android.media.Image;

import java.util.ArrayList;
import java.util.List;

public class WordQuestion {
    private final String word;
    private final List<String> categories;
    private final WordDifficulty difficulty;
    private final int imageId;

    public WordQuestion(String word, List<String> categories, WordDifficulty difficulty, int imageId){
        this.word = word;
        this.categories = categories;
        this.difficulty = difficulty;
        this.imageId = imageId;
    }

    public List<String> getCategories() {
        return new ArrayList<>(categories);
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
}
