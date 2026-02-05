package db.prepare;

public class Packaging  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int orderNumber;
	private String orderLetter;
	private String customerOrderCode;
	private float customerOrderCodeNumber;
	private int feesType;
	private int fromPackageNumber;
	private String customerCode;

	
	public Packaging() {}

	public String getCustomerOrderCode() {
		return customerOrderCode;
	}


	public void setCustomerOrderCode(String customerOrderCode) {
		this.customerOrderCode = customerOrderCode;
	}


	public float getCustomerOrderCodeNumber() {
		return customerOrderCodeNumber;
	}


	public void setCustomerOrderCodeNumber(float customerOrderCodeNumber) {
		this.customerOrderCodeNumber = customerOrderCodeNumber;
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

	public int getFeesType() {
		return feesType;
	}

	public void setFeesType(int feesType) {
		this.feesType = feesType;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public int getFromPackageNumber() {
		return fromPackageNumber;
	}

	public void setFromPackageNumber(int fromPackageNumber) {
		this.fromPackageNumber = fromPackageNumber;
	}

}