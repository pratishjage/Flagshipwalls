package com.flagshipwalls.app.Fragments;

import android.content.Context;
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


public class OSFragment extends Fragment {

    RecyclerView os_recycler;
    List<OSData> osDataList;
    OsAdp osAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String TAG = getClass().getSimpleName();
    ContentLoadingProgressBar progressbar;

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
        context = getContext();


        Query baseQuery =  db.collection("os").orderBy("release_date", Query.Direction.DESCENDING);
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(4)
                .setPageSize(5)
                .build();

        FirestorePagingOptions<OSData> options = new FirestorePagingOptions.Builder<OSData>()
                .setLifecycleOwner(this)
                .setQuery(baseQuery, config, OSData.class)
                .build();
        osAdapter = new OsAdp(options, getContext());
        os_recycler.setAdapter(osAdapter);

       // getOS();
    }

    public void getOS() {
        progressbar.show();
        db.collection("os").orderBy("release_date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                osDataList.add(document.toObject(OSData.class));
                            }
                            progressbar.hide();
                            osAdapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}
