package com.os4.ecb.beans;

import java.util.ArrayList;
import java.util.List;

public class AffiliationItems {

	private List<Item> items = new ArrayList<Item>();
	
	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public void addItem(String affiliation) {
		Item item = new Item(affiliation);
		items.add(item);
	}
	
	public void addItem(String jid,String name) {
		Item item = new Item(jid,name);
		items.add(item);
	}

	public void addItem(Item item) {
		items.add(item);
	}
	
	public Item newItem(){
		return new Item();
	}
	
	public class Item {

		private String jid;
		private String name;
		private String nick;
		private String affiliation;
		private String role;

		public Item(){}
		public Item(String affiliation ){
			this.affiliation = affiliation;
		}
		public Item(String jid,String name){
			this.jid = jid;
			this.name = name;
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
		public String getNick() {
			return nick;
		}
		public void setNick(String nick) {
			this.nick = nick;
		}
		public String getAffiliation() {
			return affiliation;
		}
		public void setAffiliation(String affiliation) {
			this.affiliation = affiliation;
		}
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}		
	}
}
