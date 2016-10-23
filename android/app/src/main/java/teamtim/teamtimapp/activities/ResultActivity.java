package teamtim.teamtimapp.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import teamtim.teamtimapp.R;
import teamtim.teamtimapp.managers.GameData;

public class ResultActivity extends AppCompatActivity {

    private LinearLayout list;
    private GameData gameData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        if (getActionBar() != null) {
            getActionBar().hide();
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        gameData = (GameData) getIntent().getSerializableExtra("DATA");
        showResult(gameData);
    }

    private void showResult(GameData data) {
        LinearLayout your = (LinearLayout) findViewById(R.id.yourList);
        LinearLayout correct = (LinearLayout) findViewById(R.id.correctList);

        for (int i = 0; i < data.getQuestions().size(); i++) {
            TextView answer = writeText(data.getAnswers().get(i).toUpperCase(new Locale("swe")), your);
            TextView question = writeText(data.getQuestions().get(i).getWord().toUpperCase(new Locale("swe")), correct);

            if(answer.getText().equals(question.getText())){
                answer.setTextColor(Color.BLUE);
                question.setTextColor(Color.BLUE);
            }
            else{
                answer.setTextColor(Color.RED);
                question.setTextColor(Color.BLUE);
            }

            your.addView(answer);
            correct.addView(question);

        }
    }
    private TextView writeText(String question, LinearLayout linear){
        TextView text = new TextView(this);
        text.setText(question);
        text.setTextSize(18);
        text.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return text;
    }
}
