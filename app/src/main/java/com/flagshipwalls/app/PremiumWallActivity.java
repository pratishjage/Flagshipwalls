package com.flagshipwalls.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.flagshipwalls.app.Adapters.Walladp;
import com.flagshipwalls.app.Fragments.QueryWallpaperFrgment;
import com.flagshipwalls.app.beans.WallpaperData;
import com.flagshipwalls.app.interfaces.IWallpaperActivity;
import com.flagshipwalls.app.interfaces.LoadingListner;
import com.flagshipwalls.app.utils.AppConstants;
import com.flagshipwalls.app.utils.FragmentTag;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class PremiumWallActivity extends AppCompatActivity implements LoadingListner, View.OnClickListener,IWallpaperActivity {

    RecyclerView recyclerview;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LinearLayoutManager linearLayoutManager;
    ContentLoadingProgressBar progressbar;
    Walladp adapter;
    Query baseQuery;
    LinearLayout noConnectionLayout;
    MaterialButton retryBtn;
    TextView noConnectionTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_wall);
        recyclerview = findViewById(R.id.recyclerview);
        progressbar = findViewById(R.id.progressbar);
        noConnectionLayout = findViewById(R.id.no_connection_layout);
        retryBtn = findViewById(R.id.retry_btn);
        noConnectionTxt = findViewById(R.id.no_coonection_msg_txt);
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (AppConstants.isNetworkAvailable(this)) {
            setWallpapers();
        } else {
            recyclerview.setVisibility(View.GONE);
            progressbar.setVisibility(View.GONE);
            noConnectionLayout.setVisibility(View.VISIBLE);
            retryBtn.setOnClickListener(this);

        }

    }
    @Override
    public void onLoadingFinished() {
        progressbar.hide();
    }

    private void setWallpapers() {
        recyclerview.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.VISIBLE);
        noConnectionLayout.setVisibility(View.GONE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 3 == 0) {
                    return 2;
                }
                return 1;
            }
        });
        recyclerview.setLayoutManager(gridLayoutManager);
      //  recyclerview.setLayoutManager(new GridLayoutManager(this, 2));

        baseQuery = db.collection("premiumcollection")
                .orderBy("created_at", Query.Direction.DESCENDING);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(8)
                .setPageSize(16)
                .build();

        FirestorePagingOptions<WallpaperData> options = new FirestorePagingOptions.Builder<WallpaperData>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, config, WallpaperData.class)
                .build();

        adapter = new Walladp(options, this, this);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.retry_btn) {
            if (AppConstants.isNetworkAvailable(this)) {
                setWallpapers();
            } else {
                noConnectionTxt.setVisibility(View.INVISIBLE);
                noConnectionTxt.postDelayed(new Runnable() {
                    public void run() {
                        noConnectionTxt.setVisibility(View.VISIBLE);
                    }
                }, 300);
            }
        }
    }


    @Override
    public void inflateQueryWallpaperFragment(String whereTag, String whereValue) {

    }

    @Override
    public void showSetWallpaperActivity(String wallurl, String downloadurl) {

        Intent intent = new Intent(PremiumWallActivity.this, SetWallpaperActivity.class);
        intent.putExtra(AppConstants.WALLURL, wallurl);
        intent.putExtra(AppConstants.DOWNLOAD_URL, downloadurl);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


}
