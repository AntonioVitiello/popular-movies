package com.udacity.popularmovie.log;

import timber.log.Timber;

/**
 * Created by Antonio on 05/03/2018.
 */

public class TimberLogImplementation {
    public static void init(String tag) {
        Timber.plant(new ReleaseTree(tag));
    }
}
