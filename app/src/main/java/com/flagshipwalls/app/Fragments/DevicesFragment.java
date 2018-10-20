package com.flagshipwalls.app.Fragments;

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
import com.flagshipwalls.app.Adapters.DeviceAdp;
import com.flagshipwalls.app.R;
import com.flagshipwalls.app.beans.DeviceData;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class DevicesFragment extends Fragment implements View.OnClickListener {


    RecyclerView recyclerView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = getClass().getSimpleName();
    private ArrayList<DeviceData> deviceDataList;
    DeviceAdp deviceAdapter;
    ContentLoadingProgressBar progressbar;
    private Query baseQuery;
    LinearLayout noConnectionLayout;
    MaterialButton retryBtn;
    TextView noConnectionTxt;
    public DevicesFragment() {
        // Required empty public constructor
    }


    public static DevicesFragment newInstance() {
        DevicesFragment fragment = new DevicesFragment();

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
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.devices_recycler);
        deviceDataList = new ArrayList<>();
        progressbar = view.findViewById(R.id.progressbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        noConnectionLayout = view.findViewById(R.id.no_connection_layout);
        retryBtn = view.findViewById(R.id.retry_btn);
        noConnectionTxt = view.findViewById(R.id.no_coonection_msg_txt);



        if (AppConstants.isNetworkAvailable(getContext())) {
            getListOfDevices();
        } else {
            recyclerView.setVisibility(View.GONE);
            progressbar.setVisibility(View.GONE);
            noConnectionLayout.setVisibility(View.VISIBLE);
            retryBtn.setOnClickListener(this);

        }

    }

    private void getListOfDevices() {
        recyclerView.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.VISIBLE);
        noConnectionLayout.setVisibility(View.GONE);
        baseQuery = db.collection("devices").orderBy("device_release_date", Query.Direction.DESCENDING);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(8)
                .setPageSize(16)
                .build();

        FirestorePagingOptions<DeviceData> options = new FirestorePagingOptions.Builder<DeviceData>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, config, DeviceData.class)
                .build();
        deviceAdapter = new DeviceAdp(options, getContext());
        recyclerView.setAdapter(deviceAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.retry_btn) {
            if (AppConstants.isNetworkAvailable(getContext())) {
               getListOfDevices();
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
