package cn.edu.nottingham.hnyzx3.recipebook.contentProviders;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class RecipeDatabase extends SQLiteOpenHelper {
    private static final String TAG = RecipeDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "recipeBook.db";

    /**
     * our all table names
     * currently it only has one table
     */
    interface Tables {
        String RECIPE = "recipe";
    }

    public RecipeDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.i(TAG, "Creating database");

        // Create a table to hold recipes
        db.execSQL("CREATE TABLE " + Tables.RECIPE + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RecipeContract.RecipeColumns.RECIPE_TITLE + " TEXT NOT NULL,"
                + RecipeContract.RecipeColumns.RECIPE_TIME + " TEXT NOT NULL,"
                + RecipeContract.RecipeColumns.RECIPE_INSTRUCTION + " TEXT NOT NULL,"
                + RecipeContract.RecipeColumns.RECIPE_INGREDIENTS + " TEXT NOT NULL,"
                + RecipeContract.RecipeColumns.RECIPE_VIDEO + " BLOB,"
                + RecipeContract.RecipeColumns.RECIPE_RATING + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
