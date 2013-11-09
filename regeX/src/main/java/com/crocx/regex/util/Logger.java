package com.crocx.regex.util;

import android.util.Log;

/**
 * Created by Croc on 9.11.2013.
 */
public class Logger {

    //    public static int logLevel = -1;
    public static String tag = "foo";

    public static void verbose(String message) {
        Log.v(tag, message);
    }

    public static void debug(String message) {
        Log.d(tag, message);
    }

    public static void info(String message) {
        Log.i(tag, message);
    }

    public static void warning(String message) {
        Log.w(tag, message);
    }

    public static void error(String message) {
        Log.e(tag, message);
    }
}
