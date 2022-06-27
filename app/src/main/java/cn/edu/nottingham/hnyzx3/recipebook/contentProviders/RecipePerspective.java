package cn.edu.nottingham.hnyzx3.recipebook.contentProviders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Date;

import cn.edu.nottingham.hnyzx3.recipebook.components.recipeItem.RecipeItemViewModel;
import cn.edu.nottingham.hnyzx3.recipebook.models.RecipeModel;

public class RecipePerspective {
    public static ArrayList<RecipeItemViewModel> getAll(Context context) {
        Cursor mCursor;

        String[] projection = {
                BaseColumns._ID,
                RecipeContract.Recipe.RECIPE_TITLE,
                RecipeContract.Recipe.RECIPE_TIME,
                RecipeContract.Recipe.RECIPE_INSTRUCTION,
                RecipeContract.Recipe.RECIPE_RATING};

        ArrayList<RecipeItemViewModel> entries = new ArrayList<>();

        mCursor = context.getContentResolver().query(RecipeContract.URI_TABLE, projection, null, null, null);

        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                do {
                    long _id = mCursor.getLong(mCursor.getColumnIndex(BaseColumns._ID));
                    String title = mCursor.getString(mCursor.getColumnIndex(RecipeContract.Recipe.RECIPE_TITLE));
                    String instruction = mCursor.getString(mCursor.getColumnIndex(RecipeContract.Recipe.RECIPE_INSTRUCTION));
                    String rating = mCursor.getString(mCursor.getColumnIndex(RecipeContract.Recipe.RECIPE_RATING));
                    Date date = new Date(mCursor.getString(mCursor.getColumnIndex(RecipeContract.Recipe.RECIPE_TIME)));
                    RecipeItemViewModel recipeItemViewModel = new RecipeItemViewModel(_id, title, instruction, rating, date);
                    entries.add(recipeItemViewModel);
                } while (mCursor.moveToNext());
            }
        }
        return entries;
    }

    public static RecipeModel getSingle(Context context, long id) {
        Cursor mCursor;

        String[] projection = {
                BaseColumns._ID,
                RecipeContract.Recipe.RECIPE_TITLE,
                RecipeContract.Recipe.RECIPE_INSTRUCTION,
                RecipeContract.Recipe.RECIPE_RATING,
                RecipeContract.Recipe.RECIPE_INGREDIENTS,
                RecipeContract.Recipe.RECIPE_VIDEO};

        mCursor = context.getContentResolver().query(RecipeContract.URI_TABLE, projection, BaseColumns._ID + " = " + id, null, null);

        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                String title = mCursor.getString(mCursor.getColumnIndex(RecipeContract.Recipe.RECIPE_TITLE));
                String instruction = mCursor.getString(mCursor.getColumnIndex(RecipeContract.Recipe.RECIPE_INSTRUCTION));
                String ingredients = mCursor.getString(mCursor.getColumnIndex(RecipeContract.Recipe.RECIPE_INGREDIENTS));
                Double rating = mCursor.getDouble(mCursor.getColumnIndex(RecipeContract.Recipe.RECIPE_RATING));
                Uri video = null;
                try {
                    video = Uri.parse(mCursor.getString(mCursor.getColumnIndex(RecipeContract.Recipe.RECIPE_VIDEO)));
                } catch (Exception ignored) {
                }
                return new RecipeModel(title, instruction, ingredients, rating, video);
            }
        }
        return null;
    }

    public static boolean insert(Context context, String title, String instruction, String ingredients, Double rating, String optionalVideo) {
        ContentValues values = new ContentValues();
        values.put(RecipeContract.Recipe.RECIPE_TIME, new Date().toString());
        values.put(RecipeContract.Recipe.RECIPE_TITLE, title);
        values.put(RecipeContract.Recipe.RECIPE_INSTRUCTION, instruction);
        values.put(RecipeContract.Recipe.RECIPE_INGREDIENTS, ingredients);
        values.put(RecipeContract.Recipe.RECIPE_RATING, rating);
        if (optionalVideo != null) {
            values.put(RecipeContract.Recipe.RECIPE_VIDEO, optionalVideo);
        }
        return context.getContentResolver().insert(RecipeContract.URI_TABLE, values) != null;
    }

    public static boolean delete(Context context, long id) {
        try {
            Uri uri = RecipeContract.Recipe.buildRecipeUri(id);
            context.getContentResolver().delete(uri, null, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean updateRating(Context context, long id, Double newRating) {
        try {
            ContentValues values = new ContentValues();
            values.put(RecipeContract.Recipe.RECIPE_RATING, newRating);
            Uri uri = RecipeContract.Recipe.buildRecipeUri(id);
            context.getContentResolver().update(uri, values, null, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
