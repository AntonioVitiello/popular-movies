package com.util;

/**
 * Created by Antonio on 20/02/2018.
 */

public class Log {
    private static String sLogTag = null;
    private static boolean sIsLogTagDefined = false;
    private static boolean sIsLogTagConcat = false;
    private static boolean sDebug = true;

    public static void init(String debugTag) {
        init(true, debugTag, true);
    }

    public static void init(boolean isDebugTime) {
        init(isDebugTime, null, false);
    }

    public static void init(boolean isDebugTime, String debugTag) {
        init(isDebugTime, debugTag, true);
    }

    public static void init(boolean isDebugTime, String debugTag, boolean concatDebugTagWithLogTag) {
        sDebug = isDebugTime;
        sIsLogTagDefined = debugTag != null;
        if (sIsLogTagDefined) {
            sLogTag = debugTag;
            sIsLogTagConcat = concatDebugTagWithLogTag;
        }
    }

    public static void v(String tag, String msg, Object... args) {
        if (sDebug) {
            String debugTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
            android.util.Log.v(debugTag, formatMessage(msg, args));
        }
    }

    public static void v(String tag, String msg, Throwable throwable, Object... args) {
        if (sDebug) {
            String debugTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
            android.util.Log.v(debugTag, formatMessage(msg, args), throwable);
        }
    }

    public static void d(String tag, String msg, Object... args) {
        if (sDebug) {
            String debugTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
            android.util.Log.d(debugTag, formatMessage(msg, args));
        }
    }

    public static void d(String tag, String msg, Throwable throwable, Object... args) {
        if (sDebug) {
            String debugTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
            android.util.Log.d(debugTag, formatMessage(msg, args), throwable);
        }
    }

    public static void i(String tag, String msg, Object... args) {
        if (sDebug) {
            String debugTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
            android.util.Log.i(debugTag, formatMessage(msg, args));
        }
    }

    public static void i(String tag, String msg, Throwable throwable, Object... args) {
        if (sDebug) {
            String debugTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
            android.util.Log.i(debugTag, formatMessage(msg, args), throwable);
        }
    }

    public static void w(String tag, String msg, Object... args) {
        if (sDebug) {
            String debugTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
            android.util.Log.w(debugTag, formatMessage(msg, args));
        }
    }

    public static void w(String tag, Throwable throwable) {
        if (sDebug) {
            String debugTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
            android.util.Log.w(debugTag, throwable);
        }
    }

    public static void w(String tag, String msg, Throwable throwable, Object... args) {
        if (sDebug) {
            String debugTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
            android.util.Log.w(debugTag, formatMessage(msg, args), throwable);
        }
    }

    public static void e(String tag, String msg, Object... args) {
        String debugTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
        android.util.Log.e(debugTag, formatMessage(msg, args));
    }

    public static void e(String tag, Throwable throwable) {
        String debugTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
        android.util.Log.e(debugTag, formatMessage(null), throwable);
    }

    public static void e(String tag, String msg, Throwable throwable, Object... args) {
        String debugTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
        android.util.Log.e(debugTag, formatMessage(msg, args), throwable);
    }

    public static void wtf(String tag, String msg, Object... args) {
        String debugTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
        android.util.Log.wtf(debugTag, formatMessage(msg, args));
    }

    public static void wtf(String tag, Throwable throwable) {
        String debugTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
        android.util.Log.wtf(debugTag, throwable);
    }

    public static void wtf(String tag, String msg, Throwable throwable, Object... args) {
        String debugTag = sIsLogTagDefined ? (sIsLogTagConcat ? sLogTag + tag : sLogTag) : tag;
        android.util.Log.wtf(debugTag, formatMessage(msg, args), throwable);
    }

    /* eg: String output = formatMessage("My name is: %s, age: %d", "Joe", 35);
        => output = "My name is: Joe, age: 35"
       see: https://dzone.com/articles/java-string-format-examples
            https://docs.oracle.com/javase/8/docs/api/index.html
     */
    private static String formatMessage(String msg, Object... args) {
        String message = msg;
        if (message == null) {
            message = "";
        } else if (args.length > 0) {
            message = String.format(message, args);
        }
        return message;
    }
}