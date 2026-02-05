package org.belex.supplier;

import db.supplier.ISupplierDAO;

import java.util.List;
import java.util.Vector;

public class SupplierBusiness implements ISupplierBusiness {
	
	ISupplierDAO supplierDAO;

	public Vector<Supplier> getSuppliers() {

		Vector<Supplier> result = new Vector<>();
		
		List<db.supplier.Supplier> suppliers = supplierDAO.getAll();
		
		for (db.supplier.Supplier supplier : suppliers) {
			result.add(new Supplier(supplier.getSupplierCode(), supplier.getDescription()));
		}
		
		return result;
	}

	public void setSupplierDAO(ISupplierDAO iSupplierDAO) {
		supplierDAO = iSupplierDAO;
	}
}
