package db.entry;


public class SupplierEntry implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int supplierEntryId;
	private String supplierCode;
	private String orderNumbers;
	private String checked;
	private String responsible;
	private String entryDate;
	private String entryTime;
	private String closed;

	public SupplierEntry() {}

	public String getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(String entryTime) {
		this.entryTime = entryTime;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getClosed() {
		return closed;
	}

	public void setClosed(String closed) {
		this.closed = closed;
	}

	public int getSupplierEntryId() {
		return supplierEntryId;
	}
	public void setSupplierEntryId(int supplierEntryId) {
		this.supplierEntryId = supplierEntryId;
	}
	public String getOrderNumbers() {
		return orderNumbers;
	}
	public void setOrderNumbers(String orderNumbers) {
		this.orderNumbers = orderNumbers;
	}
	public String getResponsible() {
		return responsible;
	}
	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
}
