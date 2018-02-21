package com.udacity.popularmovie.model;

import android.content.Context;

import com.udacity.popularmovie.R;

/**
 * Created by Antonio on 21/02/2018.
 */

public class DetailActivityModel {
    private String posterPath;
    private String rating;
    private String releaseDate;
    private String synopsis;

    public DetailActivityModel(Context context, Result result) {
        posterPath = result.getPosterPath();
        rating = result.getVoteAverage().toString();
        if (isBlank(rating)) {
            posterPath = context.getString(R.string.no_rating_movie);
        }
        releaseDate = result.getReleaseDate();
        if (isBlank(releaseDate)) {
            releaseDate = context.getString(R.string.no_release_date);
        }
        synopsis = result.getOverview();
        if (isBlank(synopsis)) {
            synopsis = context.getString(R.string.no_synopsis);
        }
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getSynopsis() {
        return synopsis;
    }

    private boolean isBlank(String s) {
        return (s == null || s.length() == 0);
    }
}
