package db.entry;

import java.util.List;

public interface ISupplierEntryDAO {
	
	SupplierEntry getBySupplierEntryId(int supplierEntryId);
	SupplierEntry  getSupplierByDate(String supplierCode, String date);
	List<SupplierEntry> getSuppliersByDate(String date);
	boolean exists(SupplierEntry supplierEntry);
	boolean save(SupplierEntry supplierEntry);
	boolean update(SupplierEntry supplierEntry);

}