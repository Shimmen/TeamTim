package teamtim.teamtimapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class PlayActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText answerText;
    private String check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.mipmap.ic_launcher);
    }

    public void checkAnswer(View v){
        answerText = (EditText) findViewById(R.id.answerTextField);
        check = answerText.getText().toString();
        if (check.equals("Android")) {
            Log.d("Answer:", "Correct");
        }
        else{
            Log.d("Answer:", "Wrong");
        }

    }
}
