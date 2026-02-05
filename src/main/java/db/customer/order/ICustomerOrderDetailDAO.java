package db.customer.order;

import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public interface ICustomerOrderDetailDAO {

	public List<CustomerOrderDetail> get(Set<Integer> orderNumbers);
	public List<CustomerOrderDetail> getByProductCode(String productCode, StringTokenizer orderNumbers);
	//public ArrayList<CustomerOrderDetail> getBySupplierCode(String supplierCode, Set<Integer> orderNumbers);
	public List<CustomerOrderDetail> get(int orderNumber, String orderLetter);
	public List<String> getLettersByOrderNumber(int orderNumber);
	public List<CustomerOrderDetail> get(String productCode, String supplierOrderNumbers, String supplierOrderLetters,String customerCodeOrder, int customerCodeOrderNumber);
	public boolean update(CustomerOrderDetail customerOrderDetail);
}