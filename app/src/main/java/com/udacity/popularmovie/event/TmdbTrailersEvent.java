package com.udacity.popularmovie.event;

import com.udacity.popularmovie.net.json.trailers.TmdbTrailers;

import java.util.List;

/**
 * Created by Antonio on 06/03/2018.
 */

public class TmdbTrailersEvent {
    private final List<TmdbTrailers> mResults;

    public TmdbTrailersEvent(List<TmdbTrailers> results) {
        mResults = results;
    }

    public List<TmdbTrailers> getResults() {
        return mResults;
    }

}
