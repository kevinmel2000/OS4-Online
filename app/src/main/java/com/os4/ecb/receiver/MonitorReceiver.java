package com.os4.ecb.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.os4.ecb.service.MonitorService;

public class MonitorReceiver extends BroadcastReceiver {

    private static final String TAG = MonitorReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"onReceive");
        Intent startIntent = new Intent(context, MonitorService.class);
        context.startService(startIntent);
    }
}
