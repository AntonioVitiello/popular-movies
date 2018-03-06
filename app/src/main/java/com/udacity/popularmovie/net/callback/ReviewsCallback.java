package com.udacity.popularmovie.net.callback;

import com.udacity.popularmovie.event.TmdbReviewsEvent;
import com.udacity.popularmovie.net.json.reviews.TmdbReview;
import com.udacity.popularmovie.net.json.reviews.TmdbReviewsContainer;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Antonio on 06/03/2018.
 */

public class ReviewsCallback implements Callback<TmdbReviewsContainer> {
    @Override
    public void onResponse(Call<TmdbReviewsContainer> call, Response<TmdbReviewsContainer> response) {
        Timber.d("TMDb Request URL: %s", call.request().url());
        if (response.isSuccessful()) {
            TmdbReviewsContainer reviewsContainer = response.body();
            List<TmdbReview> results = reviewsContainer.getResults();
            EventBus.getDefault().post(new TmdbReviewsEvent(results));
        } else {
            // handle request errors depending on status code...
            int statusCode = response.code();
            Timber.d("Received HTTP %d on TMDb reviews data request.", statusCode);
        }
    }

    @Override
    public void onFailure(Call<TmdbReviewsContainer> call, Throwable thr) {
        Timber.e(thr, "Error loading JSON from TMDb");
    }
}
