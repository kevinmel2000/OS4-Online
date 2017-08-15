package com.os4.ecb.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.os4.ecb.service.PDXServiceExt;

public class BootCompleteReceiver extends BroadcastReceiver {
	
	private static final String TAG = "BootCompleteReceiver";
	
    @Override
    public void onReceive(final Context context, final Intent intent) {
    	Log.d(TAG,"onReceive");
		Intent startIntent = new Intent(context, PDXServiceExt.class);
		context.startService(startIntent);    	
    }
}
