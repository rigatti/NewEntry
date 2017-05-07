package db.entry;

import org.hibernate.Session;

public interface ISupplierEntryDetailDAO {

	boolean save(SupplierEntryDetail supplierEntryDetail, Session session);
}