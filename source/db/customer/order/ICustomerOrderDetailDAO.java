package db.customer.order;

import java.util.ArrayList;
import java.util.Set;
import java.util.StringTokenizer;

import org.hibernate.Session;

public interface ICustomerOrderDetailDAO {

	public ArrayList<CustomerOrderDetail> get(Set<Integer> orderNumbers);
	public ArrayList<CustomerOrderDetail> getByProductCode(String productCode, StringTokenizer orderNumbers);
	//public ArrayList<CustomerOrderDetail> getBySupplierCode(String supplierCode, Set<Integer> orderNumbers);
	public ArrayList<CustomerOrderDetail> get(int orderNumber, String orderLetter);
	public ArrayList<String> getLettersByOrderNumber(int orderNumber);
	public ArrayList<CustomerOrderDetail> get(String productCode, String supplierOrderNumbers, String supplierOrderLetters,String customerCodeOrder, int customerCodeOrderNumber, Session session);
	public boolean update(CustomerOrderDetail customerOrderDetail, Session session);
}