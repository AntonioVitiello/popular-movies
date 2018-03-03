package com.udacity.popularmovie.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

import com.udacity.popularmovie.BuildConfig;
import com.udacity.popularmovie.MainApplication;
import com.udacity.popularmovie.R;
import com.udacity.popularmovie.net.json.movies.TmdbMoviesContainer;
import com.udacity.popularmovie.net.json.reviews.TmdbReviewsContainer;
import com.udacity.popularmovie.net.json.trailers.TmdbTrailersContainer;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.udacity.popularmovie.MainActivityFragment.TYPE_MOST_POPULAR;
import static com.udacity.popularmovie.MainActivityFragment.TYPE_TOP_RATED;


/**
 * Created by Antonio on 20/02/2018.
 */

public class TmdbUtils {
    private static final String LOG_TAG = "TmdbUtils";
    private static TmdbUtils sInstance;
    private static Toast mToast;
    public String mTmdbPosterBaseUrl;
    private String mTmdbBaseUrl;
    private TmdbService mService;

    private TmdbUtils() {
    }

    public static final TmdbUtils getInstance() {
        if (sInstance == null) {
            synchronized (TmdbUtils.class) {
                if (sInstance == null) {
                    sInstance = new TmdbUtils();
                    sInstance.mTmdbBaseUrl = MainApplication.getStringResource(R.string.tmdm_base_url);
                    sInstance.mTmdbPosterBaseUrl = MainApplication.getStringResource(R.string.tmdm_poster_base_url);
                    sInstance.mService = sInstance.getClient(sInstance.mTmdbBaseUrl).create(TmdbService.class);
                }
            }
        }
        return sInstance;
    }

    public static Uri buildImageUrl(String encodedPath) {
        Uri posterUri = Uri.parse(getInstance().mTmdbPosterBaseUrl)
                .buildUpon()
                .appendEncodedPath(encodedPath)
                .build();
        return posterUri;
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    public static boolean isConnected(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean checkConnection(Context context) {
        boolean isConnected = isConnected(context);
        if (!isConnected) {
            if (mToast != null) {
                mToast.cancel();
            }
            String msg = context.getString(R.string.no_connection_msg);
            mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            mToast.show();
        }
        return isConnected;
    }

    public static void trailersRequest(int movieId, Callback<TmdbTrailersContainer> trailersCallback) {
        getInstance().mService.getTrailers(movieId, BuildConfig.TMDB_API_KEY).enqueue(trailersCallback);
    }

    public static void reviewsRequest(int movieId, Callback<TmdbReviewsContainer> reviewsCallback) {
        getInstance().mService.getReviews(movieId, BuildConfig.TMDB_API_KEY).enqueue(reviewsCallback);
    }

    public static void mostPopularRequest(Callback<TmdbMoviesContainer> tmdbCallback) {
        getInstance().mService.getPopularMovies(BuildConfig.TMDB_API_KEY).enqueue(tmdbCallback);
    }

    public static void topRatedRequest(Callback<TmdbMoviesContainer> tmdbCallback) {
        getInstance().mService.getTopRatedMovies(BuildConfig.TMDB_API_KEY).enqueue(tmdbCallback);
    }

    public static void revenueRequest(Callback<TmdbMoviesContainer> tmdbCallback) {
        getInstance().mService.getRevenueMovies(BuildConfig.TMDB_API_KEY).enqueue(tmdbCallback);
    }

    public static int getServiceType(List<String> pathSegments) {
        int serviceType = -1;
        int index = pathSegments.size() - 1;
        String p2 = pathSegments.get(index);
        String p1 = pathSegments.get(--index);

        if (p1.equals("movie")) {
            if (p2.equals("popular")) {
                // path is: movie/popular
                return TYPE_MOST_POPULAR;
            } else if (p2.equals("top_rated")) {
                // path is: movie/top_rated
                return TYPE_TOP_RATED;
            }
        }

        return serviceType;
    }

    private Retrofit getClient(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
