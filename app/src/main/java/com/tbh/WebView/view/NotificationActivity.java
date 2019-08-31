package com.tbh.WebView.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tbh.WebView.R;
import com.tbh.WebView.adapter.NotificationAdapter;
import com.tbh.WebView.apicommon.ApiClient;
import com.tbh.WebView.apicommon.ApiInterface;
import com.tbh.WebView.database.DatabaseHandler;
import com.tbh.WebView.model.NotificationModel;
import com.tbh.WebView.model.SuccessModel;
import com.tbh.WebView.notification.MyFirebaseMessagingService;
import com.tbh.WebView.utils.NetworkUtils;
import com.tbh.WebView.utils.ShowProgress;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView_Notification;
    private ImageView iv_back;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationModel> notificationModelArrayList=new ArrayList<>();
    private MyFirebaseMessagingService myFirebaseMessagingService;
    private LinearLayout linear_layout_no_notification;
    private DatabaseHandler databaseHandler;
    private ShowProgress showProgress;
    public static ApiInterface apiInterface;
    private SwipeRefreshLayout swipeRefresh_offer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        find_View_IDs();
        events();
    }

    private void find_View_IDs() {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        recyclerView_Notification=findViewById(R.id.recyclerView_Notification);
        iv_back=findViewById(R.id.iv_back);
        swipeRefresh_offer=findViewById(R.id.swipeRefresh_offer);
        linear_layout_no_notification=findViewById(R.id.linear_layout_no_notification);
        databaseHandler=new DatabaseHandler(NotificationActivity.this);
        showProgress=new ShowProgress(NotificationActivity.this);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(NotificationActivity.this);
        recyclerView_Notification.setLayoutManager(linearLayoutManager);
        if (NetworkUtils.isNetworkAvailable(NotificationActivity.this)) {
            offerList();
        }
        else {
            NetworkUtils.isNetworkNotAvailable(NotificationActivity.this);
        }

    }

    private void events() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        swipeRefresh_offer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (NetworkUtils.isNetworkAvailable(NotificationActivity.this)) {
                    offerList();
                }
                else {
                    NetworkUtils.isNetworkNotAvailable(NotificationActivity.this);
                }
                swipeRefresh_offer.setRefreshing(false);
            }
        });

    }

    private void offerList() {
        showProgress.showDialog();

        Call<SuccessModel> call = apiInterface.offerList();
        call.enqueue(new Callback<SuccessModel>() {
            @Override
            public void onResponse(Call<SuccessModel> call, Response<SuccessModel> response) {
                showProgress.dismissDialog();
                String str_response = new Gson().toJson(response.body());
                Log.d("Response >>", str_response);

                try {
                    if (response.isSuccessful()) {
                        SuccessModel successModule = response.body();

                        String message = null, code = null,folderPath;
                        if (successModule != null) {
                            message = successModule.getMessage();
                            code = successModule.getCode();
                            folderPath=successModule.getFolder_path();
                            notificationModelArrayList.clear();
                            if (code.equalsIgnoreCase("1")) {
                                notificationModelArrayList=successModule.getNotificationModelArrayList();
                                Collections.reverse(notificationModelArrayList);
                                if(notificationModelArrayList.size()>0) {
                                    notificationAdapter = new NotificationAdapter(NotificationActivity.this, notificationModelArrayList,folderPath);
                                    recyclerView_Notification.setAdapter(notificationAdapter);
                                    linear_layout_no_notification.setVisibility(View.GONE);
                                    notificationAdapter.notifyDataSetChanged();
                                }
                                else
                                {
                                    linear_layout_no_notification.setVisibility(View.VISIBLE);
                                    notificationAdapter.notifyDataSetChanged();
                                }
                            } else {
                                linear_layout_no_notification.setVisibility(View.VISIBLE);
                                notificationAdapter.notifyDataSetChanged();
                            }


                        } else {
                            Toast.makeText(NotificationActivity.this, "Response Null", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<SuccessModel> call, Throwable t) {
                showProgress.dismissDialog();
            }
        });

    }

}
