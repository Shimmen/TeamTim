package teamtim.teamtimapp.database;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class WordQuestionTest {

    @Test
    public void testGetCategories() throws Exception {
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("category1");
        categories.add("category2");
        categories.add("category3");

        WordQuestion question = WordQuestionFactory.create("Test", Prefix.EN, categories, WordDifficulty.EASY, -1);

        assertNotNull(question.getCategories());
        assertEquals(categories.size(), question.getCategories().size());

        // If you remove the intersection the list should be empty
        categories.removeAll(question.getCategories());
        assertTrue(categories.size() == 0);
    }

    @Test
    public void testGetImage() throws Exception {
        int imageId = 12345;
        WordQuestion question = WordQuestionFactory.create("Test", Prefix.EN, "category", WordDifficulty.EASY, imageId);

        assertEquals(imageId, question.getImage());
    }

    @Test
    public void testGetWord() throws Exception {
        String word = "word";
        WordQuestion question = WordQuestionFactory.create(word, Prefix.EN, "category", WordDifficulty.EASY, -1);

        assertNotNull(question.getWord());
        assertEquals(word, question.getWord());
    }

    @Test
    public void testGetDifficulty() throws Exception {
        WordDifficulty difficulty = WordDifficulty.HARD;
        WordQuestion question = WordQuestionFactory.create("Test", Prefix.EN, "category", WordDifficulty.HARD, -1);

        assertNotNull(question.getDifficulty());
        assertEquals(difficulty, question.getDifficulty());
    }

    @Test
    public void testGetPrefix() throws Exception{
        WordQuestion question = WordQuestionFactory.create("Banan", Prefix.EN, "category", WordDifficulty.EASY, -1);
        Prefix prefix = Prefix.EN;
        String pString = "EN";

        assertNotNull(question.getPrefix());
        assertEquals(prefix.toString(), question.getPrefix());
        assertEquals(pString, question.getPrefix());
    }
}