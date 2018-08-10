package com.btb.nixorstudentapplication.Notifications;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.btb.nixorstudentapplication.BookMyTa.Main_Activity_Ta_Tab;
import com.btb.nixorstudentapplication.Misc.permission_util;
import com.btb.nixorstudentapplication.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public MyFirebaseMessagingService() {
    }

    String TAG = "MyFirebasaeMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.i(TAG, "From: " + remoteMessage.getFrom());

        //Log.i(TAG, "Message data payload: " + remoteMessage.getNotification().getBody());


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.i(TAG, "Message data payload: " + remoteMessage.getData());

        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.i(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, null);
            mBuilder.setVibrate(new long[]{500, 500, 500, 500});
            mBuilder.setSmallIcon(R.drawable.shark_image);
            mBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
            if (remoteMessage.getNotification().getTitle().equals("")) {
                mBuilder.setContentTitle("Nixor Student Application");
            } else {
                mBuilder.setContentTitle(remoteMessage.getNotification().getTitle());
            }
            mBuilder.setContentText(remoteMessage.getNotification().getBody());
            mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
// notificationId is a unique int for each notification that you must define
            notificationManager.notify(Main_Activity_Ta_Tab.i, mBuilder.build());
            Main_Activity_Ta_Tab.i += 1;
            Log.i(TAG, String.valueOf(Main_Activity_Ta_Tab.i));
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification

    }

}