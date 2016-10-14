package teamtim.teamtimapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import teamtim.teamtimapp.R;

public class SingleplayerResultActivity extends AppCompatActivity {

    private String[] questions;
    private LinearLayout list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Bundle extras = getIntent().getExtras();
        questions = extras.getStringArray("QUESTIONS");
        list = (LinearLayout) findViewById(R.id.resultList);
        showResult(questions);
    }

    private void showResult(String[] questions) {
        for (int i = 0; i < questions.length; i++) {
            LinearLayout horizontal = new LinearLayout(this);
            horizontal.setOrientation(LinearLayout.HORIZONTAL);
            createText(questions[i], horizontal);
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
