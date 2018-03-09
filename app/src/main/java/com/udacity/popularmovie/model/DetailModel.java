package com.udacity.popularmovie.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.udacity.popularmovie.BR;
import com.udacity.popularmovie.MainApplication;
import com.udacity.popularmovie.R;
import com.udacity.popularmovie.net.json.movies.TmdbMovie;

/**
 * Created by Antonio on 21/02/2018.
 */

public class DetailModel extends BaseObservable {
    private String posterPath;
    private String backdropPath;
    private String rating;
    private String releaseDate;
    private String synopsis;
    private String title;
    private boolean isFavorite;

    public DetailModel(TmdbMovie movieData) {
        title = movieData.getTitle();
        posterPath = movieData.getPosterPath();
        backdropPath = movieData.getBackdropPath();

        rating = movieData.getVoteAverage().toString();
        if (isBlank(rating)) {
            rating = MainApplication.getStringResource(R.string.no_movie_rating);
        }

        releaseDate = movieData.getReleaseDate();
        if (isBlank(releaseDate)) {
            releaseDate = MainApplication.getStringResource(R.string.no_movie_release_date);
        }

        synopsis = movieData.getOverview();
        if (isBlank(synopsis)) {
            synopsis = MainApplication.getStringResource(R.string.no_movie_synopsis);
        }
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
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
        return (s == null || s.isEmpty());
    }

    public String getTitle() {
        return title;
    }

    @Bindable
    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
        notifyPropertyChanged(BR.favorite);
    }
}
