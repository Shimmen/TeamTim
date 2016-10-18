package teamtim.teamtimapp.database;

import teamtim.teamtimapp.activities.CategoryActivity;

public class CategoryWrapper {
    private String category;
    private int image;
    private float ratio;

    public CategoryWrapper(String category, int image, float ratio){
        this.category = category;
        this.image = image;
        this.ratio = ratio;
    }


    public String getCategory() {
        return category;
    }

    public int getImage() {
        return image;
    }

    public float getRatio() {
        return ratio;
    }
}
