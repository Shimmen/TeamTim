package teamtim.teamtimapp.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

        add(WordQuestionFactory.create("Köttbulle",  Prefix.EN, "Mat",  WordDifficulty.HARD,   R.drawable.kottbulle));
        add(WordQuestionFactory.create("Kebabpizza", Prefix.EN, "Mat",  WordDifficulty.MEDIUM,   R.drawable.kebabpizza));
        add(WordQuestionFactory.create("Ägg",        Prefix.ETT, "Mat",  WordDifficulty.EASY,   R.drawable.agg));
        add(WordQuestionFactory.create("Smörgås",    Prefix.EN, "Mat",  WordDifficulty.MEDIUM,   R.drawable.smorgas));
        add(WordQuestionFactory.create("Äta",        Prefix.ATT, "Mat",  WordDifficulty.EASY,   R.drawable.ata));

        add(WordQuestionFactory.create("Penna",      Prefix.EN, "Skola",  WordDifficulty.EASY,   R.drawable.penna));
        add(WordQuestionFactory.create("Suddigum",   Prefix.ETT, "Skola",  WordDifficulty.HARD,   R.drawable.suddigum));
        add(WordQuestionFactory.create("Bok",        Prefix.EN, "Skola",  WordDifficulty.EASY,   R.drawable.bok));
        add(WordQuestionFactory.create("Klassrum",   Prefix.ETT, "Skola",  WordDifficulty.MEDIUM,   R.drawable.klassrum));
        add(WordQuestionFactory.create("Kollegieblock",Prefix.ETT, "Skola",  WordDifficulty.HARD,   R.drawable.kollegieblock));

        add(WordQuestionFactory.create("Fotboll",    Prefix.EN, "Sport",  WordDifficulty.MEDIUM,   R.drawable.fotboll));
        add(WordQuestionFactory.create("Tennisracket",Prefix.ETT, "Sport",  WordDifficulty.HARD,   R.drawable.tennisracket));
        add(WordQuestionFactory.create("Basketboll", Prefix.EN, "Sport",  WordDifficulty.MEDIUM,   R.drawable.basketboll));
        add(WordQuestionFactory.create("Lag",        Prefix.ETT, "Sport",  WordDifficulty.EASY,   R.drawable.lag));
        add(WordQuestionFactory.create("Jogga",      Prefix.ATT, "Sport",  WordDifficulty.EASY,   R.drawable.jogga));

        add(WordQuestionFactory.create("Kanelbulle", Prefix.EN, "Fika",  WordDifficulty.HARD,   R.drawable.kanelbulle));
        add(WordQuestionFactory.create("Fika",       Prefix.ATT, "Fika",  WordDifficulty.EASY,   R.drawable.fika));
        add(WordQuestionFactory.create("Kaffe",      Prefix.EN, "Fika",  WordDifficulty.MEDIUM,   R.drawable.kaffe));
        add(WordQuestionFactory.create("Läsk",       Prefix.EN, "Fika",  WordDifficulty.EASY,   R.drawable.lask));
        add(WordQuestionFactory.create("Prata",      Prefix.ATT, "Fika",  WordDifficulty.EASY,   R.drawable.prata));

        add(WordQuestionFactory.create("Kompis",      Prefix.EN, "Vänner",  WordDifficulty.HARD,   R.drawable.kompis));
        add(WordQuestionFactory.create("Leka",        Prefix.ATT, "Vänner",  WordDifficulty.EASY,   R.drawable.leka));
        add(WordQuestionFactory.create("Spel",        Prefix.ETT, "Vänner",  WordDifficulty.HARD,   R.drawable.spel));
        add(WordQuestionFactory.create("Skratta",     Prefix.ATT, "Vänner",  WordDifficulty.HARD,   R.drawable.skratta));
        add(WordQuestionFactory.create("Vän",         Prefix.EN, "Vänner",  WordDifficulty.HARD,   R.drawable.kompis));

    }

    // Helper for adding questions and assigning its id
    private void add(WordQuestion wordQuestion) {
        wordQuestion.setQuestionId(wordQuestions.size());
        wordQuestions.add(wordQuestion);
    }

    public static void initialize(Context context) throws Exception{
        if (instance != null) throw new Exception("Database already initialized!");
        instance = new MockDatabase();
        preferences = context.getSharedPreferences("categories", Context.MODE_PRIVATE);
        //PreferenceManager.getDefaultSharedPreferences(context);

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
                    float ratio = preferences.getFloat(category, 0.5f);
                    categories.add(new CategoryWrapper(category, question.getImage(), ratio));
                }
            }
        }
        System.out.println(preferences.getAll());
        return categories;
    }

    public void updateCategorySuccessRatio(String category, int points, int total_points){
        float ratio = preferences.getFloat(category.toLowerCase(), 0.5f);
        ratio += (float)((float)points/(float)total_points);
        ratio/=2;
        preferences.edit().putFloat(category.toLowerCase(), ratio).apply();
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
