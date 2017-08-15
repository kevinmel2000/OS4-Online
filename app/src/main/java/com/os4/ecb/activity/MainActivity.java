package com.os4.ecb.activity;

import java.io.File;

import com.os4.ecb.Constant;
import com.os4.ecb.R;
import com.os4.ecb.service.PDXServiceExt;
import com.os4.ecb.service.aidl.IPDXServiceExt;

import android.os.Bundle;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getName();
	private IPDXServiceExt pdxService = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		View view = findViewById(R.id.view_id);
		view.setBackgroundResource(R.drawable.img_welcome);
        File dbdir = new File(Constant.DATABASE_FILE_PATH);
        if (!dbdir.exists()) dbdir.mkdirs();
        File imgdir = new File(Constant.IMAGES_FILE_PATH);
        if (!imgdir.exists()) imgdir.mkdirs();
		File thumbdir = new File(Constant.THUMBNAILS_PATH);
		if (!thumbdir.exists()) thumbdir.mkdirs();
		File icondir = new File(Constant.ICONS_PATH);
		if (!icondir.exists()) icondir.mkdirs();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		Log.d(TAG,"onPostCreate");
		Intent startIntent = new Intent(this, PDXServiceExt.class);
		startService(startIntent);    	
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG,"onPause");
		mConnection.onServiceDisconnected(null);
		unbindService(mConnection);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"onResume");
		bindService(new Intent(IPDXServiceExt.class.getName()),mConnection, Context.BIND_AUTO_CREATE);
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		
        public void onServiceConnected(ComponentName className, IBinder service) {
            pdxService = IPDXServiceExt.Stub.asInterface(service);
            try{
				if(pdxService!=null && pdxService.isAuthorize()){
					if(pdxService.isConnected()) {
						startApplication();
					}else{
						pdxService.connect(pdxService.getSessionUser());
						startApplication();
					}
            	}else{
					Intent intent = new Intent(MainActivity.this, PDXLoginActivity.class);
					startActivityForResult(intent, Constant.LOGIN_ACTIVITY);
				}
            	
            }catch(Exception e){e.printStackTrace();}
        }
        public void onServiceDisconnected(ComponentName className) {

        }
    };

	public void startApplication(){
		try{
			Log.d(TAG,"Start Application");
			Intent intent = new Intent(this, PDXMapActivity.class);
			startActivityForResult(intent, Constant.MENU_ACTIVITY);
		}catch(Exception e){e.printStackTrace();}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG,"onActivityResult");
		try{
			if(requestCode==Constant.LOGIN_ACTIVITY && resultCode==Constant.RESULT_OK){
				startApplication();
			}else{
				finish();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}    	
}
