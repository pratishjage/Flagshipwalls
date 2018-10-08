package com.flagshipwalls.app;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;
import io.fabric.sdk.android.Fabric;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        MobileAds.initialize(this, getString(R.string.admob_app_id));
    }
}
