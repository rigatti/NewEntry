package db.entry.treated.temp;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TreatedEntryCustomerTempDAO extends HibernateDaoSupport implements ITreatedEntryCustomerTempDAO {

	private static final Category log = Logger.getLogger(TreatedEntryCustomerTempDAO.class);

	public boolean save(TreatedEntryCustomerTemp treatedEntryCustomerTemp, Session session) {
		
		boolean result = true;
		
		try {
			log.debug("Saving TreatedEntryCustomerTemp");
			
			session.save(treatedEntryCustomerTemp);
			
			log.debug("Save TreatedEntryCustomerTemp finished");
		} catch (Exception e) {
			log.error("Error while saving treatedEntryCustomerTemp id:" + treatedEntryCustomerTemp.getTreatedEntryTempId(), e);
			result = false;
		}

		return result;
	}
	public ArrayList<TreatedEntryCustomerTemp> getAllForEntryId(int entryId) {
		return getAllForEntryId(entryId, null);
	}
	
	public ArrayList<TreatedEntryCustomerTemp> getAll() {
		ArrayList<TreatedEntryCustomerTemp> treatedEntryCustomerTemps = new ArrayList<TreatedEntryCustomerTemp>();
		String sql = "from TreatedEntryCustomerTemp";
		try {
			log.debug("Finding getAll");
			
			Object obj = getHibernateTemplate().find(sql);
			
			if (obj instanceof List) {

				treatedEntryCustomerTemps = (ArrayList<TreatedEntryCustomerTemp>) obj;
				
			} else {
				log.info("No treatedEntryCustomerTemp found:" + sql);
			}
		} catch (Exception e) {
			log.error("Error while finding treatedEntryCustomerTemp:" + sql, e);
		}

		return treatedEntryCustomerTemps;
	}

	public ArrayList<TreatedEntryCustomerTemp> getAllForEntryId(int entryId, Session session) {
		ArrayList<TreatedEntryCustomerTemp> treatedEntryCustomerTemps = new ArrayList<TreatedEntryCustomerTemp>();
		String sql = "from TreatedEntryCustomerTemp where treatedEntryTempId=" + entryId;
		try {
			log.debug("Finding getAllForEntryId:" + entryId);
			
			Object obj = null;
			
			if (session == null) {
				obj = getHibernateTemplate().find(sql);
			} else {
				obj = session.createQuery(sql).list();
			}
			
			if (obj instanceof List) {

				treatedEntryCustomerTemps = (ArrayList<TreatedEntryCustomerTemp>) obj;
				
			} else {
				log.info("No treatedEntryCustomerTemp found:" + sql);
			}
		} catch (Exception e) {
			log.error("Error while finding TreatedEntryCustomerTemp:" + sql, e);
		}

		return treatedEntryCustomerTemps;
	}

	public boolean deleteAllByEntryId(int entryId, Session session) {
		boolean result = true;
		log.info("Delete treatedEntryCustomerTemp by id:" + entryId);
		try {
			log.info("before getAllForEntryId:" + entryId);
			
			ArrayList<TreatedEntryCustomerTemp> treatedEntryCustomerTemps = getAllForEntryId(entryId, session);
			
			log.info("after getAllForEntry id : number of tects:" + treatedEntryCustomerTemps.size());
			
			for (TreatedEntryCustomerTemp tect : treatedEntryCustomerTemps) {
				session.delete(tect);
			}
		} catch (Exception e) {
			log.error("Error during deleting treatedEntryCustomerTemp by id:" + entryId, e);
			result = false;
		}
		log.info("Delete treatedEntryCustomerTemp by id DONE");
		//getHibernateTemplate().deleteAll(treatedEntryCustomerTemps);
		//if ( ! result) {
		//	log.error("No element found in treatedEntryCustomerTemp for the given entryId:" + entryId);
		//}
		return result;
	}
}