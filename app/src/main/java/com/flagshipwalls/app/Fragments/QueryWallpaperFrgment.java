package com.flagshipwalls.app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.flagshipwalls.app.Adapters.Walladp;
import com.flagshipwalls.app.R;
import com.flagshipwalls.app.beans.WallpaperData;
import com.flagshipwalls.app.interfaces.LoadingListner;
import com.flagshipwalls.app.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class QueryWallpaperFrgment extends Fragment implements LoadingListner, View.OnClickListener {


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
    LinearLayout noConnectionLayout;
    MaterialButton retryBtn;
    TextView noConnectionTxt;


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
        //recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        noConnectionLayout = view.findViewById(R.id.no_connection_layout);
        retryBtn = view.findViewById(R.id.retry_btn);
        noConnectionTxt = view.findViewById(R.id.no_coonection_msg_txt);
        if (AppConstants.isNetworkAvailable(getContext())) {
            getWalpapers();
        } else {
            emptyLayout.setVisibility(View.GONE);
            recyclerview.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            noConnectionLayout.setVisibility(View.VISIBLE);
            retryBtn.setOnClickListener(this);

        }

    }

    private void getWalpapers() {
        recyclerview.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        noConnectionLayout.setVisibility(View.GONE);
        baseQuery = db.collection("debug_wallpaper").
                whereEqualTo(mWhereTag, mWhereValue)
                .orderBy("release_date", Query.Direction.ASCENDING);

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
    public void onLoadingFinished() {
        if (adapter.getItemCount() == 0) {
            progressBar.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
            noConnectionLayout.setVisibility(View.GONE);
            recyclerview.setVisibility(View.GONE);
        } else if (adapter.getItemCount() > 0) {
            noConnectionLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.retry_btn) {
            if (AppConstants.isNetworkAvailable(getContext())) {
                getWalpapers();
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
