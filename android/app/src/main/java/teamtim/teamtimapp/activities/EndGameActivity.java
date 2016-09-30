package teamtim.teamtimapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import teamtim.teamtimapp.R;

public class EndGameActivity extends AppCompatActivity {

    private TextView stats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        stats = (TextView) findViewById(R.id.nbrCorrectText);

        Bundle extras = getIntent().getExtras();
        String correct = extras.getString("CORRECT_ANSWERS");
        String total = extras.getString("TOTAL_ANSWERS");
        stats.setText(correct + " av " + total + " rätt!");
    }

    public void goToMainMenu(View v){
        Intent i = new Intent(this, MainMenuActivity.class);
        startActivity(i);
    }

    public void playAgain(View v){
        Intent i = new Intent(this, CategoryActivity.class);
        startActivity(i);
    }
}
