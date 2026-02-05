package db.entry;

public interface ISupplierEntryQuantityDAO {

	boolean save(SupplierEntryQuantity supplierEntryQuantity);
	boolean update(SupplierEntryQuantity supplierEntryQuantity);
	SupplierEntryQuantity getUnique(int supplierEntryId, String productCode);
}
