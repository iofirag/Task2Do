package com.oa.task2do;

/**
 * Created by Avishay on 26/02/14.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class LocationNotification extends BroadcastReceiver {

    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Get taskId & message
        int taskId = intent.getIntExtra("taskId", 0);
        String notificationText = intent.getStringExtra("taskMessage");

        Intent myIntent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, taskId, myIntent, PendingIntent.FLAG_ONE_SHOT);

//        Notification notification = new Notification(R.drawable.ic_launcher, "task notification", System.currentTimeMillis());
//        notification.setLatestEventInfo( context,"Task2Do", notificationText, pendingIntent);
//        notification.flags = Notification.FLAG_AUTO_CANCEL;
//        notificationManager.notify(null, taskId, notification); //0 is id
//
//        // Vibrate the mobile phone
//        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//        vibrator.vibrate(200);

        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif = new Notification(R.drawable.ic_launcher, "", System.currentTimeMillis());
        notif.setLatestEventInfo(context, "Proximity Alert!", notificationText, pendingIntent);
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        //notif.defaults |= Notification.DEFAULT_SOUND;
        notif.defaults |= Notification.DEFAULT_SOUND;
        notif.defaults |= Notification.DEFAULT_VIBRATE;
        nm.notify(taskId, notif);
    }
}