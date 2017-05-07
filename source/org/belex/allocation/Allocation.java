package org.belex.allocation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.belex.customer.CustomerOrder;

import db.supplier.Supplier;

public class Allocation  implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Allocation() {
	}

	boolean isUpdated = false;
	
	List<AllocationEntry> entries = new ArrayList<AllocationEntry>();
	List<Supplier> suppliers = new ArrayList<Supplier>();
	List<CustomerOrder> customerOrders = new ArrayList<CustomerOrder>();
	List<CustomerOrder> customerOrdersAllocateds = new ArrayList<CustomerOrder>();
	List<CustomerOrder> customerOrdersToAllocates = new ArrayList<CustomerOrder>();

	public List<Supplier> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}

	public String getSupplierName(String supplierCode) {
		String result = "";
		
		Iterator<Supplier> it = suppliers.iterator();
		while (it.hasNext()) {
			Supplier supplier = it.next();
			if (supplier.getSupplierCode().equals(supplierCode)) {
				result = supplier.getDescription();
				break;
			}
		}

		return result;
	}

	public void setEntries(List<AllocationEntry> entries) {
		this.entries = entries;
	}

	public List<AllocationEntry> getEntries() {
		return entries;
	}

	public List<AllocationEntry> getEntries(String supplierCode) {
		List<AllocationEntry> tempEntries = new ArrayList<AllocationEntry>();
		
		for (int i = 0 ; i < entries.size() ; i++) {
			if (entries.get(i).getSupplier().getSupplierCode().equals(supplierCode) ) {
				tempEntries.add(entries.get(i));
			}
		}
		
		return tempEntries;
	}

	public List<CustomerOrder> getCustomerOrders() {
		return customerOrders;
	}
	public void setCustomerOrders(List<CustomerOrder> customerOrders) {
		this.customerOrders = customerOrders;
	}
	
	//public void addCustomerOrder(CustomerOrder co) {
	//	this.customerOrders.add(co);
	//}
	public void addAllCustomerOrders(List<CustomerOrder> cos) {
		this.customerOrders.addAll(cos);
	}

	public List<CustomerOrder> getCustomerOrdersAllocateds() {
		return customerOrdersAllocateds;
	}

	public void setCustomerOrdersAllocateds(
			ArrayList<CustomerOrder> coas) {
		this.customerOrdersAllocateds = coas;
	}
	
	public void addCustomerOrdersAllocated(CustomerOrder coa) {
		this.customerOrdersAllocateds.add(coa);
	}

	public List<CustomerOrder> getCustomerOrdersToAllocates() {
		return customerOrdersToAllocates;
	}

	public void setCustomerOrdersToAllocates(
			List<CustomerOrder> cotas) {
		this.customerOrdersToAllocates = cotas;
	}
	public void addCustomerOrdersToAllocate(CustomerOrder cota) {
		this.customerOrdersToAllocates.add(cota);
	}

	public boolean isUpdated() {
		return isUpdated;
	}

	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}
}
