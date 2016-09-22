package teamtim.teamtimapp.speechSynthesizer;

public interface ISpeechSynthesizer {

    void speakOrShutUp(String word);

    void speak(String word);

    void shutUp();
}
