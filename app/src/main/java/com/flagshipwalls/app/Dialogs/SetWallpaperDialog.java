package com.flagshipwalls.app.Dialogs;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.flagshipwalls.app.utils.AppConstants;
import com.flagshipwalls.app.interfaces.WallpaperListner;
import com.flagshipwalls.app.R;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SetWallpaperDialog extends BottomSheetDialogFragment {
    public SetWallpaperDialog() {
        // Required empty public constructor
    }

    WallpaperListner listner;

    @SuppressLint("ValidFragment")
    public SetWallpaperDialog(WallpaperListner listner) {
        this.listner = listner;
    }

    String wallurl, downloadurl;
    SimpleDraweeView wallimg;
    private ProgressDialog progressDialog;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.setwallpaper_bottom_sheet_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wallurl = getArguments().getString(AppConstants.WALLURL);
        downloadurl = getArguments().getString(AppConstants.DOWNLOAD_URL);
        wallimg = view.findViewById(R.id.wallpaper_img);
        wallimg.setImageURI(wallurl);
        context = getContext();

        view.findViewById(R.id.setwallpaper_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WallAsync().execute(downloadurl);
            }
        });
    }

    private class WallAsync extends AsyncTask<String, Void, String> {
        public WallAsync() {
            progressDialog = new ProgressDialog(context);
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
                    = WallpaperManager.getInstance(context);

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
                        listner.onWallpaperSet("");
                        getDialog().dismiss();
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