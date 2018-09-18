package com.bulunduc.todosha.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


public class AlarmReceiver extends BroadcastReceiver {
    private final String REMINDER_BUNDLE = "MyReminderBundle";

    private static final int PERIOD=15000; // 15 seconds

    @Override
    public void onReceive(Context context, Intent intent) {
        //intent.getBundleExtra(REMINDER_BUNDLE));
        Toast.makeText(context, "Alarm went off", Toast.LENGTH_SHORT).show();
    }

    public AlarmReceiver() {
    }

    public AlarmReceiver(Context context, Bundle extras){
        AlarmManager alarmMgr =
                (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        //intent.putExtra(REMINDER_BUNDLE, extras);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + PERIOD,
                pendingIntent);
    }

}
