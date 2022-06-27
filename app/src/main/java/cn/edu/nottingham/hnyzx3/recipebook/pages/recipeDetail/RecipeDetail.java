package cn.edu.nottingham.hnyzx3.recipebook.pages.recipeDetail;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import cn.edu.nottingham.hnyzx3.recipebook.R;
import cn.edu.nottingham.hnyzx3.recipebook.contentProviders.RecipePerspective;
import cn.edu.nottingham.hnyzx3.recipebook.databinding.PageRecipeDetailBinding;
import cn.edu.nottingham.hnyzx3.recipebook.pages.videoPlayer.VideoPlayer;
import cn.edu.nottingham.hnyzx3.recipebook.utils.Toast;

public class RecipeDetail extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    // viewModels
    RecipeDetailViewModel recipeDetail;

    // lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("Recipe Detail");
        // display the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        PageRecipeDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.page_recipe_detail);
        binding.setRecipe(recipeDetail = new RecipeDetailViewModel(getApplication()));

        long id = getIntent().getExtras().getLong("id");
        recipeDetail.loadRecipeDetailFromDatabase(id);

        // when the text changes, show the save button
        ((TextInputEditText) findViewById(R.id.recipe_detail_rating)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    double intermediate = Double.parseDouble(s.toString());
                    recipeDetail.invalidRatingInput.set(intermediate > 5 || intermediate < 0);
                    // if can parse, show dismiss the error hint
                } catch (NumberFormatException e) {
                    recipeDetail.invalidRatingInput.set(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                recipeDetail.haveModified.set(!s.toString().equals(recipeDetail.rating.get().toString()));
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        if (recipeDetail.haveModified.get()) {
            this.canSafelyFinish();
        } else {
            this.finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (recipeDetail.haveModified.get()) {
            this.canSafelyFinish();
        } else {
            super.onBackPressed();
        }
    }

    // if the user has modified the recipe, ask if they want to save
    private void canSafelyFinish() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Before going back?")
                .setMessage("Do you want to go back without saving the draft?")
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Yes", (dialog, which) -> {
                    finish();
                })
                .show();
    }


    // click listeners

    public void onSaveClick(View view) {
        String newRatingText = ((TextInputEditText) findViewById(R.id.recipe_detail_rating)).getText().toString();
        double newRating;
        try {
            newRating = Double.parseDouble(newRatingText);
        } catch (NumberFormatException e) {
            Toast.showToastShort(this, "Please enter a valid rating number");
            return;
        }
        if (newRating < 0 || newRating > 5) {
            Toast.showToastShort(this, "Recipe rating must be between 0 and 5");
            return;
        }
        if (RecipePerspective.updateRating(this, recipeDetail.id.get(), newRating)) {
            Toast.showToastShort(this, "Recipe rating updated");
        } else {
            Toast.showToastShort(this, "Error occurred when updating recipe rating");
        }
        this.finish();
    }

    public void onRecipeVideoClick(View view) {
        if (recipeDetail.videoUri.get() == null) {
            Toast.showToastShort(this, "No video available");
        } else {
            // open the video in a new activity
            Intent intent = new Intent(this, VideoPlayer.class);
            intent.putExtra("videoUri", recipeDetail.videoUri.get());
            startActivity(intent);
        }
    }

}