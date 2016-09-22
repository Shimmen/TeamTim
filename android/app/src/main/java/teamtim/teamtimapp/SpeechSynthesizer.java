package teamtim.teamtimapp;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class SpeechSynthesizer implements ISpeechSynthesizer{
    private TextToSpeech tts;

    public SpeechSynthesizer(Context context){
        tts=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
        @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.GERMAN);
                tts.setSpeechRate(0.5f);
                //tts.speak("Svenska", TextToSpeech.QUEUE_FLUSH, null);
                //System.out.println("OnInit");
            }
        }
    );}



    @Override
    public void speak(String word) {
        tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void shutUp() {
        if(tts.isSpeaking()) tts.stop();
    }
}
