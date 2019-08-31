package com.tbh.WebView.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tbh.WebView.R;
import com.tbh.WebView.model.NotificationModel;
import com.tbh.WebView.view.NotificationActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MYViewHolder> {

    private Context mContext;
    private ArrayList<NotificationModel> notificationModelArrayList;
    private String folderPath;

    public NotificationAdapter(Context mContext, ArrayList<NotificationModel> notificationModelArrayList, String folderPath) {
        this.mContext = mContext;
        this.notificationModelArrayList = notificationModelArrayList;
        this.folderPath = folderPath;
    }

    @NonNull
    @Override
    public MYViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_item_notification, parent, false);
        return new MYViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MYViewHolder holder, int position) {
        final NotificationModel notificationModel = notificationModelArrayList.get(position);
        String imageUrl = "http://mezyapps.com/tbh/" + folderPath + "/" + notificationModel.getImage_path();
        Picasso.with(mContext).load(imageUrl).into(holder.iv_offer_image);
        holder.textTitle.setText(notificationModel.getTitle());
        holder.textDescription.setText(notificationModel.getDescription());
        String date=notificationModel.getStart_date();
        StringTokenizer stringTokenizer=new StringTokenizer(date,"-");
        String year=stringTokenizer.nextToken().trim();
        String month=stringTokenizer.nextToken().trim();
        String day=stringTokenizer.nextToken().trim();
        String newDate=day+"-"+month+"-"+year;
        holder.textOfferStartDate.setText("valid offer "+newDate);
    }

    @Override
    public int getItemCount() {
        return notificationModelArrayList.size();
    }

    public class MYViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle, textDescription, textOfferStartDate, textOfferEndDate;
        private ImageView iv_offer_image;

        public MYViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
            textOfferStartDate = itemView.findViewById(R.id.textOfferStartDate);
            textOfferEndDate = itemView.findViewById(R.id.textOfferEndDate);
            iv_offer_image = itemView.findViewById(R.id.iv_offer_image);
        }
    }
}
