package com.hjt.mydouya.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by HJT on 2017/11/16.
 */

public class LogUtils {
    private static final String TAG = "JTWEIBO";

    public static void e(String message) {
        if (!TextUtils.isEmpty(message)) {
            Log.e(TAG, message);
        }
    }

    public static void d(String message) {
        if (!TextUtils.isEmpty(message)) {
            Log.d(TAG, message);
        }
    }

}
