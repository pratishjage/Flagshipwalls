package com.flagshipwalls.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.flagshipwalls.app.R;
import com.flagshipwalls.app.beans.OSData;
import com.flagshipwalls.app.interfaces.IWallpaperActivity;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class OsAdp extends FirestorePagingAdapter<OSData,OsAdp.OSHolder> {

    private IWallpaperActivity inteface;
    Context context;
    RequestOptions requestOptions;
    public OsAdp(@NonNull FirestorePagingOptions<OSData> options,Context context) {
        super(options);
        this.context=context;
        requestOptions=  new RequestOptions().placeholder(R.drawable.ic_all_out_black_24dp).error(R.drawable.ic_all_out_black_24dp);

    }

    @Override
    protected void onBindViewHolder(@NonNull OSHolder holder, final int position, @NonNull final OSData osData) {
        holder.mainLayout.setBackgroundColor(getBgColor(position));
        holder.osNameText.setText(osData.getName());
        Glide.with(context)
                .load(osData.getPlatform_logo_url())
                .apply(requestOptions)
                .into(holder.osLogo);

        holder.releseDateTxt.setText(" · " + getYearforDate(osData.getRelease_date()));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inteface.inflateQueryWallpaperFragment("osName",osData.getName());
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inteface = (IWallpaperActivity) context;
    }

    @NonNull
    @Override
    public OSHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.os_item, parent, false);

        return new OSHolder(view);
    }

    public class OSHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mainLayout;
        private TextView osNameText;
        private ImageView osLogo;
        private TextView releseDateTxt;

        public OSHolder(@NonNull View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.main_layout);
            osNameText = itemView.findViewById(R.id.os_name_text);
            osLogo = itemView.findViewById(R.id.os_logo);
            releseDateTxt = itemView.findViewById(R.id.relese_date_txt);
        }
    }

    private int getBgColor(int position) {
        int i;
        if (position > 0) {
            i = (position % 10);
        } else {
            i = 0;
        }

        switch (i) {
            case 0:
                return ContextCompat.getColor(context, R.color.grey_100);
            case 1:
                return ContextCompat.getColor(context, R.color.grey_200);
            case 2:
                return ContextCompat.getColor(context, R.color.grey_300);
            case 3:
                return ContextCompat.getColor(context, R.color.grey_400);
            case 4:
                return ContextCompat.getColor(context, R.color.grey_300);
            case 5:
                return ContextCompat.getColor(context, R.color.grey_200);
            case 6:
                return ContextCompat.getColor(context, R.color.grey_100);
            case 7:
                return ContextCompat.getColor(context, R.color.grey_200);
            case 8:
                return ContextCompat.getColor(context, R.color.grey_300);
            case 9:
                return ContextCompat.getColor(context, R.color.grey_400);
            case 10:
                return ContextCompat.getColor(context, R.color.grey_300);
            default:
                return ContextCompat.getColor(context, R.color.grey_200);

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
