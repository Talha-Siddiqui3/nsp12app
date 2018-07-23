package com.btb.nixorstudentapplication.Sharks_on_cloud;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.btb.nixorstudentapplication.R;

public class NotificationClass {
    NotificationManagerCompat notificationManager;
    NotificationCompat.Builder mBuilder;
    public Activity context;
    int notificationId;


    public NotificationClass(Activity context, int notificationId){
        this.context=context;
        this.notificationId=notificationId;

    }


    public void uploadNotification(String name, int progress, boolean done,boolean initialNotification) {
        int PROGRESS_MAX = 100;
        int PROGRESS_CURRENT = progress;
        if (initialNotification) {
            notificationManager = NotificationManagerCompat.from(context);
            mBuilder = new NotificationCompat.Builder(context, "111");
            mBuilder.setContentTitle(name)
                    .setContentText("Uploading(0%)")
                    .setSmallIcon(R.drawable.shark_black)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setOngoing(true);

// Issue the initial notification with zero progress

            mBuilder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
            notificationManager.notify(notificationId, mBuilder.build());
// Do the job here that tracks the progress.
// Usually, this should be in a worker thread


        } else {
            // To show progress, update PROGRESS_CURRENT and update the notification with:
            mBuilder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
            mBuilder.setContentText("Uploading(" + progress + "%)");
            notificationManager.notify(notificationId, mBuilder.build());
        }

        if (done) {
// When done, update the notification one more time to remove the progress bar
            mBuilder.setContentText("Upload complete")
                    .setProgress(0, 0, false)
                    .setOngoing(false);
            notificationManager.notify(notificationId, mBuilder.build());
        }
    }







}
