package db.entry.treated;

public class TreatedEntry  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int treatedEntryId;
	private String productCode;
	private String validityDate;
	private String ean;
	private String lotNumber;
	private String supplierCode;
	private String arrivalDate;
	private String arrivalTime;
	
	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public TreatedEntry() {}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public int getTreatedEntryId() {
		return treatedEntryId;
	}

	public void setTreatedEntryId(int id) {
		this.treatedEntryId = id;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
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

	public String getValidityDate() {
		return validityDate;
	}

	public void setValidityDate(String validityDate) {
		this.validityDate = validityDate;
	}
}