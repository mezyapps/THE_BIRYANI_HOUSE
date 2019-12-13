package com.tbh.New_WebView.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tbh.New_WebView.R;
import com.tbh.New_WebView.apicommon.BaseApi;
import com.tbh.New_WebView.model.NotificationModel;
import com.tbh.New_WebView.view.MainActivity;
import com.tbh.New_WebView.view.NotificationActivity;

import java.util.ArrayList;
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
    public void onBindViewHolder(@NonNull MYViewHolder holder, final int position) {
        final NotificationModel notificationModel = notificationModelArrayList.get(position);
        String imageUrl = BaseApi.IMAGE_PATH + folderPath + "/" + notificationModel.getImage_path();
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

        holder.linear_layout_no_notification_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final Dialog offerDialog = new Dialog(mContext);
                offerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                offerDialog.setContentView(R.layout.offer_dialog_view);

                ImageView offer_image= offerDialog.findViewById(R.id.iv_offer_image);
                TextView textTitle = offerDialog.findViewById(R.id.textTitle);
                TextView textDescription = offerDialog.findViewById(R.id.textDescription);
                TextView textOfferStartDate = offerDialog.findViewById(R.id.textOfferStartDate);

                ImageView iv_close_dialog = offerDialog.findViewById(R.id.iv_close_dialog);
                TextView btn_view_all = offerDialog.findViewById(R.id.btn_view_all);

                offerDialog.setCancelable(false);
                offerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                offerDialog.show();

                Window window = offerDialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );


                String imageUrl = BaseApi.IMAGE_PATH+folderPath + "/" + notificationModelArrayList.get(position).getImage_path();
                Picasso.with(offerDialog.getContext())
                        .load(imageUrl)
                       .fit()
                        .priority(Picasso.Priority.HIGH)
                        .into(offer_image);
                textTitle.setText(notificationModelArrayList.get(position).getTitle());
                textDescription.setText(notificationModelArrayList.get(position).getDescription());
                String date=notificationModelArrayList.get(position).getStart_date();
                StringTokenizer stringTokenizer=new StringTokenizer(date,"-");
                String year=stringTokenizer.nextToken().trim();
                String month=stringTokenizer.nextToken().trim();
                String day=stringTokenizer.nextToken().trim();
                String newDate=day+"-"+month+"-"+year;
                textOfferStartDate.setText("valid offer "+newDate);

                iv_close_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        offerDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationModelArrayList.size();
    }

    public class MYViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle, textDescription, textOfferStartDate, textOfferEndDate;
        private ImageView iv_offer_image;
        private LinearLayout linear_layout_no_notification_view;

        public MYViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
            textOfferStartDate = itemView.findViewById(R.id.textOfferStartDate);
            textOfferEndDate = itemView.findViewById(R.id.textOfferEndDate);
            iv_offer_image = itemView.findViewById(R.id.iv_offer_image);
            linear_layout_no_notification_view = itemView.findViewById(R.id.linear_layout_no_notification_view);
        }
    }
}
