package com.os4.ecb.session;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.regex.Pattern;

import com.os4.ecb.beans.DiscoInfo;
import com.os4.ecb.beans.DiscoItems;
import com.os4.ecb.beans.AffiliationItems;
import com.os4.ecb.beans.FileTransferInfo;
import com.os4.ecb.beans.RosterItems;

/**
 *
 * @author yanyan
 */
public class SessionRequest {
    
	private DatagramPacket packet;
	private String xml;
	
    private String sessionId;
    private String tag;
    private String id;
    private String ip;
    private String type;
    private String xmlns;
    private String from;
    private String to;
    
    private String jidFrom;
    private String jidTo;
    private String userFrom;
    private String userTo;
    private String roomFrom;
    private String roomTo;
    private String resourceFrom;
    private String resourceTo;
    private String domainFrom;
    private String domainTo;
    private String subDomainFrom;
    private String subDomainTo;
    
    private String errorCode;
    private String errorMessage;

    private String username;
    private String password;
    private String photo;
    private String hash;
	private String photoType;
    private String photoBinVal;
	private String nickname;

    private String msgTag;
    private String inviteTo;
    private String inviteFrom;
    private String declineTo;
    private String declineFrom;
    private String reason;

    /*
     * IQ Packet properties
     */
    private AffiliationItems affiliations = new AffiliationItems();
    private RosterItems rosters = new RosterItems();
    private DiscoItems items = new DiscoItems();
    private DiscoInfo discoInfo = new DiscoInfo();
    
    /*
     * Presence properties
     */
    private String show;
    private String status;
    private String priority;

    /*
     * Message properties
     */
	private String subject;
    private String msgBody;
	private FileTransferInfo fileTransferInfo;

    /*
     * Constructor
     */
    public SessionRequest(){}
    public SessionRequest(DatagramPacket packet){
        this.packet = packet;
    	byte[] buffer = Arrays.copyOf(packet.getData(),packet.getLength());
        this.xml = new String(buffer);        
    }
    public SessionRequest(String str){
    	try{
	    	String[] parts = str.split(Pattern.quote(";;"));
	    	InetAddress ipAddress = InetAddress.getByName(parts[0]);
	    	this.xml = new String(parts[2].getBytes());
	    	this.packet = new DatagramPacket(xml.getBytes(),xml.getBytes().length);
	    	this.packet.setAddress(ipAddress);
	    	this.packet.setPort(Integer.parseInt(parts[1]));
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void setXml(String xml) {
		this.xml = xml;
	}
    
	/*
     * Get & Set Method
     */
    public String getIPPort(){
    	return packet.getAddress().getHostAddress()+":"+Integer.toString(packet.getPort());
    }
    
    public String getHost(){
        return packet.getAddress().getHostAddress();
    }

    public String getSocketAddress(){
        return packet.getSocketAddress().toString();
    }
    
    public int getPort(){
        return packet.getPort();
    }
    
    public String getXml(){
    	return xml;
    }
    
    public int getOffset(){
        return packet.getOffset();
    }
    
    public int getLength(){
        return packet.getLength();
    }    
    
    public String getUDPString(){
    	StringBuilder str = new StringBuilder();
    	str.append(packet.getAddress().getHostAddress()).append(";;");
    	str.append(Integer.toString(packet.getPort())).append(";;");
    	byte[] buffer = Arrays.copyOf(packet.getData(),packet.getLength());
    	str.append(new String(buffer));
    	return str.toString();
    }
    
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getXmlns() {
		return xmlns;
	}
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}	
	public String getJidFrom() {
		return jidFrom;
	}
	public void setJidFrom(String jidFrom) {
		this.jidFrom = jidFrom;
	}
	public String getJidTo() {
		return jidTo;
	}
	public void setJidTo(String jidTo) {
		this.jidTo = jidTo;
	}
	public String getUserFrom() {
		return userFrom;
	}
	public void setUserFrom(String userFrom) {
		this.userFrom = userFrom;
	}
	public String getUserTo() {
		return userTo;
	}
	public void setUserTo(String userTo) {
		this.userTo = userTo;
	}	
	public String getRoomFrom() {
		return roomFrom;
	}
	public void setRoomFrom(String roomFrom) {
		this.roomFrom = roomFrom;
	}
	public String getRoomTo() {
		return roomTo;
	}
	public void setRoomTo(String roomTo) {
		this.roomTo = roomTo;
	}
	public String getResourceFrom() {
		return resourceFrom;
	}
	public void setResourceFrom(String resourceFrom) {
		this.resourceFrom = resourceFrom;
	}
	public String getResourceTo() {
		return resourceTo;
	}
	public void setResourceTo(String resourceTo) {
		this.resourceTo = resourceTo;
	}
	public String getDomainFrom() {
		return domainFrom;
	}
	public void setDomainFrom(String domainFrom) {
		this.domainFrom = domainFrom;
	}
	public String getDomainTo() {
		return domainTo;
	}
	public void setDomainTo(String domainTo) {
		this.domainTo = domainTo;
	}
	public String getSubDomainFrom() {
		return subDomainFrom;
	}
	public void setSubDomainFrom(String subDomainFrom) {
		this.subDomainFrom = subDomainFrom;
	}
	public String getSubDomainTo() {
		return subDomainTo;
	}
	public void setSubDomainTo(String subDomainTo) {
		this.subDomainTo = subDomainTo;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getShow() {
		return show;
	}
	public void setShow(String show) {
		this.show = show;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getPhotoType() {
		return photoType;
	}
	public void setPhotoType(String photoType) {
		this.photoType = photoType;
	}
	public String getPhotoBinVal() {
		return photoBinVal;
	}
	public void setPhotoBinVal(String photoBinVal) {
		this.photoBinVal = photoBinVal;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getMsgTag() {
		return msgTag;
	}
	public void setMsgTag(String msgTag) {
		this.msgTag = msgTag;
	}
	public String getInviteTo() {
		return inviteTo;
	}
	public void setInviteTo(String inviteTo) {
		this.inviteTo = inviteTo;
	}	
	public String getInviteFrom() {
		return inviteFrom;
	}
	public void setInviteFrom(String inviteFrom) {
		this.inviteFrom = inviteFrom;
	}	
	public String getDeclineTo() {
		return declineTo;
	}
	public void setDeclineTo(String declineTo) {
		this.declineTo = declineTo;
	}
	public String getDeclineFrom() {
		return declineFrom;
	}
	public void setDeclineFrom(String declineFrom) {
		this.declineFrom = declineFrom;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMsgBody() {
		return msgBody;
	}
	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}	
	public RosterItems getRosters() {
		return rosters;
	}
	public void setRosters(RosterItems rosters) {
		this.rosters = rosters;
	}
	public DiscoItems getItems() {
		return items;
	}
	public void setItems(DiscoItems items) {
		this.items = items;
	}
	public DiscoInfo getDiscoInfo() {
		return discoInfo;
	}
	public void setDiscoInfo(DiscoInfo discoInfo) {
		this.discoInfo = discoInfo;
	}
	public AffiliationItems getAffiliations() {
		return affiliations;
	}
	public void setAffiliations(AffiliationItems affiliations) {
		this.affiliations = affiliations;
	}
	public FileTransferInfo getFileTransferInfo() {
		return fileTransferInfo;
	}
	public void setFileTransferInfo(FileTransferInfo fileTransferInfo) {
		this.fileTransferInfo = fileTransferInfo;
	}
}
