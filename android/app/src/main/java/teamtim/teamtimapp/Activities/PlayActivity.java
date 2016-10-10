package teamtim.teamtimapp.Activities;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import teamtim.teamtimapp.R;
import teamtim.teamtimapp.database.WordQuestion;
import teamtim.teamtimapp.Presenter.PlayPresenter;

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
    private boolean approachToMinimum;
    private boolean approachToMax;
    private int lastLoc;
    private int buttonsMoved;

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
        buttonsMoved = 0;
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
        final int iCopy = i;
        final Button b = new Button(this);
        final Button bCopy = b;
        tiles[i] = bCopy;
        b.setText(Character.toString(lettersInWord[i]));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((bCopy.getText().equals(" "))){
                    //Do nothing
                }else if((!tileSelected && !bCopy.getText().equals(" "))){
                    selectTile(bCopy);
                }else{
                    switchTilePosition(bCopy);
                }
            }
        });
        b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData data = ClipData.newPlainText(Integer.toString(iCopy), bCopy.getText());
                data.addItem(new ClipData.Item(bCopy.getText()));
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
                view.startDrag(data,
                            shadow,
                            iCopy,
                            0
                );
                return true;
            }
        });
        b.setOnDragListener(new DragEventListener());
        buttonGrid.addView(b, 100, 110);
    }

    private void selectTile(Button tile){
        tileSelected = true;

        selectedTile = tile;
        selectedTile.getBackground().setColorFilter(Color.parseColor("#737aff"), PorterDuff.Mode.MULTIPLY);
        selectedTile.setTextColor(Color.WHITE);
    }

    private void switchTilePosition(Button tile){
        CharSequence newChar = selectedTile.getText();
        selectedTile.setText(tile.getText());
        tile.setText(newChar);

        selectedTile.getBackground().clearColorFilter();
        selectedTile.setTextColor(Color.BLACK);

        tileSelected = false;
    }

    public void speak(View v) {
        p.speakWord(getApplicationContext());
    }

    protected class DragEventListener implements View.OnDragListener {
        public boolean onDrag(View v, DragEvent event) {
            int buttonIndex = (Integer) event.getLocalState();
            final int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    if (event.getX() < 20) {
                        approachToMinimum = true;
                        approachToMax = false;
                        if (lastLoc > 80){
                            String copy = tiles[buttonIndex+buttonsMoved].getText().toString();
                            tiles[buttonIndex + buttonsMoved ].setText(tiles[buttonIndex+ buttonsMoved+1].getText());
                            tiles[buttonIndex+ buttonsMoved+1].setText(copy);
                            buttonsMoved += 1;
                        }
                    }   else if (event.getX() > 80) {
                        approachToMinimum = false;
                        approachToMax = true;
                        if (lastLoc < 20) {
                            String copy = tiles[buttonIndex-1+buttonsMoved].getText().toString();
                            tiles[buttonIndex+buttonsMoved-1].setText(tiles[buttonIndex+ buttonsMoved].getText());
                            tiles[buttonIndex+buttonsMoved].setText(copy);
                            buttonsMoved -= 1;
                        }
                    }
                    lastLoc = Math.round(event.getX());
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    return true;
                case DragEvent.ACTION_DROP:
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    buttonsMoved = 0;
                    approachToMax = false;
                    approachToMinimum = false;
                    return true;
                default:
                    Log.e("DragDrop Error","Unknown action type received by OnDragListener.");
                    break;
            }
            return false;
        }
    }
}
