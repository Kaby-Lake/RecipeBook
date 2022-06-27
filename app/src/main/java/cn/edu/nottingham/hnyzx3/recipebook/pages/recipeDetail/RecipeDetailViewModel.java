package cn.edu.nottingham.hnyzx3.recipebook.pages.recipeDetail;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.material.textfield.TextInputEditText;

import cn.edu.nottingham.hnyzx3.recipebook.contentProviders.RecipePerspective;
import cn.edu.nottingham.hnyzx3.recipebook.models.RecipeModel;
import cn.edu.nottingham.hnyzx3.recipebook.utils.Toast;
import cn.edu.nottingham.hnyzx3.recipebook.utils.VideoUtil;

public class RecipeDetailViewModel extends AndroidViewModel {

    public RecipeDetailViewModel(Application application) {
        super(application);
    }

    public void loadRecipeDetailFromDatabase(long id) {

        RecipeModel recipeModel = RecipePerspective.getSingle(getApplication(), id);
        this.id.set(id);
        if (recipeModel != null) {
            this.title.set(recipeModel.title);
            this.instruction.set(recipeModel.instruction);
            this.ingredients.set(recipeModel.ingredients);
            this.rating.set(recipeModel.rating);
            this.videoUri.set(recipeModel.videoUri);
            if (recipeModel.videoUri != null) {
                videoPreview.set(VideoUtil.getThumbVideo(getApplication().getApplicationContext(), videoUri.get()));
            }
        } else {
            Toast.showToastShort(getApplication().getApplicationContext(), "Recipe not found");
        }
    }

    public ObservableBoolean invalidRatingInput = new ObservableBoolean(false);
    public ObservableBoolean haveModified = new ObservableBoolean(false);

    public ObservableField<Long> id = new ObservableField<>(null);
    public ObservableField<String> title = new ObservableField<>(null);
    public ObservableField<String> instruction = new ObservableField<>(null);
    public ObservableField<String> ingredients = new ObservableField<>(null);
    public ObservableField<Double> rating = new ObservableField<>(null);
    public ObservableField<Uri> videoUri = new ObservableField<>(null);
    public ObservableField<Bitmap> videoPreview = new ObservableField<>(null);


    // BindingAdapter for TextInputEditText to set the TextInputEditText with double
    @BindingAdapter("android:text")
    public static void setRating(TextInputEditText view, Double rating) {
        if (rating != null) {
            view.setText(String.valueOf(rating));
        } else {
            view.setText("");
        }
    }
}
