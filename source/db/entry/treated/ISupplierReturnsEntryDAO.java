package db.entry.treated;

import org.hibernate.Session;

public interface ISupplierReturnsEntryDAO {

	public SupplierReturnsEntry get(int treatedEntryDetailDestinationId, Session session);
	public boolean update(SupplierReturnsEntry supplierReturnsEntry, Session session);
	public boolean insert(SupplierReturnsEntry supplierReturnsEntry, Session session);
	public boolean updateProductCode(String oldProductCode, String newProductCode, Session session);
	
}