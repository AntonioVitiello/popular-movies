package com.udacity.popularmovie;

import android.app.Application;

import com.util.Log;

/**
 * Created by Antonio on 20/02/2018.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.init(BuildConfig.DEBUG,
                getString(R.string.log_tag),
                getResources().getBoolean(R.bool.log_tag_concat));
    }

}
