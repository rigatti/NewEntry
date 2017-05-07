package db.entry;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.belex.util.Util;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SupplierEntryDAO extends HibernateDaoSupport implements ISupplierEntryDAO {

	private static final Category log = Logger.getLogger(SupplierEntryDAO.class);

	public SupplierEntry getBySupplierEntryId(int supplierEntryId){
		return getBySupplierEntryId(supplierEntryId, null);
	}
	public SupplierEntry  getBySupplierEntryId(int supplierEntryId, Session session){

		SupplierEntry supplierEntry = new SupplierEntry();

		try {
			log.debug("Finding SupplierEntry by ref id:" + supplierEntryId);

			String sql = "from SupplierEntry where" +
									" supplierEntryId='" + supplierEntryId + "'";

			Object obj = null; 

			if (session == null) {
				obj = getHibernateTemplate().find(sql);
			} else {
				obj = session.createQuery(sql).list();
			}
			
			if (obj instanceof List) {

				List<SupplierEntry> listOfSupplierEntry = (List<SupplierEntry>) obj;
				
				if (listOfSupplierEntry.size() > 0) {
					// List size should be 1
					if (listOfSupplierEntry.size() == 1) {
						supplierEntry = listOfSupplierEntry.get(0);
					} else {
						log.error("List of supplierEntry is bigger than 1 : " + sql);
						supplierEntry = listOfSupplierEntry.get(0);
					}
				} else {
					log.debug("No supplierEntry found");
				}
			} else {
				log.debug("No supplierEntry found");
			}
		} catch (Exception e) {
			log.error("Error while finding supplierEntry(" + supplierEntryId + ")", e);
		}
		
		return supplierEntry;

	}

	public ArrayList<SupplierEntry> getSuppliersByDate(String date) {
		ArrayList<SupplierEntry> result = null;

		try {
			log.debug("Finding SupplierEntry by date:" + date);
			
			//String sql = "from SupplierEntry where entryDate=CONVERT(smalldatetime, '" + date + "', 113)";
			String sql = "from SupplierEntry where entryDate='" + Util.formatDate(date, "yyyyMMdd", "yyyy-MM-dd") + "'";

			Object obj = getHibernateTemplate().find(sql);

			if (obj instanceof List) {

				result = (ArrayList<SupplierEntry>) obj;

			} else {
				log.debug("No supplierEntry found");
			}
		} catch (Exception e) {
			log.error("Error while finding supplierEntry for a date:" + date, e);
		}

		return result;
	}
	public SupplierEntry  getSupplierByDate(String supplierCode, String date){

		SupplierEntry supplierEntry = new SupplierEntry();

		try {
			log.debug("Finding SupplierEntry:" +  supplierCode + " " + date);

			String sql = "from SupplierEntry where" +
									" supplierCode='" + supplierCode + "'" +
									" and entryDate=CONVERT(smalldatetime, '" + date + "', 113)";
	
			Object obj = getHibernateTemplate().find(sql);
			
			if (obj instanceof List) {

				List<SupplierEntry> listOfSupplierEntry = (List<SupplierEntry>) obj;
				
				if (listOfSupplierEntry.size() > 0) {
					// List size should be 1
					if (listOfSupplierEntry.size() == 1) {
						supplierEntry = listOfSupplierEntry.get(0);
					} else {
						log.error("List of supplierEntry is bigger than 1 : " +  sql);
						supplierEntry = listOfSupplierEntry.get(0);
					}
				} else {
					log.debug("No supplierEntry found");
				}
			} else {
				log.debug("No supplierEntry found");
			}
		} catch (Exception e) {
			log.error("Error while finding supplierEntry", e);
		}

		return supplierEntry;

	}

	public boolean exist(SupplierEntry supplierEntry, Session session) {
		boolean result = false;
		try {
			log.debug("Finding existance of SupplierEntry");

			String sql = "from SupplierEntry where" +
									" supplierCode='" + supplierEntry.getSupplierCode() + "'" +
									" and orderNumbers='" + supplierEntry.getOrderNumbers() + "'" +
									" and entryDate=CONVERT(smalldatetime, '" + Util.formatDate(supplierEntry.getEntryDate(), "yyyy-MM-dd", "yyyyMMdd") + "', 113)" +
									" and entryTime='" + supplierEntry.getEntryTime() + "'";
			Object obj = null;
			if (session == null) {
				obj = getHibernateTemplate().find(sql);
			} else {
				obj = session.createQuery(sql).list();
			}

			if (obj instanceof List) {

				List<SupplierEntry> listOfSupplierEntry = (List<SupplierEntry>) obj;
				
				if (listOfSupplierEntry.size() > 1) {
					result = true;
				}	
			}

		} catch (Exception e) {
			log.error("Error while finding existance of supplierEntry", e);
		}

		return result;
	}

	public boolean save(SupplierEntry supplierEntry, Session session){
		boolean result = true;
		try {
			log.debug("Save SupplierEntry");
			session.save(supplierEntry);
			log.debug("Save SupplierEntry DONE");
		} catch (Exception e) {
			log.error("Error while saving supplierEntry" + supplierEntry.getSupplierCode() + "(" + supplierEntry.getEntryDate() + ")", e);
			result = false;
		}
		return result; 
	}

	public boolean update(SupplierEntry supplierEntry, Session session){
		boolean result = true;
		try {
			log.debug("Update SupplierEntry:" + supplierEntry.getSupplierEntryId());
			session.update(supplierEntry);
			log.debug("Update SupplierEntry DONE");
		} catch (Exception e) {
			log.error("Error while updating supplierEntry" + supplierEntry.getSupplierCode() + "(" + supplierEntry.getEntryDate() + ")", e);
			result = false;
		}
		return result; 
	}
}