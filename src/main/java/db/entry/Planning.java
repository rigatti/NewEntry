package db.entry;

public class Planning  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int orderNumber;
	private int creationNumber;
	private String supplierCode;
	private String plannedDate;
	private String plannedMode;

	public Planning(){}

	public String getPlannedMode() {
		return plannedMode;
	}

	public void setPlannedMode(String plannedMode) {
		this.plannedMode = plannedMode;
	}

	public int getCreationNumber() {
		return creationNumber;
	}

	public void setCreationNumber(int creationNumber) {
		this.creationNumber = creationNumber;
	}

	public String getPlannedDate() {
		return plannedDate;
	}

	public void setPlannedDate(String plannedDate) {
		this.plannedDate = plannedDate;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String toString() {
		return getSupplierCode() + ";OrderNbr=" + getOrderNumber() + ";CreationNbr=" + getCreationNumber() + ";\r\n";
	}
}