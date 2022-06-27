package cn.edu.nottingham.hnyzx3.recipebook.contentProviders;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * RecipeContract is a class to simplify creation of URI.
 */
public class RecipeContract {
    interface RecipeColumns {
        String RECIPE_TIME = "recipe_time";
        String RECIPE_TITLE = "recipe_title";
        String RECIPE_INSTRUCTION = "recipe_instruction";
        String RECIPE_INGREDIENTS = "recipe_ingredients";
        String RECIPE_RATING = "recipe_rating";
        String RECIPE_VIDEO = "recipe_video";
    }

    // Used to access the content
    public static final String AUTHORITY = "cn.edu.nottingham.hnyzx3.recipebook.provider";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final Uri URI_TABLE = Uri.parse(BASE_URI.toString() + "/" + "recipe");

    // Table for recipe
    public static class Recipe implements RecipeColumns, BaseColumns {

        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendEncodedPath("recipe").build();

        public static final String CONTENT_TYPE_MULTIPLE = "vnd.android.cursor.item/vnd." + AUTHORITY + ".recipe";
        public static final String CONTENT_TYPE_SINGLE = "vnd.android.cursor.dir/vnd." + AUTHORITY + ".recipe";

        public static Uri buildRecipeUri(Long recipeId) {
            return CONTENT_URI.buildUpon().appendEncodedPath(String.valueOf(recipeId)).build();
        }

        public static String getRecipeId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
