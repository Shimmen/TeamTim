package teamtim.teamtimapp.activities;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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
    private TextView prefixLabel;
    private char[] lettersInWord;
    private TextView[] currentLetters;
    private int currentQ;
    private PlayPresenter p;
    private String word;
    private int currentLetterToAdd;
    private Button[] tiles;
    private boolean tileSelected;
    private Button selectedTile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        prefixLabel = (TextView) findViewById(R.id.pefixLabel);
        imageView = (ImageView) findViewById(R.id.imageView);
        p = new PlayPresenter(this, this.getIntent().getExtras().getString("SELECTED_CATEGORY"));
        buttonGrid = (GridLayout) findViewById(R.id.buttonGrid);
        letterInput = (LinearLayout) findViewById(R.id.linearLayout);
        setKeyboard();
        currentQ = 1;
        tileSelected = false;
    }

    private void setImage(int image){
        imageView.setImageResource(image);
    }

    public void newQuestion(WordQuestion w){
        currentLetters = new TextView[w.getWord().length()];
        currentLetterToAdd = 0;
        setImage(w.getImage());
        word = w.getWord();
        prefixLabel.setText(w.getPrefix());
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
        tiles = new Button[word.length()];
        for (int i = 0; i < word.length(); i++) {
            createButtons(i);
        }
    }

    public void checkAnswer(View v){
        StringBuffer buf = new StringBuffer();
        for (Button b : tiles) {
            buf.append(b.getText());
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
        final Button b = new Button(this);
        final Button bCopy = b;
        tiles[i] = bCopy;
        b.setText(Character.toString(lettersInWord[i]));
        b.setOnTouchListener(new TouchEventListener());
        buttonGrid.addView(b, 100, 110);
    }

    public void speak(View v) {
        p.speakWord(getApplicationContext());
    }

    protected class TouchEventListener implements View.OnTouchListener {
        int lastMod;
        int startOfGrid;
        public boolean onTouch(View v, MotionEvent e) {
            final int action = e.getActionMasked();
            View parent = (View) v.getParent();
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    if (e.getRawX() > startOfGrid && e.getRawX() < tiles.length*v.getWidth()+startOfGrid) {
                        if ((e.getRawX() - startOfGrid) / v.getWidth() != lastMod) {
                            int changedButton = (int) Math.floor((e.getRawX() - startOfGrid) / v.getWidth());
                            String copy = tiles[lastMod].getText().toString();
                            tiles[lastMod].setText(tiles[changedButton].getText());
                            tiles[changedButton].setText(copy);
                        }
                        lastMod = (int) Math.floor((e.getRawX() - startOfGrid) / v.getWidth());
                    }
                    return true;
                case MotionEvent.ACTION_DOWN:
                    startOfGrid = Math.round(parent.getX());
                    lastMod = (int)Math.floor((e.getRawX() - startOfGrid) / v.getWidth());
                    return true;
                case MotionEvent.ACTION_BUTTON_PRESS:
                    return true;
                case MotionEvent.ACTION_UP:
                    v.refreshDrawableState();
                    return true;
                default:
                    Log.e("DragDrop Error","Unknown action type received by TouchEventListener.");
                    break;
            }
            return false;
        }
    }
}
