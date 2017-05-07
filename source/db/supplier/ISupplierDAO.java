package db.supplier;

import java.util.List;

public interface ISupplierDAO  {

	public List<Supplier> getAll();
	public Supplier getByCode(String supplierCode);
}