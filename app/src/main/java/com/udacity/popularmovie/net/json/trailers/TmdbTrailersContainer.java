package com.udacity.popularmovie.net.json.trailers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Antonio on 23/02/2018.
 */

public class TmdbTrailersContainer {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<TmdbTrailers> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TmdbTrailers> getResults() {
        return results;
    }

    public void setResults(List<TmdbTrailers> results) {
        this.results = results;
    }

}