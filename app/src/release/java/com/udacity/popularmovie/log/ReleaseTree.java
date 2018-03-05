package com.udacity.popularmovie.log;

import timber.log.Timber;

/**
 * Created by Antonio on 05/03/2018.
 */

public class ReleaseTree extends Timber.Tree {
    private final String mTag;

    public ReleaseTree(String tag) {
        mTag = tag;
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
//        DO NOTHING...
//        if (priority == ERROR || priority == WARNING)
//            YourCrashLibrary.log(priority, tag, message);
    }
}
