package com.udacity.popularmovie.event;

import com.udacity.popularmovie.net.json.reviews.TmdbReview;

import java.util.List;

/**
 * Created by Antonio on 06/03/2018.
 */

public class TmdbReviewsEvent {
    private final List<TmdbReview> mResults;

    public TmdbReviewsEvent(List<TmdbReview> results) {
        mResults = results;
    }

    public List<TmdbReview> getResults() {
        return mResults;
    }
}
