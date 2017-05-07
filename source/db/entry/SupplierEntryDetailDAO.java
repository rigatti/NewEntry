package db.entry;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SupplierEntryDetailDAO extends HibernateDaoSupport implements ISupplierEntryDetailDAO {

	private static final Category log = Logger.getLogger(SupplierEntryDetailDAO.class);
	
	public boolean save(SupplierEntryDetail supplierEntryDetail, Session session){
		boolean result = true;
		try {
			log.debug("Save SupplierEntryDetail");
			session.save(supplierEntryDetail);
			log.debug("Save SupplierEntryDetail DONE");
		} catch (Exception e) {
			log.error("Error while saving supplierEntryDetail: " + supplierEntryDetail.getSupplierCode() + " (" + supplierEntryDetail.getSupplierEntryId() + ")", e);
			result = false;
		}
		return result; 
	}
}