package cz.drabek.feedreader.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class DownloadAlarmManager {

    public static final long DOWNLOAD_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES / (15 * 6);

    public static void setAlarm(Context context) {
        // initiate repeating download service
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent launchIntent = new Intent(context, DownloadBroadCastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, launchIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), DOWNLOAD_INTERVAL, pi);
    }

}
