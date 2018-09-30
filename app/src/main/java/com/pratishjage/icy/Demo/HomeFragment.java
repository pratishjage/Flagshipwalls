package com.pratishjage.icy.Demo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pratishjage.icy.R;
import com.pratishjage.icy.WallpaperData;
import com.pratishjage.icy.WallpaperrAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class HomeFragment extends Fragment{


    String TAG = getClass().getSimpleName();
    RecyclerView recyclerview;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    List<WallpaperData> wallpaperDataList;
  //  WallpaperrAdapter adapter;
    private ListenerRegistration queryListner;
    ContentLoadingProgressBar progressbar, bottomProgressbar;
    private InterstitialAd mInterstitialAd;
    LinearLayoutManager linearLayoutManager;

    Context mContext;
    private ProgressDialog progressDialog;
    private int totalItemCount, lastVisibleItem;
    private boolean canLoadData;
    DocumentSnapshot lastVisible;
    private boolean isFirst;
    private int visibleThreshold = 7;
    private NestedScrollView scrollView;
    FirestorePagingAdapter<WallpaperData, ViewWallHolder> adapter ;
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @SuppressLint("NewApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();

        recyclerview = view.findViewById(R.id.recyclerview);

        scrollView = view.findViewById(R.id.scrollview);
        wallpaperDataList = new ArrayList<>();
        progressbar = view.findViewById(R.id.progressbar);
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Setting Wallpaper");
        linearLayoutManager = new LinearLayoutManager(getActivity());
      //  bottomProgressbar = view.findViewById(R.id.bottomprogressbar);
        recyclerview.setLayoutManager(linearLayoutManager);
      /*  adapter = new WallpaperrAdapter(wallpaperDataList, getActivity(), this);
        recyclerview.setAdapter(adapter);
*/


/*

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(){

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int lastVisible = linearLayoutManager.findLastVisibleItemPosition();

                    boolean endHasBeenReached = lastVisible + 5 >= totalItemCount;
                    if (totalItemCount > 0 && endHasBeenReached) {
                        Log.d(TAG, "onScrollChange: ");
                        bottomProgressbar.show();
                        getWallpapers();
                    }
                }
            }
        });


        isFirst = true;
        getWallpapers();


        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId(mContext.getString(R.string.ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });
*/


        Query baseQuery = db.collection("debug_wallpaper")
                .orderBy("release_date", Query.Direction.DESCENDING);



// This configuration comes from the Paging Support Library
// https://developer.android.com/reference/android/arch/paging/PagedList.Config.html
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(4)
                .setPageSize(5)
                .build();

        FirestorePagingOptions<WallpaperData> options = new FirestorePagingOptions.Builder<WallpaperData>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, config, WallpaperData.class)
                .build();



        adapter =
                new FirestorePagingAdapter<WallpaperData, ViewWallHolder>(options) {
                    @NonNull
                    @Override
                    public ViewWallHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        // Create the ItemViewHolder
                        // ...
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallpaper_item, parent, false);
                        return new ViewWallHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull ViewWallHolder holder,
                                                    int position,
                                                    @NonNull WallpaperData model) {
                        // Bind the item to the view holder
                        // ...
                        Log.d(TAG, "onBindViewHolder: "+model.getName());
                        holder.wallpaperImg.setImageURI(model.getImgurl());
                         /*holder.wallpaperImg.setController(
                               Fresco.newDraweeControllerBuilder()
                                        .setTapToRetryEnabled(true)
                                        .setUri(model.getImgurl())
                                        .build());
*/
                    }

                    @Override
                    protected void onLoadingStateChanged(@NonNull LoadingState state) {
                        super.onLoadingStateChanged(state);

                        if (state==LoadingState.LOADING_INITIAL) {
                            Log.d(TAG, "onLoadingStateChanged: INITIAL");
                        } if (state==LoadingState.LOADING_MORE) {
                            Log.d(TAG, "onLoadingStateChanged: MORE");
                        }if (state==LoadingState.ERROR) {
                            Log.d(TAG, "onLoadingStateChanged_ERROR");
                          //  retry();
                            retry();
                        }
                    }
                };


        recyclerview.setAdapter(adapter);


    }
/*

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
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


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void getWallpapers() {
        //   progressbar.show();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getWallpapers: " + isFirst);
        Query query;

        if (isFirst) {
            progressbar.show();
            query = db.collection("debug_wallpaper")
                    .orderBy("release_date", Query.Direction.DESCENDING)
                    .limit(visibleThreshold);
            isFirst = false;
        } else {
            query = db.collection("debug_wallpaper")
                    .orderBy("release_date", Query.Direction.DESCENDING)
                    .startAfter(lastVisible)
                    .limit(visibleThreshold);
        }

        // db.collection("debug_wallpaper").orderBy("release_date", Query.Direction.DESCENDING)
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                            if (documentSnapshots.size() > 0) {
                                for (DocumentSnapshot document : documentSnapshots) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    wallpaperDataList.add(document.toObject(WallpaperData.class));
                                }
                                adapter.notifyDataSetChanged();
                                //  List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                                lastVisible = documentSnapshots.get(documentSnapshots.size() - 1);

                                if (documentSnapshots.size() % visibleThreshold == 0) {
                                    canLoadData = true;
                                } else {
                                    canLoadData = false;
                                }
                            } else {
                                canLoadData = false;
                            }
                            if (isFirst) {
                                progressbar.hide();
                            } else {
                                bottomProgressbar.hide();
                            }

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
*/


}
