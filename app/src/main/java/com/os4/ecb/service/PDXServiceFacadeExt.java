package com.os4.ecb.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.os4.ecb.Constant;
import com.os4.ecb.adapter.XMPPConnector;
import com.os4.ecb.beans.FileTransferInfo;
import com.os4.ecb.beans.GroupInfo;
import com.os4.ecb.beans.Messages;
import com.os4.ecb.misc.Base64;
import com.os4.ecb.misc.ImageUtility;
import com.os4.ecb.misc.NetworkUtil;
import com.os4.ecb.misc.Properties;
import com.os4.ecb.service.aidl.IPDXServiceExt;
import com.os4.ecb.service.aidl.IPDXServiceListenerExt;
import com.google.gson.Gson;
import com.os4.ecb.adapter.TCPConnector;
import com.os4.ecb.beans.AffiliationItems;
import com.os4.ecb.beans.DiscoItems;
import com.os4.ecb.beans.RosterItems;
import com.os4.ecb.session.SessionEvent;
import com.os4.ecb.session.SessionListener;
import com.os4.ecb.session.SessionRequest;
import com.os4.ecb.session.SessionUser;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by yanyan on 3/3/2017.
 */

public class PDXServiceFacadeExt extends IPDXServiceExt.Stub {

    private static final String TAG = PDXServiceFacadeExt.class.getName();
    private Context context;
    private RemoteCallbackList<IPDXServiceListenerExt> callbackList = new RemoteCallbackList<IPDXServiceListenerExt>();

    private SharedPreferences preferences;
    private XMPPConnector connector;
    private SessionUser sessionUser;
    private RosterItems rosterItems = new RosterItems();
    private DiscoItems serviceItems = new DiscoItems();
    private HashMap<String,DiscoItems> groupItems = new HashMap<>();
    private HashMap<String,DiscoItems> participantItems = new HashMap<>();
    private HashMap<String,AffiliationItems> affiliationItems = new HashMap<>();
    private HashMap<String,Messages> contactMessages = new HashMap<>();
    private HashMap<String,Messages> groupMessages = new HashMap<>();
    private HashMap<String,Messages> participantMessages = new HashMap<>();
    private HashMap<String,FileTransferInfo> transferFiles = new HashMap<>();
    private String currentJid;
    private String currentJidGroup;
    private String currentJidParticipant;

    public PDXServiceFacadeExt(Service service){
        context = service.getApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        connector = new XMPPConnector();
    }

	/*
	 * API Service Functions
	 */

    @Override
    public String getContacts(){
        String json = (new Gson()).toJson(rosterItems);
        return json;
    }

    @Override
    public String getServices(){
        String json = (new Gson()).toJson(serviceItems);
        return json;
    }

    @Override
    public String getGroups(String jid){
        DiscoItems groups = groupItems.get(jid);
        if(groups==null) groups = new DiscoItems();
        String json = (new Gson()).toJson(groups);
        return json;
    }

    @Override
    public String getAffiliations(String jid){
        AffiliationItems affiliations = affiliationItems.get(jid);
        if(affiliations==null) affiliations = new AffiliationItems();
        String json = (new Gson()).toJson(affiliations);
        return json;
    }

    @Override
    public String getParticipants(String jid){
        DiscoItems participants = participantItems.get(jid);
        if(participants==null) participants = new DiscoItems();
        String json = (new Gson()).toJson(participants);
        return json;
    }

    @Override
    public String getMessages(String jid){
        Messages messages = contactMessages.get(jid);
        if(messages==null) messages = new Messages();
        rosterItems.resetCounter(jid);
        String json = (new Gson()).toJson(messages);
        return json;
    }

    public Messages.Message addMessage(SessionRequest request){
        Messages messagesAll = contactMessages.get(request.getJidFrom());
        if(messagesAll==null) messagesAll = new Messages();
        Messages.Message message = messagesAll.newMessage(request.getId(),request.getJidFrom(),request.getJidTo(),request.getMsgBody());
        messagesAll.addMessage(message);
        contactMessages.put(request.getJidFrom(),messagesAll);
        return message;
    }

    @Override
    public String getMessagesGroup(String jid){
        Messages messages = groupMessages.get(jid);
        if(messages==null) messages = new Messages();
        try {
            String json = (new Gson()).toJson(messages);
            return json;
        }catch (ConcurrentModificationException m){
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Messages.Message addMessagesGroup(SessionRequest request){
        Messages messagesAll = groupMessages.get(request.getJidFrom());
        if(messagesAll==null) messagesAll = new Messages();
        Messages.Message message = messagesAll.newMessage(request.getId(),request.getFrom(),request.getJidTo(),request.getMsgBody());
        if(request.getSubject()!=null && request.getSubject().trim().length()>0) {
            message.setSubject(request.getSubject());
            message.setTextMessage(request.getSubject());
        }
        messagesAll.addMessage(message);
        groupMessages.put(request.getJidFrom(),messagesAll);
        return message;
    }

    @Override
    public String getMessagesParticipant(String jid){
        Messages messages = participantMessages.get(jid);
        if(messages==null) messages = new Messages();
        String json = (new Gson()).toJson(messages);
        return json;
    }

    public Messages.Message addMessageParticipant(SessionRequest request){
        Messages messagesAll = participantMessages.get(request.getFrom());
        if(messagesAll==null) messagesAll = new Messages();
        Messages.Message message = messagesAll.newMessage(request.getId(),request.getFrom(),request.getTo(),request.getMsgBody());
        messagesAll.addMessage(message);
        participantMessages.put(request.getFrom(),messagesAll);
        return message;
    }

    @Override
    public void setCurrentContact(String jid){
        this.currentJid = jid;
    }
    @Override
    public void setCurrentGroup(String jid){
        this.currentJidGroup = jid;
    }
    @Override
    public void setCurrentParticipant(String jid){
        this.currentJidParticipant = jid;
    }

	/*
	 * XMPP API Functions
	 */

    @Override
    public void setSessionUser(String json){
        Gson gson = new Gson();
        sessionUser = gson.fromJson(json,SessionUser.class);
    }

    @Override
    public String getSessionUser(){
        Gson gson = new Gson();
        SessionUser sessionUser = new SessionUser();
        sessionUser.setUsername(preferences.getString("username",""));
        sessionUser.setPassword(preferences.getString("password",""));
        sessionUser.setNickName(preferences.getString("nickname",sessionUser.getUsername()));
        sessionUser.setIp(preferences.getString("ip",Properties.host));
        sessionUser.setPort(Integer.parseInt(preferences.getString("port",Integer.toString(Properties.port))));
        sessionUser.setResource(Properties.resource);
        sessionUser.setDomain(Properties.domain);
        String json = gson.toJson(sessionUser);
        return json;
    }

    @Override
    public boolean isConnected(){
        if(connector!=null) return connector.isConnected();
        else return false;
    }

    @Override
    public void connect(String json) {
        if(json!=null) {
            Gson gson = new Gson();
            sessionUser = gson.fromJson(json, SessionUser.class);
        }
        if(NetworkUtil.getConnectivityStatus(context)!=NetworkUtil.TYPE_NOT_CONNECTED) {
            connect();
        }else{
            onServiceListener("onErrorResponse",NetworkUtil.getConnectivityStatusString(NetworkUtil.TYPE_NOT_CONNECTED));
        }
    }

    public void connect(){
        try {
            new Thread(new Runnable(){
                @Override
                public void run() {
                    TCPConnector tcpConnector = new TCPConnector(sessionUser.getDomain(),sessionUser.getIp(),sessionUser.getPort());
                    tcpConnector.addSessionListener(listener);
                    tcpConnector.connect(sessionUser);
                    tcpConnector.start();
                    connector.setConnector(tcpConnector);
                }
            }).start();
        } catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }
/*
    public void connect(){
        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.execute((Void) null);
    }
*/
    public class BackgroundTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                TCPConnector tcpConnector = new TCPConnector(sessionUser.getDomain(),sessionUser.getIp(),sessionUser.getPort());
                tcpConnector.addSessionListener(listener);
                tcpConnector.connect(sessionUser);
                tcpConnector.start();
                connector.setConnector(tcpConnector);
            } catch (Exception e){
                Log.e(TAG,e.getMessage());
                return false;
            }
            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
        }
    }

    @Override
    public void disconnect(){if(connector!=null) connector.disconnect();}
    @Override
    public void register(String json){connector.register();}
    @Override
    public boolean isAuthorize(){
        boolean isAuthorize = preferences.getBoolean("authorize",false);
        return isAuthorize;
    }
    @Override
    public void authorize(String json){
        connector.authorize();
    }
    @Override
    public void signout(){
        Log.i(TAG,"signout");
        contactMessages.clear();
        rosterItems.deleteItem();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("authorize", false);
        editor.commit();
        connector.signout();
        onServiceListener("onSignOut","signout");
    }
    @Override
    public void setPresence(String json){
        connector.setPresence();
    }
    @Override
    public void getRosters(){connector.getRosters();}
    @Override
    public void getAvatar(String to){
        connector.getAvatar(to);
    }
    @Override
    public void setAvatar(String json){
        connector.setAvatar(null,null,null,null,null);
    }
    @Override
    public void getService(){
        connector.getService();
    }
    @Override
    public void getDiscoItems(String jid){
        connector.getDiscoItems(jid);
    }
    @Override
    public void getDiscoInfo(String jid){
        connector.getDiscoInfo(jid);
    }
    @Override
    public void addRoster(String to,String name,String groupname){
        connector.addRoster(to,name,groupname);
    }
    @Override
    public void deleteRoster(String to){
        connector.deleteRoster(to);
    }
    @Override
    public void acceptSubscription(String to){
        connector.acceptSubscription(to);
    }
    @Override
    public void rejectSubscription(String to){
        connector.rejectSubscription(to);
    }
    @Override
    public void acceptInvitation(String to,String password){connector.acceptInvitation(to,password);}
    @Override
    public void joinRoom(String to,String password){
        groupMessages.remove(to);
        connector.joinRoom(to,password);
    }
    @Override
    public void leaveRoom(String to){
        connector.leaveRoom(to);
    }
    @Override
    public void getAffiliation(String to,String affiliation){connector.getAffiliation(to,affiliation);}
    @Override
    public void grandAffiliation(String to,String affiliation,String jid){connector.grandAffiliation(to,affiliation,jid);}
    @Override
    public void grandRole(String to,String nick,String role){
        connector.grandRole(to,nick,role);
    }
    @Override
    public void createRoom(String json){
        GroupInfo groupInfo = (new Gson()).fromJson(json,GroupInfo.class);
        connector.createRoom(groupInfo);
    }
    @Override
    public void deleteRoom(String to,String password){
        connector.deleteRoom(to,password);
    }
    @Override
    public void sendMessage(String to,String msg){

        String resourceTo = rosterItems.getItem(to).getResource();
        connector.sendMessage(to+"/"+resourceTo, msg);

        Messages messagesAll = contactMessages.get(to);
        if (messagesAll == null) messagesAll = new Messages();
        Messages.Message message = messagesAll.newMessage(connector.nextId(), sessionUser.getJid(), to, msg);
        messagesAll.addMessage(message);
        contactMessages.put(to, messagesAll);
        String json = (new Gson()).toJson(message);
        onServiceListener("onMessage", json);
    }
    @Override
    public void sendMessageGroup(String to,String msg,String thread){connector.sendMessageGroup(to,msg,thread);}
    @Override
    public void sendMessageParticipant(String to,String msg,String thread){
        Log.d(TAG,"sendMessageParticipant");
        connector.sendMessageParticipant(to, msg, thread);

        Messages messagesAll = participantMessages.get(to);
        if (messagesAll == null) messagesAll = new Messages();
        Messages.Message message = messagesAll.newMessage(connector.nextId(), sessionUser.getJid(), to, msg);
        messagesAll.addMessage(message);
        participantMessages.put(to, messagesAll);
        String json = (new Gson()).toJson(message);
        onServiceListener("onMessageParticipant", json);
    }
    @Override
    public void setMessageState(String to,String state){
        connector.setMessageState(to,state);
    }
    @Override
    public void ping(){if(connector!=null) connector.ping();}
    @Override
    public void setBookmarks(){
        connector.setBookmarks();
    }
    @Override
    public void setScratchpad(){
        connector.setScratchpad();
    }
    @Override
    public void sendTransferFile(String to,String filename){

        File imgFile = new File(Uri.parse(filename).getPath());
        String id = connector.nextId();
        String sid = "jsi_"+connector.generateId(15);
        String name = filename.substring(filename.lastIndexOf("/")+1);
        String ext = name.substring(name.lastIndexOf('.')+1).toLowerCase();
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        int size = 0;

        try{
            //FileInputStream fis = new FileInputStream(new File(Constant.THUMBNAILS_PATH,name));
            FileInputStream fis = new FileInputStream(imgFile);
            size = fis.available();
            fis.close();
        }catch(Exception e){
            Log.e(TAG,e.getMessage());
        }

        FileTransferInfo fileTransferInfo = new FileTransferInfo();
        fileTransferInfo.setName(name);
        fileTransferInfo.setFullPath(filename);
        fileTransferInfo.setSid(sid);
        fileTransferInfo.setMimeType(type);
        fileTransferInfo.setSize(size);
        fileTransferInfo.setBlockSize(4096);

        String resourceTo = rosterItems.getItem(to).getResource();
        connector.sendTransferFile(id,to+"/"+resourceTo,fileTransferInfo);

        Messages messagesAll = contactMessages.get(to);
        if (messagesAll == null) messagesAll = new Messages();
        Messages.Message message = messagesAll.newMessage(id, sessionUser.getJid(), to, fileTransferInfo);

        messagesAll.addMessage(message);
        contactMessages.put(to, messagesAll);
        String json = (new Gson()).toJson(message);
        onServiceListener("onMessage", json);

        transferFiles.put(id,fileTransferInfo);
    }
    @Override
    public void acceptTransferFile(String id,String to){connector.acceptTransferFile(id,to);}
    @Override
    public void rejectTransferFile(String id,String to){connector.rejectTransferFile(id,to);}

    @Override
    public void addServiceListener(IPDXServiceListenerExt listen) throws RemoteException {
        Log.d(TAG,"addServiceListener");
        if (listen != null) callbackList.register(listen);
    }

    @Override
    public void removeServiceListener(IPDXServiceListenerExt listen) throws RemoteException {
        Log.d(TAG,"removeServiceListener");
        if (listen != null) callbackList.unregister(listen);
    }

    public void setUserPreference(boolean authorize){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString("username", sessionUser.getUsername());
        editor.putString("password", sessionUser.getPassword());
        editor.putString("nickname", sessionUser.getNickName());
        editor.putString("resource", sessionUser.getResource());
        editor.putString("domain", sessionUser.getDomain());
        editor.putString("ip", sessionUser.getIp());
        editor.putString("port",Integer.toString(sessionUser.getPort()));
        editor.putBoolean("authorize", authorize);
        editor.commit();
    }
	/*
	 * XMPP API SessionListener
	 */
    private SessionListener listener = new SessionListener(){

        @Override
        public void onConnected(SessionEvent event) {
            Log.d(TAG,"onConnected");
            if(isAuthorize()) connector.authorize();
            onServiceListener("onConnected","");
        }

        @Override
        public void onDisconnected(SessionEvent event) {
            Log.d(TAG,"onDisconnected");
            String reason = (String) event.getData();
            if(preferences.getBoolean("authorize",false)) {
                connector.sleep(5000);
                connect();
            }
            onServiceListener("onDisconnected",reason);
        }

        @Override
        public void onRegistered(SessionEvent event) {
            Log.d(TAG,"onRegistered");
            onServiceListener("onRegistered","");
        }

        @Override
        public void onAuthorized(SessionEvent event) {
            Log.d(TAG,"onAuthorized");
            boolean isAuthorize = true;
            setUserPreference(isAuthorize);
            connector.getRosters();
            connector.setPresence();
            Intent startIntent = new Intent(context, MonitorService.class);
            context.startService(startIntent);
            onServiceListener("onAuthorized","");
        }

        @Override
        public void onGetRosters(SessionEvent event) {
            Log.d(TAG,"onGetRosters");
            SessionRequest request = event.getRequest();
            RosterItems items = request.getRosters();
            for(RosterItems.Item item : items.getItems()){
                if(rosterItems.getItem(item.getJid())==null)
                    rosterItems.addItem(item);
            }
            onServiceListener("onGetRosters","");
            connector.setPresence();
            connector.getAvatar(null);
        }

        @Override
        public void onPresence(SessionEvent event) {
            Log.d(TAG,"onPresence");
            String xml = (String) event.getData();
            SessionRequest request = event.getRequest();
            rosterItems.setPresence(request.getJidFrom(),request.getResourceFrom(),request.getShow(),request.getStatus());
            if(request.getPhoto()!=null && rosterItems.isPhotoUpdate(request.getJidFrom(),request.getPhoto())) {
                rosterItems.setPhoto(request.getJidFrom(),request.getPhoto());
                connector.getAvatar(request.getJidFrom());
            }
            onServiceListener("onPresence","");
        }

        @Override
        public void onPresenceGroup(SessionEvent event) {
            Log.d(TAG,"onPresenceGroup");
            String xml = (String) event.getData();
            onServiceListener("onPresenceGroup","");
        }

        @Override
        public void onUpdateAvatar(SessionEvent event) {
            Log.d(TAG,"onUpdateAvatar");
            String xml = (String) event.getData();
            SessionRequest request = event.getRequest();
            rosterItems.setNickname(request.getJidFrom(),request.getNickname());
            rosterItems.setPhotoBinVal(request.getJidFrom(),request.getPhotoType(),request.getPhotoBinVal());
            onServiceListener("onUpdateAvatar","");
        }

        @Override
        public void onUpdateService(SessionEvent event) {
            Log.d(TAG,"onUpdateService");
            SessionRequest request = event.getRequest();
            serviceItems = request.getItems();
            onServiceListener("onUpdateService","");
            connector.setBookmarks();
        }

        @Override
        public void onUpdateGroup(SessionEvent event) {
            Log.d(TAG,"onUpdateGroup");
            SessionRequest request = event.getRequest();
            DiscoItems items = request.getItems();
            groupItems.put(request.getJidFrom(),items);
            onServiceListener("onUpdateGroup","");
        }

        @Override
        public void onUpdateParticipant(SessionEvent event) {
            Log.d(TAG,"onUpdateParticipant");
            SessionRequest request = event.getRequest();
            DiscoItems items = request.getItems();
            String json = (new Gson()).toJson(items);
            participantItems.put(request.getJidFrom(),items);
            onServiceListener("onUpdateParticipant",json);
        }

        @Override
        public void onUpdateServiceInfo(SessionEvent event) {
            Log.d(TAG,"onUpdateServiceInfo");
            SessionRequest request = event.getRequest();
            String json = (new Gson()).toJson(request.getDiscoInfo());
            onServiceListener("onUpdateServiceInfo",json);
        }

        @Override
        public void onUpdateGroupInfo(SessionEvent event) {
            Log.d(TAG,"onUpdateGroupInfo");
            SessionRequest request = event.getRequest();
            String json = (new Gson()).toJson(request.getDiscoInfo());
            onServiceListener("onUpdateGroupInfo",json);
        }

        @Override
        public void onSubscribe(SessionEvent event) {
            Log.d(TAG,"onSubscribe");
            SessionRequest request = event.getRequest();
            RosterItems.Item item = rosterItems.getItem(request.getJidFrom());
            if(item!=null) {
                if(item.getAsk()!=null) {
                    connector.addRoster(item.getJid(),item.getName(),"Friends");
                    connector.acceptSubscription(item.getJid());
                    item.setAsk(null);
                }
                rosterItems.updateItem(item);
            }
            else {
                String fromJid = request.getJidFrom();
                String name = fromJid.substring(0, fromJid.lastIndexOf("@"));
                rosterItems.addItem(fromJid, name,"none");
            }
            onServiceListener("onSubscribe","");
        }

        @Override
        public void onUnsubscribe(SessionEvent event) {
            Log.d(TAG,"onUnsubscribe");
            SessionRequest request = event.getRequest();
            connector.deleteRoster(request.getJidFrom());
            onServiceListener("onUnSubscribe","");
        }

        @Override
        public void onAddRoster(SessionEvent event) {
            Log.d(TAG,"onAddRoster");
            SessionRequest request = event.getRequest();
            RosterItems items = request.getRosters();
            for(RosterItems.Item item : items.getItems()){
                if(rosterItems.getItem(item.getJid())==null)
                    rosterItems.addItem(item);
            }
            onServiceListener("onAddRoster","");
        }

        @Override
        public void onUpdateRoster(SessionEvent event) {
            Log.d(TAG,"onUpdateRoster");
            SessionRequest request = event.getRequest();
            RosterItems items = request.getRosters();
            for(RosterItems.Item item : items.getItems()){
                if(rosterItems.getItem(item.getJid())!=null) {
                    rosterItems.updateItem(item);
                }
            }
            onServiceListener("onUpdateRoster","");
        }

        @Override
        public void onDeleteRoster(SessionEvent event) {
            Log.d(TAG,"onDeleteRoster");
            SessionRequest request = event.getRequest();
            RosterItems items = request.getRosters();
            for(RosterItems.Item item : items.getItems()){
                if(rosterItems.getItem(item.getJid())!=null)
                    rosterItems.deleteItem(item.getJid());
            }
            onServiceListener("onDeleteRoster","");
        }

        @Override
        public void onMessage(SessionEvent event) {
            Log.d(TAG,"onMessage");
            SessionRequest request = event.getRequest();
            /*
            Messages messagesAll = contactMessages.get(request.getJidFrom());
            if(messagesAll==null) messagesAll = new Messages();
            Messages.Message message = messagesAll.newMessage(request.getId(),request.getJidFrom(),request.getJidTo(),request.getMsgBody());
            messagesAll.addMessage(message);
            contactMessages.put(request.getJidFrom(),messagesAll);
            */
            Messages.Message message = addMessage(request);
            String json = (new Gson()).toJson(rosterItems.getItem(request.getJidFrom()));
            String msg = (new Gson()).toJson(message);
            onServiceListener("onMessage",msg);

            if(currentJid==null || (currentJid!=null && !currentJid.equalsIgnoreCase(request.getJidFrom()))) {
                Intent intentNotification = new Intent("com.os4.ecb.BROADCAST_MESSAGE_NOTIFICATION");
                intentNotification.putExtra("json", json);
                intentNotification.putExtra("msg", msg);
                context.sendBroadcast(intentNotification);
            }
            rosterItems.setCounter(request.getJidFrom());
            onServiceListener("onPresence","");
        }

        @Override
        public void onMessageGroup(SessionEvent event) {
            Log.d(TAG,"onMessageGroup");
            SessionRequest request = event.getRequest();
            /*
            Messages messagesAll = groupMessages.get(request.getJidFrom());
            if(messagesAll==null) messagesAll = new Messages();
            Messages.Message message = messagesAll.newMessage(request.getId(),request.getFrom(),request.getJidTo(),request.getMsgBody());
            messagesAll.addMessage(message);
            groupMessages.put(request.getJidFrom(),messagesAll);
            */
            Messages.Message message = addMessagesGroup(request);
            String msg = (new Gson()).toJson(message);
            onServiceListener("onMessageGroup",msg);
        }

        @Override
        public void onMessageParticipant(SessionEvent event) {
            Log.d(TAG,"onMessageParticipant");
            SessionRequest request = event.getRequest();
            /*
            Messages messagesAll = participantMessages.get(request.getFrom());
            if(messagesAll==null) messagesAll = new Messages();
            Messages.Message message = messagesAll.newMessage(request.getId(),request.getFrom(),request.getTo(),request.getMsgBody());
            messagesAll.addMessage(message);
            participantMessages.put(request.getFrom(),messagesAll);
            */
            Messages.Message message = addMessageParticipant(request);
            String msg = (new Gson()).toJson(message);
            onServiceListener("onMessageParticipant",msg);
        }

        @Override
        public void onMessageState(SessionEvent event) {
            Log.d(TAG,"onMessageState");
            SessionRequest request = event.getRequest();
            onServiceListener("onMessageState","");
        }

        @Override
        public void onInvitation(SessionEvent event) {
            Log.d(TAG,"onInvitation");
            SessionRequest request = event.getRequest();
            onServiceListener("onInvitation","");
            connector.acceptInvitation(request.getFrom(),request.getPassword());
        }

        @Override
        public void onGetAffiliation(SessionEvent event) {
            Log.d(TAG,"onGetAffiliation");
            SessionRequest request = event.getRequest();
            AffiliationItems items = request.getAffiliations();
            AffiliationItems participants = affiliationItems.get(request.getJidFrom());
            if(participants==null) participants = new AffiliationItems();
            affiliationItems.put(request.getJidFrom(),participants);
            String json = (new Gson()).toJson(items);
            onServiceListener("onGetAffiliation",json);
        }

        @Override
        public void onPing(SessionEvent event) {
            Log.d(TAG,"onPing");
            String xml = (String) event.getData();
            onServiceListener("onPing","");
        }

        @Override
        public void onTransferFile(SessionEvent event) {
            Log.d(TAG,"onTransferFile");
            SessionRequest request = event.getRequest();
            FileTransferInfo fileTransferInfo = request.getFileTransferInfo();
            String filejson = (new Gson()).toJson(fileTransferInfo);
            onServiceListener("onTransferFile",filejson);

            if(fileTransferInfo.getTag().equalsIgnoreCase("si") && request.getType().equalsIgnoreCase("set")) {

                Messages messagesAll = contactMessages.get(request.getJidFrom());
                if (messagesAll == null) messagesAll = new Messages();
                Messages.Message message = messagesAll.newMessage(request.getId(),request.getJidFrom(), request.getJidTo(), fileTransferInfo);
                messagesAll.addMessage(message);
                contactMessages.put(request.getJidFrom(), messagesAll);
                String json = (new Gson()).toJson(rosterItems.getItem(request.getJidFrom()));
                String msg = (new Gson()).toJson(message);
                onServiceListener("onMessage", msg);

                if (currentJid == null || (currentJid != null && !currentJid.equalsIgnoreCase(request.getJidFrom()))) {
                    Intent intentNotification = new Intent("com.os4.ecb.BROADCAST_MESSAGE_NOTIFICATION");
                    intentNotification.putExtra("json", json);
                    intentNotification.putExtra("msg", msg);
                    context.sendBroadcast(intentNotification);
                }
                rosterItems.setCounter(request.getJidFrom());
                onServiceListener("onPresence", "");
            }
            else if(fileTransferInfo.getTag().equalsIgnoreCase("si") && request.getType().equalsIgnoreCase("result")) {

                FileTransferInfo infoFile = (FileTransferInfo) transferFiles.get(request.getId());
                connector.openTransferFile(connector.nextId(),request.getFrom(),infoFile);
                int blockSize = infoFile.getBlockSize();
                int seq = 0;
                try {
                    //FileInputStream fis = new FileInputStream(new File(Constant.THUMBNAILS_PATH,infoFile.getName()));
                    FileInputStream fis = new FileInputStream(new File(Uri.parse(infoFile.getFullPath()).getPath()));
                    while(fis.available()>0){
                        if(fis.available()<blockSize) blockSize = fis.available();
                        byte[] data = new byte[blockSize];
                        fis.read(data);
                        String base64 = Base64.encodeBytes(data);
                        infoFile.setSeq(seq);
                        infoFile.setBase64(base64);
                        connector.dataTransferFile(connector.nextId(),request.getFrom(),infoFile);
                        seq++;
                    }
                    fis.close();
                }catch(Exception e){
                    Log.e(TAG,e.getMessage());
                }
                connector.closeTransferFile(connector.nextId(),request.getFrom(),infoFile);
                transferFiles.remove(request.getId());
            }
        }

        @Override
        public void onIQPacket(SessionEvent event) {
            Log.d(TAG,"onIQPacket");
            String xml = (String) event.getData();
            onServiceListener("onIQPacket","");
        }

        @Override
        public void onMessagePacket(SessionEvent event) {
            Log.d(TAG,"onMessagePacket");
            String xml = (String) event.getData();
            onServiceListener("onMessagePacket","");
        }

        @Override
        public void onErrorResponse(SessionEvent event) {
            Log.e(TAG,"onErrorResponse");
            String xml = (String) event.getData();
            if(xml.startsWith("failed to connect to")){
                try{
                    Thread.sleep(5000);
                }catch(Exception e){Log.e(TAG,e.getMessage());}
            }
            onServiceListener("onErrorResponse",xml);
        }

        @Override
        public void onSessionClose(SessionEvent event) {
            Log.e(TAG,"onSessionClose");
            Intent startIntent = new Intent(context, MonitorService.class);
            context.startService(startIntent);
        }
    };

    public Document getDocument(String xml){
        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = docFactory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            return document;
        }catch(ParserConfigurationException | SAXException | IOException e){
            Log.e(TAG,e.getMessage());
            return null;
        }
    }

    public String getStringFromDocument(Document doc)
    {
        try
        {
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        }
        catch(Exception ex)
        {
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }

	/*
	 * API Service Callback
	 */

    public void onServiceListener(String action,String json){
        try{
            final int N = callbackList.beginBroadcast();
            for (int i=0; i<N; i++) {
                try {
                    if(action.equalsIgnoreCase("onConnected")) {
                        callbackList.getBroadcastItem(i).onConnected(json);
                    }
                    else if(action.equalsIgnoreCase("onDisconnected")) {
                        callbackList.getBroadcastItem(i).onDisconnected(json);
                    }
                    else if(action.equalsIgnoreCase("onRegistered")) {
                        callbackList.getBroadcastItem(i).onRegistered(json);
                    }
                    else if(action.equalsIgnoreCase("onAuthorized")) {
                        callbackList.getBroadcastItem(i).onAuthorized(json);
                    }
                    else if(action.equalsIgnoreCase("onGetRosters")) {
                        callbackList.getBroadcastItem(i).onGetRosters(json);
                    }
                    else if(action.equalsIgnoreCase("onPresence")) {
                        callbackList.getBroadcastItem(i).onPresence(json);
                    }
                    else if(action.equalsIgnoreCase("onPresenceGroup")) {
                        callbackList.getBroadcastItem(i).onPresenceGroup(json);
                    }
                    else if(action.equalsIgnoreCase("onUpdateAvatar")) {
                        callbackList.getBroadcastItem(i).onUpdateAvatar(json);
                    }
                    else if(action.equalsIgnoreCase("onUpdateService")) {
                        callbackList.getBroadcastItem(i).onUpdateService(json);
                    }
                    else if(action.equalsIgnoreCase("onUpdateGroup")) {
                        callbackList.getBroadcastItem(i).onUpdateGroup(json);
                    }
                    else if(action.equalsIgnoreCase("onUpdateParticipant")) {
                        callbackList.getBroadcastItem(i).onUpdateParticipant(json);
                    }
                    else if(action.equalsIgnoreCase("onUpdateServiceInfo")) {
                        callbackList.getBroadcastItem(i).onUpdateServiceInfo(json);
                    }
                    else if(action.equalsIgnoreCase("onUpdateGroupInfo")) {
                        callbackList.getBroadcastItem(i).onUpdateGroupInfo(json);
                    }
                    else if(action.equalsIgnoreCase("onSubscribe")) {
                        callbackList.getBroadcastItem(i).onSubscribe(json);
                    }
                    else if(action.equalsIgnoreCase("onUnSubscribe")) {
                        callbackList.getBroadcastItem(i).onUnSubscribe(json);
                    }
                    else if(action.equalsIgnoreCase("onAddRoster")) {
                        callbackList.getBroadcastItem(i).onAddRoster(json);
                    }
                    else if(action.equalsIgnoreCase("onUpdateRoster")) {
                        callbackList.getBroadcastItem(i).onUpdateRoster(json);
                    }
                    else if(action.equalsIgnoreCase("onDeleteRoster")) {
                        callbackList.getBroadcastItem(i).onDeleteRoster(json);
                    }
                    else if(action.equalsIgnoreCase("onMessage")) {
                        callbackList.getBroadcastItem(i).onMessage(json);
                    }
                    else if(action.equalsIgnoreCase("onMessageGroup")) {
                        callbackList.getBroadcastItem(i).onMessageGroup(json);
                    }
                    else if(action.equalsIgnoreCase("onMessageParticipant")) {
                        callbackList.getBroadcastItem(i).onMessageParticipant(json);
                    }
                    else if(action.equalsIgnoreCase("onMessageState")) {
                        callbackList.getBroadcastItem(i).onMessageState(json);
                    }
                    else if(action.equalsIgnoreCase("onInvitation")) {
                        callbackList.getBroadcastItem(i).onInvitation(json);
                    }
                    else if(action.equalsIgnoreCase("onGetAffiliation")) {
                        callbackList.getBroadcastItem(i).onGetAffiliation(json);
                    }
                    else if(action.equalsIgnoreCase("onPing")) {
                        callbackList.getBroadcastItem(i).onPing(json);
                    }
                    else if(action.equalsIgnoreCase("onTransferFile")) {
                        callbackList.getBroadcastItem(i).onTransferFile(json);
                    }
                    else if(action.equalsIgnoreCase("onIQPacket")) {
                        callbackList.getBroadcastItem(i).onIQPacket(json);
                    }
                    else if(action.equalsIgnoreCase("onMessagePacket")) {
                        callbackList.getBroadcastItem(i).onMessagePacket(json);
                    }
                    else if(action.equalsIgnoreCase("onErrorResponse")) {
                        callbackList.getBroadcastItem(i).onErrorResponse(json);
                    }
                    else if(action.equalsIgnoreCase("onSignOut")) {
                        callbackList.getBroadcastItem(i).onSignOut();
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            callbackList.finishBroadcast();
        }catch(Exception e){
            e.printStackTrace();
            callbackList.finishBroadcast();
        }
    }
}
