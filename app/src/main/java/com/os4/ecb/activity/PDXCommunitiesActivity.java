package com.os4.ecb.activity;

import com.google.gson.Gson;
import com.os4.ecb.Constant;
import com.os4.ecb.R;
import com.os4.ecb.beans.DiscoItems;
import com.os4.ecb.holder.GroupHolder;
import com.os4.ecb.service.aidl.IPDXServiceExt;
import com.os4.ecb.service.aidl.IPDXServiceListenerExt;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PDXCommunitiesActivity extends BaseActivity {

	private static final String TAG = PDXCommunitiesActivity.class.getName();
	private IPDXServiceExt pdxService = null;
	private ListView communitiesView;
	private DiscoItems communities = new DiscoItems();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_groups_view);

		communitiesView = (ListView) findViewById(R.id.list_view);
		communitiesView.setAdapter(new CommunitiesAdapter());
		communitiesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				Log.d(TAG,"onItemClick");
				DiscoItems.Item item = (DiscoItems.Item) communities.getItems().get(position);
				Intent intent = new Intent(PDXCommunitiesActivity.this, PDXGroupsActivity.class);
				intent.putExtra("jid", item.getJid());
				startActivity(intent);
			}
		});
	}

	private CommunitiesReceiver updateCommunitiesReceiver = new CommunitiesReceiver();
	public class CommunitiesReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG,"onUpdate Communities View");
			try {
				String json = pdxService.getServices();
				if(json!=null && !json.equalsIgnoreCase("null")) {
					communities = (new Gson()).fromJson(json, DiscoItems.class);
					communitiesView.setAdapter(new CommunitiesAdapter());
				}
			}catch(Exception e){
				Log.e(TAG,e.getMessage());
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause .....");
		unregisterReceiver(updateCommunitiesReceiver);
		mConnection.onServiceDisconnected(null);
		unbindService(mConnection);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume .....");
		registerReceiver(updateCommunitiesReceiver, new IntentFilter("COMMUNITIES_BROADCAST"));
		bindService(new Intent(IPDXServiceExt.class.getName()),mConnection, Context.BIND_AUTO_CREATE);
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			pdxService = IPDXServiceExt.Stub.asInterface(service);
			try{
				if(pdxService!=null) {
					pdxService.addServiceListener(serviceListener);
					pdxService.getService();
					String json = pdxService.getServices();
					if(json!=null && !json.equalsIgnoreCase("null")) {
						communities = (new Gson()).fromJson(json, DiscoItems.class);
						communitiesView.setAdapter(new CommunitiesAdapter());
					}
				}
			}catch(Exception e){e.printStackTrace();}
		}
		public void onServiceDisconnected(ComponentName className) {
			try{
				pdxService.removeServiceListener(serviceListener);
			}catch(Exception e){e.printStackTrace();}
			pdxService = null;
		}
	};

	private IPDXServiceListenerExt serviceListener = new IPDXServiceListenerExt.Stub() {
		@Override
		public void onConnected(String sessionId) throws RemoteException {}
		@Override
		public void onDisconnected(String reason) throws RemoteException {}
		@Override
		public void onRegistered(String json) throws RemoteException {}
		@Override
		public void onAuthorized(String json) throws RemoteException {}
		@Override
		public void onGetRosters(String json) throws RemoteException {}
		@Override
		public void onPresence(String json) throws RemoteException {}
		@Override
		public void onPresenceGroup(String json) throws RemoteException {}
		@Override
		public void onUpdateAvatar(String json) throws RemoteException {}
		@Override
		public void onUpdateService(String json) throws RemoteException {
			sendBroadcast(new Intent("COMMUNITIES_BROADCAST"));
		}
		@Override
		public void onUpdateGroup(String json) throws RemoteException {}
		@Override
		public void onUpdateParticipant(String json) throws RemoteException {}
		@Override
		public void onUpdateServiceInfo(String json) throws RemoteException {}
		@Override
		public void onUpdateGroupInfo(String json) throws RemoteException {}
		@Override
		public void onSubscribe(String json) throws RemoteException {}
		@Override
		public void onUnSubscribe(String json) throws RemoteException {}
		@Override
		public void onAddRoster(String json) throws RemoteException {}
		@Override
		public void onUpdateRoster(String json) throws RemoteException {}
		@Override
		public void onDeleteRoster(String json) throws RemoteException {}
		@Override
		public void onMessage(String json) throws RemoteException {}
		@Override
		public void onMessageGroup(String json) throws RemoteException {}
		@Override
		public void onMessageParticipant(String json) throws RemoteException {}
		@Override
		public void onMessageState(String json) throws RemoteException {}
		@Override
		public void onInvitation(String json) throws RemoteException {}
		@Override
		public void onGetAffiliation(String json) throws RemoteException {}
		@Override
		public void onPing(String json) throws RemoteException {}
		@Override
		public void onTransferFile(String json) throws RemoteException {}
		@Override
		public void onIQPacket(String json) throws RemoteException {}
		@Override
		public void onMessagePacket(String json) throws RemoteException {}
		@Override
		public void onErrorResponse(String error) throws RemoteException {}
		@Override
		public void onSignOut() throws RemoteException {
			finish();
		}
	};

	private class CommunitiesAdapter extends BaseAdapter {

		public int getCount() {
			return communities.getItems().size();
		}

		public Object getItem(int position) {
			return communities.getItems().get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup viewgroup) {

			GroupHolder holder;

			if (convertView == null) {
				LayoutInflater mInflater = PDXCommunitiesActivity.this.getLayoutInflater();
				convertView = mInflater.inflate(R.layout.view_group, null);
				holder = new GroupHolder();
				holder.avatar = (ImageView) convertView.findViewById(R.id.icon);
				holder.name = (TextView) convertView.findViewById(R.id.title);
				holder.description = (TextView) convertView.findViewById(R.id.summary);
				convertView.setTag(holder);
			}
			else{
				holder = (GroupHolder) convertView.getTag();
			}

			try {
				DiscoItems.Item item = (DiscoItems.Item) communities.getItems().get(position);
				int ic = R.drawable.ic_group;
				if(item.getJid().equalsIgnoreCase("alumni.os4-it.com")) ic = R.drawable.ic_alumni;
				if(item.getJid().equalsIgnoreCase("conference.os4-it.com")) ic = R.drawable.ic_group;
				if(item.getJid().equalsIgnoreCase("culinary.os4-it.com")) ic = R.drawable.ic_culinary;
				if(item.getJid().equalsIgnoreCase("group.os4-it.com")) ic = R.drawable.ic_group;
				if(item.getJid().equalsIgnoreCase("hobby.os4-it.com")) ic = R.drawable.ic_hobby;
				if(item.getJid().equalsIgnoreCase("otomotif.os4-it.com")) ic = R.drawable.ic_otomotif;
				if(item.getJid().equalsIgnoreCase("shop.os4-it.com")) ic = R.drawable.ic_group;
				if(item.getJid().equalsIgnoreCase("sport.os4-it.com")) ic = R.drawable.ic_sport;
				if(item.getJid().equalsIgnoreCase("travel.os4-it.com")) ic = R.drawable.ic_travel;
				holder.avatar.setImageResource(ic);
				holder.name.setText(item.getName());
				holder.description.setText(item.getJid());

			}catch(Exception e){
				Log.e(TAG,e.getMessage());
			}
			return convertView;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG,"onActivityResult");
		try{
			if(requestCode== Constant.GROUP_ACTIVITY && resultCode==Constant.RESULT_OK){
				setResult(Constant.RESULT_OK);
				finish();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
