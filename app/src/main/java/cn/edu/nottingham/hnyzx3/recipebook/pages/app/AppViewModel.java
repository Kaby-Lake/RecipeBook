package cn.edu.nottingham.hnyzx3.recipebook.pages.app;

import android.content.Context;

import androidx.databinding.Observable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Objects;

import cn.edu.nottingham.hnyzx3.recipebook.BR;
import cn.edu.nottingham.hnyzx3.recipebook.R;
import cn.edu.nottingham.hnyzx3.recipebook.components.recipeItem.RecipeItemViewModel;
import cn.edu.nottingham.hnyzx3.recipebook.contentProviders.RecipePerspective;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class AppViewModel extends ViewModel {

    private Context context;

    public AppViewModel(Context context) {
        this.context = context;
        sortByRating.addOnPropertyChangedCallback(new androidx.databinding.Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                loadRecipesFromDatabase();
            }
        });
    }

    /**
     * custom binder to the component inside the recycler view
     */
    public final ItemBinding<RecipeItemViewModel> itemBinding = ItemBinding.of(BR.recipe, R.layout.components_recipe_item);

    /**
     * if true, then sort by rating, if false, then sort by add time
     */
    public ObservableBoolean sortByRating = new ObservableBoolean(true);

    public final ObservableArrayList<RecipeItemViewModel> recipeList = new ObservableArrayList<>();

    public RecipeItemViewModel getRecipeById(Long id) {
        for (RecipeItemViewModel recipe : recipeList) {
            if (Objects.equals(recipe.id, id)) {
                return recipe;
            }
        }
        return null;
    }

    public void loadRecipesFromDatabase() {
        ArrayList<RecipeItemViewModel> recipes = RecipePerspective.getAll(context);

        if (this.sortByRating.get()) {
            recipes.sort((o1, o2) -> o2.rating.compareTo(o1.rating));
        } else {
            recipes.sort((o1, o2) -> o2.time.compareTo(o1.time));
        }

        recipeList.clear();

        recipeList.addAll(recipes);
    }
}