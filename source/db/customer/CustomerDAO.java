package db.customer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CustomerDAO extends HibernateDaoSupport implements ICustomerDAO {

	private static final Category log = Logger.getLogger(Customer.class);

	public Customer get(String customerCode) {
		Customer customer = new Customer(customerCode);
		
		log.debug("Get customer address of furniture:" + customerCode);
		
		try {

			boolean itemfound = false;
			String sql = "from Customer where code='" + customerCode + "'";
			
			Object objList = getHibernateTemplate().find(sql); 
			
			if (objList instanceof ArrayList) {

				ArrayList<Customer> listOfCustomers = (ArrayList<Customer>) objList;

				if (listOfCustomers.size() > 0) {
					itemfound = true;
					// List size should be 1
					if (listOfCustomers.size() == 1) {
						customer = listOfCustomers.get(0);
					} else {
						log.error("List of customer is bigger than 1 : " + sql);
					}
				}
			}

			if (! itemfound) {
				log.debug("No customer found");
			}

			log.debug("end of get");

		} catch (Exception e) {
			log.error("Error while getting customer address of furniture:" + customerCode, e);
		}
		return customer;
	}

	public String getAddressFurniture(String customerCode) {
		Customer c = get(customerCode);

		if (c!= null && c.getAddressFurniture() != null) {
			return c.getAddressFurniture();
		} else {
			return "";
		}
	}
	public List<?> getCustomersToExport() {
		List<?> result = null;
		try {
			log.debug("Finding CUSTOMER to export");
					
			String sql = "FROM " +
					" Customer AS c " + 
					" WHERE (c.email != 'NULL')";

			Object obj = getHibernateTemplate().find(sql);
			if (obj instanceof List<?>) {
				result = (List<?>)obj;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return result;
	}
	
	public List<?> getCustomerToExport(String email) {
		List<?> result = null;
		try {
			log.debug("Finding CUSTOMER to export with email : " + email);
					
			String sql = "FROM " +
					" Customer AS c " + 
					" WHERE (c.email = '" + email + "')";

			Object obj = getHibernateTemplate().find(sql);
			if (obj instanceof List<?>) {
				result = (List<?>)obj;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return result;
	}
}