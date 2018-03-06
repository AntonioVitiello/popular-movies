package com.udacity.popularmovie.event;

import com.udacity.popularmovie.net.json.movies.TmdbMovie;

import java.util.List;

/**
 * Created by Antonio on 05/03/2018.
 */

public class TmdbMovieEvent {
    private final List<TmdbMovie> mResults;
    private final int mServiceType;

    public TmdbMovieEvent(List<TmdbMovie> results, int serviceType) {
        mResults = results;
        mServiceType = serviceType;
    }

    public List<TmdbMovie> getResults() {
        return mResults;
    }

    public int getServiceType() {
        return mServiceType;
    }
}
