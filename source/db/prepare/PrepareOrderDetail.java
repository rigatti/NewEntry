package db.prepare;

public class PrepareOrderDetail  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int orderNumber;
	private String orderLetter;	
	private String customerOrderCode;
	private int customerOrderCodeNumber;
	private int feesType;
	private String customerCode;

	public PrepareOrderDetail(){}

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

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
}