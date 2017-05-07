package org.belex.export;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class CustomerToExport {
	private String name;
	private String nameAdditionalPart;
	private String email;
	private String groupsCodes;
	private List<String> groups;
	private float buyRate;
	
	public CustomerToExport() {
		init();
	}
	private void init() {
		this.name = "";
		this.nameAdditionalPart = "";
		this.email = "";
		this.groups= new ArrayList<String>(); 
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameAdditionalPart() {
		return nameAdditionalPart;
	}
	public void setNameAdditionalPart(String nameAdditionalPart) {
		this.nameAdditionalPart = nameAdditionalPart;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<String> getGroups() {
		return groups;
	}
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}
	public void addGroup(String group) {
		if (StringUtils.isNotBlank(group)) {
			this.groups.add(group);
		}
	}
	public float getBuyRate() {
		return buyRate;
	}
	public void setBuyRate(float buyRate) {
		this.buyRate = buyRate;
	}
	public String getGroupsCodes() {
		return groupsCodes;
	}
	public void setGroupsCodes(String groupsCodes) {
		this.groupsCodes = groupsCodes;
	}
	
}
