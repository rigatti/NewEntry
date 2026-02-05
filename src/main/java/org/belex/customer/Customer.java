package org.belex.customer;

import java.io.Serializable;

public class Customer extends db.customer.Customer implements Serializable {

	private static final long serialVersionUID = 1L;

	boolean stockCustomer = false;
	boolean supplierReturnsCustomer = false;

	private String allocationDate = "";
	
	private CustomerOrder customerOrder;

	// customer specification for one entry 
	// 	(nbr of the same product, conditionnement, ... 
	//	from CommandesLignes table)  
	private CustomerEntry customerEntry;

	public Customer() {
		super();
	}
	
	public Customer(String code) {
		super(code);
	}
	public CustomerEntry getCustomerEntry() {
		return customerEntry;
	}

	public void setCustomerEntry(CustomerEntry customerEntry) {
		this.customerEntry = customerEntry;
	}

	public CustomerOrder getCustomerOrder() {
		if (customerOrder == null) {
			customerOrder = new CustomerOrder();
		}
		return customerOrder;
	}

	public void setCustomerOrder(CustomerOrder customerOrder) {
		this.customerOrder = customerOrder;
	}

	public boolean isStockCustomer() {
		return stockCustomer;
	}

	public void setStockCustomer(boolean stockCustomer) {
		this.stockCustomer = stockCustomer;
	}

	public boolean isSupplierReturnsCustomer() {
		return supplierReturnsCustomer;
	}

	public void setSupplierReturnsCustomer(boolean supplierReturnsCustomer) {
		this.supplierReturnsCustomer = supplierReturnsCustomer;
	}

	public String getAllocationDate() {
		return allocationDate;
	}

	public void setAllocationDate(String allocationDate) {
		this.allocationDate = allocationDate;
	}
}