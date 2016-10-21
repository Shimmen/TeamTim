package teamtim.teamtimapp.database;


import java.util.ArrayList;
import java.util.List;

public final class WordQuestionFactory {
    private WordQuestionFactory(){
        //nothing
    }

    public static WordQuestion create(String word, Prefix prefix, List<String> categories, WordDifficulty difficulty, int imageId){
       return new WordQuestion(word, prefix, categories, difficulty, imageId);
    }

    public static WordQuestion create(String word, Prefix prefix, String category, WordDifficulty difficulty, int imageId){
        List<String> array = new ArrayList<>();
        array.add(category);
        return create(word, prefix, array, difficulty, imageId);
    }
}