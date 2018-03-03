package com.udacity.popularmovie.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.udacity.popularmovie.data.FavoritesContract.FavoritesEntry.TABLE_NAME;

/**
 * Created by Antonio on 27/02/2018.
 */

public class FavoritesProvider extends ContentProvider {
    // Constants for directory and single item of favorites table
    public static final int MATCH_FAVORITES = 100;
    public static final int MATCH_FAVORITE_WITH_ID = 101;

    // CDeclare a static variable for the Uri matcher that you construct
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavoritesDbHelper mFavoritesDbHelper;

    // buildUriMatcher associates URI's with their int match
    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoritesContract.AUTHORITY, FavoritesContract.PATH_FAVORITES, MATCH_FAVORITES);
        uriMatcher.addURI(FavoritesContract.AUTHORITY, FavoritesContract.PATH_FAVORITES + "/#", MATCH_FAVORITE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mFavoritesDbHelper = new FavoritesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Uri returnUri;
        final SQLiteDatabase db = mFavoritesDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case MATCH_FAVORITES:
                long id = db.insert(TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavoritesContract.FavoritesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver the favorite uri has been changed
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        final SQLiteDatabase db = mFavoritesDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        switch (match) {
            case MATCH_FAVORITES:
                cursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count;
        final SQLiteDatabase db = mFavoritesDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        // delete a single row of data by its row ID
        switch (match) {
            case MATCH_FAVORITES:
                count = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case MATCH_FAVORITE_WITH_ID:
                // selection = WHERE clause
                selection = "_id=?";
                // Get the favorite _id from the URI path
                selectionArgs = new String[]{uri.getPathSegments().get(1)};
                count = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change
        if (count != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
