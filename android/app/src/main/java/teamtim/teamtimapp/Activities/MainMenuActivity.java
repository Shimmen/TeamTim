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


import teamtim.teamtimapp.R;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
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
    public void onBackPressed() {
        // We're in the main menu, do nothing.
    }
}
