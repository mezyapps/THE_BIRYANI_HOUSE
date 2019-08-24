package com.tbh.WebView.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tbh.WebView.R;
import com.tbh.WebView.model.NotificationModel;
import com.tbh.WebView.view.NotificationActivity;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MYViewHolder> {

    private Context mContext;
    private ArrayList<NotificationModel> notificationModelArrayList;

    public NotificationAdapter(Context mContext,ArrayList<NotificationModel> notificationModelArrayList) {
        this.mContext=mContext;
        this.notificationModelArrayList=notificationModelArrayList;
    }

    @NonNull
    @Override
    public MYViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_item_notification,parent,false);
        return new MYViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MYViewHolder holder, int position) {
        final NotificationModel notificationModel=notificationModelArrayList.get(position);

        holder.textTitle.setText(notificationModel.getTitle());
        holder.textDescription.setText(notificationModel.getDescription());

    }

    @Override
    public int getItemCount() {
        return notificationModelArrayList.size();
    }

    public class MYViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle,textDescription;

        public MYViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle=itemView.findViewById(R.id.textTitle);
            textDescription=itemView.findViewById(R.id.textDescription);
        }
    }
}
