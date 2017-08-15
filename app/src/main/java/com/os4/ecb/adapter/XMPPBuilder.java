package com.os4.ecb.adapter;

import com.os4.ecb.beans.FileTransferInfo;
import com.os4.ecb.beans.GroupInfo;
import com.os4.ecb.session.SessionUser;

public class XMPPBuilder {

    private String domain;
    
    public XMPPBuilder(String domain) {
        this.domain = domain;
    }

    public String getInitialClientStream(String domain) throws Exception {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version='1.0' encoding='UTF-8'?>\n");
        xml.append("<stream:stream");
        xml.append(" to='").append(domain).append("'");
        xml.append(" xmlns='jabber:client'");
        xml.append(" xmlns:stream='http://etherx.jabber.org/streams'");
        xml.append(" version='1.0'>\n");
        return xml.toString();
    }

	public String getStarttls() throws Exception {
		StringBuilder xml = new StringBuilder();
		xml.append("<starttls xmlns='urn:ietf:params:xml:ns:xmpp-tls'/>");
		return xml.toString();
	}

	public String authMechanism() {
		StringBuilder xml = new StringBuilder();
		xml.append("<auth mechanism='DIGEST-MD5' xmlns='urn:ietf:params:xml:ns:xmpp-sasl'></auth>");
		return xml.toString();
	}

	public String authResponse(String base64) throws Exception {
		StringBuilder xml = new StringBuilder();
		xml.append("<response xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>"+base64+"</response>");
		return xml.toString();
	}

	public String setBind(String id,String resource) throws Exception {
		StringBuilder xml = new StringBuilder();
		xml.append("<iq id='"+id+"' type='set'><bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'><resource>"+resource+"</resource></bind></iq>");
		return xml.toString();
	}

	public String setSession(String id) throws Exception {
		StringBuilder xml = new StringBuilder();
		xml.append("<iq id='"+id+"' type='set'><session xmlns='urn:ietf:params:xml:ns:xmpp-session'/></iq>");
		return xml.toString();
	}

	public String disconnect() {
        StringBuilder xml = new StringBuilder();
		xml.append("</stream:stream>");
        return xml.toString();        
	}	
    
    public String setSession(SessionUser sessionUser) throws Exception {
        StringBuilder xml = new StringBuilder();
        xml.append("<session type='set' user='"+sessionUser.getUsername()+"' domain='"+domain+"' resource='"+sessionUser.getResource()+"'/>");
        return xml.toString();
    }

	public String setRegistration(SessionUser sessionUser, String id){
		StringBuilder xml = new StringBuilder();
		xml.append("<iq type='set' id='"+id+"'>");
		xml.append("<query xmlns='jabber:iq:register'>");
		xml.append("<x xmlns='jabber:x:data' type='submit'>");
		xml.append("<field var='FORM_TYPE'><value>jabber:iq:register</value></field>");
		xml.append("<field var='username'><value>"+sessionUser.getUsername()+"</value></field>");
		xml.append("<field var='password'><value>"+sessionUser.getPassword()+"</value></field>");
		xml.append("<field var='name'><value>"+sessionUser.getNickName()+"</value></field>");
		xml.append("<field var='email'><value>"+sessionUser.getEmail()+"</value></field>");
		xml.append("</x>");
		xml.append("</query>");
		xml.append("</iq>");
		return xml.toString();
	}

    public String getAuthentication(SessionUser sessionUser,String id){
    	StringBuilder xml = new StringBuilder();
    	xml.append("<iq id='"+id+"' type='get'><query xmlns='jabber:iq:auth'><username>"+sessionUser.getUsername()+"</username></query></iq>");
    	return xml.toString();
    }
    
    public String setAuthentication(String id,String username,String digest,String resource){
    	StringBuilder xml = new StringBuilder();    	
    	xml.append("<iq type='set' id='"+id+"'>");
    	xml.append("<query xmlns='jabber:iq:auth'>");
    	xml.append("<username>"+username+"</username>");
    	xml.append("<digest>"+digest+"</digest>");
    	xml.append("<resource>"+resource+"</resource>");
    	xml.append("</query>");
    	xml.append("</iq>");
    	return xml.toString();
    }

	public String setSubscribe(String id,String from,String to){
		StringBuilder xml = new StringBuilder();
		xml.append("<presence id='"+id+"' to='"+to+"' type='subscribe' from='"+from+"'/>");
		return xml.toString();
	}

	public String setSubscribed(String id,String from,String to){
		StringBuilder xml = new StringBuilder();
		xml.append("<presence id='"+id+"' to='"+to+"' type='subscribed' from='"+from+"'/>");
		return xml.toString();		
	}
	
	public String setUnsubscribe(String id,String from,String to){
		StringBuilder xml = new StringBuilder();
		xml.append("<presence id='"+id+"' to='"+to+"' type='unsubscribe' from='"+from+"'/>");
		return xml.toString();		
	}
	
	public String setUnsubscribed(String id,String from,String to){
		StringBuilder xml = new StringBuilder();
		xml.append("<presence id='"+id+"' to='"+to+"' type='unsubscribed' from='"+from+"'/>");
		return xml.toString();		
	}
    
	public String getRoasters(String id){
		StringBuilder xml = new StringBuilder();
		xml.append("<iq type='get' id='"+id+"'>");
		xml.append("<query xmlns='jabber:iq:roster'/>");
		xml.append("</iq>");			
		return xml.toString();
	}

	public String setRoster(String id,String jid,String subscription){
		StringBuilder xml = new StringBuilder();
		xml.append("<iq type='set' id='"+id+"'>");
		xml.append("<query xmlns='jabber:iq:roster'>");
		xml.append("<item jid='"+jid+"' subscription='"+subscription+"'/>");
		xml.append("</query>");
		xml.append("</iq>");
		return xml.toString();
	}

	public String setRoster(String id,String jid,String name,String groupname){
		StringBuilder xml = new StringBuilder();
		xml.append("<iq type='set' id='"+id+"'>");
		xml.append("<query xmlns='jabber:iq:roster'>");
		xml.append("<item jid='"+jid+"' name='"+name+"'>");
		xml.append("<group>"+groupname+"</group>");
		xml.append("</item>");
		xml.append("</query>");
		xml.append("</iq>");			
		return xml.toString();
	}
	
    public String getDiscoItems(String id,String to){
    	StringBuilder xml = new StringBuilder();
    	xml.append("<iq ");
    	xml.append("id='"+id+"' ");
    	xml.append("to='"+to+"' ");
    	xml.append("type='get'>");
    	xml.append("<query xmlns='http://jabber.org/protocol/disco#items'/>");
    	xml.append("</iq>");
		return xml.toString();
    }

    public String getDiscoInfo(String id,String to){
    	StringBuilder xml = new StringBuilder();
    	xml.append("<iq ");
    	xml.append("id='"+id+"' ");
    	xml.append("to='"+to+"' ");
    	xml.append("type='get'>");
    	xml.append("<query xmlns='http://jabber.org/protocol/disco#info'/>");
    	xml.append("</iq>");
		return xml.toString();
    }
    
    public String getAvatar(String id,String to){
    	StringBuilder xml = new StringBuilder();
    	xml.append("<iq id='"+id+"' ");
    	xml.append("type='get'"); 
    	if(to!=null) xml.append(" to='"+to+"'>");
    	else xml.append(">");
    	xml.append("<vCard xmlns='vcard-temp'/>");
    	xml.append("</iq>");
    	return xml.toString();
    }
    
    public String setAvatar(String id,String from,String to,String nickname,String resource,String email,String base64){
    	StringBuilder xml = new StringBuilder();
    	xml.append("<iq type='set' id='"+id+"' from='"+from+"' to='"+to+"'>");
    	xml.append("<vCard xmlns='vcard-temp'>");
    	xml.append("<NICKNAME>"+nickname+"</NICKNAME>");
    	xml.append("<RESOURCE>"+resource+"</RESOURCE>");
    	xml.append("<PHOTO><TYPE>image/png</TYPE><BINVAL>");
    	xml.append(base64);
    	xml.append("</BINVAL></PHOTO>");
    	xml.append("<EMAIL><HOME/><INTERNET/><PREF/><USERID>"+email+"</USERID></EMAIL>");
    	xml.append("</vCard></iq>");
    	return xml.toString();
    }
    
	public String setPresence(String id,SessionUser sessionUser){
		StringBuilder xml = new StringBuilder();
		xml.append("<presence id='"+id+"'>");
		xml.append("<show>"+sessionUser.getShow()+"</show>");			
		xml.append("<status>"+sessionUser.getStatus()+"</status>");
		xml.append("</presence>");
		return xml.toString();
	}	
	
	public String acceptInvitation(String id,String to,String password){
		StringBuilder xml = new StringBuilder();
		xml.append("<presence id='"+id+"' to='"+to+"'>");
		xml.append("<x xmlns='http://jabber.org/protocol/muc'>");			
		if(password!=null) xml.append("<password>"+password+"</password>");
		xml.append("</x>");
		xml.append("</presence>");
		return xml.toString();
	}
	
    public String getAffiliation(String id,String to,String affiliation){
    	StringBuilder xml = new StringBuilder();
    	xml.append("<iq ");
    	xml.append("id='"+id+"' ");
    	xml.append("to='"+to+"' ");
    	xml.append("type='get'>");
    	xml.append("<query xmlns='http://jabber.org/protocol/muc#admin'>");
    	xml.append("<item affiliation='"+affiliation+"'/>");
    	xml.append("</query>");
    	xml.append("</iq>");
		return xml.toString();
    }
    
    public String joinRoom(String id,String to,String password){
    	StringBuilder xml = new StringBuilder();
    	xml.append("<presence id='"+id+"' to='"+to+"'>");
		if(password!=null){
			xml.append("<x xmlns='http://jabber.org/protocol/muc'>");
			xml.append("<password>"+password+"</password>");
			xml.append("</x>");
		}else
			xml.append("<x xmlns='http://jabber.org/protocol/muc'/>");
		xml.append("</presence>");
		return xml.toString();
    }
    
    public String getAffiliation(String id,String from,String to,String affiliation){
    	StringBuilder xml = new StringBuilder();
    	xml.append("<iq id='"+id+"' from='"+from+"' to='"+to+"' type='get'>");
    	xml.append("<query xmlns='http://jabber.org/protocol/muc#admin'>");
    	xml.append("<item affiliation='"+affiliation+"'/>");
    	xml.append("</query>");
    	xml.append("</iq>");    	
		return xml.toString();
    }
    
    public String grandAffiliation(String id,String from,String to,String affiliation,String jid){
    	StringBuilder xml = new StringBuilder();
    	xml.append("<iq id='"+id+"' from='"+from+"' to='"+to+"' type='set'>");
    	xml.append("<query xmlns='http://jabber.org/protocol/muc#admin'>");
    	xml.append("<item affiliation='"+affiliation+"' jid='"+jid+"'/>");
    	xml.append("</query>");
    	xml.append("</iq>");    	
		return xml.toString();
    }

    public String grandRole(String id,String from,String to,String nick,String role){
    	StringBuilder xml = new StringBuilder();
    	xml.append("<iq id='"+id+"' from='"+from+"' to='"+to+"' type='set'>");
    	xml.append("<query xmlns='http://jabber.org/protocol/muc#admin'>");
    	xml.append("<item nick='"+nick+"' role='"+role+"'/>");
    	xml.append("</query>");
    	xml.append("</iq>");    	
		return xml.toString();
    }
    
	public String leaveRoom(String id,String from,String to){
		StringBuilder xml = new StringBuilder();
		xml.append("<presence id='"+id+"' from='"+from+"' to='"+to+"' type='unavailable'/>");
		return xml.toString();
	}
    
    public String presenceRoom(String id,String to){
    	StringBuilder xml = new StringBuilder();
    	xml.append("<presence id='"+id+"' to='"+to+"'>");
		xml.append("<x xmlns='http://jabber.org/protocol/muc'/>");
		xml.append("<x xmlns='vcard-temp:x:update'><photo>e41022bb9864cf8d2d8596f2404ca7f0fe38966d</photo></x>");
		xml.append("<x xmlns='jabber:x:avatar'><hash>e41022bb9864cf8d2d8596f2404ca7f0fe38966d</hash></x>");		
		xml.append("</presence>");
		return xml.toString();
    }
    
    public String getRoomInfo(String id,String from,String to){
    	StringBuilder xml = new StringBuilder();
    	xml.append("<iq id='"+id+"' from='"+from+"' to='"+to+"' type='get'>");
    	xml.append("<query xmlns='http://jabber.org/protocol/muc#owner'>");
    	xml.append("</query>");
    	xml.append("</iq>");    	
		return xml.toString();
    }
    
    public String createRoom(String id,String from,String to,GroupInfo group){
    	StringBuilder xml = new StringBuilder();
    	xml.append("<iq id='"+id+"' from='"+from+"' to='"+to+"' type='set'>");
    	xml.append("<query xmlns='http://jabber.org/protocol/muc#owner'>");
    	xml.append("<x xmlns='jabber:x:data' type='submit'>");
    	xml.append("<field var='FORM_TYPE'><value>http://jabber.org/protocol/muc#roomconfig</value></field>");
    	xml.append("<field var='muc#roomconfig_roomname'><value>"+group.getName()+"</value></field>");
    	xml.append("<field var='muc#roomconfig_roomdesc'><value>"+group.getDescription()+"</value></field>");
    	
		if(group.isPublicRoom()) xml.append("<field var='muc#roomconfig_publicroom'><value>1</value></field>");
		else xml.append("<field var='muc#roomconfig_publicroom'><value>0</value></field>");		
		if(group.isMemberOnly()) xml.append("<field var='muc#roomconfig_membersonly'><value>1</value></field>");
		else xml.append("<field var='muc#roomconfig_membersonly'><value>0</value></field>");		
		if(group.isPersistent()) xml.append("<field var='muc#roomconfig_persistentroom'><value>1</value></field>");
		else xml.append("<field var='muc#roomconfig_persistentroom'><value>0</value></field>");
		
		if(group.isSecured()){
			xml.append("<field var='muc#roomconfig_passwordprotectedroom'><value>1</value></field>");
			xml.append("<field var='muc#roomconfig_roomsecret'><value>"+group.getPassword()+"</value></field>");
		}		
		xml.append("</x>");
		xml.append("</query>");
    	xml.append("</iq>");    	
		return xml.toString();
    }

    public String destroyRoom(String id,String from,String to,String password){
    	StringBuilder xml = new StringBuilder();
    	xml.append("<iq id='"+id+"' from='"+from+"' to='"+to+"' type='set'>");
    	xml.append("<query xmlns='http://jabber.org/protocol/muc#owner'>");
    	xml.append("<destroy>");
		if(password!=null) xml.append("<password>"+password+"</password>");
		xml.append("</destroy>");    	
    	xml.append("</query>");
    	xml.append("</iq>");    	
		return xml.toString();
    }

    public String sendMessage(String id,String from,String to,String msg){
    	StringBuilder xml = new StringBuilder();
    	xml.append("<message id='"+id+"' from='"+from+"' to='"+to+"' type='chat'>");
    	xml.append("<body>"+msg+"</body>");
    	xml.append("</message>");
		return xml.toString();
    }
    
    public String sendMessage(String id,String from,String to,String msg,String thread){
    	StringBuilder xml = new StringBuilder();
    	xml.append("<message id='"+id+"' from='"+from+"' to='"+to+"' type='chat'>");
    	xml.append("<body>"+msg+"</body>");
    	if(thread!=null) xml.append("<thread>"+thread+"</thread>");
    	xml.append("</message>");
		return xml.toString();
    }
    
    public String sendMessageGroup(String id,String from,String to,String msg,String thread){
    	StringBuilder xml = new StringBuilder();
    	xml.append("<message id='"+id+"' from='"+from+"' to='"+to+"' type='groupchat'>");
    	xml.append("<body>"+msg+"</body>");
    	if(thread!=null) xml.append("<thread>"+thread+"</thread>");
    	xml.append("</message>");
		return xml.toString();
    }

	public String sendMessageParticipant(String id,String from,String to,String msg,String thread){
		StringBuilder xml = new StringBuilder();
		xml.append("<message id='"+id+"' from='"+from+"' to='"+to+"' type='chat'>");
		xml.append("<body>"+msg+"</body>");
		if(thread!=null) xml.append("<thread>"+thread+"</thread>");
		xml.append("</message>");
		return xml.toString();
	}

    public String setMessageState(String id,String from,String to,String state,String thread){
    	StringBuilder xml = new StringBuilder();
    	xml.append("<message id='"+id+"' from='"+from+"' to='"+to+"' type='chat'>");
    	if(thread!=null) xml.append("<thread>"+thread+"</thread>");
    	xml.append("<"+state+" xmlns='http://jabber.org/protocol/chatstates'/>");
    	xml.append("</message>");
		return xml.toString();
    }

	public String ping(String id,String from,String to){
		StringBuilder xml = new StringBuilder();
		xml.append("<iq id='"+id+"' ");
		xml.append("from='"+from+"' ");
		xml.append("to='"+to+"' ");
		xml.append("type='get'>");
		xml.append("<ping xmlns='urn:xmpp:ping'/>");
		xml.append("</iq>");
		return xml.toString();
	}

	public String pingReply(String id,String from,String to){
		StringBuilder xml = new StringBuilder();
		xml.append("<iq id='"+id+"' ");
		xml.append("from='"+from+"' ");
		xml.append("to='"+to+"' ");
		xml.append("type='result' xmlns='jabber:client'/>");
		return xml.toString();
	}

	public String setBookmarks(String id){
		StringBuilder xml = new StringBuilder();
		xml.append("<iq type='get' id='"+id+"'>");
		xml.append("<query xmlns='jabber:iq:private'><storage xmlns='storage:bookmarks'/></query>");
    	xml.append("</iq>");
		return xml.toString();
    }    

	public String setScratchpad(String id){
		StringBuilder xml = new StringBuilder();
		xml.append("<iq type='get' id='"+id+"'>");
		xml.append("<query xmlns='jabber:iq:private'><scratchpad xmlns='scratchpad:tasks'/></query>");
    	xml.append("</iq>");
		return xml.toString();
    }

	public String confirm(String id,String from,String to){
		StringBuilder xml = new StringBuilder();
		xml.append("<iq id='"+id+"' ");
		xml.append("from='"+from+"' ");
		xml.append("to='"+to+"' ");
		xml.append("type='result'/>");
		return xml.toString();
	}

	public String replyDiscoInfo(String id,String to){
		/*
		 * The features are the Application capability to response the service
		 */
		StringBuilder xml = new StringBuilder();
		xml.append("<iq id='"+id+"' to='"+to+"' type='result'>");
		xml.append("<query xmlns='http://jabber.org/protocol/disco#info'>");
		xml.append("<identity category='client' name='OS4Messenger' type='mobile'/>");
		xml.append("<feature var=\"http://jabber.org/protocol/disco#items\"/>");
		xml.append("<feature var=\"urn:xmpp:tmp:jingle\"/>");
		//xml.append("<feature var=\"http://jabber.org/protocol/bytestreams\"/>");
		xml.append("<feature var=\"http://jabber.org/protocol/ibb\"/>");
		xml.append("<feature var=\"http://jabber.org/protocol/si\"/>");
		xml.append("<feature var=\"http://jabber.org/protocol/xhtml-im\"/>");
		xml.append("<feature var=\"http://jabber.org/protocol/chatstates\"/>");
		xml.append("<feature var=\"http://jabber.org/protocol/si/profile/file-transfer\"/>");
		xml.append("<feature var=\"urn:xmpp:ping\"/>");
		xml.append("<feature var=\"jabber:iq:last\"/>");
		xml.append("<feature var=\"http://jabber.org/protocol/commands\"/>");
		xml.append("<feature var=\"http://jabber.org/protocol/muc\"/>");
		xml.append("<feature var=\"http://www.xmpp.org/extensions/xep-0166.html#ns\"/>");
		xml.append("<feature var=\"http://jabber.org/protocol/disco#info\"/>");
		xml.append("</query>");
		xml.append("</iq>");
		return xml.toString();
	}

    public String sendTransferFile(String id, String from, String to, FileTransferInfo fileTransferInfo){
		/*
		 * Have to follow the format, otherwise will receive 503 service not available
		 * from : user@domain/resource
		 * to : user@domain/resource
		 */
    	StringBuilder xml = new StringBuilder();
    	xml.append("<iq id='"+id+"' from='"+from+"' to='"+to+"' type='set'>");
    	xml.append("<si xmlns='http://jabber.org/protocol/si' id='"+fileTransferInfo.getSid()+"' mime-type='"+fileTransferInfo.getMimeType()+"' profile='http://jabber.org/protocol/si/profile/file-transfer'>");
    	xml.append("<file xmlns='http://jabber.org/protocol/si/profile/file-transfer' name='"+fileTransferInfo.getName()+"' size='"+Long.toString(fileTransferInfo.getSize())+"'>");
    	xml.append("<desc>Sending file</desc>");
    	xml.append("</file>");
    	xml.append("<feature xmlns='http://jabber.org/protocol/feature-neg'>");
    	xml.append("<x xmlns='jabber:x:data' type='form'>");
    	xml.append("<field var='stream-method' type='list-single'>");
    	//xml.append("<option><value>http://jabber.org/protocol/bytestreams</value></option>");
    	xml.append("<option><value>http://jabber.org/protocol/ibb</value></option>");
    	xml.append("</field>");
    	xml.append("</x>");
    	xml.append("</feature>");
    	xml.append("</si>");    	
    	xml.append("</iq>");			
		return xml.toString();
    }

	public String acceptTransferFile(String id,String from,String to){
		/*
		 * Have to follow the format, otherwise will receive 503 service not available
		 * from : user@domain/resource
		 * to : user@domain/resource
		 */
		StringBuilder xml = new StringBuilder();
		xml.append("<iq id='"+id+"' from='"+from+"' to='"+to+"' type='result'>");
		xml.append("<si xmlns='http://jabber.org/protocol/si'>");
		xml.append("<feature xmlns='http://jabber.org/protocol/feature-neg'>");
		xml.append("<x xmlns='jabber:x:data' type='submit'>");
		xml.append("<field var='stream-method'>");
		//xml.append("<value>http://jabber.org/protocol/bytestreams</value>");
		xml.append("<value>http://jabber.org/protocol/ibb</value>");
		xml.append("</field></x></feature>");
		xml.append("</si>");
		xml.append("</iq>");
		return xml.toString();
	}

	public String rejectTransferFile(String id,String from,String to){
		/*
		 * Have to follow the format, otherwise will receive 503 service not available
		 * from : user@domain/resource
		 * to : user@domain/resource
		 */
		StringBuilder xml = new StringBuilder();
		xml.append("<iq id='"+id+"' from='"+from+"' to='"+to+"' type='error'>");
		xml.append("<error code='406' type='MODIFY'>");
		xml.append("<not-acceptable xmlns='urn:ietf:params:xml:ns:xmpp-stanzas'/>");
		xml.append("</error>");
		xml.append("</iq>");
		return xml.toString();
	}

	public String openTransferFile(String id,String from,String to,FileTransferInfo fileTransferInfo){
		/*
		 * Have to follow the format, otherwise will receive 503 service not available
		 * from : user@domain/resource
		 * to : user@domain/resource
		 */
		StringBuilder xml = new StringBuilder();
		xml.append("<iq id='"+id+"' from='"+from+"' to='"+to+"' type='set'>");
		xml.append("<open xmlns='http://jabber.org/protocol/ibb' block-size='"+fileTransferInfo.getBlockSize()+"' sid='"+fileTransferInfo.getSid()+"' stanza='iq'/>");
		xml.append("</iq>");
		return xml.toString();
	}

	public String dataTransferFile(String id,String from,String to,FileTransferInfo fileTransferInfo){
		/*
		 * Have to follow the format, otherwise will receive 503 service not available
		 * from : user@domain/resource
		 * to : user@domain/resource
		 */
		StringBuilder xml = new StringBuilder();
		xml.append("<iq id='"+id+"' from='"+from+"' to='"+to+"' type='set'>");
		xml.append("<data xmlns='http://jabber.org/protocol/ibb' seq='"+fileTransferInfo.getSeq()+"' sid='"+fileTransferInfo.getSid()+"'>");
		xml.append(fileTransferInfo.getBase64());
		xml.append("</data>");
		xml.append("</iq>");
		return xml.toString();
	}

	public String closeTransferFile(String id,String from,String to,FileTransferInfo fileTransferInfo){
		/*
		 * Have to follow the format, otherwise will receive 503 service not available
		 * from : user@domain/resource
		 * to : user@domain/resource
		 */
		StringBuilder xml = new StringBuilder();
		xml.append("<iq id='"+id+"' from='"+from+"' to='"+to+"' type='set'>");
		xml.append("<close xmlns='http://jabber.org/protocol/ibb' sid='"+fileTransferInfo.getSid()+"'/>");
		xml.append("</iq>");
		return xml.toString();
	}
}
