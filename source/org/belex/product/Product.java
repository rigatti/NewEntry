package org.belex.product;
import java.io.Serializable;
import java.util.Vector;

public class Product implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String productCode = "";
	private String description = "";

	//private String ean = "";

	private String validityDate = "";
	private String lotNumber = "";
	private String additionalData = "";

	private String newProductLabel = "";
	private String newProductEan = "";
	private String newProductUnitConditionnement = "";
	private String newProductNumberOfUnit = "";
	private String newProductSupplierCode = "";

	private Vector<Unit> units;
	
	public class Unit implements Serializable {
		private static final long serialVersionUID = 1L;

		private String supplierCode;
		private String ean;
		private String conditionnement;
		private String dateLastMofication;
		private int number;
		private boolean selected;
		
		public Unit() {
			setSupplierCode("");
			setEan("");
			setNumber(0);
			setConditionnement("");
			setDateLastMofication("");
			setSelected(false);
		}

		public String getConditionnement() {
			return conditionnement;
		}
		public void setConditionnement(String conditionnement) {
			if (conditionnement != null) {
				this.conditionnement = conditionnement.toUpperCase();
			}
		}
		public int getNumber() {
			return number;
		}
		public void setNumber(int number) {
			this.number = number;
		}

		public String getSupplierCode() {
			return supplierCode;
		}

		public void setSupplierCode(String supplierCode) {
			if (supplierCode != null) {
				this.supplierCode = supplierCode;
			}
		}

		public String getEan() {
			return ean;
		}

		public void setEan(String ean) {
			if (ean != null) {
				this.ean = ean;
			}
		}

		public boolean isSelected() {
			return selected;
		}

		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		public String getDateLastMofication() {
			return dateLastMofication;
		}

		public void setDateLastMofication(String dateLastMofication) {
			if (dateLastMofication != null) {
				this.dateLastMofication = dateLastMofication;
			}
		}
	}
	
	public Product() {
	}

	public Product(String productCode) {
		setProductCode(productCode);
		setDescription("");
		//setEan("");
		setValidityDate("");
		
		//setNumberOfUnit("");
		//setUnitConditionnement("");
		
		setLotNumber("");
		setAdditionalData("");
		
		units = new Vector<Unit>() ;
	}

	public void setProductCode(String productCode){
		this.productCode = productCode;
	}

	public String getProductCode(){
		return productCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public String getValidityDate() {
		return validityDate;
	}

	public void setValidityDate(String validityDate) {
		this.validityDate = validityDate;
	}

	//public String getEan() {
	//	return ean;
	//}

	//public void setEan(String ean) {
	//	this.ean = ean;
	//}

	//public String getUnitConditionnement() {
	//	return unitConditionnement;
	//}

	//public void setUnitConditionnement(String unitConditionnement) {
	//	this.unitConditionnement = unitConditionnement;
	//}

	//public String getNumberOfUnit() {
	//	return numberOfUnit;
	//}

	//public void setNumberOfUnit(String numberOfUnit) {
	//	if (numberOfUnit != null && numberOfUnit.endsWith(".0")) {
	//		numberOfUnit = numberOfUnit.substring(0,numberOfUnit.length()-2);
	//	}
	//	this.numberOfUnit = numberOfUnit;
	//}

	public Vector<Unit> getUnits() {
		return units;
	}

	public void setUnits(Vector<Unit> units) {
		this.units = units;
	}
	public void addUnit(Unit unit) {
		if (units == null){
			units = new Vector<Unit>();
		}
		units.add(unit);
	}

	public void setUnitSelected(String unitIndexSelected) {
		try {
			int index = Integer.parseInt(unitIndexSelected);
			if (index >= 0 && index <= units.size()) {
				units.get(index).setSelected(true);
			}
		} catch(NumberFormatException nfe) {
			// ignore
		}
	}

	public String getNewProductEan() {
		return newProductEan;
	}

	public void setNewProductEan(String newProductEan) {
		this.newProductEan = newProductEan;
	}

	public String getNewProductLabel() {
		return newProductLabel;
	}

	public void setNewProductLabel(String newProductLabel) {
		this.newProductLabel = newProductLabel.toUpperCase();
	}

	public String getNewProductNumberOfUnit() {
		return newProductNumberOfUnit;
	}

	public void setNewProductNumberOfUnit(String newProductNumberOfUnit) {
		this.newProductNumberOfUnit = newProductNumberOfUnit;
	}

	public String getNewProductSupplierCode() {
		return newProductSupplierCode;
	}

	public void setNewProductSupplierCode(String newProductSupplierCode) {
		this.newProductSupplierCode = newProductSupplierCode;
	}

	public String getNewProductUnitConditionnement() {
		return newProductUnitConditionnement;
	}

	public void setNewProductUnitConditionnement(
			String newProductUnitConditionnement) {
		this.newProductUnitConditionnement = newProductUnitConditionnement.toUpperCase();
	}

}
