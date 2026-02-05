package db.customer;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Slf4j
public class CustomerDAO implements ICustomerDAO {

	@Autowired
	private final SessionFactory sessionFactory;

	public CustomerDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Transactional(readOnly = true)
	public Customer get(String customerCode) {
		Customer customer = new Customer(customerCode);
		
		log.debug("Get customer address of furniture:" + customerCode);
		
		try {

			return currentSession().createQuery("from Customer where code=:customerCode", Customer.class)
					.setParameter("customerCode", customerCode)
					.getSingleResult();

		} catch (Exception e) {
			log.error("Error while getting customer address of furniture:" + customerCode, e);
		}
		return customer;
	}

	@Transactional(readOnly = true)
	public String getAddressFurniture(String customerCode) {
		Customer c = get(customerCode);

		if (c != null && c.getAddressFurniture() != null) {
			return c.getAddressFurniture();
		} else {
			return "";
		}
	}

	@Transactional(readOnly = true)
	public List<?> getCustomersToExport() {
		List<?> result = null;
		try {
			log.debug("Finding CUSTOMER to export");


			return currentSession().createQuery("from Customer AS c WHERE (c.email != 'NULL')", Customer.class)
					.list();

		} catch (Exception e) {
			log.error("Error while getting customers with email not null", e);
		}
		
		return result;
	}

	@Transactional(readOnly = true)
	public List<?> getCustomerToExport(String email) {
		List<?> result = null;
		try {
			log.debug("Finding CUSTOMER to export with email : " + email);


			return currentSession().createQuery("from Customer AS c WHERE (c.email =:email)", Customer.class)
					.setParameter("email", email)
					.list();

		} catch (Exception e) {
			log.error("Error while getting customers with email :" + email, e);
		}
		
		return result;
	}
}