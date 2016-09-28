package teamtim.teamtimapp.speechSynthesizer;

import teamtim.teamtimapp.database.WordQuestion;

public interface ISpeechSynthesizer {

    void speak(WordQuestion wordQuestion);

    void shutUp();

    boolean isSpeaking();
}
