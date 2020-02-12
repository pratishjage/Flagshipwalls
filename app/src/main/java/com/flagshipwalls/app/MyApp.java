package com.flagshipwalls.app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.fabric.sdk.android.Fabric;

public class MyApp extends Application {
    public static final String CHANNEL_1_ID = "weeklyWallpaper";
    public static final String CHANNEL_2_ID = "newdevice";

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        createNotificationChannels();
    }


    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Wallpaperoftheweek",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Weekly Wallpaper");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "newdevice",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("New Device Wallpapers");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }
}
