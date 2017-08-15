/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Projects\\OS4-IT\\Development\\OS4-Online\\app\\src\\main\\aidl\\com\\os4\\ecb\\service\\aidl\\IPDXServiceListenerExt.aidl
 */
package com.os4.ecb.service.aidl;
public interface IPDXServiceListenerExt extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.os4.ecb.service.aidl.IPDXServiceListenerExt
{
private static final java.lang.String DESCRIPTOR = "com.os4.ecb.service.aidl.IPDXServiceListenerExt";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.os4.ecb.service.aidl.IPDXServiceListenerExt interface,
 * generating a proxy if needed.
 */
public static com.os4.ecb.service.aidl.IPDXServiceListenerExt asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.os4.ecb.service.aidl.IPDXServiceListenerExt))) {
return ((com.os4.ecb.service.aidl.IPDXServiceListenerExt)iin);
}
return new com.os4.ecb.service.aidl.IPDXServiceListenerExt.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_onConnected:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onConnected(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onDisconnected:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onDisconnected(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onRegistered:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onRegistered(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onAuthorized:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onAuthorized(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onGetRosters:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onGetRosters(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onPresence:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onPresence(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onPresenceGroup:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onPresenceGroup(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onUpdateAvatar:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onUpdateAvatar(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onUpdateService:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onUpdateService(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onUpdateGroup:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onUpdateGroup(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onUpdateParticipant:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onUpdateParticipant(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onUpdateServiceInfo:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onUpdateServiceInfo(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onUpdateGroupInfo:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onUpdateGroupInfo(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onSubscribe:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onSubscribe(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onUnSubscribe:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onUnSubscribe(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onAddRoster:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onAddRoster(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onUpdateRoster:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onUpdateRoster(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onDeleteRoster:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onDeleteRoster(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onMessage:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onMessage(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onMessageGroup:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onMessageGroup(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onMessageParticipant:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onMessageParticipant(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onMessageState:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onMessageState(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onInvitation:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onInvitation(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onGetAffiliation:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onGetAffiliation(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onPing:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onPing(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onTransferFile:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onTransferFile(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onIQPacket:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onIQPacket(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onMessagePacket:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onMessagePacket(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onErrorResponse:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onErrorResponse(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onSignOut:
{
data.enforceInterface(DESCRIPTOR);
this.onSignOut();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.os4.ecb.service.aidl.IPDXServiceListenerExt
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void onConnected(java.lang.String sessionId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(sessionId);
mRemote.transact(Stub.TRANSACTION_onConnected, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onDisconnected(java.lang.String reason) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(reason);
mRemote.transact(Stub.TRANSACTION_onDisconnected, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onRegistered(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onRegistered, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onAuthorized(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onAuthorized, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onGetRosters(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onGetRosters, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onPresence(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onPresence, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onPresenceGroup(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onPresenceGroup, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onUpdateAvatar(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onUpdateAvatar, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onUpdateService(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onUpdateService, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onUpdateGroup(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onUpdateGroup, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onUpdateParticipant(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onUpdateParticipant, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onUpdateServiceInfo(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onUpdateServiceInfo, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onUpdateGroupInfo(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onUpdateGroupInfo, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onSubscribe(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onSubscribe, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onUnSubscribe(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onUnSubscribe, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onAddRoster(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onAddRoster, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onUpdateRoster(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onUpdateRoster, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onDeleteRoster(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onDeleteRoster, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onMessage(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onMessage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onMessageGroup(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onMessageGroup, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onMessageParticipant(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onMessageParticipant, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onMessageState(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onMessageState, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onInvitation(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onInvitation, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onGetAffiliation(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onGetAffiliation, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onPing(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onPing, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onTransferFile(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onTransferFile, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onIQPacket(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onIQPacket, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onMessagePacket(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onMessagePacket, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onErrorResponse(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_onErrorResponse, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void onSignOut() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onSignOut, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onConnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onDisconnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onRegistered = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onAuthorized = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_onGetRosters = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_onPresence = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_onPresenceGroup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_onUpdateAvatar = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_onUpdateService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_onUpdateGroup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_onUpdateParticipant = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_onUpdateServiceInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_onUpdateGroupInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_onSubscribe = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_onUnSubscribe = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_onAddRoster = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_onUpdateRoster = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_onDeleteRoster = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_onMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_onMessageGroup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_onMessageParticipant = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_onMessageState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_onInvitation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
static final int TRANSACTION_onGetAffiliation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
static final int TRANSACTION_onPing = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
static final int TRANSACTION_onTransferFile = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
static final int TRANSACTION_onIQPacket = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
static final int TRANSACTION_onMessagePacket = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
static final int TRANSACTION_onErrorResponse = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
static final int TRANSACTION_onSignOut = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
}
public void onConnected(java.lang.String sessionId) throws android.os.RemoteException;
public void onDisconnected(java.lang.String reason) throws android.os.RemoteException;
public void onRegistered(java.lang.String json) throws android.os.RemoteException;
public void onAuthorized(java.lang.String json) throws android.os.RemoteException;
public void onGetRosters(java.lang.String json) throws android.os.RemoteException;
public void onPresence(java.lang.String json) throws android.os.RemoteException;
public void onPresenceGroup(java.lang.String json) throws android.os.RemoteException;
public void onUpdateAvatar(java.lang.String json) throws android.os.RemoteException;
public void onUpdateService(java.lang.String json) throws android.os.RemoteException;
public void onUpdateGroup(java.lang.String json) throws android.os.RemoteException;
public void onUpdateParticipant(java.lang.String json) throws android.os.RemoteException;
public void onUpdateServiceInfo(java.lang.String json) throws android.os.RemoteException;
public void onUpdateGroupInfo(java.lang.String json) throws android.os.RemoteException;
public void onSubscribe(java.lang.String json) throws android.os.RemoteException;
public void onUnSubscribe(java.lang.String json) throws android.os.RemoteException;
public void onAddRoster(java.lang.String json) throws android.os.RemoteException;
public void onUpdateRoster(java.lang.String json) throws android.os.RemoteException;
public void onDeleteRoster(java.lang.String json) throws android.os.RemoteException;
public void onMessage(java.lang.String json) throws android.os.RemoteException;
public void onMessageGroup(java.lang.String json) throws android.os.RemoteException;
public void onMessageParticipant(java.lang.String json) throws android.os.RemoteException;
public void onMessageState(java.lang.String json) throws android.os.RemoteException;
public void onInvitation(java.lang.String json) throws android.os.RemoteException;
public void onGetAffiliation(java.lang.String json) throws android.os.RemoteException;
public void onPing(java.lang.String json) throws android.os.RemoteException;
public void onTransferFile(java.lang.String json) throws android.os.RemoteException;
public void onIQPacket(java.lang.String json) throws android.os.RemoteException;
public void onMessagePacket(java.lang.String json) throws android.os.RemoteException;
public void onErrorResponse(java.lang.String json) throws android.os.RemoteException;
public void onSignOut() throws android.os.RemoteException;
}
