package teamtim.teamtimapp;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by Gustav on 16-09-20.
 */
public class SpeechSynthesizer implements ISpeechSynthesizer{
    private TextToSpeech tts;

    public SpeechSynthesizer(Context context){
        tts=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
        @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.US);
                tts.speak("Sweden", TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    );}



    @Override
    public void speak(String word) {
        System.out.println("hit");
        tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void shutUp() {
        if(tts.isSpeaking()) tts.stop();
    }
}
