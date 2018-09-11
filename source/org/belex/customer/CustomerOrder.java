package org.belex.customer;

import java.io.Serializable;

import org.belex.fly.Fly;

public class CustomerOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	private int 	supplierOrderNumber;
	private String 	supplierOrderLetter;

	private Fly fly = new Fly();
	
	// data from customerOrderDetail
	private String 	orderCode;
	private int 	orderNumber;
	private int 	feesType;
	private String 	productCode;
	private String 	productDescription;
	private int	 	priority;
	private int 	creationNumber;
	private int 	numberOfUnit;
	private String 	unit;
	private int 	numberOfProduct;
	private int 	totalNumberOfUnit;
	private String 	supplierProductCode;
	private String 	supplierCode;
	private String 	fridge;
	private float 	numberGets;
	
	private int packagingFromBasketNumber;
	private int packagingToBasketNumber;
	
	private boolean isRenewedOrder = false;
	private boolean isDeletedOrder = false;
	
	private String stockOrderNumber = "";
	private int totalNumberOfOrderStock;

	// in case of substitution between product order by customer and product get by supplier.
	private int treatedEDDId;
	private String substitutionProductCode;
	private String substitutionProductDescription;
	private int substitutionNumberOfUnit;
	private String substitutionUnitConditionnement;
	private String substitutionSupplierProductCode;
	private String substitutionSupplierCode;
	private String substitutionSupplierName;


	private Customer customer;
	// data from prepareOrderDetail 
	//private String 	customerCode;
	
	// data from customer
	//private String 	customerName;

	public CustomerOrder() {
	}

	public CustomerOrder(String orderCode, int orderNumber) {
		setOrderCode(orderCode);
		setOrderNumber(orderNumber);
	}

	public int getCreationNumber() {
		return creationNumber;
	}

	public void setCreationNumber(int creationNumber) {
		this.creationNumber = creationNumber;
	}
/*
	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
*/
	public int getFeesType() {
		return feesType;
	}

	public void setFeesType(int feesType) {
		this.feesType = feesType;
	}

	public String getFridge() {
		return fridge;
	}

	public void setFridge(String fridge) {
		this.fridge = fridge;
	}

	public float getNumberGets() {
		return numberGets;
	}

	public void setNumberGets(float numberGets) {
		this.numberGets = numberGets;
	}

	public int getNumberOfProduct() {
		return numberOfProduct;
	}

	public void setNumberOfProduct(int numberOfProduct) {
		this.numberOfProduct = numberOfProduct;
	}

	public int getNumberOfUnit() {
		return numberOfUnit;
	}

	public void setNumberOfUnit(int numberOfUnit) {
		this.numberOfUnit = numberOfUnit;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}


	public String getSupplierProductCode() {
		return supplierProductCode;
	}

	public void setSupplierProductCode(String supplierProductCode) {
		this.supplierProductCode = supplierProductCode;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public int getTotalNumberOfUnit() {
		return totalNumberOfUnit;
	}

	public void setTotalNumberOfUnit(int totalNumberOfUnit) {
		this.totalNumberOfUnit = totalNumberOfUnit;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getSupplierOrderLetter() {
		return supplierOrderLetter;
	}

	public void setSupplierOrderLetter(String supplierOrderLetter) {
		this.supplierOrderLetter = supplierOrderLetter;
	}

	public int getSupplierOrderNumber() {
		return supplierOrderNumber;
	}

	public void setSupplierOrderNumber(int supplierOrderNumber) {
		this.supplierOrderNumber = supplierOrderNumber;
	}
/*
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
*/
	public int getSubstitutionNumberOfUnit() {
		return substitutionNumberOfUnit;
	}

	public void setSubstitutionNumberOfUnit(int substitutionNumberOfUnit) {
		this.substitutionNumberOfUnit = substitutionNumberOfUnit;
	}

	public String getSubstitutionProductCode() {
		return substitutionProductCode;
	}

	public void setSubstitutionProductCode(String substitutionProductCode) {
		this.substitutionProductCode = substitutionProductCode;
	}

	public String getSubstitutionProductDescription() {
		return substitutionProductDescription;
	}

	public void setSubstitutionProductDescription(
			String substitutionProductDescription) {
		this.substitutionProductDescription = substitutionProductDescription;
	}

	public Customer getCustomer() {
		if (customer == null) {
			customer = new Customer();
		}
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public int getPackagingFromBasketNumber() {
		return packagingFromBasketNumber;
	}

	public void setPackagingFromBasketNumber(int packagingFromBasketNumber) {
		this.packagingFromBasketNumber = packagingFromBasketNumber;
	}

	public int getPackagingToBasketNumber() {
		return packagingToBasketNumber;
	}

	public void setPackagingToBasketNumber(int packagingToBasketNumber) {
		this.packagingToBasketNumber = packagingToBasketNumber;
	}

	public int getTreatedEDDId() {
		return treatedEDDId;
	}

	public void setTreatedEDDId(int treatedEDDId) {
		this.treatedEDDId = treatedEDDId;
	}

	public String getSubstitutionSupplierCode() {
		return substitutionSupplierCode;
	}

	public void setSubstitutionSupplierCode(String substitutionSupplierCode) {
		this.substitutionSupplierCode = substitutionSupplierCode;
	}

	public String getSubstitutionSupplierName() {
		return substitutionSupplierName;
	}

	public void setSubstitutionSupplierName(String substitutionSupplierName) {
		this.substitutionSupplierName = substitutionSupplierName;
	}

	public String getSubstitutionUnitConditionnement() {
		return substitutionUnitConditionnement;
	}

	public void setSubstitutionUnitConditionnement(
			String substitutionUnitConditionnement) {
		this.substitutionUnitConditionnement = substitutionUnitConditionnement;
	}

	public Fly getFly() {
		return fly;
	}

	public void setFly(Fly fly) {
		this.fly = fly;
	}

	public boolean isDeletedOrder() {
		return isDeletedOrder;
	}

	public void setDeletedOrder(boolean isDeletedOrder) {
		this.isDeletedOrder = isDeletedOrder;
	}

	public boolean isRenewedOrder() {
		return isRenewedOrder;
	}

	public void setRenewedOrder(boolean isRenewedOrder) {
		this.isRenewedOrder = isRenewedOrder;
	}

	public String getStockOrderNumber() {
		return stockOrderNumber;
	}

	public void setStockOrderNumber(String stockOrderNumber) {
		this.stockOrderNumber = stockOrderNumber;
	}

	public int getTotalNumberOfOrderStock() {
		return totalNumberOfOrderStock;
	}

	public void setTotalNumberOfOrderStock(int totalNumberOfOrderStock) {
		this.totalNumberOfOrderStock = totalNumberOfOrderStock;
	}


	public String getSubstitutionSupplierProductCode() {
		return substitutionSupplierProductCode;
	}

	public void setSubstitutionSupplierProductCode(String substitutionSupplierProductCode) {
		this.substitutionSupplierProductCode = substitutionSupplierProductCode;
	}
}
