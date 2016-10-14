package teamtim.teamtimapp.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        setContentView(R.layout.activity_end_multiplayer);
        gameData = (GameData)getIntent().getSerializableExtra("DATA");

        winnerText = (TextView) findViewById(R.id.winnerText);
        yourScore = (TextView) findViewById(R.id.your_score);
        theirScore = (TextView) findViewById(R.id.their_score);

        setWinner();
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

    }


}
