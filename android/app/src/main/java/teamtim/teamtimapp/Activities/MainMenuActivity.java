package teamtim.teamtimapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;

import teamtim.teamtimapp.R;

public class MainMenuActivity extends AppCompatActivity {

    private static boolean isFirstActivityLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        if (getActionBar() != null) {
            getActionBar().hide();
        } else if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Load and animate the demo gif
        WebView demoWebView = (WebView) findViewById(R.id.demoWebView);
        demoWebView.getSettings().setLoadWithOverviewMode(true);
        demoWebView.getSettings().setUseWideViewPort(true);
        demoWebView.loadUrl("file:///android_res/raw/demo_stava.gif");
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        demoWebView.startAnimation(fadeIn);

        if (MainMenuActivity.isFirstActivityLoad) {
            // Animate logo
            ImageView logo = (ImageView) findViewById(R.id.logoImageView);
            Animation logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_load);
            logo.startAnimation(logoAnim);

            MainMenuActivity.setActivityHasBeenLoaded();
        }
    }

    public void playGame(View v){
        toCategory("Single");
    }

    public void playTogether(View v){
        toCategory("Multi");
    }

    private static void setActivityHasBeenLoaded() {
        MainMenuActivity.isFirstActivityLoad = false;
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
