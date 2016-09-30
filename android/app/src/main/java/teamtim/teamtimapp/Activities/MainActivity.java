package teamtim.teamtimapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import teamtim.teamtimapp.R;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intentMain = new Intent(MainActivity.this, CategoryActivity.class);
        MainActivity.this.startActivity(intentMain);

    }

    public void start(View v){
        Intent intentMain = new Intent(MainActivity.this, CategoryActivity.class);
        MainActivity.this.startActivity(intentMain);
    }
}
