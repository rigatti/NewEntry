package db.entry;

import org.hibernate.Session;

public interface ISupplierEntryQuantityDAO {

	boolean save(SupplierEntryQuantity supplierEntryQuantity, Session session);
	boolean update(SupplierEntryQuantity supplierEntryQuantity, Session session);
	SupplierEntryQuantity getUnique(int supplierEntryId, String productCode,Session session);
}
