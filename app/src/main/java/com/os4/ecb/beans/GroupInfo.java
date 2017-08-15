package com.os4.ecb.beans;

public class GroupInfo {

	private String jid;
	private String name;
	private String description;
	private String password;
	private boolean publicRoom = true;
	private boolean memberOnly = false;
	private boolean persistent = true;
	
	public GroupInfo(){}
	public GroupInfo(String jid){
		this.jid = jid;
		this.name = jid.substring(0, jid.indexOf("@"));
		this.description = jid.substring(0, jid.indexOf("@"));
	}
	public GroupInfo(String jid,String password){
		this.jid = jid;
		this.name = jid.substring(0, jid.indexOf("@"));
		this.description = jid.substring(0, jid.indexOf("@"));
		this.password = password;		
	}
	public GroupInfo(String jid,String name,String description){
		this.jid = jid;
		this.name = name;
		this.description = description;
	}
	public GroupInfo(String jid,String name,String description,String password){
		this.jid = jid;
		this.name = name;
		this.description = description;
		this.password = password;
	}	
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isPublicRoom() {
		return publicRoom;
	}
	public void setPublicRoom(boolean publicRoom) {
		this.publicRoom = publicRoom;
	}
	public boolean isMemberOnly() {
		return memberOnly;
	}
	public void setMemberOnly(boolean memberOnly) {
		this.memberOnly = memberOnly;
	}
	public boolean isPersistent() {
		return persistent;
	}
	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}
	public boolean isSecured() {
		return password!=null;
	}
}
