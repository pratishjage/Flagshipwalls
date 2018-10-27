package com.flagshipwalls.app.interfaces;

public interface IWallpaperActivity {
     void inflateQueryWallpaperFragment(String whereTag, String whereValue);

    void showSetWallpaperActivity(String wallurl, String downloadurl);
}
