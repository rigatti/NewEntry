package org.belex.traceability;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.belex.entry.Entry;

import db.supplier.Supplier;

public class Traceability implements Serializable {
	private static final long serialVersionUID = 1L;

	private Vector<Entry> entries;
	
	private String searchProductCode;
	private String searchEan;
	private String searchLotNumber;
	private String searchValidityDate;
	
	List<Supplier> suppliers = new ArrayList<Supplier>();

	public Traceability() {
		init();
	}

	public void init(){
		setEntries(new Vector<Entry>());
		setSearchProductCode("");
		setSearchEan("");
		setSearchLotNumber("");
		setSearchValidityDate("");
	} 

	public Vector<Entry> getEntries() {
		return entries;
	}

	public void setEntries(Vector<Entry> entries) {
		this.entries = entries;
	}

	public String getSearchEan() {
		return searchEan;
	}

	public void setSearchEan(String searchEan) {
		this.searchEan = searchEan;
	}

	public String getSearchLotNumber() {
		return searchLotNumber;
	}

	public void setSearchLotNumber(String searchLotNumber) {
		this.searchLotNumber = searchLotNumber;
	}

	public String getSearchProductCode() {
		return searchProductCode;
	}

	public void setSearchProductCode(String searchProductCode) {
		this.searchProductCode = searchProductCode;
	}

	public String getSearchValidityDate() {
		return searchValidityDate;
	}

	public void setSearchValidityDate(String searchValidityDate) {
		this.searchValidityDate = searchValidityDate;
	}

	public List<Supplier> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}
	
	public String getSupplierName(String supplierCode) {
		String result = "";
		for (Supplier supplier : suppliers) {
			if (supplier.getSupplierCode().equals(supplierCode)) {
				result = supplier.getDescription();
				break;
			}
		}
		return result;
		
	}
	
}
