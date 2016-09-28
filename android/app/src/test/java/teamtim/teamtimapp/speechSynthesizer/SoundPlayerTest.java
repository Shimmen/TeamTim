package teamtim.teamtimapp.speechSynthesizer;

import android.app.Activity;

import org.junit.Test;

import teamtim.teamtimapp.database.WordQuestion;

import static org.junit.Assert.*;

/**
 * Created by Gustav on 16-09-22.
 */
public class SoundPlayerTest {

    @Test
    public void testSpeak() throws Exception {
        SoundPlayer soundPlayer = new SoundPlayer();
        WordQuestion wq = new WordQuestion("apa", null, null, 0);
        soundPlayer.speak(wq);
        assertTrue(soundPlayer.isSpeaking());
    }

    @Test
    public void testShutUp() throws Exception {
        SoundPlayer soundPlayer = new SoundPlayer();
        WordQuestion wq = new WordQuestion("apa", null, null, 0);
        soundPlayer.speak(wq);
        soundPlayer.shutUp();
        assertTrue(!soundPlayer.isSpeaking());
    }
}