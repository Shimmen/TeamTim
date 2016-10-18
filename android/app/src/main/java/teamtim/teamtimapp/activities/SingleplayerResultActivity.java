package teamtim.teamtimapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import teamtim.teamtimapp.R;
import teamtim.teamtimapp.managers.GameData;

public class SingleplayerResultActivity extends AppCompatActivity {

    private LinearLayout list;
    private GameData gameData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        gameData = (GameData) getIntent().getSerializableExtra("DATA");
        showResult(gameData);
    }

    private void showResult(GameData data) {
        LinearLayout your = (LinearLayout) findViewById(R.id.yourList);
        LinearLayout correct = (LinearLayout) findViewById(R.id.correctList);
        your.setWeightSum(0.5f);
        correct.setWeightSum(0.5f);

        for (int i = 0; i < data.getQuestions().size(); i++) {
            WriteText(data.getAnswers().get(i), your);
            WriteText(data.getQuestions().get(i).getWord(), correct);
        }
    }
    private void WriteText(String question, LinearLayout linear){
        TextView text = new TextView(this);
        text.setText(question);
        text.setTextSize(18);
        text.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        linear.addView(text);
    }
}
