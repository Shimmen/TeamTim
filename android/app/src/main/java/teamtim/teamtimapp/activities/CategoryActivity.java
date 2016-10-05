package teamtim.teamtimapp.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;
import java.util.Observable;

import teamtim.teamtimapp.R;
import teamtim.teamtimapp.managers.SinglePlayerClient;
import teamtim.teamtimapp.presenter.CategoryPresenter;
import teamtim.teamtimapp.presenter.PlayPresenter;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener{

    private CategoryPresenter presenter;
    private LinearLayout categoryLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
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
        List<String> list = presenter.getCategories();
        if(categoryLayout != null) {
            categoryLayout.removeAllViews();
        }
        for (String category : list) {
            if (category.indexOf(query.toLowerCase()) < 0) continue;
            Button categoryButton = new Button(this);
            categoryButton.setText(category);
            categoryButton.setOnClickListener(this);
            categoryLayout.addView(categoryButton);
        }
    }

    @Override
    public void onClick(View v) {
        SinglePlayerClient spc = new SinglePlayerClient(((Button)v).getText().toString());
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra("LISTENER", spc);
        startActivity(intent);
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
}
