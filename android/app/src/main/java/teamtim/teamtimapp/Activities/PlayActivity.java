package teamtim.teamtimapp.activities;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import teamtim.teamtimapp.R;
import teamtim.teamtimapp.database.WordQuestion;
import teamtim.teamtimapp.managers.GameData;
import teamtim.teamtimapp.managers.QuestionResultListener;
import teamtim.teamtimapp.managers.SinglePlayerClient;
import teamtim.teamtimapp.presenter.PlayPresenter;
import teamtim.teamtimapp.speechSynthesizer.ISpeechSynthesizer;
import teamtim.teamtimapp.speechSynthesizer.SoundPlayer;

public class PlayActivity extends AppCompatActivity {

    public static final int POST_ANSWER_DELAY_MS = 1000;

    private QuestionResultListener currentResultListener;
    private PlayPresenter presenter;

    private ImageView imageView;
    private GridLayout buttonGrid;

    private TextView prefixLabel;
    private ProgressDialog initialProgressDialog;
    private Button answerBtn;
    private TextView timerText;
    private TextView playerOneScore;
    private TextView playerTwoScore;

    private ISpeechSynthesizer soundPlayer = new SoundPlayer();

    private char[] lettersInWord;

    private int timeRemaining;
    private int totalTime;
    private final static int TICKER = 1000;

    private CountDownTimer timer;
    Animation wobbleAnimation;


    private WordQuestion question;

    private Button[] tiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play);
        prefixLabel = (TextView) findViewById(R.id.pefixLabel);
        imageView = (ImageView) findViewById(R.id.imageView);
        buttonGrid = (GridLayout) findViewById(R.id.buttonGrid);
        answerBtn = (Button) findViewById(R.id.answerButton);
        timerText = (TextView) findViewById(R.id.timerText);
        playerOneScore = (TextView) findViewById(R.id.playerOne);
        playerTwoScore = (TextView) findViewById(R.id.playerTwo);
        wobbleAnimation = AnimationUtils.loadAnimation(this, R.anim.wobble);

        initialProgressDialog = ProgressDialog.show(this, "Laddar", "Väntar på första frågan...", true, false, null);

        presenter = new PlayPresenter();
        getSupportActionBar().hide();
        currentResultListener = QuestionResultListener.getGlobalListener();
        currentResultListener.onPlayActivityCreated(this);


    }

    @Override
    protected void onPause(){
        super.onPause();
        if (timer != null)
            timer.cancel();
        currentResultListener.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        currentResultListener.onResume();
        //Handle multiplayer? Can this be done from MPC
    }

    public void resumeSinglePlayer(){
        setTime(timeRemaining);
    }

    private void setTime(int timeInSeconds){
        if (timer != null)
            timer.cancel();
        timer = new CountDownTimer(timeInSeconds * 1000, TICKER) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining--;
                System.out.print("Time remaining: "+timeRemaining);
                timerText.setText(timeRemaining + "");
                if(timeRemaining <= 5) {
                    timerText.setTextColor(Color.RED);
                }
            }

            @Override
            public void onFinish() {
                //timeRemaining = 15;
                checkAnswer(timerText);
                //currentResultListener.onQuestionResult(0);
                //this.cancel();
            }
        }.start();

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


                totalTime = 10 + 3*w.getWord().length();
                System.out.println("TOTAL TIME: " + totalTime);
                timeRemaining = totalTime;

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

                speak(answerBtn);

                setTime(totalTime);
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
        if(buttonGrid != null) {
            buttonGrid.removeAllViews();
        }
        //Kanske borde göra en till metod i playPresenter som gör båda shuffle och splitstring samtidigt?
        lettersInWord = presenter.shuffle(presenter.splitString(question.getWord()));
        for (int i = 0; i < question.getWord().length(); i++) {

            createButtons(i);
        }
    }

    public void checkAnswer(View v){
        if (timer != null)
            timer.cancel();

        StringBuffer buf = new StringBuffer();
        for (Button b : tiles) {
            buf.append(b.getText());
        }
        final String toCheck = buf.toString();
        System.out.println(question.getWord() + ", "+toCheck);

        boolean isCorrect = question.getWord().equals(toCheck);
        soundPlayer.speak(this, isCorrect);

        if (!isCorrect) {
            imageView.startAnimation(wobbleAnimation);
            v.performHapticFeedback(1);
        }

        answerBtn.setClickable(false);
        answerBtn.setTextColor(Color.GRAY);


        final int pointsAcquired = question.getWord().equals(toCheck) ? 1 : 0;
        final int answerTime = totalTime;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentResultListener.onQuestionResult(pointsAcquired, answerTime, toCheck);
            }
        }, POST_ANSWER_DELAY_MS);

    }

    /** TODO: remove this if other endGame works
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
     */

    public void endSingleGame(GameData data){
        timer.cancel();
        timer = null;
        System.out.println("PlayActivity -> EndGAme");
        Intent i = new Intent(PlayActivity.this, EndGameActivity.class);

        i.putExtra("DATA", data);
        startActivity(i);
    }

    public void endMultiGame(GameData data){
        timer.cancel();
        timer = null;
        Intent i = new Intent(PlayActivity.this, EndMultiplayerActivity.class);

        i.putExtra("DATA", data);
        startActivity(i);
    }

    private void createButtons(int i) {


        Button b = new Button(this);

        final Button bCopy = b;
        tiles[i] = bCopy;
        b.setText(Character.toString(lettersInWord[i]));
        b.setOnTouchListener(new TouchEventListener());
        buttonGrid.addView(b, 150, 150);
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
        int currentPos;
        int startOfGrid;
        int gridY;
        TextView left;
        TextView middle;
        TextView right;

        public void removeTextViews(RelativeLayout layout) {
            layout.removeView(left);
            layout.removeView(middle);
            layout.removeView(right);
        }

        public TextView createTextView(TextView text, int position) {
            text.setText(tiles[position].getText());
            return text;
        }

        public RelativeLayout.LayoutParams createParams(int position) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
            params.topMargin = gridY;
            params.leftMargin = (int) Math.floor(startOfGrid + tiles[position].getX());
            return params;
        }

        public void createTextViews(RelativeLayout layout) {
            if (currentPos > 0) {
                left = new TextView(layout.getContext());
                layout.addView(createTextView(left, currentPos-1), createParams(currentPos-1));
            }

            middle = new TextView(layout.getContext());
            middle.setTextColor(Color.parseColor("#737aff"));
            layout.addView(createTextView(middle, currentPos), createParams(currentPos));

            if (currentPos < tiles.length - 1) {
                right = new TextView(layout.getContext());
                layout.addView(createTextView(right, currentPos+1), createParams(currentPos+1));
            }
        }

        public boolean onTouch(View v, MotionEvent e) {
            final int action = e.getActionMasked();
            View parent = (View) v.getParent();
            RelativeLayout layout = (RelativeLayout) parent.getParent();
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    if (e.getRawX() > startOfGrid && e.getRawX() < tiles.length*v.getWidth()+startOfGrid) {
                        if (((int) Math.floor(e.getRawX() - startOfGrid) / v.getWidth()) != currentPos) {
                            int changedButton = (int) Math.floor((e.getRawX() - startOfGrid) / v.getWidth());
                            String copy = tiles[currentPos].getText().toString();
                            tiles[currentPos].setText(tiles[changedButton].getText());
                            tiles[changedButton].setText(copy);
                            tiles[currentPos].getBackground().clearColorFilter();
                            tiles[changedButton].getBackground().setColorFilter(Color.parseColor("#737aff"), PorterDuff.Mode.MULTIPLY);
                            v.performHapticFeedback(1);
                            removeTextViews(layout);
                            currentPos = (int) Math.floor((e.getRawX() - startOfGrid) / v.getWidth());
                            createTextViews(layout);
                        }
                        currentPos = (int) Math.floor((e.getRawX() - startOfGrid) / v.getWidth());
                    }
                    return true;
                case MotionEvent.ACTION_DOWN:
                    v.performHapticFeedback(0);
                    startOfGrid = Math.round(parent.getX());
                    gridY = Math.round(parent.getY() - v.getHeight() -  14);
                    currentPos = (int)Math.floor((e.getRawX() - startOfGrid) / v.getWidth());
                    tiles[currentPos].getBackground().setColorFilter(Color.parseColor("#737aff"), PorterDuff.Mode.MULTIPLY);

                    createTextViews(layout);
                    return true;
                case MotionEvent.ACTION_BUTTON_PRESS:
                    return true;
                case MotionEvent.ACTION_UP:
                    tiles[currentPos].getBackground().clearColorFilter();
                    removeTextViews(layout);
                    v.refreshDrawableState();
                    return true;
                case MotionEvent.ACTION_CANCEL:
                    removeTextViews(layout);
                default:
                    Log.e("DragDrop Error","Unknown action type received by TouchEventListener.");
                    break;
            }
            return false;
        }
    }
}
