package teamtim.teamtimapp.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

public final class WordQuestionFactory {
    private WordQuestionFactory(){
        //nothing...
    }

    public static WordQuestion create(String word, Prefix prefix, List<String> categories, WordDifficulty difficulty, int imageId){
        /*BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = false;
        //can set bounderies later...
        Bitmap image = BitmapFactory.decodeFile(imageURI, options);*/
        return new WordQuestion(word, prefix, categories, difficulty, imageId);
    }

    public static WordQuestion create(String word, Prefix prefix, String category, WordDifficulty difficulty, int imageId){
        List<String> array = new ArrayList<>();
        array.add(category);
        return create(word, prefix, array, difficulty, imageId);
    }
}