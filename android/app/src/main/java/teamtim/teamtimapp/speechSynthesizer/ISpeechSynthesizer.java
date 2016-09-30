package teamtim.teamtimapp.speechSynthesizer;

import android.content.Context;

import teamtim.teamtimapp.database.WordQuestion;

public interface ISpeechSynthesizer {

    void speak(Context c, WordQuestion wordQuestion);
    void speak(Context c, boolean b);
    void shutUp();

    boolean isSpeaking();
}
