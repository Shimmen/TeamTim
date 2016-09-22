package teamtim.teamtimapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private ISpeechSynthesizer voice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        voice = new SpeechSynthesizer(getApplicationContext());
    }
    protected void onStart() {
        super.onStart();
        voice.speak("Wälkommen. Tack för att du anwänder appen.");
    }
}
