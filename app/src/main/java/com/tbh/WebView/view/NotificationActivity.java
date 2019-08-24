package com.tbh.WebView.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tbh.WebView.R;
import com.tbh.WebView.adapter.NotificationAdapter;
import com.tbh.WebView.database.DatabaseHandler;
import com.tbh.WebView.model.NotificationModel;
import com.tbh.WebView.notification.MyFirebaseMessagingService;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView_Notification;
    private ImageView iv_back;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationModel> notificationModelArrayList=new ArrayList<>();
    private MyFirebaseMessagingService myFirebaseMessagingService;
    private LinearLayout linear_layout_no_notification;
    private DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        find_View_IDs();
        events();
    }

    private void find_View_IDs() {
        recyclerView_Notification=findViewById(R.id.recyclerView_Notification);
        iv_back=findViewById(R.id.iv_back);
        linear_layout_no_notification=findViewById(R.id.linear_layout_no_notification);
        databaseHandler=new DatabaseHandler(NotificationActivity.this);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(NotificationActivity.this);
        recyclerView_Notification.setLayoutManager(linearLayoutManager);

        notificationModelArrayList.clear();

        notificationModelArrayList=databaseHandler.getNotification();

        if(notificationModelArrayList.size()>0) {
            notificationAdapter = new NotificationAdapter(NotificationActivity.this, notificationModelArrayList);
            recyclerView_Notification.setAdapter(notificationAdapter);
            linear_layout_no_notification.setVisibility(View.GONE);
            notificationAdapter.notifyDataSetChanged();
        }
        else
        {
            linear_layout_no_notification.setVisibility(View.VISIBLE);
        }

    }

    private void events() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
