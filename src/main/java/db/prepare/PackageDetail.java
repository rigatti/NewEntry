package db.prepare;

public class PackageDetail  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int orderNumber;
	private String orderLetter;
	private String customerOrderCode;
	private int customerOrderCodeNumber;
	private int feesType;
	private String productCode;
	private int fromPackageNumber;
	private int numberOfUnit;
	private String unit;
	private int numberOfProduct;
	private String supplierCode;

	public PackageDetail() {
	}

	public String getCustomerOrderCode() {
		return customerOrderCode;
	}

	public void setCustomerOrderCode(String customerOrderCode) {
		this.customerOrderCode = customerOrderCode;
	}

	public int getCustomerOrderCodeNumber() {
		return customerOrderCodeNumber;
	}

	public void setCustomerOrderCodeNumber(int customerOrderCodeNumber) {
		this.customerOrderCodeNumber = customerOrderCodeNumber;
	}

	public int getFeesType() {
		return feesType;
	}

	public void setFeesType(int feesType) {
		this.feesType = feesType;
	}

	public int getFromPackageNumber() {
		return fromPackageNumber;
	}

	public void setFromPackageNumber(int fromPackageNumber) {
		this.fromPackageNumber = fromPackageNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	

}