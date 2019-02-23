package com.flagshipwalls.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.FragmentTransaction;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        /*view.findViewById(R.id.sort_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter=null;
                baseQuery = db.collection("debug_wallpaper")
                        .orderBy("release_date", Query.Direction.ASCENDING);
                PagedList.Config config = new PagedList.Config.Builder()
                        .setEnablePlaceholders(true)
                        .setPrefetchDistance(4)
                        .setPageSize(5)
                        .build();

                FirestorePagingOptions<WallpaperData> options = new FirestorePagingOptions.Builder<WallpaperData>()
                        .setLifecycleOwner(getActivity())
                        .setQuery(baseQuery, config, WallpaperData.class)
                        .build();

                adapter = new Walladp(options);
                recyclerview.setAdapter(adapter);
            }
        });*/
        // linearLayoutManager = new LinearLayoutManager(getActivity());
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
        recyclerview.setLayoutManager(new GridLayoutManager(this, 2));

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
      /*  header = whereValue;
        if (queryWallpaperFrgment != null) {
            getSupportFragmentManager().beginTransaction().remove(queryWallpaperFrgment).commitAllowingStateLoss();
        }
        queryWallpaperFrgment = new QueryWallpaperFrgment();
        Bundle args = new Bundle();
        args.putString(AppConstants.INTENT_WHERE_TAG, whereTag);
        args.putString(AppConstants.INTENT_WHERE_VALUE, whereValue);
        queryWallpaperFrgment.setArguments(args);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, queryWallpaperFrgment, getString(R.string.tag_fragment_query_wallpaper));
        fragmentTransaction.commit();
        mfragmentTags.add(getString(R.string.tag_fragment_query_wallpaper));

        mFragments.add(new FragmentTag(queryWallpaperFrgment, getString(R.string.tag_fragment_query_wallpaper)));
        setFragmentVisible(getString(R.string.tag_fragment_query_wallpaper));*/
    }

    @Override
    public void showSetWallpaperActivity(String wallurl, String downloadurl) {

        Intent intent = new Intent(PremiumWallActivity.this, SetWallpaperActivity.class);
        intent.putExtra(AppConstants.WALLURL, wallurl);
        intent.putExtra(AppConstants.DOWNLOAD_URL, downloadurl);
        startActivityIfNeeded(intent, 111);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


    }

}
