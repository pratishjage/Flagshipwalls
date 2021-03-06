package com.flagshipwalls.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.flagshipwalls.app.utils.AppConstants;
import com.flagshipwalls.app.utils.KotlinExt;

import java.io.IOException;

public class SetWallpaperActivity extends AppCompatActivity {

    String wallurl, downloadurl;
    ImageView wallimg;
    RequestOptions requestOptions;
    private WallpaperManager myWallpaperManager;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_wallpaper);

        wallurl = getIntent().getStringExtra(AppConstants.WALLURL);
        downloadurl = getIntent().getStringExtra(AppConstants.DOWNLOAD_URL);
        wallimg = findViewById(R.id.wallpaper_img);
        requestOptions = new RequestOptions().placeholder(R.drawable.place).error(R.drawable.broken_img).centerCrop();
        Glide.with(this)
                .load(wallurl)
                .apply(requestOptions)
                .into(wallimg);
        progressDialog = new ProgressDialog(SetWallpaperActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Setting Wallpaper");
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        findViewById(R.id.setwallpaper_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppConstants.isNetworkAvailable(SetWallpaperActivity.this)) {
                    progressDialog.show();
                    myWallpaperManager
                            = WallpaperManager.getInstance(SetWallpaperActivity.this);
                    Glide.with(getApplicationContext())
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
                                        Answers.getInstance().logCustom(new CustomEvent("Wallpaper Set"));
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    progressDialog.dismiss();
                                    Intent returnIntent = new Intent();
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                    return true;
                                }
                            }).submit();
                } else {
                    Toast.makeText(SetWallpaperActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
        KotlinExt kotlinExt = new KotlinExt();
        kotlinExt.makeStatusBarTransparent(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_container), new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                findViewById(R.id.top_frame).setPadding(0,insets.getSystemWindowInsetTop(),0,0);
                findViewById(R.id.top_frame).getLayoutParams().height=(insets.getSystemWindowInsetTop()+getActionBarSize());
           //kotlinExt.setMarginTop(findViewById(R.id.top_frame),insets.getSystemWindowInsetTop());
               // findViewById(R.id.top_frame).kotlinExt.set
                insets.consumeSystemWindowInsets();
                return insets;
            }
        });
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.content_container)) { _, insets ->
                findViewById<FloatingActionButton>(R.id.fab1).setMarginTop(insets.systemWindowInsetTop)
            findViewById<FloatingActionButton>(R.id.fab2).setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }*/
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public int getActionBarSize() {
        TypedArray styledAttributes = getTheme().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarSize;
    }


}
