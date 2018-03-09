package com.udacity.popularmovie.net.json.reviews;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Antonio on 26/02/2018.
 */

public class TmdbReviewsContainer {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("results")
    @Expose
    private List<TmdbReview> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TmdbReview> getResults() {
        return results;
    }

    public void setResults(List<TmdbReview> results) {
        this.results = results;
    }

}
