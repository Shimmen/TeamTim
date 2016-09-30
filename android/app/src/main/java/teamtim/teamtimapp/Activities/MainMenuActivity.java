package teamtim.teamtimapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import teamtim.teamtimapp.R;

public class MainMenuActivity extends AppCompatActivity {
    private ImageButton burgerButton;
    private ImageView playAloneIcon;
    private ImageView playTogetherIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        burgerButton = (ImageButton) findViewById(R.id.burgerButton);
        playAloneIcon = (ImageView) findViewById(R.id.playAloneIcon);
        playTogetherIcon = (ImageView) findViewById(R.id.playTogetherIcon);

        setImage();
    }

    private void setImage(){
        burgerButton.setImageResource(R.drawable.burger_image);
        playAloneIcon.setImageResource(R.drawable.play_alone_image);
        playTogetherIcon.setImageResource(R.drawable.play_together_image);
    }

    public void playGame(View v){
        Intent intentMain = new Intent(MainMenuActivity.this, CategoryActivity.class);
        MainMenuActivity.this.startActivity(intentMain);
    }

    public void playTogether(View v){
        Intent intentMain = new Intent(MainMenuActivity.this, CategoryActivity.class);
        MainMenuActivity.this.startActivity(intentMain);
    }

    public void openMenu(View v){
        //Do stuff
    }
}
