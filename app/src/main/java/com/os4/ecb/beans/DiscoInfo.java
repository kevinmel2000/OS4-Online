package com.os4.ecb.beans;

import java.util.ArrayList;
import java.util.List;

public class DiscoInfo {

	private List<Identity> identities = new ArrayList<Identity>();
	private List<String> features = new ArrayList<String>();
	public List<Identity> getIdentities() {
		return identities;
	}

	public void setIdentities(List<Identity> identities) {
		this.identities = identities;
	}

	public void addIdentity(String jid,String name,String type){
		
		Identity identity = new Identity(jid,name,type);
		this.identities.add(identity);
	}

	public List<String> getFeatures() {
		return features;
	}

	public void setFeatures(List<String> features) {
		this.features = features;
	}

	public void addFeature(String feature){
		this.features.add(feature);
	}
	
	public class Identity {
		
		private String category;
		private String name;
		private String type;
		
		public Identity(String category,String name,String type){
			this.category = category;
			this.name = name;
			this.type = type;
		}		
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}		
	}

	private String description;
	private String subject;
	private int numberOfOccupant;
	private String creationDate;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getNumberOfOccupant() {
		return numberOfOccupant;
	}

	public void setNumberOfOccupant(int numberOfOccupant) {
		this.numberOfOccupant = numberOfOccupant;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
}
