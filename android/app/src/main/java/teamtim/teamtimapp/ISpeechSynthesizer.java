package teamtim.teamtimapp;

import android.speech.tts.TextToSpeech;
import java.util.Locale;

/**
 * Created by Gustav on 16-09-20.
 */
public interface ISpeechSynthesizer {

    void speak(String word);

    void shutUp();
}
