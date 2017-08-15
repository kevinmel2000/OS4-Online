package com.os4.ecb.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.os4.ecb.Constant;
import com.os4.ecb.R;
import com.os4.ecb.activity.PDXChatActivity;
import com.os4.ecb.beans.Messages;
import com.os4.ecb.beans.RosterItems;
import com.os4.ecb.misc.NetworkUtil;
import com.os4.ecb.misc.Base64;

public class PDXServiceExt extends Service {

	private static final String TAG = PDXServiceExt.class.getName();
	private PDXServiceFacadeExt binder = null;
	private NetworkChangeReceiver networkChange = new NetworkChangeReceiver();
	private NetworkChangeReceiver wifiChange = new NetworkChangeReceiver();
	private NotificationMessageReceiver notifMessage = new NotificationMessageReceiver();

    public PDXServiceExt(){}
    
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate");
		registerReceiver(networkChange, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
		registerReceiver(wifiChange, new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED"));
		registerReceiver(notifMessage, new IntentFilter("com.os4.ecb.BROADCAST_MESSAGE_NOTIFICATION"));
	}

	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(TAG, "onBind");
		
		if(binder==null){
			Log.d(TAG, "PDXServiceExt.onCreate:binder==null");
			binder = new PDXServiceFacadeExt(this);

			Notification note = new NotificationCompat.Builder(this).build();
		    note.flags |= Notification.FLAG_NO_CLEAR;
			int id = 19700411;
			startForeground(id, note);			
		}
		return binder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG,"onStartCommand");
		super.onStartCommand(intent, flags, startId);

		if(binder==null){
			Log.d(TAG, "PDXServiceExt.onCreate:binder==null");
			binder = new PDXServiceFacadeExt(this);

			Notification note = new NotificationCompat.Builder(this).build();
		    note.flags |= Notification.FLAG_NO_CLEAR;
			int id = 19700411;
			startForeground(id, note);			
		}		
		return Service.START_STICKY;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.d(TAG, "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(TAG, "onUnbind");
		return super.onUnbind(intent);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		unregisterReceiver(networkChange);
		unregisterReceiver(wifiChange);
		unregisterReceiver(notifMessage);
	}

	private int networkType = NetworkUtil.TYPE_NOT_CONNECTED;

	public class NetworkChangeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			Log.d(TAG,"NetworkChangeReceiver.onReceive");
			int conn = NetworkUtil.getConnectivityStatus(context);
			Log.i(TAG,NetworkUtil.getConnectivityStatusString(conn));

			if(conn == NetworkUtil.TYPE_WIFI) {
				if(conn!=networkType) {
					Log.i(TAG,"Network change to Wifi");
					binder.disconnect();
				}
				networkType = conn;
			}else if(conn == NetworkUtil.TYPE_MOBILE) {
				if(conn!=networkType) {
					Log.i(TAG,"Network change to Mobile Data");
					binder.disconnect();
				}
				networkType = conn;
			}else if(conn == NetworkUtil.TYPE_NOT_CONNECTED) {
				networkType = conn;
				//binder.disconnect();
			}
		}
	}

	public class NotificationMessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(final Context context, final Intent intent) {

			String json = intent.getStringExtra("json");
			String msg = intent.getStringExtra("msg");


			Messages.Message message = (new Gson()).fromJson(msg,Messages.Message.class);
			String text = message.getTextMessage();
			if(message.getFileInfo()!=null) text = message.getFileInfo().getName();

			RosterItems.Item item = (new Gson()).fromJson(json,RosterItems.Item.class);
			Bitmap bm = BitmapFactory.decodeResource(getResources(),R.drawable.ic_user);
			if(item.getPhotoBinVal()!=null && item.getPhotoBinVal().trim().length()>0) {
				byte[] bytes = Base64.decode(item.getPhotoBinVal());
				bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			}

			int notificationId = Constant.NOTIFICATION_CHAT;
			Intent notificationIntent;

			notificationIntent = new Intent(context, PDXChatActivity.class);
			notificationIntent.putExtra("json",json);
			notificationIntent.putExtra("msg",msg);

			if(notificationIntent!=null){
				PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				Notification notification = new NotificationCompat.Builder(PDXServiceExt.this)
						.setSmallIcon(R.drawable.ic_delivered)
						.setLargeIcon(bm)
						.setContentTitle(item.getName())
						.setContentText(text)
						.setContentIntent(contentIntent)
						.setDefaults(Notification.DEFAULT_ALL)
						.build();

				NotificationManager notifier = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				notifier.notify(notificationId,notification);
			}
		}
	}
}
