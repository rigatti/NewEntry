package org.belex.export;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Export implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// list of all suppliers for the current date
	private List<ProductToExport> products;
	
	// list of all customers to export
	private List<CustomerToExport> customers;
	
	private String errorMessage;
	
	public Export() {
		init();
	}

	public void init(){
		products = new ArrayList<ProductToExport>();
		customers = new ArrayList<CustomerToExport>();
		errorMessage = "";
	} 
	
	public List<ProductToExport> getProducts() {
		return products;
	}

	public void setProducts(List<ProductToExport> products) {
		this.products = products;
	}
	
	public void addProduct(ProductToExport product) {
		if (products == null) {
			products = new ArrayList<ProductToExport>();
		}
		products.add(product);
	}

	public List<CustomerToExport> getCustomers() {
		return customers;
	}

	public void setCustomers(List<CustomerToExport> customers) {
		this.customers = customers;
	}
	
	public void addCustomer(CustomerToExport customer) {
		if (customers == null) {
			customers = new ArrayList<CustomerToExport>();
		}
		customers.add(customer);
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
