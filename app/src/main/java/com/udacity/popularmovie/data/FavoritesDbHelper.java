package com.udacity.popularmovie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.udacity.popularmovie.data.FavoritesContract.FavoritesEntry;

/**
 * Created by Antonio on 27/02/2018.
 */

public class FavoritesDbHelper extends SQLiteOpenHelper {
    // The name of the database
    private static final String DATABASE_NAME = "favorites.db";

    // Increment the database version on database schema changes
    private static final int VERSION = 1;

    FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Called when the database is created for the first time
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String sql = "CREATE TABLE " + FavoritesEntry.TABLE_NAME + " ("
                + FavoritesEntry._ID + " INTEGER PRIMARY KEY, "
                + FavoritesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                + FavoritesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, "
                + FavoritesEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + FavoritesEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, "
                + FavoritesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, "
                + FavoritesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                + FavoritesEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL);"
                + " UNIQUE (" + FavoritesEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(sql);
    }

    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoritesEntry.TABLE_NAME);
        onCreate(db);
    }
}
