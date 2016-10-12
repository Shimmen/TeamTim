package teamtim.teamtimapp.database;

import org.hamcrest.core.StringContains;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WordQuestionFactoryTest {

    @Test
    public void testNeverNull() throws Exception {
        WordQuestion question = WordQuestionFactory.create("Test", Prefix.EN, "category", WordDifficulty.EASY, -1);
        assertNotNull(question);
    }
}