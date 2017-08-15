package com.os4.ecb.adapter;

import java.io.IOException;
import java.io.StringReader;
import java.util.Random;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.os4.ecb.beans.DiscoInfo;
import com.os4.ecb.beans.DiscoItems;
import com.os4.ecb.beans.AffiliationItems;
import com.os4.ecb.beans.FileTransferInfo;
import com.os4.ecb.beans.RosterItems;
import com.os4.ecb.beans.RosterItems.Item;
import com.os4.ecb.session.SessionRequest;
import com.os4.ecb.session.SessionUser;

public class Connector extends Thread {

	protected String generateId;
	protected int seqNo = 0;
	protected String ipAddress;
	protected int port;	
	protected String domain;
	protected SessionUser sessionUser;
	protected XMPPBuilder xmppBuilder;

	public Connector(String domain,String ipAddress,int port) {
		this.domain = domain;
		this.ipAddress = ipAddress;
		this.port = port;
		this.generateId = generateId();
		this.xmppBuilder = new XMPPBuilder(domain);		
	}

	public void sleep(int sleep){
		try{
			Thread.sleep(sleep);
		}catch(Exception e){}    	
    }

	public String generateId() {
		return generateId(4);
	}

	public String generateId(int n){
        final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        final int N = alphabet.length();
        Random r = new Random();            
        StringBuilder str = new StringBuilder();
        str = str.append(String.valueOf((char)(r.nextInt(26) + 'A')).toUpperCase());            
        for(int i=0;i<n;i++) str = str.append(alphabet.charAt(r.nextInt(N)));
        return str.toString();
    }
	
    public String nextId(){
    	String id = generateId;
        StringBuilder str = new StringBuilder(id);
        str = str.append("-").append(Integer.toString(seqNo++));
        return str.toString();
    }
	
    protected Document getDocument(String xml) throws Exception {
        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = docFactory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            return document;
        }catch(ParserConfigurationException | SAXException | IOException e){
            throw new Exception();
        }
    }
    
    protected SessionRequest getSessionBean(SessionRequest request, Element rootElement) throws Exception {
    	
        String tag = rootElement.getTagName();
        request.setTag(tag);
        String id = rootElement.getAttribute("id");
        request.setId(id);
        String type = rootElement.getAttribute("type");
        request.setType(type);
        String ip = rootElement.getAttribute("ip");
        request.setIp(ip);
        String sessionId = rootElement.getAttribute("session");
        request.setSessionId(sessionId);
        
        String from = rootElement.getAttribute("from");
        if(from!=null && from.trim().length()>0){
        	request.setFrom(from);
        	request.setJidFrom(from);
        	if(from.contains("@")){
        		String username = from.substring(0,from.indexOf("@"));
        		request.setUserFrom(username);
        		String source = from.substring(from.indexOf("@")+1);
        		if(from.contains("/")){
        			String jidFrom = from.substring(0,from.indexOf("/"));
        			request.setJidFrom(jidFrom);
        			String resource = source.substring(source.indexOf("/")+1);
        			request.setResourceFrom(resource);
        			String service = source.substring(0,source.indexOf("/"));
        			String[] parts = service.split(Pattern.quote("."));
        			if(parts.length>2){
        				request.setRoomFrom(username);
		        		String subDomain = parts[0];
		        		request.setSubDomainFrom(subDomain);
		        		String domainFrom = parts[1]+"."+parts[2];
		        		request.setDomainFrom(domainFrom);
        			}else{
        				String domainFrom = service;
        				request.setDomainFrom(domainFrom);
        			}
        		}
        		else{
        			String[] parts = source.split(Pattern.quote("."));
        			if(parts.length>2){
        				request.setRoomFrom(username);
		        		String subDomainFrom = parts[0];
		        		request.setSubDomainFrom(subDomainFrom);
		        		String domainFrom = parts[1]+"."+parts[2];
		        		request.setDomainFrom(domainFrom);
        			}else{
        				String domainFrom = source;
        				request.setDomainFrom(domainFrom);
        			}        			
        		}
        	}
        	else{
    			String[] parts = from.split(Pattern.quote("."));
    			if(parts.length>2){
	        		String subDomainFrom = parts[0];
	        		request.setSubDomainFrom(subDomainFrom);
	        		String domainFrom = parts[1]+"."+parts[2];
	        		request.setDomainFrom(domainFrom);
    			}else{
    				String domainFrom = from;
    				request.setDomainFrom(domainFrom);
    			}        		
        	}
        }

        String to = rootElement.getAttribute("to");
        if(to!=null){
        	request.setTo(to);
        	request.setJidTo(to);
	        if(to.contains("@")){
	        	String userTo = to.substring(0,to.indexOf("@"));
	        	request.setUserTo(userTo);
	        	String dest = to.substring(to.indexOf("@")+1);
	        	if(to.contains("/")){
	        		String jidTo = to.substring(0, to.indexOf("/"));
	        		request.setJidTo(jidTo);
        			String resourceTo = dest.substring(dest.indexOf("/")+1);
        			request.setResourceTo(resourceTo);
        			String service = dest.substring(0,dest.indexOf("/"));	        		
		        	String[] parts = service.split(Pattern.quote("."));
		        	if(parts.length>2){
		        		request.setRoomTo(userTo);
		        		String subDomainTo = parts[0];
		        		request.setSubDomainTo(subDomainTo);
		        		String domainTo = parts[1]+"."+parts[2];
		        		request.setDomainTo(domainTo);
		        	}else{
		        		String domainTo = service;
		        		request.setDomainTo(domainTo);
		        	}
	        	}else{
		        	String[] parts = dest.split(Pattern.quote("."));
		        	if(parts.length>2){
		        		request.setRoomTo(userTo);
		        		String subDomainTo = parts[0];
		        		request.setSubDomainTo(subDomainTo);
		        		String domainTo = parts[1]+"."+parts[2];
		        		request.setDomainTo(domainTo);
		        	}else{
		        		String domainTo = dest;
		        		request.setDomainTo(domainTo);
		        	}	        		
	        	}
	        }else{
	        	String[] parts = to.split(Pattern.quote("."));
	        	if(parts.length>2){	        		
	        		String subDomainTo = parts[0];
	        		request.setSubDomainTo(subDomainTo);
	        		String domainTo = parts[1]+"."+parts[2];
	        		request.setDomainTo(domainTo);
	        	}else{
	        		String domainTo = to;
	        		request.setDomainTo(domainTo);
	        	}
	        }
        }

        if(tag.contains("iq") && rootElement.hasChildNodes()){
        	for(int i=0;i<rootElement.getChildNodes().getLength();i++){
        		Node node = rootElement.getChildNodes().item(i);
        		if(node.getNodeName().equalsIgnoreCase("query")){
        			if(node.getAttributes().getNamedItem("xmlns")!=null){
                        String xmlns = node.getAttributes().getNamedItem("xmlns").getNodeValue();
                        request.setXmlns(xmlns);
                        if(node.hasChildNodes() && xmlns.equalsIgnoreCase("jabber:iq:roster")){
                        	RosterItems rosters = new RosterItems();
	                        for(int j=0;j<node.getChildNodes().getLength();j++){
	                        	Node nodeChild = node.getChildNodes().item(j);
	                        	request.setMsgTag(nodeChild.getNodeName());
	                        	if(nodeChild.getNodeName().equalsIgnoreCase("item")){
	                        		Item item = rosters.newItem();
	                        		if(nodeChild.getAttributes().getNamedItem("jid")!=null){
	                        			String jid = nodeChild.getAttributes().getNamedItem("jid").getNodeValue();
	                        			item.setJid(jid);
	                        		}
	                        		if(nodeChild.getAttributes().getNamedItem("name")!=null){
	                        			String name = nodeChild.getAttributes().getNamedItem("name").getNodeValue();
	                        			item.setName(name);
	                        		}
	                        		if(nodeChild.getAttributes().getNamedItem("subscription")!=null){
	                        			String subscription = nodeChild.getAttributes().getNamedItem("subscription").getNodeValue();
	                        			item.setSubscription(subscription);
	                        		}
	                        		if(nodeChild.getAttributes().getNamedItem("ask")!=null){
	                        			String ask = nodeChild.getAttributes().getNamedItem("ask").getNodeValue();
	                        			item.setAsk(ask);
	                        		}
                        			rosters.addItem(item);
	                        	}
	                        }
	                        request.setRosters(rosters);
                        }
                        else if(node.hasChildNodes() && xmlns.equalsIgnoreCase("http://jabber.org/protocol/disco#items") && type.equalsIgnoreCase("result")){
                        	DiscoItems items = new DiscoItems();
	                        for(int j=0;j<node.getChildNodes().getLength();j++){
	                        	Node nodeChild = node.getChildNodes().item(j);
	                        	request.setMsgTag(nodeChild.getNodeName());
	                        	if(nodeChild.getNodeName().equalsIgnoreCase("item")){
	                        		DiscoItems.Item item = items.newItem();
	                        		if(nodeChild.getAttributes().getNamedItem("jid")!=null){
	                        			String jid = nodeChild.getAttributes().getNamedItem("jid").getNodeValue();
	                        			item.setJid(jid);
	                        		}
	                        		if(nodeChild.getAttributes().getNamedItem("name")!=null){
	                        			String name = nodeChild.getAttributes().getNamedItem("name").getNodeValue();
	                        			item.setName(name);
	                        		}
									if(!item.getJid().equalsIgnoreCase("pubsub."+domain)
											&& !item.getJid().equalsIgnoreCase("search."+domain)
											&& !item.getJid().equalsIgnoreCase("proxy."+domain))
	                        		items.addItem(item);
	                        	}
	                        }
	                        request.setItems(items);	                        
                        }
                        else if(node.hasChildNodes() && xmlns.equalsIgnoreCase("http://jabber.org/protocol/disco#info") && type.equalsIgnoreCase("result")){
                        	DiscoInfo discoInfo = new DiscoInfo();
	                        for(int j=0;j<node.getChildNodes().getLength();j++){
	                        	Node nodeChild = node.getChildNodes().item(j);
	                        	request.setMsgTag(nodeChild.getNodeName());
	                        	if(nodeChild.getNodeName().equalsIgnoreCase("identity")){
	                        		String category = nodeChild.getAttributes().getNamedItem("category").getNodeValue();
	                        		String name = nodeChild.getAttributes().getNamedItem("name").getNodeValue();
	                        		String infoType = nodeChild.getAttributes().getNamedItem("type").getNodeValue();
	                        		discoInfo.addIdentity(category,name,infoType);
	                        	}
	                        	if(nodeChild.getNodeName().equalsIgnoreCase("feature")){
	                        		String feature = nodeChild.getAttributes().getNamedItem("var").getNodeValue();
	                        		discoInfo.addFeature(feature);
	                        	}
								if(nodeChild.getNodeName().equalsIgnoreCase("x")){
									if(nodeChild.hasChildNodes()){
										for(int k=0;k<nodeChild.getChildNodes().getLength();k++){
											Node xnode = nodeChild.getChildNodes().item(k);
											String fieldVar = xnode.getAttributes().getNamedItem("var").getNodeValue();
											if(fieldVar.equalsIgnoreCase("muc#roominfo_description")){
												String value = xnode.getLastChild().getTextContent();
												discoInfo.setDescription(value);
											}
											if(fieldVar.equalsIgnoreCase("muc#roominfo_subject")){
												String value = xnode.getLastChild().getTextContent();
												discoInfo.setSubject(value);
											}
											if(fieldVar.equalsIgnoreCase("muc#roominfo_occupants")){
												String value = xnode.getLastChild().getTextContent();
												discoInfo.setNumberOfOccupant(Integer.parseInt(value));
											}
											if(fieldVar.equalsIgnoreCase("x-muc#roominfo_creationdate")){
												String value = xnode.getLastChild().getTextContent();
												discoInfo.setCreationDate(value);
											}
										}
									}
								}
	                        }
	                        request.setDiscoInfo(discoInfo);	                        
                        }
                        else if(node.hasChildNodes() && xmlns.equalsIgnoreCase("http://jabber.org/protocol/muc#admin") && type.equalsIgnoreCase("result")){
                        	AffiliationItems items = new AffiliationItems();                        	
	                        for(int j=0;j<node.getChildNodes().getLength();j++){
	                        	Node nodeChild = node.getChildNodes().item(j);
	                        	request.setMsgTag(nodeChild.getNodeName());
	                        	AffiliationItems.Item item = items.newItem();
	                        	if(nodeChild.getNodeName().equalsIgnoreCase("item")){
	                        		String affiliation = nodeChild.getAttributes().getNamedItem("affiliation").getNodeValue();
	                        		String jid = nodeChild.getAttributes().getNamedItem("jid").getNodeValue();
	                        		item.setAffiliation(affiliation);
	                        		item.setJid(jid);
	                        		items.addItem(item);
	                        	}
	                        }
	                        request.setAffiliations(items);	                        
                        }
        			}
        		}
				else if(node.getNodeName().equalsIgnoreCase("vCard")){
					if(node.getAttributes().getNamedItem("xmlns")!=null) {
						String xmlns = node.getAttributes().getNamedItem("xmlns").getNodeValue();
						request.setXmlns(xmlns);
						if(node.hasChildNodes() && xmlns.equalsIgnoreCase("vcard-temp")) {
							for (int j = 0; j < node.getChildNodes().getLength(); j++) {
								Node nodeChild = node.getChildNodes().item(j);
								request.setMsgTag(nodeChild.getNodeName());
								if(nodeChild.getNodeName().equalsIgnoreCase("PHOTO")) {
									for(int k=0;k<nodeChild.getChildNodes().getLength();k++){
										Node nodePhotoChild = nodeChild.getChildNodes().item(k);
										if(nodePhotoChild.getNodeName().equalsIgnoreCase("TYPE")){
											request.setPhotoType(nodePhotoChild.getTextContent());
										}
										else if(nodePhotoChild.getNodeName().equalsIgnoreCase("BINVAL")){
											request.setPhotoBinVal(nodePhotoChild.getTextContent());
										}
									}
								}
								else if(nodeChild.getNodeName().equalsIgnoreCase("NICKNAME")) {
									request.setNickname(nodeChild.getTextContent());
								}
							}
						}
					}
				}
				else if(node.getNodeName().equalsIgnoreCase("si")){
					FileTransferInfo fileTransferInfo = new FileTransferInfo();
					fileTransferInfo.setTag(node.getNodeName());
					if(node.getAttributes().getNamedItem("xmlns")!=null){
						String xmlns = node.getAttributes().getNamedItem("xmlns").getNodeValue();
						request.setXmlns(xmlns);
						if(node.hasChildNodes() && xmlns.equalsIgnoreCase("http://jabber.org/protocol/si")) {
							for (int j = 0; j < node.getChildNodes().getLength(); j++) {
								Node nodeChild = node.getChildNodes().item(j);
								request.setMsgTag(nodeChild.getNodeName());
								if(nodeChild.getNodeName().equalsIgnoreCase("file")) {
									if(nodeChild.getAttributes().getNamedItem("name")!=null) {
										String name = nodeChild.getAttributes().getNamedItem("name").getNodeValue();
										fileTransferInfo.setName(name);
									}
									if(nodeChild.getAttributes().getNamedItem("size")!=null) {
										String size = nodeChild.getAttributes().getNamedItem("size").getNodeValue();
										fileTransferInfo.setSize(Integer.parseInt(size));
									}
									if(nodeChild.getAttributes().getNamedItem("xmlns")!=null) {
										String sixmlns = nodeChild.getAttributes().getNamedItem("xmlns").getNodeValue();
										fileTransferInfo.setXmlns(sixmlns);
									}
								}
							}
						}
					}
					if(node.getAttributes().getNamedItem("id")!=null){
						String id_profile = node.getAttributes().getNamedItem("id").getNodeValue();
						fileTransferInfo.setId(id_profile);
					}
					if(node.getAttributes().getNamedItem("mime-type")!=null){
						String mimeType = node.getAttributes().getNamedItem("mime-type").getNodeValue();
						fileTransferInfo.setMimeType(mimeType);
					}
					if(node.getAttributes().getNamedItem("profile")!=null){
						String profile = node.getAttributes().getNamedItem("profile").getNodeValue();
						fileTransferInfo.setProfile(profile);
					}
					request.setFileTransferInfo(fileTransferInfo);
				}
				else if(node.getNodeName().equalsIgnoreCase("open") || node.getNodeName().equalsIgnoreCase("data") || node.getNodeName().equalsIgnoreCase("close")) {
					FileTransferInfo fileTransferInfo = new FileTransferInfo();
					fileTransferInfo.setTag(node.getNodeName());
					if (node.getAttributes().getNamedItem("xmlns") != null) {
						String xmlns = node.getAttributes().getNamedItem("xmlns").getNodeValue();
						request.setXmlns(xmlns);
						if (node.hasChildNodes() && xmlns.equalsIgnoreCase("http://jabber.org/protocol/ibb")) {
						}
					}
					if(node.getAttributes().getNamedItem("sid")!=null){
						String sid = node.getAttributes().getNamedItem("sid").getNodeValue();
						fileTransferInfo.setSid(sid);
					}
					if(node.getAttributes().getNamedItem("block-size")!=null){
						String blockSize = node.getAttributes().getNamedItem("block-size").getNodeValue();
						fileTransferInfo.setBlockSize(Integer.parseInt(blockSize));
					}
					if(node.getAttributes().getNamedItem("seq")!=null){
						String seq = node.getAttributes().getNamedItem("seq").getNodeValue();
						fileTransferInfo.setSeq(Integer.parseInt(seq));
						String base64 = node.getTextContent();
						fileTransferInfo.setBase64(base64);
					}
					request.setFileTransferInfo(fileTransferInfo);
				}
				else if(node.getNodeName().equalsIgnoreCase("error")){
        			if(node.getAttributes().getNamedItem("code")!=null){
                        String errorCode = node.getAttributes().getNamedItem("code").getNodeValue();
                        request.setErrorCode(errorCode);
                        String errorMessage = node.getLastChild().getNodeName();
                        request.setErrorMessage(errorMessage);
        			}
        		}
        		else{
        			if(node.getAttributes().getNamedItem("xmlns")!=null){
                        String xmlns = node.getAttributes().getNamedItem("xmlns").getNodeValue();
                        request.setXmlns(xmlns);
        			}
        		}
        	}
        }
		else if(tag.contains("presence")){
			if(type!=null && type.equalsIgnoreCase("subscribe") || type.equalsIgnoreCase("unsubscribe")
					|| type.equalsIgnoreCase("subscribed") || type.equalsIgnoreCase("unsubscribed")) {
				/* Nothing */
			}else if(type!=null && type.equalsIgnoreCase("error")) {
				if(rootElement.hasChildNodes()) {
					for (int i = 0; i < rootElement.getChildNodes().getLength(); i++) {
						Node node = rootElement.getChildNodes().item(i);
						if (node.getNodeName().equalsIgnoreCase("error")) {
							if (node.getAttributes().getNamedItem("code") != null) {
								String errorCode = node.getAttributes().getNamedItem("code").getNodeValue();
								request.setErrorCode(errorCode);
								String errorMessage = node.getLastChild().getNodeName();
								request.setErrorMessage(errorMessage);
							}
						}
					}
				}
			}else if(type!=null && type.equalsIgnoreCase("unavailable")){
				request.setShow("unavailable");
        	}else {
				if(rootElement.hasChildNodes()) {
					for (int i = 0; i < rootElement.getChildNodes().getLength(); i++) {
						Node node = rootElement.getChildNodes().item(i);
						if (node.getNodeName().equalsIgnoreCase("show")) {
							request.setShow(node.getTextContent());
						}
						if (node.getNodeName().equalsIgnoreCase("status")) {
							request.setStatus(node.getTextContent());
							if (request.getShow() == null || request.getShow().length() == 0)
								request.setShow("available");
						}
						if (node.getNodeName().equalsIgnoreCase("priority")) {
							request.setPriority(node.getTextContent());
						}
						if (node.getNodeName().equalsIgnoreCase("x")) {
							String xmlns = node.getAttributes().getNamedItem("xmlns").getNodeValue();
							request.setXmlns(xmlns);
							Node child = node.getFirstChild();
							if (child.getNodeName().equalsIgnoreCase("photo"))
								request.setPhoto(child.getTextContent());
							if (child.getNodeName().equalsIgnoreCase("hash"))
								request.setHash(child.getTextContent());
							if (child.getNodeName().equalsIgnoreCase("item")){
								String jid = child.getAttributes().getNamedItem("jid").getNodeValue();
								String affiliation = child.getAttributes().getNamedItem("affiliation").getNodeValue();
								String role = child.getAttributes().getNamedItem("role").getNodeValue();
								AffiliationItems items = new AffiliationItems();
								AffiliationItems.Item item = items.newItem();
								item.setJid(jid);
								item.setAffiliation(affiliation);
								item.setRole(role);
								items.addItem(item);
								request.setAffiliations(items);
							}
						}
					}
				}
				else{
					request.setShow("available");
					request.setStatus("Online");
				}
        	}
        }
        else if(tag.contains("message") && rootElement.hasChildNodes()){
        	for(int i=0;i<rootElement.getChildNodes().getLength();i++){
        		Node node = rootElement.getChildNodes().item(i);
        		if(node.getNodeName().equalsIgnoreCase("x")){
        			if(node.getAttributes().getNamedItem("xmlns")!=null){
                        String xmlns = node.getAttributes().getNamedItem("xmlns").getNodeValue();
                        if(xmlns.equalsIgnoreCase("http://jabber.org/protocol/muc#user")){
		                    request.setXmlns(xmlns);
		                    if(node.hasChildNodes()){
		                        for(int j=0;j<node.getChildNodes().getLength();j++){
		                        	Node nodeChild = node.getChildNodes().item(j);
		                        	request.setMsgTag(nodeChild.getNodeName());
		                        	if(nodeChild.getNodeName().equalsIgnoreCase("invite")){
		                        		if(nodeChild.getAttributes().getNamedItem("to")!=null){
			                        		String inviteTo = nodeChild.getAttributes().getNamedItem("to").getNodeValue();
			                        		String reason = nodeChild.getLastChild().getTextContent();
			                        		request.setInviteTo(inviteTo);
			                        		request.setReason(reason);
		                        		}
		                        		if(nodeChild.getAttributes().getNamedItem("from")!=null){
			                        		String inviteFrom = nodeChild.getAttributes().getNamedItem("from").getNodeValue();
			                        		String reason = nodeChild.getLastChild().getTextContent();
			                        		request.setInviteFrom(inviteFrom);
			                        		request.setReason(reason);
		                        		}                        		
		                        	}
		                        	else if(nodeChild.getNodeName().equalsIgnoreCase("password")){
		                        		String password = nodeChild.getTextContent();
		                        		request.setPassword(password);
		                        	}
		                        }
		                    }
		    			}
                        else if(xmlns.equalsIgnoreCase("jabber:x:conference")){
                        	
                        }
        			}
        		}
				else if(node.getNodeName().equalsIgnoreCase("subject")){
					String subject = node.getTextContent();
					request.setSubject(subject);
				}
        		else if(node.getNodeName().equalsIgnoreCase("body")){
        			String msg = node.getTextContent();
        			request.setMsgBody(msg);
        		}
                else if(node.getAttributes().getNamedItem("xmlns")!=null){
            		String xmlns = node.getAttributes().getNamedItem("xmlns").getNodeValue();
            		request.setXmlns(xmlns);
                	String msgTag = node.getNodeName();
                	request.setMsgTag(msgTag);            		
                }        		
        	}
        }
        return request;
    }    
}
