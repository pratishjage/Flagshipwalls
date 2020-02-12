package com.flagshipwalls.app.Adapters;

import android.view.View;
import android.widget.ImageView;



import com.flagshipwalls.app.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

class ViewWallHolder extends RecyclerView.ViewHolder {

    ImageView wallpaperImg;
    CardView cardView;
    public ViewWallHolder(@NonNull View itemView) {
        super(itemView);

        wallpaperImg = itemView.findViewById(R.id.wallpaper_img);

        cardView= itemView.findViewById(R.id.cardview);
    }
}
