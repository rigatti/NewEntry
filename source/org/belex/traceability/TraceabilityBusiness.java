package org.belex.traceability;

import java.util.ArrayList;
import java.util.Vector;

import org.belex.customer.Customer;
import org.belex.customer.CustomerEntry;
import org.belex.customer.CustomerOrder;
import org.belex.entry.Entry;
import org.belex.fly.Fly;
import org.belex.product.Product;
import org.belex.product.Product.Unit;
import org.belex.requestparams.RequestParams;
import org.belex.supplier.Supplier;

import db.entry.treated.ITreatedEntryDAO;
import db.entry.treated.ITreatedEntryDetailDAO;
import db.entry.treated.ITreatedEntryDetailDestinationDAO;
import db.entry.treated.TreatedEntry;
import db.entry.treated.TreatedEntryDetail;
import db.entry.treated.TreatedEntryDetailDestination;
import db.supplier.ISupplierDAO;

public class TraceabilityBusiness implements ITraceabilityBusiness {
	ITreatedEntryDAO treatedEntryDAO;
	ITreatedEntryDetailDAO treatedEntryDetailDAO;
	ITreatedEntryDetailDestinationDAO treatedEntryDetailDestinationDAO;
	ISupplierDAO supplierDAO;
	
	public Traceability searchEntry(Traceability traceability, RequestParams requestParams) {
		Vector<Entry> entries = new Vector<Entry>();

		if (traceability.getSuppliers().size() == 0) {
			traceability.setSuppliers(supplierDAO.getAll());
		}

		ArrayList<TreatedEntry> tes = treatedEntryDAO.get(
									 requestParams.getTraceProductCode().toUpperCase(), 
									 requestParams.getTraceEan(), 
									 requestParams.getTraceLot(), 
									 requestParams.getTraceValidityDate() );

		for (TreatedEntry te : tes) {
			
			TreatedEntryDetail ted = treatedEntryDetailDAO.get(te.getTreatedEntryId());
			ArrayList<TreatedEntryDetailDestination> tedds = 
				treatedEntryDetailDestinationDAO.getAll(ted.getTreatedEntryDetailId());
			
			// build the entry (+ flight and product)
			Product product = new Product(te.getProductCode());
			product.setLotNumber(te.getLotNumber());
			product.setValidityDate(te.getValidityDate());
			product.setAdditionalData(ted.getAdditionalData());
			product.setDescription(ted.getDescription());

			Unit unit = product. new Unit();
			unit.setConditionnement(ted.getUnitConditionnement());
			unit.setNumber(ted.getNumberOfUnit());
			unit.setEan(te.getEan());
			product.addUnit(unit);
			
			Entry entry = new Entry();
			entry.setArrivalDate(te.getArrivalDate());
			entry.setSupplierDocumentDescription(ted.getSupplierDocumentDescription());
			entry.setSupplierDocumentType(ted.getSupplierDocumentType());
			entry.setOrderNumbers(ted.getSupplierOrderNumbers());
			entry.setOrderLetter(ted.getSupplierOrderLetters());
			entry.setNumberOfProduct(ted.getNumberOfProduct());
			entry.setProduct(product);

			Supplier s = new Supplier();
			s.setSupplierCode(te.getSupplierCode());
			s.setSupplierName(traceability.getSupplierName(s.getSupplierCode()));
			entry.setSupplier(s);
			
			for (TreatedEntryDetailDestination tedd : tedds) {
				
				Customer customer = new Customer(tedd.getCustomerCode());
				customer.setStockCustomer(tedd.getStockFlag() == 0?false:true);
				customer.setSupplierReturnsCustomer(tedd.getSupplierReturnsFlag() == 0?false:true);
				customer.setAllocationDate(tedd.getAllocationDate());
				
				CustomerEntry customerEntry = new CustomerEntry();
				customerEntry.setNumberOfUnit(tedd.getNumberOfUnit());
				customer.setCustomerEntry(customerEntry);

				if ( ! (customer.isStockCustomer() || customer.isSupplierReturnsCustomer()) ) {
					Fly fly = new Fly();
					fly.setAirportCode(tedd.getAirportCode());
					fly.setFlyNumber(tedd.getFlight());
					fly.setLtaNumber(tedd.getLta());
					
					CustomerOrder co = new CustomerOrder();
					co.setFly(fly);
					
					customer.setCustomerOrder(co);
				}
	
				entry.addCustomer(customer);
			}

			entries.add(entry);

		}

		traceability.setEntries(entries);

		return traceability;
	}

	public void setSupplierDAO(ISupplierDAO supplierDAO) {
		this.supplierDAO = supplierDAO;
	}

	public void setTreatedEntryDAO(ITreatedEntryDAO treatedEntryDAO) {
		this.treatedEntryDAO = treatedEntryDAO;
	}

	public void setTreatedEntryDetailDAO(
			ITreatedEntryDetailDAO treatedEntryDetailDAO) {
		this.treatedEntryDetailDAO = treatedEntryDetailDAO;
	}

	public void setTreatedEntryDetailDestinationDAO(
			ITreatedEntryDetailDestinationDAO treatedEntryDetailDestinationDAO) {
		this.treatedEntryDetailDestinationDAO = treatedEntryDetailDestinationDAO;
	}

}
