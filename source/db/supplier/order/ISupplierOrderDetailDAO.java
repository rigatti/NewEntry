package db.supplier.order;

import java.util.ArrayList;

public interface ISupplierOrderDetailDAO {
	public ArrayList<SupplierOrderDetail> get(String supplierCode, int orderNumber);
	public ArrayList<SupplierOrderDetail> get(String supplierCode, int orderNumber, String productCode);
}