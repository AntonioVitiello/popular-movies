package com.udacity.popularmovie.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.popularmovie.R;
import com.udacity.popularmovie.data.FavoritesContract;
import com.udacity.popularmovie.net.TmdbUtils;
import com.udacity.popularmovie.net.json.movies.TmdbMovie;

/**
 * Created by Antonio on 01/03/2018.
 */

public class FavoritesAdapter extends CursorAdapter {
    private static final String LOG_TAG = "FavoritesAdapter";
    private final LayoutInflater mInflater;
    private int columnTitleIndex;
    private int columnMovieIdIndex;
    private int columnOverviewIndex;
    private int columnPosterPathIndex;
    private int columnReleaseDateIndex;
    private int columnVoteAverageIndex;
    private int columnBackdropIndex;

    public FavoritesAdapter(Context context, Cursor cursor) {
        super(context, cursor, false);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor != null) {
            columnMovieIdIndex = newCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID);
            columnPosterPathIndex = newCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH);
            columnTitleIndex = newCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_TITLE);
            columnBackdropIndex = newCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_BACKDROP_PATH);
            columnOverviewIndex = newCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW);
            columnReleaseDateIndex = newCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE);
            columnVoteAverageIndex = newCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_VOTE_AVERAGE);
        }
        return super.swapCursor(newCursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View rootView = mInflater.inflate(R.layout.poster_item, parent, false);
        ViewHolder holder = new ViewHolder();
        holder.imageView = rootView.findViewById(R.id.poster_iv);
        rootView.setTag(holder);
        return rootView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        Uri posterUrl = TmdbUtils.buildImageUrl(cursor.getString(columnPosterPathIndex));
        Picasso.with(mContext)
                .load(posterUrl)
                .error(R.drawable.im_poster_placeholder_error)
                .placeholder(R.drawable.im_poster_placeholder)
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        TmdbUtils.checkConnection(mContext);
                    }
                });
    }

    @Override
    public int getCount() {
        return (mCursor != null ? mCursor.getCount() : 0);
    }

    public TmdbMovie buildResult(int position) {
        mCursor.moveToPosition(position);
        TmdbMovie result = new TmdbMovie();
        result.setId(mCursor.getInt(columnMovieIdIndex));
        result.setPosterPath(mCursor.getString(columnPosterPathIndex));
        result.setTitle(mCursor.getString(columnTitleIndex));
        result.setBackdropPath(mCursor.getString(columnBackdropIndex));
        result.setOverview(mCursor.getString(columnOverviewIndex));
        result.setReleaseDate(mCursor.getString(columnReleaseDateIndex));
        result.setVoteAverage(mCursor.getDouble(columnVoteAverageIndex));
        return result;
    }

    class ViewHolder {
        ImageView imageView;
    }

}
