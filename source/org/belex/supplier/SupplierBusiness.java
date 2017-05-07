package org.belex.supplier;

import java.util.ArrayList;
import java.util.Vector;

import db.supplier.ISupplierDAO;

public class SupplierBusiness implements ISupplierBusiness {
	
	ISupplierDAO supplierDAO;

	public Vector<Supplier> getSuppliers() {

		Vector<Supplier> result = new Vector<Supplier>();
		
		ArrayList<db.supplier.Supplier> suppliers = (ArrayList<db.supplier.Supplier>) supplierDAO.getAll();
		
		for (db.supplier.Supplier supplier : suppliers) {
			result.add(new Supplier(supplier.getSupplierCode(), supplier.getDescription()));
		}
		
		return result;
	}

	public void setSupplierDAO(ISupplierDAO iSupplierDAO) {
		supplierDAO = iSupplierDAO;
	}
}
