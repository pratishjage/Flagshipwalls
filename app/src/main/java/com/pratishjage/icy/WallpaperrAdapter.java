package com.pratishjage.icy;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSources;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.button.MaterialButton;
import com.pratishjage.icy.Demo.WallpaperListner;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class WallpaperrAdapter extends RecyclerView.Adapter<WallpaperrAdapter.WallpaperHolder> {

    List<WallpaperData> wallpaperDataList;
    Context mContext;
    private int selectedWallpaperPostion = -1;
    private ProgressDialog progressDialog;
    private InterstitialAd mInterstitialAd;
    private WallpaperListner listner;

    public WallpaperrAdapter(List<WallpaperData> wallpaperDataList, Context context, WallpaperListner wallpaperListner) {
        this.wallpaperDataList = wallpaperDataList;
        this.mContext = context;
        this.listner = wallpaperListner;
    }

    @NonNull
    @Override
    public WallpaperHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaper_item, parent, false);
        return new WallpaperHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperHolder holder, final int position) {
        holder.wallpaperImg.setImageURI(wallpaperDataList.get(position).getImgurl());
       /* if (selectedWallpaperPostion == position) {
            holder.overlayLayout.setVisibility(View.VISIBLE);
        } else {
            holder.overlayLayout.setVisibility(View.GONE);
        }*/
        /*holder.setwallpaper_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WallAsync().execute(wallpaperDataList.get(position).getImgurl());
            }
        });*/
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return wallpaperDataList.size();

    }

    public static class WallpaperHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView wallpaperImg;
        RelativeLayout overlayLayout;
        MaterialButton setwallpaper_btn;

        public WallpaperHolder(@NonNull View itemView) {
            super(itemView);

            wallpaperImg = itemView.findViewById(R.id.wallpaper_img);
            overlayLayout = itemView.findViewById(R.id.overlay_layout);
            setwallpaper_btn = itemView.findViewById(R.id.setwallpaper_btn);

            wallpaperImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  //  selectedWallpaperPostion = getAdapterPosition();
                  //  notifyDataSetChanged();

                }
            });
            overlayLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                  //  selectedWallpaperPostion = -1;
                   // notifyDataSetChanged();
                }
            });
        }
    }


    private class WallAsync extends AsyncTask<String, Void, String> {
        public WallAsync() {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Setting Wallpaper");
            progressDialog.show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Uri uri = Uri.parse(s);
            final WallpaperManager myWallpaperManager
                    = WallpaperManager.getInstance(mContext);

            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setAutoRotateEnabled(true)
                    .build();

            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            final DataSource<CloseableReference<CloseableImage>>
                    dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);

            dataSource.subscribe(new BaseBitmapDataSubscriber() {

                @Override
                public void onNewResultImpl(@Nullable Bitmap bitmap) {
                    if (dataSource.isFinished() && bitmap != null) {
                        Log.d("Bitmap", "has come");
                        try {
                            myWallpaperManager.setBitmap(Bitmap.createBitmap(bitmap));
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        // Toast.makeText(mContext, "Wallpaper set", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        selectedWallpaperPostion = -1;
                        listner.onWallpaperSet("");

                        dataSource.close();
                    }
                }

                @Override
                public void onFailureImpl(DataSource dataSource) {
                    if (dataSource != null) {
                        dataSource.close();
                    }
                }
            }, CallerThreadExecutor.getInstance());
        }
    }


}
