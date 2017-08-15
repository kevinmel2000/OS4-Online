package com.os4.ecb.adapter;

import com.os4.ecb.beans.FileTransferInfo;
import com.os4.ecb.beans.GroupInfo;
import com.os4.ecb.session.SessionUser;

/**
 * Created by yanyan on 3/11/2017.
 */

public class XMPPConnector {

    private static final String TAG = XMPPConnector.class.getName();
    private TCPConnector connector;

    public XMPPConnector() {}
    public String generateId(int n){return connector.generateId(n);}
    public String nextId(){return connector.nextId();}
    public void sleep(int sleep){
        try{
            Thread.sleep(sleep);
        }catch(Exception e){}
    }

    public boolean isConnected(){
        if(connector!=null) return connector.isConnected();
        else return false;
    }

    public void setConnector(TCPConnector connector){
        this.connector = connector;
    }
    public void send(String xml){connector.send(xml);}
    public void disconnect(){if(connector!=null) connector.disconnect();}
    public void register(){connector.register();}
    public void authorize(){connector.authorize();}
    public void signout(){connector.signout();}
    public void getRosters(){connector.getRosters();}
    public void setPresence(){connector.setPresence();}
    public void setPresence(SessionUser sessionUser){
        connector.setPresence(sessionUser);
    }
    public void getAvatar(String to){
        connector.getAvatar(to);
    }
    public void setAvatar(String to,String nickname,String resource,String email,String base64){
        connector.setAvatar(to,nickname,resource,email,base64);
    }
    public void getService(){
        connector.getService();
    }
    public void getDiscoItems(String jid){
        connector.getDiscoItems(jid);
    }
    public void getDiscoInfo(String jid){
        connector.getDiscoInfo(jid);
    }
    public void addRoster(String to, String name,String groupname){connector.addRoster(to,name,groupname);}
    public void deleteRoster(String to){connector.deleteRoster(to);}
    public void acceptSubscription(String to){connector.acceptSubscription(to);}
    public void rejectSubscription(String to){connector.rejectSubscription(to);}
    public void acceptInvitation(String to,String password){connector.acceptInvitation(to,password);}
    public void joinRoom(String to,String password){
        connector.joinRoom(to,password);
    }
    public void leaveRoom(String to){
        connector.leaveRoom(to);
    }
    public void getAffiliation(String to,String affiliation){connector.getAffiliation(to,affiliation);}
    public void grandAffiliation(String to,String affiliation,String jid){connector.grandAffiliation(to,affiliation,jid);}
    public void grandRole(String to,String nick,String role){
        connector.grandRole(to,nick,role);
    }
    public void createRoom(GroupInfo group){
        connector.createRoom(group);
    }
    public void deleteRoom(String to,String password){
        connector.deleteRoom(to,password);
    }
    public void sendMessage(String to,String msg){
        connector.sendMessage(to,msg);
    }
    public void sendMessage(String to,String msg,String thread){connector.sendMessage(to,msg,thread);}
    public void sendMessageGroup(String to,String msg,String thread){connector.sendMessageGroup(to,msg,thread);}
    public void sendMessageParticipant(String to,String msg,String thread){connector.sendMessageParticipant(to,msg,thread);}
    public void setMessageState(String to,String state){
        connector.setMessageState(to,state);
    }
    public void ping(){
        connector.ping();
    }
    public void setBookmarks(){
        connector.setBookmarks();
    }
    public void setScratchpad(){
        connector.setScratchpad();
    }
    public void sendTransferFile(String id,String to,FileTransferInfo fileTransferInfo){connector.sendTransferFile(id,to,fileTransferInfo);}
    public void acceptTransferFile(String to, String filename){connector.acceptTransferFile(to,filename);}
    public void rejectTransferFile(String to, String filename){connector.rejectTransferFile(to,filename);}
    public void openTransferFile(String id,String to,FileTransferInfo fileTransferInfo){connector.openTransferFile(id,to,fileTransferInfo);}
    public void dataTransferFile(String id,String to,FileTransferInfo fileTransferInfo){connector.dataTransferFile(id,to,fileTransferInfo);}
    public void closeTransferFile(String id,String to,FileTransferInfo fileTransferInfo){connector.closeTransferFile(id,to,fileTransferInfo);}
}
