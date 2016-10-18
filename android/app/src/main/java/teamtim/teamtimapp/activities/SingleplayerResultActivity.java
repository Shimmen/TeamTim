package teamtim.teamtimapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
        list = (LinearLayout) findViewById(R.id.resultList);
        showResult(gameData);
    }

    private void showResult(GameData data) {
        for (int i = 0; i < data.getQuestions().size(); i++) {
            LinearLayout linAnswer = CreateLinear();
            WriteText(data.getAnswers().get(i), linAnswer);
            WriteText(data.getQuestions().get(i).getWord(), linAnswer);

            list.addView(linAnswer);
        }
    }
    private LinearLayout CreateLinear(){
        LinearLayout linear = new LinearLayout(this);
        linear.setOrientation(LinearLayout.HORIZONTAL);
        linear.setGravity(Gravity.CENTER);
        return linear;
    }
    private void WriteText(String question, LinearLayout linear){
        TextView text = new TextView(this);
        text.setText(question);
        text.setTextSize(18);
        linear.addView(text);
    }
}
