package com.flagshipwalls.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppConstants {
    public static final String WALLURL = "wallurl";
    public static final String DOWNLOAD_URL = "download_wallpaper_url";
    public static String INTENT_WHERE_TAG = "intent_where_tag";
    public static String INTENT_WHERE_VALUE = "intent_where_value";
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
