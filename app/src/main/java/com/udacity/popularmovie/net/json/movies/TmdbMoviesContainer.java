package com.udacity.popularmovie.net.json.movies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Antonio on 20/02/2018.
 */

public class TmdbMoviesContainer {
    @SerializedName("results")
    @Expose
    private List<TmdbMovie> results = null;

    public List<TmdbMovie> getResults() {
        return results;
    }

    public void setResults(List<TmdbMovie> results) {
        this.results = results;
    }

}
