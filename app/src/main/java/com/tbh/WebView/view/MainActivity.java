package com.tbh.WebView.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.tbh.WebView.R;
import com.tbh.WebView.database.DatabaseHandler;

public class MainActivity extends AppCompatActivity {

    private Button btn_order_now;
    private boolean doubleBackToExitPressedOnce = false;
    private ImageView iv_notification;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        find_View_IDs();
        events();

    }

    private void find_View_IDs() {
        btn_order_now=findViewById(R.id.btn_order_now);
        iv_notification=findViewById(R.id.iv_notification);
        databaseHandler=new DatabaseHandler(MainActivity.this);


        Bundle bundle = getIntent().getExtras();

        Intent intent = getIntent();

        if (intent != null) {
            String title = intent.getStringExtra("TITLE");
            String description = intent.getStringExtra("DESCRIPTION");
            if(!(title==null||description==null)) {
                databaseHandler.addNotification(title, description);
            }
        }


    }

    private void events() {
        btn_order_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,OrderWebViewActivity.class);
                startActivity(intent);
                finish();
            }
        });
        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,NotificationActivity.class);
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

}
