package teamtim.teamtimapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import teamtim.teamtimapp.R;

public class ResultActivity extends AppCompatActivity {

    private String[] questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle extras = getIntent().getExtras();
        questions = extras.getStringArray("QUESTIONS");
        showResult(questions);
        getSupportActionBar().hide();
    }

    private void showResult(String[] questions) {
        LinearLayout list = (LinearLayout) findViewById(R.id.resultList);


        for (int i = 0; i < questions.length; i++) {
            LinearLayout horizontal = new LinearLayout(this);
            horizontal.setOrientation(LinearLayout.HORIZONTAL);

            TextView text = new TextView(this);

            text.setText(questions[i]);

            text.setGravity(Gravity.END);
            text.setTextSize(18);

            horizontal.addView(text);
            list.addView(horizontal);
        }
    }
}
