package teamtim.teamtimapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.DownloadListener;
import android.webkit.WebView;

import teamtim.teamtimapp.R;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        WebView demoWebView = (WebView) findViewById(R.id.demoWebView);
        //demoWebView.setAlpha(0.0f);

        // Load the contents...
        demoWebView.getSettings().setLoadWithOverviewMode(true);
        demoWebView.getSettings().setUseWideViewPort(true);
        demoWebView.loadUrl("file:///android_res/raw/demo_stava.gif");

        // ...then fade in
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        demoWebView.startAnimation(fadeIn);
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
