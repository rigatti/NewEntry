package db.customer;

import java.io.Serializable;

public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;
	private String name;
	private String nameAdditionalPart;
	private String groupment;
	private String address;
	private String addressFees;
    private String addressFurniture;
    private String airportCode;
	private String buyRate;
    private String email;
    private String codesTypesOfEditions;
    
    public Customer() {
    }
    public Customer(String customerCode) {
    	this.code = customerCode;
    }
	public String getAddressFurniture() {
		return addressFurniture;
	}
	public void setAddressFurniture(String addressFurniture) {
		this.addressFurniture = addressFurniture;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddressFees() {
		return addressFees;
	}
	public void setAddressFees(String addressFees) {
		this.addressFees = addressFees;
	}
	public String getAirportCode() {
		return airportCode;
	}
	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}
	public String getGroupment() {
		return groupment;
	}
	public void setGroupment(String groupment) {
		this.groupment = groupment;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCodesTypesOfEditions() {
		return codesTypesOfEditions;
	}
	public void setCodesTypesOfEditions(String codesTypesOfEditions) {
		this.codesTypesOfEditions = codesTypesOfEditions;
	}
	public String getBuyRate() {
		return buyRate;
	}
	public void setBuyRate(String buyRate) {
		this.buyRate = buyRate;
	}
}