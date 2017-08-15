package com.os4.ecb.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.os4.ecb.misc.Properties;
import com.os4.ecb.service.aidl.IPDXServiceExt;

public class MonitorService extends Service {

    private static final String TAG = MonitorService.class.getName();
    private IPDXServiceExt pdxService = null;

    @Override
    public void onCreate() {
        Log.v(TAG,"onCreate");
        super.onCreate();
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG,"onStartCommand");
        super.onStartCommand(intent, flags, startId);

        int alarmType = AlarmManager.ELAPSED_REALTIME_WAKEUP;
        long timeToRefresh = Properties.checkingInterval;
        Intent intentToFire = new Intent("com.os4.ecb.receiver.MonitorReceiver");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intentToFire, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(alarmType, SystemClock.elapsedRealtime()+timeToRefresh,timeToRefresh, pendingIntent);
        bindService(new Intent(IPDXServiceExt.class.getName()),mConnection,0);
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy");
        mConnection.onServiceDisconnected(null);
        unbindService(mConnection);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.v(TAG,"onServiceConnected");
            pdxService = IPDXServiceExt.Stub.asInterface(service);
            try{
                if(pdxService!=null && pdxService.isConnected()){
                    Log.e(TAG,"Checking..........>>>");
                    pdxService.ping();

                }else if(pdxService!=null && !pdxService.isConnected()){
                    if(pdxService.isAuthorize()) {
                        Log.e(TAG, "Try to connect..........>>>");
                        pdxService.connect(pdxService.getSessionUser());
                    }
                }else{
                    Log.e(TAG,"Service Down..........>>>");
                }
                stopSelf();

            }catch(Exception e){e.printStackTrace();}
        }
        public void onServiceDisconnected(ComponentName className) {
            Log.v(TAG,"onServiceDisconnected");
        }
    };
}
