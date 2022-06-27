package cn.edu.nottingham.hnyzx3.recipebook.contentProviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import androidx.annotation.NonNull;

/**
 * provides a CRUD interface to manage the db.
 */
public class RecipeProvider extends ContentProvider {
    private RecipeDatabase recipeDatabase;

    // Checks for valid URIs
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int RECIPE = 1;

    private static final int RECIPE_ID = 2;


    static {
        // See if matches recipe records
        uriMatcher.addURI(RecipeContract.AUTHORITY, "recipe", RECIPE);

        // See if matches recipe item
        uriMatcher.addURI(RecipeContract.AUTHORITY, "recipe/#", RECIPE_ID);
    }


    @Override
    public boolean onCreate() {
        recipeDatabase = new RecipeDatabase(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case RECIPE:
                return RecipeContract.Recipe.CONTENT_TYPE_SINGLE;
            case RECIPE_ID:
                return RecipeContract.Recipe.CONTENT_TYPE_MULTIPLE;
            default:
                throw new IllegalArgumentException("Unknown URI : " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) == RECIPE) {
            // Create a new record
            long recordId = recipeDatabase.getWritableDatabase().insertOrThrow(RecipeDatabase.Tables.RECIPE, null, values);
            return RecipeContract.Recipe.buildRecipeUri(recordId);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) == RECIPE_ID) {
            String id = RecipeContract.Recipe.getRecipeId(uri);
            String selectionCriteria = String.format("%s = %s", BaseColumns._ID, id);
            if (!TextUtils.isEmpty(selection)) {
                selectionCriteria += String.format(" AND (%s)", selection);
            }
            return recipeDatabase.getWritableDatabase().delete(RecipeDatabase.Tables.RECIPE, selectionCriteria, selectionArgs);
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (uriMatcher.match(uri) == RECIPE_ID) {
            String id = RecipeContract.Recipe.getRecipeId(uri);
            String selectionCriteria = String.format("%s = %s", BaseColumns._ID, id);
            if (!TextUtils.isEmpty(selection)) {
                selectionCriteria += String.format(" AND (%s)", selection);
            }
            return recipeDatabase.getWritableDatabase().update(RecipeDatabase.Tables.RECIPE, values, selectionCriteria, selectionArgs);
        }
        return 0;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // we use SQLiteQueryBuilder instead of rawQuery() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(RecipeDatabase.Tables.RECIPE);

        if (uriMatcher.match(uri) == RECIPE_ID) {
            String id = RecipeContract.Recipe.getRecipeId(uri);
            queryBuilder.appendWhere(String.format("%s=%s", BaseColumns._ID, id));
        }

        return queryBuilder.query(recipeDatabase.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
    }
}
