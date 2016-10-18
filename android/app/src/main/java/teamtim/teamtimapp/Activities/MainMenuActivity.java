package teamtim.teamtimapp.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;

import teamtim.teamtimapp.R;
import teamtim.teamtimapp.network.DefaultNetworkManager;

public class MainMenuActivity extends AppCompatActivity {
    private ImageButton burgerButton;
    private ImageView playAloneIcon;
    private ImageView playTogetherIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        playAloneIcon = (ImageView) findViewById(R.id.playAloneIcon);
        playTogetherIcon = (ImageView) findViewById(R.id.playTogetherIcon);
        getSupportActionBar().hide();
        setImage();
    }

    private void setImage(){
        playAloneIcon.setImageResource(R.drawable.play_alone_image);
        playTogetherIcon.setImageResource(R.drawable.play_together_image);
    }

    public void playGame(View v){
        toCategory("Single");
    }

    public void playTogether(View v){
        toCategory("Multi");
    }

    public void toCategory(String mode){
        Intent intentMain = new Intent(MainMenuActivity.this, CategoryActivity.class);
        intentMain.putExtra("MODE", mode);
        MainMenuActivity.this.startActivity(intentMain);

    }

    public void openMenu(View v){
        //Do stuff
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Avsluta")
                .setMessage("Vill du stänga appen?")
                .setPositiveButton("Ja", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(1);
                    }

                })
                .setNegativeButton("Nej", null)
                .show();
    }
}
