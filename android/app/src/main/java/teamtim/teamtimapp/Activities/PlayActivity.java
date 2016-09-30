package teamtim.teamtimapp.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import teamtim.teamtimapp.R;
import teamtim.teamtimapp.database.WordQuestion;
import teamtim.teamtimapp.presenter.PlayPresenter;

public class PlayActivity extends AppCompatActivity {

    private ImageView imageView;
    private GridLayout buttonGrid;
    private LinearLayout letterInput;
    private char[] lettersInWord;
    private TextView[] currentLetters;
    private int currentQ;
    private PlayPresenter p;
    private String word;
    private int currentLetterToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        imageView = (ImageView) findViewById(R.id.imageView);
        p = new PlayPresenter(this, this.getIntent().getExtras().getString("SELECTED_CATEGORY"));
        buttonGrid = (GridLayout) findViewById(R.id.buttonGrid);
        letterInput = (LinearLayout) findViewById(R.id.linearLayout);
        setKeyboard();
        currentQ = 1;
    }

    private void setImage(int image){
        imageView.setImageResource(image);
    }

    public void newQuestion(WordQuestion w){
        currentLetters = new TextView[w.getWord().length()];
        currentLetterToAdd = 0;
        setImage(w.getImage());
        word = w.getWord();
        //Change this somehow, since setKeyboard is called before the presenter has been completely
        //created the app crashes. Either change some implementation or move shuffle and split
        //back into Activity
        currentQ += 1;
        if (currentQ > 1) {
            setKeyboard();
        }
    }

    public void setKeyboard(){
        //Will change a lot, purely for demonstration
        if(buttonGrid != null && letterInput != null) {
            buttonGrid.removeAllViews();
            letterInput.removeAllViews();
        }
        //Kanske borde göra en till metod i playPresenter som gör båda shuffle och splitstring samtidigt?
        lettersInWord = p.shuffle(p.splitString(word));
        for (int i = 0; i < word.length(); i++) {
            createButtons(i);
            createTextFields(i);
        }
    }

    public void checkAnswer(View v){
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < currentLetters.length ; ++i) {
            buf.append(currentLetters[i].getText());
        }
        String toCheck = buf.toString();
        p.checkAnswer(toCheck, getApplicationContext());
    }
    public void endGame(int correctAnswers, int totalAnswers){
        Intent i = new Intent(PlayActivity.this, EndGameActivity.class);
        Bundle extras = new Bundle();
        String answers = String.valueOf(correctAnswers);
        String total = String.valueOf(totalAnswers);
        extras.putString("CORRECT_ANSWERS", answers);
        extras.putString("TOTAL_ANSWERS", total);
        i.putExtras(extras);
        startActivity(i);
    }

    public void createButtons(int i) {
        final int iCopy = i;
        Button b = new Button(this);
        final Button bCopy = b;
        b.setText(Character.toString(lettersInWord[i]));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentLetters[currentLetterToAdd].setText(Character.toString(lettersInWord[iCopy]));
                bCopy.setEnabled(false);
                currentLetterToAdd += 1;
            }
        });
        buttonGrid.addView(b, 130, 130);
    }

    public void createTextFields(int i) {
        TextView tv = new TextView(this);
        currentLetters[i] = tv;
        tv.setEnabled(false);
        tv.setPaintFlags(tv.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        tv.setTextColor(0xFF000000);
        tv.setGravity(0x50 | 0x11);
        letterInput.addView(tv, 130, 130);
    }

    public void speak(View v) {
        p.speakWord(getApplicationContext());
    }
}
