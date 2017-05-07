package db.product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.belex.product.Product.Unit;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ConditioningDAO extends HibernateDaoSupport implements IConditioningDAO{

	private static final Category log = Logger.getLogger(ConditioningDAO.class);

	public ArrayList<Conditioning> getProductsByEan(String ean, String searchOnSupplier){
		ArrayList<Conditioning> conditionings = null;
		
		try {
			log.debug("Finding all product conditioning by ean");

			String sql = "from Conditioning where ean='" + ean.toUpperCase() + "'";

			if (searchOnSupplier != null && searchOnSupplier.trim().length() > 0){
				sql += " and supplierCode='" + searchOnSupplier + "'";
			}

			Object obj = getHibernateTemplate().find(sql);

			if (obj instanceof ArrayList) {
				conditionings = (ArrayList<Conditioning>) obj;
			}

		} catch (Exception e) {
			log.error("Error while finding product conditioning", e);
		}

		return conditionings;
	}
	
	public ArrayList<Conditioning> getProductsByCode(String productCode, boolean searchExactMatch, String searchOnSupplier) {
		ArrayList<Conditioning> conditionings = null;
		
		try {
			log.debug("Finding all product conditioning by product code");

			String sql = "from Conditioning where ";

			if (searchExactMatch) {
				sql += "codeArticle='" + productCode.toUpperCase() + "'";
			} else {
				sql += "codeArticle LIKE '%" + productCode.toUpperCase() + "%'";
			}

			if (searchOnSupplier != null && searchOnSupplier.trim().length() > 0){
				sql += " and CodeFournisseur='" + searchOnSupplier + "'";
			}

			Object obj = getHibernateTemplate().find(sql);

			if (obj instanceof ArrayList) {
				conditionings = (ArrayList<Conditioning>) obj;
			}

		} catch (Exception e) {
			log.error("Error while finding product conditioning", e);
		}

		return conditionings;
	}

	public ArrayList<Conditioning> get(String productCode, String supplierCode) {
		return get(productCode, supplierCode, null);
	}
	public ArrayList<Conditioning> get(String productCode, String supplierCode, Unit unit) {
		ArrayList<Conditioning> conditionings = new ArrayList<Conditioning>();
		
		try {
			log.debug("Finding all product conditioning (" + productCode +")");
			
			String sql = "from Conditioning where " +
								"productCode='" + productCode + "'";

			if (supplierCode != null) {
				sql += " and supplierCode='" + supplierCode + "'";
			}

			if (unit != null) {
				sql += " and unit='" + unit.getConditionnement() + "'" +
					   " and numberOfUnit='" + unit.getNumber() + "'";
			}

			Object obj = getHibernateTemplate().find(sql);
	
			if (obj instanceof ArrayList) {
				conditionings = (ArrayList<Conditioning>) obj;
			}
		} catch (Exception e) {
			log.error("Error while finding product(" + productCode + ")", e);
		}
		return conditionings;
	}

	public boolean insert(Conditioning conditioning) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		conditioning.setDateLastModification(sdf.format(java.util.Calendar.getInstance().getTime()));

		return insert(conditioning, null);
	}

	public boolean insert(Conditioning conditioning, Session session) {
		boolean result = true;

		log.debug("Insert new Conditioning - " + conditioning.getProductCode());

		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
			conditioning.setDateLastModification(sdf.format(java.util.Calendar.getInstance().getTime()));
			
			if (session == null) {
				getHibernateTemplate().save(conditioning);
			} else {
				session.save(conditioning);
			}
		} catch (Exception e) {
			result = false;
			log.error("Error while saving Conditioning (" + conditioning.getProductCode() + ")", e);
		}

		log.debug("Save Conditioning done");

		return result;
	}

	public boolean update(Conditioning conditioning) {
		boolean result = true;

		log.debug("Update Conditioning");

		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
			conditioning.setDateLastModification(sdf.format(java.util.Calendar.getInstance().getTime()));
			
			getHibernateTemplate().update(conditioning);	
		} catch (Exception e) {
			result = false;
			log.error("Error while updating Conditioning (" + conditioning.getProductCode() + ")", e);
		}

		log.debug("update Conditioning done");

		return result;
	}
	
	public int getNextPriority(String productCode, String supplierCode) {
		int result = 1;

		try {
			String sql = 
				"select MAX(priority) from Conditioning where " +
											"productCode='" + productCode + "' and " +
											"supplierCode='" + supplierCode + "'";
	
			List objList = getHibernateTemplate().find(sql);
			if (objList != null && objList.size() > 0) {
				result = ((Integer) objList.get(0)).intValue() + 1;
			}
		} catch (Exception e) {
			log.error("Error while getting MAX in Conditioning (" + supplierCode + " - " + productCode + ")", e);
		}
		
		return result;
	}
	
	public boolean updateProductCode(String oldProductCode, String newProductCode, Session session) {
		boolean result = true;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		String dateLastModification = (sdf.format(java.util.Calendar.getInstance().getTime()));

		String sql = "update Conditioning set productCode='" + newProductCode + "', dateLastModification='" + dateLastModification + "' where productCode='" + oldProductCode + "'"; 

		log.debug("Update Conditioning - " + sql);

		try {
			session.createQuery(sql).executeUpdate();
		} catch (Exception e) {
			result = false;
			log.error("Error while update Conditioning", e);
		}

		log.debug("Update Conditioning done");

		return result;
		
	}

	public boolean deleteProductCode(String productCode, Session session) {
		boolean result = true;

		String sql = "delete from Conditioning where productCode='" + productCode + "'"; 

		log.debug("Delete Conditioning - " + sql);

		try {
			session.createQuery(sql).executeUpdate();
		} catch (Exception e) {
			result = false;
			log.error("Error while deleting Conditioning", e);
		}

		log.debug("Delete Conditioning done");

		return result;
		
	}

	public Conditioning getUnitScaleForTopPriority(String productCode) {
		Conditioning conditioning = null;
		
		try {
			log.debug("Finding getUnitScaleForTopPriority (" + productCode +")");
			
			String sql = "from Conditioning where " +
								"productCode='" + productCode + "' order by priority";

			List objList = getHibernateTemplate().find(sql);
			if (objList != null && objList.size() > 0) {
				conditioning = (Conditioning) objList.get(0);
			}

		} catch (Exception e) {
			log.error("Error while finding product(" + productCode + ")", e);
		}
		return conditioning;
	}
}