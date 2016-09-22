package teamtim.teamtimapp.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MockDatabase implements DatabaseInterface {
    private List<WordQuestion> wordQuestions;
    private Random randomizer = new Random();

    public MockDatabase(){
        wordQuestions = new ArrayList<WordQuestion>();
        wordQuestions.add(WordQuestionFactory.create("Apa", "Djur", WordDifficulty.EASY, "https://www.google.se/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiszLr21Z3PAhUDDiwKHQhLBB4QjRwIBw&url=http%3A%2F%2Fdayviews.com%2Fstrawberrywhite%2F488194605%2F&psig=AFQjCNF9E4tBdsLZUT_kn7HlFjQF9lf-Dw&ust=1474451474413758"));
        wordQuestions.add(WordQuestionFactory.create("Giraff", "Djur", WordDifficulty.HARD, "https://www.google.se/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiszLr21Z3PAhUDDiwKHQhLBB4QjRwIBw&url=http%3A%2F%2Fdayviews.com%2Fstrawberrywhite%2F488194605%2F&psig=AFQjCNF9E4tBdsLZUT_kn7HlFjQF9lf-Dw&ust=1474451474413758"));
        wordQuestions.add(WordQuestionFactory.create("Orm", "Djur", WordDifficulty.EASY, "https://www.google.se/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiszLr21Z3PAhUDDiwKHQhLBB4QjRwIBw&url=http%3A%2F%2Fdayviews.com%2Fstrawberrywhite%2F488194605%2F&psig=AFQjCNF9E4tBdsLZUT_kn7HlFjQF9lf-Dw&ust=1474451474413758"));
        wordQuestions.add(WordQuestionFactory.create("Häst", "Djur", WordDifficulty.MEDIUM, "https://www.google.se/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiszLr21Z3PAhUDDiwKHQhLBB4QjRwIBw&url=http%3A%2F%2Fdayviews.com%2Fstrawberrywhite%2F488194605%2F&psig=AFQjCNF9E4tBdsLZUT_kn7HlFjQF9lf-Dw&ust=1474451474413758"));
        wordQuestions.add(WordQuestionFactory.create("Ko", "Djur", WordDifficulty.EASY, "https://www.google.se/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiszLr21Z3PAhUDDiwKHQhLBB4QjRwIBw&url=http%3A%2F%2Fdayviews.com%2Fstrawberrywhite%2F488194605%2F&psig=AFQjCNF9E4tBdsLZUT_kn7HlFjQF9lf-Dw&ust=1474451474413758"));
        wordQuestions.add(WordQuestionFactory.create("Banan", "Frukt", WordDifficulty.EASY, "https://www.google.se/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiszLr21Z3PAhUDDiwKHQhLBB4QjRwIBw&url=http%3A%2F%2Fdayviews.com%2Fstrawberrywhite%2F488194605%2F&psig=AFQjCNF9E4tBdsLZUT_kn7HlFjQF9lf-Dw&ust=1474451474413758"));
        wordQuestions.add(WordQuestionFactory.create("Äpple", "Frukt", WordDifficulty.MEDIUM, "https://www.google.se/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiszLr21Z3PAhUDDiwKHQhLBB4QjRwIBw&url=http%3A%2F%2Fdayviews.com%2Fstrawberrywhite%2F488194605%2F&psig=AFQjCNF9E4tBdsLZUT_kn7HlFjQF9lf-Dw&ust=1474451474413758"));
        wordQuestions.add(WordQuestionFactory.create("Päron", "Frukt", WordDifficulty.MEDIUM, "https://www.google.se/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiszLr21Z3PAhUDDiwKHQhLBB4QjRwIBw&url=http%3A%2F%2Fdayviews.com%2Fstrawberrywhite%2F488194605%2F&psig=AFQjCNF9E4tBdsLZUT_kn7HlFjQF9lf-Dw&ust=1474451474413758"));
        wordQuestions.add(WordQuestionFactory.create("Apelsin", "Frukt", WordDifficulty.MEDIUM, "https://www.google.se/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiszLr21Z3PAhUDDiwKHQhLBB4QjRwIBw&url=http%3A%2F%2Fdayviews.com%2Fstrawberrywhite%2F488194605%2F&psig=AFQjCNF9E4tBdsLZUT_kn7HlFjQF9lf-Dw&ust=1474451474413758"));
        wordQuestions.add(WordQuestionFactory.create("Grapefrukt", "Frukt", WordDifficulty.HARD, "https://www.google.se/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiszLr21Z3PAhUDDiwKHQhLBB4QjRwIBw&url=http%3A%2F%2Fdayviews.com%2Fstrawberrywhite%2F488194605%2F&psig=AFQjCNF9E4tBdsLZUT_kn7HlFjQF9lf-Dw&ust=1474451474413758"));
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
