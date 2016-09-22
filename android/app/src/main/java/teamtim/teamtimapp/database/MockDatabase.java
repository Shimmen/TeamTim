package teamtim.teamtimapp.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 2016-09-20.
 */
public class MockDatabase implements DatabaseInterface {
    private List<WordQuestion> wordQuestions;

    public MockDatabase(){
        wordQuestions = new ArrayList<WordQuestion>();
        wordQuestions.add(WordQuestionFactory.create("Apa", "Djur", WordDifficulty.EASY, "https://www.google.se/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiszLr21Z3PAhUDDiwKHQhLBB4QjRwIBw&url=http%3A%2F%2Fdayviews.com%2Fstrawberrywhite%2F488194605%2F&psig=AFQjCNF9E4tBdsLZUT_kn7HlFjQF9lf-Dw&ust=1474451474413758"));
        wordQuestions.add(WordQuestionFactory.create("Giraff", "Djur", WordDifficulty.HARD, "https://www.google.se/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiszLr21Z3PAhUDDiwKHQhLBB4QjRwIBw&url=http%3A%2F%2Fdayviews.com%2Fstrawberrywhite%2F488194605%2F&psig=AFQjCNF9E4tBdsLZUT_kn7HlFjQF9lf-Dw&ust=1474451474413758"));
        wordQuestions.add(WordQuestionFactory.create("Orm", "Djur", WordDifficulty.EASY, "https://www.google.se/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiszLr21Z3PAhUDDiwKHQhLBB4QjRwIBw&url=http%3A%2F%2Fdayviews.com%2Fstrawberrywhite%2F488194605%2F&psig=AFQjCNF9E4tBdsLZUT_kn7HlFjQF9lf-Dw&ust=1474451474413758"));
        wordQuestions.add(WordQuestionFactory.create("Häst", "Djur", WordDifficulty.MEDIUM, "https://www.google.se/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiszLr21Z3PAhUDDiwKHQhLBB4QjRwIBw&url=http%3A%2F%2Fdayviews.com%2Fstrawberrywhite%2F488194605%2F&psig=AFQjCNF9E4tBdsLZUT_kn7HlFjQF9lf-Dw&ust=1474451474413758"));
        wordQuestions.add(WordQuestionFactory.create("Ko", "Djur", WordDifficulty.EASY, "https://www.google.se/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=0ahUKEwiszLr21Z3PAhUDDiwKHQhLBB4QjRwIBw&url=http%3A%2F%2Fdayviews.com%2Fstrawberrywhite%2F488194605%2F&psig=AFQjCNF9E4tBdsLZUT_kn7HlFjQF9lf-Dw&ust=1474451474413758"));
    }

    @Override
    public List<WordQuestion> getQuestions(WordDifficulty difficulty, int maxAmount) {
        List<WordQuestion> questions = new ArrayList<>();
        for (WordQuestion question : wordQuestions){
            if (question.getDifficulty() == difficulty)
                questions.add(question);
        }
        return questions;
    }

    @Override
    public List<WordQuestion> getQuestions(String category, int maxAmount) {
        List<WordQuestion> questions = new ArrayList<>();
        for (WordQuestion question : wordQuestions){
            if (question.getCategories().contains(category))
                questions.add(question);
        }
        return questions;
    }

    @Override
    public List<WordQuestion> getQuestions(WordDifficulty difficulty, String category, int maxAmount) {
        List<WordQuestion> questions = new ArrayList<>();
        for (WordQuestion question : wordQuestions){
            if (question.getDifficulty() == difficulty && question.getCategories().contains(category))
                questions.add(question);
        }
        return questions;
    }
}
