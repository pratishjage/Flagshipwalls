package com.flagshipwalls.app.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.flagshipwalls.app.utils.AppConstants;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.flagshipwalls.app.Adapters.Walladp;
import com.flagshipwalls.app.R;
import com.flagshipwalls.app.beans.WallpaperData;
import com.flagshipwalls.app.interfaces.LoadingListner;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class HomeFragment extends Fragment implements LoadingListner, View.OnClickListener {


    RecyclerView recyclerview;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    LinearLayoutManager linearLayoutManager;
    ContentLoadingProgressBar progressbar;
    Walladp adapter;
    Query baseQuery;
    LinearLayout noConnectionLayout;
    MaterialButton retryBtn;
    TextView noConnectionTxt;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @SuppressLint("NewApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerview = view.findViewById(R.id.recyclerview);
        progressbar = view.findViewById(R.id.progressbar);
        noConnectionLayout = view.findViewById(R.id.no_connection_layout);
        retryBtn = view.findViewById(R.id.retry_btn);
        noConnectionTxt = view.findViewById(R.id.no_coonection_msg_txt);
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
        if (AppConstants.isNetworkAvailable(getContext())) {
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
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        baseQuery = db.collection("debug_wallpaper")
                .orderBy("release_date", Query.Direction.DESCENDING);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(8)
                .setPageSize(16)
                .build();

        FirestorePagingOptions<WallpaperData> options = new FirestorePagingOptions.Builder<WallpaperData>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, config, WallpaperData.class)
                .build();

        adapter = new Walladp(options, getContext(), this);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.retry_btn) {
            if (AppConstants.isNetworkAvailable(getContext())) {
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
}
