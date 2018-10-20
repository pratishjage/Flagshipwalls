package com.flagshipwalls.app.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.flagshipwalls.app.utils.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.flagshipwalls.app.Adapters.OsAdp;
import com.flagshipwalls.app.R;
import com.flagshipwalls.app.beans.OSData;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class OSFragment extends Fragment implements View.OnClickListener {

    RecyclerView os_recycler;
    List<OSData> osDataList;
    OsAdp osAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = getClass().getSimpleName();
    ContentLoadingProgressBar progressbar;
    LinearLayout noConnectionLayout;
    MaterialButton retryBtn;
    TextView noConnectionTxt;
    public OSFragment() {
        // Required empty public constructor
    }
    public static OSFragment newInstance() {
        OSFragment fragment = new OSFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        osDataList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_os, container, false);
    }


    Context context;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        os_recycler = view.findViewById(R.id.os_recycler);
        os_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressbar = view.findViewById(R.id.progressbar);
        noConnectionLayout = view.findViewById(R.id.no_connection_layout);
        retryBtn = view.findViewById(R.id.retry_btn);
        noConnectionTxt = view.findViewById(R.id.no_coonection_msg_txt);
        context = getContext();

        if (AppConstants.isNetworkAvailable(getContext())) {
            getOSList();
        } else {
            os_recycler.setVisibility(View.GONE);
            progressbar.setVisibility(View.GONE);
            noConnectionLayout.setVisibility(View.VISIBLE);
            retryBtn.setOnClickListener(this);

        }



    }

    private void getOSList() {
        os_recycler.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.VISIBLE);
        noConnectionLayout.setVisibility(View.GONE);
        Query baseQuery =  db.collection("os").orderBy("release_date", Query.Direction.DESCENDING);
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(8)
                .setPageSize(16)
                .build();

        FirestorePagingOptions<OSData> options = new FirestorePagingOptions.Builder<OSData>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, config, OSData.class)
                .build();
        osAdapter = new OsAdp(options, getContext());
        os_recycler.setAdapter(osAdapter);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.retry_btn) {
            if (AppConstants.isNetworkAvailable(getContext())) {
                getOSList();
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
