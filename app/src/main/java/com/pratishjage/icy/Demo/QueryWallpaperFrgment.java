package com.pratishjage.icy.Demo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.pratishjage.icy.R;
import com.pratishjage.icy.WallpaperData;
import com.pratishjage.icy.WallpaperrAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class QueryWallpaperFrgment extends Fragment implements Walladp.LoadingListner {


    private String mWhereTag;
    private String mWhereValue;


    String TAG = getClass().getSimpleName();
    RecyclerView recyclerview;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    List<WallpaperData> wallpaperDataList;
    Walladp adapter;
    private ListenerRegistration queryListner;
    ContentLoadingProgressBar progressBar;
    LinearLayout emptyLayout;
    private InterstitialAd mInterstitialAd;
    private Query baseQuery;

    public QueryWallpaperFrgment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWhereTag = getArguments().getString(AppConstants.INTENT_WHERE_TAG);
            mWhereValue = getArguments().getString(AppConstants.INTENT_WHERE_VALUE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_query_wallpaper_frgment, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerview = view.findViewById(R.id.recyclerview);
        progressBar = view.findViewById(R.id.progressbar);
        emptyLayout = view.findViewById(R.id.emptylayout);
        wallpaperDataList = new ArrayList<>();
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        baseQuery = db.collection("debug_wallpaper").
                whereEqualTo(mWhereTag, mWhereValue)
                .orderBy("release_date", Query.Direction.ASCENDING);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(4)
                .setPageSize(5)
                .build();

        FirestorePagingOptions<WallpaperData> options = new FirestorePagingOptions.Builder<WallpaperData>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, config, WallpaperData.class)
                .build();

        adapter = new Walladp(options, getContext(),this);
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void onLoadingFinished() {
        progressBar.setVisibility(View.GONE);
        emptyLayout.setVisibility(View.VISIBLE);
        recyclerview.setVisibility(View.GONE);
    }



}
