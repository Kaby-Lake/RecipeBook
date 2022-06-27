package cn.edu.nottingham.hnyzx3.recipebook.pages.app;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import cn.edu.nottingham.hnyzx3.recipebook.R;
import cn.edu.nottingham.hnyzx3.recipebook.components.recipeItem.RecipeItemViewModel;
import cn.edu.nottingham.hnyzx3.recipebook.contentProviders.RecipePerspective;
import cn.edu.nottingham.hnyzx3.recipebook.databinding.PageAppBinding;
import cn.edu.nottingham.hnyzx3.recipebook.pages.newRecipe.NewRecipe;
import cn.edu.nottingham.hnyzx3.recipebook.pages.recipeDetail.RecipeDetail;
import cn.edu.nottingham.hnyzx3.recipebook.utils.Toast;

public class App extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    // viewModels
    AppViewModel app;


    // life cycle

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.i(TAG, "permission granted");
                } else {
                    Log.i(TAG, "permission not granted");
                    Toast.showToastShort(getApplicationContext(), "RecipeBook needs file read permission to access videos");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("RecipeBook");
        super.onCreate(savedInstanceState);
        PageAppBinding binding = DataBindingUtil.setContentView(this, R.layout.page_app);
        // find the cached view model, if doesn't exist, create a new one
        binding.setApp(app = new AppViewModel(this));
        binding.appRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.appRecyclerView.setNestedScrollingEnabled(false);

        // fetch recipes from database
        app.loadRecipesFromDatabase();

        if (ContextCompat.checkSelfPermission(
                this, READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "permission is granted");
        } else {
            Log.i(TAG, "granting permission");
            requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        // register check buttons click listeners
        ((MaterialButtonToggleGroup) findViewById(R.id.toggle_buttons)).addOnButtonCheckedListener(((group, checkedId, isChecked) -> {
            int checkedIndex = group.indexOfChild(findViewById(checkedId));
            if (checkedIndex == 0) {
                app.sortByRating.set(isChecked);
                Log.i(TAG, "sort by rating: " + app.sortByRating.get());
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        app.loadRecipesFromDatabase();
    }

    // click listeners

    public void onRecipeClick(View view) {
        Long id = (Long) view.getTag();
        Intent intent = new Intent(this, RecipeDetail.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void onRecipeDeleteClick(View view) {
        Long id = (Long) view.getTag();
        RecipeItemViewModel item = app.getRecipeById(id);
        new MaterialAlertDialogBuilder(this)
                .setTitle("Confirm Delete Recipe?")
                .setMessage(String.format("Are you sure you want to delete recipe %s?", item.title))
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (RecipePerspective.delete(this, id)) {
                        Snackbar.make(findViewById(R.id.app_root), String.format("Recipe %s has been deleted.", item.title), Snackbar.LENGTH_SHORT).show();
                        this.app.loadRecipesFromDatabase();
                    } else {
                        Toast.showToastShort(getApplicationContext(), "Failed to delete recipe");
                    }
                })
                .show();
    }

    // click listeners
    public void onNewRecipeClick(View view) {
        Intent intent = new Intent(this, NewRecipe.class);
        startActivity(intent);
    }
}