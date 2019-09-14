package com.tbh.WebView.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tbh.WebView.R;
import com.tbh.WebView.adapter.NotificationAdapter;
import com.tbh.WebView.apicommon.ApiClient;
import com.tbh.WebView.apicommon.ApiInterface;
import com.tbh.WebView.apicommon.BaseApi;
import com.tbh.WebView.database.DatabaseHandler;
import com.tbh.WebView.model.NotificationModel;
import com.tbh.WebView.model.SuccessModel;
import com.tbh.WebView.utils.ShowProgress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button btn_order_now;
    private boolean doubleBackToExitPressedOnce = false;
    private ImageView iv_notification;
    private DatabaseHandler databaseHandler;
    private Dialog offerDialog;
    private ShowProgress showProgress;
    public static ApiInterface apiInterface;
    private ArrayList<NotificationModel> notificationModelArrayList=new ArrayList<>();
    String folderPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("All");
        find_View_IDs();
        events();

    }

    private void find_View_IDs() {
        showProgress=new ShowProgress(MainActivity.this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        btn_order_now = findViewById(R.id.btn_order_now);
        iv_notification = findViewById(R.id.iv_notification);
        databaseHandler = new DatabaseHandler(MainActivity.this);


       /* Bundle bundle = getIntent().getExtras();

        Intent intent = getIntent();

        if (intent != null) {
            String title = intent.getStringExtra("TITLE");
            String description = intent.getStringExtra("DESCRIPTION");
            if (!(title == null || description == null)) {
                databaseHandler.addNotification(title, description);
            }
        }
        */

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
           offerList();
        }

    }

    private void events() {
        btn_order_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderWebViewActivity.class);
                startActivity(intent);
                finish();
            }
        });
        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        doubleBackPressLogic();
    }

    // ============ End Double tab back press logic =================
    private void doubleBackPressLogic() {
        if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit !!", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 1000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    public void displayOfferDialog() {
        offerDialog = new Dialog(MainActivity.this);
        offerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        offerDialog.setContentView(R.layout.offer_dialog);

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


        String imageUrl = BaseApi.IMAGE_PATH+folderPath + "/" + notificationModelArrayList.get(0).getImage_path();
        Picasso.with(offerDialog.getContext()).load(imageUrl).into(offer_image);
        textTitle.setText(notificationModelArrayList.get(0).getTitle());
        textDescription.setText(notificationModelArrayList.get(0).getDescription());
        String date=notificationModelArrayList.get(0).getStart_date();
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
        btn_view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,NotificationActivity.class);
                startActivity(intent);
                offerDialog.dismiss();
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

                        String message = null, code = null;
                        if (successModule != null) {
                            message = successModule.getMessage();
                            code = successModule.getCode();
                            folderPath = successModule.getFolder_path();
                            notificationModelArrayList.clear();
                            if (code.equalsIgnoreCase("1")) {
                                notificationModelArrayList = successModule.getNotificationModelArrayList();
                                Collections.reverse(notificationModelArrayList);
                                displayOfferDialog();
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "Response Null", Toast.LENGTH_SHORT).show();
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

  /*  @Override
    protected void onRestart() {
        super.onRestart();
        offerList();
    }*/
}
