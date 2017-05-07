package db.entry;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SupplierEntryQuantityDAO extends HibernateDaoSupport implements ISupplierEntryQuantityDAO{

	private static final Category log = Logger.getLogger(SupplierEntryQuantityDAO.class);
	
	public boolean save(SupplierEntryQuantity supplierEntryQuantity, Session session){
		boolean result = true;
		try {
			log.debug("Save SupplierEntryQuantity");
			session.save(supplierEntryQuantity);
			log.debug("Save SupplierEntryQuantity DONE");
		} catch (Exception e) {
			log.error("Error while saving supplierEntryQuantity: " + supplierEntryQuantity.getProductCode() + " (" + supplierEntryQuantity.getSupplierEntryId() + ")", e);
			result = false;
		}
		return result; 
	}

	public boolean update(SupplierEntryQuantity supplierEntryQuantity, Session session){
		boolean result = true;
		try {
			log.debug("Update SupplierEntryQuantity");
			session.update(supplierEntryQuantity);
			log.debug("update SupplierEntryQuantity DONE");
		} catch (Exception e) {
			log.error("Error while updating supplierEntryQuantity: " + supplierEntryQuantity.getProductCode() + " (" + supplierEntryQuantity.getSupplierEntryId() + ")", e);
			result = false;
		}
		return result; 
	}
	
	public SupplierEntryQuantity getUnique(int supplierEntryId, String productCode,Session session){
		SupplierEntryQuantity supplierEntryQuantity = null;

		String sql = "from SupplierEntryQuantity where " +
							"productCode='" + productCode + "' and " +
							"supplierEntryId=" + supplierEntryId;
		try {
			log.debug("Finding unique SupplierEntryQuantity:" + supplierEntryId + " - " + productCode);
			
				Object obj = session.createQuery(sql).uniqueResult();
				
				if (obj != null && obj instanceof SupplierEntryQuantity) {

					supplierEntryQuantity = (SupplierEntryQuantity) obj;
					
				} else {
					log.debug("No SupplierEntryQuantity found SQL:" + sql);
				}
			} catch (Exception e) {
				log.error("Error while finding SupplierEntryQuantity SQL:" + sql, e);
			}
		return supplierEntryQuantity; 
	}
	
}