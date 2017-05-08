package cz.drabek.feedreader.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DownloadBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("HONZA", "DownloadBroadCastReceiver - onReceive: ");
        DownloadWakeLockHelper.acquire(context);
        context.startService(new Intent(context, DownloadService.class));
    }
}
