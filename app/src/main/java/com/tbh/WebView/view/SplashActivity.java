package com.tbh.WebView.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tbh.WebView.R;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        find_View_IDs();
        events();
    }

    private void find_View_IDs() {
    }

    private void events() {
        handlerCall();
    }
    private void handlerCall() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
            }
        }, 3000);
    }
}
