package com.udacity.popularmovie.net.callback;

import com.udacity.popularmovie.event.TmdbTrailersEvent;
import com.udacity.popularmovie.net.json.trailers.TmdbTrailersContainer;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Antonio on 06/03/2018.
 */

public class TrailersCallback implements Callback<TmdbTrailersContainer> {
    @Override
    public void onResponse(Call<TmdbTrailersContainer> call, Response<TmdbTrailersContainer> response) {
        Timber.d("TMDb Request URL: %s", call.request().url());
        if (response.isSuccessful()) {
            TmdbTrailersContainer trailersContainer = response.body();
            EventBus.getDefault().post(new TmdbTrailersEvent(trailersContainer.getResults()));
        } else {
            // handle request errors depending on status code...
            int statusCode = response.code();
            Timber.d("Received HTTP %d on TMDb trailers data request.", statusCode);
        }
    }

    @Override
    public void onFailure(Call<TmdbTrailersContainer> call, Throwable thr) {
        Timber.e(thr, "Error loading JSON from TMDb");
    }
}
