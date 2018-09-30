package com.pratishjage.icy.Demo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pratishjage.icy.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceHolder> {

    ArrayList<DeviceData> deviceDataList;
    Context context;
    String TAG = getClass().getSimpleName();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private IWallpaperActivity mInterface;

    public DeviceAdapter(ArrayList<DeviceData> deviceDataList, Context context) {
        this.deviceDataList = deviceDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item, parent, false);
        return new DeviceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DeviceHolder holder, final int position) {
        holder.deviceNameTxt.setText(deviceDataList.get(position).getDeviceName());
        holder.osNameTxt.setText(deviceDataList.get(position).getOsName());
        holder.deviceReleaseDateTxt.setText(getYearforDate(deviceDataList.get(position).getDevice_release_date()));


        db.collection("os").document(deviceDataList.get(position).getOsID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "onComplete:url " + document.getString("platform_logo_url"));
                    holder.osLogoImg.setImageURI(document.getString("platform_logo_url"));
                }
            }
        });
        holder.maindeviceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterface.inflateQueryWallpaperFragment("deviceName", deviceDataList.get(position).getDeviceName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return deviceDataList.size();
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mInterface = (IWallpaperActivity) context;
    }

    private void initView() {

    }

    public class DeviceHolder extends RecyclerView.ViewHolder {
        private TextView deviceNameTxt, osNameTxt, deviceReleaseDateTxt;
        private SimpleDraweeView osLogoImg;
        private ConstraintLayout maindeviceLayout;

        public DeviceHolder(@NonNull View itemView) {
            super(itemView);
            deviceNameTxt = itemView.findViewById(R.id.device_name_txt);
            deviceReleaseDateTxt = itemView.findViewById(R.id.device_release_date_txt);
            osLogoImg = itemView.findViewById(R.id.os_logo_img);
            osNameTxt = itemView.findViewById(R.id.os_name_txt);
            maindeviceLayout = itemView.findViewById(R.id.main_device_item);
        }
    }

    private String getYearforDate(Date date) {


        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return checkDigit(cal.get(Calendar.YEAR));
    }

    private String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

/*
    private String getOSLogo(String osId) {
        final String[] OSLogoUrl = new String[1];
        DocumentReference docRef = db.collection("os").document(osId);
// asynchronously retrieve the document

        db.collection("os").document(osId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "onComplete:url "+document.getString("platform_logo_url"));
                    OSLogoUrl[0] = document.getString("platform_logo_url");
                }
            }
        });
        Log.d(TAG, "onComplete: "+OSLogoUrl[0]);


        DocumentReference docRef1 = db.collection("cities").document("SF");
// asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef1.get();
// ...
// future.get() blocks on response
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            System.out.println("Document data: " + document.getData());
        } else {
            System.out.println("No such document!");
        }



        return OSLogoUrl[0];
    }
*/
}
