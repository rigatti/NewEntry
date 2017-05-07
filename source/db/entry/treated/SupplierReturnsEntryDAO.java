package db.entry.treated;

import java.util.ArrayList;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SupplierReturnsEntryDAO extends HibernateDaoSupport implements ISupplierReturnsEntryDAO {

	private static final Category log = Logger.getLogger(SupplierReturnsEntryDAO.class);

	public boolean insert(SupplierReturnsEntry supplierReturnsEntry, Session session) {
		boolean result = true;

		try {
			log.debug("Insert SupplierReturnsEntry");

			if (session == null) {
				getHibernateTemplate().save(supplierReturnsEntry);
			} else {
				session.save(supplierReturnsEntry);
			}

			log.debug("Insert SupplierReturnsEntry DONE");
		} catch (Exception e) {
			log.error("Error during insert of SupplierReturnsEntry:" + supplierReturnsEntry.getProductCode(), e);
			result = false;
		}
		return result;
	}


	public boolean update(SupplierReturnsEntry supplierReturnsEntry, Session session) {
		boolean result = true;

		try {
			log.debug("Update SupplierReturnsEntry");
			if (session == null) {
				getHibernateTemplate().update(supplierReturnsEntry);
			} else { 
				session.update(supplierReturnsEntry);
			}
			log.debug("Update SupplierReturnsEntry DONE");
		} catch (Exception e) {
			log.error("Error during update of SupplierReturnsEntry:" + supplierReturnsEntry.getProductCode(), e);
			result = false;
		}
		return result;
	}
	
	public SupplierReturnsEntry get(int treatedEntryDetailDestinationId, Session session) {
		SupplierReturnsEntry supplierReturnsEntry = new SupplierReturnsEntry();

		try {
			log.debug("Finding supplier returns entries for id :" + treatedEntryDetailDestinationId);

			String sql = "from SupplierReturnsEntry where " +
							"treatedEntryDetailDestinationId='" + treatedEntryDetailDestinationId + "'"; 

			Object obj;
			if (session == null) {
				obj = getHibernateTemplate().find(sql);
			} else {
				obj = session.createQuery(sql).list();
			}
			
			if (obj instanceof ArrayList ) {
				
				ArrayList<SupplierReturnsEntry> supplierReturnsEntries = (ArrayList<SupplierReturnsEntry>) obj;
				if (supplierReturnsEntries.size() >= 1) {
					supplierReturnsEntry = supplierReturnsEntries.get(0);
				}

			} else {
				log.debug("No supplier returns found");
			}
			log.debug("Finding supplier returns DONE");

		} catch (Exception e) {
			log.error("Error while finding supplier returns", e);
		}

		return supplierReturnsEntry ;
	}

	public boolean updateProductCode(String oldProductCode, String newProductCode, Session session) {
		boolean result = true;

		String sql = "update SupplierReturnsEntry set productCode='" + newProductCode + "' where productCode='" + oldProductCode + "'"; 

		log.debug("Update SupplierReturnsEntry - " + sql);

		try {
			session.createQuery(sql).executeUpdate();
		} catch (Exception e) {
			result = false;
			log.error("Error while update SupplierReturnsEntry", e);
		}

		log.debug("Update SupplierReturnsEntry done");

		return result;
		
	}
}