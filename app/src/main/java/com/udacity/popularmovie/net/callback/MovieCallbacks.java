package com.udacity.popularmovie.net.callback;

import com.udacity.popularmovie.event.TmdbMovieEvent;
import com.udacity.popularmovie.net.TmdbUtils;
import com.udacity.popularmovie.net.json.movies.TmdbMovie;
import com.udacity.popularmovie.net.json.movies.TmdbMoviesContainer;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Antonio on 05/03/2018.
 */

public class MovieCallbacks implements Callback<TmdbMoviesContainer> {
    @Override
    public void onResponse(Call<TmdbMoviesContainer> call, Response<TmdbMoviesContainer> response) {
        HttpUrl url = call.request().url();
        Timber.d("HTTP Request: %s", url.toString());
        if (response.isSuccessful()) {
            int serviceType = TmdbUtils.getServiceType(url.pathSegments());
            List<TmdbMovie> results = response.body().getResults();
            EventBus.getDefault().post(new TmdbMovieEvent(results, serviceType));
        } else {
            // handle request errors depending on status code...
            int statusCode = response.code();
            Timber.d("Received HTTP %d on %s", statusCode, url.toString());
        }
    }

    @Override
    public void onFailure(Call<TmdbMoviesContainer> call, Throwable thr) {
        Timber.e(thr, "Error loading JSON from TMDb");
    }
}
