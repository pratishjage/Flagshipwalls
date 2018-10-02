package com.flagshipwalls.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;

import com.flagshipwalls.app.beans.DeviceData;
import com.flagshipwalls.app.interfaces.IWallpaperActivity;
import com.flagshipwalls.app.R;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class DeviceAdp extends FirestorePagingAdapter<DeviceData, DeviceAdp.DeviceHolder> {
    private IWallpaperActivity mInterface;
    Context context;

    public DeviceAdp(@NonNull FirestorePagingOptions<DeviceData> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final DeviceHolder holder, int i, @NonNull final DeviceData deviceData) {
        holder.deviceNameTxt.setText(deviceData.getDeviceName());
        holder.osNameTxt.setText(deviceData.getOsName());
        holder.deviceReleaseDateTxt.setText(getYearforDate(deviceData.getDevice_release_date()));


       /* db.collection("os").document(deviceData.getOsID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d(TAG, "onComplete:url " + document.getString("platform_logo_url"));
                    holder.osLogoImg.setImageURI(document.getString("platform_logo_url"));
                }
            }
        });*/
        holder.maindeviceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterface.inflateQueryWallpaperFragment("deviceName", deviceData.getDeviceName());
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mInterface = (IWallpaperActivity) context;
    }

    @NonNull
    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item, parent, false);
        return new DeviceHolder(view);
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
}
