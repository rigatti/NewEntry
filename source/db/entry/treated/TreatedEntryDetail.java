package db.entry.treated;

import java.util.ArrayList;

public class TreatedEntryDetail  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int treatedEntryDetailId;
	private int treatedEntryId;
	private String supplierOrderNumbers;
	private String supplierOrderLetters;
	private String additionalData;
	private String description;
	private String unitConditionnement;
	private int numberOfUnit;
	private int numberOfProduct;
	private String supplierDocumentDescription;
	private int supplierDocumentType;
	private int stockMovement;
	private int supplierEntryProductIntegrity;
	private int supplierEntryPackagingIntegrity;
	private int supplierEntryDlcDdmValidity;
	private int supplierEntryTemperatureValidity;
	private String supplierEntryCommentOnQuality;

	private ArrayList<TreatedEntryDetailDestination> treatedEntryDetailDestinations  = new ArrayList<TreatedEntryDetailDestination>();
	
	public TreatedEntryDetail() {}

	public String getSupplierOrderNumbers() {
		return supplierOrderNumbers;
	}

	public void setSupplierOrderNumbers(String supplierOrderNumbers) {
		this.supplierOrderNumbers = supplierOrderNumbers;
	}

	public int getTreatedEntryId() {
		return treatedEntryId;
	}

	public void setTreatedEntryId(int treatedEntryId) {
		this.treatedEntryId = treatedEntryId;
	}

	public String getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumberOfUnit() {
		return numberOfUnit;
	}

	public void setNumberOfUnit(int numberOfUnit) {
		this.numberOfUnit = numberOfUnit;
	}

	public String getUnitConditionnement() {
		return unitConditionnement;
	}

	public void setUnitConditionnement(String unitConditionnement) {
		this.unitConditionnement = unitConditionnement;
	}

	public int getNumberOfProduct() {
		return numberOfProduct;
	}

	public void setNumberOfProduct(int numberOfProduct) {
		this.numberOfProduct = numberOfProduct;
	}

	public int getTreatedEntryDetailId() {
		return treatedEntryDetailId;
	}

	public void setTreatedEntryDetailId(int id) {
		this.treatedEntryDetailId = id;
	}

	public String getSupplierOrderLetters() {
		return supplierOrderLetters;
	}

	public void setSupplierOrderLetters(String supplierOrderLetters) {
		this.supplierOrderLetters = supplierOrderLetters;
	}

	public ArrayList<TreatedEntryDetailDestination> getTreatedEntryDetailDestinations() {
		return treatedEntryDetailDestinations;
	}

	public void setTreatedEntryDetailDestinations(
			ArrayList<TreatedEntryDetailDestination> treatedEntryDetailDestinations) {
		this.treatedEntryDetailDestinations = treatedEntryDetailDestinations;
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

	public int getStockMovement() {
		return stockMovement;
	}

	public void setStockMovement(int stockMovement) {
		this.stockMovement = stockMovement;
	}

	public int getSupplierEntryProductIntegrity() {
		return supplierEntryProductIntegrity;
	}

	public void setSupplierEntryProductIntegrity(int supplierEntryProductIntegrity) {
		this.supplierEntryProductIntegrity = supplierEntryProductIntegrity;
	}

	public int getSupplierEntryPackagingIntegrity() {
		return supplierEntryPackagingIntegrity;
	}

	public void setSupplierEntryPackagingIntegrity(int supplierEntryPackagingIntegrity) {
		this.supplierEntryPackagingIntegrity = supplierEntryPackagingIntegrity;
	}

	public int getSupplierEntryDlcDdmValidity() {
		return supplierEntryDlcDdmValidity;
	}

	public void setSupplierEntryDlcDdmValidity(int supplierEntryDlcDdmValidity) {
		this.supplierEntryDlcDdmValidity = supplierEntryDlcDdmValidity;
	}

	public int getSupplierEntryTemperatureValidity() {
		return supplierEntryTemperatureValidity;
	}

	public void setSupplierEntryTemperatureValidity(int supplierEntryTemperatureValidity) {
		this.supplierEntryTemperatureValidity = supplierEntryTemperatureValidity;
	}

	public String getSupplierEntryCommentOnQuality() {
		return supplierEntryCommentOnQuality;
	}

	public void setSupplierEntryCommentOnQuality(String supplierEntryCommentOnQuality) {
		this.supplierEntryCommentOnQuality = supplierEntryCommentOnQuality;
	}
}