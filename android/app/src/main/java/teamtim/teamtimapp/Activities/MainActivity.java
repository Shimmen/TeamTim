package teamtim.teamtimapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import teamtim.teamtimapp.R;
import teamtim.teamtimapp.network.DefaultNetworkManager;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultNetworkManager.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Intent intentMain = new Intent(MainActivity.this, CategoryActivity.class);
        MainActivity.this.startActivity(intentMain);

    }

    public void singleplayerStart(View v){
        Intent i = new Intent(MainActivity.this, CategoryActivity.class);
        startActivity(i);
    }

    public void multiplayerStart(View v) {
        Intent i = new Intent(this, MultiplayerActivity.class);
        startActivity(i);
    }
}
