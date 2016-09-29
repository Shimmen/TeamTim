package teamtim.teamtimapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import teamtim.teamtimapp.R;
import teamtim.teamtimapp.presenter.CategoryPresenter;
import teamtim.teamtimapp.presenter.PlayPresenter;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener{

    private CategoryPresenter p;
    private LinearLayout l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        p = new CategoryPresenter();
        l = (LinearLayout)findViewById(R.id.category_layout);
        setCategories();
    }

    private void setCategories(){
        List<String> list = p.getCategories();
        if(l != null) {
            l.removeAllViews();
        }
        for (String s : list) {

            Button coolButtonB = new Button(this);
            coolButtonB.setText(s);
            coolButtonB.setOnClickListener(this);
            l.addView(coolButtonB, 300, 130);


        }
    }

    @Override
    public void onClick(View v) {
        Intent intentMain = new Intent(CategoryActivity.this, PlayActivity.class);
        intentMain.putExtra("SELECTED_CATEGORY", ((Button)v).getText().toString());
        CategoryActivity.this.startActivity(intentMain);
    }
}
