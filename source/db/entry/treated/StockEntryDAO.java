package db.entry.treated;

import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class StockEntryDAO extends HibernateDaoSupport implements IStockEntryDAO {

	private static final Category log = Logger.getLogger(StockEntryDAO.class);

	public boolean insert(StockEntry stockEntry, Session session) {
		boolean result = true;

		try {
			log.debug("Insert StockEntry");
			if (session == null) {
				getHibernateTemplate().save(stockEntry);
			} else {
				session.save(stockEntry);
			}
			log.debug("Insert StockEntry DONE");
		} catch (Exception e) {
			log.error("Error during insert of StockEntry:" + stockEntry.getProductCode(), e);
			result = false;
		}
		return result;
	}
	
	public boolean update(StockEntry stockEntry, Session session) {
		boolean result = true;
		try {
			log.debug("Update StockEntry");
			if (session == null) {
				getHibernateTemplate().update(stockEntry);
			} else {
				session.update(stockEntry);
			}
			log.debug("Update StockEntry DONE");
		} catch (Exception e) {
			log.error("Error during update of StockEntry:" + stockEntry.getProductCode(), e);
			result = false;
		}
		return result;
	}

	public StockEntry get(int treatedEntryDetailDestinationId, Session session) {
		StockEntry stockEntry = new StockEntry();

		try {
			log.debug("Finding stock entries for id :" + treatedEntryDetailDestinationId);

			String sql = "from StockEntry where " +
							"treatedEntryDetailDestinationId='" + treatedEntryDetailDestinationId + "'"; 

			Object obj;
			if (session == null) {
				obj = getHibernateTemplate().find(sql);
			} else {
				obj = session.createQuery(sql).list();
			}
			
			if (obj instanceof List ) {
				
				List<StockEntry> stockEntries = (List<StockEntry>) obj;
				if (stockEntries.size() >= 1) {
					stockEntry = stockEntries.get(0);
				}

			} else {
				log.debug("No supplier returns found");
			}
			log.debug("Finding supplier returns DONE");

		} catch (Exception e) {
			log.error("Error while finding supplier returns", e);
		}

		return stockEntry ;
	}

	public boolean updateProductCode(String oldProductCode, String newProductCode, Session session) {
		boolean result = true;

		String sql = "update StockEntry set productCode='" + newProductCode + "' where productCode='" + oldProductCode + "'"; 

		log.debug("Update StockEntry - " + sql);

		try {
			session.createQuery(sql).executeUpdate();
		} catch (Exception e) {
			result = false;
			log.error("Error while update StockEntry", e);
		}

		log.debug("Update StockEntry done");

		return result;
		
	}
}