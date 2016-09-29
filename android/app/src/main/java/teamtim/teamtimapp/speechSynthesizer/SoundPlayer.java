package teamtim.teamtimapp.speechSynthesizer;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import java.io.IOException;
import teamtim.teamtimapp.database.WordQuestion;

public class SoundPlayer implements ISpeechSynthesizer{
    private MediaPlayer mediaPlayer;

    public SoundPlayer(){
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void speak(Context c, WordQuestion wordQuestion) {
        String path = "soundFiles/";
        path += wordQuestion.getWord() + ".wav";
        shutUp();
        try{
            AssetFileDescriptor descriptor = c.getAssets().openFd(path);
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            }
            descriptor.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void shutUp() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    @Override
    public boolean isSpeaking() {
        return mediaPlayer.isPlaying();
    }
}
