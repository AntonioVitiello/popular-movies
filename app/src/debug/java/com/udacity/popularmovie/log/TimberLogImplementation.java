package com.udacity.popularmovie.log;

import timber.log.Timber;

/**
 * Created by Antonio on 05/03/2018.
 * See: https://medium.com/@caueferreira/timber-enhancing-your-logging-experience-330e8af97341
 */

public class TimberLogImplementation {
    public static void init(final String debugTag) {
        Timber.plant(new Timber.DebugTree() {
            @Override
            protected String createStackElementTag(StackTraceElement element) {
                return String.format("[%1$s][%4$s.%2$s(%3$s)]",
                        debugTag,
                        element.getMethodName(),
                        element.getLineNumber(),
                        super.createStackElementTag(element));
            }
        });
    }
}
