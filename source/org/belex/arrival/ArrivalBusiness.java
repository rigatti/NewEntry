package org.belex.arrival;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.belex.customer.Customer;
import org.belex.customer.CustomerEntry;
import org.belex.customer.CustomerOrder;
import org.belex.entry.Entry;
import org.belex.fly.Fly;
import org.belex.product.Product;
import org.belex.product.Product.Unit;
import org.belex.requestparams.RequestParams;
import org.belex.supplier.Supplier;
import org.belex.supplier.Supplier.Order;
import org.belex.util.Constants;
import org.belex.util.Util;
import org.belex.util.UtilityBean;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.webflow.execution.Event;

import db.customer.ICustomerDAO;
import db.customer.order.CustomerOrderDetail;
import db.customer.order.ICustomerOrderDetailDAO;
import db.entry.IPlanningDAO;
import db.entry.ISupplierEntryDAO;
import db.entry.ISupplierEntryDetailDAO;
import db.entry.ISupplierEntryQuantityDAO;
import db.entry.Planning;
import db.entry.SupplierEntry;
import db.entry.SupplierEntryDetail;
import db.entry.SupplierEntryQuantity;
import db.entry.treated.ITreatedEntryDAO;
import db.entry.treated.ITreatedEntryDetailDAO;
import db.entry.treated.ITreatedEntryDetailDestinationDAO;
import db.entry.treated.TreatedEntry;
import db.entry.treated.TreatedEntryDetail;
import db.entry.treated.TreatedEntryDetailDestination;
import db.entry.treated.temp.ITreatedEntryCustomerTempDAO;
import db.entry.treated.temp.ITreatedEntryTempDAO;
import db.entry.treated.temp.TreatedEntryCustomerTemp;
import db.entry.treated.temp.TreatedEntryTemp;
import db.prepare.IPackageDetailDAO;
import db.prepare.IPackagingDAO;
import db.prepare.IPackagingDetailDAO;
import db.prepare.IPrepareOrderDAO;
import db.prepare.IPrepareOrderDetailDAO;
import db.prepare.PackageDetail;
import db.prepare.Packaging;
import db.prepare.PackagingDetail;
import db.prepare.PrepareOrder;
import db.product.Conditioning;
import db.product.IConditioningDAO;
import db.product.IProductDAO;
import db.supplier.ISupplierDAO;
import db.supplier.order.ISupplierOrderDetailDAO;
import db.supplier.order.SupplierOrderDetail;

public class ArrivalBusiness extends HibernateDaoSupport implements IArrivalBusiness {
	
	private static final Category log = Logger.getLogger(ArrivalBusiness.class);

	public static final int STORE_ERROR = -1;
	public static final int STORE_WARNING = 0;
	public static final int STORE_UPDATE = 9;
	public static final int STORE_SUCCESS = 1;
	
	ISupplierDAO supplierDAO;
	ICustomerOrderDetailDAO customerOrderDetailDAO;
	ISupplierOrderDetailDAO supplierOrderDetailDAO;
	ISupplierEntryDAO supplierEntryDAO;
	ISupplierEntryDetailDAO supplierEntryDetailDAO;
	ISupplierEntryQuantityDAO supplierEntryQuantityDAO;
	IPackagingDAO packagingDAO;
	ICustomerDAO customerDAO;
	IPrepareOrderDAO prepareOrderDAO;
	IPrepareOrderDetailDAO prepareOrderDetailDAO;
	IPackageDetailDAO packageDetailDAO;
	IPackagingDetailDAO packagingDetailDAO;
	IPlanningDAO planningDAO;
	IProductDAO productDAO;
	IConditioningDAO conditioningDAO;
	ITreatedEntryTempDAO treatedEntryTempDAO;
	ITreatedEntryCustomerTempDAO treatedEntryCustomerTempDAO;
	ITreatedEntryDAO treatedEntryDAO;
	ITreatedEntryDetailDAO treatedEntryDetailDAO;
	ITreatedEntryDetailDestinationDAO treatedEntryDetailDestinationDAO;

	public String storeEntry(Arrival arrival) {
		return storeEntry(arrival, "0");
	}

	public String forceStoreEntry(Arrival arrival) {
		return storeEntry(arrival, "1");
	}

	public String storeEntry(Arrival arrival, String forceStoreEntry) {
		int result = STORE_ERROR;
		boolean forceStore = forceStoreEntry.equals("1");

		if ( ! arrival.isSavedEntry()) {

			result = ArrivalBusiness.STORE_SUCCESS;

			Product product = arrival.getEntry().getProduct();
			UtilityBean utb = new UtilityBean();
			Unit unit = utb.getSelectedUnit(product);
			
			// search requested entries (by customer)
			Vector<Entry> requestedEntries = arrival.getSupplier().getRequestedEntries();
			int nbrToTransfer = 0;
			for (int i=0; i < requestedEntries.size() && nbrToTransfer == 0; i++ ) { 
				Entry currentRequestedEntry = requestedEntries.get(i);
				nbrToTransfer = Util.containsSameUnit(arrival.getSavedEntries(), currentRequestedEntry); 
			}

			// get total conditionnement received for the current entry
			int nbrReceived = Integer.valueOf(arrival.getEntry().getNumberOfProduct()) * Integer.valueOf((utb.getSelectedUnit(arrival.getEntry().getProduct()).getNumber()));
			
			// search existing item in temp table 
			TreatedEntryTemp treatedEntryTemp = 
				treatedEntryTempDAO.getByDateSupplierCodeProductCodeAndUnit(arrival.getDate(), arrival.getSupplier().getSupplierCode(), product.getProductCode(), unit.getNumber(), unit.getConditionnement());

			boolean newEntry = true; 

			if (treatedEntryTemp != null) {
				// the same product with same unit has already been inserted
				newEntry = false;

				if ( ! forceStore ) {
					// display warning and request if force store entry needed
					result = ArrivalBusiness.STORE_WARNING;
				} else {
					// update the number of product this new entry
					
					int storedNumberOfProduct = treatedEntryTemp.getNumberOfProduct();
					float storedNbrReceived = treatedEntryTemp.getNumberOfProductReceived();
					
					try {
						
						int newNumberOfProduct = arrival.getEntry().getNumberOfProduct();
						
						// do not add nbrToTransfer because it has already been added in the search procedure (Util.containsSameUnit)							
						treatedEntryTemp.setNumberOfProduct(storedNumberOfProduct + newNumberOfProduct);
						treatedEntryTemp.setNumberOfProductReceived(nbrReceived + storedNbrReceived);
						treatedEntryTemp.setEan(unit.getEan());
						
						boolean isUpdated = treatedEntryTempDAO.update(treatedEntryTemp);

						if (isUpdated) {
							result = ArrivalBusiness.STORE_UPDATE;
						}
		
					} catch (NumberFormatException nfe){
						newEntry = true;
					}
				}

			}
			
			if (newEntry) {

				Session currentSession = getSession();
				Transaction trx = currentSession.beginTransaction();
				
				treatedEntryTemp = new TreatedEntryTemp();
				treatedEntryTemp.setSupplierCode(arrival.getSupplier().getSupplierCode());
				treatedEntryTemp.setArrivalDate(arrival.getDate());
				treatedEntryTemp.setArrivalTime(arrival.getTime());
				treatedEntryTemp.setSupplierProductCode(unit.getSupplierProductCode());
				treatedEntryTemp.setProductCode(arrival.getEntry().getProduct().getProductCode());
				treatedEntryTemp.setEan(unit.getEan());
				treatedEntryTemp.setUnitConditionnement(Util.formatForDB(unit.getConditionnement()));
				treatedEntryTemp.setNumberOfUnit(unit.getNumber());
				treatedEntryTemp.setNumberOfProduct(arrival.getEntry().getNumberOfProduct());
				treatedEntryTemp.setLotNumber(Util.formatForDB(arrival.getEntry().getProduct().getLotNumber()));
				treatedEntryTemp.setSupplierOrderNumbers(arrival.getEntry().getOrderNumbers());
				treatedEntryTemp.setSupplierOrderLetters(arrival.getEntry().getOrderLetter());
				treatedEntryTemp.setValidityDate(arrival.getEntry().getProduct().getValidityDate());
				treatedEntryTemp.setAdditionalData(Util.formatForDB(arrival.getEntry().getProduct().getAdditionalData()));
				treatedEntryTemp.setDescription(Util.formatForDB(arrival.getEntry().getProduct().getDescription(), 50));
				treatedEntryTemp.setSupplierDocumentDescription(Util.formatForDB(arrival.getSupplierDocumentDescription(), 50));
				treatedEntryTemp.setSupplierDocumentType(arrival.getSupplierDocumentType());
				treatedEntryTemp.setNumberOfProductReceived(nbrReceived);
				treatedEntryTemp.setStockMovement(StringUtils.equals(arrival.getSupplier().getSupplierCode(), Constants.SUPPLIER_STOCK)?1:0);
				treatedEntryTemp.setSupplierEntryProductIntegrity(arrival.getSupplierEntryProductIntegrity());
				treatedEntryTemp.setSupplierEntryPackagingIntegrity(arrival.getSupplierEntryPackagingIntegrity());
				treatedEntryTemp.setSupplierEntryDlcDdmValidity(arrival.getSupplierEntryDlcDdmValidity());
				treatedEntryTemp.setSupplierEntryTemperatureValidity(arrival.getSupplierEntryTemperatureValidity());

				if (treatedEntryTempDAO.save(treatedEntryTemp, currentSession)) {

					int treatedEntryTempId =  treatedEntryTemp.getId();

					for (Customer customer : arrival.getEntry().getCustomers()) {

						//if ( customer.getCustomerEntry().getAssignedSupplierCode().equals(arrival.getSupplier().getSupplierCode()) ) {

							TreatedEntryCustomerTemp tect = new TreatedEntryCustomerTemp();
							tect.setTreatedEntryTempId(treatedEntryTempId);
							tect.setCustomerCode(customer.getCode());
							tect.setCustomerOrderCode(customer.getCustomerOrder().getOrderCode());
							tect.setCustomerOrderCodeNumber(customer.getCustomerOrder().getOrderNumber());
							tect.setPackagingFromBasket(customer.getCustomerEntry().getFromBasketNumber());
							tect.setPackagingToBasket(customer.getCustomerEntry().getToBasketNumber());
							tect.setCustomerNumberOfUnitRequested(customer.getCustomerEntry().getTotalUnit());
							tect.setAirportCode(customer.getCustomerOrder().getFly().getAirportCode());
							tect.setFlight(customer.getCustomerOrder().getFly().getFlyNumber());
							tect.setLta(customer.getCustomerOrder().getFly().getLtaNumber());
							
							if ( ! treatedEntryCustomerTempDAO.save(tect, currentSession)) {
								result = STORE_ERROR;
							}
						//}
					}
				} else {
					result = STORE_ERROR;
				}
				
				if (result == STORE_SUCCESS) {
					
					trx.commit();
					
					arrival.setSavedEntry(true);
				
				} else {
					
					trx.rollback();
				}
			}
		}

		if (result == STORE_ERROR) {
			return "error";
		} else if (result == STORE_WARNING) {
			return "warning";
		} else if (result == STORE_UPDATE) {
			return "update";
		} else {
			return "success";
		}
	}

	public Event modifyEntry(Arrival arrival, RequestParams requestParams) {
		try {
			int entryId = requestParams.getEntryId();
			Vector<Entry> entries = arrival.getSavedEntries();
			Entry entry = entries.get(entryId);
			entry.setNumberOfProduct(requestParams.getEntryNumberOfProduct());
			entry.getProduct().setValidityDate(requestParams.getEntryValidityDate());
			entry.getProduct().setLotNumber(requestParams.getEntryLotNumber());
	
			ArrayList<TreatedEntryTemp> treatedEntryTemps = treatedEntryTempDAO.get(arrival.getSupplier().getSupplierCode(), arrival.getDate(), entry.getProduct().getProductCode(), null);
			
			TreatedEntryTemp treatedEntryTemp = null;

			if (treatedEntryTemps.size() == 1) {
				treatedEntryTemp = treatedEntryTemps.get(0);
			} else {
				for (TreatedEntryTemp tet: treatedEntryTemps) {
					for (Unit u : entry.getProduct().getUnits()) {
						if (u.getConditionnement().equals(tet.getUnitConditionnement())) {
							treatedEntryTemp = tet;
							break;
						}
					}
					if (treatedEntryTemp != null) {
						break;
					}
				}
			}
			
			if (treatedEntryTemp != null) {
	
				treatedEntryTemp.setNumberOfProduct(entry.getNumberOfProduct());
				treatedEntryTemp.setLotNumber(entry.getProduct().getLotNumber());
				treatedEntryTemp.setValidityDate(entry.getProduct().getValidityDate());
	
				treatedEntryTempDAO.update(treatedEntryTemp);

			} else {

				log.error(getClass().getName() + ".modifyEntry ERROR " +
						"getSupplierCode():" + entry.getSupplier().getSupplierCode() + " " + 
						"date:" + entry.getArrivalDate() + " " + 
						"productCode:" + entry.getProduct().getProductCode() + " " + 
						"size:" + treatedEntryTemps.size() + " AND  treatedEntryTemp == null");
			}

		} catch (Exception e) {

			log.error(getClass().getName() + ".modifyEntry ERROR while " +
					"getting requestParams entryId : " + requestParams.getEntryId() + 
					" for arrival.getSavedEntries().size=" + arrival.getSavedEntries().size() + " supplier:" + arrival.getSupplier().getSupplierCode() + " " + arrival.getDate(), e);
		}
		return new Event("modifyEntry", "success");

	}

	public Event removeEntry(Arrival arrival, RequestParams requestParams) {

		int entryId = requestParams.getEntryId();
		Vector<Entry> entries = arrival.getSavedEntries();
		Entry entry = entries.get(entryId);

		ArrayList<TreatedEntryTemp> treatedEntryTemps = treatedEntryTempDAO.get(arrival.getSupplier().getSupplierCode(), arrival.getDate(), entry.getProduct().getProductCode(), null);
		
		TreatedEntryTemp treatedEntryTemp = null;

		if (treatedEntryTemps.size() == 1) {
			treatedEntryTemp = treatedEntryTemps.get(0);
		} else {
			for (TreatedEntryTemp tet: treatedEntryTemps) {
				for (Unit u : entry.getProduct().getUnits()) {
					if (u.getConditionnement().equals(tet.getUnitConditionnement())) {
						treatedEntryTemp = tet;
						break;
					}
				}
				if (treatedEntryTemp != null) {
					break;
				}
			}
		}
		
		if (treatedEntryTemp != null) {

			Session session = getSession();
			Transaction transaction = session.getTransaction();
			transaction.begin();

			treatedEntryCustomerTempDAO.deleteAllByEntryId(treatedEntryTemp.getId(), session);
			treatedEntryTempDAO.delete(treatedEntryTemp, session);

			transaction.commit();


		} else {

			log.error(getClass().getName() + ".removeEntry ERROR " +
					"getSupplierCode():" + entry.getSupplier().getSupplierCode() + " " + 
					"date:" + entry.getArrivalDate() + " " + 
					"productCode:" + entry.getProduct().getProductCode() + " " + 
					"size:" + treatedEntryTemps.size() + " AND  treatedEntryTemp == null");
		}

		entries.remove(requestParams.getEntryId());

		if (entries.size() > 0) {
			return new Event("removeEntry", "existEntry");
		}

		return new Event("removeEntry", "noMoreEntry");
	}

	public Event saveEntry(Arrival arrival) {

		ArrayList<TreatedEntryTemp> treatedEntryTemps = treatedEntryTempDAO.getBySupplierCodeAndDate(arrival.getSupplier().getSupplierCode(), arrival.getDate());
		
		boolean isfirst = true;
		int supplierEntryId = -1;
		String allSupplierOrdersNumbersTreated = "";

		boolean saveEntrySucceeded = true;

		if (treatedEntryTemps == null || treatedEntryTemps.size() == 0) {
			// The entry is closed without product scanned.
			
			//save empty entry
			SupplierEntry supplierEntry = new SupplierEntry();
			supplierEntry.setSupplierCode(arrival.getSupplier().getSupplierCode());
			
			// To avoid the problem of empty order number, fill field with all requestOrder knowed for this supplier entry
			String orderNumbers = "";
			for (int i = 0; i < arrival.getSupplier().getOrders().size() ; i++) {
				String strTmp = String.valueOf(arrival.getSupplier().getOrders().get(i).getNumber());
				strTmp = Util.containsAll(orderNumbers, strTmp, ",");
				if (StringUtils.isNotEmpty(strTmp)) {
					orderNumbers += strTmp + ",";
				}
			}
			supplierEntry.setOrderNumbers(orderNumbers);
			supplierEntry.setChecked("0");
			supplierEntry.setResponsible("Ouistiti 2");
			supplierEntry.setEntryDate(Util.formatDate(arrival.getDate(), "yyyyMMdd","yyyy-MM-dd"));
			supplierEntry.setEntryTime(Util.formatDate(arrival.getTime(), "HHmmss","HH:mm:ss"));
			supplierEntry.setClosed("0");
			
			return new Event("removeEntry", String.valueOf(supplierEntryDAO.save(supplierEntry, getSession()))); 
		}

		// loop takes all the current method
		for (int index = 0 ; index < treatedEntryTemps.size(); index++) { 

			TreatedEntryTemp treatedEntryTemp = treatedEntryTemps.get(index);

			int entryTempId = treatedEntryTemp.getId();
			String supplierCode = treatedEntryTemp.getSupplierCode();				
			String arrivalDate = treatedEntryTemp.getArrivalDate();
			String arrivalTime = treatedEntryTemp.getArrivalTime();
			String productCode = treatedEntryTemp.getProductCode();
			String ean = treatedEntryTemp.getEan();
			String unitConditionnement = treatedEntryTemp.getUnitConditionnement();
			int numberOfUnit = treatedEntryTemp.getNumberOfUnit();
			int numberOfProduct = treatedEntryTemp.getNumberOfProduct();
			String lotNumber = treatedEntryTemp.getLotNumber();
			String supplierProductCode = treatedEntryTemp.getSupplierProductCode();
			String supplierOrderNumbers = treatedEntryTemp.getSupplierOrderNumbers();
			String supplierOrderLetters = treatedEntryTemp.getSupplierOrderLetters();
			int stockMovement = treatedEntryTemp.getStockMovement();
			
			// temp
			if (supplierOrderLetters == null) {
				supplierOrderLetters = "";
			}
			// temp 
			
			String containsAllResult = Util.containsAll(allSupplierOrdersNumbersTreated, supplierOrderNumbers, ",");
			if ( ! containsAllResult.equals("")) {
				if (allSupplierOrdersNumbersTreated.length() > 0) {
					allSupplierOrdersNumbersTreated += ",";
				}
				allSupplierOrdersNumbersTreated += containsAllResult;
			}
			
			String validityDate = treatedEntryTemp.getValidityDate();
			String additionalData = treatedEntryTemp.getAdditionalData();
			String description = treatedEntryTemp.getDescription();
			String supplierDocumentDescription = treatedEntryTemp.getSupplierDocumentDescription();
			int supplierDocumentType = treatedEntryTemp.getSupplierDocumentType();

			ArrayList<TreatedEntryCustomerTemp> treatedEntryCustomerTemps = 
				treatedEntryCustomerTempDAO.getAllForEntryId(entryTempId);
			
			Vector<Customer> customers = new Vector<Customer>();
			for (TreatedEntryCustomerTemp treatedEntryCustomerTemp : treatedEntryCustomerTemps) {
				
				Customer customer = new Customer(treatedEntryCustomerTemp.getCustomerCode());

				CustomerOrder co = new CustomerOrder(treatedEntryCustomerTemp.getCustomerOrderCode(),
													 treatedEntryCustomerTemp.getCustomerOrderCodeNumber());
				
				Fly fly = new Fly();
				fly.setAirportCode(treatedEntryCustomerTemp.getAirportCode());
				fly.setFlyNumber(treatedEntryCustomerTemp.getFlight());
				fly.setLtaNumber(treatedEntryCustomerTemp.getLta());
				co.setFly(fly);

				customer.setCustomerOrder(co);

				CustomerEntry customerEntry = new CustomerEntry();
				customerEntry.setTotalUnit(treatedEntryCustomerTemp.getCustomerNumberOfUnitRequested());
				customerEntry.setFromBasketNumber(treatedEntryCustomerTemp.getPackagingFromBasket());
				customerEntry.setToBasketNumber(treatedEntryCustomerTemp.getPackagingToBasket());
				customer.setCustomerEntry(customerEntry);
				
				customers.add(customer);
			}
			
			TreatedEntry treatedEntry = new TreatedEntry();
			treatedEntry.setValidityDate(validityDate);
			treatedEntry.setProductCode(productCode);
			treatedEntry.setSupplierProductCode(supplierProductCode);
			treatedEntry.setEan(ean);
			treatedEntry.setLotNumber(lotNumber);
			treatedEntry.setSupplierCode(supplierCode);
			treatedEntry.setArrivalDate(arrivalDate);
			treatedEntry.setArrivalTime(arrivalTime);

			boolean currentTransactionSucceeded = true;
			Session session = getSession();
			Transaction trx = session.getTransaction();
			trx.begin();

			try {
				currentTransactionSucceeded = treatedEntryDAO.insert(treatedEntry, session);
			} catch (Exception e) {
				log.error("Exception caught while saving the treatedEntry", e);
				// continue to close the current transaction but all traceability data will be lost
				currentTransactionSucceeded = false;
			}

			if (currentTransactionSucceeded) {
			
				TreatedEntryDetail treatedEntryDetail = new TreatedEntryDetail();
				
				// set foreign id
				treatedEntryDetail.setTreatedEntryId(treatedEntry.getTreatedEntryId());
	
				treatedEntryDetail.setSupplierOrderNumbers(supplierOrderNumbers);
				treatedEntryDetail.setSupplierOrderLetters(supplierOrderLetters);
				treatedEntryDetail.setAdditionalData(additionalData);
				treatedEntryDetail.setDescription(description);
				treatedEntryDetail.setUnitConditionnement(unitConditionnement);
				treatedEntryDetail.setNumberOfUnit(numberOfUnit);
				treatedEntryDetail.setNumberOfProduct(numberOfProduct);
				treatedEntryDetail.setSupplierDocumentDescription(supplierDocumentDescription);
				treatedEntryDetail.setSupplierDocumentType(supplierDocumentType);
				treatedEntryDetail.setStockMovement(stockMovement);

				try {
					currentTransactionSucceeded = treatedEntryDetailDAO.insert(treatedEntryDetail, session);
				} catch (Exception e) {
					log.error("Exception caught while saving the treatedEntryDetail", e);
					// continue to close the current transaction but all traceability data will be lost
					currentTransactionSucceeded = false;
				}

				if (currentTransactionSucceeded) {

					int totalUnitRemaining = numberOfUnit * numberOfProduct;

					for (Customer customer : customers) {

						if (totalUnitRemaining > 0) {

							TreatedEntryDetailDestination tedd = new TreatedEntryDetailDestination();
	
							// set foreign id
							tedd.setTreatedEntryDetailId(treatedEntryDetail.getTreatedEntryDetailId());
							tedd.setAllocationDate(Util.getNowFormated("yyyyMMdd"));
							tedd.setCustomerCode(customer.getCode());
	
							Fly fly = customer.getCustomerOrder().getFly();
							tedd.setAirportCode(fly.getAirportCode());
							tedd.setFlight(fly.getFlyNumber());
							tedd.setLta(fly.getLtaNumber());

							CustomerEntry ce = customer.getCustomerEntry();
							
							tedd.setPackagingFromBasket(ce.getFromBasketNumber());
							tedd.setPackagingToBasket(ce.getToBasketNumber());
							
							int totalUnitRequestedForCurrentClient = ce.getTotalUnit();

							if (totalUnitRemaining < totalUnitRequestedForCurrentClient) {
								tedd.setNumberOfUnit(totalUnitRemaining);
								totalUnitRemaining = 0;
							} else {
								tedd.setNumberOfUnit(totalUnitRequestedForCurrentClient);
								totalUnitRemaining -= totalUnitRequestedForCurrentClient;
							}

							try {
								currentTransactionSucceeded = treatedEntryDetailDestinationDAO.insert(tedd, session);
							} catch (Exception e) {
								log.error("Exception caught while saving the treatedEntryDetailDestination", e);
								// continue to close the current transaction but all traceability data will be lost
								currentTransactionSucceeded = false;
							}
						}
					}
				}
			}

			if ( ! currentTransactionSucceeded) {

				saveEntrySucceeded = false;
				// at the first exception encountered, flag the global result to 'failed'
				// BUT continue the process with the next entry.
				// => DO NOT BLOCK THE PROCESS => the all valid entry will be saved
				// 		and show the error
			}

			//commit all changes (tables impacted : treatedEntry, treatedEntryDetail and treatedEntrydetailDestination)
			trx.commit();
			
			
			// start treatment of of tables

			int totalUnitReceived = numberOfUnit *  numberOfProduct;
			try {
				
				currentTransactionSucceeded = true;
				
				// start a new transaction flow
				trx.begin();

				if (isfirst) {
					SupplierEntry supplierEntry = new SupplierEntry();
					supplierEntry.setSupplierCode(supplierCode);

					// To avoid the problem of empty order number, fill field with all requestOrder knowed for this supplier entry
					String orderNumbers = "";
					for (int i = 0; i < arrival.getSupplier().getOrders().size() ; i++) {
						String strTmp = String.valueOf(arrival.getSupplier().getOrders().get(i).getNumber());
						strTmp = Util.containsAll(orderNumbers, strTmp, ",");
						if (StringUtils.isNotEmpty(strTmp)) {
							orderNumbers += strTmp + ",";
						}
					}
					supplierEntry.setOrderNumbers(orderNumbers);

					supplierEntry.setChecked("0");
					supplierEntry.setResponsible("Ouistiti 2");
					supplierEntry.setEntryDate(Util.formatDate(arrival.getDate(), "yyyyMMdd","yyyy-MM-dd"));
					supplierEntry.setEntryTime(Util.formatDate(arrival.getTime(), "HHmmss","HH:mm:ss"));
					supplierEntry.setClosed("0");
					
					// check if not already exist in db for same data 
					// (abnormal behavior seen once leading to duplicate products in administration screens)
					if ( ! supplierEntryDAO.exist(supplierEntry, session)) {
						currentTransactionSucceeded = supplierEntryDAO.save(supplierEntry, session);
					}

					supplierEntryId = supplierEntry.getSupplierEntryId();
					isfirst = false;

				} else {

					SupplierEntry supplierEntry = supplierEntryDAO.getBySupplierEntryId(supplierEntryId, session);
					supplierEntryId = supplierEntry.getSupplierEntryId();
				}
				
				SupplierEntryDetail supplierEntryDetail = new SupplierEntryDetail();
				supplierEntryDetail.setSupplierEntryId(supplierEntryId);
				supplierEntryDetail.setSupplierCode(supplierCode);
				supplierEntryDetail.setLigneNumber(index + 1);

				supplierEntryDetail.setDate(Util.formatDate(arrivalDate,"yyyyMMdd","dd/MM/yyyy"));
				supplierEntryDetail.setProductCode(productCode);
				// CodeEanUnité not used
				supplierEntryDetail.setEan(ean);
				supplierEntryDetail.setUnit(Util.displayContitionnement(numberOfUnit, unitConditionnement));
				supplierEntryDetail.setNumberGets(totalUnitReceived);
				supplierEntryDetail.setNumberOfUnit(numberOfProduct);
				supplierEntryDetail.setChecked("0");
				supplierEntryDetail.setUpdated("0");
				supplierEntryDetail.setLot(lotNumber);
				
				currentTransactionSucceeded = supplierEntryDetailDAO.save(supplierEntryDetail, session);
				
			} catch (Exception e) {
				log.error("Error while saving the entry. Traceability tables are affected.");
				log.error("Rollback transaction cause :", e);
				currentTransactionSucceeded = false;
			}

			if (currentTransactionSucceeded) {
				trx.commit();
			} else {
				// at the first exception encountered, flag the global result to 'failed'
				// BUT continue the process with the next entry.
				// => DO NOT BLOCK THE PROCESS => the all valid entry will be saved
				// 		and show the error
				saveEntrySucceeded = false;
				trx.rollback();			
			}


			// insert/update data in the existing table (pod/MSAccess form integration) 
			trx.begin();

			currentTransactionSucceeded = true;

			try {

				int totalUnitRequested = 0;
				for (int i = 0; i < customers.size(); i++) {
					totalUnitRequested += customers.get(i).getCustomerEntry().getTotalUnit();
				}

				// check if existing product (same product Code with different unitconditionnement)
				SupplierEntryQuantity supplierEntryQuantity = supplierEntryQuantityDAO.getUnique(supplierEntryId, productCode, session);
				boolean existingSupplierEntryQuantity = false;
				
				// totalUnitReceivedOfProductCode contains all units for the current entry + units of previous entry with same product code
				int totalUnitReceivedOfProductCode = totalUnitReceived;
				if (supplierEntryQuantity != null) {
					existingSupplierEntryQuantity = true;
					totalUnitReceivedOfProductCode = totalUnitReceived + supplierEntryQuantity.getNumberGets();  
				} else {
					supplierEntryQuantity = new SupplierEntryQuantity(supplierEntryId, productCode);
					//supplierEntryQuantity.setNumberTransferedToStock(0);					
				}

				int totalUnitSendToCustomer;
				if (totalUnitRequested == totalUnitReceivedOfProductCode) {
					totalUnitSendToCustomer = totalUnitRequested;
				} else if (totalUnitRequested < totalUnitReceivedOfProductCode) {
					totalUnitSendToCustomer = totalUnitRequested;
				} else {
					totalUnitSendToCustomer = totalUnitReceivedOfProductCode;
				}

				supplierEntryQuantity.setNumberGets(totalUnitReceivedOfProductCode);
				supplierEntryQuantity.setNumberTransferedToCustomer(totalUnitSendToCustomer);
				
				if (existingSupplierEntryQuantity) {
					currentTransactionSucceeded = supplierEntryQuantityDAO.update(supplierEntryQuantity, session);
				} else {
					currentTransactionSucceeded = supplierEntryQuantityDAO.save(supplierEntryQuantity, session); 
				}

				
				if (currentTransactionSucceeded) {

					// add the link to the number of product in stock in the Articles table
					float totalUnitRemaining = totalUnitReceivedOfProductCode;
					for (int i=0; i < customers.size() && totalUnitRemaining > 0; i++) {

						Customer customer = customers.get(i);

						ArrayList<CustomerOrderDetail> cods = new ArrayList<CustomerOrderDetail>(); 

						// search for all treated supplierOrderLetters and numbers with the corresponding customerOrderLetter and number
						StringTokenizer numbers = new StringTokenizer(supplierOrderNumbers, ",");
						StringTokenizer letters = new StringTokenizer(supplierOrderLetters, ",");
						// workaround...
						// we've detected problems when one number contains several letters order.
						// ex : numbers = 100 and letters = OTH,CMC => only the first number will be treated if 
						// we don't duplicate the same number for different letters. BUT there will be side effect.
						// => we need to do some refactoring with the numbers and letters at the store side of this object.						
						if ( StringUtils.isNotEmpty(supplierOrderNumbers) && StringUtils.isNotEmpty(supplierOrderLetters)) {
							String tempNumber = "";
							String tempLetter = "";
							while (numbers.hasMoreTokens() || letters.hasMoreTokens()) {
								if (numbers.hasMoreTokens()) {
									tempNumber = numbers.nextToken();
								}
								if (letters.hasMoreTokens()) {
									tempLetter = letters.nextToken();
								}
								cods.addAll(customerOrderDetailDAO.get(productCode, tempNumber, tempLetter ,customer.getCustomerOrder().getOrderCode(), customer.getCustomerOrder().getOrderNumber(), session));
							}
							
							// check if the total of unit request correspond to the one of each record.
							// This must be done this way because the field TotalNumberOfUnit is not always correctly filled by ouistiti(v1)
							// => re-compute it to be sure.
							for (int j = 0; j < cods.size() ; j++) {
								CustomerOrderDetail cod = cods.get(j);
								if (customer.getCustomerEntry().getTotalUnit() != (cod.getNumberOfProduct() * cod.getNumberOfUnit())) {
									cods.remove(j);
									j--;
								}
							}

						}

						if (cods != null && cods.size() > 0) {
							
							for (CustomerOrderDetail cod : cods) {

								// filter only for the requested supplier code foreseen
								// Update for multiple supplier corresponding to one customer order (GERPANA sample)
								//if (cod.getSupplierCode().equals(supplierCode)) {

									// WARNING the field TotalNumberOfUnit is not always correctly filled by ouistiti
									// re-compute it to be sure.
									// DONT USE : int totalUnitRequestedForCurrentClient = sod.getTotalNumberOfUnit();
									int totalUnitRequestedForCurrentClient = (cod.getNumberOfProduct() * cod.getNumberOfUnit()) - new Float(cod.getNumberGets()).intValue();
		
									// if the current sod has not previously been treated
									if (totalUnitRequestedForCurrentClient > 0) {
										
										float totalUnitésReçues = totalUnitRequestedForCurrentClient;
										if (totalUnitRemaining < totalUnitRequestedForCurrentClient) {
											totalUnitésReçues = totalUnitRemaining;
											totalUnitRemaining = 0;
										} else {
											totalUnitRemaining -= totalUnitRequestedForCurrentClient; 
										}
		
										cod.setNumberGets(cod.getNumberGets() + totalUnitésReçues);
		
										currentTransactionSucceeded = customerOrderDetailDAO.update(cod, session);
									}
								//}
							}
						}
					}
				}

			} catch (Exception e) {
				log.error("Error while saving the entry. Traceability tables are affected.");
				log.error("Rollback transaction cause :", e);
				currentTransactionSucceeded = false;
			}

			if (currentTransactionSucceeded) {
				trx.commit();
			} else {
				// at the first exception encountered, flag the global result to 'failed'
				// BUT continue the process with the next entry.
				// => DO NOT BLOCK THE PROCESS => the all valid entry will be saved
				// 		and show the error
				saveEntrySucceeded = false;
				trx.rollback();			
			}
			
			session.close();
		}
		

		return new Event("removeEntry", String.valueOf(saveEntrySucceeded));

	}

	public Event checkPending(Arrival arrival) {
		
		arrival.setDate(arrival.getSearchSupplierDate());
		arrival.setTime(Util.formatDate(null, null, "kkmmss"));
		
		Vector<Supplier> suppliers = arrival.getSuppliers();
		for (int i = 0; i < suppliers.size(); i++) {
			Supplier supplier = suppliers.get(i);

			// if date must be took into account for the pending check
			ArrayList<TreatedEntryTemp> treatedEntryTemps = treatedEntryTempDAO.getBySupplierCodeAndDate(supplier.getSupplierCode(), arrival.getSearchSupplierDate());

			if (treatedEntryTemps.size() > 0) {

				supplier.setStatus(Supplier.STATUS_PENDING);

			}
			
			// check if this supplier for the current date has already been treated
			SupplierEntry supplierEntry = supplierEntryDAO.getSupplierByDate(supplier.getSupplierCode(), arrival.getDate());
			
			if ( StringUtils.isNotEmpty(supplierEntry.getOrderNumbers()) ) {
				
				boolean allOrdersClosed = true;
				String orderNumbers = supplierEntry.getOrderNumbers();
				Vector<Order> orders = supplier.getOrders();
				for (int j = 0; j < orders.size(); j++) {
					if (orderNumbers.indexOf(String.valueOf(orders.get(j).getNumber())) == -1){
						allOrdersClosed = false;
					}
				}
				if (allOrdersClosed) {
					supplier.setStatus(Supplier.STATUS_CLOSED);
				} else {
					supplier.setStatus(Supplier.STATUS_PENDING);
				}
			} else if ( StringUtils.isNotEmpty(supplierEntry.getEntryDate()) ) { 
				// the current supplier entry has been closed without any product entered
				supplier.setStatus(Supplier.STATUS_CLOSED);
			}

			suppliers.setElementAt(supplier, i);
		}
		
		return new Event("arrivalBusiness","success");
	}

	public Event getPending(Arrival arrival) {
		boolean result = false;
		
		ArrayList<TreatedEntryTemp> tets = treatedEntryTempDAO.getBySupplierCodeAndDate(arrival.getSupplier().getSupplierCode(), arrival.getDate());

		if (tets.size() > 0) {
			
			result = true;
			Vector<Entry> entries = arrival.getSavedEntries();
			
			for (TreatedEntryTemp tet : tets) {

				arrival.setSupplierDocumentDescription(tet.getSupplierDocumentDescription());
				arrival.setSupplierDocumentType(tet.getSupplierDocumentType());

				Product product = new Product(tet.getProductCode());
				product.setLotNumber(tet.getLotNumber());
				product.setValidityDate(tet.getValidityDate());
				product.setAdditionalData(tet.getAdditionalData());
				product.setDescription(tet.getDescription());
				
				Unit unit = product .new Unit();
				unit.setNumber(tet.getNumberOfUnit());
				unit.setConditionnement(tet.getUnitConditionnement());
				unit.setEan(tet.getEan());

				product.addUnit(unit);

				Entry entry = new Entry();
				entry.setProduct(product);
				entry.setNumberOfProduct(tet.getNumberOfProduct());

				ArrayList<TreatedEntryCustomerTemp> tects = 
					treatedEntryCustomerTempDAO.getAllForEntryId(tet.getId());

				for (TreatedEntryCustomerTemp tect : tects) {

					Customer customer = new Customer(tect.getCustomerCode());

					CustomerOrder co = new CustomerOrder(tect.getCustomerOrderCode(), 
														 tect.getCustomerOrderCodeNumber());
					customer.setCustomerOrder(co);

					CustomerEntry customerEntry = new CustomerEntry();
					customerEntry.setTotalUnit(tect.getCustomerNumberOfUnitRequested());
					
					customer.setCustomerEntry(customerEntry);
					
					entry.addCustomer(customer);
				}

				entries.add(entry);

			}
		}

		if (result) {
			return new Event("arrivalBusiness","true");			
		} else {
			return new Event("arrivalBusiness","false");
		}
	}
	
	public boolean deletePending(Arrival arrival){
		boolean result = true;

		Session session = getSession();
		Transaction trx = session.getTransaction();
		trx.begin();

		//[[RM 28/09/2010]] delete only regarding the supplier and the date 
		//result = treatedEntryTempDAO.deleteAllBySupplier(arrival.getSupplier().getSupplierCode(), session, treatedEntryCustomerTempDAO);
		result = treatedEntryTempDAO.deleteAll(arrival.getSupplier().getSupplierCode(), arrival.getDate(), session, treatedEntryCustomerTempDAO);

		if (result) {
			trx.commit();
			arrival.setSavedEntries(new Vector<Entry>());
			arrival.setSaved(false);
			arrival.setSavedEntry(false);

		} else {
			trx.rollback();
		}

		return result;
	}
	
	public Arrival getPlannedSuppliers(Arrival arrival) {
		if (arrival == null) {
			arrival = new Arrival();
		}

		Vector<Supplier> suppliers = new Vector<Supplier>();
		
		ArrayList<Planning> plannings = (ArrayList<Planning>) planningDAO.getByDate(arrival.getSearchSupplierDate());

		for (Planning planning : plannings) {
			int orderNumber = planning.getOrderNumber();
			String supplierName = "";
			String supplierCode = planning.getSupplierCode();
			int creationNumber = planning.getCreationNumber();

			Supplier supplier = null;

			// search if this supplier has an other NuméroCommande
			int supplierIndex = -1;
			for (int i=0; i< suppliers.size(); i++){

				if (suppliers.get(i).getSupplierCode().equals(supplierCode)) {
					supplierIndex = i;
					supplier = suppliers.get(i);

					Order order = supplier. new Order();
					order.setDate(arrival.getSearchSupplierDate());
					order.setNumber(orderNumber);
					order.setLetter("");
					order.setCreationNumber(creationNumber);
					supplier.addOrder(order);
					
				}
			}
			
			if (supplier == null) {

				if (supplierCode.equals("")) {

					supplierName = "Inconnu";

				} else {
					
					db.supplier.Supplier supplierDbObj = supplierDAO.getByCode(supplierCode);
					supplierName = supplierDbObj.getDescription();	
				}

				supplier = new Supplier(supplierCode, supplierName);
				supplier.setStatus(Supplier.STATUS_OPENED);

				Order order = supplier. new Order();
				order.setDate(arrival.getSearchSupplierDate());
				order.setNumber(orderNumber);
				order.setLetter("");
				order.setCreationNumber(creationNumber);
				supplier.addOrder(order);

			}

			if (supplierIndex >= 0) {
				suppliers.setElementAt(supplier, supplierIndex);
			} else {
				suppliers.add(supplier);
			}

		}
		
		arrival.setSuppliers(suppliers);

		Collections.sort(arrival.getSuppliers(), new Comparator<Supplier>(){

			public int compare(Supplier supplier0, Supplier supplier1) {
				return supplier0.getSupplierName().compareTo(supplier1.getSupplierName());
			}
		});

		return arrival;
	}

	public Event getPlannedSupplier(Arrival arrival) {
		if (arrival == null) {
			arrival = new Arrival();
			arrival.setSupplier(new Supplier()); 
		}

		Vector<Supplier> suppliers = arrival.getSuppliers();
		Supplier supplier = arrival.getSupplier();
		
		for (int i=0; i < suppliers.size(); i++) {
			if (supplier.getSupplierCode().equals(suppliers.get(i).getSupplierCode())){
				supplier = suppliers.get(i);
				break;
			}
		}
		
		supplier = fillSupplierOrder(supplier);
		
		arrival.setSupplier(supplier);
		
		if (supplier.isClosed()) {
			return new Event("arrivalBusiness", "isClosed");
		} else {
			return new Event("arrivalBusiness", "isNotClosed");
		}
		
		//return arrival;
	}
	
	public Supplier fillSupplierOrder(Supplier supplier){

		Vector<Entry> entries = supplier.getRequestedEntries();
		
		boolean isSupplierStock = StringUtils.equals(supplier.getSupplierCode(), Constants.SUPPLIER_STOCK);
		
		Hashtable<Integer, ArrayList<Integer>> orderReferences = new Hashtable<Integer, ArrayList<Integer>>(); 
		// vérifier si plusieurs commandes en cours
		for ( Order supplierOrder : supplier.getOrders() ) {
			if (orderReferences.containsKey(supplierOrder.getNumber())) {
				ArrayList<Integer> creationNumbers = orderReferences.get(supplierOrder.getNumber());
				creationNumbers.add(supplierOrder.getCreationNumber());
				orderReferences.put(supplierOrder.getNumber(), creationNumbers);
			} else {
				ArrayList<Integer> creationNumbers = new ArrayList<Integer>();
				creationNumbers.add(supplierOrder.getCreationNumber());
				orderReferences.put(supplierOrder.getNumber(), creationNumbers);
			}
		}

		HashSet<String> treatedItems = new HashSet<String>();

		ArrayList<CustomerOrderDetail> cods = customerOrderDetailDAO.get(orderReferences.keySet());

		for (CustomerOrderDetail cod : cods) {
			
			int currentOrderNumber =   cod.getOrderNumber();
			
			if (checkCreationNumber(orderReferences, currentOrderNumber, cod.getCreationNumber())) {

				String currentOrderLetter =  cod.getOrderLetter();
				String productCode = cod.getProductCode();
				
				if (treatedItems.contains(String.valueOf(currentOrderNumber) + productCode)) {
					// skip this supplier order validation
				} else {
					treatedItems.add(String.valueOf(currentOrderNumber) + productCode);

					if (cod.getProductCode().equals("CLIQUOT")) {
						String test = "";
					}
					
					ArrayList<SupplierOrderDetail> sods = supplierOrderDetailDAO.get(supplier.getSupplierCode(), currentOrderNumber, productCode);
					for (SupplierOrderDetail sod : sods) {
	
						ArrayList<Conditioning> cs = conditioningDAO.get(productCode, supplier.getSupplierCode());

						if (isSupplierStock) {
							if ( cs == null || cs.size() == 0 ) {
								// search for priority 1 of this product code and add the conditionnement for the unit scale
								Conditioning c = conditioningDAO.getUnitScaleForTopPriority(productCode);
								if (c != null) {
									cs.add(c);
								}
							}
						}

						for (Conditioning c : cs) {
							boolean treatItem = false;
							boolean simpleUnit = false;

							if (c.getUnitLargeScale().startsWith(sod.getUnitLargeScale()) || sod.getUnitLargeScale().startsWith(c.getUnitLargeScale())) {
								treatItem = true;
							} else {
								if (isSupplierStock && c.getUnit().equals(sod.getUnitLargeScale())) {
									simpleUnit = true;
									treatItem = true;
								}
							}

							if ( treatItem ) {
	
								int numberOfProduct = sod.getNumberOfProductOrdered();
								String unitConditionnement = c.getUnit();
								int numberOfUnit = c.getNumberOfUnit();

								if ( simpleUnit ) {
									numberOfUnit = 1;
								}
	
								Product product = new Product(productCode);
								product.setDescription(productDAO.getDescription(productCode));

								Unit unit = product. new Unit();
								unit.setNumber(numberOfUnit);
								unit.setConditionnement(unitConditionnement);
								unit.setDateLastMofication(c.getDateLastModification());
								
								// get the EAN (will be used in order status view for direct links to completeEntryView or not)
								ArrayList<Conditioning> conditionnings = conditioningDAO.getProductsByCode(productCode, true, supplier.getSupplierCode());
								if (conditionnings.size() == 1) {
									String ean = conditionnings.get(0).getEan();
									if (StringUtils.isNumeric(ean) && ean.length() > 8) {
										unit.setEan(ean);
									}
								}
					
								product.addUnit(unit);
					
								Entry entry = new Entry();
								entry.setOrderNumbers(String.valueOf(currentOrderNumber));
								entry.setOrderLetter(currentOrderLetter);
								entry.setProduct(product);
								entry.setNumberOfProduct(numberOfProduct);
					
								// check if entry not already insert in the entries
								// if yes, add the current number of product to the one found in entries
								// if no, just add entry in entries
								/*
								if (entry.getProduct().getProductCode().equals("ROLSAUM")) {
									String debugBreakPoint = "";
								}
								*/
					
								int itemFoundIndex = -1;
								for (int i = 0; (i < entries.size()) && (itemFoundIndex < 0); i++) {
									Entry tempEntry = entries.get(i);
									if ( tempEntry.getProduct().getProductCode().equals(product.getProductCode()) ) {
										Vector<Unit> tempUnits = tempEntry.getProduct().getUnits();
										Unit currentUnit = product.getUnits().get(0);
										for (int j = 0; j < tempUnits.size(); j++) {
											if (
													tempUnits.get(j).getConditionnement().equals(currentUnit.getConditionnement()) &&
													tempUnits.get(j).getNumber() == currentUnit.getNumber()
												) {
												numberOfProduct = tempEntry.getNumberOfProduct() + entry.getNumberOfProduct();
												itemFoundIndex = i;
											}
										}
					
										entry.setOrderNumbers(tempEntry.getOrderNumbers());
										entry.setOrderLetter(tempEntry.getOrderLetter());
					
										entry.addOrderNumbers(String.valueOf(currentOrderNumber));
										entry.addOrderLetter(currentOrderLetter);
					
										break;
									}
								}
								if (itemFoundIndex < 0) {
									entries.add(entry);
								} else {
									entry.setNumberOfProduct(numberOfProduct);
									entries.setElementAt(entry, itemFoundIndex);
								}
	
							}
						}
					}
				}
			}
		}

		supplier.setRequestedEntries(entries);
		
		return supplier;
	}

	private boolean checkCreationNumber(Hashtable<Integer, ArrayList<Integer>> orderReferences, int key, int value) {
		if (orderReferences.containsKey(key)) {
			ArrayList<Integer> list = orderReferences.get(key);  
			for (Integer integer : list) {
				if (integer == Integer.valueOf(value)) {
					return true;
				}
			}
		}
		return false;
	}

	public Arrival productSelection(Arrival arrival, Vector<Product> products) {
		String productCodeSelected = arrival.getProductCodeSelected();
		String unitIndexSelected = arrival.getUnitIndexSelected(); 
		
		Product product = null;

		if (productCodeSelected != null && unitIndexSelected != null) {
			for (int i = 0; i < products.size(); i++) {
				if (productCodeSelected.equals(products.get(i).getProductCode())) {
					product = products.get(i);
					// si l'unit selected > units size alors c'est un nouveau conditionnement.
					// il faut creer une nouvelle unit
					if (Integer.parseInt(unitIndexSelected) >= product.getUnits().size()) {
						product.getUnits().add(product. new Unit());
					}

					product.setUnitSelected(unitIndexSelected);
					break;
				}
			}
		}
		arrival.getEntry().setProduct(product);
		return arrival;
	}
	
	public void selectBasket(Arrival arrival){
		Supplier supplier = arrival.getSupplier();
		Entry entry = arrival.getEntry();

		String orderNumbers = "";
		for (int i = 0; i < supplier.getOrders().size(); i++) {
			if (i != 0) {
				orderNumbers += ",";
			}
			orderNumbers += supplier.getOrders().get(i).getNumber();
		}
		
		// get the list of packages declared for the current product 
		ArrayList<PackageDetail> packageDetails = packageDetailDAO.get(entry.getProduct().getProductCode(), new StringTokenizer(orderNumbers, ","));
		
		// get client info + fill entry specifications
		ArrayList<CustomerOrderDetail> cods = customerOrderDetailDAO.getByProductCode(entry.getProduct().getProductCode(), new StringTokenizer(orderNumbers, ","));

		entry.setCustomers(new Vector<Customer>());
		
		HashSet<Integer> orderTreated = new HashSet<Integer>();

		for (CustomerOrderDetail cod : cods) {

			entry.addOrderNumbers(String.valueOf(cod.getOrderNumber()));
			entry.addOrderLetter(cod.getOrderLetter());

			String customerOrderCode = cod.getCustomerOrderCode();
			int customerOrderCodeNumber = cod.getCustomerOrderCodeNumber();
			
			// store global data
			CustomerEntry globalCE = new CustomerEntry();
			globalCE.setAssignedSupplierCode(cod.getSupplierCode());
			globalCE.setPriority(cod.getPriority());
			globalCE.setNumberOfConditionnement((int) cod.getNumberOfProduct());
			globalCE.setUnitConditionnement(cod.getUnit());
			globalCE.setNumberOfUnit((int) cod.getNumberOfUnit());
			//customerEntry.setTotalUnit(sod.getTotalNumberOfUnit());
			// WARNING the field TotalNumberOfUnit is not always correctly filled by ouistiti
			// re-compute it to be sure.
			globalCE.setTotalUnit(cod.getNumberOfProduct() * cod.getNumberOfUnit());
			globalCE.setNeedFridge(cod.getFridge().equals("1"));

			CustomerOrder co = new CustomerOrder(customerOrderCode, customerOrderCodeNumber);
			Fly fly = new Fly();
			PrepareOrder prepareOrder = prepareOrderDAO.get(cod.getOrderNumber(), cod.getOrderLetter());
			if (prepareOrder != null) {
				fly.setAirportCode(prepareOrder.getAirportCode());
				fly.setFlyNumber(prepareOrder.getFlightNumber());
				fly.setLtaNumber(prepareOrder.getLtaNumber());
				fly.setFret(prepareOrder.getFret());
			}
			co.setFly(fly);


			int feesType = 0;
			Packaging packaging = null;
			boolean orderFilled = false;

			for (PackageDetail pd : packageDetails) {
				
				if (	pd.getCustomerOrderCodeNumber() == customerOrderCodeNumber && 
						pd.getCustomerOrderCode().equals(customerOrderCode)		   &&

						pd.getNumberOfUnit() == cod.getNumberOfUnit()			   &&
						pd.getUnit().equals(cod.getUnit()) 		   				   &&
						pd.getNumberOfProduct() == cod.getNumberOfProduct()		   &&
						
						! orderTreated.contains(pd.getId())
					) {

					orderTreated.add(pd.getId());
					orderFilled = true;

					feesType = pd.getFeesType();

					// retrieve global data
					CustomerEntry customerEntry = new CustomerEntry();
					customerEntry.setAssignedSupplierCode(globalCE.getAssignedSupplierCode());
					customerEntry.setPriority(globalCE.getPriority());
					customerEntry.setNumberOfConditionnement(globalCE.getNumberOfConditionnement());
					customerEntry.setUnitConditionnement(globalCE.getUnitConditionnement());
					customerEntry.setNumberOfUnit(globalCE.getNumberOfUnit());
					customerEntry.setTotalUnit(globalCE.getTotalUnit());
					customerEntry.setNeedFridge(globalCE.isNeedFridge());
					
					// retrieve local data
					customerEntry.setFromBasketNumber(pd.getFromPackageNumber());

					PackagingDetail packagingDetail = packagingDetailDAO.get(pd.getOrderNumber(), 
																			pd.getOrderLetter(), 
																			pd.getCustomerOrderCode(), 
																			pd.getCustomerOrderCodeNumber(),
																			pd.getFeesType(),
																			pd.getFromPackageNumber());

					customerEntry.setToBasketNumber(packagingDetail.getToPackageNumber());
					customerEntry.setNeedFridge(packagingDetail.getFridge() == 1);

					packaging = packagingDAO.get(cod.getOrderNumber(), cod.getOrderLetter(), customerOrderCode, customerOrderCodeNumber, feesType);

					Customer customer = new Customer();
					customer.setCustomerOrder(co);
					customer.setCustomerEntry(customerEntry);

					if (packaging != null && StringUtils.isNotEmpty(packaging.getCustomerCode()) ) {
						customer.setCode(packaging.getCustomerCode());
						if ( ! customer.getCode().equals("") ) {
							customer.setAddressFurniture(customerDAO.getAddressFurniture(customer.getCode()));
						}
					} else {
						// retrieve the customer code from the prepareOrderDetail table
						// because no packaging has been set
						String customerCode = prepareOrderDetailDAO.getCustomerCode(cod.getOrderNumber(), cod.getOrderLetter(), customerOrderCode, customerOrderCodeNumber, feesType);
						if (StringUtils.isNotEmpty(customerCode)) {
							customer.setCode(customerCode);
							customer.setAddressFees(customerDAO.getAddressFurniture(customer.getCode()));
						} else {
							customer.setCode("");
							customer.setAddressFees("");
						}
					}

					entry.addCustomer(customer);

				}
			}

			if ( ! orderFilled && orderTreated.size() == 0 ) {
				// should not be the case...
				// EXCEPT if not packaging has been done by ouistiti access program
				// Same code as above (from customer object creation line)
				log.error("PROBLEM DETECTED during packaging set.");

				Customer customer = new Customer();
				customer.setCustomerOrder(co);
				customer.setCustomerEntry(globalCE);

				if (packaging != null && StringUtils.isNotEmpty(packaging.getCustomerCode()) ) {
					customer.setCode(packaging.getCustomerCode());
					if ( ! customer.getCode().equals("") ) {
						customer.setAddressFurniture(customerDAO.getAddressFurniture(customer.getCode()));
					}
				} else {
					// retrieve the customer code from the prepareOrderDetail table
					// because no packaging has been set
					String customerCode = prepareOrderDetailDAO.getCustomerCode(cod.getOrderNumber(), cod.getOrderLetter(), customerOrderCode, customerOrderCodeNumber, feesType);
					if (StringUtils.isNotEmpty(customerCode)) {
						customer.setCode(customerCode);
						customer.setAddressFees(customerDAO.getAddressFurniture(customer.getCode()));
					} else {
						customer.setCode("");
						customer.setAddressFees("");
					}
				}
	
				entry.addCustomer(customer);
			}

		}

		arrival.setEntry(entry);
	}

	public void setCustomerDAO(ICustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	public void setPackageDetailDAO(IPackageDetailDAO packageDetailDAO) {
		this.packageDetailDAO = packageDetailDAO;
	}

	public void setPackagingDAO(IPackagingDAO packagingDAO) {
		this.packagingDAO = packagingDAO;
	}

	public void setPackagingDetailDAO(IPackagingDetailDAO packagingDetailDAO) {
		this.packagingDetailDAO = packagingDetailDAO;
	}

	public void setPrepareOrderDAO(IPrepareOrderDAO prepareOrderDAO) {
		this.prepareOrderDAO = prepareOrderDAO;
	}

	public void setCustomerOrderDetailDAO(
			ICustomerOrderDetailDAO customerOrderDetailDAO) {
		this.customerOrderDetailDAO = customerOrderDetailDAO;
	}
	
	public void setTreatedEntryTempDAO(ITreatedEntryTempDAO treatedEntryTempDAO) {
		this.treatedEntryTempDAO = treatedEntryTempDAO;
	}

	public void setSupplierEntryDAO(ISupplierEntryDAO supplierEntryDAO) {
		this.supplierEntryDAO = supplierEntryDAO;
	}

	public void setPlanningDAO(IPlanningDAO planningDAO) {
		this.planningDAO = planningDAO;
	}

	public void setSupplierDAO(ISupplierDAO supplierDAO) {
		this.supplierDAO = supplierDAO;
	}

	public void setProductDAO(IProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	public void setTreatedEntryCustomerTempDAO(
			ITreatedEntryCustomerTempDAO treatedEntryCustomerTempDAO) {
		this.treatedEntryCustomerTempDAO = treatedEntryCustomerTempDAO;
	}

	public void setTreatedEntryDAO(ITreatedEntryDAO treatedEntryDAO) {
		this.treatedEntryDAO = treatedEntryDAO;
	}

	public void setSupplierEntryQuantityDAO(
			ISupplierEntryQuantityDAO supplierEntryQuantityDAO) {
		this.supplierEntryQuantityDAO = supplierEntryQuantityDAO;
	}

	public void setSupplierEntryDetailDAO(
			ISupplierEntryDetailDAO supplierEntrydetailDAO) {
		this.supplierEntryDetailDAO = supplierEntrydetailDAO;
	}

	public void setTreatedEntryDetailDestinationDAO(
			ITreatedEntryDetailDestinationDAO treatedEntryDetailDestinationDAO) {
		this.treatedEntryDetailDestinationDAO = treatedEntryDetailDestinationDAO;
	}

	public void setTreatedEntryDetailDAO(
			ITreatedEntryDetailDAO treatedEntryDetailDAO) {
		this.treatedEntryDetailDAO = treatedEntryDetailDAO;
	}

	public void setPrepareOrderDetailDAO(
			IPrepareOrderDetailDAO prepareOrderDetailDAO) {
		this.prepareOrderDetailDAO = prepareOrderDetailDAO;
	}

	public void setConditioningDAO(IConditioningDAO conditioningDAO) {
		this.conditioningDAO = conditioningDAO;
	}

	public void setSupplierOrderDetailDAO(
			ISupplierOrderDetailDAO supplierOrderDetailDAO) {
		this.supplierOrderDetailDAO = supplierOrderDetailDAO;
	}
}