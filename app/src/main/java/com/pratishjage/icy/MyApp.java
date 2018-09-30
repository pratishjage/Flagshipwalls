package com.pratishjage.icy;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.ads.MobileAds;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        MobileAds.initialize(this, getString(R.string.admob_app_id));
    }
}
