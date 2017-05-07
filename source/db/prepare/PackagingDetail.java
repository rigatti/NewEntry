package db.prepare;

public class PackagingDetail  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int orderNumber;
	private String orderLetter;
	private String customerOrderCode;
	private int customerOrderCodeNumber;
	private int feesType;

	private int fromPackageNumber;
	private int toPackageNumber;
	private int fridge;

	public PackagingDetail() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getToPackageNumber() {
		return toPackageNumber;
	}

	public void setToPackageNumber(int toPackageNumber) {
		this.toPackageNumber = toPackageNumber;
	}

	public int getFromPackageNumber() {
		return fromPackageNumber;
	}

	public void setFromPackageNumber(int fromPackageNumber) {
		this.fromPackageNumber = fromPackageNumber;
	}

	public int getFridge() {
		return fridge;
	}

	public void setFridge(int fridge) {
		this.fridge = fridge;
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

	public String getOrderLetter() {
		return orderLetter;
	}

	public void setOrderLetter(String orderLetter) {
		this.orderLetter = orderLetter;
	}

}