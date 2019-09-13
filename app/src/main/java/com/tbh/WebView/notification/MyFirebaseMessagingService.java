package com.tbh.WebView.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tbh.WebView.R;
import com.tbh.WebView.database.DatabaseHandler;
import com.tbh.WebView.model.NotificationModel;
import com.tbh.WebView.view.MainActivity;
import com.tbh.WebView.view.NotificationActivity;

import java.util.ArrayList;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private Context mContext;
    private static final String TAG = "FCM Service";
    String doctor_name;
    public static final String CHANNEL_ID = "mychannelid";
    private DatabaseHandler databaseHandler;


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

           databaseHandler=new DatabaseHandler(mContext);
           databaseHandler.addNotification(title,description);
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
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(title)
                .setContentText(description)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setAutoCancel(true)
                .setSound(uri)
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
        adminChannel = new NotificationChannel(CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", NotificationManager.IMPORTANCE_LOW);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        builder.setChannelId(CHANNEL_ID);

        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }

        if (notificationManager != null) {
            notificationManager.notify(m, builder.build());
        }
    }


}

