package teamtim.teamtimapp.activities;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;


import android.util.Log;
import android.view.MotionEvent;

import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;


import teamtim.teamtimapp.R;
import teamtim.teamtimapp.database.WordQuestion;
import teamtim.teamtimapp.managers.QuestionResultListener;
import teamtim.teamtimapp.presenter.PlayPresenter;
import teamtim.teamtimapp.speechSynthesizer.ISpeechSynthesizer;
import teamtim.teamtimapp.speechSynthesizer.SoundPlayer;

public class PlayActivity extends AppCompatActivity {

    private QuestionResultListener currentResultListener;
    private PlayPresenter presenter;

    private ImageView imageView;
    private GridLayout buttonGrid;
    private LinearLayout letterInput;

    private TextView prefixLabel;
    private TextView[] currentLetters;
    private ProgressDialog initialProgressDialog;
    private Button answerBtn;
    private TextView timerText;
    private TextView playerOneScore;
    private TextView playerTwoScore;

    private ISpeechSynthesizer soundPlayer = new SoundPlayer();

    private char[] lettersInWord;
    private int currentLetterToAdd;

    private int totalTime;
    private final static int TOTALTIME = 15000;
    private final static int TICKER = 1000;

    private CountDownTimer timer;


    private WordQuestion question;

    private Button[] tiles;
    private boolean tileSelected;
    private Button selectedTile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play);
        prefixLabel = (TextView) findViewById(R.id.pefixLabel);
        imageView = (ImageView) findViewById(R.id.imageView);
        buttonGrid = (GridLayout) findViewById(R.id.buttonGrid);
        letterInput = (LinearLayout) findViewById(R.id.linearLayout);
        answerBtn = (Button) findViewById(R.id.answerButton);
        timerText = (TextView) findViewById(R.id.timerText);
        playerOneScore = (TextView) findViewById(R.id.playerOne);
        playerTwoScore = (TextView) findViewById(R.id.playerTwo);

        initialProgressDialog = ProgressDialog.show(this, "Laddar", "Väntar på första frågan...", true, false, null);

        presenter = new PlayPresenter();
        getSupportActionBar().hide();
        currentResultListener = QuestionResultListener.getGlobalListener();
        currentResultListener.onPlayActivityCreated(this);




        tileSelected = false;
    }

    private void setImage(int image){
        imageView.setImageResource(image);
    }

    public void newQuestion(final WordQuestion w){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                question = w;
                prefixLabel.setText(w.getPrefix());
                tiles = new Button[question.getWord().length()];

                currentLetters = new TextView[w.getWord().length()];
                currentLetterToAdd = 0;
                totalTime = 15;
                setImage(w.getImage());
                //Set keyboard for new question
                setKeyboard();
                //Change this somehow, since setKeyboard is called before the presenter has been completely
                //created the app crashes. Either change some implementation or move shuffle and split
                //back into Activity

                initialProgressDialog.hide();
                answerBtn.setClickable(true);
                answerBtn.setTextColor(Color.BLACK);
                timerText.setTextColor(Color.BLACK);

                timer = new CountDownTimer(TOTALTIME, TICKER) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        totalTime--;
                        timerText.setText(totalTime + "");
                        if(totalTime <= 5) {
                            timerText.setTextColor(Color.RED);
                        }
                    }

                    @Override
                    public void onFinish() {
                        totalTime = 15;
                        checkAnswer(timerText);
                        //currentResultListener.onQuestionResult(0);
                        //this.cancel();
                    }
                }.start();



            }
        });
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Avsluta spel")
                .setMessage("Vill du avsluta pågående spel?")
                .setPositiveButton("Ja", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timer.cancel();
                        Intent main = new Intent(PlayActivity.this, MainMenuActivity.class);
                        startActivity(main);
                    }

                })
                .setNegativeButton("Nej", null)
                .show();

    }

    public void setKeyboard(){
        //Will change a lot, purely for demonstration
        if(buttonGrid != null && letterInput != null) {
            buttonGrid.removeAllViews();
            letterInput.removeAllViews();
        }
        //Kanske borde göra en till metod i playPresenter som gör båda shuffle och splitstring samtidigt?
        lettersInWord = presenter.shuffle(presenter.splitString(question.getWord()));
        for (int i = 0; i < question.getWord().length(); i++) {

            createButtons(i);
        }
    }

    public void checkAnswer(View v){
        timer.cancel();

        StringBuffer buf = new StringBuffer();
        for (Button b : tiles) {
            buf.append(b.getText());
        }
        String toCheck = buf.toString();
        System.out.println(question.getWord() + ", "+toCheck);
        soundPlayer.speak(this, question.getWord().equals(toCheck));

        answerBtn.setClickable(false);
        answerBtn.setTextColor(Color.GRAY);

        int pointsAcquired = question.getWord().equals(toCheck) ? 1 : 0;
        currentResultListener.onQuestionResult(pointsAcquired, totalTime);
    }

    public void endGame(int correctAnswers, int totalAnswers, List<WordQuestion> questions){
        Intent i = new Intent(PlayActivity.this, EndGameActivity.class);
        Bundle extras = new Bundle();

        String answers = String.valueOf(correctAnswers);
        String total = String.valueOf(totalAnswers);
        String[] questionArray = new String[questions.size()];

        for(int j = 0; j < questions.size(); j++){
            questionArray[j] = questions.get(j).getWord();
        }

        extras.putStringArray("QUESTIONS", questionArray);
        extras.putString("CORRECT_ANSWERS", answers);
        extras.putString("TOTAL_ANSWERS", total);

        i.putExtras(extras);
        startActivity(i);
    }

    private void createButtons(int i) {


        Button b = new Button(this);

        final Button bCopy = b;
        tiles[i] = bCopy;
        b.setText(Character.toString(lettersInWord[i]));
        /**
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentLetters[currentLetterToAdd].setText(Character.toString(lettersInWord[iCopy]));
                bCopy.setEnabled(false);
                currentLetterToAdd += 1;
            }
        });
         */
        b.setOnTouchListener(new TouchEventListener());
        buttonGrid.addView(b, 100, 110);
    }

    private void createTextFields(int i) {
        TextView tv = new TextView(this);
        currentLetters[i] = tv;
        tv.setEnabled(false);
        tv.setPaintFlags(tv.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        tv.setTextColor(0xFF000000);
        tv.setGravity(0x50 | 0x11);
        letterInput.addView(tv, 130, 130);

    }

    public void speak(View v){
        soundPlayer.speak(this, question);
    }


    public void setPlayerOneScore(final int score){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerOneScore.setText(score + "p");
            }
        });

    }

    public void setPlayerTwoScore(final int score){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerTwoScore.setText(score + "p");
            }
        });
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
                            tiles[lastMod].getBackground().clearColorFilter();
                            tiles[changedButton].getBackground().setColorFilter(Color.parseColor("#737aff"), PorterDuff.Mode.MULTIPLY);
                        }
                        lastMod = (int) Math.floor((e.getRawX() - startOfGrid) / v.getWidth());
                    }
                    return true;
                case MotionEvent.ACTION_DOWN:
                    v.performHapticFeedback(0);
                    startOfGrid = Math.round(parent.getX());
                    lastMod = (int)Math.floor((e.getRawX() - startOfGrid) / v.getWidth());
                    tiles[lastMod].getBackground().setColorFilter(Color.parseColor("#737aff"), PorterDuff.Mode.MULTIPLY);
                    return true;
                case MotionEvent.ACTION_BUTTON_PRESS:
                    return true;
                case MotionEvent.ACTION_UP:
                    tiles[lastMod].getBackground().clearColorFilter();
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
