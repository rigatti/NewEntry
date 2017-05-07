package db.customer;

import java.util.List;

public interface ICustomerDAO {

	public Customer get(String customerCode);
	public String getAddressFurniture(String customerCode);
	public List<?> getCustomersToExport();
	public List<?> getCustomerToExport(String email);
	
}