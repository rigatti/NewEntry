package org.belex.product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.belex.arrival.Arrival;
import org.belex.arrival.ArrivalBusiness;
import org.belex.product.Product.Unit;
import org.belex.util.Constants;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import db.entry.treated.IStockEntryDAO;
import db.entry.treated.ISupplierReturnsEntryDAO;
import db.entry.treated.ITreatedEntryDAO;
import db.entry.treated.temp.ITreatedEntryTempDAO;
import db.product.Conditioning;
import db.product.Description;
import db.product.IConditioningDAO;
import db.product.IDescriptionDAO;
import db.product.IProductDAO;

public class ProductBusiness extends HibernateDaoSupport implements IProductBusiness {
	
	private static final Category log = Logger.getLogger(ArrivalBusiness.class);
	
	IProductDAO productDAO;
	IConditioningDAO conditioningDAO;
	IDescriptionDAO descriptionDAO;
	ITreatedEntryDAO treatedEntryDAO;
	ITreatedEntryTempDAO treatedEntryTempDAO;
	ISupplierReturnsEntryDAO supplierReturnsEntryDAO;
	IStockEntryDAO stockEntryDAO;
	
	
	public static final String SEARCH_TYPE_CODE = "1";
	public static final String SEARCH_TYPE_LABEL = "2";
	
	public static final String SEARCH_NOT_EXACT_MATCH = "0";
	public static final String SEARCH_EXACT_MATCH = "1";
	
	public Arrival newProduct(Product product, Arrival arrival) {
		Unit unit = product. new Unit();
		unit.setConditionnement(product.getNewProductUnitConditionnement());
		unit.setNumber(Integer.parseInt(product.getNewProductNumberOfUnit()));

		String productCode = createProduct(product.getNewProductLabel(), product.getNewProductEan(), unit, product.getNewProductSupplierCode(), "");

		if ( StringUtils.isNotEmpty(productCode) ) {
			arrival.setSearchValue(productCode);
			arrival.setSearchType("1");
			arrival.setSearchExactMatch("1");
			arrival.setSearchOnSupplier(product.getNewProductSupplierCode());
		}

		return arrival;
	}

	public void updateProductUnit(Arrival arrival) {
		
		Product currentProduct = arrival.getEntry().getProduct();
		String productCode = currentProduct.getProductCode();
		Product.Unit unit = fillSelectedUnit(arrival);
		
		String supplierCode = arrival.getSupplier().getSupplierCode();

		ArrayList<Conditioning> conditionings = conditioningDAO.get(productCode, supplierCode, unit);
			
		if (conditionings.size() > 0) {
			
			Conditioning conditioning = conditionings.get(0);
			conditioning.setEan(currentProduct.getNewProductEan());

			conditioningDAO.update(conditioning);
	
		} else {
			
			// does this product exist for another supplier (same unit) ?
			conditionings = conditioningDAO.get(productCode, null, unit);

			if (conditionings.size() > 0) {
				
				createProduct(productDAO.getDescription(productCode), currentProduct.getNewProductEan(), unit, supplierCode, productCode);

			} else {
				// create a new unit for this product
				Conditioning conditioning = new Conditioning();
				conditioning.setProductCode(productCode);
				conditioning.setProductCode(productCode);
				conditioning.setPriority(conditioningDAO.getNextPriority(productCode, supplierCode));
				conditioning.setSupplierCode(supplierCode);
				conditioning.setEan(currentProduct.getNewProductEan());
				conditioning.setUnit(unit.getConditionnement());
				conditioning.setNumberOfUnit(unit.getNumber());

				conditioningDAO.insert(conditioning);
			}
		}
	}

	public String createProduct(String description, String ean, Unit unit, String supplierCode, String alternateProductCode){
		
		String productCode = String.valueOf(productDAO.getMaxId());
		productCode += "TBD       ";
		productCode = productCode.substring(0,10).trim();
		
		db.product.Product dbProduct = new db.product.Product();
		dbProduct.setDescription(description);
		dbProduct.setProductCode(productCode);
		dbProduct.setAlternateProductCode(alternateProductCode);
		dbProduct.setUnitConditioning(unit.getConditionnement());

		boolean success = true;
		Session session = getSession();
		Transaction trx = session.getTransaction();
		trx.begin();

		try {
			success = productDAO.insert(dbProduct, session);

			if (success) {
				Conditioning conditioning = new Conditioning();
				conditioning.setProductCode(productCode);
				conditioning.setPriority(1); //getNextPriority(productCode, supplierCode)
				conditioning.setSupplierCode(supplierCode);
				conditioning.setEan(ean);
				conditioning.setUnit(unit.getConditionnement());
				conditioning.setNumberOfUnit(unit.getNumber());
				
				success = conditioningDAO.insert(conditioning, session);
				
				if (success) {
					Description DBDescription = new Description();
					DBDescription.setProductCode(productCode);
					DBDescription.setLanguage("ENG");
					DBDescription.setDescription(description);

					success = descriptionDAO.insert(DBDescription, session);
				}
			}
			
		} catch (Exception e) {
			log.error("Create new product", e);
			success = false;
		}

		if (success) {
			trx.commit();
		} else {
			trx.rollback();
			productCode = "";
		}
		
		return productCode;
	}
	public Vector<Product> getProducts(String searchValue, String searchExactMatch, String searchType, String searchOnSupplier) {

		Vector<Product> result = new Vector<Product>();
		
		 String stockFilteredSupplierCode = searchOnSupplier;
		 									//(StringUtils.equals(searchOnSupplier, Constants.SUPPLIER_STOCK)?"": searchOnSupplier);

		if ( ! searchValue.equals("")) {

			if (searchType.equals(SEARCH_TYPE_CODE)) {

				ArrayList<db.product.Conditioning> conditioningsDB = conditioningDAO.getProductsByEan(searchValue, stockFilteredSupplierCode);

				if (conditioningsDB.size() == 0) {
					conditioningsDB = conditioningDAO.getProductsByCode(searchValue, searchExactMatch.equals("1"),  stockFilteredSupplierCode);				
				}
				if (conditioningsDB != null) {
					for (int i=0 ; i < conditioningsDB.size() ; i++) {
						db.product.Conditioning currentconditioningDB = conditioningsDB.get(i);
	
						// search if this productCode has already been treated. 
						// If true => update vector of units if needed
						// Else add a new product in the vResult
						Product currentProduct = null;
						int resultIndex = -1;
						for (int j = 0; j < result.size(); j++) {
							if (result.get(j).getProductCode().equals(currentconditioningDB.getProductCode())){
								currentProduct = result.get(j);
								resultIndex = j;
							}
						}
	
						if (currentProduct == null) {
							currentProduct = new Product(currentconditioningDB.getProductCode());
						}	
	
						currentProduct.setDescription(productDAO.getDescription(currentProduct.getProductCode()));
	
						Product.Unit unit = currentProduct. new Unit();
						unit.setSupplierCode(currentconditioningDB.getSupplierCode());
						unit.setEan(currentconditioningDB.getEan());
						unit.setConditionnement(currentconditioningDB.getUnit());
	
						if (StringUtils.equals(searchOnSupplier, Constants.SUPPLIER_STOCK) ) {
							unit.setNumber(1);
						} else {
							unit.setNumber(currentconditioningDB.getNumberOfUnit());
						}
	
						unit.setDateLastMofication(currentconditioningDB.getDateLastModification());
	
						// Avoid duplicate conditionnement
						boolean ignoreCurrentUnit = false;
						Vector<Unit> treatedUnits = currentProduct.getUnits();
						for (int j = 0; j < treatedUnits.size() && ! ignoreCurrentUnit; j++) {
							Unit treatedUnit = treatedUnits.get(j);
							if (
								treatedUnit.getConditionnement().equals(unit.getConditionnement()) && 
								treatedUnit.getNumber() == unit.getNumber()
								)
							{
								if ( ! treatedUnit.getSupplierCode().equals(unit.getSupplierCode())) {
									//if same unit for different supplier 
									// => remove supplier reference AND 
									// => remove ean if different ean exist
									treatedUnit.setSupplierCode("");
									if ( ! treatedUnit.getEan().equals(unit.getEan()) ) {
										treatedUnit.setEan("");
									}
	
									// previously : keep all references
									//treatedUnit.setSupplierCode(treatedUnit.getSupplierCode() + ";" + unit.getSupplierCode());
									//treatedUnit.setEan(treatedUnit.getEan() + ";" + unit.getEan());
	
									treatedUnits.setElementAt(treatedUnit, j);
									currentProduct.setUnits(treatedUnits);
								}
								ignoreCurrentUnit = true;
							}
						}
		
						if (! ignoreCurrentUnit) {
							currentProduct.addUnit(unit);
						}
		
						if (resultIndex > -1) {
							result.setElementAt(currentProduct, resultIndex);
						} else { 
							result.add(currentProduct);	
						}
					}
				}
	
			}
			
			if (searchType.equals(SEARCH_TYPE_LABEL)) {
				ArrayList<db.product.Product> productsDB = productDAO.getProductsByDescription(searchValue,  stockFilteredSupplierCode);
	
				for (int i = 0; i < productsDB.size(); i++) {
					String productCode = productsDB.get(i).getProductCode();
					result.addAll(this.getProducts(productCode, "1", SEARCH_TYPE_CODE,  stockFilteredSupplierCode));
				}
	
			}
		}
		return result;
	}
	/*
	public Product buildObject(Product product){
		if (product == null){
			return new Product();
		}
		return product;
	}
	*/
	public void setProductDAO(IProductDAO pDao) {
		productDAO = pDao;
	}

	public Product.Unit fillSelectedUnit(Arrival arrival) {
		Vector<Unit> units = arrival.getEntry().getProduct().getUnits();
		Product.Unit unit = null;
		for (int i = 0; i < units.size(); i++) {
			if (units.get(i).isSelected()) {
				unit = units.get(i);
				
				// can append when multiple unit with same conditionnement for different suppliers
				if (unit.getSupplierCode().equals("")){
					unit.setSupplierCode(arrival.getSupplier().getSupplierCode());
				}
				if (unit.getConditionnement().equals("")) {
					unit.setConditionnement(arrival.getEntry().getProduct().getNewProductUnitConditionnement());
				}
				if (unit.getNumber() == 0) {
					unit.setNumber(Integer.parseInt(arrival.getEntry().getProduct().getNewProductNumberOfUnit()));
				}
				unit.setEan(arrival.getEntry().getProduct().getNewProductEan());
				
				break;
			}
		}
		return unit;
	}
	
	public void updateProductCode(String oldCode, String newCode) {

		if (StringUtils.isNotEmpty(oldCode) && StringUtils.isNotEmpty(newCode)) {
			
			newCode = newCode.toUpperCase();
			
			db.product.Product oldDbProduct = productDAO.getProductByCode(oldCode, true);
	
			// if oldCode exist AND newCode doesn't => update all tables
			if ( oldDbProduct != null ) {
				db.product.Product newDbProduct = productDAO.getProductByCode(newCode, false);
				
				if ( newDbProduct == null ) {
					
					updateNewProduct(oldCode, newCode);

				} else {
					
					updateExistingProduct(oldDbProduct, newDbProduct);

				}
			}
		} // if is not empty 
	}

	private void updateExistingProduct(db.product.Product oldDbProduct, db.product.Product newDbProduct) {
		// 	- check if old unit == new unit 
		//		If true : 
		//			update tracability table and delete from conditioning and description tables
		//		If false : 
		//			create new unit for this product and update all tables.
		if (oldDbProduct != null && newDbProduct != null) {
			
			String newProductCode = newDbProduct.getProductCode();
			String oldProductCode = oldDbProduct.getProductCode();
			
			/*
			boolean sameUnitExist = false;
			ArrayList<Conditioning> oldProductConditionning = conditioningDAO.getProductsByCode(oldProductCode, true, null);
			ArrayList<Conditioning> newProductConditionning = conditioningDAO.getProductsByCode(newProductCode, true, null);			

			for (Conditioning newConditioning : newProductConditionning) {
				for (Conditioning oldConditioning : oldProductConditionning) {
					if ( oldConditioning.getUnit().equals(newConditioning.getUnit()) &&
						 oldConditioning.getNumberOfUnit() == newConditioning.getNumberOfUnit() && 
						 oldConditioning.getSupplierCode().equals(newConditioning.getSupplierCode())
						) {
						sameUnitExist = true;
						break;
					}
				}
				if (sameUnitExist) {
					break;
				}
			}
			*/

			// force true fo avoid update conditioning management. 
			// => Will be managed in a futur development phase
			boolean sameUnitExist = true;

			boolean success = false;
			Session session = getSession();
			Transaction trx = session.getTransaction();
			trx.begin();

			success = productDAO.delete(oldDbProduct, session);

			if (success) {
				
				if (sameUnitExist) {
					success = conditioningDAO.deleteProductCode(oldProductCode, session);
				} else {
					success = conditioningDAO.updateProductCode(oldProductCode, newProductCode, session);
				}

				if (success) {
					success = descriptionDAO.deleteProductCode(oldProductCode, session);

					if (success) {
						success = treatedEntryDAO.updateProductCode(oldProductCode, newProductCode, session);
						
						if (success) {
							success = treatedEntryTempDAO.updateProductCode(oldProductCode, newProductCode, session);

							if (success) {
								success = supplierReturnsEntryDAO.updateProductCode(oldProductCode, newProductCode, session);

								if (success) {
									success = stockEntryDAO.updateProductCode(oldProductCode, newProductCode, session);
								}
							}
						}
					}
				}
			}

			if (success) {
				trx.commit();
			} else {
				trx.rollback();
			}		

		}
	}

	private void updateNewProduct(String oldCode, String newCode) {
		boolean success = false;
		Session session = getSession();
		Transaction trx = session.getTransaction();
		trx.begin();
		
		db.product.Product oldDbProduct = productDAO.getProductByCode(oldCode, true);
		oldDbProduct.setProductCode(newCode);
		success = productDAO.update(oldDbProduct, session);

		if (success) {
			success = conditioningDAO.updateProductCode(oldCode, newCode, session);

			if (success) {
				success = descriptionDAO.updateProductCode(oldCode, newCode, session);

				if (success) {
					success = treatedEntryDAO.updateProductCode(oldCode, newCode, session);
					
					if (success) {
						success = treatedEntryTempDAO.updateProductCode(oldCode, newCode, session);

						if (success) {
							success = supplierReturnsEntryDAO.updateProductCode(oldCode, newCode, session);

							if (success) {
								success = stockEntryDAO.updateProductCode(oldCode, newCode, session);
							}
						}
					}
				}
			}
		}

		if (success) {
			trx.commit();
		} else {
			trx.rollback();
		}		
	}

	public void deleteProductCode(String productCode) {

		if ( StringUtils.isNotEmpty(productCode) ) {

			db.product.Product dbProduct = productDAO.getProductByCode(productCode.toUpperCase(), true);

			// if oldCode exist AND newCode doesn't => update all tables
			if ( dbProduct != null ) {
	
				boolean success = false;
				Session session = getSession();
				Transaction trx = session.getTransaction();
				trx.begin();
				
				success = productDAO.delete(dbProduct, session);
	
				if (success) {
					success = conditioningDAO.deleteProductCode(productCode.toUpperCase(), session);
	
					if (success) {
						success = descriptionDAO.deleteProductCode(productCode.toUpperCase(), session);
	
					}
				}
	
				if (success) {
					trx.commit();
				} else {
					trx.rollback();
				}
			}
		} // if is not empty 
	}

	public void setConditioningDAO(IConditioningDAO conditioningDAO) {
		this.conditioningDAO = conditioningDAO;
	}

	public void setDescriptionDAO(IDescriptionDAO descriptionDAO) {
		this.descriptionDAO = descriptionDAO;
	}

	public void setStockEntryDAO(IStockEntryDAO stockEntryDAO) {
		this.stockEntryDAO = stockEntryDAO;
	}

	public void setSupplierReturnsEntryDAO(
			ISupplierReturnsEntryDAO supplierReturnsEntryDAO) {
		this.supplierReturnsEntryDAO = supplierReturnsEntryDAO;
	}

	public void setTreatedEntryDAO(ITreatedEntryDAO treatedEntryDAO) {
		this.treatedEntryDAO = treatedEntryDAO;
	}

	public void setTreatedEntryTempDAO(ITreatedEntryTempDAO treatedEntryTempDAO) {
		this.treatedEntryTempDAO = treatedEntryTempDAO;
	}
}
