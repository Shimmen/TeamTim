package teamtim.teamtimapp.Presenter;

import java.util.List;

import teamtim.teamtimapp.database.DatabaseInterface;
import teamtim.teamtimapp.database.MockDatabase;

public class CategoryPresenter {

    private DatabaseInterface db;

    public CategoryPresenter(){
        db = MockDatabase.getInstance();
    }

    public List<String> getCategories() {
        return db.getCategories();
    }
}
