package com.os4.ecb.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.os4.ecb.Constant;
import com.os4.ecb.R;
import com.os4.ecb.beans.DiscoInfo;
import com.os4.ecb.beans.DiscoItems;
import com.os4.ecb.beans.FileTransferInfo;
import com.os4.ecb.beans.Messages;
import com.os4.ecb.holder.MessageHolder;
import com.os4.ecb.misc.EmoticonGenerator;
import com.os4.ecb.misc.Format;
import com.os4.ecb.misc.ImageUtility;
import com.os4.ecb.misc.Properties;
import com.os4.ecb.service.aidl.IPDXServiceExt;
import com.os4.ecb.service.aidl.IPDXServiceListenerExt;
import com.os4.ecb.session.SessionUser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PDXChatGroupActivity extends BaseActivity {

	private static final String TAG = PDXChatGroupActivity.class.getName();
	IPDXServiceExt pdxService = null;

	private EditText msgText;
	private ListView msgListView;
	private SessionUser sessionUser;
	private DiscoItems.Item selectedGroup;
	private Messages messages = new Messages();
	private HashMap<String,FileTransferInfo> transferFiles = new HashMap<String,FileTransferInfo>();
	private String fileAttachment = new String();
	private DiscoInfo discoInfo;

	private final int DIALOG_KEY_DOWNLOAD_PROGRESS = 1;
	private final int DIALOG_KEY_IMAGE_PREVIEW = 2;
	private final int DIALOG_KEY_EMOTICON_PREVIEW = 3;
	private final int DIALOG_KEY_SECURED_ROOM = 4;
	private final int DIALOG_KEY_INVITE_GROUP = 5;

	private String ENTER_SECURED_ROOM = "Please enter the password";
	private String INVITE_PARTICIPANT_MESSAGE = "Do you want to invite participant ?";

	private ProgressDialog dialog_download;
	private Dialog dialog_preview;
	private Dialog dialog_emoticon;
	private Dialog dialog_enter;
	private Dialog dialog_invite;

	public static final int TAKE_PICTURE = 1;
	public static final int TAKE_GALERY = 2;

	private EditText etPassword;
	private EditText etParticipant;
	private Button  buttonOK;
	private Button  buttonCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_chat_view);

		String json = getIntent().getStringExtra("json");
		String msg = getIntent().getStringExtra("msg");

		selectedGroup = (new Gson()).fromJson(json, DiscoItems.Item.class);
		getSupportActionBar().setTitle(selectedGroup.getName());
		getSupportActionBar().setSubtitle(selectedGroup.getJid());

		if(msg!=null){
			Messages.Message message = (new Gson()).fromJson(msg, Messages.Message.class);
			if(message.getFileInfo()!=null) {
				FileTransferInfo fileTransferInfo = message.getFileInfo();
				transferFiles.put(fileTransferInfo.getId(), fileTransferInfo);
			}
		}

		msgText = (EditText) this.findViewById(R.id.chat_input);
		msgListView = (ListView) findViewById(R.id.chat_message_list);
		msgListView.setStackFromBottom(true);
		msgListView.setDividerHeight(0);

		ImageView sendButton = (ImageView) this.findViewById(R.id.chat_send_message);
		sendButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				try{
					String msg = msgText.getText().toString();
					if(msg.length()>0) processMessage(msg);
				}catch(Exception e){e.printStackTrace();}
			}
		});
	}

	private MessageReceiver updateMessageReceiver = new MessageReceiver();
	public class MessageReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG,"onUpdate Messages Group");
			try {
				String json = intent.getStringExtra("json");
				Messages.Message message = (new Gson()).fromJson(json, Messages.Message.class);
				if(message.getSubject()!=null){
					getSupportActionBar().setSubtitle(message.getSubject());
				}
				String msg = (String) pdxService.getMessagesGroup(selectedGroup.getJid());
				if(msg!=null && !msg.equalsIgnoreCase("null")) {
					messages = (new Gson()).fromJson(msg, Messages.class);
					msgListView.setAdapter(new MessagesAdapter());
				}
			}catch(Exception e){
				Log.e(TAG,e.getMessage());
			}
		}
	}

	private DiscoInfoReceiver updateDiscoInfoReceiver = new DiscoInfoReceiver();
	public class DiscoInfoReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG,"onUpdate Group Info");
			String json = intent.getStringExtra("json");
			discoInfo = (new Gson()).fromJson(json,DiscoInfo.class);

			boolean muc_passwordprotected = false;
			for (String feature : discoInfo.getFeatures()) {
				if (feature.equalsIgnoreCase("muc_passwordprotected")) {
					muc_passwordprotected = true;
					break;
				}
			}
			if (muc_passwordprotected) {
				dialog_enter = PDXChatGroupActivity.this.onCreateDialog(DIALOG_KEY_SECURED_ROOM);
				dialog_enter.show();
			} else {
				try {
					pdxService.joinRoom(selectedGroup.getJid(),null);
				} catch (Exception e) {
					Log.e(TAG, e.getMessage());
				}
			}
		}
	}

    @Override
	protected void onPause() {
    	super.onPause();
    	Log.d(TAG, "onPause .....");
		if(dialog_download!=null) dialog_download.dismiss();
		if(dialog_preview!=null) dialog_preview.dismiss();
		if(dialog_emoticon!=null) dialog_emoticon.dismiss();
		if(dialog_enter!=null) dialog_enter.dismiss();
		if(dialog_invite!=null) dialog_invite.dismiss();
		try{
			if(pdxService!=null)
				pdxService.setCurrentGroup(null);
		}catch(Exception e){
			Log.e(TAG,e.getMessage());
		}
		unregisterReceiver(updateMessageReceiver);
		unregisterReceiver(updateDiscoInfoReceiver);
    	mConnection.onServiceDisconnected(null);
    	unbindService(mConnection);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume .....");
		registerReceiver(updateMessageReceiver, new IntentFilter("MESSAGE_BROADCAST"));
		registerReceiver(updateDiscoInfoReceiver, new IntentFilter("DISCOINFO_BROADCAST"));
		bindService(new Intent(IPDXServiceExt.class.getName()),mConnection, Context.BIND_AUTO_CREATE);
	}

	private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            pdxService = IPDXServiceExt.Stub.asInterface(service);
            try{
				if(pdxService!=null) {
					pdxService.addServiceListener(serviceListener);
					pdxService.setCurrentGroup(selectedGroup.getJid());
					pdxService.getDiscoInfo(selectedGroup.getJid());
					String user = pdxService.getSessionUser();
					sessionUser = (new Gson()).fromJson(user,SessionUser.class);
				}
            }catch(Exception e){e.printStackTrace();}
        }
        public void onServiceDisconnected(ComponentName className) {
        	try{
        		if(pdxService!=null && serviceListener!=null) pdxService.removeServiceListener(serviceListener);
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
		public void onUpdateParticipant(String json) throws RemoteException {}
		@Override
		public void onUpdateServiceInfo(String json) throws RemoteException {}
		@Override
		public void onUpdateGroupInfo(String json) throws RemoteException {
			Intent intent = new Intent("DISCOINFO_BROADCAST");
			intent.putExtra("json",json);
			sendBroadcast(intent);
		}
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
		public void onMessage(String json) {}
		@Override
		public void onMessageGroup(String json) throws RemoteException {
			Intent intent = new Intent("MESSAGE_BROADCAST");
			intent.putExtra("json",json);
			sendBroadcast(intent);
		}
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
		public void onTransferFile(String json) throws RemoteException {

			FileTransferInfo fileTransferInfo = (new Gson()).fromJson(json,FileTransferInfo.class);
			if(fileTransferInfo.getTag().equalsIgnoreCase("si")){
				transferFiles.put(fileTransferInfo.getId(),fileTransferInfo);
			}else if(fileTransferInfo.getTag().equalsIgnoreCase("open")){
				FileTransferInfo fileInfo = (FileTransferInfo) transferFiles.get(fileTransferInfo.getSid());
				fileInfo.setBlockSize(fileTransferInfo.getBlockSize());
				transferFiles.put(fileInfo.getId(),fileInfo);
			}else if(fileTransferInfo.getTag().equalsIgnoreCase("data")){
				FileTransferInfo fileInfo = (FileTransferInfo) transferFiles.get(fileTransferInfo.getSid());
				fileInfo.setSeq(fileTransferInfo.getSeq());
				StringBuilder buffer = new StringBuilder();
				if(fileInfo.getBase64()!=null) buffer.append(fileInfo.getBase64());
				buffer.append(fileTransferInfo.getBase64());
				fileInfo.setBase64(buffer.toString());
				transferFiles.put(fileInfo.getId(),fileInfo);
				double percentage = ((double)fileInfo.getBlockSize()/(double)fileInfo.getSize())*(fileInfo.getSeq()+1)*100;
				if(dialog_download.isShowing()) dialog_download.setProgress(((int)percentage));
			}else if(fileTransferInfo.getTag().equalsIgnoreCase("close")){
				FileTransferInfo fileInfo = (FileTransferInfo) transferFiles.get(fileTransferInfo.getSid());
				ImageUtility.saveImageFile(fileInfo);
				if(dialog_download.isShowing()) {
					dialog_download.setProgress(100);
					dialog_download.dismiss();
				}
				ImageUtility.createThumbnailImageFile(fileInfo);
				transferFiles.remove(fileInfo.getId());
				sendBroadcast(new Intent("MESSAGE_BROADCAST"));
			}
		}
		@Override
		public void onIQPacket(String json) throws RemoteException {}
		@Override
		public void onMessagePacket(String json) throws RemoteException {}
		@Override
		public void onErrorResponse(String error) throws RemoteException {}
		@Override
		public void onSignOut() throws RemoteException {}
    };

	@Override
	protected Dialog onCreateDialog(int id) {
		LayoutInflater factory = LayoutInflater.from(this);
		View view;
		switch (id)
		{
			case DIALOG_KEY_DOWNLOAD_PROGRESS:
				ProgressDialog progressDialog = new ProgressDialog(this);
				progressDialog.setMessage("Downloading file..");
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.setCancelable(false);
				progressDialog.show();
				return progressDialog;

			case DIALOG_KEY_IMAGE_PREVIEW:
				LayoutInflater factoryPreview = LayoutInflater.from(this);
				View dlgPreview = factoryPreview.inflate(R.layout.dialog_image_view, null);
				ImageView preView = (ImageView) dlgPreview.findViewById(R.id.file_preview);
				String previewFile = ImageUtility.createThumbnailImageFile(fileAttachment);
				preView.setImageURI(Uri.parse(previewFile));

				Button buttonOK = (Button) dlgPreview.findViewById(R.id.ok_button);
				buttonOK.setText("Send");
				Button buttonCancel = (Button) dlgPreview.findViewById(R.id.cancel_button);
				buttonCancel.setText("Cancel");
				buttonOK.setOnClickListener(onSendPictureListener);
				buttonCancel.setOnClickListener(onCancelListener);

				return new AlertDialog.Builder(this)
						.setView(dlgPreview)
						.setOnCancelListener(onBackListener)
						.create();

			case DIALOG_KEY_EMOTICON_PREVIEW:
				LayoutInflater factoryEmoPreview = LayoutInflater.from(this);
				View dlgPreviewEmo = factoryEmoPreview.inflate(R.layout.dialog_emo_view, null);
				GridView gridView = (GridView) dlgPreviewEmo.findViewById(R.id.gridview);
				Map<Integer,String> emoticons = EmoticonGenerator.getEmoticonResources();
				Integer[] EmoticonResources = new Integer[emoticons.size()];
				EmoticonResources = (Integer[]) emoticons.keySet().toArray(EmoticonResources);
				gridView.setAdapter(new ImageAdapter(this,EmoticonResources));
				gridView.setOnItemClickListener(onEmoticonListener);
				return new AlertDialog.Builder(this)
						.setView(dlgPreviewEmo)
						.setOnCancelListener(onBackListener)
						.create();

			case DIALOG_KEY_SECURED_ROOM:
				LayoutInflater factoryEnterPreview = LayoutInflater.from(this);
				View viewEnter = factoryEnterPreview.inflate(R.layout.dialog_room_secret, null);
				etPassword = (EditText) viewEnter.findViewById(R.id.password_input);
				buttonOK = (Button) viewEnter.findViewById(R.id.ok_button);
				buttonOK.setText("Ok");
				buttonCancel = (Button) viewEnter.findViewById(R.id.cancel_button);
				buttonCancel.setText("Cancel");
				buttonOK.setOnClickListener(onSecuredRoomListener);
				buttonCancel.setOnClickListener(onCancelListener);
				return new AlertDialog.Builder(this)
						.setView(viewEnter)
						.setOnCancelListener(onBackListener)
						.setMessage(ENTER_SECURED_ROOM)
						.create();

			case DIALOG_KEY_INVITE_GROUP:
				view = factory.inflate(R.layout.dialog_contact_view, null);
				etParticipant = (EditText) view.findViewById(R.id.username_input);
				buttonOK = (Button) view.findViewById(R.id.ok_button);
				buttonOK.setText("Add");
				buttonCancel = (Button) view.findViewById(R.id.cancel_button);
				buttonCancel.setText("Cancel");
				buttonOK.setOnClickListener(onInviteParticipantListener);
				buttonCancel.setOnClickListener(onCancelListener);
				return new AlertDialog.Builder(this)
						.setView(view)
						.setOnCancelListener(onBackListener)
						.setMessage(INVITE_PARTICIPANT_MESSAGE)
						.create();
		}
		return null;
	}

	private DialogInterface.OnCancelListener onBackListener = new DialogInterface.OnCancelListener(){
		@Override
		public void onCancel(DialogInterface arg0) {
			Log.d(TAG,"onBackPressed...");
			if(dialog_download!=null) dialog_download.dismiss();
			if(dialog_preview!=null) dialog_preview.dismiss();
			if(dialog_emoticon!=null) dialog_emoticon.dismiss();
			if(dialog_enter!=null) dialog_enter.dismiss();
			if(dialog_invite!=null) dialog_invite.dismiss();
		}
	};

	public void onClickEmoticon(View view) {
		Log.d(TAG,"Onclick Emoticon");
		dialog_emoticon = onCreateDialog(DIALOG_KEY_EMOTICON_PREVIEW);
		dialog_emoticon.show();
	}

	private AdapterView.OnItemClickListener onEmoticonListener = new AdapterView.OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position,long arg3) {
			String msg = msgText.getText().toString();
			Integer resource = (Integer) view.getTag();
			msg = msg + EmoticonGenerator.getPatern(resource);
			msgText.setText(msg);
			dialog_emoticon.dismiss();
		}
	};

	private View.OnClickListener onSendPictureListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			try{
				pdxService.sendTransferFile(selectedGroup.getJid(),fileAttachment);
				if(dialog_preview!=null) dialog_preview.dismiss();
			}catch(Exception e){
				Log.e(TAG,e.getMessage());
			}
		}
	};

	private View.OnClickListener onSecuredRoomListener = new View.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Log.d(TAG,"On Enter Secured Room");
			try{
				String password = etPassword.getText().toString();
				pdxService.joinRoom(selectedGroup.getJid(),password);
			}catch(Exception e){e.printStackTrace();}
			if(dialog_enter!=null) dialog_enter.dismiss();
		}
	};

	private View.OnClickListener onInviteParticipantListener = new View.OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Log.d(TAG,"On Invite Participant");
			if(dialog_invite!=null) dialog_invite.dismiss();
		}
	};

	private View.OnClickListener onCancelListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d(TAG,"onCancelListener...");
			if(dialog_download!=null) dialog_download.dismiss();
			if(dialog_preview!=null) dialog_preview.dismiss();
			if(dialog_emoticon!=null) dialog_emoticon.dismiss();
			if(dialog_enter!=null) dialog_enter.dismiss();
			if(dialog_invite!=null) dialog_invite.dismiss();
		}
	};

	public class ImageAdapter extends BaseAdapter {

		private Context mContext;
		private Integer[] EmoticonResources;

		public ImageAdapter(Context c,Integer[] EmoticonResources) {
			mContext = c;
			this.EmoticonResources = EmoticonResources;
		}

		public int getCount() {
			return EmoticonResources.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				imageView.setAdjustViewBounds(false);
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				imageView.setPadding(2, 2, 2, 2);
			} else {
				imageView = (ImageView) convertView;
			}

			imageView.setImageResource(EmoticonResources[position]);
			imageView.setTag(EmoticonResources[position]);
			return imageView;
		}
	}

	public void processMessage(String msg){
		try{
			msgText.getText().clear();
			pdxService.sendMessageGroup(selectedGroup.getJid(),msg,null);
		}catch(Exception e){e.printStackTrace();}
	}

	private class MessagesAdapter extends BaseAdapter {

		public int getCount() {
			return messages.getMessageList().size();
		}

		public Object getItem(int position) {
			return messages.getMessageList().get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup viewgroup) {

			Messages.Message message = messages.getMessageList().get(position);
			final MessageHolder holder = new MessageHolder();

			LayoutInflater mInflater = PDXChatGroupActivity.this.getLayoutInflater();
			if(!message.getFrom().equalsIgnoreCase(selectedGroup.getJid()+"/"+sessionUser.getNickName())){
				if (message.getFileInfo() != null)
					convertView = mInflater.inflate(R.layout.view_message_img_contact, null);
				else convertView = mInflater.inflate(R.layout.view_message_contact, null);
			} else {
				if (message.getFileInfo() != null)
					convertView = mInflater.inflate(R.layout.view_message_img_user, null);
				else convertView = mInflater.inflate(R.layout.view_message_user, null);
			}

			holder.avatar = (ImageView) convertView.findViewById(R.id.chatavatar);
			holder.msg = (TextView) convertView.findViewById(R.id.chatmessagetext);
			holder.img = (ImageView) convertView.findViewById(R.id.chatmessageimage);
			holder.date = (TextView) convertView.findViewById(R.id.chatmessagedate);
			holder.name = (TextView) convertView.findViewById(R.id.chatname);
			holder.textContainer = (RelativeLayout) convertView.findViewById(R.id.text_container);

			try{
				if(!message.getFrom().equalsIgnoreCase(selectedGroup.getJid()+"/"+sessionUser.getNickName())){
					holder.avatar.setImageResource(R.drawable.ic_user);
					FileTransferInfo fileInfo = message.getFileInfo();
					if(fileInfo!=null && fileInfo.getName()!=null) {
						File file = new File(Constant.THUMBNAILS_PATH, fileInfo.getName());
						if (file.exists()) {
							Uri uri;

							if(fileInfo.getName().startsWith("file://")) uri = Uri.parse(fileInfo.getName());
							else uri = Uri.parse("file://" + Constant.THUMBNAILS_PATH + fileInfo.getName());

							holder.img.setImageURI(uri);
							holder.img.setTag(message);
							holder.img.setOnClickListener(onImageViewListener);
							String filename = (new File(message.getFileInfo().getName())).getName();
							holder.msg.setText(filename);
						} else {
							holder.img.setImageResource(R.drawable.download);
							holder.img.setTag(message);
							holder.img.setOnClickListener(onImageDownloadListener);
							holder.msg.setText(message.getFileInfo().getName());
						}
					}else {
						holder.msg.setText(message.getTextMessage());
					}
					holder.date.setText(Format.formatDateTime(message.getDateTime()));
					holder.name.setText(message.getNickname());
					holder.textContainer.setBackgroundColor(Properties.BACKGROUND_PARTNERCHAT);
				}else {

					holder.avatar.setImageResource(R.drawable.ic_user);

					FileTransferInfo fileInfo = message.getFileInfo();
					if(fileInfo!=null && fileInfo.getName()!=null) {
						Uri uri;

						if(fileInfo.getName().startsWith("file://")) uri = Uri.parse(fileInfo.getName());
						else uri = Uri.parse("file://" + Constant.THUMBNAILS_PATH + fileInfo.getName());

						holder.img.setImageURI(uri);
						holder.img.setTag(message);
						holder.img.setOnClickListener(onImageViewListener);
						String filename = (new File(message.getFileInfo().getName())).getName();
						holder.msg.setText(filename);
					}else {
						holder.msg.setText(message.getTextMessage());
					}
					holder.date.setText(Format.formatDateTime(message.getDateTime()));
					holder.textContainer.setBackgroundColor(Properties.BACKGROUND_MYCHAT);
				}
			}catch(Exception e){
				Log.e(TAG,e.getMessage());
			}

			convertView.setTag(holder);
			return convertView;
		}
	}

	private View.OnClickListener onImageDownloadListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d(TAG,"onImageDownloadListener...");
			try{
				Messages.Message msg = (Messages.Message) v.getTag();
				if(pdxService!=null) pdxService.acceptTransferFile(msg.getId(),selectedGroup.getJid());
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
				dialog_download = (ProgressDialog) onCreateDialog(DIALOG_KEY_DOWNLOAD_PROGRESS);
				dialog_download.show();
			}catch(Exception e){
				Log.e(TAG,e.getMessage());
			}
		}
	};

	private View.OnClickListener onImageViewListener = new View.OnClickListener(){
		@Override
		public void onClick(View view) {
			Log.d(TAG,"onImageViewListener...");
			Messages.Message msg = (Messages.Message) view.getTag();
			File imgFile = new File(Constant.IMAGES_FILE_PATH + msg.getFileInfo().getName());
			if(imgFile.exists()){
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse("file://" + Constant.IMAGES_FILE_PATH + msg.getFileInfo().getName()), "image/*");
				startActivity(intent);
			}
		}
	};

	private void clearNotification(){
		NotificationManager notifier = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notifier.cancel(Constant.NOTIFICATION_CHAT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actions_chat_group, menu);
		return true;
	}

	public void onClickCamera(MenuItem item) {
		Log.d(TAG,"Onclick Camera");
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileAttachment = "file://" + Constant.IMAGES_FILE_PATH + "IMG_"+Format.formatFileDate()+".jpg";
		Uri uri  = Uri.parse(fileAttachment);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("android.intent.extra.sizeLimit",1048576L);
		startActivityForResult(intent,TAKE_PICTURE);
	}

	public void onClickGallery(MenuItem item) {
		Log.d(TAG,"Onclick Galery");
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent,TAKE_GALERY);
	}

	public void onGetProfile(MenuItem item) {
		Log.d(TAG,"onGetProfile");
	}

	public void onViewParticipants(MenuItem item) {
		Log.d(TAG,"onViewParticipants");
		Intent intent = new Intent(PDXChatGroupActivity.this,PDXParticipantActivity.class);
		intent.putExtra("jid",selectedGroup.getJid());
		startActivity(intent);
	}

	public void onInvite(MenuItem item) {
		Log.d(TAG,"onInvite Group");
		dialog_invite = onCreateDialog(DIALOG_KEY_INVITE_GROUP);
		dialog_invite.show();
	}

	public void onLeave(MenuItem item) {
		Log.d(TAG,"onLeave Group");
	}

	public void onClearMessageHistory(MenuItem item) {
		Log.d(TAG,"Onclick Clear Message History");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d(TAG, "onActivityResult");
		if (resultCode == RESULT_OK){
			if (requestCode == TAKE_PICTURE){
				try{
					if(data!=null){
						Log.d(TAG, "onActivityResult : TAKE_PICTURE");
						Bitmap bm = data.getParcelableExtra("data");
						String base64 = ImageUtility.toBase64(bm,100);
						String filename = ImageUtility.saveImageFile(base64);
						fileAttachment = filename;
					}
					Log.e(TAG,fileAttachment);
					dialog_preview = onCreateDialog(DIALOG_KEY_IMAGE_PREVIEW);
					dialog_preview.show();

				}catch(Exception e){
					Log.e(TAG,e.getMessage());
				}
			}
			else if (requestCode == TAKE_GALERY){
				try{
					Log.d(TAG, "onActivityResult : TAKE_GALERY");
					Uri uri = data.getData();
					Log.d(TAG,uri.toString());
					String filename = uri.toString();

					if (filename.startsWith("content://")) {
						Cursor cursor = null;
						try {
							cursor = getContentResolver().query(uri, null, null, null, null);
							if (cursor != null && cursor.moveToFirst()) {
								filename = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
								filename = "file://" + filename;
							}
						} finally {
							cursor.close();
						}
					}
					Log.d(TAG,filename);
					fileAttachment = filename;
					dialog_preview = onCreateDialog(DIALOG_KEY_IMAGE_PREVIEW);
					dialog_preview.show();

				}catch(Exception e){
					Log.e(TAG,e.getMessage());
				}
			}
			else if (requestCode==Constant.CONTACT_ACTIVITY && resultCode==Constant.RESULT_OK){
				try{
					setResult(Constant.RESULT_OK);
					finish();
				}catch(Exception e){
					Log.e(TAG,e.getMessage());
				}
			}
		}
	}
}
