package com.os4.ecb.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RosterItems {

	private List<Item> items = new ArrayList<Item>();
	
	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public void addItem(Item item){
		items.add(item);
	}
	public void addItem(String jid,String name,String subscription) {
		Item item = new Item(jid,name,subscription);
		items.add(item);
	}
	public void addItem(String jid,String name,String ask,String subscription) {
		Item item = new Item(jid,name,ask,subscription);
		items.add(item);
	}
	public Item getItem(String jid){
		for(Item item : items){
			if(item.getJid().equalsIgnoreCase(jid)) return item;
		}
		return null;
	}
	public void updateItem(Item itemIn){
		for(Item item : items){
			if(item.getJid().equalsIgnoreCase(itemIn.getJid())){
				items.remove(item);
				item.setSubscription(itemIn.getSubscription());
				items.add(item);
			}
		}
	}
	public void deleteItem(){items.clear();}
	public void deleteItem(String jid){
		for(Item item : items){
			if(item.getJid().equalsIgnoreCase(jid)) items.remove(item);
		}
	}
	public boolean isPhotoUpdate(String jid, String photo){
		Item item = getItem(jid);
		if(item!=null && item.getPhoto()!=null && item.getPhoto().equalsIgnoreCase(photo)) return false;
		else return true;
	}

	public void setPhoto(String jid, String photo){
		Item item = getItem(jid);
		item.setPhoto(photo);
	}

	public void setPhotoBinVal(String jid, String photoType,String photoBinVal){
		Item item = getItem(jid);
		if(item!=null){
			item.setPhotoType(photoType);
			item.setPhotoBinVal(photoBinVal);
		}
	}

	public void setNickname(String jid, String nickname){
		Item item = getItem(jid);
		if(item!=null){
			item.setName(nickname);
		}
	}

	public void setCounter(String jid){
		Item item = getItem(jid);
		if(item!=null) {
			int unread = item.getUnread() + 1;
			item.setUnread(unread);
		}
	}

	public void resetCounter(String jid){
		Item item = getItem(jid);
		if(item!=null) {
			item.setUnread(0);
		}
	}

	public void setPresence(String jid, String resource, String show, String status){
		Item item = getItem(jid);
		if(item!=null){
			if(resource!=null) item.setResource(resource);
			if(status!=null) item.setStatus(status);
			if(show!=null && item.getShow()==null) item.setDateTime(new Date());
			if(show==null && item.getShow()!=null) item.setDateTime(new Date());
			item.setShow(show);
		}
	}

	public Item newItem(){
		return new Item();
	}
	
	public class Item {

		private String jid;
		private String name;
		private String ask;
		private String subscription;

		private String resource;
		private String show;
		private String status;

		private String photo;
		private String hash;
		private String photoType;
		private String photoBinVal;

		private Date dateTime;
		private int unread = 0;

		public Item(){}
		public Item(String jid,String name,String subscription){
			this.jid = jid;
			this.name = name;
			this.subscription = subscription;
		}
		public Item(String jid,String name,String ask,String subscription){
			this.jid = jid;
			this.name = name;
			this.ask = ask;
			this.subscription = subscription;
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
		public String getAsk() {
			return ask;
		}
		public void setAsk(String ask) {
			this.ask = ask;
		}
		public String getSubscription() {
			return subscription;
		}
		public void setSubscription(String subscription) {
			this.subscription = subscription;
		}
		public String getResource() {
			return resource;
		}
		public void setResource(String resource) {
			this.resource = resource;
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
		public Date getDateTime() {
			return dateTime;
		}
		public void setDateTime(Date dateTime) {
			this.dateTime = dateTime;
		}
		public int getUnread() {
			return unread;
		}
		public void setUnread(int unread) {
			this.unread = unread;
		}
	}
}
