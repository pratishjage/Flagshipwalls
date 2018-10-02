package com.flagshipwalls.app.Adapters;

import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.button.MaterialButton;
import com.flagshipwalls.app.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ViewWallHolder extends RecyclerView.ViewHolder {

    SimpleDraweeView wallpaperImg;
    RelativeLayout overlayLayout;
    MaterialButton setwallpaper_btn;

    public ViewWallHolder(@NonNull View itemView) {
        super(itemView);

        wallpaperImg = itemView.findViewById(R.id.wallpaper_img);
        overlayLayout = itemView.findViewById(R.id.overlay_layout);
        setwallpaper_btn = itemView.findViewById(R.id.setwallpaper_btn);
    }
}
