package teamtim.teamtimapp.database;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MockDatabaseTest {
    public MockDatabaseTest() {
        try {
            MockDatabase.initialize(null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetQuestionsNeverNull() throws Exception {
        DatabaseInterface db = MockDatabase.getInstance();

        String category = "Nonexisting category name. If nothing is found, it should return an empty list!";

        List<WordQuestion> questions = db.getQuestions(category, 999999);

        assertNotNull(questions);
        assertTrue(questions.isEmpty());
    }

    @Test
    public void testGetQuestionsCorrectCategory() throws Exception {
        DatabaseInterface db = MockDatabase.getInstance();

        String category = "frukt";

        List<WordQuestion> questions = db.getQuestions(category, 999999);
        assertTrue("Required for testing purposes!", questions.size() > 0);

        for (WordQuestion question: questions) {
            assertTrue(question.getCategories().contains(category));
        }
    }

    @Test
    public void testGetQuestionsCorrectDifficulty() throws Exception {
        DatabaseInterface db = MockDatabase.getInstance();

        WordDifficulty difficulty = WordDifficulty.EASY;

        List<WordQuestion> questions = db.getQuestions(difficulty, 999999);
        assertTrue("Required for testing purposes!", questions.size() > 0);

        for (WordQuestion question: questions) {
            assertTrue(question.getDifficulty() == difficulty);
        }
    }

    @Test
    public void testGetQuestionsCorrectCategoryAndDifficulty() throws Exception {
        DatabaseInterface db = MockDatabase.getInstance();

        String category = "frukt";
        WordDifficulty difficulty = WordDifficulty.EASY;

        List<WordQuestion> questions = db.getQuestions(difficulty, category, 999999);
        assertTrue("Required for testing purposes!", questions.size() > 0);

        for (WordQuestion question: questions) {
            assertTrue(question.getCategories().contains(category));
            assertTrue(question.getDifficulty() == difficulty);
        }
    }

    @Test
    public void testGetQuestionsCorrectMaxAmount() throws Exception {
        DatabaseInterface db = MockDatabase.getInstance();

        int maxAmount = 2;
        List<WordQuestion> questions = db.getQuestions("Djur", maxAmount);
        assertTrue(questions.size() >= 1);
        assertTrue(questions.size() <= maxAmount);

        maxAmount = 20_000_000;
        questions = db.getQuestions("Djur", maxAmount);
        assertTrue(questions.size() >= 1);
        assertTrue(questions.size() <= maxAmount);
    }

    @Test
    public void testGetQuestionsCaseInsensitiveCategory() throws Exception {
        DatabaseInterface db = MockDatabase.getInstance();

        String category1 = "Frukt";
        String category2 = "fRuKt";

        List<WordQuestion> questions1 = db.getQuestions(category1, 999999);
        List<WordQuestion> questions2 = db.getQuestions(category2, 999999);

        // They should be identical in content (and not both empty, for testing purposes)
        assertTrue(questions1.size() > 0);
        assertTrue(questions2.size() > 0);
        assertEquals(questions1, questions2);

        // Also test the other method that takes in a category!
        questions1 = db.getQuestions(WordDifficulty.EASY, category1, 999999);
        questions2 = db.getQuestions(WordDifficulty.EASY, category2, 999999);

        // They should be identical in content (and not both empty, for testing purposes)
        assertTrue(questions1.size() > 0);
        assertTrue(questions2.size() > 0);
        assertEquals(questions1, questions2);
    }

}