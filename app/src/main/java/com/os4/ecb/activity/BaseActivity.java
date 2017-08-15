package com.os4.ecb.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.os4.ecb.MessengerApp;
import com.os4.ecb.R;
import com.os4.ecb.service.aidl.IPDXServiceExt;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

	private static final String TAG = BaseActivity.class.getName();
	private IPDXServiceExt pdxService = null;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawer;
    NavigationView navigationView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate .....");
		super.onCreate(savedInstanceState);
	}

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);
        FrameLayout activityContainer = (FrameLayout) findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer);
        setupMenu();
    }

    private void setupMenu() {
        Log.d(TAG, "setupMenu");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //if(navigationView.getMenu().)
        Integer menuItemId = ((MessengerApp)getApplicationContext()).getMenuItemId();
        if(menuItemId == null){
            navigationView.setCheckedItem(R.id.nav_home);
        }else{
            navigationView.setCheckedItem(menuItemId.intValue());
        }
    }

    public int getCurrentMenuItemId(){
        return ((MessengerApp)getApplicationContext()).getMenuItemId();
    }

    public void setCurrentMenuItemId(int id){
        navigationView.setCheckedItem(id);
        ((MessengerApp)getApplicationContext()).setMenuItemId(id);
    }

    @Override
	protected void onPause() {
    	super.onPause();
    	Log.d(TAG, "onPause .....");
    	mConnection.onServiceDisconnected(null);
    	unbindService(mConnection);
	}

	@Override
	protected void onResume() {
		super.onResume();
		bindService(new Intent(IPDXServiceExt.class.getName()),mConnection, Context.BIND_AUTO_CREATE);
	}

	private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            pdxService = IPDXServiceExt.Stub.asInterface(service);
            try{
            	if(pdxService!=null){
                    String user = pdxService.getSessionUser();
            	}
            }catch(Exception e){
            	e.printStackTrace();
            }
        }
        public void onServiceDisconnected(ComponentName className) {
        	pdxService = null;
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void startHome() {
        Log.d(TAG,"startHome");
        Intent intent = new Intent(this, PDXMapActivity.class);
        startActivity(intent);
    }

    public void startContact() {
        Log.d(TAG,"startAccount");
        Intent intent = new Intent(this, PDXContactActivity.class);
        startActivity(intent);
    }

    public void startCommunities() {
        Log.d(TAG,"startCommunities");
        Intent intent = new Intent(this, PDXCommunitiesActivity.class);
        startActivity(intent);
    }

    public void startSetting() {
        Log.d(TAG,"startSetting");
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (item.isChecked()){
            drawer.closeDrawer(GravityCompat.START);
            item.setChecked(false);
            return true;
        }else{
            ((MessengerApp)getApplicationContext()).setMenuItemId(id);
        }

        if (id == R.id.nav_home) {
            startHome();
        } else if (id == R.id.nav_contacts) {
            startContact();
        } else if (id == R.id.nav_communities) {
            startCommunities();
        }else if (id == R.id.nav_settings){
            startSetting();
        } else if (id == R.id.nav_sign_out) {
            try {
                if(pdxService!=null) pdxService.signout();
            }catch(Exception e){
                Log.e(TAG,e.getMessage());
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
