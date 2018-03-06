package com.udacity.popularmovie.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.popularmovie.R;
import com.udacity.popularmovie.net.TmdbUtils;
import com.udacity.popularmovie.net.json.movies.TmdbMovie;

import java.util.List;

/**
 * Created by Antonio on 20/02/2018.
 */

public class PostersAdapter extends ArrayAdapter<TmdbMovie> {
    private final LayoutInflater mInflater;

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context The current context. Used to inflate the layout file.
     * @param tmdbMovieData A List of TmdbMovie objects to display in a list
     */
    public PostersAdapter(Activity context, List<TmdbMovie> tmdbMovieData) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, tmdbMovieData);
        mInflater = LayoutInflater.from(getContext());
    }

    public void updateData(List<TmdbMovie> tmdbMovieData) {
        clear();
        addAll(tmdbMovieData);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     *                    (search online for "android view recycling" to learn more)
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the TmdbMovie object from the ArrayAdapter at the appropriate position
        TmdbMovie tmdbMovie = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.poster_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Uri posterUrl = TmdbUtils.buildImageUrl(tmdbMovie.getPosterPath());
        Picasso.with(getContext())
                .load(posterUrl)
                .error(R.drawable.im_poster_placeholder_error)
                .placeholder(R.drawable.im_poster_placeholder)
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        TmdbUtils.checkConnection(getContext());
                    }
                });

        return convertView;
    }


    public static class ViewHolder {
        private ImageView imageView;

        public ViewHolder(View view) {
            imageView = view.findViewById(R.id.poster_iv);
        }
    }

}
