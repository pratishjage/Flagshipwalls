package com.flagshipwalls.app.Adapters;

import android.view.View;
import android.widget.ImageView;



import com.flagshipwalls.app.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class ViewWallHolder extends RecyclerView.ViewHolder {

    ImageView wallpaperImg;
    public ViewWallHolder(@NonNull View itemView) {
        super(itemView);

        wallpaperImg = itemView.findViewById(R.id.wallpaper_img);

    }
}
