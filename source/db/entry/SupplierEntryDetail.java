package db.entry;

public class SupplierEntryDetail {

	private int id;
	private int supplierEntryId;
	private String supplierCode;
	private int ligneNumber;
	private int orderNumber;
	private String date;
	private String productCode;
	private String ean;
	private String unit;
	private int numberGets;
	private int numberOfUnit;
	private String checked;
	private String updated;
	private String lot;

	public SupplierEntryDetail() {}

	public SupplierEntryDetail(int supplierEntryId) {
		super();
		this.supplierEntryId = supplierEntryId;
	}

	public String isChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getEan() {
		return ean;
	}
	public void setEan(String ean) {
		this.ean = ean;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLigneNumber() {
		return ligneNumber;
	}
	public void setLigneNumber(int ligneNumber) {
		this.ligneNumber = ligneNumber;
	}
	public String getLot() {
		return lot;
	}
	public void setLot(String lot) {
		this.lot = lot;
	}
	public int getNumberGets() {
		return numberGets;
	}
	public void setNumberGets(int numberGets) {
		this.numberGets = numberGets;
	}
	public int getNumberOfUnit() {
		return numberOfUnit;
	}
	public void setNumberOfUnit(int numberOfUnit) {
		this.numberOfUnit = numberOfUnit;
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
	public int getSupplierEntryId() {
		return supplierEntryId;
	}
	public void setSupplierEntryId(int supplierEntryId) {
		this.supplierEntryId = supplierEntryId;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String isUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
}
