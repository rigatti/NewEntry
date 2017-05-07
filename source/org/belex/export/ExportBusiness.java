package org.belex.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.belex.arrival.ArrivalBusiness;
import org.belex.util.DataSender;
import org.belex.util.FileSender;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import db.customer.Customer;
import db.customer.ICustomerDAO;
import db.editiontype.EditionType;
import db.editiontype.IEditionTypeDAO;
import db.family.FamilyLevel1;
import db.family.FamilyLevel3;
import db.product.Conditioning;
import db.product.IConditioningDAO;
import db.product.IProductDAO;
import db.product.Product;

public class ExportBusiness extends HibernateDaoSupport implements IExportBusiness {
	
	private static final Category log = Logger.getLogger(ArrivalBusiness.class);
	
	IProductDAO productDAO;
	IConditioningDAO conditioningDAO;
	
	ICustomerDAO customerDAO;
	IEditionTypeDAO editionTypeDAO;

	public Export productExport(Export export, String productReference, String productExportWithImage) {
		if (export == null) {
			export = new Export();
		}
		log.info("Reference:" + productReference);
		String imagePath = "";
		if (StringUtils.equals(productExportWithImage, "1")) {
			
			imagePath = new FileSender().uploadImage(StringUtils.upperCase(productReference));
			
		}
		if (StringUtils.isNotBlank(imagePath)) {
			
			List<?> resultFromDb = productDAO.getProductsToExportByReference(productReference);
			extractData(export, resultFromDb);
			List<ProductToExport> products = export.getProducts();
			
			log.info("Reference:" + productReference + ". Number of products to treat:" + (products == null?0:products.size()) );
			
			String listOfImpactedReferences = "";
			for (ProductToExport productToExport : products) {
				listOfImpactedReferences += encode(productToExport.getProductCode() + "_" + productToExport.getEditionTypeLabel());
				if (StringUtils.isNotBlank(productToExport.getEditionTypeAdditionalLabel())) {
					listOfImpactedReferences += "_" + productToExport.getEditionTypeAdditionalLabel();
				}
				listOfImpactedReferences += ",";
			}
			listOfImpactedReferences = StringUtils.removeEnd(listOfImpactedReferences,",");
			
			log.debug("Reference:" + productReference + ". ListOfImpactedReferences:" + listOfImpactedReferences );
			
			for (ProductToExport productToExport : products) {
				StringBuffer data = new StringBuffer();

				data.append("dateLastModif=" + encode(StringUtils.substring(productToExport.getDateLastUpdate(), 0, 19)));
				data.append("&");
				data.append("impactedReferences=" + listOfImpactedReferences);
				data.append("&");
				data.append("referenceRoot=" + encode(productToExport.getProductCode()));
				data.append("&");
				data.append("reference=" + encode(productToExport.getProductCode() + "_" + productToExport.getEditionTypeLabel()));
				if (StringUtils.isNotBlank(productToExport.getEditionTypeAdditionalLabel())) {
					data.append("_" + productToExport.getEditionTypeAdditionalLabel());
				}

				data.append("&");
				data.append("isPlane=" + (productToExport.isPlaneMandatory()?1:0) );
				
				data.append("&");
				data.append("name=" + encode(productToExport.getDescription_fr()));
				data.append("&");
				data.append("description=" + encode(productToExport.getTotalConditioning()));
				data.append("&");
				data.append("price=" + productToExport.getTotalPrice());
				data.append("&");
				data.append("category=" + productToExport.getFamilyCode());
				data.append("&");
				data.append("sortingOrder=" + productToExport.getSortingOrder());
				data.append("&");
				data.append("reduction_percent=" + productToExport.getMarginForStandardCustomer());
				data.append("&");
	
				if (productToExport.isPro()) {
					data.append("reduction_group=" + encode("PRO"));
					data.append("&");
				}
	
				data.append("assignment_group=" + productToExport.getEditionTypeId());
				if (StringUtils.isNotBlank(productToExport.getEditionTypeAdditionalId())) {
					data.append("," + productToExport.getEditionTypeAdditionalId());
				}
				data.append("&");
				data.append("newimage=1");
				data.append("&");
				data.append("image=" + encode(imagePath));

				DataSender ds = new DataSender();
				boolean result = ds.sendProductUpdate(data.toString());
			}
		} else {
			// img not detected
			export.setErrorMessage("Image non transférée sur le site");
			log.error("Reference:" + productReference + " IMG not detected or not transfered to site.");
		}
		return export;
	}

	private void extractData(Export export, List<?> resultFromDb) {
		for (Object row : resultFromDb) {
			Conditioning currentConditioningDb = null;
			Product currentProductDb = null;
			EditionType currentEditionTypeDb = null;
			FamilyLevel1 currentFamilyLevel1Db = null;
			FamilyLevel3 currentFamilyLevel3Db = null;
			
			Object[] recordArray = (Object[]) row;
			for (Object object : recordArray) {
				if (object instanceof Conditioning) {
					currentConditioningDb = (Conditioning) object;
				}
				if (object instanceof Product) {
					currentProductDb = (Product) object;
				}
				if (object instanceof EditionType) {
					currentEditionTypeDb = (EditionType) object;
				}
				if (object instanceof FamilyLevel1) {
					currentFamilyLevel1Db = (FamilyLevel1) object;
				}
				if (object instanceof FamilyLevel3) {
					currentFamilyLevel3Db = (FamilyLevel3) object;
				}

			}
			if (currentConditioningDb != null && currentProductDb != null && currentEditionTypeDb != null && currentFamilyLevel1Db != null) {
				export.addProduct(buildResult(currentProductDb, currentConditioningDb, currentEditionTypeDb, currentFamilyLevel1Db));
			}
		}
	}
	public Export productsExport(Export export) {
		if (export == null) {
			export = new Export();
		}
		//return export;

		List<?> resultFromDb = productDAO.getProductsToExport(); 
		if (resultFromDb != null) {
			for (Object row : resultFromDb) {
				Product currentProductDb = null;
				Conditioning currentConditioningDb = null;
				EditionType currentEditionTypeDb = null;
				FamilyLevel1 currentFamilyLevel1Db = null;
				
				Object[] recordArray = (Object[]) row;
				for (Object object : recordArray) {
					if (object instanceof Product) {
						currentProductDb = (Product) object;
					}
					if (object instanceof Conditioning) {
						currentConditioningDb = (Conditioning) object;
					}
					if (object instanceof EditionType) {
						currentEditionTypeDb = (EditionType) object;
					}
					if (object instanceof FamilyLevel1) {
						currentFamilyLevel1Db = (FamilyLevel1) object;
					}
				}
				if (currentProductDb != null) {
					//this.productExport(null, currentProductDb.getProductCode(), "1");
					export.addProduct(buildResult(currentProductDb, currentConditioningDb, currentEditionTypeDb, currentFamilyLevel1Db));
				}
			}
			File f = null;
			FileOutputStream fos = null; 
			try {
				f = new File("c:\\wamp\\updatedata.sql");
				fos = new FileOutputStream(f);
			
				for(ProductToExport productToExport : export.getProducts()) {
					fos.write(("Update ps_product set date_upd='" + productToExport.getDateLastUpdate() + "' where reference like '" + productToExport.getProductCode() + "_%' AND date_upd < '2013-08-21 00:00:00';").getBytes());
				}
					
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {	
			// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
			}
		}
		return export;
	}
	/*
	public Export productsExport(Export export) {
		if (export == null) {
			export = new Export();
		}
		//Session session = getSession();
		//Transaction transaction = session.getTransaction();

		List<?> resultFromDb = productDAO.getProductsToExport("FRANCE"); 
		extractData(export, resultFromDb);

		//export.setProducts(products );
		return export;
	}
	 */
	private ProductToExport buildResult(Product productDb,
			Conditioning conditioningDb, EditionType editionTypeDb, FamilyLevel1 familyLevel1Db) {
		
		ProductToExport result = new ProductToExport();
		
		result.setProductCode(productDb.getProductCode());
		
		result.setDescription_fr(productDb.getDescription());
		
		result.setFamilyCode(productDb.getFamilyCode() + 1000); // = category
		
		result.setSortingOrder(productDb.getSortingOrder());
		
		result.setEditionTypeLabel(editionTypeDb.getDescription());
		result.setEditionTypeId("10" + editionTypeDb.getId()); // = group

		result.setEditionTypeAdditionalLabel("");
		result.setEditionTypeAdditionalId("");
		if (StringUtils.isNotBlank(conditioningDb.getEditionTypeAdditionalId())) {
			EditionType additionalEditionTypeDb = editionTypeDAO.get(conditioningDb.getEditionTypeAdditionalId());
			if (additionalEditionTypeDb != null) {
				result.setEditionTypeAdditionalLabel(additionalEditionTypeDb.getDescription());
				result.setEditionTypeAdditionalId("10" + additionalEditionTypeDb.getId()); // = group
			}
		}
		
		result.setPro(StringUtils.equalsIgnoreCase(result.getEditionTypeLabel(), "PRO") || StringUtils.equalsIgnoreCase(result.getEditionTypeAdditionalLabel(), "PRO") ? true : false);

		result.setPlaneMandatory(productDb.isPlaneMandatory());
		
		result.setUnitConditioning(conditioningDb.getUnit());
		result.setUnitNumber(conditioningDb.getNumberOfUnit());
		result.setUnitPrice(conditioningDb.getUnitPrice());
		
		result.setDateLastUpdate(StringUtils.remove(conditioningDb.getDateLastModification(), ".000"));
		
		result.setMarginForStandardCustomer(familyLevel1Db.getMargin() * (-1));
		
		result.setTotalConditioning(conditioningDb.getUnitLargeScale());
		
		BigDecimal bd = new BigDecimal(conditioningDb.getNumberOfUnit() * conditioningDb.getUnitPrice());
		bd = bd.setScale(2, RoundingMode.UP);
		result.setTotalPrice(bd.floatValue());
		
		return result;
	}

	public Export customerExport(Export export, String custCode, String custEmail, String custNewEmail) {
		if (export == null) {
			export = new Export();
		}
		
		Customer resultFromDb = customerDAO.get(custCode);
		List<Customer> tempList = new ArrayList<Customer>();
		tempList.add(resultFromDb);

		getCustomerDataToExport(export, tempList);
		
		List<CustomerToExport> customers = export.getCustomers();
		
		if (customers != null && customers.size() == 1) {
			StringBuffer data = new StringBuffer();
			
			CustomerToExport custToExport = customers.get(0);
			custToExport.getEmail();
			
			data.append("email=" + encode(custToExport.getEmail()));
			data.append("&");
			if (StringUtils.isNotBlank(custNewEmail)) {
				data.append("newemail=" + encode(custNewEmail));
				data.append("&");
			}
			data.append("name=" + encode(custToExport.getName()));
			data.append("&");
			data.append("nameadditionalpart=" + encode(custToExport.getNameAdditionalPart()));
			data.append("&");
			data.append("buyrate=" + encode(String.valueOf(custToExport.getBuyRate())));
			data.append("&");
			data.append("groupscodes=" + encode(custToExport.getGroupsCodes()));
		
			DataSender ds = new DataSender();
			boolean result = ds.sendCustomerUpdate(data.toString());

		}
		
		return export;
	}

	private String encode(String data) {
		String result = data;
		try {
			if (data != null) {
				result = URLEncoder.encode(data, "UTF-8");
			}
		} catch (Exception e) {
			log.error("encode utf-8:", e);
		}

		return result;
	}

	public Export customersExport(Export export) {
		if (export == null) {
			export = new Export();
		}

		List<?> resultFromDb = customerDAO.getCustomersToExport();
		
		getCustomerDataToExport(export, resultFromDb);

		//export.setProducts(products );
		return export;
	}

	private void getCustomerDataToExport(Export export, List<?> resultFromDb) {
		for (Object row : resultFromDb) {
			Customer customerDb = null;
			if (row instanceof Customer) {
				customerDb = (Customer) row;
			}
			CustomerToExport customer = new CustomerToExport();
			customer.setName(customerDb.getName());
			String nameAddPart = customerDb.getNameAdditionalPart();
			if (StringUtils.isNotBlank(nameAddPart) && !StringUtils.equalsIgnoreCase(nameAddPart, "null")) {
				customer.setNameAdditionalPart(customerDb.getNameAdditionalPart());
			} else {
				customer.setNameAdditionalPart(" ");
			}
			customer.setEmail(customerDb.getEmail());
			
			String buyRate = customerDb.getBuyRate();
			if (StringUtils.isNotBlank(buyRate)) {
				customer.setBuyRate(Float.valueOf(buyRate));
			} else {
				customer.setBuyRate(-1);
			}

			String codesTypesOfEditions = customerDb.getCodesTypesOfEditions();
			if (StringUtils.isNotBlank(codesTypesOfEditions) && !codesTypesOfEditions.equalsIgnoreCase("null")) {
				customer.setGroupsCodes(codesTypesOfEditions);
				StringTokenizer types = new StringTokenizer(customerDb.getCodesTypesOfEditions(), ",");
				while (types.hasMoreTokens()) {
					String currentTypeId = (String) types.nextToken();
					EditionType editionType = editionTypeDAO.get(currentTypeId);
					if (editionType != null) {
						customer.addGroup(editionType.getDescription());
					}
				}
			}

			export.addCustomer(customer);	
		}
	}
	public void setProductDAO(IProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	public void setConditioningDAO(IConditioningDAO conditioningDAO) {
		this.conditioningDAO = conditioningDAO;
	}
	
	public IProductDAO getProductDAO() {
		return productDAO;
	}
	public IConditioningDAO getConditioningDAO() {
		return conditioningDAO;
	}

	public ICustomerDAO getCustomerDAO() {
		return customerDAO;
	}

	public void setCustomerDAO(ICustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	public IEditionTypeDAO getEditionTypeDAO() {
		return editionTypeDAO;
	}

	public void setEditionTypeDAO(IEditionTypeDAO editionTypeDAO) {
		this.editionTypeDAO = editionTypeDAO;
	}
}
