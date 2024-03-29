package com.tbh.New_WebView.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.tbh.New_WebView.R;
import com.tbh.New_WebView.utils.ShowProgress;

public class OrderWebViewActivity extends AppCompatActivity {

    private WebView webViewTBH;
    private ShowProgress showProgress;
    private ImageView iv_back;
    private ImageView iv_notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_web_view);

        find_View_IDs();
        events();
    }


    private void find_View_IDs() {
        showProgress=new ShowProgress(OrderWebViewActivity.this);
        iv_notification=findViewById(R.id.iv_notification);
        webViewTBH=findViewById(R.id.webViewTBH);
        iv_back=findViewById(R.id.iv_back);
        webViewTBH.getSettings().setLoadsImagesAutomatically(true);
        webViewTBH.getSettings().setJavaScriptEnabled(true);
        webViewTBH.getSettings().setDomStorageEnabled(true);
        webViewTBH.getSettings().setLoadsImagesAutomatically(true);
        webViewTBH.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webViewTBH.setWebChromeClient(new WebChromeClient());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webViewTBH.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webViewTBH.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
             //   showProgress.showDialog();
            }
            @Override
            public void onPageFinished(WebView view, String url) {
               // showProgress.dismissDialog();
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                if (url.startsWith("mailto:"))
                {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        webViewTBH.loadUrl("https://thebiryanihouse.petpooja.com/");
    }

    private void events() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderWebViewActivity.this,MainActivity.class);
                intent.putExtra("back","true");
                startActivity(intent);
                finish();
            }
        });

        iv_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OrderWebViewActivity.this,NotificationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

}
