package com.os4.ecb.service.aidl;

import  com.os4.ecb.service.aidl.IPDXServiceListenerExt;

interface IPDXServiceExt {

    String getSessionUser();
    String getContacts();
    String getServices();
    String getGroups(in String jid);
    String getAffiliations(in String jid);
    String getParticipants(in String jid);
    String getMessages(in String jid);
    String getMessagesGroup(in String jid);
    String getMessagesParticipant(in String jid);

    void setSessionUser(in String json);
    void setCurrentContact(in String jid);
    void setCurrentGroup(in String jid);
    void setCurrentParticipant(in String jid);

    boolean isConnected();
    boolean isAuthorize();

    void connect(in String json);
    void disconnect();
    void register(in String json);
	void authorize(in String json);
	void signout();

	void setPresence(in String json);
	void getRosters();
	void getAvatar(in String to);
	void setAvatar(in String json);
	void getService();
	void getDiscoItems(in String jid);
	void getDiscoInfo(in String jid);

	void addRoster(in String to,in String name,in String groupname);
	void deleteRoster(in String to);
	void acceptSubscription(in String to);
	void rejectSubscription(in String to);

	void createRoom(in String json);
	void deleteRoom(in String to,in String password);
	void joinRoom(in String to,in String password);
	void leaveRoom(in String to);
	void acceptInvitation(in String to,in String password);
	void getAffiliation(in String to,in String affiliation);
	void grandAffiliation(in String to,in String affiliation,in String jid);
	void grandRole(in String to,in String nick,in String role);

	void sendMessage(in String to,String msg);
	void sendMessageGroup(in String to,in String msg,in String thread);
	void sendMessageParticipant(in String to,in String msg,in String thread);
	void setMessageState(in String to,in String state);

	void ping();
	void setBookmarks();
	void setScratchpad();

	void sendTransferFile(in String to,in String filename);
	void acceptTransferFile(in String id,in String to);
	void rejectTransferFile(in String id,in String to);

    void addServiceListener(in IPDXServiceListenerExt listen);
    void removeServiceListener(in IPDXServiceListenerExt listen);
}
