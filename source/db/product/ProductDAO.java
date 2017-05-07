package db.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProductDAO extends HibernateDaoSupport implements IProductDAO{

	private static final Category log = Logger.getLogger(ProductDAO.class);
	
	public String getDescription(String productCode) {
		String result = "";
		try {
			log.debug("Finding product description from " + productCode);

			String sql = "from Product where productCode='" + productCode + "'";
			Object obj = getHibernateTemplate().find(sql);
			
			if (obj instanceof List) {

				List<Product> listOfProduct = (List<Product>) obj;
				
				if (listOfProduct.size() != 0) {
					// List size should be 1
					if (listOfProduct.size() == 1) {
						result = listOfProduct.get(0).getDescription();
					} else {
						log.error("List of Products is bigger than 1 : " + sql);
					}
				} else {
					log.debug("No product description found for the code:" + productCode);
				}
			} else {
				log.debug("No product description found for the code:" + productCode);
			}
		} catch (Exception e) {
			log.error("Error while finding product description", e);
		}
		
		return result;
	}
	
	public ArrayList<Product> getProductsByDescription(String description, String searchOnSupplier){
		ArrayList<Product> products = null;
		
		try {
			log.debug("Finding all product by description");
			
			String sql = "from Product where description LIKE '%" + description.toUpperCase() + "%'";

			Object obj = getHibernateTemplate().find(sql);
			
			if (obj instanceof ArrayList) {
				products = (ArrayList<Product>) obj;
			}

		} catch (Exception e) {
			log.error("Error while finding products by description", e);
		}
		return products;
	}

	public Product getProductByCode(String code, boolean logError) {
		Product product = null;
		
		String sql = "from Product where productCode = '" + code + "'";

		try {
			log.debug("Finding all product by code");

			Object obj = getHibernateTemplate().find(sql);
			
			if (obj instanceof List) {
				product = ((List<Product>) obj).get(0);
			}

		} catch (Exception e) {
			if (logError) {
				log.error("Error while finding product by code sql:" + sql, e);
			}
		}
		return product;
	}

	public boolean insert(Product product, Session session) {
		boolean result = true;

		try {
			session.save(product);
		} catch (Exception e) {
			log.error("Error while saving product: " + product.getProductCode(), e);
			 result = false;
		}

		return result;
	}

	public boolean update(Product product, Session session) {
		boolean result = true;

		try {
			session.update(product);
		} catch (Exception e) {
			log.error("Error while saving product: " + product.getProductCode(), e);
			 result = false;
		}

		return result;
	}
	
	public boolean delete(Product product, Session session) {
		boolean result = true;

		try {
			session.delete(product);
		} catch (Exception e) {
			log.error("Error while deleting product: " + product.getProductCode(), e);
			 result = false;
		}

		return result;
	}

	public int getMaxId() {
		int result = 1;

		try {
			String sql = 
				"select MAX(tempId) from Product";

			List objList = getHibernateTemplate().find(sql);
			if (objList != null && objList.size() > 0) {
				result = ((Integer) objList.get(0)).intValue() + 1; 
			}
		} catch (Exception e) {
			log.error("Error while getting MAX tempId in product", e);
		}
		return result;
	}
	/*
	//private Log logger;
	
	public boolean save(Product obj) {
		boolean result = true;
		try {
			//logger = LogFactory.getLog(ProductDAO.class);
			//logger.debug("");
			log.debug("Prepare to save the Product");
			
			Session session = getSession();
			Transaction transaction = session.beginTransaction();
			
			session.saveOrUpdate(obj);
			
			transaction.commit();
			
			log.debug("Transaction submitted");
			
		} catch (Exception e) {
			log.error("Error while saving given product", e);
			result = false;
			throw e;
		}
		return result;
	}
	
	public Product complete(String productCode) {
		Product product = new Product(productCode);
		
		try {
			log.debug("Finding Products from " + productCode);
			/*
			Session session = getSession();
			Transaction transaction = session.beginTransaction();
			
			Criteria crit = session.createCriteria(Product.class);
			crit.add(Restrictions.eq( "productCode", productCode ));
			//Criteria descrCrit = crit.createCriteria("descriptions");
			//descrCrit.add(Restrictions.eq("language", "ENG"));

			DetachedCriteria dc = new DetachedCriteria();
			Object obj = crit.list();
			
			getHibernateTemplate().
			*/
	/*
			Object obj = getHibernateTemplate().find("from Product where productCode=?", new Object[]{productCode});
			
			
			if (obj instanceof List) {

				List<Product> listOfProduct = (List<Product>) obj;
				
				// List size should be 1
				if (listOfProduct.size() == 1) {
					product = listOfProduct.get(0);
				} else {
					log.error("List of Products is bigger than 1 for the current productCode:" + productCode);
				}
			} else {
				log.debug("No product found for the code:" + productCode);
			}
		} catch (Exception e) {
			log.error("Error while finding products", e);
			throw e;
		}
		
		return product;
	}
	*/

	public List<?> getProductsToExport(String editionType) {
		return getProductsToExport(editionType, false);
	}
	public List<?> getProductsToExport(String editionType, boolean mandatoryPlane) {
		List<?> result = null;
		try {
			log.debug("Finding product to export");

			/*String sql = 
				"from Conditioning" +
					"where CodeArticle not like '~%' " +
							"and CodeArticle not like '%TBD' " +
							"and CodeTypeEdition = '0'" +
							"and DateDernièreModification > GETDATE() - 185 " +
					"order by DateDernièreModification desc";
			*/
					String sql = "FROM " +
							" Conditioning AS c " + 
							", Product AS p " + 
							", EditionType AS t " +
							", FamilyLevel1 AS fl1 " +
							", FamilyLevel3 AS fl3 " +
							
							" WHERE  (p.productCode NOT LIKE '~%')  " +
							" AND (p.productCode NOT LIKE '%TBD')  " +
							" AND (c.dateLastModification > GETDATE() - 185) " +
							" AND (p.productCode = c.productCode) " +
							" AND (p.followedProduct = '0') " +
							" AND (t.id = c.editionTypeId) " +
							" AND (t.description = '" + editionType + "') " +
							" AND (fl3.id = p.familyCode) " +
							" AND (fl3.idFamilyLevel1 = fl1.id) ";

							if (mandatoryPlane) {
								sql += " AND (p.AvionObligatoire != 1) ";
							}
							
							sql +=" ORDER BY p.productCode ASC ";

			Object obj = getHibernateTemplate().find(sql);
			if (obj instanceof List<?>) {
				result = (List<?>)obj;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return result;
	}
	public List<?> getProductsToExportByReference(String productReference) {
		List<?> result = null;
		try {
			log.debug("Finding product to export by reference : " + productReference);

			/*String sql = 
				"from Conditioning" +
					"where CodeArticle not like '~%' " +
							"and CodeArticle not like '%TBD' " +
							"and CodeTypeEdition = '0'" +
							"and DateDernièreModification > GETDATE() - 185 " +
					"order by DateDernièreModification desc";
			*/
					String sql = "FROM " +
							" Conditioning AS c " + 
							", Product AS p " + 
							", EditionType AS t " +
							", FamilyLevel1 AS fl1 " +
							", FamilyLevel3 AS fl3 " +
							
							" WHERE  (p.productCode NOT LIKE '~%')  " +
							" AND (p.productCode NOT LIKE '%TBD')  " +
							" AND (c.dateLastModification > GETDATE() - 185) " +
							" AND (p.productCode = c.productCode) " +
							" AND (p.productCode = '" + productReference + "') " +
							" AND (p.followedProduct = '0') " +
							" AND (t.id = c.editionTypeId) " +
							//" AND (t.description = '" + editionType + "') " +
							" AND (fl3.id = p.familyCode) " +
							" AND (fl3.idFamilyLevel1 = fl1.id) ";

			Object obj = getHibernateTemplate().find(sql);
			if (obj instanceof List<?>) {
				result = (List<?>)obj;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return result;
	}
	
	public List<?> getProductsToExport() {
		List<?> result = null;
		try {
			log.debug("Finding product to export");

			/*String sql = 
				"from Conditioning" +
					"where CodeArticle not like '~%' " +
							"and CodeArticle not like '%TBD' " +
							"and CodeTypeEdition = '0'" +
							"and DateDernièreModification > GETDATE() - 185 " +
					"order by DateDernièreModification desc";
			*/
					String sql = "FROM " +
							" Conditioning AS c " + 
							", Product AS p " + 
							", EditionType AS t " +
							", FamilyLevel1 AS fl1 " +
							", FamilyLevel3 AS fl3 " +
							
							" WHERE  (p.productCode NOT LIKE '~%')  " +
							" AND (p.productCode NOT LIKE '%TBD')  " +
							" AND (c.dateLastModification > GETDATE() - 185) " +
							" AND (p.productCode = c.productCode) " +
							" AND (p.followedProduct = '0') " +
							" AND (t.id = c.editionTypeId) " +
							//" AND (t.description = '" + AMB + "' OR ) " +
							" AND (fl3.id = p.familyCode) " +
							" AND (fl3.idFamilyLevel1 = fl1.id) ";

							//if (mandatoryPlane) {
							//	sql += " AND (p.AvionObligatoire != 1) ";
							//}
							
							sql +=" ORDER BY p.productCode ASC ";

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