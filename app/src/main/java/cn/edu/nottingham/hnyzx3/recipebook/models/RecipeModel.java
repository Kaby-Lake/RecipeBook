package cn.edu.nottingham.hnyzx3.recipebook.models;

import android.net.Uri;

public class RecipeModel {

    public RecipeModel(String title, String instruction, String ingredients, Double rating, Uri video) {
        this.title = title;
        this.instruction = instruction;
        this.ingredients = ingredients;
        this.rating = rating;
        this.videoUri = video;
    }

    public String title;
    public String instruction;
    public String ingredients;
    public Double rating;

    /**
     * videoUri can be null
     */
    public Uri videoUri = null;
}
