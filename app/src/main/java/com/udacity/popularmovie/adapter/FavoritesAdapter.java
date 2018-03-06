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
    private final LayoutInflater mInflater;
    private int titleColumnIndex;
    private int movieIdColumnIndex;
    private int overviewColumnIndex;
    private int posterPathColumnIndex;
    private int releaseDateColumnIndex;
    private int voteAverageColumnIndex;
    private int backdropColumnIndex;

    public FavoritesAdapter(Context context, Cursor cursor) {
        super(context, cursor, false);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor != null) {
            movieIdColumnIndex = newCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID);
            posterPathColumnIndex = newCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH);
            titleColumnIndex = newCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_TITLE);
            backdropColumnIndex = newCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_BACKDROP_PATH);
            overviewColumnIndex = newCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW);
            releaseDateColumnIndex = newCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE);
            voteAverageColumnIndex = newCursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_VOTE_AVERAGE);
        }
        return super.swapCursor(newCursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View rootView = mInflater.inflate(R.layout.poster_item, parent, false);
        ViewHolder holder = new ViewHolder(rootView);
        rootView.setTag(holder);
        return rootView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        Uri posterUrl = TmdbUtils.buildImageUrl(cursor.getString(posterPathColumnIndex));
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
        result.setId(mCursor.getInt(movieIdColumnIndex));
        result.setPosterPath(mCursor.getString(posterPathColumnIndex));
        result.setTitle(mCursor.getString(titleColumnIndex));
        result.setBackdropPath(mCursor.getString(backdropColumnIndex));
        result.setOverview(mCursor.getString(overviewColumnIndex));
        result.setReleaseDate(mCursor.getString(releaseDateColumnIndex));
        result.setVoteAverage(mCursor.getDouble(voteAverageColumnIndex));
        return result;
    }

    public static class ViewHolder {
        private final ImageView imageView;

        public ViewHolder(View rootView) {
            imageView = rootView.findViewById(R.id.poster_iv);
        }
    }

}
