package com.udacity.popularmovie.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

import com.udacity.popularmovie.R;

/**
 * Created by Antonio on 20/02/2018.
 */

public class TmdbUtils {
    public final static String TMDB_BASE_URL = "https://api.themoviedb.org/3/";
    public final static String TMDB_DISCOVER_MOVIE_PATH = "discover/movie";
    public final static String TMDB_SORT_BY_POPULARITY = "popularity.desc";
    public final static String TMDB_POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    private static final String LOG_TAG = "TmdbUtils";
    private static Toast mToast;

    public static TmdbService getService() {
        return RetrofitClient.getClient(TMDB_BASE_URL).create(TmdbService.class);
    }

    public static Uri buildImageUrl(String encodedPath) {
        Uri posterUri = Uri.parse(TmdbUtils.TMDB_POSTER_BASE_URL)
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
        Boolean isConnected = isConnected(context);
        String msg = context.getString(R.string.no_connection_msg);
        if(!isConnected){
            if(mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            mToast.show();
        }
        return isConnected;
    }

}
