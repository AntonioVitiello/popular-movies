package com.udacity.popularmovie.net;

import com.udacity.popularmovie.model.TmdbResponseData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Antonio on 20/02/2018.
 */

public interface TmdbService {

    @GET("movie/popular")
    Call<TmdbResponseData> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<TmdbResponseData> getTopRatedMovies(@Query("api_key") String apiKey);

    // final URL will be eg: https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&page=1&api_key=<<API-KEY>>
    @GET("discover/movie?sort_by=popularity.desc&page=1")
    Call<TmdbResponseData> getDiscoverPopularMovies(@Query("api_key") String apiKey);

    @GET("discover/movie?sort_by=toprated.desc&page=1")
    Call<TmdbResponseData> getDiscoverTopRatedMovies(@Query("api_key") String apiKey);

    @GET("discover/movie?sort_by=revenue.desc&page=1")
    Call<TmdbResponseData> getFavoriteMovies(@Query("api_key") String apiKey);

    @GET("discover/movie?sort_by=original_title.desc&page=1")
    Call<TmdbResponseData> getTitleMovies(@Query("api_key") String apiKey);

    @GET("discover/movie?sort_by=vote_average.desc&page=1")
    Call<TmdbResponseData> getTopVotedMovies(@Query("api_key") String apiKey);

    @GET("discover/movie?sort_by=vote_count.desc&page=1")
    Call<TmdbResponseData> getMostVotedMovies(@Query("api_key") String apiKey);

}
