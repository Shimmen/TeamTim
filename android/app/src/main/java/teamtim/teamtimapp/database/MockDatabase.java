package teamtim.teamtimapp.database;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import teamtim.teamtimapp.R;

public class MockDatabase implements DatabaseInterface {

    private static DatabaseInterface instance = null;
    private static SharedPreferences preferences;
    private List<WordQuestion> wordQuestions = new ArrayList<>();
    private Random randomizer = new Random();

    private MockDatabase(){
        List<String> grapeFruitCategoryTest = new ArrayList<>();
        grapeFruitCategoryTest.add("Frukt");
        grapeFruitCategoryTest.add("Röd frukt");
        grapeFruitCategoryTest.add("Potentiellt mordvapen");
        grapeFruitCategoryTest.add("Frukt som inte är banan");
        grapeFruitCategoryTest.add("Saker som är röda på insidan men orangea utanpå");
        grapeFruitCategoryTest.add("Runda saker");
        grapeFruitCategoryTest.add("Objekt med en diameter på cirka en dm");
        grapeFruitCategoryTest.add("Undercover apelsin");
        grapeFruitCategoryTest.add("Saker som har flera kategorier");

        wordQuestions = new ArrayList<WordQuestion>();
        add(WordQuestionFactory.create("Apa",        Prefix.EN, "Djur",  WordDifficulty.EASY,   R.drawable.apa));
        add(WordQuestionFactory.create("Giraff",     Prefix.EN, "Djur",  WordDifficulty.HARD,   R.drawable.giraff));
        add(WordQuestionFactory.create("Orm",        Prefix.EN, "Djur",  WordDifficulty.EASY,   R.drawable.orm));
        add(WordQuestionFactory.create("Häst",       Prefix.EN, "Djur",  WordDifficulty.MEDIUM, R.drawable.horse));
        add(WordQuestionFactory.create("Ko",         Prefix.EN, "Djur",  WordDifficulty.EASY,   R.drawable.ko));
        add(WordQuestionFactory.create("Banan",      Prefix.EN, "Frukt", WordDifficulty.EASY,   R.drawable.banan));
        add(WordQuestionFactory.create("Äpple",      Prefix.ETT, "Frukt", WordDifficulty.MEDIUM, R.drawable.apple));
        add(WordQuestionFactory.create("Päron",      Prefix.ETT, "Frukt", WordDifficulty.MEDIUM, R.drawable.pear));
        add(WordQuestionFactory.create("Apelsin",    Prefix.EN, "Frukt", WordDifficulty.MEDIUM, R.drawable.apelsin));
        add(WordQuestionFactory.create("Grapefrukt", Prefix.EN, grapeFruitCategoryTest, WordDifficulty.HARD,   R.drawable.grapefrukt));
    }

    // Helper for adding questions and assigning its id
    private void add(WordQuestion wordQuestion) {
        wordQuestion.setQuestionId(wordQuestions.size());
        wordQuestions.add(wordQuestion);
    }

    public static void initialize(Context context) throws Exception{
        if (instance != null) throw new Exception("Database already initialized!");
        instance = new MockDatabase();
        preferences = context.getSharedPreferences("CategoryData", Context.MODE_PRIVATE);
    }

    public static DatabaseInterface getInstance() throws NullPointerException {
        if (instance == null){
            throw new NullPointerException("Database not initialized!");
        }
        return instance;
    }

    @Override
    public List<WordQuestion> getQuestions(WordDifficulty difficulty, int maxAmount) {
        List<WordQuestion> questions = new ArrayList<>();
        for (WordQuestion question : wordQuestions){
            if (question.getDifficulty() == difficulty)
                questions.add(question);
        }
        filter(questions, maxAmount);
        return questions;
    }

    @Override
    public List<WordQuestion> getQuestions(String category, int maxAmount) {
        List<WordQuestion> questions = new ArrayList<>();
        for (WordQuestion question : wordQuestions){
            if (question.getCategories().contains(category.toLowerCase())) {
                questions.add(question);
            }
        }
        filter(questions, maxAmount);
        return questions;
    }

    @Override
    public List<WordQuestion> getQuestions(WordDifficulty difficulty, String category, int maxAmount) {
        List<WordQuestion> questions = new ArrayList<>();
        for (WordQuestion question : wordQuestions){
            if (question.getDifficulty() == difficulty && question.getCategories().contains(category.toLowerCase())) {
                questions.add(question);
            }
        }
        filter(questions, maxAmount);
        return questions;
    }

    @Override
    public WordQuestion getQuestion(int id) {
        return wordQuestions.get(id);
    }

    @Override
    public List<CategoryWrapper> getCategories() {
        List<CategoryWrapper> categories = new ArrayList<>();
        //TODO Optimize this, its really slow
        for (WordQuestion question : wordQuestions) {
            for (String category : question.getCategories()) {
                boolean alreadyAppended = false;
                for (CategoryWrapper cw : categories) {
                    if (cw.getCategory().equals(category))
                        alreadyAppended = true;
                }
                if (!alreadyAppended) {
                    float ratio = preferences.getFloat(category, 0f);
                    categories.add(new CategoryWrapper(category, question.getImage(), ratio));
                }
            }
        }
        return categories;
    }

    public void updateCategorySuccessRatio(String category, int points, int total_points){
        float ratio = preferences.getFloat(category, 0f);
        ratio += points/total_points;
        ratio/=2;
        //NOT TESTED
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(category, ratio);
        editor.commit();
    }

    /**
     * Removes elements from the list until preferred size is reached.
     * @param query the list with questions
     * @param maxAmount the preferred size of the list
     */
    private void filter(List<WordQuestion> query, int maxAmount) {
        if (maxAmount == -1) return;
        while (query.size() > maxAmount ){
            int index = randomizer.nextInt(query.size());
            query.remove(index);
        }
    }
}
