package com.bulunduc.todosha.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.bulunduc.todosha.R;
import com.bulunduc.todosha.TaskLab;
import com.bulunduc.todosha.TaskListActivity;


public class AlarmReceiver extends BroadcastReceiver {
    public static final String REMINDER_DATE = "reminder_date";
    public static final String REMINDER_TITLE = "reminder_title";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        Intent intentTL = new Intent(context, TaskListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,intentTL, 0);
        builder.setSmallIcon(R.drawable.outline_alarm_24)
                .setContentTitle(intent.getStringExtra(REMINDER_TITLE))
                .setAutoCancel(true)
                .setVibrate(new long[] { 1000, 1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(pendingIntent);
        Notification notification = builder.getNotification();
        nm.notify(0, notification);
        setNextNotification(context);
    }

    public static void restartNotify(Context context, Bundle extras){
        AlarmManager alarmMgr =
                (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtras(extras);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
        alarmMgr.cancel(pendingIntent);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, extras.getLong(REMINDER_DATE),
                pendingIntent);
    }

    private static void setNextNotification(Context context){
        TaskLab.get(context).addAlarmForNextTask();
    }

}
