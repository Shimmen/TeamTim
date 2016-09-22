package teamtim.teamtimapp.Activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

import teamtim.teamtimapp.R;

public class PlayActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText answerText;
    private GridLayout buttonGrid;
    private String check;
    private char[] test;
    private int currentQ;
    private int totalQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        imageView = (ImageView) findViewById(R.id.imageView);
        setImage(null);

        totalQ = 1;
        currentQ = 1;

        //Will change a lot, purely for demonstration
        test = shuffle(splitString("Android"));
        buttonGrid = (GridLayout) findViewById(R.id.grid);
        for (int i = 0; i < 7; i++) {
            Button b = new Button(this);
            b.setText(Character.toString(test[i]));
            buttonGrid.addView(b, 130, 130);
        }
    }

    public void setImage(Bitmap image){
        imageView.setImageResource(R.mipmap.ic_launcher);
        //imageView.setImageBitmap(image);
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

        if(currentQ == totalQ){
            //finishGame();}
        }
        else {currentQ++;}
    }

    //Put into another class or something later
    //And write better methods
    public char[] shuffle(char[] wordLetters) {
        for (int i = 0; i < wordLetters.length; i ++) {
            int newPos = (int)((Math.random() * wordLetters.length)-1);
            char tempChar = wordLetters[newPos];
            wordLetters[newPos] = wordLetters[i];
            wordLetters[i] = tempChar;
        }
        return wordLetters;
    }

    public char[] splitString(String word) {
        char[] splitString = new char[word.length()];
        for (int i = 0; i < word.length(); i++) {
            splitString[i] = word.charAt(i);
        }
        return splitString;
    }
}
