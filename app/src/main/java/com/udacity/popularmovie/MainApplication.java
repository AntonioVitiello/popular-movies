package com.udacity.popularmovie;

import android.app.Application;
import android.content.res.Resources;

import com.util.Log;

/**
 * Created by Antonio on 20/02/2018.
 */

public class MainApplication extends Application {

    private static Resources mResources;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the logger
        Log.init(BuildConfig.DEBUG,
                getString(R.string.log_tag),
                getResources().getBoolean(R.bool.log_tag_concat));

        mResources = this.getApplicationContext().getResources();
    }

    public static String getStringResource(int resId) {
        return mResources.getString(resId);
    }

    public static String getStringResource(int resId, Object... formatArgs) {
        return mResources.getString(resId, formatArgs);
    }

    public static int getIntResource(int resId) {
        return mResources.getInteger(resId);
    }
}
