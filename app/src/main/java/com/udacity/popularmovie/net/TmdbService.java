package com.udacity.popularmovie.net;

import com.udacity.popularmovie.net.json.movies.TmdbMoviesContainer;
import com.udacity.popularmovie.net.json.reviews.TmdbReviewsContainer;
import com.udacity.popularmovie.net.json.trailers.TmdbTrailersContainer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Antonio on 20/02/2018.
 */

public interface TmdbService {

    @GET("movie/popular")
    Call<TmdbMoviesContainer> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<TmdbMoviesContainer> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("discover/movie?sort_by=revenue.desc&page=1")
    Call<TmdbMoviesContainer> getRevenueMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<TmdbTrailersContainer> getTrailers(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<TmdbReviewsContainer> getReviews(@Path("id") int id, @Query("api_key") String apiKey);
}
