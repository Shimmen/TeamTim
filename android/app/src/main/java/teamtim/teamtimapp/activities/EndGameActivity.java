package teamtim.teamtimapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import teamtim.teamtimapp.R;
import teamtim.teamtimapp.database.MockDatabase;

public class EndGameActivity extends AppCompatActivity {

    private TextView stats;
    private String[] questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        stats = (TextView) findViewById(R.id.nbrCorrectText);

        Bundle extras = getIntent().getExtras();
        questions = extras.getStringArray("QUESTIONS");
        String correct = extras.getString("CORRECT_ANSWERS");
        String total = extras.getString("TOTAL_ANSWERS");
        stats.setText(correct + " av " + total + " rätt!");
        getSupportActionBar().hide();
    }

    public void goToMainMenu(View v){
        Intent i = new Intent(this, MainMenuActivity.class);
        startActivity(i);
    }

    public void playAgain(View v){
        Intent i = new Intent(this, CategoryActivity.class);
        i.putExtra("MODE", "Single");
        startActivity(i);
    }

    public void showResult(View v){
        Intent i = new Intent(this, ResultActivity.class);
        Bundle extras = new Bundle();
        extras.putStringArray("QUESTIONS", questions);
        i.putExtras(extras);
        startActivity(i);
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Avsluta spel")
                .setMessage("Vill du avsluta pågående spel?")
                .setPositiveButton("Ja", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent main = new Intent(EndGameActivity.this, MainMenuActivity.class);
                        startActivity(main);
                    }

                })
                .setNegativeButton("Nej", null)
                .show();
    }
}
