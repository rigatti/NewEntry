package db.supplier.order;


public class SupplierOrderDetail implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int orderNumber;
	private String supplierCode;
	private String productCode;
	private String unitLargeScale;
	private int numberOfProductToOrder;	
	private int numberOfProductOrdered;	
	private float numberGets;

	public SupplierOrderDetail() {}

	
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


	public int getNumberOfProductOrdered() {
		return numberOfProductOrdered;
	}


	public void setNumberOfProductOrdered(int numberOfProductOrdered) {
		this.numberOfProductOrdered = numberOfProductOrdered;
	}


	public int getNumberOfProductToOrder() {
		return numberOfProductToOrder;
	}


	public void setNumberOfProductToOrder(int numberOfProductToOrder) {
		this.numberOfProductToOrder = numberOfProductToOrder;
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

	public String toString() {
		return getProductCode() + ";OrderNumber=" + getOrderNumber() + ";supplier=" + getSupplierCode() + "\r\n";
	}


	public String getUnitLargeScale() {
		return unitLargeScale;
	}


	public void setUnitLargeScale(String unitLargeScale) {
		this.unitLargeScale = unitLargeScale;
	}
}