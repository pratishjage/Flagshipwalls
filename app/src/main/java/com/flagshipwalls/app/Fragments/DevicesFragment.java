package com.flagshipwalls.app.Fragments;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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


public class DevicesFragment extends Fragment {


    RecyclerView recyclerView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = getClass().getSimpleName();
    private ArrayList<DeviceData> deviceDataList;
    DeviceAdp deviceAdapter;
    ContentLoadingProgressBar progressbar;
    private Query baseQuery;

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



        baseQuery = db.collection("devices").orderBy("device_release_date", Query.Direction.DESCENDING);

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(4)
                .setPageSize(5)
                .build();

        FirestorePagingOptions<DeviceData> options = new FirestorePagingOptions.Builder<DeviceData>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, config, DeviceData.class)
                .build();
        deviceAdapter = new DeviceAdp(options, getContext());
        recyclerView.setAdapter(deviceAdapter);
        //getDevices();
    }

    public void getDevices() {
        progressbar.show();
        db.collection("devices").orderBy("device_release_date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                deviceDataList.add(document.toObject(DeviceData.class));
                            }
                            progressbar.hide();
                            deviceAdapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}
