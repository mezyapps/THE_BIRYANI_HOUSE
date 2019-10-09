package com.tbh.New_WebView.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tbh.New_WebView.R;
import com.tbh.New_WebView.view.NotificationActivity;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private Context mContext;
    private static final String TAG = "FCM Service";
    String doctor_name;
    public static final String CHANNEL_ID = "mychannelid";


    NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    int m;

    public MyFirebaseMessagingService() {
        // empty required
    }


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token: " + token);

        //SharedUtils.putDeviceToken(getApplicationContext(), token);

    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        mContext = this;

           String title = null, description = null,image=null;

           description=remoteMessage.getNotification().getBody();
           title=remoteMessage.getNotification().getTitle();
           image=remoteMessage.getNotification().getIcon();

        sendNotification(title,description);

    }

    private void sendNotification(String title,String description) {


        final String NOTIFICATION_CHANNEL_ID = "10001";
        Random random = new Random();
        m = random.nextInt(9999 - 1000) + 1000;
        long[] v = {500, 1000};

        Intent intent;

        intent = new Intent(this, NotificationActivity.class);
        intent.putExtra("TITLE", title);
        intent.putExtra("DESCRIPTION", description);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.splash_icon)
                .setContentTitle(title)
                .setContentText(description)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setAutoCancel(true)
                .setSound(uri)
                .setColor(ContextCompat.getColor(mContext, R.color.app_title))
                .setVibrate(v)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setContentIntent(pendingIntent);


        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(0, builder.build());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels() {

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", NotificationManager.IMPORTANCE_HIGH);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        adminChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        builder.setChannelId(CHANNEL_ID);

        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }

        if (notificationManager != null) {
            notificationManager.notify(m, builder.build());
        }
    }


}

