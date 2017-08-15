/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Projects\\OS4-IT\\Development\\OS4-Online\\app\\src\\main\\aidl\\com\\os4\\ecb\\service\\aidl\\IPDXServiceExt.aidl
 */
package com.os4.ecb.service.aidl;
public interface IPDXServiceExt extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.os4.ecb.service.aidl.IPDXServiceExt
{
private static final java.lang.String DESCRIPTOR = "com.os4.ecb.service.aidl.IPDXServiceExt";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.os4.ecb.service.aidl.IPDXServiceExt interface,
 * generating a proxy if needed.
 */
public static com.os4.ecb.service.aidl.IPDXServiceExt asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.os4.ecb.service.aidl.IPDXServiceExt))) {
return ((com.os4.ecb.service.aidl.IPDXServiceExt)iin);
}
return new com.os4.ecb.service.aidl.IPDXServiceExt.Stub.Proxy(obj);
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
case TRANSACTION_getSessionUser:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getSessionUser();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getContacts:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getContacts();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getServices:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getServices();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getGroups:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getGroups(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getAffiliations:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getAffiliations(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getParticipants:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getParticipants(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getMessages:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getMessages(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getMessagesGroup:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getMessagesGroup(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getMessagesParticipant:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.getMessagesParticipant(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_setSessionUser:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setSessionUser(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setCurrentContact:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setCurrentContact(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setCurrentGroup:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setCurrentGroup(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setCurrentParticipant:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setCurrentParticipant(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isConnected:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isConnected();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_isAuthorize:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isAuthorize();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_connect:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.connect(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_disconnect:
{
data.enforceInterface(DESCRIPTOR);
this.disconnect();
reply.writeNoException();
return true;
}
case TRANSACTION_register:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.register(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_authorize:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.authorize(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_signout:
{
data.enforceInterface(DESCRIPTOR);
this.signout();
reply.writeNoException();
return true;
}
case TRANSACTION_setPresence:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setPresence(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getRosters:
{
data.enforceInterface(DESCRIPTOR);
this.getRosters();
reply.writeNoException();
return true;
}
case TRANSACTION_getAvatar:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.getAvatar(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setAvatar:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setAvatar(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getService:
{
data.enforceInterface(DESCRIPTOR);
this.getService();
reply.writeNoException();
return true;
}
case TRANSACTION_getDiscoItems:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.getDiscoItems(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getDiscoInfo:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.getDiscoInfo(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_addRoster:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.addRoster(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_deleteRoster:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.deleteRoster(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_acceptSubscription:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.acceptSubscription(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_rejectSubscription:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.rejectSubscription(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_createRoom:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.createRoom(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_deleteRoom:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.deleteRoom(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_joinRoom:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.joinRoom(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_leaveRoom:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.leaveRoom(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_acceptInvitation:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.acceptInvitation(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_getAffiliation:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.getAffiliation(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_grandAffiliation:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.grandAffiliation(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_grandRole:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.grandRole(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_sendMessage:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.sendMessage(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_sendMessageGroup:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.sendMessageGroup(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_sendMessageParticipant:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
this.sendMessageParticipant(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_setMessageState:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.setMessageState(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_ping:
{
data.enforceInterface(DESCRIPTOR);
this.ping();
reply.writeNoException();
return true;
}
case TRANSACTION_setBookmarks:
{
data.enforceInterface(DESCRIPTOR);
this.setBookmarks();
reply.writeNoException();
return true;
}
case TRANSACTION_setScratchpad:
{
data.enforceInterface(DESCRIPTOR);
this.setScratchpad();
reply.writeNoException();
return true;
}
case TRANSACTION_sendTransferFile:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.sendTransferFile(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_acceptTransferFile:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.acceptTransferFile(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_rejectTransferFile:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.rejectTransferFile(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_addServiceListener:
{
data.enforceInterface(DESCRIPTOR);
com.os4.ecb.service.aidl.IPDXServiceListenerExt _arg0;
_arg0 = com.os4.ecb.service.aidl.IPDXServiceListenerExt.Stub.asInterface(data.readStrongBinder());
this.addServiceListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_removeServiceListener:
{
data.enforceInterface(DESCRIPTOR);
com.os4.ecb.service.aidl.IPDXServiceListenerExt _arg0;
_arg0 = com.os4.ecb.service.aidl.IPDXServiceListenerExt.Stub.asInterface(data.readStrongBinder());
this.removeServiceListener(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.os4.ecb.service.aidl.IPDXServiceExt
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
@Override public java.lang.String getSessionUser() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSessionUser, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getContacts() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getContacts, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getServices() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getServices, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getGroups(java.lang.String jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jid);
mRemote.transact(Stub.TRANSACTION_getGroups, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getAffiliations(java.lang.String jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jid);
mRemote.transact(Stub.TRANSACTION_getAffiliations, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getParticipants(java.lang.String jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jid);
mRemote.transact(Stub.TRANSACTION_getParticipants, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getMessages(java.lang.String jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jid);
mRemote.transact(Stub.TRANSACTION_getMessages, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getMessagesGroup(java.lang.String jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jid);
mRemote.transact(Stub.TRANSACTION_getMessagesGroup, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.lang.String getMessagesParticipant(java.lang.String jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jid);
mRemote.transact(Stub.TRANSACTION_getMessagesParticipant, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void setSessionUser(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_setSessionUser, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setCurrentContact(java.lang.String jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jid);
mRemote.transact(Stub.TRANSACTION_setCurrentContact, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setCurrentGroup(java.lang.String jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jid);
mRemote.transact(Stub.TRANSACTION_setCurrentGroup, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setCurrentParticipant(java.lang.String jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jid);
mRemote.transact(Stub.TRANSACTION_setCurrentParticipant, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public boolean isConnected() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isConnected, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public boolean isAuthorize() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isAuthorize, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void connect(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_connect, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void disconnect() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_disconnect, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void register(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_register, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void authorize(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_authorize, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void signout() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_signout, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setPresence(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_setPresence, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void getRosters() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getRosters, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void getAvatar(java.lang.String to) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(to);
mRemote.transact(Stub.TRANSACTION_getAvatar, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setAvatar(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_setAvatar, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void getService() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getService, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void getDiscoItems(java.lang.String jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jid);
mRemote.transact(Stub.TRANSACTION_getDiscoItems, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void getDiscoInfo(java.lang.String jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(jid);
mRemote.transact(Stub.TRANSACTION_getDiscoInfo, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void addRoster(java.lang.String to, java.lang.String name, java.lang.String groupname) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(to);
_data.writeString(name);
_data.writeString(groupname);
mRemote.transact(Stub.TRANSACTION_addRoster, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void deleteRoster(java.lang.String to) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(to);
mRemote.transact(Stub.TRANSACTION_deleteRoster, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void acceptSubscription(java.lang.String to) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(to);
mRemote.transact(Stub.TRANSACTION_acceptSubscription, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void rejectSubscription(java.lang.String to) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(to);
mRemote.transact(Stub.TRANSACTION_rejectSubscription, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void createRoom(java.lang.String json) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(json);
mRemote.transact(Stub.TRANSACTION_createRoom, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void deleteRoom(java.lang.String to, java.lang.String password) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(to);
_data.writeString(password);
mRemote.transact(Stub.TRANSACTION_deleteRoom, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void joinRoom(java.lang.String to, java.lang.String password) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(to);
_data.writeString(password);
mRemote.transact(Stub.TRANSACTION_joinRoom, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void leaveRoom(java.lang.String to) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(to);
mRemote.transact(Stub.TRANSACTION_leaveRoom, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void acceptInvitation(java.lang.String to, java.lang.String password) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(to);
_data.writeString(password);
mRemote.transact(Stub.TRANSACTION_acceptInvitation, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void getAffiliation(java.lang.String to, java.lang.String affiliation) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(to);
_data.writeString(affiliation);
mRemote.transact(Stub.TRANSACTION_getAffiliation, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void grandAffiliation(java.lang.String to, java.lang.String affiliation, java.lang.String jid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(to);
_data.writeString(affiliation);
_data.writeString(jid);
mRemote.transact(Stub.TRANSACTION_grandAffiliation, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void grandRole(java.lang.String to, java.lang.String nick, java.lang.String role) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(to);
_data.writeString(nick);
_data.writeString(role);
mRemote.transact(Stub.TRANSACTION_grandRole, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sendMessage(java.lang.String to, java.lang.String msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(to);
_data.writeString(msg);
mRemote.transact(Stub.TRANSACTION_sendMessage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sendMessageGroup(java.lang.String to, java.lang.String msg, java.lang.String thread) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(to);
_data.writeString(msg);
_data.writeString(thread);
mRemote.transact(Stub.TRANSACTION_sendMessageGroup, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sendMessageParticipant(java.lang.String to, java.lang.String msg, java.lang.String thread) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(to);
_data.writeString(msg);
_data.writeString(thread);
mRemote.transact(Stub.TRANSACTION_sendMessageParticipant, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setMessageState(java.lang.String to, java.lang.String state) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(to);
_data.writeString(state);
mRemote.transact(Stub.TRANSACTION_setMessageState, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void ping() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_ping, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setBookmarks() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_setBookmarks, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setScratchpad() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_setScratchpad, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sendTransferFile(java.lang.String to, java.lang.String filename) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(to);
_data.writeString(filename);
mRemote.transact(Stub.TRANSACTION_sendTransferFile, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void acceptTransferFile(java.lang.String id, java.lang.String to) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(id);
_data.writeString(to);
mRemote.transact(Stub.TRANSACTION_acceptTransferFile, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void rejectTransferFile(java.lang.String id, java.lang.String to) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(id);
_data.writeString(to);
mRemote.transact(Stub.TRANSACTION_rejectTransferFile, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void addServiceListener(com.os4.ecb.service.aidl.IPDXServiceListenerExt listen) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listen!=null))?(listen.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_addServiceListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void removeServiceListener(com.os4.ecb.service.aidl.IPDXServiceListenerExt listen) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listen!=null))?(listen.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_removeServiceListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_getSessionUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_getContacts = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_getServices = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getGroups = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getAffiliations = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getParticipants = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_getMessages = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getMessagesGroup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getMessagesParticipant = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_setSessionUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_setCurrentContact = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_setCurrentGroup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_setCurrentParticipant = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_isConnected = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_isAuthorize = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_connect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_disconnect = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_register = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_authorize = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_signout = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_setPresence = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_getRosters = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_getAvatar = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
static final int TRANSACTION_setAvatar = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
static final int TRANSACTION_getService = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
static final int TRANSACTION_getDiscoItems = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
static final int TRANSACTION_getDiscoInfo = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
static final int TRANSACTION_addRoster = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
static final int TRANSACTION_deleteRoster = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
static final int TRANSACTION_acceptSubscription = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
static final int TRANSACTION_rejectSubscription = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
static final int TRANSACTION_createRoom = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
static final int TRANSACTION_deleteRoom = (android.os.IBinder.FIRST_CALL_TRANSACTION + 32);
static final int TRANSACTION_joinRoom = (android.os.IBinder.FIRST_CALL_TRANSACTION + 33);
static final int TRANSACTION_leaveRoom = (android.os.IBinder.FIRST_CALL_TRANSACTION + 34);
static final int TRANSACTION_acceptInvitation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 35);
static final int TRANSACTION_getAffiliation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 36);
static final int TRANSACTION_grandAffiliation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 37);
static final int TRANSACTION_grandRole = (android.os.IBinder.FIRST_CALL_TRANSACTION + 38);
static final int TRANSACTION_sendMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 39);
static final int TRANSACTION_sendMessageGroup = (android.os.IBinder.FIRST_CALL_TRANSACTION + 40);
static final int TRANSACTION_sendMessageParticipant = (android.os.IBinder.FIRST_CALL_TRANSACTION + 41);
static final int TRANSACTION_setMessageState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 42);
static final int TRANSACTION_ping = (android.os.IBinder.FIRST_CALL_TRANSACTION + 43);
static final int TRANSACTION_setBookmarks = (android.os.IBinder.FIRST_CALL_TRANSACTION + 44);
static final int TRANSACTION_setScratchpad = (android.os.IBinder.FIRST_CALL_TRANSACTION + 45);
static final int TRANSACTION_sendTransferFile = (android.os.IBinder.FIRST_CALL_TRANSACTION + 46);
static final int TRANSACTION_acceptTransferFile = (android.os.IBinder.FIRST_CALL_TRANSACTION + 47);
static final int TRANSACTION_rejectTransferFile = (android.os.IBinder.FIRST_CALL_TRANSACTION + 48);
static final int TRANSACTION_addServiceListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 49);
static final int TRANSACTION_removeServiceListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 50);
}
public java.lang.String getSessionUser() throws android.os.RemoteException;
public java.lang.String getContacts() throws android.os.RemoteException;
public java.lang.String getServices() throws android.os.RemoteException;
public java.lang.String getGroups(java.lang.String jid) throws android.os.RemoteException;
public java.lang.String getAffiliations(java.lang.String jid) throws android.os.RemoteException;
public java.lang.String getParticipants(java.lang.String jid) throws android.os.RemoteException;
public java.lang.String getMessages(java.lang.String jid) throws android.os.RemoteException;
public java.lang.String getMessagesGroup(java.lang.String jid) throws android.os.RemoteException;
public java.lang.String getMessagesParticipant(java.lang.String jid) throws android.os.RemoteException;
public void setSessionUser(java.lang.String json) throws android.os.RemoteException;
public void setCurrentContact(java.lang.String jid) throws android.os.RemoteException;
public void setCurrentGroup(java.lang.String jid) throws android.os.RemoteException;
public void setCurrentParticipant(java.lang.String jid) throws android.os.RemoteException;
public boolean isConnected() throws android.os.RemoteException;
public boolean isAuthorize() throws android.os.RemoteException;
public void connect(java.lang.String json) throws android.os.RemoteException;
public void disconnect() throws android.os.RemoteException;
public void register(java.lang.String json) throws android.os.RemoteException;
public void authorize(java.lang.String json) throws android.os.RemoteException;
public void signout() throws android.os.RemoteException;
public void setPresence(java.lang.String json) throws android.os.RemoteException;
public void getRosters() throws android.os.RemoteException;
public void getAvatar(java.lang.String to) throws android.os.RemoteException;
public void setAvatar(java.lang.String json) throws android.os.RemoteException;
public void getService() throws android.os.RemoteException;
public void getDiscoItems(java.lang.String jid) throws android.os.RemoteException;
public void getDiscoInfo(java.lang.String jid) throws android.os.RemoteException;
public void addRoster(java.lang.String to, java.lang.String name, java.lang.String groupname) throws android.os.RemoteException;
public void deleteRoster(java.lang.String to) throws android.os.RemoteException;
public void acceptSubscription(java.lang.String to) throws android.os.RemoteException;
public void rejectSubscription(java.lang.String to) throws android.os.RemoteException;
public void createRoom(java.lang.String json) throws android.os.RemoteException;
public void deleteRoom(java.lang.String to, java.lang.String password) throws android.os.RemoteException;
public void joinRoom(java.lang.String to, java.lang.String password) throws android.os.RemoteException;
public void leaveRoom(java.lang.String to) throws android.os.RemoteException;
public void acceptInvitation(java.lang.String to, java.lang.String password) throws android.os.RemoteException;
public void getAffiliation(java.lang.String to, java.lang.String affiliation) throws android.os.RemoteException;
public void grandAffiliation(java.lang.String to, java.lang.String affiliation, java.lang.String jid) throws android.os.RemoteException;
public void grandRole(java.lang.String to, java.lang.String nick, java.lang.String role) throws android.os.RemoteException;
public void sendMessage(java.lang.String to, java.lang.String msg) throws android.os.RemoteException;
public void sendMessageGroup(java.lang.String to, java.lang.String msg, java.lang.String thread) throws android.os.RemoteException;
public void sendMessageParticipant(java.lang.String to, java.lang.String msg, java.lang.String thread) throws android.os.RemoteException;
public void setMessageState(java.lang.String to, java.lang.String state) throws android.os.RemoteException;
public void ping() throws android.os.RemoteException;
public void setBookmarks() throws android.os.RemoteException;
public void setScratchpad() throws android.os.RemoteException;
public void sendTransferFile(java.lang.String to, java.lang.String filename) throws android.os.RemoteException;
public void acceptTransferFile(java.lang.String id, java.lang.String to) throws android.os.RemoteException;
public void rejectTransferFile(java.lang.String id, java.lang.String to) throws android.os.RemoteException;
public void addServiceListener(com.os4.ecb.service.aidl.IPDXServiceListenerExt listen) throws android.os.RemoteException;
public void removeServiceListener(com.os4.ecb.service.aidl.IPDXServiceListenerExt listen) throws android.os.RemoteException;
}
