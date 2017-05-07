package db.entry.treated;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TreatedEntryDAO extends HibernateDaoSupport implements ITreatedEntryDAO {

	private static final Category log = Logger.getLogger(TreatedEntryDAO.class);

	public boolean insert(TreatedEntry treatedEntry, Session session) {
		boolean result = true;

		try {
			log.debug("Insert TreatedEntry");
			session.save(treatedEntry);
			log.debug("Insert TreatedEntry DONE");
		} catch (Exception e) {
			log.error("Error during insert of TreatedEntry:" + treatedEntry.getProductCode(), e);
			result = false;
		}
		return result;
	}

	public TreatedEntry get(int treatedEntryId) {
		return get(treatedEntryId, null);
	}

	public TreatedEntry get(int treatedEntryId, Session session) {
		TreatedEntry treatedEntry = new TreatedEntry();

		try {
			log.debug("Finding treated entries for id :" + treatedEntryId);

			String sql = "from TreatedEntry where treatedEntryId='" + treatedEntryId + "'"; 

			Object obj;
			if (session == null) {
				obj = getHibernateTemplate().find(sql);
			} else {
				obj = session.createQuery(sql).list();
			}
			
			if (obj instanceof ArrayList ) {
				
				ArrayList<TreatedEntry> treatedEntries = (ArrayList<TreatedEntry>) obj;
				if (treatedEntries.size() == 1) {
					treatedEntry = treatedEntries.get(0);
				}

			} else {
				log.debug("No treated entries found");
			}
			log.debug("Finding treated entries DONE");

		} catch (Exception e) {
			log.error("Error while finding treated entries", e);
		}

		return treatedEntry;
	}

	public TreatedEntry getUnique(String supplierCode, String arrivalDate, String productCode) {
		return getUnique(supplierCode, arrivalDate, productCode, null);
	}
	public TreatedEntry getUnique(String supplierCode, String arrivalDate, String productCode, Session session) {

		TreatedEntry treatedEntry = new TreatedEntry();

		try {
			log.debug("Finding treated entry for supplier:" + supplierCode + ", arrival date:" + arrivalDate + "and product code:" + productCode);

			String sql = "from TreatedEntry where supplierCode='" + supplierCode + "'" + 
												" and arrivalDate='" + arrivalDate + "'" + 
												" and productCode='" + productCode + "'";
			
			Object obj;
			if (session == null) {
				obj = getHibernateTemplate().find(sql);
			} else {
				obj = session.createQuery(sql).list();
			}
		
			if (obj instanceof ArrayList) {

				ArrayList<TreatedEntry> treatedEntries = (ArrayList<TreatedEntry>) obj;
				if (treatedEntries.size() > 0) {
					treatedEntry = treatedEntries.get(0);
				}
			} else {
				log.debug("No treated entry found");
			}
			log.debug("Finding treated entry DONE");
		} catch (Exception e) {
			log.error("Error while finding treated entry", e);
		}

		return treatedEntry;

	}

	public ArrayList<TreatedEntry> get(String supplierCode, String arrivalDate) {
		return get(supplierCode, arrivalDate, null);
	}

	public ArrayList<TreatedEntry> get(String supplierCode, String arrivalDate, Session session) {
		ArrayList<TreatedEntry> treatedEntries = new ArrayList<TreatedEntry>();

		try {
			log.debug("Finding treated entries for supplier:" + supplierCode + " and arrival date:" + arrivalDate);

			String sql = "from TreatedEntry where supplierCode='" + supplierCode + "'" + 
												" and arrivalDate='" + arrivalDate + "'";

			Object obj;
			if (session == null) {
				obj = getHibernateTemplate().find(sql);
			} else {
				obj = session.createQuery(sql).list();
			}
			
			if (obj instanceof ArrayList) {

				treatedEntries = (ArrayList<TreatedEntry>) obj;

			} else {
				log.debug("No treated entries found");
			}
			log.debug("Finding treated entries DONE");
		} catch (Exception e) {
			log.error("Error while finding treated entries", e);
		}

		return treatedEntries;
	}

	public ArrayList<TreatedEntry> get(String productCode, String ean, String lotNumber, String validityDate) {
		return get(productCode, ean, lotNumber, validityDate, null);
	}

	public ArrayList<TreatedEntry> get(String productCode, String ean, String lotNumber, String validityDate, Session session) {
		ArrayList<TreatedEntry> treatedEntries = new ArrayList<TreatedEntry>();
		try {
			log.debug("Finding treated entries for productCode:" + productCode + 
						" and ean:" + ean + " and lot number:" + lotNumber + 
						" and validityDate:" + validityDate);

			boolean newParam = false;

			String sql = "from TreatedEntry where ";
			
			if ( StringUtils.isNotEmpty(productCode) ) {
				sql += "productCode='" + productCode + "'";
				newParam = true;
			}
			if ( StringUtils.isNotEmpty(ean) ) {
				if (newParam) {
					sql += " and ";
				}
				newParam = true;
				sql += "ean='" + ean + "'"; 
			}
			if ( StringUtils.isNotEmpty(lotNumber) ) {
				if (newParam) {
					sql += " and ";
				}
				newParam = true;
				sql += "lotNumber LIKE '%" + lotNumber + "%'"; 
			}
			if ( StringUtils.isNotEmpty(validityDate) ) {
				if (newParam) {
					sql += " and ";
				}
				sql += "validityDate='" + validityDate + "'"; 
			}
			
			Object obj;
			if (session == null) {
				obj = getHibernateTemplate().find(sql);
			} else {
				obj = session.createQuery(sql).list();
			}
			
			if (obj instanceof ArrayList) {

				treatedEntries = (ArrayList<TreatedEntry>) obj;

			} else {
				log.debug("No treated entries found");
			}
			log.debug("Finding treated entries DONE");
		} catch (Exception e) {
			log.error("Error while finding treated entries", e);
		}
		return treatedEntries;
	}
	
	public boolean updateProductCode(String oldProductCode, String newProductCode, Session session) {
		boolean result = true;

		String sql = "update TreatedEntry set productCode='" + newProductCode + "' where productCode='" + oldProductCode + "'"; 

		log.debug("Update treated entries - " + sql);

		try {
			session.createQuery(sql).executeUpdate();
		} catch (Exception e) {
			result = false;
			log.error("Error while update treated entries", e);
		}

		log.debug("Update treated entries done");

		return result;
		
	}
	public boolean cleanDb(String maxDate, Session session, ITreatedEntryDetailDAO treatedEntryDetailDAO, ITreatedEntryDetailDestinationDAO treatedEntryDetailDestinationDAO) {
		boolean result = true;

		try {
			log.debug("Finding treated entries before date :" + maxDate);

			String sql = "from TreatedEntry where arrivalDate < '" + maxDate + "'"; 

			Object obj;
			if (session == null) {
				obj = getHibernateTemplate().find(sql);
			} else {
				obj = session.createQuery(sql).list();
			}
			
			if (obj instanceof ArrayList ) {
				
				ArrayList<TreatedEntry>treatedEntries = (ArrayList<TreatedEntry>) obj;
				for (TreatedEntry treatedEntry : treatedEntries) {
					TreatedEntryDetail ted = treatedEntryDetailDAO.get(treatedEntry.getTreatedEntryId());
					ArrayList<TreatedEntryDetailDestination> tedds = treatedEntryDetailDestinationDAO.getAll(treatedEntry.getTreatedEntryId());
					for (TreatedEntryDetailDestination tedd : tedds ) {
						session.delete(tedd);
					}
					session.delete(ted);
					session.delete(treatedEntry);
				}

			} else {
				log.debug("No treated entries found");
			}
			log.debug("Cleaning treated entries DONE");

		} catch (Exception e) {
			log.error("Error while finding treated entries", e);
		}

		return result;
	}
}