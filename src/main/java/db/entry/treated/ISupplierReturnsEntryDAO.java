package db.entry.treated;

public interface ISupplierReturnsEntryDAO {

	public SupplierReturnsEntry get(int treatedEntryDetailDestinationId);
	public boolean update(SupplierReturnsEntry supplierReturnsEntry);
	public boolean insert(SupplierReturnsEntry supplierReturnsEntry);
	public boolean updateProductCode(String oldProductCode, String newProductCode);
	
}