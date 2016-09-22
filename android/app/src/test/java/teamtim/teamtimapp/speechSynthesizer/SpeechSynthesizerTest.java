package teamtim.teamtimapp.speechSynthesizer;

import android.app.Activity;
import android.content.Context;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Gustav on 16-09-22.
 */
public class SpeechSynthesizerTest {

    @Test
    public void testSpeakOrShutUp() throws Exception {
        SpeechSynthesizer speechSynthesizer = new SpeechSynthesizer(new Activity());
        speechSynthesizer.speak("test123test213");
        boolean startTalking = speechSynthesizer.isSpeaking();
        speechSynthesizer.speakOrShutUp("test123test123");
        boolean shutsUp = !speechSynthesizer.isSpeaking();
        assertTrue(startTalking);
        assertTrue(shutsUp);
    }

    @Test
    public void testSpeak() throws Exception {
        SpeechSynthesizer speechSynthesizer = new SpeechSynthesizer(new Activity());
        speechSynthesizer.speak("test123test213");
        assertTrue(speechSynthesizer.isSpeaking());
    }

    @Test
    public void testShutUp() throws Exception {
        SpeechSynthesizer speechSynthesizer = new SpeechSynthesizer(new Activity());
        speechSynthesizer.speak("test123test213");
        speechSynthesizer.shutUp();
        assertTrue(!speechSynthesizer.isSpeaking());
    }
}