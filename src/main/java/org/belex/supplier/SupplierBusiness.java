package org.belex.supplier;

import db.supplier.ISupplierDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Vector;

@Service("supplierBusiness")
public class SupplierBusiness implements ISupplierBusiness {

	private final ISupplierDAO supplierDAO;

	public SupplierBusiness(ISupplierDAO supplierDAO) {
		this.supplierDAO = supplierDAO;
	}

	public Vector<Supplier> getSuppliers() {

		Vector<Supplier> result = new Vector<>();
		
		List<db.supplier.Supplier> suppliers = supplierDAO.getAll();
		
		for (db.supplier.Supplier supplier : suppliers) {
			result.add(new Supplier(supplier.getSupplierCode(), supplier.getDescription()));
		}
		
		return result;
	}
}
