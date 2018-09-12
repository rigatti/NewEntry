package org.belex.allocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.belex.arrival.ArrivalBusiness;
import org.belex.customer.Customer;
import org.belex.customer.CustomerOrder;
import org.belex.product.Product;
import org.belex.product.ProductTraceable;
import org.belex.product.Product.Unit;
import org.belex.product.ProductTraceable.Destination;
import org.belex.requestparams.RequestParams;
import org.belex.supplier.Supplier;
import org.belex.supplier.Supplier.Order;
import org.belex.util.Constants;
import org.belex.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.webflow.execution.Event;

import db.customer.ICustomerDAO;
import db.customer.order.CustomerOrderDetail;
import db.customer.order.ICustomerOrderDetailDAO;
import db.entry.IPlanningDAO;
import db.entry.ISupplierEntryDAO;
import db.entry.Planning;
import db.entry.SupplierEntry;
import db.entry.treated.IStockEntryDAO;
import db.entry.treated.ISupplierReturnsEntryDAO;
import db.entry.treated.ITreatedEntryDAO;
import db.entry.treated.ITreatedEntryDetailDAO;
import db.entry.treated.ITreatedEntryDetailDestinationDAO;
import db.entry.treated.StockEntry;
import db.entry.treated.SupplierReturnsEntry;
import db.entry.treated.TreatedEntry;
import db.entry.treated.TreatedEntryDetail;
import db.entry.treated.TreatedEntryDetailDestination;
import db.prepare.IPackagingDAO;
import db.prepare.IPackagingDetailDAO;
import db.prepare.IPrepareOrderDAO;
import db.prepare.IPrepareOrderDetailDAO;
import db.prepare.Packaging;
import db.product.Conditioning;
import db.product.IConditioningDAO;
import db.product.IProductDAO;
import db.supplier.ISupplierDAO;
import db.supplier.order.ISupplierOrderDetailDAO;
import db.supplier.order.SupplierOrderDetail;

public class AllocationBusiness extends HibernateDaoSupport implements IAllocationBusiness{

	private static final Category log = Logger.getLogger(ArrivalBusiness.class);

	IProductDAO productDAO;
	ISupplierEntryDAO supplierEntryDAO;
	ISupplierDAO supplierDAO;
	IPackagingDAO packagingDAO;
	IPackagingDetailDAO packagingDetailDAO;
	IPrepareOrderDAO prepareOrderDAO;
	IPrepareOrderDetailDAO prepareOrderDetailDAO;
	ICustomerOrderDetailDAO customerOrderDetailDAO;
	ISupplierOrderDetailDAO supplierOrderDetailDAO;
	ITreatedEntryDAO treatedEntryDAO;
	ITreatedEntryDetailDAO treatedEntryDetailDAO;
	ITreatedEntryDetailDestinationDAO treatedEntryDetailDestinationDAO;
	IStockEntryDAO stockEntryDAO;
	ISupplierReturnsEntryDAO supplierReturnsEntryDAO; 
	ICustomerDAO customerDAO;
	IPlanningDAO planningDAO;
	IConditioningDAO conditioningDAO;
	private Allocation allocation;
	
	// to avoid doubble treatment.... easy fix
	private HashSet<String> orderTreated;

	public Allocation getSuppliersEntries(Allocation allocation, RequestParams requestParams) {

		this.allocation = allocation;
		
		if (allocation.getSuppliers().size() == 0) {
			allocation.setSuppliers(supplierDAO.getAll());
		}

		ArrayList<AllocationEntry> entries = new ArrayList<AllocationEntry>();

		ArrayList<SupplierEntry> ses = supplierEntryDAO.getSuppliersByDate(requestParams.getDate());
		for (SupplierEntry se : ses) {

			Supplier supplier = new Supplier();
			supplier.setSupplierCode(se.getSupplierCode());

			HashSet<String> creationReferences = new HashSet<String>();

			ArrayList<Planning> plannings = planningDAO.getByDateAndSupplier(requestParams.getDate(), supplier.getSupplierCode());
			for(Planning p : plannings) {

				creationReferences.add(p.getSupplierCode() + "-" + 
									   p.getOrderNumber() + "-" + 
									   p.getCreationNumber());
			}

			// for all supplier order number, get all order letter
			StringTokenizer ordersST = new StringTokenizer(se.getOrderNumbers(), ",");
			if (ordersST.hasMoreElements()) {
				while (ordersST.hasMoreElements()) {
					try {

						String lettersTreated = "";
						int supplierOrderNumber = Integer.parseInt((String) ordersST.nextElement());

						ArrayList<SupplierOrderDetail> sods = supplierOrderDetailDAO.get(supplier.getSupplierCode(), supplierOrderNumber);
						for (SupplierOrderDetail sod : sods) {
/*
							if (StringUtils.equals(sod.getSupplierCode(), Constants.SUPPLIER_STOCK) ) {

								ArrayList<CustomerOrderDetail> cods = customerOrderDetailDAO.getByProductCode(sod.getProductCode(), new StringTokenizer(String.valueOf(supplierOrderNumber)));
								for (CustomerOrderDetail cod : cods) {

									if (creationReferences.contains(supplier.getSupplierCode() + "-" + supplierOrderNumber + "-" + cod.getCreationNumber())) {

										if ( ! StringUtils.contains(lettersTreated, ";" + cod.getOrderLetter() + ";")) {
											lettersTreated += ";" + cod.getOrderLetter() + ";";
											
											Order order = supplier. new Order();
											//order.setNumber(sod.getNumberOfProductOrdered() * c.getNumberOfUnit());
											order.setNumber(supplierOrderNumber);
											order.setLetter(cod.getOrderLetter());
											order.setCreationNumber(cod.getCreationNumber());
											supplier.addOrder(order);
										}
									}
								}
								
							} else {
*/
								ArrayList<Conditioning> cs = conditioningDAO.get(sod.getProductCode(), sod.getSupplierCode());
								for (Conditioning c : cs) {
	
									// some cases have an added string like "," "." or " " at the end of the conditioning BUT for the same product.
									if (sod.getUnitLargeScale().startsWith(c.getUnitLargeScale()) || c.getUnitLargeScale().startsWith(sod.getUnitLargeScale())) {
	
										ArrayList<CustomerOrderDetail> cods = customerOrderDetailDAO.getByProductCode(sod.getProductCode(), new StringTokenizer(String.valueOf(supplierOrderNumber)));
										for (CustomerOrderDetail cod : cods) {
	
											if (creationReferences.contains(supplier.getSupplierCode() + "-" + supplierOrderNumber + "-" + cod.getCreationNumber())) {
	
												if ( ! StringUtils.contains(lettersTreated, ";" + cod.getOrderLetter() + ";")) {
													lettersTreated += ";" + cod.getOrderLetter() + ";";
													
													Order order = supplier. new Order();
													//order.setNumber(sod.getNumberOfProductOrdered() * c.getNumberOfUnit());
													order.setNumber(supplierOrderNumber);
													order.setLetter(cod.getOrderLetter());
													order.setCreationNumber(cod.getCreationNumber());
													supplier.addOrder(order);
												}
											}
										}
									}
								}
							/*}*/
						}

					} catch (NumberFormatException nfe) {
						log.error("Exception while getting order letters for the numbers:" + ordersST, nfe);
					}
				}
			} else {
				Order order = supplier. new Order();
				order.setNumber(0);
				order.setLetter("Commande fournisseur inconnue");
				supplier.addOrder(order);
			}

			supplier.setSupplierName(allocation.getSupplierName(supplier.getSupplierCode()));
			
			AllocationEntry entry = new AllocationEntry();
			entry.setDate(Util.formatDate(se.getEntryDate(), "yyyy-MM-dd", "yyyyMMdd"));
			entry.setTime(Util.formatDate(se.getEntryTime(), "hh:mm:ss", "hhmmss"));
			entry.setSupplier(supplier);
			
			entries.add(entry);
		}

		allocation.setEntries(entries);

		return allocation;
	}

	public Allocation getSuppliers(Allocation allocation) {

		this.allocation = allocation;
		
		if (allocation.getSuppliers() == null || allocation.getSuppliers().size() == 0) {
			allocation.setSuppliers((ArrayList<db.supplier.Supplier>) supplierDAO.getAll());
		}
		
		return allocation;
	}
	
	public Allocation getEntriesToAllocate(Allocation allocation, RequestParams requestParams) {
		
		this.allocation = allocation;

		// check requestParam class for the definition of the content of this variable
		if (requestParams.getAllocationEntrySuppliers() == null) {
			return allocation;
		}

		allocation.setCustomerOrders(new ArrayList<CustomerOrder>());
		allocation.setCustomerOrdersToAllocates(new ArrayList<CustomerOrder>());
		allocation.setCustomerOrdersAllocateds(new ArrayList<CustomerOrder>());
		
		// reset selected entries in allocation object
		List<AllocationEntry> entries = allocation.getEntries();
		for (int entryIndex = 0 ; entryIndex < entries.size() ; entryIndex++) {
			entries.get(entryIndex).setSelected(false);
		}

		this.orderTreated = new HashSet<String>();

		// data send from jsp form
		StringTokenizer stOrders = new StringTokenizer(requestParams.getAllocationEntryOrders(), "#");
		while (stOrders.hasMoreElements()) {
			StringTokenizer stOrder = new StringTokenizer((String) stOrders.nextElement(), ";");
			String supplierCode = "";
			int orderNumber = 0;
			String orderLetter = "";
			
			if (stOrder.countTokens() == 3) {
				String strTmp = (String) stOrder.nextElement();
				supplierCode = strTmp.substring(strTmp.indexOf("=") + 1);
				strTmp = (String) stOrder.nextElement();
				orderNumber = Integer.parseInt(strTmp.substring(strTmp.indexOf("=") + 1));
				strTmp = (String) stOrder.nextElement();
				orderLetter= strTmp.substring(strTmp.indexOf("=") + 1);
			}

			allocation.addAllCustomerOrders(getCustomerOrders(requestParams.getDate(), supplierCode, orderNumber, orderLetter));

			// reset selected entries in allocation object
			List<AllocationEntry> currentEntries = allocation.getEntries(supplierCode);
			for (AllocationEntry ae : currentEntries) {
				ae.setSelected(true);
				ae.addProducts(getArrivalProducts(supplierCode, orderNumber, orderLetter,requestParams.getDate()));
			}

		}

		// Retrieve products without orderNumber and orderLetter stored for the requested entries. (substitution products)
		List<AllocationEntry> currentEntries = allocation.getEntries();
		for (AllocationEntry ae : currentEntries) {
			if (ae.isSelected()) {
				ae.addProducts(getArrivalProductsNotRequested(ae.getSupplier().getSupplierCode(), requestParams.getDate()));
				//ae.addProducts(getArrivalProductsForOtherOrder(ae.getSupplier().getSupplierCode(), requestParams.getDate()));
			}
		}

		sortCutomerOrdersByGets();
		
		sortEntryProducts(checkAssignedCutomerOrders());

		return allocation;
		
	}
	/*
	 * this method check the already assigned CutomerOrders with the entry products
	 * 
	 * convert cust orders assigned into arrayList of ProductTraceable
	 */
	private ArrayList<ProductTraceable> checkAssignedCutomerOrders() {
		// go through all customerOrders and search for a treatedEDDId > 0
		
		ArrayList<ProductTraceable> pts = new ArrayList<ProductTraceable>();
		
		List<CustomerOrder> cotas = allocation.getCustomerOrdersToAllocates();

		for (int i = 0; i < cotas.size() ; i++) {
			CustomerOrder co = cotas.get(i);

			if (co.getTreatedEDDId() > 0) {

				ProductTraceable product = new ProductTraceable();

				product.setProductCode(co.getSubstitutionProductCode());
				product.setDescription(co.getSubstitutionProductDescription());

				product.setUnitConditionnement(co.getSubstitutionUnitConditionnement());
				product.setSupplierProductCode(co.getSubstitutionSupplierProductCode());
				product.setSupplierCode(co.getSubstitutionSupplierCode());
				product.setSupplierName(co.getSubstitutionSupplierName());

				Destination dest = product. new Destination();

				dest.setSubstitutionItem(true);

				dest.setCustomerCode(co.getCustomer().getCode());
				dest.setCustomerName(co.getCustomer().getName());
				dest.setNumberOfUnit(co.getSubstitutionNumberOfUnit());
				dest.setPackagingFromBasket(co.getPackagingFromBasketNumber());
				dest.setPackagingToBasket(co.getPackagingToBasketNumber());
				dest.setSubstitutionOfProduct(co.getProductCode());
				dest.setSubstitutionOfProductDescr(co.getProductDescription());
				dest.setSubstitutionOfNumberOfProduct(co.getNumberOfProduct());
				dest.setSubstitutionOfNumberOfUnit(co.getNumberOfUnit());
				dest.setSubstitutionOfUnitConditionnement(co.getUnit());

				product.addDestination(dest);
				
				pts.add(product);
			}
		}
		return pts;
	}

	private ArrayList<CustomerOrder> getCustomerOrders(String currentDate, String supplierCode, int orderNumber, String orderLetter) {
		
		ArrayList<CustomerOrder> cos = new ArrayList<CustomerOrder>();

		// filter with creationNumber treated for the current date and supplier in the planning
		ArrayList<Planning> plannings = planningDAO.getByDateAndSupplier(currentDate, supplierCode);
		HashSet<String> creationNumbers = new HashSet<String>();
		for(Planning p : plannings) {
			//creationNumbers.add(supplierCode + "-" + p.getOrderNumber() + "-" + p.getCreationNumber());
			creationNumbers.add(p.getOrderNumber() + "-" + p.getCreationNumber());
		}
		
		// for each order, get all entries requested
		ArrayList<CustomerOrderDetail> cods = customerOrderDetailDAO.get(orderNumber, orderLetter);
		
		for (CustomerOrderDetail cod : cods) {

			boolean customerOrderConfirmed = false;

			ArrayList<SupplierOrderDetail> sods = supplierOrderDetailDAO.get(supplierCode, orderNumber, cod.getProductCode());
			for (SupplierOrderDetail sod : sods) {
				
				if (StringUtils.equals(cod.getProductCode(), sod.getProductCode())) {
					
					String searchConditioningOnSupplier = sod.getSupplierCode();
					
					/*
					// for supplier == stock, search on all suppliers for the conditioning
					if (StringUtils.equals(searchConditioningOnSupplier, Constants.SUPPLIER_STOCK)) {
						searchConditioningOnSupplier = null;
					}
					*/
					
					ArrayList<Conditioning> cs = conditioningDAO.get(sod.getProductCode(), searchConditioningOnSupplier);
					
					for (Conditioning c : cs) {
	
						// some cases have an added string like "," "." or " " at the end of the conditioning BUT for the same product.
						if (c.getUnitLargeScale().startsWith(sod.getUnitLargeScale()) || sod.getUnitLargeScale().startsWith(c.getUnitLargeScale())/* && sod.getNumberOfProductToOrder() == cod.getTotalNumberOfUnit()*/) {
							customerOrderConfirmed = true;
						}
					}
					
					// if supplier == stock
					if (searchConditioningOnSupplier == null && ! customerOrderConfirmed) {
						for (Conditioning c : cs) {
							
							// some cases have an added string like "," "." or " " at the end of the conditioning BUT for the same product.
							// AND if supplier == stock, we work with based unit instead of the largeScale 
							if ( c.getUnit().startsWith(sod.getUnitLargeScale()) || sod.getUnitLargeScale().startsWith(c.getUnit()) ) {
								customerOrderConfirmed = true;
								break;
							}
						}
					}

					if (customerOrderConfirmed) {
						break;
					}
				}
			}

			
			if (customerOrderConfirmed) {

				//if (creationNumbers.contains(cod.getSupplierCode() + "-" + cod.getOrderNumber() + "-" + cod.getCreationNumber())) {
				if (creationNumbers.contains(cod.getOrderNumber() + "-" + cod.getCreationNumber())) {
					Customer cust = new Customer();
					CustomerOrder co = new CustomerOrder(cod.getCustomerOrderCode(), cod.getCustomerOrderCodeNumber());
	
					co.setSupplierOrderNumber(orderNumber);
					co.setSupplierOrderLetter(orderLetter);
	
					co.setCreationNumber(cod.getCreationNumber());
					co.setFeesType(cod.getFeesType());
					co.setFridge(cod.getFridge());
					co.setNumberGets(cod.getNumberGets());
					co.setNumberOfProduct(cod.getNumberOfProduct());
					co.setNumberOfUnit(cod.getNumberOfUnit());
					co.setPriority(cod.getPriority());
					co.setProductCode(cod.getProductCode());
					co.setProductDescription(cod.getProductDescription());
					co.setSupplierCode(cod.getSupplierCode());

					Product pr = new Product();
					Product.Unit unit = pr.new Unit();
					
					unit.setConditionnement(cod.getUnit());
					unit.setNumber(cod.getNumberOfUnit());

					ArrayList<Conditioning> productConditionings = conditioningDAO.get(cod.getProductCode(), cod.getSupplierCode(), unit);
					if (productConditionings != null) {
						if (productConditionings.size() == 1) {
							co.setSupplierProductCode(productConditionings.get(0).getSupplierProductCode());
						}
					}
					
					//co.setTotalNumberOfUnit(sod.getTotalNumberOfUnit());
					// WARNING the field TotalNumberOfUnit is not always correctly filled by ouistiti
					// re-compute it to be sure.
					co.setTotalNumberOfUnit(cod.getNumberOfProduct() * cod.getNumberOfUnit());
					
					co.setUnit(cod.getUnit());
	
					cust.setCode(prepareOrderDetailDAO.getCustomerCode(
										orderNumber, orderLetter, 
											co.getOrderCode(), 
											co.getOrderNumber(), 
											co.getFeesType()));
	
					Packaging packaging = packagingDAO.get(orderNumber, orderLetter, co.getOrderCode(), co.getOrderNumber(), co.getFeesType());
					co.setPackagingFromBasketNumber(packaging.getFromPackageNumber());
					if (co.getPackagingFromBasketNumber() > 0) {
						co.setPackagingToBasketNumber(packagingDetailDAO.get(orderNumber, orderLetter, co.getOrderCode(), co.getOrderNumber(), co.getFeesType(), co.getPackagingFromBasketNumber()).getToPackageNumber());
					}
	
					cust.setName(customerDAO.get(cust.getCode()).getName());
	
					co.setCustomer(cust);
	
					// if the current customer order is flagged as renewed
					if (cod.getRenewedOrderFlag() == 1) {
						co.setRenewedOrder(true);
					}
	
					// if the current customer order is flagged as deleted
					if (cod.getDeletedOrderFlag() == 1) {
						co.setDeletedOrder(true);
					}
	
					// if <> 0 then there's a substitution product and it has already been treated  
					co.setTreatedEDDId(cod.getTreatedEDDId());
					if (co.getTreatedEDDId() > 0) {
						TreatedEntryDetailDestination tedd = treatedEntryDetailDestinationDAO.get(co.getTreatedEDDId());
						if (tedd != null && tedd.getTreatedEntryDetailId() > 0) {
							TreatedEntryDetail ted = treatedEntryDetailDAO.get(tedd.getTreatedEntryDetailId());
							if (ted != null && ted.getTreatedEntryId() > 0) {
								TreatedEntry te = treatedEntryDAO.get(ted.getTreatedEntryId());
								if (te != null) {
									co.setSubstitutionProductCode(te.getProductCode());
									co.setSubstitutionProductDescription(ted.getDescription());
									co.setSubstitutionNumberOfUnit(tedd.getNumberOfUnit());
									co.setSubstitutionUnitConditionnement(ted.getUnitConditionnement());
									co.setSubstitutionSupplierProductCode(te.getSupplierProductCode());
									co.setSubstitutionSupplierCode(te.getSupplierCode());
									co.setSubstitutionSupplierName(supplierDAO.getByCode(te.getSupplierCode()).getDescription());
								}
							}
						}
					}
	
					cos.add(co);
				}
			}
				//}
			//}
		}

		// sort customer order array by product description
		Collections.sort(cos, new Comparator<CustomerOrder>(){
			public int compare(CustomerOrder co0, CustomerOrder co1) {
				return co0.getProductDescription().compareToIgnoreCase(co1.getProductDescription());
			}
		});

		return cos;
	}
	
	private ArrayList<ProductTraceable> getArrivalProducts(String supplierCode, int orderNumber, String orderLetter, String arrivalDate) {
		ArrayList<ProductTraceable> products = new ArrayList<ProductTraceable>();

		// get all treated entries for each order of the given supplierCode and arrivalDate
		ArrayList<TreatedEntry> treatedEntries = treatedEntryDAO.get(supplierCode, arrivalDate);
		
		for (TreatedEntry te : treatedEntries) {

			TreatedEntryDetail ted = treatedEntryDetailDAO.get(te.getTreatedEntryId());

			if (  
				  Util.contains(new StringTokenizer(ted.getSupplierOrderNumbers(), ","), String.valueOf(orderNumber))// && 
				  //Util.contains(new StringTokenizer(ted.getSupplierOrderLetters(), ","), orderLetter) || true				
			) {

				// avoid duplicate on different order lettres
				// check for already treated products
				if ( ! this.orderTreated.contains(te.getProductCode() + te.getEan()) ) {
				
					ProductTraceable product = new ProductTraceable();
	
					product.setProductCode(te.getProductCode());
					product.setSupplierProductCode(te.getSupplierProductCode());
					product.setSupplierCode(te.getSupplierCode());
					product.setSupplierName(supplierDAO.getByCode(product.getSupplierCode()).getDescription());
					product.setArrivalDate(te.getArrivalDate());
					product.setArrivalTime(te.getArrivalTime());
	
					products.add(fillProduct(product, ted));

					this.orderTreated.add(te.getProductCode() + te.getEan());
				}
			}
		}

		return products;
	}

	private ArrayList<ProductTraceable> getArrivalProductsNotRequested(String supplierCode, String arrivalDate) {
		ArrayList<ProductTraceable> products = new ArrayList<ProductTraceable>();

		// get all treated entries for each order of the given supplierCode and arrivalDate
		ArrayList<TreatedEntry> treatedEntries = treatedEntryDAO.get(supplierCode, arrivalDate);

		for (TreatedEntry te : treatedEntries) {
			TreatedEntryDetail ted = treatedEntryDetailDAO.get(te.getTreatedEntryId());

			if ( StringUtils.isEmpty(ted.getSupplierOrderLetters()) ) {
   				// get only non ordered products

				ProductTraceable product = new ProductTraceable();
	
				product.setProductCode(te.getProductCode());
				product.setSupplierProductCode(te.getSupplierProductCode());
				product.setSupplierCode(te.getSupplierCode());
				product.setSupplierName(supplierDAO.getByCode(product.getSupplierCode()).getDescription());
				product.setArrivalDate(te.getArrivalDate());
				product.setArrivalTime(te.getArrivalTime());

				products.add(fillProduct(product, ted));

			}

		}

		return products;
	}

	private ProductTraceable fillProduct(ProductTraceable product, TreatedEntryDetail ted) {

		product.setDescription(ted.getDescription());
		product.setUnitConditionnement(ted.getUnitConditionnement());
		product.setNumberOfUnit(ted.getNumberOfUnit());
		product.setNumberOfProduct(ted.getNumberOfProduct());
		
		product.setTotalToAllocate(product.getNumberOfUnit() * product.getNumberOfProduct());
		
		ArrayList<TreatedEntryDetailDestination> tedds = treatedEntryDetailDestinationDAO.getAll(ted.getTreatedEntryDetailId());

		for (TreatedEntryDetailDestination tedd : tedds) {

			Destination dest = product. new Destination(false);

			dest.setCustomerCode(tedd.getCustomerCode());
			dest.setCustomerName(customerDAO.get(dest.getCustomerCode()).getName());
			dest.setNumberOfUnit(tedd.getNumberOfUnit());

			// update nbr of product remaining for allocation
			product.setTotalToAllocate(product.getTotalToAllocate() - dest.getNumberOfUnit());

			dest.setPackagingFromBasket(tedd.getPackagingFromBasket());
			dest.setPackagingToBasket(tedd.getPackagingToBasket());

			if ( StringUtils.isNotEmpty(tedd.getSubstitutionOfProduct()) ) {
				
				dest.setSubstitutionOfProduct(tedd.getSubstitutionOfProduct());
				dest.setSubstitutionOfProductDescr(tedd.getSubstitutionOfProductDescr());
				dest.setSubstitutionOfNumberOfProduct(tedd.getSubstitutionOfNumberOfProduct());
				dest.setSubstitutionOfNumberOfUnit(tedd.getSubstitutionOfNumberOfUnit());
				dest.setSubstitutionOfUnitConditionnement(tedd.getSubstitutionOfUnitConditionnement());

				dest.setSubstitutionOfSupplierCode(tedd.getSubstitutionOfSupplierCode());
				dest.setSubstitutionOfSupplierOrderLetter(tedd.getSubstitutionOfSupplierOrderLetter());
				dest.setSubstitutionOfSupplierOrderNumber(tedd.getSubstitutionOfSupplierOrderNumber());

				dest.setSubstitutionOfCustomerOrderCode(tedd.getSubstitutionOfCustomerOrderCode());
				dest.setSubstitutionOfCustomerOrderNumber(tedd.getSubstitutionOfCustomerOrderNumber());
				
				dest.setSubstitutionItem(true);
			}
			
			if (tedd.getStockFlag() == 1) {
				dest.setStockItem(true);
			} else if (tedd.getSupplierReturnsFlag() == 1) {
				dest.setSupplierReturnItem(true);
			}
			
			product.addDestination(dest);
			
			if (dest.isSubstitutionItem()) {
				// need to remove the item from the pending customer array. (as already assigned in the substitution process) 
				removeFromCustomerOrder(product);
			}
		}
		return product;
	}

	private void removeFromCustomerOrder(ProductTraceable product) {
		List<CustomerOrder> cos = allocation.getCustomerOrders();
		
		for (int i=0; i < cos.size(); i++) {
			CustomerOrder co = cos.get(i); 
			for (Destination dest : product.getDestinations()) {
				if ( 
						dest.getCustomerCode().equals(co.getCustomer().getCode()) &&
						dest.getSubstitutionOfProduct().equals(co.getProductCode()) &&
						dest.getSubstitutionOfCustomerOrderNumber() == co.getOrderNumber() &&
						dest.getSubstitutionOfCustomerOrderCode().equals(co.getOrderCode())
					) {
					cos.remove(co);
					i--;
				}
			}
		}

	}
	
	/**
	 * check in the cutomer orders collection if the nbr of gets < the number of unit ordered
	 * and store it in arrayList
	 * customerOrdersAllocated
	 * customerOrdersToAllocate
	 */
	private void sortCutomerOrdersByGets() {
		List<CustomerOrder> cos = allocation.getCustomerOrders();
		
		for (int i=0; i < cos.size(); i++) {
			CustomerOrder co = cos.get(i);

			if (co.getTotalNumberOfUnit() == co.getNumberGets()) {
				if ( ! isExist(allocation.getCustomerOrdersAllocateds(), co)) {
					allocation.addCustomerOrdersAllocated(co);
				}
			} else {
				if ( ! isExist(allocation.getCustomerOrdersToAllocates(), co)) {
					ArrayList<SupplierOrderDetail> sods = supplierOrderDetailDAO.get(Constants.SUPPLIER_STOCK, co.getSupplierOrderNumber(), co.getProductCode());
					if (sods != null && sods.size() > 0) {
						co.setStockOrderNumber(String.valueOf(co.getSupplierOrderNumber()));
						int totalNumberOfOrderStock = 0;
						for(SupplierOrderDetail sod : sods) {
							totalNumberOfOrderStock += sod.getNumberOfProductOrdered();
						}
						co.setTotalNumberOfOrderStock(totalNumberOfOrderStock);
					}
					allocation.addCustomerOrdersToAllocate(co);
				}
			}
		}
	}
	
	private boolean isExist(List<CustomerOrder> cos, CustomerOrder coToCheck) {
		for (CustomerOrder co : cos) {
			if (
				coToCheck.getCreationNumber() == co.getCreationNumber() && 
				(
					(coToCheck.getCustomer() == null && co.getCustomer() == null) 	 || 
					(StringUtils.equals(coToCheck.getCustomer().getCode(),co.getCustomer().getCode()))
				) &&
				coToCheck.getFeesType() == co.getFeesType() &&
				StringUtils.equals(coToCheck.getFly().getAirportCode(), co.getFly().getAirportCode()) &&
				coToCheck.getNumberGets() == co.getNumberGets() &&
				StringUtils.equals(coToCheck.getOrderCode(), co.getOrderCode()) &&
				coToCheck.getOrderNumber() == co.getOrderNumber() &&
				StringUtils.equals(coToCheck.getProductCode(), co.getProductCode()) &&
				coToCheck.getTotalNumberOfUnit() == co.getTotalNumberOfUnit() &&
				StringUtils.equals(coToCheck.getUnit(), co.getUnit())
				) {
				return true;
			}
		}
		return false;
	}
	

	/**
	 * check all entry product and check with the customer order if items has already been attributed
	 * substract attributed nbr of product to the total nbr of entryProduct.
	 * Store the product auto assign into a collection "productsAllocated" of entry
	 * If remains nbr of entryProduct then store it in a collection "productsToAllocate" of entry
	 * 
	 * Store the product with substitution items in the productAssigned collection
	 */
	private void sortEntryProducts(ArrayList<ProductTraceable> ptsExtractedFromDB) {

		for (AllocationEntry entry : allocation.getEntries()) {

			if (entry.isSelected()) {

				for ( ProductTraceable product : entry.getProducts() ) {

					if (product.getTotalToAllocate() > 0) {
						for (ProductTraceable pt : ptsExtractedFromDB) {
	
							if (pt.getSupplierCode() != null) {
	
								if (pt.getSupplierCode().equals(entry.getSupplier().getSupplierCode())) {
	
									if (pt.getProductCode().equals(product.getProductCode())) {
	
										if (pt.getUnitConditionnement().equals(product.getUnitConditionnement())) {

											for (Destination dest : pt.getDestinations()) {
												product.setTotalToAllocate(product.getTotalToAllocate() - dest.getNumberOfUnit());
												product.addDestination(dest);
											}

											// and remove it from the "to assigned array" of customer orders
											List<CustomerOrder> cos = allocation.getCustomerOrdersToAllocates();
											boolean removed = false;
											for (int k = 0 ; k < cos.size() && ! removed; k++) {
												CustomerOrder co = cos.get(k);
												if (pt.getSupplierCode().equals(co.getSubstitutionSupplierCode())) {
													if (pt.getProductCode().equals(co.getSubstitutionProductCode())) {
														if (pt.getUnitConditionnement().equals(co.getSubstitutionUnitConditionnement())) {
															cos.remove(k);
															removed = true;
															if (k > 0) {
																k--;
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public Allocation updateAllocation(Allocation allocation, RequestParams requestParams){
		
		this.allocation = allocation;

		// stock, supplier events
		updateEntries(requestParams);

		// delete, renew events
		updateCustomerOrders(requestParams);
		
		return this.allocation;
	}

	private void updateEntries(RequestParams requestParams) {

		for (AllocationEntry entry : allocation.getEntries()) {

			// extract the entry of the requested supplier
			if ( entry.getSupplier().getSupplierCode().equals(requestParams.getAllocSupplierCode()) ) {

				// extract the product to allocate to the given customer
				// check if the numberOfUnit requested remains free (not allocated)
				for (ProductTraceable product : entry.getProducts()) {
					
					if ( product.getProductCode().equals(requestParams.getAllocProductCode()) ) {
						
						int totalToAllocate = product.getTotalToAllocate();
						int numberOfUnitRequestedToAllocate = requestParams.getAllocNumberOfUnit();

						if ( totalToAllocate >= numberOfUnitRequestedToAllocate ) {
							
							allocation.setUpdated(true);

							Destination dest = product. new Destination(true);
							dest.setCustomerCode(requestParams.getAllocCustomerCode());
							dest.setCustomerName(requestParams.getAllocCustomerName());

							dest.setNumberOfUnit(numberOfUnitRequestedToAllocate);
							
							product.setTotalToAllocate(dest.getNumberOfUnit());

							if ( StringUtils.isNotEmpty(requestParams.getAllocOriginProduct()) ) {

								dest.setSubstitutionItem(true);
								
								dest.setSubstitutionOfProduct(requestParams.getAllocOriginProduct());
								dest.setSubstitutionOfProductDescr(productDAO.getDescription(requestParams.getAllocOriginProduct()));
								dest.setPackagingFromBasket(requestParams.getAllocPackagingFromBasket());
								dest.setPackagingToBasket(requestParams.getAllocPackagingToBasket());
								dest.setSubstitutionOfNumberOfUnit(requestParams.getAllocOriginNumberOfUnit());
								dest.setSubstitutionOfNumberOfProduct(requestParams.getAllocOriginNumberOfProduct());
								dest.setSubstitutionOfUnitConditionnement(requestParams.getAllocOriginUnitConditionnement());
								dest.setSubstitutionOfSupplierCode(requestParams.getAllocOriginSupplierCode());
								dest.setSubstitutionOfSupplierOrderNumber(requestParams.getAllocSupplierOrderNumber());
								dest.setSubstitutionOfSupplierOrderLetter(requestParams.getAllocSupplierOrderLetter());
								dest.setSubstitutionOfCustomerOrderCode(requestParams.getAllocCustomerOrderCode());
								dest.setSubstitutionOfCustomerOrderNumber(requestParams.getAllocCustomerOrderNumber());

								// remove the product from the array of customer orders (as it is now substituated) 
								removeCustomerOrder(requestParams.getAllocCustomerCode(), requestParams.getAllocCustomerOrderCode(), requestParams.getAllocCustomerOrderNumber(), dest.getSubstitutionOfProduct());

								product.addDestination(dest);

							} else {

								if (requestParams.getAllocationStep().equals("supplier")) {
										dest.setSupplierReturnItem(true);
								}
								if (requestParams.getAllocationStep().equals("stock")) {
									dest.setStockItem(true);
								}

								boolean updated = false;
								for (Destination originalDest : product.getDestinations()) {

									if ( (dest.isStockItem() && originalDest.isStockItem()) || 
										 (dest.isSupplierReturnItem() && originalDest.isSupplierReturnItem())) {

										originalDest.setNumberOfUnit(originalDest.getNumberOfUnit() + dest.getNumberOfUnit());
										updated = true;
										break;
									}
								}

								if ( ! updated ) {
									product.addDestination(dest);
								}

							}

							product.setTotalToAllocate(totalToAllocate - numberOfUnitRequestedToAllocate);

							break;
						}

					}
				}
			}
		}
	}

	private void updateCustomerOrders(RequestParams requestParams) {

		List<CustomerOrder> cotas = allocation.getCustomerOrdersToAllocates();

		for (int i=0 ; i < cotas.size() ; i++) {

			CustomerOrder co = cotas.get(i);

			if (co.getCustomer().getCode().equals(requestParams.getAllocCustomerCode()) && co.getOrderCode().equals(requestParams.getAllocCustomerOrderCode()) && co.getOrderNumber() == requestParams.getAllocCustomerOrderNumber() && co.getProductCode().equals(requestParams.getAllocOriginProduct())) {
				if (requestParams.getAllocationStep().equals("deleteOrder")) {
					co.setDeletedOrder(true);
					allocation.setUpdated(true);
				}
				if (requestParams.getAllocationStep().equals("renewOrder")) {
					co.setRenewedOrder(true);
					allocation.setUpdated(true);
				}
				break;
			}
		}

		allocation.setCustomerOrdersToAllocates(cotas);

	}

	private void removeCustomerOrder(String custCode, String custOrderCode, int custOrderNumber, String productCode) {

		List<CustomerOrder> cotas = allocation.getCustomerOrdersToAllocates();
		
		for (int i=0 ; i < cotas.size() ; i++) {

			CustomerOrder co = cotas.get(i);

			if (co.getCustomer().getCode().equals(custCode) && co.getOrderCode().equals(custOrderCode) && co.getOrderNumber() == custOrderNumber && co.getProductCode().equals(productCode)) {
				cotas.remove(co);
				break;
			}
		}
		allocation.setCustomerOrdersToAllocates(cotas);
	}

	public Allocation confirmAllocation(Allocation allocation) {
		// this method will update the DB with the substitution information updated
		
		this.allocation = allocation;
		
		confirmEntries();
		confirmCustomerOrders();
		
		return this.allocation;
	}
	
	private void confirmEntries() {
	
		Session session = getSession();
		Transaction trx = session.getTransaction();
		trx.begin();
		log.debug("Hibernate transaction has been created");
		
		for (AllocationEntry entry : allocation.getEntries()) {
			
			if (entry.isSelected()) {

				for (ProductTraceable product : entry.getProducts()) {

					for (Destination dest : product.getDestinations()) {

						if (dest.isModified() ) {

							TreatedEntry te = treatedEntryDAO.getUnique(entry.getSupplier().getSupplierCode(), entry.getDate(), product.getProductCode(), session);
							TreatedEntryDetail ted = treatedEntryDetailDAO.get(te.getTreatedEntryId(), session);

							boolean updated = false;

							int treatedEDDId = 0;
							String airportCode = "";
							String lta = "";
							String flight = "";

							// search existing destination and update OR create a new destination
							ArrayList<TreatedEntryDetailDestination> tedds = treatedEntryDetailDestinationDAO.getAll(ted.getTreatedEntryDetailId(), session);

							for (TreatedEntryDetailDestination tedd : tedds) {

								if ( dest.getCustomerCode().equals(tedd.getCustomerCode()) ) {

									if ( airportCode.equals("") ) {
										airportCode = tedd.getAirportCode();
										lta = tedd.getLta();
										flight = tedd.getFlight();
									}

									if ( (	dest.isSubstitutionItem() && 
											StringUtils.isNotEmpty(dest.getSubstitutionOfProduct()) && 
											tedd.getSubstitutionOfProduct().equals(dest.getSubstitutionOfProduct()) 
										  ) 
										  ||
										  (dest.isStockItem() && tedd.getStockFlag() == 1)
										  ||
										  (dest.isSupplierReturnItem() && tedd.getSupplierReturnsFlag() == 1) )
										{
										// update the already inserted product of substitution for this customer
										tedd.setNumberOfUnit(tedd.getNumberOfUnit() + dest.getNumberOfUnit());

										treatedEntryDetailDestinationDAO.update(tedd, session);

										treatedEDDId = tedd.getId();

										if (dest.isStockItem()) {

											StockEntry se = stockEntryDAO.get(tedd.getId(), session);
											se.setNumberOfProducts(tedd.getNumberOfUnit());
											stockEntryDAO.update(se, session);
											
										} else if ( dest.isSupplierReturnItem() ){

											SupplierReturnsEntry sre = supplierReturnsEntryDAO.get(tedd.getId(), session);
											sre.setNumberOfProducts(tedd.getNumberOfUnit());
											supplierReturnsEntryDAO.update(sre, session);

										}

										updated = true;
										break;

									} else {
										//create a new dest for this customer
									}
								}
							}
							if ( ! updated ) {

								// create a new destination
								TreatedEntryDetailDestination newTedd = new TreatedEntryDetailDestination();
							
								newTedd.setTreatedEntryDetailId(ted.getTreatedEntryDetailId());

								if ( dest.isSubstitutionItem() ) {
									newTedd.setCustomerCode(dest.getCustomerCode());
								} else if ( dest.isStockItem()){
									newTedd.setCustomerCode("");
									newTedd.setStockFlag(1);
								} else if ( dest.isSupplierReturnItem() ) {
									newTedd.setCustomerCode("");
									newTedd.setSupplierReturnsFlag(1);
								}
								
								newTedd.setAirportCode(airportCode);
								newTedd.setLta(lta);
								newTedd.setFlight(flight);

								newTedd.setNumberOfUnit(dest.getNumberOfUnit());

								if ( dest.isSubstitutionItem() && 
									 ! product.getProductCode().equals(dest.getSubstitutionOfProduct())
								   ) {

									newTedd.setSubstitutionOfProduct(dest.getSubstitutionOfProduct());
									newTedd.setSubstitutionOfProductDescr(dest.getSubstitutionOfProductDescr());
									newTedd.setSubstitutionOfUnitConditionnement(dest.getSubstitutionOfUnitConditionnement());
									newTedd.setSubstitutionOfNumberOfUnit(dest.getSubstitutionOfNumberOfUnit());
									newTedd.setSubstitutionOfNumberOfProduct(dest.getSubstitutionOfNumberOfProduct());
									newTedd.setSubstitutionOfCustomerOrderCode(dest.getSubstitutionOfCustomerOrderCode());
									newTedd.setSubstitutionOfCustomerOrderNumber(dest.getSubstitutionOfCustomerOrderNumber());
									newTedd.setSubstitutionOfSupplierCode(dest.getSubstitutionOfSupplierCode());
									newTedd.setSubstitutionOfSupplierOrderLetter(dest.getSubstitutionOfSupplierOrderLetter());
									newTedd.setSubstitutionOfSupplierOrderNumber(dest.getSubstitutionOfSupplierOrderNumber());

								}

								newTedd.setAllocationDate(Util.getNowFormated("yyyyMMdd"));

								treatedEntryDetailDestinationDAO.insert(newTedd, session);

								treatedEDDId = newTedd.getId();

								if ( dest.isStockItem() ) {

									StockEntry se = new StockEntry();
									se.setNumberOfProducts(newTedd.getNumberOfUnit());
									se.setProductCode(te.getProductCode());
									se.setTreatedEntryDetailDestinationId(treatedEDDId);
									
									stockEntryDAO.insert(se, session);

								} else if ( dest.isSupplierReturnItem() ) {

									SupplierReturnsEntry sre = new SupplierReturnsEntry();
									sre.setNumberOfProducts(newTedd.getNumberOfUnit());
									sre.setProductCode(te.getProductCode());
									//???setSupplierProductCode
									sre.setSupplierCode(te.getSupplierCode());
									sre.setTreatedEntryDetailDestinationId(treatedEDDId);
									
									supplierReturnsEntryDAO.insert(sre, session);
									
								}

							}
							
							if (dest.isSubstitutionItem() && treatedEDDId > 0) {
								// now update the customerOrderDetail table (CommandesLignes) with the ID of the tedd just inserted/updated
								ArrayList<CustomerOrderDetail> cods = customerOrderDetailDAO.get(dest.getSubstitutionOfProduct(), String.valueOf(dest.getSubstitutionOfSupplierOrderNumber()), dest.getSubstitutionOfSupplierOrderLetter(), dest.getSubstitutionOfCustomerOrderCode(), dest.getSubstitutionOfCustomerOrderNumber(), session);
								for (CustomerOrderDetail cod : cods) {
									if (cod.getNumberGets() == 0) {
										cod.setTreatedEDDId(treatedEDDId);
										customerOrderDetailDAO.update(cod, session);
									}
								}
							}
						}
					}
				}
			}
		}
		log.debug("Before submitting the Hibernate transaction");
		trx.commit();
		session.close();
		log.debug("Hibernate transaction has been submited and closed");
		
		allocation.setUpdated(false);
	}

	private void confirmCustomerOrders() {

		Session session = getSession();
		Transaction trx = session.getTransaction();
		trx.begin();
		log.debug("Hibernate transaction has been created");

		for (CustomerOrder co : allocation.getCustomerOrdersToAllocates()) {

			if (co.isDeletedOrder() || co.isRenewedOrder()) {
				ArrayList<CustomerOrderDetail> sods = customerOrderDetailDAO.get(co.getProductCode(), String.valueOf(co.getSupplierOrderNumber()), co.getSupplierOrderLetter(), co.getOrderCode(), co.getOrderNumber(), session);

				for (CustomerOrderDetail sod : sods) {

					if (co.isDeletedOrder() && sod.getDeletedOrderFlag() != 1) {

						sod.setDeletedOrderFlag(1);
						customerOrderDetailDAO.update(sod, session);

					} else if (co.isRenewedOrder() && sod.getRenewedOrderFlag() != 1) {

						sod.setRenewedOrderFlag(1);
						customerOrderDetailDAO.update(sod, session);

					}
				}
			}
		}

		log.debug("Before submitting the Hibernate transaction");
		trx.commit();
		session.close();
		log.debug("Hibernate transaction has been submited and closed");

		allocation.setUpdated(false);
	}
	
	public Event cleanDb(Allocation allocation) {

		Session session = getSession();
		Transaction transaction = session.getTransaction();
		transaction.begin();

		// remove all old entries from db
		//treatedEntryDAO.cleanDb(session);

		transaction.commit();
		
		return new Event("removeEntry", "noMoreEntry");
	}

	public void setSupplierEntryDAO(ISupplierEntryDAO supplierEntryDAO) {
		this.supplierEntryDAO = supplierEntryDAO;
	}

	public void setSupplierDAO(ISupplierDAO supplierDAO) {
		this.supplierDAO = supplierDAO;
	}

	public void setPrepareOrderDAO(IPrepareOrderDAO prepareOrderDAO) {
		this.prepareOrderDAO = prepareOrderDAO;
	}

	public void setCustomerOrderDetailDAO(
			ICustomerOrderDetailDAO customerOrderDetailDAO) {
		this.customerOrderDetailDAO = customerOrderDetailDAO;
	}

	public void setPrepareOrderDetailDAO(
			IPrepareOrderDetailDAO prepareOrderDetailDAO) {
		this.prepareOrderDetailDAO = prepareOrderDetailDAO;
	}

	public void setTreatedEntryDAO(ITreatedEntryDAO treatedEntryDAO) {
		this.treatedEntryDAO = treatedEntryDAO;
	}

	public void setTreatedEntryDetailDestinationDAO(
			ITreatedEntryDetailDestinationDAO treatedEntryDetailDestinationDAO) {
		this.treatedEntryDetailDestinationDAO = treatedEntryDetailDestinationDAO;
	}

	public void setCustomerDAO(ICustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	public void setTreatedEntryDetailDAO(
			ITreatedEntryDetailDAO treatedEntryDetailDAO) {
		this.treatedEntryDetailDAO = treatedEntryDetailDAO;
	}

	public void setProductDAO(IProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	public void setPackagingDAO(IPackagingDAO packagingDAO) {
		this.packagingDAO = packagingDAO;
	}

	public void setPackagingDetailDAO(IPackagingDetailDAO packagingDetailDAO) {
		this.packagingDetailDAO = packagingDetailDAO;
	}

	public void setStockEntryDAO(IStockEntryDAO stockEntryDAO) {
		this.stockEntryDAO = stockEntryDAO;
	}

	public void setSupplierReturnsEntryDAO(
			ISupplierReturnsEntryDAO supplierReturnsEntryDAO) {
		this.supplierReturnsEntryDAO = supplierReturnsEntryDAO;
	}

	public void setPlanningDAO(IPlanningDAO planningDAO) {
		this.planningDAO = planningDAO;
	}

	public void setSupplierOrderDetailDAO(
			ISupplierOrderDetailDAO supplierOrderDetailDAO) {
		this.supplierOrderDetailDAO = supplierOrderDetailDAO;
	}

	public void setConditioningDAO(IConditioningDAO conditioningDAO) {
		this.conditioningDAO = conditioningDAO;
	}
}
