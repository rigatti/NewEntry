package db.customer;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
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
}