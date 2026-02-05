package org.belex.traceability;

import db.entry.treated.*;
import db.supplier.ISupplierDAO;
import org.belex.customer.Customer;
import org.belex.customer.CustomerEntry;
import org.belex.customer.CustomerOrder;
import org.belex.entry.Entry;
import org.belex.fly.Fly;
import org.belex.product.Product;
import org.belex.product.Product.Unit;
import org.belex.requestparams.RequestParams;
import org.belex.supplier.Supplier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TraceabilityBusiness implements ITraceabilityBusiness {
	ITreatedEntryDAO treatedEntryDAO;
	ITreatedEntryDetailDAO treatedEntryDetailDAO;
	ITreatedEntryDetailDestinationDAO treatedEntryDetailDestinationDAO;
	ISupplierDAO supplierDAO;
	
	public Traceability searchSupplierEntry(Traceability traceability, RequestParams requestParams) {
		Traceability traceabilityResult = execute(traceability, requestParams, getForSupplierTracability(requestParams));
		
		Vector<Entry> entries = traceabilityResult.getEntries();
		Map<String, Entry> entriesByDate = new HashMap();
		Map<String, Integer> numberOfEntriesByDate = new HashMap();
		for (Entry entry : entries) {
			String currentDate = entry.getArrivalDate();
			entriesByDate.put(currentDate, entry);
			numberOfEntriesByDate.put(currentDate, numberOfEntriesByDate.containsKey(currentDate)?numberOfEntriesByDate.get(currentDate) + 1 : 1);
		}
		
		// overwrite the entries with new results
		entries = new Vector<>();
		for (String arrivalDate : entriesByDate.keySet()) {
			Entry entry = entriesByDate.get(arrivalDate);
			entry.setSupplierEntryNumberOfProducts(numberOfEntriesByDate.get(arrivalDate));			
			entry.setCustomers(new Vector<>());
			entry.setNumberOfProduct(0);
			entry.setOrderLetter("");
			entry.setOrderNumbers("");
			entry.setProduct(null);
			entries.add(entry);
		}
		traceabilityResult.setEntries(entries);
		return traceabilityResult;
	}
	
	public Traceability searchProductEntry(Traceability traceability, RequestParams requestParams) {
		return execute(traceability, requestParams, getForProductTracability(requestParams));
	}
	
	private Traceability execute(Traceability traceability, RequestParams requestParams, List<TreatedEntry> tes) {

		Vector<Entry> entries = new Vector<Entry>();

		if (traceability.getSuppliers().size() == 0) {
			traceability.setSuppliers(supplierDAO.getAll());
		}
		
		for (TreatedEntry te : tes) {
			
			TreatedEntryDetail ted = treatedEntryDetailDAO.get(te.getTreatedEntryId());
			List<TreatedEntryDetailDestination> tedds =
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
			entry.setSupplierEntryProductIntegrity(ted.getSupplierEntryProductIntegrity());
			entry.setSupplierEntryPackagingIntegrity(ted.getSupplierEntryPackagingIntegrity());
			entry.setSupplierEntryDlcDdmValidity(ted.getSupplierEntryDlcDdmValidity());
			entry.setSupplierEntryTemperatureValidity(ted.getSupplierEntryTemperatureValidity());
			entry.setSupplierEntryCommentOnQuality(ted.getSupplierEntryCommentOnQuality());

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

	private List<TreatedEntry> getForProductTracability(RequestParams requestParams) {
		return treatedEntryDAO.get(
									 requestParams.getTraceProductCode().toUpperCase(), 
									 requestParams.getTraceEan(), 
									 requestParams.getTraceLot(), 
									 requestParams.getTraceValidityDate() );
	}

	private List<TreatedEntry> getForSupplierTracability(RequestParams requestParams) {
		return treatedEntryDAO.getByDateRange(
									 requestParams.getTraceEntrySupplierCode().toUpperCase(),
									 requestParams.getTraceEntryStartDate(),
									 requestParams.getTraceEntryEndDate());
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
