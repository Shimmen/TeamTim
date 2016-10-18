package teamtim.teamtimapp.activities;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;

import teamtim.teamtimapp.R;
import teamtim.teamtimapp.database.CategoryWrapper;
import teamtim.teamtimapp.managers.SinglePlayerClient;
import teamtim.teamtimapp.presenter.CategoryPresenter;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener{

    private CategoryPresenter presenter;
    private LinearLayout categoryLayout;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mode = getIntent().getStringExtra("MODE");

        presenter = new CategoryPresenter();
        categoryLayout = (LinearLayout)findViewById(R.id.category_layout);
        setCategories("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(this);
        return true;
    }

    private void setCategories(String query){
        List<CategoryWrapper> list = presenter.getCategories();
        if(categoryLayout != null) {
            categoryLayout.removeAllViews();
        }
        for (CategoryWrapper category : list) {
            if (!category.getCategory().contains(query.toLowerCase())) continue;
            LinearLayout wrapper = new LinearLayout(this);
            //TODO: more colors maybe
            //category.getRatio() < .5f ? Color.RED : Color.GREEN
            wrapper.setBackgroundColor(Color.TRANSPARENT);
            wrapper.setOrientation(LinearLayout.HORIZONTAL);

            ImageView iv = new ImageView(this);
            iv.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
            TextView tv = new TextView(this);
            tv.setTextSize(18f);
            ImageView emoji = new ImageView(this);
            int[] emojiRef = new int[]{R.drawable.ic_sentiment_dissatisfied_black_24dp, R.drawable.ic_sentiment_neutral_black_24dp, R.drawable.ic_sentiment_satisfied_black_24dp, R.drawable.ic_sentiment_very_satisfied_black_24dp};
            //TODO: DO better
            int pick = Math.min(Math.round(category.getRatio() * 4), 4) -1;
            emoji.setImageResource(emojiRef[pick]);
            //Button categoryButton = new Button(this);
            tv.setText(category.getCategory().toUpperCase());
            iv.setImageResource(category.getImage());
            tv.setPadding(14,100,0,0);
            emoji.setPadding(14, 100, 0, 14);
            wrapper.addView(iv);
            wrapper.addView(tv);
            wrapper.addView(emoji);
            wrapper.setOnClickListener(this);
            categoryLayout.addView(wrapper);
        }
    }

    @Override
    public void onClick(View v) {
        String category = ((TextView)((LinearLayout) v).getChildAt(1)).getText().toString(); //err...
        if (mode.equals("Single")) {

            new SinglePlayerClient(category);
            Intent intent = new Intent(this, PlayActivity.class);
            startActivity(intent);

        } else {

            Intent intent = new Intent(this, MultiplayerActivity.class);
            intent.putExtra("CATEGORY", ((TextView)((LinearLayout) v).getChildAt(1)).getText().toString());
            startActivity(intent);

        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        setCategories(newText);
        return false;
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this, MainMenuActivity.class);
        startActivity(i);
    }
}
