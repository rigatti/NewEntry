package db.customer.order;


public class CustomerOrderDetail implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int id;

	private int orderNumber;
	private String orderLetter;
	private int creationNumber;
	private String customerOrderCode;
	private int customerOrderCodeNumber;
	private int feesType;
	private String productCode;
	private String productDescription;
	private int priority;
	private int numberOfUnit;
	private String unit;
	private int numberOfProduct;
	private int totalNumberOfUnit;
	private String supplierCode;
	private String fridge;
	private float numberGets;
	private int treatedEDDId;
	private int deletedOrderFlag;
	private int renewedOrderFlag;

	public CustomerOrderDetail() {}

	public String getCustomerOrderCode() {
		return customerOrderCode;
	}

	public void setCustomerOrderCode(String customerOrderCode) {
		this.customerOrderCode = customerOrderCode;
	}


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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getOrderLetter() {
		return orderLetter;
	}

	public void setOrderLetter(String orderLetter) {
		this.orderLetter = orderLetter;
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

	public int getCustomerOrderCodeNumber() {
		return customerOrderCodeNumber;
	}

	public void setCustomerOrderCodeNumber(int customerOrderCodeNumber) {
		this.customerOrderCodeNumber = customerOrderCodeNumber;
	}

	public int getCreationNumber() {
		return creationNumber;
	}

	public void setCreationNumber(int creationNumber) {
		this.creationNumber = creationNumber;
	}

	public int getDeletedOrderFlag() {
		return deletedOrderFlag;
	}

	public void setDeletedOrderFlag(int deletedOrderFlag) {
		this.deletedOrderFlag = deletedOrderFlag;
	}

	public int getTreatedEDDId() {
		return treatedEDDId;
	}

	public void setTreatedEDDId(int treatedEDDId) {
		this.treatedEDDId = treatedEDDId;
	}

	public int getRenewedOrderFlag() {
		return renewedOrderFlag;
	}

	public void setRenewedOrderFlag(int renewedOrderFlag) {
		this.renewedOrderFlag = renewedOrderFlag;
	}
	
	public String toString() {
		return getProductDescription() + "(" + getProductCode() + ");OrderNumber=" + getOrderNumber() + ";OrderLetter=" + getOrderLetter() + "\r\n";
	}
}