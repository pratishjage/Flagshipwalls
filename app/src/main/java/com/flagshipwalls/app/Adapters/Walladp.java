package com.flagshipwalls.app.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.flagshipwalls.app.interfaces.IWallpaperActivity;

import com.flagshipwalls.app.R;
import com.flagshipwalls.app.beans.WallpaperData;
import com.flagshipwalls.app.interfaces.LoadingListner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Walladp extends FirestorePagingAdapter<WallpaperData, ViewWallHolder> {
    private IWallpaperActivity inteface;
    Context context;
    LoadingListner loadingListner;
    RequestOptions requestOptions;

    public Walladp(@NonNull FirestorePagingOptions<WallpaperData> options, Context context) {
        super(options);
        this.context = context;
    }

    public Walladp(@NonNull FirestorePagingOptions<WallpaperData> options, Context context, LoadingListner loadingListner) {
        super(options);
        this.context = context;
        this.loadingListner = loadingListner;
        requestOptions = new RequestOptions().placeholder(R.drawable.place).error(R.drawable.broken_img).centerCrop();
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewWallHolder viewWallHolder, int i, @NonNull final WallpaperData wallpaperData) {
        //  viewWallHolder.wallpaperImg.setImageURI(wallpaperData.getCompressed_imgurl());
                         /*holder.wallpaperImg.setController(
                               Fresco.newDraweeControllerBuilder()
                                        .setTapToRetryEnabled(true)
                                        .setUri(wallpaperData.getImgurl())
                                        .build());
*/

      /*  Glide.with(context).
                load(wallpaperData.getCompressed_imgurl()).into(viewWallHolder.wallpaperImg);
*/
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewWallHolder.wallpaperImg.getLayoutParams();
        if (i % 3 == 0) {
//2 colums will be displayed


            layoutParams.gravity = Gravity.CENTER;
            viewWallHolder.wallpaperImg.setLayoutParams(layoutParams);
            viewWallHolder.wallpaperImg.setPadding(getPX(32), getPX(48), getPX(32), getPX(48));
        } else {
            if (i % 2 == 0) {
                //image will be in bottom
                //move image to right
                layoutParams.gravity = Gravity.RIGHT;
                viewWallHolder.wallpaperImg.setPadding(getPX(32), getPX(32), 0, getPX(16));
            } else {
                //image will be in top
                // move image to left
                layoutParams.gravity = Gravity.LEFT;
                viewWallHolder.wallpaperImg.setPadding(0, getPX(32), getPX(16), 0);
            }
            viewWallHolder.wallpaperImg.setLayoutParams(layoutParams);

        }
        Glide.with(context)
                .load(wallpaperData.getCompressed_imgurl())
                .apply(requestOptions)
                .into(viewWallHolder.wallpaperImg);

        viewWallHolder.wallpaperImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inteface.showSetWallpaperActivity(wallpaperData.getCompressed_imgurl(), wallpaperData.getImgurl());
            }
        });


    }

    @NonNull
    @Override
    public ViewWallHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaper_item, parent, false);
        return new ViewWallHolder(view);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        super.onLoadingStateChanged(state);
        switch (state) {

            case LOADING_INITIAL:
                // initial load begun
                Log.d("LOADING", "LOADING INITIAL");

                break;

            case LOADING_MORE:
                //loading an additional page
                Log.d("LOADING", "LOADING MORE");

                break;
            case LOADED:

                Log.d("c", "LOADED");
                break;


            case ERROR:
                //previous load (either initial or additional) failed.  Call the retry() method to retry load.
                Log.e("LOADING", "LOADING error ");
                break;
            case FINISHED:

                Log.d("LOADING", "FINISHED");
                if (loadingListner != null) {
                    loadingListner.onLoadingFinished();
                }
                break;

        }

    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inteface = (IWallpaperActivity) context;
    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    private int getPX(int dp) {
        Resources r = context.getResources();

        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }
}
