package db.entry.treated;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TreatedEntryDetailDestinationDAO extends HibernateDaoSupport implements ITreatedEntryDetailDestinationDAO {

	private static final Category log = Logger.getLogger(TreatedEntryDetailDestinationDAO.class);

	public boolean insert(TreatedEntryDetailDestination treatedEntryDetailDestination, Session session) {
		boolean result = true;

		try {
			log.debug("Insert TreatedEntryDetailDestination");
			if (session == null) {
				getHibernateTemplate().save(treatedEntryDetailDestination);	
			} else {
				session.save(treatedEntryDetailDestination);
			}
			log.debug("Insert TreatedEntryDetailDestination DONE");
		} catch (Exception e) {
			log.error("Error during insert of TreatedEntryDetailDestination:" + treatedEntryDetailDestination.getTreatedEntryDetailId(), e);
			result = false;
		}
		return result;
	}

	public boolean insert(TreatedEntryDetailDestination treatedEntryDetailDestination) {
		return insert(treatedEntryDetailDestination, null);
	}

	public boolean update(TreatedEntryDetailDestination treatedEntryDetailDestination) {
		return update(treatedEntryDetailDestination, null);
	}
	public boolean update(TreatedEntryDetailDestination treatedEntryDetailDestination, Session session) {
		boolean result = true;
		try {
			log.debug("Update TreatedEntryDetailDestination");
			if (session == null) {
				getHibernateTemplate().update(treatedEntryDetailDestination);	
			} else {
				session.update(treatedEntryDetailDestination);
			}
			log.debug("Update TreatedEntryDetailDestination DONE");
		} catch (Exception e) {
			log.error("Error during update of TreatedEntryDetailDestination:" + treatedEntryDetailDestination.getTreatedEntryDetailId(), e);
			result = false;
		}
		return result;
	}

	public ArrayList<TreatedEntryDetailDestination> getAll(int entryDetailId) {
		return getAll(entryDetailId, null);
	}
	public ArrayList<TreatedEntryDetailDestination> getAll(int entryDetailId, Session session) {
		ArrayList<TreatedEntryDetailDestination> tedds = new ArrayList<TreatedEntryDetailDestination>();

		try {
			log.debug("Finding treated entries detail destination for detailId:" + entryDetailId);

			String sql = "from TreatedEntryDetailDestination where treatedEntryDetailId=" + entryDetailId;
			
			Object obj = null;
			
			if (session == null) {
				obj = getHibernateTemplate().find(sql);
			} else {
				obj = session.createQuery(sql).list();
			}
			
			if (obj instanceof ArrayList) {

				tedds = (ArrayList<TreatedEntryDetailDestination>) obj;

			} else {
				log.debug("No treated entries destination found");
			}
			log.debug("Finding treated entries destination DONE");
		} catch (Exception e) {
			log.error("Error while finding treated entries destination ", e);
		}

		return tedds;
	}
	public TreatedEntryDetailDestination get(int id) {
		TreatedEntryDetailDestination tedd = new TreatedEntryDetailDestination();

		try {
			log.debug("Finding treated entries detail destination for id:" + id);

			String sql = "from TreatedEntryDetailDestination where id=" + id;
			Object obj = getHibernateTemplate().find(sql);
			if (obj instanceof List) {
				if (((List) obj).size() == 1) {
					tedd = ((List<TreatedEntryDetailDestination>) obj).get(0);
				}
			}

			log.debug("Finding treated entries destination DONE");
		} catch (Exception e) {
			log.error("Error while finding treated entries destination ", e);
		}

		return tedd;
	}
}