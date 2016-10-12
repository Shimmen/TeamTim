package teamtim.teamtimapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.NumberKeyListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import teamtim.teamtimapp.R;
import teamtim.teamtimapp.database.WordQuestion;
import teamtim.teamtimapp.managers.OnResultCallback;
import teamtim.teamtimapp.managers.OnResultListener;
import teamtim.teamtimapp.managers.ResultKey;
import teamtim.teamtimapp.managers.SinglePlayerClient;
import teamtim.teamtimapp.presenter.PlayPresenter;
import teamtim.teamtimapp.speechSynthesizer.ISpeechSynthesizer;
import teamtim.teamtimapp.speechSynthesizer.SoundPlayer;

public class PlayActivity extends AppCompatActivity {

    private static PlayActivity instance = null;

    private PlayPresenter presenter;

    private OnResultCallback resultCallback;

    private ImageView imageView;
    private GridLayout buttonGrid;
    private LinearLayout letterInput;
    private TextView[] currentLetters;

    private ISpeechSynthesizer soundPlayer = new SoundPlayer();

    private char[] lettersInWord;
    private int currentLetterToAdd;

    private WordQuestion question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play);
        imageView = (ImageView) findViewById(R.id.imageView);
        buttonGrid = (GridLayout) findViewById(R.id.buttonGrid);
        letterInput = (LinearLayout) findViewById(R.id.linearLayout);

        presenter = new PlayPresenter();

        instance = this;
        setResultCallback(OnResultListener.getListener());
        resultCallback.onResult(ResultKey.READY, 0);
    }

    public static PlayActivity getInstance() throws NullPointerException {
        if (instance == null) throw new NullPointerException("Game not initialized");
        return instance;
    }

    private void setImage(int image){
        imageView.setImageResource(image);
    }

    public void newQuestion(WordQuestion w){
        question = w;
        currentLetters = new TextView[w.getWord().length()];
        currentLetterToAdd = 0;
        setImage(w.getImage());
        //Set keyboard for new question
        setKeyboard();
        //Change this somehow, since setKeyboard is called before the presenter has been completely
        //created the app crashes. Either change some implementation or move shuffle and split
        //back into Activity
    }

    public void onKeyPressed(int keyCode, KeyEvent event){

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
            createTextFields(i);
        }
    }

    public void checkAnswer(View v){
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < currentLetters.length ; ++i) {
            buf.append(currentLetters[i].getText());
        }
        String toCheck = buf.toString();
        System.out.println(question.getWord() + ", "+toCheck);
        soundPlayer.speak(this, question.getWord().equals(toCheck));
        resultCallback.onResult(ResultKey.SUBMIT, question.getWord().equals(toCheck) ? 1 : 0);
    }

    public void setResultCallback(OnResultCallback resultCallback){
        this.resultCallback = resultCallback;
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

}
