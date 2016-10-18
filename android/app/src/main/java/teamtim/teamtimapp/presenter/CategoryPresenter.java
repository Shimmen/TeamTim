package teamtim.teamtimapp.presenter;

import java.util.List;

import teamtim.teamtimapp.database.CategoryWrapper;
import teamtim.teamtimapp.database.DatabaseInterface;
import teamtim.teamtimapp.database.MockDatabase;

public class CategoryPresenter {

    private DatabaseInterface db;

    public CategoryPresenter(){
        db = MockDatabase.getInstance();
    }

    public List<CategoryWrapper> getCategories() {
        return db.getCategories();
    }
}
