package db.entry;

import java.util.ArrayList;

import org.hibernate.Session;

public interface ISupplierEntryDAO {
	
	SupplierEntry getBySupplierEntryId(int supplierEntryId);
	SupplierEntry getBySupplierEntryId(int supplierEntryId, Session session);
	SupplierEntry  getSupplierByDate(String supplierCode, String date);
	ArrayList<SupplierEntry> getSuppliersByDate(String date);
	boolean exist(SupplierEntry supplierEntry, Session session);
	boolean save(SupplierEntry supplierEntry, Session session);
	boolean update(SupplierEntry supplierEntry, Session session);

}