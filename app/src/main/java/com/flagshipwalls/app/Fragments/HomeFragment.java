package com.flagshipwalls.app.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
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


public class HomeFragment extends Fragment implements LoadingListner {


    RecyclerView recyclerview;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<WallpaperData> wallpaperDataList;
    LinearLayoutManager linearLayoutManager;
    ContentLoadingProgressBar progressbar;
    Walladp adapter;
    Query baseQuery;

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
        wallpaperDataList = new ArrayList<>();
        // linearLayoutManager = new LinearLayoutManager(getActivity());


        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        baseQuery = db.collection("debug_wallpaper")
                .orderBy("release_date", Query.Direction.DESCENDING);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(4)
                .setPageSize(5)
                .build();

        FirestorePagingOptions<WallpaperData> options = new FirestorePagingOptions.Builder<WallpaperData>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, config, WallpaperData.class)
                .build();

        adapter = new Walladp(options, getContext(), this);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void onLoadingFinished() {
        progressbar.hide();
    }
}
