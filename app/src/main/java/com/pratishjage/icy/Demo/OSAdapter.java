package com.pratishjage.icy.Demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.pratishjage.icy.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class OSAdapter extends RecyclerView.Adapter<OSAdapter.OSHolder> {


    List<OSData> osDataList;
    Context context;
    private IWallpaperActivity inteface;

    public OSAdapter(List<OSData> osDataList, Context context) {
        this.osDataList = osDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public OSHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.os_item, parent, false);

        return new OSHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OSHolder holder, final int position) {
        holder.mainLayout.setBackgroundColor(getBgColor(position));
        holder.osNameText.setText(osDataList.get(position).getName());
        holder.osLogo.setImageURI(osDataList.get(position).getPlatform_logo_url());
        holder.releseDateTxt.setText(" · " + getYearforDate(osDataList.get(position).getRelease_date()));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inteface.inflateQueryWallpaperFragment("osName", osDataList.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return osDataList.size();
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        inteface = (IWallpaperActivity) context;
    }

    public class OSHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mainLayout;
        private TextView osNameText;
        private SimpleDraweeView osLogo;
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
