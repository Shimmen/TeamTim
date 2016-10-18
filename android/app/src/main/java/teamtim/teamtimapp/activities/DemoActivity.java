package teamtim.teamtimapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import teamtim.teamtimapp.R;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        //VideoView videoView = new VideoView(this);

        //String path = "android.resource://" + getPackageName() + "/" + R.raw.demo;
        //videoView.setVideoURI(Uri.parse(path));
        WebView webView = new WebView(this);
        webView.loadUrl("file:///android_res/raw/demo.gif");

        RelativeLayout container = (RelativeLayout) findViewById(R.id.videoContainer);
        container.addView(webView);
        //videoView.start();

    }
}
