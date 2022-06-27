package cn.edu.nottingham.hnyzx3.recipebook.pages.newRecipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import cn.edu.nottingham.hnyzx3.recipebook.R;
import cn.edu.nottingham.hnyzx3.recipebook.contentProviders.RecipePerspective;
import cn.edu.nottingham.hnyzx3.recipebook.databinding.PageNewRecipeBinding;
import cn.edu.nottingham.hnyzx3.recipebook.utils.Toast;

public class NewRecipe extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    // viewModels
    NewRecipeViewModel recipeDetail;

    // lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("New Recipe");
        // display the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        PageNewRecipeBinding binding = DataBindingUtil.setContentView(this, R.layout.page_new_recipe);
        binding.setRecipe(recipeDetail = new NewRecipeViewModel(this));
    }

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

    @Override
    public boolean onSupportNavigateUp() {
        this.canSafelyFinish();
        return true;
    }

    @Override
    public void onBackPressed() {
        this.canSafelyFinish();
    }

    // click listeners

    public void onSaveClick(View view) {

        if (recipeDetail.title.get() == null) {
            Toast.showToastShort(this, "Recipe title can not be empty");
            return;
        }
        if (recipeDetail.instruction.get() == null) {
            Toast.showToastShort(this, "Recipe instruction can not be empty");
            return;
        }
        if (recipeDetail.ingredients.get() == null) {
            Toast.showToastShort(this, "Recipe ingredients can not be empty");
            return;
        }

        double rating;

        try {
            TextInputEditText ratingText = findViewById(R.id.new_recipe_rating);
            rating = Double.parseDouble(ratingText.getText().toString());
            if (rating <= 0 || rating > 5) {
                Toast.showToastShort(this, "Recipe rating must be between 0 and 5");
                return;
            }
        } catch (NumberFormatException e) {
            Toast.showToastShort(this, "Recipe rating must valid number between 0 and 5");
            return;
        }

        String recipeVideoPath = recipeDetail.videoUri.get() != null ? String.valueOf(recipeDetail.videoUri.get()) : null;

        if (RecipePerspective.insert(this, recipeDetail.title.get(), recipeDetail.instruction.get(), recipeDetail.ingredients.get(), rating, recipeVideoPath)) {
            Toast.showToastShort(this, "Recipe saved");
            finish();
        } else {
            Toast.showToastShort(this, "Error occurred while saving recipe");
        }
    }

    /**
     * here we use the ActivityResultLauncher() to delegate StartActivityForResult()
     * this provides a callback paradigm to handle the result provided by that page
     */
    ActivityResultLauncher<Intent> recipeVideoUploadResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    try {
                        recipeDetail.onVideoSelect(this, result.getData().getData());
                    } catch (Exception ignored) {
                    }
                }
            });

    public void onRecipeVideoUploadClick(View view) {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        recipeVideoUploadResultLauncher.launch(intent);
    }
}