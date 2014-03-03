package com.oa.task2do;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by joe on 24/11/13.
 */
public class ReminderBroadCastReceiver extends BroadcastReceiver {


    public void	onReceive(Context context,	Intent intent)	{
                                System.out.println("BroadcastReceiver: onRecive()");

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Get taskId & message
        int taskId = intent.getIntExtra("taskId", 0);
        String notificationText = intent.getStringExtra("taskMessage");

        Intent myIntent = new Intent(context, MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, 0);

        Notification notification = new Notification(R.drawable.ic_launcher, "task2Do-"+notificationText , System.currentTimeMillis());
        notification.setLatestEventInfo( context,"Task2Do", notificationText, pendingIntent);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(null, taskId, notification); //0 is id

        System.out.println("@@@@@@@@@@@@@@@@@ INTENT RECEIVED @@@@@@@@@@@@@@@@@@@");
        Task t = Singleton.getInstance(context).getDb().getTask(taskId);
        t.set_done(1);
        t.set_alarm(0);

        t.set_dateYear(-1);
        t.set_dateMonth(-1);
        t.set_dateDay(-1);

        t.set_timeHour(-1);
        t.set_timeMinute(-1);

        t.set_mapLatitude(-1);
        t.set_mapLongitude(-1);

        Singleton.getInstance(context).getDb().updateTask(t);
//        // Vibrate the mobile phone
//        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//        vibrator.vibrate(200);
    }
}
