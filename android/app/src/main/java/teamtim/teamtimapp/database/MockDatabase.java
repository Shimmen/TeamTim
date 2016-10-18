package teamtim.teamtimapp.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import teamtim.teamtimapp.R;

public class MockDatabase implements DatabaseInterface {

    private static DatabaseInterface instance = null;
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

    public static DatabaseInterface getInstance() {
        if (instance == null){
            instance = new MockDatabase();
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
    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        for (WordQuestion question : wordQuestions) {
            for (String category : question.getCategories()) {
                if (!categories.contains(category))
                    categories.add(category);
            }
        }
        return categories;
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
