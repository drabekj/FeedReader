package cz.drabek.feedreader.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DownloadAlarmManager.setAlarm(context);
    }
}
