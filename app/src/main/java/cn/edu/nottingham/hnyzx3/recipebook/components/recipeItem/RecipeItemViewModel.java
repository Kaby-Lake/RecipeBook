package cn.edu.nottingham.hnyzx3.recipebook.components.recipeItem;

import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import java.util.Date;

public class RecipeItemViewModel {

    public RecipeItemViewModel(Long _id, String title, String instructions, String rating, Date time) {
        this.id = _id;
        this.title = title;
        this.instruction = instructions;
        this.rating = Double.parseDouble(rating);
        this.time = time;
    }

    public Long id;
    public String title;
    public String instruction;
    public Double rating;
    public Date time;

    // BindingAdapter for TextInputEditText to set the TextInputEditText with double
    @BindingAdapter("android:text")
    public static void setRecipeItemRating(TextView view, Double rating) {
        if (rating != null) {
            view.setText(String.valueOf(rating));
        } else {
            view.setText("");
        }
    }
}
