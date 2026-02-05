package db.supplier.order;

import java.util.List;

public interface ISupplierOrderDetailDAO {
	public List<SupplierOrderDetail> get(String supplierCode, int orderNumber);
	public List<SupplierOrderDetail> get(String supplierCode, int orderNumber, String productCode);
}