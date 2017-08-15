package com.os4.ecb.session;

public interface SessionListener {

    public void onConnected(SessionEvent event);
    public void onDisconnected(SessionEvent event);
    public void onRegistered(SessionEvent event);
    public void onAuthorized(SessionEvent event);
    public void onGetRosters(SessionEvent event);
    public void onPresence(SessionEvent event);
    public void onPresenceGroup(SessionEvent event);
    public void onUpdateAvatar(SessionEvent event);
    public void onUpdateService(SessionEvent event);
    public void onUpdateGroup(SessionEvent event);
    public void onUpdateParticipant(SessionEvent event);
    public void onUpdateServiceInfo(SessionEvent event);
    public void onUpdateGroupInfo(SessionEvent event);
    public void onSubscribe(SessionEvent event);
    public void onUnsubscribe(SessionEvent event);    
    public void onAddRoster(SessionEvent event);
    public void onUpdateRoster(SessionEvent event);
    public void onDeleteRoster(SessionEvent event);
    public void onMessage(SessionEvent event);
    public void onMessageGroup(SessionEvent event);
    public void onMessageParticipant(SessionEvent event);
    public void onMessageState(SessionEvent event);
    public void onInvitation(SessionEvent event);
    public void onGetAffiliation(SessionEvent event);
    public void onPing(SessionEvent event);
    public void onTransferFile(SessionEvent event);

    public void onIQPacket(SessionEvent event);
    public void onMessagePacket(SessionEvent event);
    public void onErrorResponse(SessionEvent event);
    public void onSessionClose(SessionEvent event);
}
