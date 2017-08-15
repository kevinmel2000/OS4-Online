package com.os4.ecb.adapter;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.os4.ecb.beans.DiscoItems;
import com.os4.ecb.beans.FileTransferInfo;
import com.os4.ecb.beans.GroupInfo;
import com.os4.ecb.beans.RosterItems;
import com.os4.ecb.misc.Base64;
import com.os4.ecb.misc.SASLMechanism;
import com.os4.ecb.session.SessionEvent;
import com.os4.ecb.session.SessionListener;
import com.os4.ecb.session.SessionRequest;
import com.os4.ecb.session.SessionUser;
import com.os4.ecb.misc.AuthFactory;

/**
 *
 * @author yanyan
 */
public class TCPConnector extends Connector {

    private static final String TAG = TCPConnector.class.getName();
	
	protected Socket tcpSocket;	
    private Writer writer;
    private Reader reader;
    private XmlPullParserFactory factory;
	private XmlPullParser parser;

    private boolean isAuthorize = false;
	private String lastxmlns;
	private List<SessionListener> listeners = new ArrayList<SessionListener>();
	
	private String filterChatType = "chat";
	private String filterGroupChatType = "groupchat";
	private String xmlns;

	public TCPConnector(String domain,String ip,int port) {
		super(domain,ip,port);
	}
	
	public SessionUser getSessionUser() {
		return sessionUser;
	}

	public void setSessionUser(SessionUser sessionUser) {
		this.sessionUser = sessionUser;
	}

	public boolean isConnected(){
		if(tcpSocket!=null) return tcpSocket.isConnected();
		else return false;
	}
	
    public void connect(SessionUser sessionUser) {
		Log.i(TAG,"connect");
    	this.sessionUser = sessionUser;
    	this.sessionUser.setDomain(domain);
    	try {
			InetAddress inetAddress = InetAddress.getByName(ipAddress);
			SocketAddress socketAddress = new InetSocketAddress(inetAddress, port);
			this.tcpSocket = new Socket();
			this.tcpSocket.connect(socketAddress, 10000);
			this.reader = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream(), "UTF-8"));
			this.writer = new BufferedWriter(new OutputStreamWriter(tcpSocket.getOutputStream(), "UTF-8"));
			this.factory = XmlPullParserFactory.newInstance();
			this.factory.setNamespaceAware(true);
			String xmlresp = xmppBuilder.getInitialClientStream(domain);
			send(xmlresp);
		}catch(IOException io){
			Log.e(TAG,io.getMessage());
			onErrorResponse(io.getMessage(),null);
    	}catch(Exception e){
    		Log.e(TAG,e.getMessage());
    	}
    }

    public void signout(){
    	try{
			isAuthorize = false;
	        String xmlresp = xmppBuilder.disconnect();
	        send(xmlresp);
    	}catch(Exception e){
    		Log.e(TAG,e.getMessage());
    	}    	
    }

	@Override
    public void run()
    {
        while(!Thread.currentThread().isInterrupted())
        {        	
            try{
				parser = factory.newPullParser();
                parser.setInput(reader);
                onXMLParser(parser);

            }catch(NullPointerException n){
				Log.e(TAG,n.getMessage());
				Thread.currentThread().interrupt();
            }catch(Exception e){
				Log.e(TAG,e.getMessage());
				Thread.currentThread().interrupt();
            }
        }
		if(tcpSocket!=null) onSessionClose("Interrupted");
    }

	public void disconnect()
	{
		Log.i(TAG,"disconnect");
		try
		{
			if(!Thread.currentThread().isInterrupted()) Thread.currentThread().interrupt();
			onDisconnected("disconnected");
		}
		catch( Exception e ){
			Log.e(TAG,e.getMessage());
		}
	}

	public synchronized void send(String xml)
	{
		try
		{
			if(writer!=null) {
				writer.write(xml);
				writer.flush();
			}
		}
		catch(Exception e){
			Log.e(TAG,e.getMessage());
		}
	}

	public void onXMLParser(XmlPullParser parser) {
        try {
            int eventType = parser.getEventType();
            do {
                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("stream")) {
                    	SessionRequest request = new SessionRequest();
                    	request.setXml(parser.getText());
                        for (int i=0; i<parser.getAttributeCount(); i++) {
                            if (parser.getAttributeName(i).equals("id")) {
                                String sessionId = parser.getAttributeValue(i);
                                request.setId(sessionId);
                            }
                            else if (parser.getAttributeName(i).equals("from")) {
                                String domain = parser.getAttributeValue(i);
                                request.setDomainFrom(domain);
                            }                            
                        }
                    }
                    else {                    	
                        String xml = parse(parser);
                        SessionRequest request = new SessionRequest();
                        request.setXml(xml);
                        onDataCapture(request);
                    }
                }
                else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("stream")) {
                    	onSessionClose("request to disconnected");
                    }
                }

                eventType = parser.next();

            } while (eventType != XmlPullParser.END_DOCUMENT);

			Thread.currentThread().interrupt();

    	}catch(XmlPullParserException x){
            Log.e(TAG,x.getMessage());
			Thread.currentThread().interrupt();
    	}catch(IOException io){
    		Log.e(TAG,io.getMessage());
			Thread.currentThread().interrupt();
    	}catch(Exception e){
    		Log.e(TAG,e.getMessage());
			Thread.currentThread().interrupt();
    	}
    }
	
    private String parse(XmlPullParser parser) throws Exception {
        
        StringBuilder xml = new StringBuilder();        
        String prefix = parser.getPrefix();
        String tag = parser.getName();
        String namespace = parser.getNamespace();
        
        if(prefix!=null) xml.append("<"+prefix+":"+tag);
        else xml.append("<"+tag);
        
        for(int i=0;i<parser.getAttributeCount();i++)
        	xml.append(" "+parser.getAttributeName(i)+"='"+parser.getAttributeValue(i)+"'");        
        
        if(!namespace.equalsIgnoreCase(xmlns)) {
        	xml.append(" xmlns='"+namespace+"'");
        	xmlns = namespace;
        }
        
        xml.append(">");
        		
        boolean done = false;
        while (!done) {
            int eventTag = parser.next();
            if (eventTag == XmlPullParser.START_TAG) {
                String child = parse(parser);
                xml.append(child);
            }
            else if (eventTag == XmlPullParser.TEXT) {
                xml.append(parser.getText());
            }
            else if (eventTag == XmlPullParser.END_TAG) {
                if(prefix!=null) xml.append("</"+prefix+":"+tag+">");
                else xml.append("</"+tag+">");
                done = true;
            }
        }
        return xml.toString();
    }

	public void onSessionOpen(SessionRequest request){
		Log.d(TAG,"onSessionOpen");
		try{
			this.domain = request.getDomainFrom();
			sessionUser.setSessionId(request.getId());
			onConnected(request.getId(),request.getDomainFrom());

		}catch(Exception e){
			Log.e(TAG,e.getMessage());
		}
	}

	public void onSessionClose(String reason){
		Log.d(TAG,"onSessionClose "+reason);
		try
		{
			if(writer!=null) writer.close();
			if(reader!=null) reader.close();
			if(tcpSocket!=null) tcpSocket.close();
		}
		catch( Exception e ){
			Log.e(TAG,e.getMessage());
		}
		writer = null;reader = null;tcpSocket = null;
		onSessionClose(reason,null);
	}

    public void onDataCapture(SessionRequest request) {
    	try{
	        String xml = request.getXml();
            Document doc = getDocument(xml);
            Element rootElement = doc.getDocumentElement();
            request = getSessionBean(request,rootElement);

			if(request.getTag().startsWith("stream:features")){
				Log.d(TAG,"RESPONSE:"+xml);
				if(sessionUser.isSuccess()) {
					String bind = xmppBuilder.setBind(nextId(), sessionUser.getResource());
					send(bind);
					Log.d(TAG,"REQUEST:"+bind);
					sessionUser.setSuccess(false);
				}
				else onConnected(ipAddress,domain);
			}
			else if(request.getTag().startsWith("challenge")) {
				Log.d(TAG,"RESPONSE:"+xml);
				byte[] challenge = Base64.decode(rootElement.getTextContent());
				Log.d(TAG,new String(challenge));
				SASLMechanism sasl = new SASLMechanism(sessionUser);
				byte[] response = sasl.evaluateChallenge(challenge);
				String base64Response = Base64.encodeBytes(response,Base64.DONT_BREAK_LINES);
				String xmlresp = xmppBuilder.authResponse(base64Response);
				send(xmlresp);
				Log.d(TAG,"REQUEST:"+xmlresp);
			}
			else if(request.getTag().startsWith("success")) {
				Log.d(TAG,"RESPONSE:"+xml);
				sessionUser.setSuccess(true);
				parser.setInput(reader);
				String xmlresp = xmppBuilder.getInitialClientStream(domain);
				send(xmlresp);
				Log.d(TAG,"REQUEST:"+xmlresp);
			}
			else if(request.getTag().startsWith("failure")) {
				Log.d(TAG,"RESPONSE:"+xml);
				onErrorResponse(xml,request);
			}
			else if(request.getTag().startsWith("session")){
				onSessionOpen(request);
			}
			else if(request.getTag().startsWith("iq")){
				SessionEvent evt = new SessionEvent(this,xml,request);
				onDataIQ(evt);				
			}
			else if(request.getTag().startsWith("presence")){
				SessionEvent evt = new SessionEvent(this,xml,request);
				onDataPresence(evt);								
			}
			else if(request.getTag().startsWith("message")){
				SessionEvent evt = new SessionEvent(this,xml,request);
				onDataMessage(evt);				
			}
			else{
				Log.d(TAG,"RESPONSE:"+xml);
				SessionEvent evt = new SessionEvent(this,xml,request);
				onDataRequest(evt);
			}
			
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    public void onDataIQ(SessionEvent event){
    	try{
    		String xml = (String) event.getData();
    		SessionRequest request  = event.getRequest();
    		
    		if(request.getType()!=null && request.getType().equalsIgnoreCase("result")){
    			if(request.getXmlns()!=null && request.getXmlns().equalsIgnoreCase("jabber:iq:auth")){
    				String sessionId = sessionUser.getSessionId();
    				String password = sessionUser.getPassword();
    				String scamblePassword = sessionId.concat(password);
    				String digest = AuthFactory.hash(scamblePassword);
    				
    				String xmlresp = xmppBuilder.setAuthentication(nextId(), sessionUser.getUsername(), digest, sessionUser.getResource());
    				send(xmlresp);
    				isAuthorize = false;
    				lastxmlns = "jabber:iq:auth";
    				Log.d(TAG,"REQUEST:"+xmlresp);
    			}
				else if(request.getXmlns()!=null && request.getXmlns().equalsIgnoreCase("urn:ietf:params:xml:ns:xmpp-bind")){
					String xmlresp = xmppBuilder.setSession(nextId());
					send(xmlresp);
					lastxmlns = "urn:ietf:params:xml:ns:xmpp-session";
					Log.d(TAG,"REQUEST:"+xmlresp);
				}
    			else if(request.getXmlns()!=null && request.getXmlns().equalsIgnoreCase("jabber:iq:roster")){
    				onGetRosters(xml,request);    				
    			}
    			else if(request.getXmlns()!=null && request.getXmlns().equalsIgnoreCase("http://jabber.org/protocol/disco#items")){
					if(request.getSubDomainFrom()==null) onUpdateService(xml,request);
					else if(request.getSubDomainFrom()!=null && request.getRoomFrom()== null) onUpdateGroup(xml,request);
					//else if(request.getSubDomainFrom()!=null && request.getRoomFrom()!= null) onUpdateParticipant(xml,request);
    			} 
    			else if(request.getXmlns()!=null && request.getXmlns().equalsIgnoreCase("http://jabber.org/protocol/disco#info")){
    				if(request.getRoomFrom()==null) onUpdateServiceInfo(xml,request);
    				else if(request.getRoomFrom()!=null) onUpdateGroupInfo(xml,request);
    			}
				else if(request.getXmlns()!=null && request.getXmlns().equalsIgnoreCase("http://jabber.org/protocol/muc#owner")){
					onUpdateGroup(xml,request);
				}
    			else if(request.getXmlns()!=null && request.getXmlns().equalsIgnoreCase("http://jabber.org/protocol/muc#admin")){
    				onGetAffiliation(xml,request);
    			}
				else if(request.getXmlns()!=null && request.getXmlns().equalsIgnoreCase("vcard-temp")){
					onUpdateAvatar(xml,request);
				}
    			else if(request.getXmlns()==null){
    				if(lastxmlns!=null && lastxmlns.equalsIgnoreCase("jabber:iq:auth")){
    					isAuthorize = true;
    					lastxmlns = null;
    					onAuthorized();
    				}
					else if(lastxmlns!=null && lastxmlns.equalsIgnoreCase("urn:ietf:params:xml:ns:xmpp-session")){
						isAuthorize = true;
						lastxmlns = null;
						onAuthorized();
					}
					else if(lastxmlns!=null && lastxmlns.equalsIgnoreCase("jabber:iq:register")){
						lastxmlns = null;
						onRegistered();
					}
					else if(lastxmlns!=null && lastxmlns.equalsIgnoreCase("urn:xmpp:ping")){
						lastxmlns = null;
						onPing();
					}
    				else
    					onIQPacket(xml,request);
    			}
				else if(request.getXmlns()!=null && request.getXmlns().equalsIgnoreCase("http://jabber.org/protocol/si")){
					onTransferFile(xml,request);
				}
    			else
    				onIQPacket(xml,request);
    		}
    		else if(request.getType()!=null && request.getType().equalsIgnoreCase("set")){
    			if(request.getXmlns()!=null && request.getXmlns().equalsIgnoreCase("jabber:iq:roster")){    				
    				RosterItems items = request.getRosters();
    				for(RosterItems.Item item : items.getItems()){
    					if(item.getAsk()!=null && item.getAsk().equalsIgnoreCase("subscribe")){
    						onAddRoster(xml,request);
    					}
						else if(item.getSubscription().equalsIgnoreCase("from")){
							onUpdateRoster(xml,request);
						}
						else if(item.getSubscription().equalsIgnoreCase("to")){
							onUpdateRoster(xml,request);
						}
    					else if(item.getSubscription().equalsIgnoreCase("both")){
    						onUpdateRoster(xml,request);
    					}
    					else if(item.getSubscription().equalsIgnoreCase("remove")){
    						onDeleteRoster(xml,request);
    					}
    					else if(item.getSubscription().equalsIgnoreCase("none")){
    						onDeleteRoster(xml,request);
    					}    					
    				}
    			}
				else if(request.getXmlns()!=null && request.getXmlns().equalsIgnoreCase("http://jabber.org/protocol/si")){
					onTransferFile(xml,request);
				}
				else if(request.getXmlns()!=null && request.getXmlns().equalsIgnoreCase("http://jabber.org/protocol/ibb")){
					onTransferFile(xml,request);
					String xmlresp = xmppBuilder.confirm(request.getId(),sessionUser.getJid(),request.getFrom());
					send(xmlresp);
				}
    			else
    				onIQPacket(xml,request);
    		}
			else if(request.getType()!=null && request.getType().equalsIgnoreCase("get")){
				if(request.getXmlns()!=null && request.getXmlns().equalsIgnoreCase("urn:xmpp:ping")){
					String xmlresp = xmppBuilder.pingReply(request.getId(),sessionUser.getJid(),domain);
					send(xmlresp);
				}
				else if(request.getXmlns()!=null && request.getXmlns().equalsIgnoreCase("http://jabber.org/protocol/disco#info")){
					String xmlresp = xmppBuilder.replyDiscoInfo(request.getId(),request.getFrom());
					send(xmlresp);
				}
				else
					onIQPacket(xml,request);
			}
    		else if(request.getType()!=null && request.getType().equalsIgnoreCase("error")){
    			onErrorResponse(xml,request);
    		}
    		else
    			onIQPacket(xml,request);

    		Log.d(TAG,"RESPONSE:"+xml);
    		
    	}catch(Exception e){
    		if(e.getMessage()!=null) Log.e(TAG,e.getMessage());
    		e.printStackTrace();
    	}    	
    }
	
	public void onDataPresence(SessionEvent event){
    	try{
    		String xml = (String) event.getData();
    		SessionRequest request = event.getRequest();
    		
    		if(request.getType()!=null && request.getType().equalsIgnoreCase("subscribe")){
    			onSubscribe(xml,request);
    		}else if(request.getType()!=null && request.getType().equalsIgnoreCase("subscribed")){
    			
    		}
    		else if(request.getType()!=null && request.getType().equalsIgnoreCase("unsubscribe")){
				String xmlresp = xmppBuilder.setUnsubscribed(nextId(),request.getJidTo(),request.getJidFrom());
				send(xmlresp);
				onUnsubscribe(xml,request);
    		}
    		else if(request.getType()!=null && request.getType().equalsIgnoreCase("unsubscribed")){

			}
			else if(request.getRoomFrom()!=null && request.getXmlns()!=null && request.getXmlns().equalsIgnoreCase("http://jabber.org/protocol/muc#user")){
				onGetAffiliation(xml,request);
    		}
			else if(request.getRoomFrom()!=null && request.getXmlns()==null && !request.getType().equalsIgnoreCase("error")){
    			onPresenceGroup(xml,request);
    		}
    		else if(request.getType()!=null && request.getType().equalsIgnoreCase("error")){
    			onErrorResponse(xml,request);
    		}    		
    		else
    			onPresence(xml,request);
    		
    		Log.d(TAG,"RESPONSE:"+xml);
    		
    	}catch(Exception e){
    		Log.e(TAG,e.getMessage());
    		e.printStackTrace();
    	}    			
	}
	
	public void onDataMessage(SessionEvent event){
    	try{
    		String xml = (String) event.getData();
    		SessionRequest request = event.getRequest();

			if(request.getType()!=null && request.getType().equalsIgnoreCase("error")){
				onErrorResponse(request.getXml(),request);
			}
			else {
				if (request.getXmlns() != null && request.getXmlns().equalsIgnoreCase("http://jabber.org/protocol/muc#user")) {
					if (request.getInviteFrom() != null) {
						onInvitation(xml, request);
					}
				}
				if (request.getXmlns() != null && request.getXmlns().equalsIgnoreCase("http://jabber.org/protocol/chatstates")) {
					onMessageState(xml, request);
				} else {
					onMessagePacket(xml, request);
				}

				if (request.getMsgBody() != null || request.getSubject() != null) {
					if (request.getType().equalsIgnoreCase(filterGroupChatType)) {
						onMessageGroup(xml, request);
					} else if (request.getRoomFrom() != null && request.getType().equalsIgnoreCase(filterChatType)) {
						onMessageParticipant(xml, request);
					} else {
						onMessage(xml, request);
					}
				}
			}
    		Log.d(TAG,"RESPONSE:"+xml);
    		
    	}catch(Exception e){
    		Log.e(TAG,e.getMessage());
    		e.printStackTrace();
    	}		
	}
	
	public void onDataRequest(SessionEvent event){
    	try{
    		String xml = (String) event.getData();
    		Log.d(TAG,"RESPONSE:"+xml);    		
    		
    	}catch(Exception e){
    		Log.e(TAG,e.getMessage());
    		e.printStackTrace();
    	}		
	}
    
    public void addSessionListener( SessionListener listener )
    {
        listeners.add(listener);
    }
    
    public void removeSessionListener( SessionListener listener ) 
    {
    	if(listener==null) listeners.clear();
    	else listeners.remove( listener );
    }

	public void register(){
		lastxmlns = "jabber:iq:register";
		String xml = xmppBuilder.setRegistration(sessionUser,nextId());
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void authorize(){
		/* Old version
		String xml = xmppBuilder.getAuthentication(sessionUser,nextId());
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
		*/
		String auth = xmppBuilder.authMechanism();
		send(auth);
		Log.d(TAG,"REQUEST:"+auth);
	}

	public void getRosters(){
		String xml = xmppBuilder.getRoasters(nextId());
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void setPresence(){
		String xml = xmppBuilder.setPresence(nextId(),sessionUser);
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void setPresence(SessionUser sessionUser){
		String xml = xmppBuilder.setPresence(nextId(),sessionUser);
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void getAvatar(String to){
		String xml = xmppBuilder.getAvatar(nextId(),to);
		send(xml);
		lastxmlns = "vcard-temp";
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void setAvatar(String to,String nickname,String resource,String email,String base64){
		String xml = xmppBuilder.setAvatar(nextId(),sessionUser.getJid(),to,nickname,resource,email,base64);
		send(xml);
		lastxmlns = "vcard-temp";
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void getService(){
		String xml = xmppBuilder.getDiscoItems(nextId(),domain);
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void getDiscoItems(String jid){
		String xml = xmppBuilder.getDiscoItems(nextId(),jid);
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void getDiscoInfo(String jid){
		String xml = xmppBuilder.getDiscoInfo(nextId(),jid);
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void addRoster(String to, String name, String groupname){
		String xmlr = xmppBuilder.setRoster(nextId(),to,name,groupname);
		send(xmlr);
		Log.d(TAG,"REQUEST:"+xmlr);
		String xmlp = xmppBuilder.setSubscribe(nextId(), sessionUser.getJid(), to);
		send(xmlp);
		Log.d(TAG,"REQUEST:"+xmlp);
	}

	public void deleteRoster(String to){
		String xml = xmppBuilder.setRoster(nextId(), to, "remove");
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void acceptSubscription(String to){
		String xml = xmppBuilder.setSubscribed(nextId(), sessionUser.getJid(), to);
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void rejectSubscription(String to){
		String xml = xmppBuilder.setUnsubscribe(nextId(), sessionUser.getJid(), to);
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void acceptInvitation(String to,String password){
		String xmlresp = xmppBuilder.acceptInvitation(nextId(),to+"/"+sessionUser.getNickName(),password);
		send(xmlresp);
	}

	public void joinRoom(String to,String password){
		String xml = xmppBuilder.joinRoom(nextId(),to+"/"+sessionUser.getNickName(),password);
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void leaveRoom(String to){
		String xml = xmppBuilder.leaveRoom(nextId(),sessionUser.getJid(),to);
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void getAffiliation(String to,String affiliation){
		String xml = xmppBuilder.getAffiliation(nextId(),sessionUser.getJid(),to,affiliation);
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void grandAffiliation(String to,String affiliation,String jid){
		String xml = xmppBuilder.grandAffiliation(nextId(),sessionUser.getJid(),to,affiliation,jid);
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void grandRole(String to,String nick,String role){
		String xml = xmppBuilder.grandRole(nextId(),sessionUser.getJid(),to,nick,role);
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void createRoom(GroupInfo group){
		String xml = xmppBuilder.presenceRoom(nextId(),group.getJid()+"/"+sessionUser.getNickName());
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
		String xmlg = xmppBuilder.getRoomInfo(nextId(),sessionUser.getJid(),group.getJid()+"/"+sessionUser.getNickName());
		send(xmlg);
		Log.d(TAG,"REQUEST:"+xmlg);
		String xmlc = xmppBuilder.createRoom(nextId(),sessionUser.getJid(),group.getJid()+"/"+sessionUser.getNickName(),group);
		send(xmlc);
		Log.d(TAG,"REQUEST:"+xmlc);
	}

	public void deleteRoom(String to,String password){
		String xml = xmppBuilder.joinRoom(nextId(),to+"/"+sessionUser.getNickName(),password);
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
		String xmlc = xmppBuilder.destroyRoom(nextId(),sessionUser.getJid(),to,password);
		send(xmlc);
		Log.d(TAG,"REQUEST:"+xmlc);
	}

	public void sendMessage(String to,String msg){
		String xml = xmppBuilder.sendMessage(nextId(),sessionUser.getJid()+"/"+sessionUser.getResource(),to,msg);
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void sendMessage(String to,String msg,String thread){
		String xml = xmppBuilder.sendMessage(nextId(),sessionUser.getJid()+"/"+sessionUser.getResource(),to,msg,generateId());
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void sendMessageGroup(String to,String msg,String thread){
		String xml = xmppBuilder.sendMessageGroup(nextId(),sessionUser.getJid()+"/"+sessionUser.getResource(),to,msg,generateId());
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void sendMessageParticipant(String to,String msg,String thread){
		String xml = xmppBuilder.sendMessageParticipant(nextId(),sessionUser.getJid(),to,msg,generateId());
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void setMessageState(String to,String state){
		String xml = xmppBuilder.setMessageState(nextId(),sessionUser.getJid()+"/"+sessionUser.getResource(),to,state,generateId());
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void ping(){
		String xml = xmppBuilder.ping(nextId(),sessionUser.getJid()+"/"+sessionUser.getResource(),domain);
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
		lastxmlns = "urn:xmpp:ping";
	}

	public void setBookmarks(){
		String xml = xmppBuilder.setBookmarks(nextId());
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void setScratchpad(){
		String xml = xmppBuilder.setScratchpad(nextId());
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	/*
	 * File Transfer
	 */
	public void sendTransferFile(String id,String to,FileTransferInfo fileTransferInfo){
		String xml = xmppBuilder.sendTransferFile(id,sessionUser.getJid()+"/"+sessionUser.getResource(),to,fileTransferInfo);
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void acceptTransferFile(String id,String to){
		String xml = xmppBuilder.acceptTransferFile(id,sessionUser.getJid()+"/"+sessionUser.getResource(),to);
		send(xml);
		Log.d(TAG,"REPLY:"+xml);
	}

	public void rejectTransferFile(String id,String to){
		String xml = xmppBuilder.rejectTransferFile(id,sessionUser.getJid()+"/"+sessionUser.getResource(),to);
		send(xml);
		Log.d(TAG,"REPLY:"+xml);
	}

	public void openTransferFile(String id,String to,FileTransferInfo fileTransferInfo){
		String xml = xmppBuilder.openTransferFile(id,sessionUser.getJid()+"/"+sessionUser.getResource(),to,fileTransferInfo);
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void dataTransferFile(String id,String to,FileTransferInfo fileTransferInfo){
		String xml = xmppBuilder.dataTransferFile(id,sessionUser.getJid()+"/"+sessionUser.getResource(),to,fileTransferInfo);
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}

	public void closeTransferFile(String id,String to,FileTransferInfo fileTransferInfo){
		String xml = xmppBuilder.closeTransferFile(id,sessionUser.getJid()+"/"+sessionUser.getResource(),to,fileTransferInfo);
		send(xml);
		Log.d(TAG,"REQUEST:"+xml);
	}
	/*
	 * SessionListener
	 */
    public void onConnected(String connectionId,String serviceName){
    	SessionEvent event = new SessionEvent(this);
        for(SessionListener listener : listeners)
        {
        	listener.onConnected(event);
        }    	
    }

    public void onDisconnected(String reason){
    	SessionEvent event = new SessionEvent(this,reason);
        for(SessionListener listener : listeners)
        {
        	listener.onDisconnected(event);
        }    	
    }

	public void onRegistered(){
		SessionEvent event = new SessionEvent(this);
		for(SessionListener listener : listeners)
		{
			listener.onRegistered(event);
		}
	}

    public void onAuthorized(){
    	SessionEvent event = new SessionEvent(this);
        for(SessionListener listener : listeners)
        {
        	listener.onAuthorized(event);
        }    	
    }
    
    public void onGetRosters(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onGetRosters(event);
        }
    }

    public void onPresence(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onPresence(event);
        }
    }

    public void onPresenceGroup(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onPresenceGroup(event);
        }
    }
    
    public void onUpdateAvatar(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onUpdateAvatar(event);
        }
    }

    public void onUpdateService(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onUpdateService(event);
        }
    }
    
    public void onUpdateGroup(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onUpdateGroup(event);
        }
    }

	public void onUpdateParticipant(String xml,SessionRequest request){
		SessionEvent event = new SessionEvent(this,xml,request);
		for(SessionListener listener : listeners)
		{
			listener.onUpdateParticipant(event);
		}
	}

    public void onUpdateServiceInfo(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onUpdateServiceInfo(event);
        }
    }

    public void onUpdateGroupInfo(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onUpdateGroupInfo(event);
        }
    }
    
    public void onSubscribe(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onSubscribe(event);
        }
    }

    public void onUnsubscribe(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onUnsubscribe(event);
        }
    }

    public void onAddRoster(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onAddRoster(event);
        }
    }

    public void onUpdateRoster(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onUpdateRoster(event);
        }
    }

    public void onDeleteRoster(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onDeleteRoster(event);
        }
    }
    
    public void onMessage(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onMessage(event);
        }
    }    
        
    public void onMessageGroup(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onMessageGroup(event);
        }
    }
    
    public void onMessageParticipant(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onMessageParticipant(event);
        }
    }    
    
    public void onMessageState(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onMessageState(event);
        }
    }
    
    public void onInvitation(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onInvitation(event);
        }
    }
    
    public void onGetAffiliation(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onGetAffiliation(event);
        }
    }

	public void onPing(){
		SessionEvent event = new SessionEvent(this);
		for(SessionListener listener : listeners)
		{
			listener.onPing(event);
		}
	}

	public void onTransferFile(String xml,SessionRequest request){
		SessionEvent event = new SessionEvent(this,xml,request);
		for(SessionListener listener : listeners)
		{
			listener.onTransferFile(event);
		}
	}

    public void onIQPacket(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onIQPacket(event);
        }
    }

    public void onMessagePacket(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onMessagePacket(event);
        }
    }
    
    public void onErrorResponse(String xml,SessionRequest request){
    	SessionEvent event = new SessionEvent(this,xml,request);
        for(SessionListener listener : listeners)
        {
        	listener.onErrorResponse(event);
        }
    }

	public void onSessionClose(String xml,SessionRequest request) {
		SessionEvent event = new SessionEvent(this, xml, request);
		for (SessionListener listener : listeners) {
			listener.onSessionClose(event);
		}
	}
}
