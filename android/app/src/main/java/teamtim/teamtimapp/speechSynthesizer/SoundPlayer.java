package teamtim.teamtimapp.speechSynthesizer;

import android.media.MediaPlayer;
import java.io.IOException;
import teamtim.teamtimapp.database.WordQuestion;

public class SoundPlayer implements ISpeechSynthesizer{
    private MediaPlayer mediaPlayer;

    public SoundPlayer(){
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void speak(WordQuestion wordQuestion) {
        String path = "/main/res/soundFiles/";
        path += wordQuestion.getWord() + ".wav";
        try{
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch(IOException e){}
    }

    @Override
    public void shutUp() {
        mediaPlayer.stop();
        mediaPlayer.release();

    }

    @Override
    public boolean isSpeaking() {
        return mediaPlayer.isPlaying();
    }
}
