package org.belex.product;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductTraceable implements Serializable {

	private static final long serialVersionUID = 1L;

	private String productCode;
	private String supplierProductCode;
	private String supplierCode;
	private String supplierName;

	private String arrivalDate;
	private String arrivalTime;
	private String description;
	private String unitConditionnement;
	private int numberOfUnit;
	private int numberOfProduct;
	private int totalToAllocate;
	//private int numberOfUnitRemaining;

	private ArrayList<Destination> destinations = new ArrayList<Destination>();
	
	@Deprecated
	private boolean isSubstitutionItem = false;
	
	public class Destination implements Serializable {
		private static final long serialVersionUID = 1L;

		private boolean modified = false;

		private String customerCode = "";
		private String customerName = "";
		private int numberOfUnit = 0;

		private boolean isSubstitutionItem = false;
		private boolean isSupplierReturnItem = false;
		private boolean isStockItem = false;
		
		private int packagingFromBasket = 0;
		private int packagingToBasket = 0;

		private String substitutionOfProduct = "";
		private String substitutionOfProductDescr = "";
		private int substitutionOfNumberOfUnit = 0;
		private int substitutionOfNumberOfProduct = 0;
		private String substitutionOfUnitConditionnement = "";

		private String substitutionOfSupplierCode = "";
		private int substitutionOfSupplierOrderNumber = 0;
		private String substitutionOfSupplierOrderLetter = "";
		private String substitutionOfCustomerOrderCode = "";
		private int substitutionOfCustomerOrderNumber = 0;

		public Destination() {
			setModified(true);
		}

		public Destination(boolean isModified) {
			setModified(isModified);
		}

		public String getCustomerCode() {
			return customerCode;
		}

		public void setCustomerCode(String customerCode) {
			this.customerCode = customerCode;
		}

		public int getNumberOfUnit() {
			return numberOfUnit;
		}

		public void setNumberOfUnit(int numberOfUnit) {
			this.numberOfUnit = numberOfUnit;
		}
		
		public int getSubstitutionOfNumberOfUnit() {
			return substitutionOfNumberOfUnit;
		}

		public void setSubstitutionOfNumberOfUnit(int substitutionOfNumberOfUnit) {
			this.substitutionOfNumberOfUnit = substitutionOfNumberOfUnit;
		}

		public String getSubstitutionOfProduct() {
			return substitutionOfProduct;
		}

		public void setSubstitutionOfProduct(String substitutionOfProduct) {
			this.substitutionOfProduct = substitutionOfProduct;
		}

		public String getSubstitutionOfCustomerOrderCode() {
			return substitutionOfCustomerOrderCode;
		}

		public void setSubstitutionOfCustomerOrderCode(
				String substitutionOfCustomerOrderCode) {
			this.substitutionOfCustomerOrderCode = substitutionOfCustomerOrderCode;
		}

		public int getSubstitutionOfCustomerOrderNumber() {
			return substitutionOfCustomerOrderNumber;
		}

		public void setSubstitutionOfCustomerOrderNumber(
				int substitutionOfCustomerOrderNumber) {
			this.substitutionOfCustomerOrderNumber = substitutionOfCustomerOrderNumber;
		}

		public String getSubstitutionOfSupplierCode() {
			return substitutionOfSupplierCode;
		}

		public void setSubstitutionOfSupplierCode(String substitutionOfSupplierCode) {
			this.substitutionOfSupplierCode = substitutionOfSupplierCode;
		}

		public String getSubstitutionOfSupplierOrderLetter() {
			return substitutionOfSupplierOrderLetter;
		}

		public void setSubstitutionOfSupplierOrderLetter(
				String substitutionOfSupplierOrderLetter) {
			this.substitutionOfSupplierOrderLetter = substitutionOfSupplierOrderLetter;
		}

		public int getSubstitutionOfSupplierOrderNumber() {
			return substitutionOfSupplierOrderNumber;
		}

		public void setSubstitutionOfSupplierOrderNumber(
				int substitutionOfSupplierOrderNumber) {
			this.substitutionOfSupplierOrderNumber = substitutionOfSupplierOrderNumber;
		}

		public String getCustomerName() {
			return customerName;
		}

		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}

		public String getSubstitutionOfProductDescr() {
			return substitutionOfProductDescr;
		}

		public void setSubstitutionOfProductDescr(String substitutionOfProductDescr) {
			this.substitutionOfProductDescr = substitutionOfProductDescr;
		}
		public int getSubstitutionOfNumberOfProduct() {
			return substitutionOfNumberOfProduct;
		}

		public void setSubstitutionOfNumberOfProduct(int substitutionOfNumberOfProduct) {
			this.substitutionOfNumberOfProduct = substitutionOfNumberOfProduct;
		}

		public String getSubstitutionOfUnitConditionnement() {
			return substitutionOfUnitConditionnement;
		}

		public void setSubstitutionOfUnitConditionnement(
				String substitutionOfUnitConditionnement) {
			this.substitutionOfUnitConditionnement = substitutionOfUnitConditionnement;
		}

		public boolean isModified() {
			return modified;
		}

		public void setModified(boolean modified) {
			this.modified = modified;
		}

		public int getPackagingFromBasket() {
			return packagingFromBasket;
		}

		public void setPackagingFromBasket(int packagingFromBasket) {
			this.packagingFromBasket = packagingFromBasket;
		}

		public int getPackagingToBasket() {
			return packagingToBasket;
		}

		public void setPackagingToBasket(int packagingToBasket) {
			this.packagingToBasket = packagingToBasket;
		}

		public boolean isStockItem() {
			return isStockItem;
		}

		public void setStockItem(boolean isStockItem) {
			this.isStockItem = isStockItem;
		}

		public boolean isSubstitutionItem() {
			return isSubstitutionItem;
		}

		public void setSubstitutionItem(boolean isSubstitutionItem) {
			this.isSubstitutionItem = isSubstitutionItem;
		}

		public boolean isSupplierReturnItem() {
			return isSupplierReturnItem;
		}

		public void setSupplierReturnItem(boolean isSupplierReturnItem) {
			this.isSupplierReturnItem = isSupplierReturnItem;
		}
	}

	public boolean containsSubstitutionItem() {
		boolean result = false;
		for(Destination dest : this.getDestinations()) {
			if (dest.isSubstitutionItem()) {
				result = true;
				break;
			}
		}
		return result;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNumberOfProduct() {
		return numberOfProduct;
	}

	public void setNumberOfProduct(int numberOfProduct) {
		this.numberOfProduct = numberOfProduct;
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

	public String getSupplierProductCode() {
		return supplierProductCode;
	}

	public void setSupplierProductCode(String supplierProductCode) {
		this.supplierProductCode = supplierProductCode;
	}
	
	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getUnitConditionnement() {
		return unitConditionnement;
	}

	public void setUnitConditionnement(String unitConditionnement) {
		this.unitConditionnement = unitConditionnement;
	}

	public ArrayList<Destination> getDestinations() {
		return destinations;
	}

	public void setDestinations(ArrayList<Destination> destinations) {
		this.destinations = destinations;
	}
	public void addDestination(Destination destination) {
		this.destinations.add(destination);
	}

	public int getTotalToAllocate() {
		return totalToAllocate;
	}

	public void setTotalToAllocate(int totalToAllocate) {
		this.totalToAllocate = totalToAllocate;
	}

	public boolean isSubstitutionItem() {
		return isSubstitutionItem;
	}

	public void setSubstitutionItem(boolean isSubstitutionItem) {
		this.isSubstitutionItem = isSubstitutionItem;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
}
