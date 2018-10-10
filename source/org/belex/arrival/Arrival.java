package org.belex.arrival;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.belex.entry.Entry;
import org.belex.supplier.Supplier;
import org.belex.util.Util;

public class Arrival implements Serializable {
	private static final long serialVersionUID = 1L;

	private String date;
	private String time;

	// list of all suppliers for the current date
	private Vector<Supplier> suppliers;
	
	private Supplier supplier = new Supplier();
	String supplierDocumentDescription = "";
	int supplierDocumentType;
	int supplierEntryProductIntegrity;
	int supplierEntryPackagingIntegrity;
	int supplierEntryDlcDdmValidity;
	int supplierEntryTemperatureValidity;

	private boolean saved = false;
	
	private Vector<Entry> savedEntries;
	private Entry entry;
	private boolean savedEntry;
	
	private String removeEntryId;
	private String searchValue;
	private String searchType;
	private String searchExactMatch;
	private String searchOnSupplier;

	// search planned suppliers
	private String searchSupplierDate;
	// select product
	private String productCodeSelected;
	private String unitIndexSelected;

	public Arrival() {
		init();
	}

	public void init(){
		getDate();
		getTime();
		setSuppliers(new Vector<Supplier>());
		supplier = new Supplier();
		setSaved(false);
		setSavedEntries(new Vector<Entry>());
		setEntry(new Entry());
		setSearchValue("");
		setSearchType("");
		setSearchSupplierDate("");
		setRemoveEntryId("0");
		setSavedEntry(false);
	} 
	public void prepareNewEntry(){
		setEntry(new Entry());
		setSavedEntry(false);
		setSearchValue("");
		setSearchType("");
		setSearchExactMatch("");
		setSearchOnSupplier("");
		
		setRemoveEntryId("0");
	}
	public String getDate() {
		if (date == null){
			setDate(Util.formatDate(null, null, "yyyyMMdd"));
		}
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		if (date == null){
			setTime(Util.formatDate(null, null, "kkmmss"));
		}
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}


	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public Vector<Entry> getSavedEntries() {
		return savedEntries;
	}

	public void setSavedEntries(Vector<Entry> savedEntries) {
		this.savedEntries = savedEntries;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public boolean isSavedEntry() {
		return savedEntry;
	}

	public void setSavedEntry(boolean savedEntry) {
		this.savedEntry = savedEntry;
	}

	public String getRemoveEntryId() {
		return removeEntryId;
	}

	public void setRemoveEntryId(String removeEntryId) {
		this.removeEntryId = removeEntryId;
	}

	public Vector<Supplier> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(Vector<Supplier> suppliers) {
		this.suppliers = suppliers;
	}

	public String getSearchExactMatch() {
		return searchExactMatch;
	}

	public void setSearchExactMatch(String searchExactMatch) {
		this.searchExactMatch = searchExactMatch;
	}

	public String getProductCodeSelected() {
		return productCodeSelected;
	}

	public void setProductCodeSelected(String productCodeSelected) {
		this.productCodeSelected = productCodeSelected;
	}

	public String getUnitIndexSelected() {
		return unitIndexSelected;
	}

	public void setUnitIndexSelected(String unitIndexSelected) {
		this.unitIndexSelected = unitIndexSelected;
	}

	public String getSearchSupplierDate() {
		if ( StringUtils.isEmpty(searchSupplierDate) ) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			setSearchSupplierDate(sdf.format(new Date()));
		}
		return searchSupplierDate;
		
	}

	public void setSearchSupplierDate(String searchSupplierDate) {
		this.searchSupplierDate = searchSupplierDate;
	}

	public String getSearchOnSupplier() {
		return searchOnSupplier;
	}

	public void setSearchOnSupplier(String searchOnSupplier) {
		this.searchOnSupplier = searchOnSupplier;
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

}
