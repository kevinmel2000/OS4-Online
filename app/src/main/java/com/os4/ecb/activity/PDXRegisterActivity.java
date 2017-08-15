package com.os4.ecb.activity;

import com.google.gson.Gson;
import com.os4.ecb.Constant;
import com.os4.ecb.R;
import com.os4.ecb.misc.Properties;
import com.os4.ecb.service.aidl.IPDXServiceExt;
import com.os4.ecb.service.aidl.IPDXServiceListenerExt;
import com.os4.ecb.session.SessionUser;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PDXRegisterActivity extends Activity {

    private static final String TAG = PDXRegisterActivity.class.getName();
    private IPDXServiceExt pdxService = null;
    private String webURL = "file:///android_asset/register.html";
    private WebView webView;
    private Dialog dialog_process;

    private final int DIALOG_KEY_PROCESS = 1;

    @JavascriptInterface
    public void startRegister(String username,String password,String nickname,String email,String birthday,String gender) {
        Log.d(TAG,"startRegister:"+username);
        try{
            dialog_process = onCreateDialog(DIALOG_KEY_PROCESS);
            dialog_process.show();
            Log.d(TAG,"Process Register Start");

            SessionUser sessionUser = new SessionUser();
            sessionUser.setUsername(username);
            sessionUser.setPassword(password);
            if(nickname!=null) sessionUser.setNickName(nickname);
            else sessionUser.setNickName(username.substring(0,1).toUpperCase().concat(username.substring(1)));
            sessionUser.setEmail(email);
            sessionUser.setBirthday(birthday);
            sessionUser.setGender(gender);

            sessionUser.setDomain(Properties.domain);
            sessionUser.setResource(Properties.resource);
            sessionUser.setIp(Properties.host);
            sessionUser.setPort(Properties.port);
            Gson gson = new Gson();
            pdxService.connect(gson.toJson(sessionUser));

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pdxmenu);

        webView = (WebView) findViewById(R.id.webView_id);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.addJavascriptInterface(this,"android");
        webView.loadUrl(webURL);
        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.contains("google")){
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else {
                    view.loadUrl(url);
                    return true;
                }
            }
            @Override
            public void onLoadResource (WebView view, String url) {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(PDXRegisterActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                try{
                    if (progressDialog!=null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                view.loadUrl("file:///android_asset/login.html");
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id)
        {
            case DIALOG_KEY_PROCESS:
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage("Please wait while processing...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(true);
                dialog.setOnCancelListener(onBackListener);
                return dialog;
        }
        return null;
    }

    private DialogInterface.OnCancelListener onBackListener = new DialogInterface.OnCancelListener(){
        @Override
        public void onCancel(DialogInterface arg0) {
            Log.d(TAG,"onBackPressed...");
            if(dialog_process!=null) dialog_process.dismiss();
        }
    };

    @Override
    public void onBackPressed() {
        Log.e(TAG,"onBackPressed");
        super.onBackPressed();
        processFinishTask(false);
    }

    public void processFinishTask(boolean status){
        Log.d(TAG,"finishActivity");
        if(dialog_process!=null) dialog_process.dismiss();
        if(status) {
            setResult(Constant.RESULT_OK);
            finish();
        }else{
			/*
			 * Error Message
			 */
        }
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
                pdxService.addServiceListener(serviceListener);
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
        public void onConnected(String sessionId) throws RemoteException {
            Log.d(TAG,"onConnected");
            pdxService.register(pdxService.getSessionUser());
        }
        @Override
        public void onDisconnected(String reason) throws RemoteException {}
        @Override
        public void onRegistered(String json) throws RemoteException {
            Log.d(TAG,"onRegistered");
            pdxService.authorize(pdxService.getSessionUser());
            processFinishTask(true);
        }
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
        public void onErrorResponse(String error) throws RemoteException {
            Log.d(TAG,"onErrorResponse");
            processFinishTask(false);
        }
        @Override
        public void onSignOut() throws RemoteException {}
    };
}
