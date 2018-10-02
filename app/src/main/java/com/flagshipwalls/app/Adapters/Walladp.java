package com.flagshipwalls.app.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


    public Walladp(@NonNull FirestorePagingOptions<WallpaperData> options, Context context) {
        super(options);
        this.context = context;
    }

    public Walladp(@NonNull FirestorePagingOptions<WallpaperData> options, Context context, LoadingListner loadingListner) {
        super(options);
        this.context = context;
        this.loadingListner = loadingListner;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewWallHolder viewWallHolder, int i, @NonNull final WallpaperData wallpaperData) {
        viewWallHolder.wallpaperImg.setImageURI(wallpaperData.getCompressed_imgurl());
                         /*holder.wallpaperImg.setController(
                               Fresco.newDraweeControllerBuilder()
                                        .setTapToRetryEnabled(true)
                                        .setUri(wallpaperData.getImgurl())
                                        .build());
*/
        viewWallHolder.wallpaperImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inteface.showWallpaperDialog(wallpaperData.getCompressed_imgurl(),wallpaperData.getImgurl());
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

                Log.d("LOADING", "LOADED");
                break;

            case FINISHED:

                Log.d("LOADING", "FINISHED");
                if (loadingListner != null) {

                        loadingListner.onLoadingFinished();

                }
                break;

            case ERROR:
                //previous load (either initial or additional) failed.  Call the retry() method to retry load.
                Log.d("LOADING", "LOADING error ");

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
}
