package com.os4.ecb.service.aidl;

interface IPDXServiceListenerExt {
    void onConnected(in String sessionId);
    void onDisconnected(in String reason);
    void onRegistered(in String json);
    void onAuthorized(in String json);
    void onGetRosters(in String json);
    void onPresence(in String json);
    void onPresenceGroup(in String json);
    void onUpdateAvatar(in String json);
    void onUpdateService(in String json);
    void onUpdateGroup(in String json);
    void onUpdateParticipant(in String json);
    void onUpdateServiceInfo(in String json);
    void onUpdateGroupInfo(in String json);
    void onSubscribe(in String json);
    void onUnSubscribe(in String json);
    void onAddRoster(in String json);
    void onUpdateRoster(in String json);
    void onDeleteRoster(in String json);
    void onMessage(in String json);
    void onMessageGroup(in String json);
    void onMessageParticipant(in String json);
    void onMessageState(in String json);
    void onInvitation(in String json);
    void onGetAffiliation(in String json);
    void onPing(in String json);
    void onTransferFile(in String json);
    void onIQPacket(in String json);
    void onMessagePacket(in String json);
    void onErrorResponse(in String json);
    void onSignOut();
}
