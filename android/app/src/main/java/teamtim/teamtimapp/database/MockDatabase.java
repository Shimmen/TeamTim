package teamtim.teamtimapp.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import teamtim.teamtimapp.R;

public class MockDatabase implements DatabaseInterface {
    private List<WordQuestion> wordQuestions;
    private Random randomizer = new Random();

    public MockDatabase(){
        wordQuestions = new ArrayList<WordQuestion>();
        wordQuestions.add(WordQuestionFactory.create("Apa", "Djur", WordDifficulty.EASY, R.drawable.giraff));
        wordQuestions.add(WordQuestionFactory.create("Giraff", "Djur", WordDifficulty.HARD, R.drawable.giraff));
        wordQuestions.add(WordQuestionFactory.create("Orm", "Djur", WordDifficulty.EASY, R.drawable.giraff));
        wordQuestions.add(WordQuestionFactory.create("Häst", "Djur", WordDifficulty.MEDIUM, R.drawable.giraff));
        wordQuestions.add(WordQuestionFactory.create("Ko", "Djur", WordDifficulty.EASY, R.drawable.giraff));
        wordQuestions.add(WordQuestionFactory.create("Banan", "Frukt", WordDifficulty.EASY, R.drawable.giraff));
        wordQuestions.add(WordQuestionFactory.create("Äpple", "Frukt", WordDifficulty.MEDIUM, R.drawable.giraff));
        wordQuestions.add(WordQuestionFactory.create("Päron", "Frukt", WordDifficulty.MEDIUM, R.drawable.giraff));
        wordQuestions.add(WordQuestionFactory.create("Apelsin", "Frukt", WordDifficulty.MEDIUM, R.drawable.giraff));
        wordQuestions.add(WordQuestionFactory.create("Grapefrukt", "Frukt", WordDifficulty.HARD, R.drawable.giraff));
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
            if (question.getCategories().contains(category))
                questions.add(question);
        }
        filter(questions, maxAmount);
        return questions;
    }

    @Override
    public List<WordQuestion> getQuestions(WordDifficulty difficulty, String category, int maxAmount) {
        List<WordQuestion> questions = new ArrayList<>();
        for (WordQuestion question : wordQuestions){
            if (question.getDifficulty() == difficulty && question.getCategories().contains(category))
                questions.add(question);
        }
        filter(questions, maxAmount);
        return questions;
    }

    /**
     * Removes elements from the list until preferred size is reached.
     * @param query the list with questions
     * @param maxAmount the preferred size of the list
     */
    public void filter(List<WordQuestion> query, int maxAmount) {
        if (maxAmount == -1) return;
        while (query.size() > maxAmount ){
            int index = randomizer.nextInt(query.size());
            query.remove(index);
        }
    }
}
