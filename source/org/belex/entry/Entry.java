package org.belex.entry;

import java.io.Serializable;
import java.util.Vector;

import org.belex.customer.Customer;
import org.belex.product.Product;
import org.belex.supplier.Supplier;
import org.belex.util.Util;

public class Entry implements Serializable {

	private static final long serialVersionUID = 1L;

	private String arrivalDate = "";
	private String supplierDocumentDescription;
	private int supplierDocumentType;

	private Integer supplierEntryProductIntegrity;
	private Integer supplierEntryPackagingIntegrity;
	private Integer supplierEntryDlcDdmValidity;
	private Integer supplierEntryTemperatureValidity;
	private String supplierEntryCommentOnQuality;
	private int supplierEntryNumberOfProducts;
	
	private Supplier supplier;

	private String date;
	private String time;
	
	private Product product;
	private int numberOfProduct;
	
	private String orderNumbers = "";
	private String orderLetter = "";
	
	private Vector<Customer> customers;

	public Entry() {
		setDate("");
		setTime("");
	}

	public int getNumberOfProduct() {
		return numberOfProduct;
	}
	public void setNumberOfProduct(int numberOfProduct) {
		this.numberOfProduct = numberOfProduct;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}

	public Vector<Customer> getCustomers() {
		if (customers == null) {
			customers = new Vector<Customer>();
		}
		return customers;
	}

	public boolean addCustomer(Customer customer) {
		if (customers == null) {
			customers = new Vector<Customer>();
		}
		boolean newCustomer = true;
		
		if (newCustomer){
			customers.add(customer);
		}
		return newCustomer;
	}

	public String getOrderNumbers() {
		return orderNumbers;
	}
	public void setOrderNumbers(String orderNumbers) {
		this.orderNumbers = orderNumbers;
	}
	public void addOrderNumbers(String orderNumbers) {
		if (this.orderNumbers != null && this.orderNumbers.trim().length() != 0) {

			String result = Util.containsAll(this.orderNumbers, orderNumbers, ",");
			if ( ! result.equals("")){
				this.orderNumbers += "," + result;
			}

		} else {
			this.orderNumbers = orderNumbers;
		}
		
	}
	
	public String getOrderLetter() {
		return orderLetter;
	}
	public void setOrderLetter(String orderLetter) {
		this.orderLetter = orderLetter;
	}
	public void addOrderLetter(String orderLetter) {
		if (this.orderLetter != null && this.orderLetter.trim().length() != 0) {
			String result = Util.containsAll(this.orderLetter, orderLetter, ",");
			if ( ! result.equals("")){
				this.orderLetter += "," + result;
			}
		} else {
			this.orderLetter = orderLetter;
		}
		
	}
	public String getArrivalDate() {
		return arrivalDate;
	}
	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public void setCustomers(Vector<Customer> customers) {
		if (customers != null) {
			this.customers = customers;
		}
		
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public String getSupplierDocumentDescription() {
		return supplierDocumentDescription;
	}

	public void setSupplierDocumentDescription(String supplierDocumentDescription) {
		this.supplierDocumentDescription = supplierDocumentDescription;
	}

	public int getSupplierDocumentType() {
		return supplierDocumentType;
	}

	public void setSupplierDocumentType(int supplierDocumentType) {
		this.supplierDocumentType = supplierDocumentType;
	}

	public int getSupplierEntryProductIntegrityPrimitive() {
		if (supplierEntryProductIntegrity != null) {
			return supplierEntryProductIntegrity.intValue();
		}
		return 0;
	}

	public void setSupplierEntryProductIntegrity(Integer supplierEntryProductIntegrity) {
		this.supplierEntryProductIntegrity = supplierEntryProductIntegrity;
	}

	public Integer getSupplierEntryPackagingIntegrityPrimitive() {
		if (supplierEntryProductIntegrity != null) {
			return supplierEntryPackagingIntegrity.intValue();
		}
		return 0;
	}

	public void setSupplierEntryPackagingIntegrity(Integer supplierEntryPackagingIntegrity) {
		this.supplierEntryPackagingIntegrity = supplierEntryPackagingIntegrity;
	}

	public Integer getSupplierEntryDlcDdmValidityPrimitive() {
		if (supplierEntryDlcDdmValidity != null) {
			return supplierEntryDlcDdmValidity.intValue();
		}
		return 0;
	}

	public void setSupplierEntryDlcDdmValidity(Integer supplierEntryDlcDdmValidity) {
		this.supplierEntryDlcDdmValidity = supplierEntryDlcDdmValidity;
	}

	public Integer getSupplierEntryTemperatureValidityPrimitive() {
		if (supplierEntryTemperatureValidity != null) {
			return supplierEntryTemperatureValidity.intValue();
		}
		return 0;
	}

	public void setSupplierEntryTemperatureValidity(Integer supplierEntryTemperatureValidity) {
		this.supplierEntryTemperatureValidity = supplierEntryTemperatureValidity;
	}

	public String getSupplierEntryCommentOnQuality() {
		return supplierEntryCommentOnQuality;
	}

	public void setSupplierEntryCommentOnQuality(String supplierEntryCommentOnQuality) {
		this.supplierEntryCommentOnQuality = supplierEntryCommentOnQuality;
	}

	public int getSupplierEntryNumberOfProducts() {
		return supplierEntryNumberOfProducts;
	}

	public void setSupplierEntryNumberOfProducts(int supplierEntryNumberOfProducts) {
		this.supplierEntryNumberOfProducts = supplierEntryNumberOfProducts;
	}
}