package db.product;


public class Conditioning  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String productCode;
	private int priority;
	private String supplierCode;
	private String supplierProductCode;
	private String ean;
	private String unitLargeScale;
	private String unit;
	private int numberOfUnit;
	private String editionTypeId;
	private String editionTypeAdditionalId;
	private String dateLastModification;
	private float unitPrice;

	public Conditioning(){}

	public String getEan() {
		return ean;
	}
	public void setEan(String ean) {
		this.ean = ean;
	}
	public int getNumberOfUnit() {
		return numberOfUnit;
	}
	public void setNumberOfUnit(int numberOfUnit) {
		this.numberOfUnit = numberOfUnit;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
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
	public String getSupplierProductCode() {
		return supplierProductCode;
	}
	public void setSupplierProductCode(String supplierProductCode) {
		this.supplierProductCode = supplierProductCode;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnitLargeScale() {
		return unitLargeScale;
	}

	public void setUnitLargeScale(String unitLargeScale) {
		this.unitLargeScale = unitLargeScale;
	}
	
	public String getDateLastModification() {
		return dateLastModification;
	}

	public void setDateLastModification(String dateLastModification) {
		this.dateLastModification = dateLastModification;
	}

	public String getEditionTypeId() {
		return editionTypeId;
	}

	public void setEditionTypeId(String editionTypeId) {
		this.editionTypeId = editionTypeId;
	}

	public String toString(){
		return productCode + ";priority=" + priority + ";supplierCode=" + supplierCode + ";supplierProductCode=" + supplierProductCode + ";ean=" + ean + ";unitLargeScale=" + unitLargeScale + ";unit=" + unit + ";numberOfUnit=" + numberOfUnit + ";editionTypeId=" + editionTypeId + ";dateLastModification=" + dateLastModification;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getEditionTypeAdditionalId() {
		return editionTypeAdditionalId;
	}

	public void setEditionTypeAdditionalId(String editionTypeAdditionalId) {
		this.editionTypeAdditionalId = editionTypeAdditionalId;
	}

}