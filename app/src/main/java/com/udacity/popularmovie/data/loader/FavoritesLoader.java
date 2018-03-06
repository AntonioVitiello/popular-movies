package com.udacity.popularmovie.data.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.udacity.popularmovie.adapter.FavoritesAdapter;
import com.udacity.popularmovie.data.FavoritesContract;

/**
 * Created by Antonio on 01/03/2018.
 */

public class FavoritesLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int FAVORITES_LOADER_ID = 0;
    private Context mContext;
    private FavoritesAdapter mFavoritesAdapter;
    private final String[] mProjection = new String[]{
            FavoritesContract.FavoritesEntry._ID,
            FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID,
            FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH,
            FavoritesContract.FavoritesEntry.COLUMN_TITLE,
            FavoritesContract.FavoritesEntry.COLUMN_BACKDROP_PATH,
            FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW,
            FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE,
            FavoritesContract.FavoritesEntry.COLUMN_VOTE_AVERAGE};


    public FavoritesLoader(Context context, FavoritesAdapter adapter) {
        mContext = context;
        mFavoritesAdapter = adapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case FAVORITES_LOADER_ID:
                Uri favoritesUri = FavoritesContract.FavoritesEntry.CONTENT_URI;
                return new CursorLoader(mContext, favoritesUri, mProjection, null, null, null);
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mFavoritesAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavoritesAdapter.swapCursor(null);
    }

}