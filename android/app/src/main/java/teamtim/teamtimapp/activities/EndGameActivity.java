package teamtim.teamtimapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import teamtim.teamtimapp.R;

public class EndGameActivity extends AppCompatActivity {

    private TextView stats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        stats = (TextView) findViewById(R.id.nbrCorrectText);

        Intent intent = getIntent();
        String text = intent.getStringExtra("CORRECT_ANSWERS");
        stats.setText(text);
    }
}
