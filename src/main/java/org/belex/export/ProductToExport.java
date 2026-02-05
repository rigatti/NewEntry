package org.belex.export;

public class ProductToExport {
	private String productCode;
	
	private String description_fr;

	private int familyCode;
	private int sortingOrder;
		
	private String editionTypeId;
	private String editionTypeLabel;
	
	private String editionTypeAdditionalId;
	private String editionTypeAdditionalLabel;
	
	private boolean isPro;
	private boolean planeMandatory;

	private String unitConditioning;
	private float unitNumber;
	private float unitPrice;
	
	private float marginForStandardCustomer;
	
	private String totalConditioning;
	private float totalPrice;
	
	private String dateLastUpdate;

	public ProductToExport() {
		init();
	}
	private void init() {
		productCode = "";
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getUnitConditioning() {
		return unitConditioning;
	}
	public void setUnitConditioning(String unitConditioning) {
		this.unitConditioning = unitConditioning;
	}
	public float getUnitNumber() {
		return unitNumber;
	}
	public void setUnitNumber(float unitNumber) {
		this.unitNumber = unitNumber;
	}
	public float getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getTotalConditioning() {
		return totalConditioning;
	}
	public void setTotalConditioning(String totalConditioning) {
		this.totalConditioning = totalConditioning;
	}
	public float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getEditionTypeId() {
		return editionTypeId;
	}
	public void setEditionTypeId(String editionTypeId) {
		this.editionTypeId = editionTypeId;
	}
	public String getEditionTypeLabel() {
		return editionTypeLabel;
	}
	public void setEditionTypeLabel(String editionTypeLabel) {
		this.editionTypeLabel = editionTypeLabel;
	}
	public boolean isPlaneMandatory() {
		return planeMandatory;
	}
	public void setPlaneMandatory(boolean planeMandatory) {
		this.planeMandatory = planeMandatory;
	}
	public int getFamilyCode() {
		return familyCode;
	}
	public void setFamilyCode(int familyCode) {
		this.familyCode = familyCode;
	}
	public String getDescription_fr() {
		return description_fr;
	}
	public void setDescription_fr(String description_fr) {
		this.description_fr = description_fr;
	}
	public float getMarginForStandardCustomer() {
		return marginForStandardCustomer;
	}
	public void setMarginForStandardCustomer(float marginForStandardCustomer) {
		this.marginForStandardCustomer = marginForStandardCustomer;
	}
	public boolean isPro() {
		return isPro;
	}
	public void setPro(boolean isPro) {
		this.isPro = isPro;
	}
	public String getEditionTypeAdditionalId() {
		return editionTypeAdditionalId;
	}
	public void setEditionTypeAdditionalId(String editionTypeAdditionalId) {
		this.editionTypeAdditionalId = editionTypeAdditionalId;
	}
	public String getEditionTypeAdditionalLabel() {
		return editionTypeAdditionalLabel;
	}
	public void setEditionTypeAdditionalLabel(String editionTypeAdditionalLabel) {
		this.editionTypeAdditionalLabel = editionTypeAdditionalLabel;
	}
	public int getSortingOrder() {
		return sortingOrder;
	}
	public void setSortingOrder(int sortingOrder) {
		this.sortingOrder = sortingOrder;
	}
	public String getDateLastUpdate() {
		return dateLastUpdate;
	}
	public void setDateLastUpdate(String dateLastUpdate) {
		this.dateLastUpdate = dateLastUpdate;
	}
}
