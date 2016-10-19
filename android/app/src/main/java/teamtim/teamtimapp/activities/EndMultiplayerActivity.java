package teamtim.teamtimapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import teamtim.teamtimapp.R;
import teamtim.teamtimapp.managers.GameData;

public class EndMultiplayerActivity extends AppCompatActivity {

    private TextView winnerText;
    private TextView yourScore;
    private TextView theirScore;

    private GameData gameData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("In EndGame");
        setContentView(R.layout.activity_end_multiplayer);
        gameData = (GameData)getIntent().getSerializableExtra("DATA");

        if (getActionBar() != null) {
            getActionBar().hide();
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        winnerText = (TextView) findViewById(R.id.winnerText);
        yourScore = (TextView) findViewById(R.id.your_score);
        theirScore = (TextView) findViewById(R.id.their_score);

        setWinner();
        setSore();
    }

    private void setWinner(){
        if(gameData.getP1Score() > gameData.getP2Score()) {
            winnerText.setText("DU VANN!");
            winnerText.setTextColor(Color.GREEN);
        } else if(gameData.getP1Score() == gameData.getP2Score()) {
            winnerText.setText("OAVGJORT");
            winnerText.setTextColor(Color.BLACK);
        } else {
            winnerText.setText("DU FÖRLORADE");
            winnerText.setTextColor(Color.RED);
        }
    }

    private void setSore(){
        yourScore.setText(gameData.getP1Score()+"");
        theirScore.setText(gameData.getP2Score()+"");
    }

    public void playAgain(View v) {
        Intent i = new Intent(this, CategoryActivity.class);
        startActivity(i);
    }

    public void showResult(View v) {
        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra("DATA", gameData);
        startActivity(i);
    }

    public void endGame(View v) {
        Intent i = new Intent(this, MainMenuActivity.class);
        startActivity(i);
    }
}
