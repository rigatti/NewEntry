package org.belex.customer;

import java.io.Serializable;

public class CustomerEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	private String assignedSupplierCode;
	
	private String productCode;
	private int priority;
	private String unitConditionnement;
	private int numberOfConditionnement;
	private int numberOfUnit;
	private int totalUnit;
	private boolean needFridge;

	private int fromBasketNumber;
	private int toBasketNumber;

	public String getUnitConditionnement() {
		return unitConditionnement;
	}
	public void setUnitConditionnement(String unitConditionnement) {
		this.unitConditionnement = unitConditionnement;
	}
	public boolean isNeedFridge() {
		return needFridge;
	}
	public void setNeedFridge(boolean needFridge) {
		this.needFridge = needFridge;
	}
	public int getNumberOfConditionnement() {
		return numberOfConditionnement;
	}
	public void setNumberOfConditionnement(int numberOfConditionnement) {
		//if (numberOfConditionnement != null && numberOfConditionnement.endsWith(".0")) {
		//	numberOfConditionnement = numberOfConditionnement.substring(0,numberOfConditionnement.length()-2);
		//}
		this.numberOfConditionnement = numberOfConditionnement;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int productPriority) {
		this.priority = productPriority;
	}
	public int getTotalUnit() {
		return totalUnit;
	}
	public void setTotalUnit(int totalUnit) {
		this.totalUnit = totalUnit;
	}
	public int getNumberOfUnit() {
		return numberOfUnit;
	}
	public void setNumberOfUnit(int numberOfUnit) {
		//if (numberOfUnit != null && numberOfUnit.endsWith(".0")) {
		//	numberOfUnit = numberOfUnit.substring(0,numberOfUnit.length()-2);
		//}
		this.numberOfUnit = numberOfUnit;
	}
	public int getFromBasketNumber() {
		return fromBasketNumber;
	}
	public void setFromBasketNumber(int fromBasketNumber) {
		this.fromBasketNumber = fromBasketNumber;
	}
	public int getToBasketNumber() {
		return toBasketNumber;
	}
	public void setToBasketNumber(int toBasketNumber) {
		this.toBasketNumber = toBasketNumber;
	}
	public String getAssignedSupplierCode() {
		return assignedSupplierCode;
	}
	public void setAssignedSupplierCode(String assignedSupplierCode) {
		this.assignedSupplierCode = assignedSupplierCode;
	}
}
