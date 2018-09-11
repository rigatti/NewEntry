package db.entry.treated.temp;


public class TreatedEntryTemp  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

  	private int id;

	private String supplierCode;
	private String arrivalDate;
	private String arrivalTime;
	private String productCode;
	private String ean;
	private String unitConditionnement;
	private int numberOfUnit;
	private int numberOfProduct;
	private String lotNumber;
	private String supplierProductCode;
	private String supplierOrderNumbers;
	private String supplierOrderLetters;
	private String validityDate;
	private String additionalData;
	private String description;
	private float numberOfProductReceived;
	private String supplierDocumentDescription;
	private int supplierDocumentType;
	private int stockMovement;

	public int getStockMovement() {
		return stockMovement;
	}

	public void setStockMovement(int stockMovement) {
		this.stockMovement = stockMovement;
	}

	public String getSupplierDocumentDescription() {
		return supplierDocumentDescription;
	}

	public void setSupplierDocumentDescription(String supplierDocumentDescription) {
		this.supplierDocumentDescription = supplierDocumentDescription;
	}

	public int getSupplierDocumentType() {
		return supplierDocumentType;
	}

	public void setSupplierDocumentType(int supplierDocumentType) {
		this.supplierDocumentType = supplierDocumentType;
	}

	public String getSupplierProductCode() {
		return supplierProductCode;
	}

	public void setSupplierProductCode(String supplierProductCode) {
		this.supplierProductCode = supplierProductCode;
	}

	public TreatedEntryTemp() {}

	public String getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public int getNumberOfProduct() {
		return numberOfProduct;
	}

	public void setNumberOfProduct(int numberOfProduct) {
		this.numberOfProduct = numberOfProduct;
	}

	public float getNumberOfProductReceived() {
		return numberOfProductReceived;
	}

	public void setNumberOfProductReceived(float numberOfProductReceived) {
		this.numberOfProductReceived = numberOfProductReceived;
	}

	public int getNumberOfUnit() {
		return numberOfUnit;
	}

	public void setNumberOfUnit(int numberOfUnit) {
		this.numberOfUnit = numberOfUnit;
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

	public String getSupplierOrderNumbers() {
		return supplierOrderNumbers;
	}

	public void setSupplierOrderNumbers(String supplierOrderNumbers) {
		this.supplierOrderNumbers = supplierOrderNumbers;
	}

	public String getUnitConditionnement() {
		return unitConditionnement;
	}

	public void setUnitConditionnement(String unitConditionnement) {
		this.unitConditionnement = unitConditionnement;
	}

	public String getValidityDate() {
		return validityDate;
	}

	public void setValidityDate(String validityDate) {
		this.validityDate = validityDate;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getSupplierOrderLetters() {
		return supplierOrderLetters;
	}

	public void setSupplierOrderLetters(String supplierOrderLetters) {
		this.supplierOrderLetters = supplierOrderLetters;
	}

}