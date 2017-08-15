package com.os4.ecb.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.os4.ecb.Constant;
import com.os4.ecb.R;
import com.os4.ecb.beans.DiscoItems;
import com.os4.ecb.holder.GroupHolder;
import com.os4.ecb.service.aidl.IPDXServiceExt;
import com.os4.ecb.service.aidl.IPDXServiceListenerExt;

public class PDXParticipantActivity extends BaseActivity {

	private static final String TAG = PDXParticipantActivity.class.getName();
	private IPDXServiceExt pdxService = null;
	private ListView participantsView;
	private DiscoItems participants = new DiscoItems();
	private String jid;

	private final int DIALOG_KEY_CREATE_GROUP = 1;
	private final int DIALOG_KEY_DELETE_GROUP = 2;
	private final int DIALOG_KEY_ACCEPT_REJECT = 3;
	private final int DIALOG_KEY_CANCEL_INVITE = 4;
	private final int DIALOG_KEY_REFRESH_GROUP = 5;

	private String CREATE_GROUP_MESSAGE = "Please fill the group's name and description";
	private String DELETE_GROUP_MESSAGE = "Do you want to delete your group ?";
	private String ACCEPT_GROUP_MESSAGE = "Do you want to accept invitation ?";
	private String CANCEL_INVITE_MESSAGE = "Do you want to cancel your invitation ?";
	private String CHANGE_REFRESH_MESSAGE = "Do you want to refresh the groups list ?";

	private Dialog dialog_create;
	private Dialog dialog_delete;
	private Dialog dialog_accept;
	private Dialog dialog_cancel;
	private Dialog dialog_refresh;

	private TextView tvGroup;
	private TextView tvGroupDesc;
	private CheckBox publicRoom;
	private CheckBox memberOnly;
	private CheckBox checkPassword;
	private TextView tvGroupPass;
	private Button  buttonOK;
	private Button buttonCancel;

	private DiscoItems.Item selectedParticipant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_groups_view);

		jid = getIntent().getStringExtra("jid");

		participantsView = (ListView) findViewById(R.id.list_view);
		participantsView.setAdapter(new ParticipantAdapter());
		participantsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				Log.d(TAG,"onItemClick");
				selectedParticipant = participants.getItems().get(position);
				Intent intent = new Intent(PDXParticipantActivity.this, PDXChatPrivateActivity.class);
				intent.putExtra("json", (new Gson()).toJson(selectedParticipant));
				startActivity(intent);
			}
		});
		participantsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Log.d(TAG,"setOnItemLongClick");
				selectedParticipant = participants.getItems().get(position);
				dialog_delete = onCreateDialog(DIALOG_KEY_DELETE_GROUP);
				dialog_delete.show();
				return true;
			}
		});
	}

	private ParticipantReceiver updateParticipantReceiver = new ParticipantReceiver();
	public class ParticipantReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG,"onUpdate Participant View");
			try {
				String json = pdxService.getParticipants(jid);
				if(json!=null && !json.equalsIgnoreCase("null")) {
					participants = (new Gson()).fromJson(json, DiscoItems.class);
					participantsView.setAdapter(new ParticipantAdapter());
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
		unregisterReceiver(updateParticipantReceiver);
		mConnection.onServiceDisconnected(null);
		unbindService(mConnection);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume .....");
		registerReceiver(updateParticipantReceiver, new IntentFilter("DISCOITEMS_BROADCAST"));
		bindService(new Intent(IPDXServiceExt.class.getName()),mConnection, Context.BIND_AUTO_CREATE);
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			pdxService = IPDXServiceExt.Stub.asInterface(service);
			try{
				if(pdxService!=null) {
					pdxService.addServiceListener(serviceListener);
					//pdxService.getDiscoItems(jid);
					String json = pdxService.getParticipants(jid);
					if(json!=null && !json.equalsIgnoreCase("null")) {
						participants = (new Gson()).fromJson(json, DiscoItems.class);
						participantsView.setAdapter(new ParticipantAdapter());
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
		public void onUpdateService(String json) throws RemoteException {}
		@Override
		public void onUpdateGroup(String json) throws RemoteException {}
		@Override
		public void onUpdateParticipant(String json) throws RemoteException {
			Intent intent = new Intent("DISCOITEMS_BROADCAST");
			intent.putExtra("json",json);
			sendBroadcast(intent);
		}
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

	private class ParticipantAdapter extends BaseAdapter {

		public int getCount() {
			return participants.getItems().size();
		}

		public Object getItem(int position) {
			return participants.getItems().get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup viewgroup) {

			GroupHolder holder;

			if (convertView == null) {
				LayoutInflater mInflater = PDXParticipantActivity.this.getLayoutInflater();
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
				DiscoItems.Item item = (DiscoItems.Item) participants.getItems().get(position);
				holder.avatar.setImageResource(R.drawable.ic_group);
				String name = item.getJid().substring(item.getJid().lastIndexOf("/")+1);
				holder.name.setText(name.substring(0,1).toUpperCase().concat(name.substring(1)));
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_groups, menu);
		return true;
	}

	public void onAddGroup(MenuItem item) {
		Log.d(TAG,"onAddGroup");
		dialog_create = onCreateDialog(DIALOG_KEY_CREATE_GROUP);
		dialog_create.show();
	}

	public void onRefresh(MenuItem item) {
		Log.d(TAG,"onRefresh");
		dialog_refresh = onCreateDialog(DIALOG_KEY_REFRESH_GROUP);
		dialog_refresh.show();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		LayoutInflater factory = LayoutInflater.from(this);
		ContextThemeWrapper ctw = new ContextThemeWrapper( this, R.style.DialogStyle);
		View view;
		switch (id)
		{
			case DIALOG_KEY_CREATE_GROUP:
				view = factory.inflate(R.layout.dialog_group_edit, null);
				tvGroup = (TextView) view.findViewById(R.id.groupname_input);
				tvGroupDesc = (TextView) view.findViewById(R.id.group_desc_input);
				publicRoom = (CheckBox) view.findViewById(R.id.checkBox1);
				memberOnly = (CheckBox) view.findViewById(R.id.checkBox2);
				checkPassword = (CheckBox) view.findViewById(R.id.checkBox3);
				tvGroupPass = (TextView) view.findViewById(R.id.group_password_input);
				checkPassword.setOnCheckedChangeListener(new OnCheckedChangeListener(){
					@Override
					public void onCheckedChanged(CompoundButton button, boolean checked) {
						if(checked) tvGroupPass.setEnabled(true);
						else tvGroupPass.setEnabled(false);
					}
				});
				buttonOK = (Button) view.findViewById(R.id.ok_button);
				buttonOK.setText("Create");
				buttonCancel = (Button) view.findViewById(R.id.cancel_button);
				buttonCancel.setText("Cancel");
				buttonOK.setOnClickListener(onCreateListener);
				buttonCancel.setOnClickListener(onCancelListener);
				return new AlertDialog.Builder(ctw)
						.setView(view)
						.setOnCancelListener(onBackListener)
						.setMessage(CREATE_GROUP_MESSAGE)
						.create();

			case DIALOG_KEY_DELETE_GROUP:
				view = factory.inflate(R.layout.dialog_group_view, null);
				tvGroup = (TextView) view.findViewById(R.id.groupname_input);
				tvGroup.setText(selectedParticipant.getJid());
				tvGroup.setEnabled(false);
				String name = selectedParticipant.getJid().substring(selectedParticipant.getJid().lastIndexOf("/")+1);
				tvGroupDesc = (TextView) view.findViewById(R.id.groupname_label);
				tvGroupDesc.setText(name.substring(0,1).toUpperCase().concat(name.substring(1)));
				buttonOK = (Button) view.findViewById(R.id.ok_button);
				buttonOK.setText("Delete");
				buttonCancel = (Button) view.findViewById(R.id.cancel_button);
				buttonCancel.setText("Cancel");
				buttonOK.setOnClickListener(onDeleteListener);
				buttonCancel.setOnClickListener(onCancelListener);
				return new AlertDialog.Builder(ctw)
						.setView(view)
						.setOnCancelListener(onBackListener)
						.setMessage(DELETE_GROUP_MESSAGE)
						.create();

			case DIALOG_KEY_ACCEPT_REJECT:
				view = factory.inflate(R.layout.dialog_group_view, null);
				tvGroup = (TextView) view.findViewById(R.id.groupname_input);
				tvGroup.setText(selectedParticipant.getJid());
				tvGroup.setEnabled(false);
				buttonOK = (Button) view.findViewById(R.id.ok_button);
				buttonOK.setText("Accept");
				buttonCancel = (Button) view.findViewById(R.id.cancel_button);
				buttonCancel.setText("Reject");
				buttonOK.setOnClickListener(onJoinListener);
				buttonCancel.setOnClickListener(onRejectListener);
				return new AlertDialog.Builder(ctw)
						.setView(view)
						.setOnCancelListener(onBackListener)
						.setMessage(ACCEPT_GROUP_MESSAGE)
						.create();

			case DIALOG_KEY_CANCEL_INVITE:
				view = factory.inflate(R.layout.dialog_group_view, null);
				tvGroup = (TextView) view.findViewById(R.id.groupname_input);
				tvGroup.setText(selectedParticipant.getJid());
				tvGroup.setEnabled(false);
				buttonOK = (Button) view.findViewById(R.id.ok_button);
				buttonOK.setText("Cancel");
				buttonCancel = (Button) view.findViewById(R.id.cancel_button);
				buttonCancel.setText("Back");
				buttonOK.setOnClickListener(onDeleteListener);
				buttonCancel.setOnClickListener(onCancelListener);
				return new AlertDialog.Builder(ctw)
						.setView(view)
						.setOnCancelListener(onBackListener)
						.setMessage(CANCEL_INVITE_MESSAGE)
						.create();

			case DIALOG_KEY_REFRESH_GROUP:
				return new AlertDialog.Builder(PDXParticipantActivity.this)
						.setIcon(R.drawable.ic_launcher)
						.setTitle(CHANGE_REFRESH_MESSAGE)
						.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
							}
						})
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
							}
						})
						.create();

		}
		return null;
	}

	private View.OnClickListener onCreateListener = new View.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Log.d(TAG,"onAddListener..."+tvGroup.getText());
			if(dialog_create!=null) dialog_create.dismiss();
			sendBroadcast(new Intent("GROUPS_BROADCAST"));
		}
	};

	private View.OnClickListener onDeleteListener = new View.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Log.d(TAG,"onDeleteListener..."+tvGroup.getText());
			if(dialog_delete!=null) dialog_delete.dismiss();
			if(dialog_accept!=null) dialog_accept.dismiss();
			if(dialog_cancel!=null) dialog_cancel.dismiss();
			sendBroadcast(new Intent("GROUPS_BROADCAST"));
		}
	};

	private View.OnClickListener onJoinListener = new View.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Log.d(TAG,"onAddListener..."+tvGroup.getText());
			if(dialog_accept!=null) dialog_accept.dismiss();
			sendBroadcast(new Intent("GROUPS_BROADCAST"));
		}
	};

	private View.OnClickListener onRejectListener = new View.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Log.d(TAG,"onAddListener..."+tvGroup.getText());
			if(dialog_accept!=null) dialog_accept.dismiss();
			sendBroadcast(new Intent("GROUPS_BROADCAST"));
		}
	};

	private View.OnClickListener onCancelListener = new View.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Log.d(TAG,"onCancelListener...");
			if(dialog_create!=null) dialog_create.dismiss();
			if(dialog_delete!=null) dialog_delete.dismiss();
			if(dialog_accept!=null) dialog_accept.dismiss();
			if(dialog_cancel!=null) dialog_cancel.dismiss();
			sendBroadcast(new Intent("GROUPS_BROADCAST"));
		}
	};

	private DialogInterface.OnCancelListener onBackListener = new DialogInterface.OnCancelListener(){
		@Override
		public void onCancel(DialogInterface arg0) {
			Log.d(TAG,"onBackPressed...");
			if(dialog_create!=null) dialog_create.dismiss();
			if(dialog_delete!=null) dialog_delete.dismiss();
			if(dialog_accept!=null) dialog_accept.dismiss();
			if(dialog_cancel!=null) dialog_cancel.dismiss();
			sendBroadcast(new Intent("GROUPS_BROADCAST"));
		}
	};
}
