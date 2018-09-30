package com.pratishjage.icy.Demo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class QueryWallpaperFrgment extends Fragment implements WallpaperListner {


    // TODO: Rename and change types of parameters
    private String mWhereTag;
    private String mWhereValue;


    String TAG = getClass().getSimpleName();
    RecyclerView recyclerview;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    List<WallpaperData> wallpaperDataList;
    WallpaperrAdapter adapter;
    private ListenerRegistration queryListner;
    ContentLoadingProgressBar progressBar;
    LinearLayout emptyLayout;
    private InterstitialAd mInterstitialAd;
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

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //    mListener = null;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(getContext().getString(R.string.ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        recyclerview = view.findViewById(R.id.recyclerview);
        progressBar = view.findViewById(R.id.progressbar);
        emptyLayout = view.findViewById(R.id.emptylayout);
        wallpaperDataList = new ArrayList<>();
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new WallpaperrAdapter(wallpaperDataList, getActivity(),this);
        recyclerview.setAdapter(adapter);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
    }


    @Override
    public void onStart() {
        super.onStart();
        progressBar.show();
        queryListner = db.collection("debug_wallpaper").
                whereEqualTo(mWhereTag, mWhereValue)
                .orderBy("release_date", Query.Direction.ASCENDING).
                        addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                if (e != null) {

                                    Log.d(TAG, "onEvent: " + e.getCode() + " , " + e);
                                    System.err.println("Listen failed:" + e);
                                    if (wallpaperDataList.size() == 0) {
                                        progressBar.setVisibility(View.GONE);
                                        emptyLayout.setVisibility(View.VISIBLE);
                                        recyclerview.setVisibility(View.GONE);
                                    }
                                    progressBar.hide();
                                    return;
                                }

                                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                                    switch (documentChange.getType()) {
                                        case ADDED:
                                            wallpaperDataList.add(documentChange.getDocument().toObject(WallpaperData.class));

                                            break;
                                        case MODIFIED:
                                            // System.out.println("Modified city: " + dc.getDocument().getData());
                                            break;
                                        case REMOVED:
                                            wallpaperDataList.remove(documentChange.getDocument().toObject(WallpaperData.class));
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                if (wallpaperDataList.size() == 0) {
                                    progressBar.setVisibility(View.GONE);
                                    emptyLayout.setVisibility(View.VISIBLE);
                                    recyclerview.setVisibility(View.GONE);
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    emptyLayout.setVisibility(View.GONE);
                                    recyclerview.setVisibility(View.VISIBLE);
                                }
                                progressBar.hide();


                            }
                        });
    }


    @Override
    public void onWallpaperSet(String imgurl) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "Wallpaper set", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        });
    }
}
