package teamtim.teamtimapp.speechSynthesizer;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class SpeechSynthesizer implements ISpeechSynthesizer{
    private TextToSpeech tts;

    public SpeechSynthesizer(Context context){
        tts=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
        @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.GERMANY); //Sweden not available, german sound almomst like swedish
                tts.setSpeechRate(0.55f);
                speakOrShutUp("Vet ni att tyska låter nästan som svenska."); //remove after merge
            }
        }
    );}

    public void speakOrShutUp(String word){
        if(tts==null) return;

        if(!tts.isSpeaking()) speak(word);
        else shutUp();
    }

    @Override
    public void speak(String word) {
        String copy = word.replace('v','w');    //remove if Local is Sweden
        copy = copy.replace('V','W');           //remove if Local is Sweden

        tts.speak(copy, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void shutUp() {
        tts.stop();
    }

    @Override
    public boolean isSpeaking() {
        return tts.isSpeaking();
    }
}
