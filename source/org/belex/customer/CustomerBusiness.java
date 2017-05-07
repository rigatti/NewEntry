package org.belex.customer;

import java.util.Vector;

import org.belex.entry.Entry;
import org.belex.traceability.Traceability;

import db.customer.ICustomerDAO;

public class CustomerBusiness implements ICustomerBusiness {
	
	ICustomerDAO customerDAO;
	
	public Traceability completeCustomer(Traceability traceability) {

		Vector<Entry> entries = traceability.getEntries();
		for (int i = 0; i < entries.size(); i++) {
			Vector<Customer> customers = entries.get(i).getCustomers();
			for (Customer customer : customers) {
				// temp solution because pbl with extend class (db.customer and org.belex....)
				db.customer.Customer cDB = customerDAO.get(customer.getCode());
				customer.setAddress(cDB.getAddress());
				customer.setAddressFees(cDB.getAddressFees());
				customer.setAddressFurniture(cDB.getAddressFurniture());
				customer.setAirportCode(cDB.getAirportCode());
				customer.setCode(cDB.getCode());
				customer.setGroupment(cDB.getGroupment());
				customer.setName(cDB.getName());
			}
			entries.get(i).setCustomers(customers);
		}
		traceability.setEntries(entries);

		return traceability;
	}


	public void setCustomerDAO(ICustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}
}
