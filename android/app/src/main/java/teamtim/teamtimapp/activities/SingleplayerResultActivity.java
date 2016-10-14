package teamtim.teamtimapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import teamtim.teamtimapp.R;
import teamtim.teamtimapp.database.WordQuestion;
import teamtim.teamtimapp.managers.GameData;

public class SingleplayerResultActivity extends AppCompatActivity {

    private List<WordQuestion> questions;
    private LinearLayout list;
    private GameData gameData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        gameData = (GameData) getIntent().getSerializableExtra("DATA");
        questions = gameData.getQuestions();
        list = (LinearLayout) findViewById(R.id.resultList);
        showResult(questions);
    }

    private void showResult(List<WordQuestion> questions) {
        for (int i = 0; i < questions.size(); i++) {
            LinearLayout horizontal = new LinearLayout(this);
            horizontal.setOrientation(LinearLayout.HORIZONTAL);
            createText(questions.get(i).getWord(), horizontal);
            list.addView(horizontal);
        }
    }
    private void createText(String question, LinearLayout linear){
        TextView text = new TextView(this);
        text.setText(question);
        text.setTextSize(18);
        text.setGravity(Gravity.CENTER);
        linear.addView(text);
    }
}
