package teamtim.teamtimapp.activities;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

import teamtim.teamtimapp.presenter.PlayPresenter;
import teamtim.teamtimapp.R;
import teamtim.teamtimapp.database.WordQuestion;

public class PlayActivity extends AppCompatActivity {

    private ImageView imageView;
    private GridLayout buttonGrid;
    private char[] test;
    private int currentQ;
    private int totalQ;
    private PlayPresenter p;
    private String word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        imageView = (ImageView) findViewById(R.id.imageView);
        p = new PlayPresenter(this);
        totalQ = 1;
        currentQ = 1;

    }

    private void setImage(int image){
        //imageView.setImageResource(R.mipmap.ic_launcher);
        imageView.setImageResource(image);
    }

    public void newQuestion(WordQuestion w){
        setImage(w.getImage());
        word = w.getWord();
        setKeyboard();
    }

    public void setKeyboard(){
        //Will change a lot, purely for demonstration

        buttonGrid = (GridLayout) findViewById(R.id.grid);
        if(buttonGrid != null) {

            buttonGrid.removeAllViews();
        }
        test = shuffle(splitString(word));
        for (int i = 0; i < word.length(); i++) {
            Button b = new Button(this);
            b.setText(Character.toString(test[i]));
            buttonGrid.addView(b, 130, 130);
        }
    }


    public void checkAnswer(View v){
        EditText answerText = (EditText) findViewById(R.id.answerTextField);
        String check = answerText.getText().toString();

        p.checkAnswer(check);
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
