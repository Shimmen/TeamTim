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
        grapeFruitCategoryTest.add("Fotbollssubstitut");
        grapeFruitCategoryTest.add("Frukter som inte växer på Grönland");
        grapeFruitCategoryTest.add("Frukter som inte är bär");
        add(WordQuestionFactory.create("Apa",        "Djur",  WordDifficulty.EASY,   R.drawable.apa));
        add(WordQuestionFactory.create("Giraff",     "Djur",  WordDifficulty.HARD,   R.drawable.giraff));
        add(WordQuestionFactory.create("Orm",        "Djur",  WordDifficulty.EASY,   R.drawable.orm));
        add(WordQuestionFactory.create("Häst",       "Djur",  WordDifficulty.MEDIUM, R.drawable.horse));
        add(WordQuestionFactory.create("Ko",         "Djur",  WordDifficulty.EASY,   R.drawable.ko));
        add(WordQuestionFactory.create("Banan",      "Frukt", WordDifficulty.EASY,   R.drawable.banan));
        add(WordQuestionFactory.create("Äpple",      "Frukt", WordDifficulty.MEDIUM, R.drawable.apple));
        add(WordQuestionFactory.create("Päron",      "Frukt", WordDifficulty.MEDIUM, R.drawable.pear));
        add(WordQuestionFactory.create("Apelsin",    "Frukt", WordDifficulty.MEDIUM, R.drawable.apelsin));
        add(WordQuestionFactory.create("Grapefrukt", grapeFruitCategoryTest, WordDifficulty.HARD,   R.drawable.grapefrukt));

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
