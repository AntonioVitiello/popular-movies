package com.udacity.popularmovie.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Antonio on 27/02/2018.
 */

public class FavoritesContract {
    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.udacity.popularmovie";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract, can be appended to BASE_CONTENT_URI to form valid URI's
    public static final String PATH_FAVORITES = "favorites";

    /* Contents of the favorites table */
    public static final class FavoritesEntry implements BaseColumns {

        // FavoriteEntry content URI = base content URI + path
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();

        // Favorite table name
        public static final String TABLE_NAME = "favorites";

        // Column names, "_ID" column automatically created
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
    }

}