package com.os4.ecb.activity;

import com.google.gson.Gson;
import com.os4.ecb.Constant;
import com.os4.ecb.R;
import com.os4.ecb.beans.RosterItems;
import com.os4.ecb.holder.ContactHolder;
import com.os4.ecb.misc.Format;
import com.os4.ecb.misc.ImageUtility;
import com.os4.ecb.misc.Properties;
import com.os4.ecb.service.aidl.IPDXServiceExt;
import com.os4.ecb.service.aidl.IPDXServiceListenerExt;
import com.os4.ecb.misc.Base64;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.preference.PreferenceManager;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class PDXContactActivity extends BaseActivity {

	private static final String TAG = PDXContactActivity.class.getName();
	private IPDXServiceExt pdxService = null;
	private ListView contactsView;
	private RosterItems rosterItems = new RosterItems();
	private SharedPreferences preferences;

	private final int DIALOG_KEY_PROCESS = 1;
	private final int DIALOG_KEY_ADD_CONTACT = 2;
	private final int DIALOG_KEY_DELETE_CONTACT = 3;
	private final int DIALOG_KEY_ACCEPT_REJECT = 4;
	private final int DIALOG_KEY_CANCEL_REQUEST = 5;
	private final int DIALOG_KEY_TELL_FRIEND = 6;

	private String ADD_CONTACT_MESSAGE = "Do you want to add from your contact number ?";
	private String DELETE_CONTACT_MESSAGE = "Do you want to delete your friend ?";
	private String ACCEPT_CONTACT_MESSAGE = "Do you want to accept friend's request ?";
	private String CANCEL_REQUEST_MESSAGE = "Do you want to cancel friend's request ?";
	private String TELL_FRIEND_MESSAGE = "Please tell your friend about "+ Properties.resource + ", send to :";

	private Dialog dialog_process;
	private Dialog dialog_add;
	private Dialog dialog_delete;
	private Dialog dialog_accept;
	private Dialog dialog_cancel;

	private TextView tvUser;
	private Button  buttonOK;
	private Button buttonCancel;

	private RosterItems.Item selectedContact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);

		setContentView(R.layout.main_contacts_view);
		contactsView = (ListView) findViewById(R.id.list_view);
		contactsView.setAdapter(new ContactsAdapter());
		contactsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectedContact = rosterItems.getItems().get(position);
				if(selectedContact.getSubscription().equalsIgnoreCase("both")) {
					Intent intent = new Intent(PDXContactActivity.this, PDXChatActivity.class);
					intent.putExtra("json", (new Gson()).toJson(selectedContact));
					startActivity(intent);
				}
				else if(selectedContact.getAsk()==null && !selectedContact.getSubscription().equalsIgnoreCase("both")){
					dialog_accept = onCreateDialog(DIALOG_KEY_ACCEPT_REJECT);
					dialog_accept.show();
				}
				else if(selectedContact.getAsk()!=null && !selectedContact.getSubscription().equalsIgnoreCase("both")){
					dialog_cancel = onCreateDialog(DIALOG_KEY_CANCEL_REQUEST);
					dialog_cancel.show();
				}
			}
		});
		contactsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				selectedContact = rosterItems.getItems().get(position);
				dialog_delete = onCreateDialog(DIALOG_KEY_DELETE_CONTACT);
				dialog_delete.show();
				return true;
			}
		});
	}

	private RosterReceiver updateRosterReceiver = new RosterReceiver();
	public class RosterReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG,"onUpdate Contacts View");
			try {
				String json = (String) pdxService.getContacts();
				if(json!=null && !json.equalsIgnoreCase("null")) {
					rosterItems = (RosterItems) (new Gson()).fromJson(json, RosterItems.class);
					contactsView.setAdapter(new ContactsAdapter());
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
		unregisterReceiver(updateRosterReceiver);
    	mConnection.onServiceDisconnected(null);
    	unbindService(mConnection);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume .....");
		registerReceiver(updateRosterReceiver, new IntentFilter("ROSTER_BROADCAST"));
		bindService(new Intent(IPDXServiceExt.class.getName()),mConnection, Context.BIND_AUTO_CREATE);
	}

	private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            pdxService = IPDXServiceExt.Stub.asInterface(service);
            try{
				if(pdxService!=null) {
					pdxService.addServiceListener(serviceListener);
					pdxService.getRosters();
					String json = (String) pdxService.getContacts();
					if(json!=null && !json.equalsIgnoreCase("null")) {
						rosterItems = (RosterItems) (new Gson()).fromJson(json, RosterItems.class);
						contactsView.setAdapter(new ContactsAdapter());
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
		public void onGetRosters(String json) throws RemoteException {
			sendBroadcast(new Intent("ROSTER_BROADCAST"));
		}
		@Override
		public void onPresence(String json) throws RemoteException {
			sendBroadcast(new Intent("ROSTER_BROADCAST"));
		}
		@Override
		public void onPresenceGroup(String json) throws RemoteException {}
		@Override
		public void onUpdateAvatar(String json) throws RemoteException {
			sendBroadcast(new Intent("ROSTER_BROADCAST"));
		}
		@Override
		public void onUpdateService(String json) throws RemoteException {}
		@Override
		public void onUpdateGroup(String json) throws RemoteException {}
		@Override
		public void onUpdateParticipant(String json) throws RemoteException {}
		@Override
		public void onUpdateServiceInfo(String json) throws RemoteException {}
		@Override
		public void onUpdateGroupInfo(String json) throws RemoteException {}
		@Override
		public void onSubscribe(String json) throws RemoteException {
			sendBroadcast(new Intent("ROSTER_BROADCAST"));
		}
		@Override
		public void onUnSubscribe(String json) throws RemoteException {
			sendBroadcast(new Intent("ROSTER_BROADCAST"));
		}
		@Override
		public void onAddRoster(String json) throws RemoteException {
			sendBroadcast(new Intent("ROSTER_BROADCAST"));
		}
		@Override
		public void onUpdateRoster(String json) throws RemoteException {
			sendBroadcast(new Intent("ROSTER_BROADCAST"));
		}
		@Override
		public void onDeleteRoster(String json) throws RemoteException {
			sendBroadcast(new Intent("ROSTER_BROADCAST"));
		}
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

	private class ContactsAdapter extends BaseAdapter {

		public int getCount() {
			return rosterItems.getItems().size();
		}

		public Object getItem(int position) {
			return rosterItems.getItems().get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup viewgroup) {

			ContactHolder holder = new ContactHolder();

			if (convertView == null) {
				LayoutInflater mInflater = PDXContactActivity.this.getLayoutInflater();
				convertView = mInflater.inflate(R.layout.view_contact, null);
				holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
				holder.name = (TextView) convertView.findViewById(R.id.contact_user);
				holder.resource = (TextView) convertView.findViewById(R.id.contact_resource);
				holder.status = (TextView) convertView.findViewById(R.id.contact_message);
				holder.datetime = (TextView) convertView.findViewById(R.id.contact_datetime);
				holder.indicator = (ImageView) convertView.findViewById(R.id.indicator);
				holder.unread = (TextView) convertView.findViewById (R.id.unread_count);
				holder.msgcounter = (ImageView) convertView.findViewById (R.id.msg_counter);
				convertView.setTag(holder);
			}
			else{
				holder = (ContactHolder) convertView.getTag();
			}

			try {
				RosterItems.Item item = (RosterItems.Item) rosterItems.getItems().get(position);
				if(item.getPhotoBinVal()!=null && item.getPhotoBinVal().trim().length()>0) {
					byte[] bytes = Base64.decode(item.getPhotoBinVal());
					Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
					holder.avatar.setImageBitmap(ImageUtility.getRoundedCornerBitmap(bm));
				}
				else
					holder.avatar.setImageResource(R.drawable.ic_user);

				if(item.getName()!=null) holder.name.setText(item.getName());
				if(item.getResource()!=null) holder.resource.setText(item.getResource());
				if(!item.getSubscription().equalsIgnoreCase("both")){
					if(item.getAsk()!=null)
						holder.resource.setText("Pending Request");
					else if(item.getAsk()==null)
						holder.resource.setText("Subscription Request");
				}
				if(item.getStatus()!=null) holder.status.setText(item.getStatus());
				if(item.getDateTime()!=null){
					holder.datetime.setTextColor(Color.GRAY);
					holder.datetime.setText(Format.formatDateTime(item.getDateTime()));
				}
				if(item.getUnread()!=0){
					holder.unread.setTextColor(Color.GRAY);
					holder.unread.setText(Integer.toString(item.getUnread()));
					holder.msgcounter.setImageResource(R.drawable.ic_delivered);
				}

				if(item.getShow()!=null)
				{
					if(item.getShow().equalsIgnoreCase("available"))
						holder.indicator.setImageResource(R.drawable.status_available);
					else if(item.getShow().equalsIgnoreCase("chat"))
						holder.indicator.setImageResource(R.drawable.status_available);
					else if(item.getShow().equalsIgnoreCase("away"))
						holder.indicator.setImageResource(R.drawable.status_away);
					else if(item.getShow().equalsIgnoreCase("xa"))
						holder.indicator.setImageResource(R.drawable.status_away);
					else if(item.getShow().equalsIgnoreCase("dnd"))
						holder.indicator.setImageResource(R.drawable.status_dnd);
					else if(item.getShow().equalsIgnoreCase("unavailable")){
						holder.indicator.setImageResource(R.drawable.status_offline);
						holder.status.setText("Offline");
					}else{
						holder.indicator.setImageResource(R.drawable.status_invisible);
						holder.status.setText("Offline");
					}
				}
				else{
					holder.indicator.setImageResource(R.drawable.status_offline);
					holder.status.setText("Offline");
				}

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
			if(requestCode==Constant.CONTACT_ACTIVITY && resultCode==Constant.RESULT_OK){
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
		inflater.inflate(R.menu.action_contacts, menu);
		return true;
	}

	public void onAddContact(MenuItem item) {
		Log.d(TAG,"onAddContact");
		dialog_add = onCreateDialog(DIALOG_KEY_ADD_CONTACT);
		dialog_add.show();
	}

	public void onTellFriend(MenuItem item) {
		Log.d(TAG,"onTellFriend");
		dialog_process = onCreateDialog(DIALOG_KEY_TELL_FRIEND);
		dialog_process.show();
	}

	public void onRefresh(MenuItem item) {
		Log.d(TAG,"onRefresh");
		try{
			pdxService.getRosters();
		}catch(Exception e){
			Log.e(TAG,e.getMessage());
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		LayoutInflater factory = LayoutInflater.from(this);
		ContextThemeWrapper ctw = new ContextThemeWrapper( this, R.style.DialogStyle);
		View view;
		switch (id)
		{
			case DIALOG_KEY_PROCESS:
				ProgressDialog dialog = new ProgressDialog(this);
				dialog.setMessage("Please wait while processing...");
				dialog.setIndeterminate(true);
				dialog.setCancelable(true);
				dialog.setOnCancelListener(onBackListener);
				return dialog;

			case DIALOG_KEY_ADD_CONTACT:
				view = factory.inflate(R.layout.dialog_contact_view, null);
				tvUser = (TextView) view.findViewById(R.id.username_input);
				buttonOK = (Button) view.findViewById(R.id.ok_button);
				buttonOK.setText("Add");
				buttonCancel = (Button) view.findViewById(R.id.cancel_button);
				buttonCancel.setText("Cancel");
				buttonOK.setOnClickListener(onAddListener);
				buttonCancel.setOnClickListener(onCancelListener);
				return new AlertDialog.Builder(ctw)
						.setView(view)
						.setOnCancelListener(onBackListener)
						.setMessage(ADD_CONTACT_MESSAGE)
						.create();

			case DIALOG_KEY_DELETE_CONTACT:
				view = factory.inflate(R.layout.dialog_contact_view, null);
				tvUser = (TextView) view.findViewById(R.id.username_input);
				tvUser.setText(selectedContact.getJid());
				tvUser.setEnabled(false);
				buttonOK = (Button) view.findViewById(R.id.ok_button);
				buttonOK.setText("Delete");
				buttonCancel = (Button) view.findViewById(R.id.cancel_button);
				buttonCancel.setText("Cancel");
				buttonOK.setOnClickListener(onDeleteListener);
				buttonCancel.setOnClickListener(onCancelListener);
				return new AlertDialog.Builder(ctw)
						.setView(view)
						.setOnCancelListener(onBackListener)
						.setMessage(DELETE_CONTACT_MESSAGE)
						.create();

			case DIALOG_KEY_ACCEPT_REJECT:
				view = factory.inflate(R.layout.dialog_contact_view, null);
				tvUser = (TextView) view.findViewById(R.id.username_input);
				tvUser.setText(selectedContact.getJid());
				tvUser.setEnabled(false);
				buttonOK = (Button) view.findViewById(R.id.ok_button);
				buttonOK.setText("Accept");
				buttonCancel = (Button) view.findViewById(R.id.cancel_button);
				buttonCancel.setText("Reject");
				buttonOK.setOnClickListener(onAcceptListener);
				buttonCancel.setOnClickListener(onRejectListener);
				return new AlertDialog.Builder(ctw)
						.setView(view)
						.setOnCancelListener(onBackListener)
						.setMessage(ACCEPT_CONTACT_MESSAGE)
						.create();

			case DIALOG_KEY_CANCEL_REQUEST:
				view = factory.inflate(R.layout.dialog_contact_view, null);
				tvUser = (TextView) view.findViewById(R.id.username_input);
				tvUser.setText(selectedContact.getJid());
				tvUser.setEnabled(false);
				buttonOK = (Button) view.findViewById(R.id.ok_button);
				buttonOK.setText("Ok");
				buttonCancel = (Button) view.findViewById(R.id.cancel_button);
				buttonCancel.setText("Back");
				buttonOK.setOnClickListener(onDeleteListener);
				buttonCancel.setOnClickListener(onCancelListener);
				return new AlertDialog.Builder(ctw)
						.setView(view)
						.setOnCancelListener(onBackListener)
						.setMessage(CANCEL_REQUEST_MESSAGE)
						.create();

			case DIALOG_KEY_TELL_FRIEND:
				view = factory.inflate(R.layout.dialog_contact_view, null);
				tvUser = (TextView) view.findViewById(R.id.username_input);
				//tvUser.setText(selectedMobileNumber);
				buttonOK = (Button) view.findViewById(R.id.ok_button);
				buttonOK.setText("Send");
				buttonCancel = (Button) view.findViewById(R.id.cancel_button);
				buttonCancel.setText("Cancel");
				buttonOK.setOnClickListener(onTellFriendListener);
				buttonCancel.setOnClickListener(onCancelListener);
				return new AlertDialog.Builder(ctw)
						.setView(view)
						.setOnCancelListener(onBackListener)
						.setMessage(TELL_FRIEND_MESSAGE)
						.create();
		}
		return null;
	}

	private View.OnClickListener onAddListener = new View.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Log.d(TAG,"onAddListener..."+tvUser.getText().toString());
			String to = tvUser.getText().toString();
			String name = tvUser.getText().toString();
			if(!to.contains("@")){
				String domain = preferences.getString("domain",Properties.domain);
				to = tvUser.getText().toString() + "@" + domain;
			}else{
				name = to.substring(0,to.lastIndexOf("@"));
			}
			try {
				pdxService.addRoster(to,name,"Friends");
			}catch(Exception e){
				Log.e(TAG,e.getMessage());
			}
			if(dialog_add!=null) dialog_add.dismiss();
		}
	};

	private View.OnClickListener onDeleteListener = new View.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Log.d(TAG,"onDeleteListener..."+selectedContact.getJid());
			try {
				pdxService.deleteRoster(selectedContact.getJid());
			}catch(Exception e){
				Log.e(TAG,e.getMessage());
			}
			if(dialog_cancel!=null) dialog_cancel.dismiss();
			if(dialog_delete!=null) dialog_delete.dismiss();
		}
	};

	private View.OnClickListener onAcceptListener = new View.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Log.d(TAG,"onAcceptListener..."+selectedContact.getJid());
			try {
				String to = selectedContact.getJid();
				String name = to.substring(0,to.lastIndexOf("@"));
				pdxService.addRoster(to,name,"Friends");
				pdxService.acceptSubscription(selectedContact.getJid());
			}catch(Exception e){
				Log.e(TAG,e.getMessage());
			}
			if(dialog_accept!=null) dialog_accept.dismiss();
		}
	};

	private View.OnClickListener onRejectListener = new View.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Log.d(TAG,"onRejectListener..."+selectedContact.getJid());
			try {
				pdxService.rejectSubscription(selectedContact.getJid());
			}catch(Exception e){
				Log.e(TAG,e.getMessage());
			}
			if(dialog_accept!=null) dialog_accept.dismiss();
		}
	};

	private View.OnClickListener onTellFriendListener = new View.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Log.d(TAG,"onTellFriendListener..."+tvUser.getText());
			if(dialog_process!=null) dialog_process.dismiss();
		}
	};

	private View.OnClickListener onCancelListener = new View.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Log.d(TAG,"onCancelListener...");
			if(dialog_process!=null) dialog_process.dismiss();
			if(dialog_add!=null) dialog_add.dismiss();
			if(dialog_delete!=null) dialog_delete.dismiss();
			if(dialog_accept!=null) dialog_accept.dismiss();
			if(dialog_cancel!=null) dialog_cancel.dismiss();
		}
	};

	private DialogInterface.OnCancelListener onBackListener = new DialogInterface.OnCancelListener(){
		@Override
		public void onCancel(DialogInterface arg0) {
			Log.d(TAG,"onBackPressed...");
			if(dialog_process!=null) dialog_process.dismiss();
			if(dialog_add!=null) dialog_add.dismiss();
			if(dialog_delete!=null) dialog_delete.dismiss();
			if(dialog_accept!=null) dialog_accept.dismiss();
			if(dialog_cancel!=null) dialog_cancel.dismiss();
		}
	};
}
