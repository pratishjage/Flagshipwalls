package com.flagshipwalls.app.Dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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
    RequestOptions requestOptions;

    @SuppressLint("ValidFragment")
    public SetWallpaperDialog(WallpaperListner listner) {
        this.listner = listner;
        requestOptions = new RequestOptions().placeholder(R.drawable.place).error(R.drawable.broken_img).centerCrop();

    }

    String wallurl, downloadurl;
    ImageView wallimg;
    private ProgressDialog progressDialog;
    Context context;
    WallpaperManager myWallpaperManager;

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
        context = getContext();
        wallurl = getArguments().getString(AppConstants.WALLURL);
        downloadurl = getArguments().getString(AppConstants.DOWNLOAD_URL);
        wallimg = view.findViewById(R.id.wallpaper_img);
        //wallimg.setImageURI(wallurl);
        Glide.with(context)
                .load(wallurl)
                .apply(requestOptions)
                .into(wallimg);
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Setting Wallpaper");

        view.findViewById(R.id.setwallpaper_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppConstants.isNetworkAvailable(context)) {
                    progressDialog.show();
                    myWallpaperManager
                            = WallpaperManager.getInstance(context);
                    Glide.with(getContext().getApplicationContext())
                            .asBitmap().
                            load(wallurl)
                            .listener(new RequestListener<Bitmap>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                    return false;
                                }


                                @Override
                                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                    // resource is your loaded Bitmap
                                    try {
                                        myWallpaperManager.setBitmap(Bitmap.createBitmap(resource));
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                    listner.onWallpaperSet("");
                                    getDialog().dismiss();
                                    return true;
                                }
                            }).submit();
                }else {
                    Toast.makeText(context, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}