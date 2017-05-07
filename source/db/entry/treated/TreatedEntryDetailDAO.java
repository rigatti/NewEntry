package db.entry.treated;

import java.util.ArrayList;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TreatedEntryDetailDAO extends HibernateDaoSupport implements ITreatedEntryDetailDAO {

	private static final Category log = Logger.getLogger(TreatedEntryDetailDAO.class);

	public boolean insert(TreatedEntryDetail treatedEntryDetail, Session session) {
		boolean result = true;
		try {
			log.debug("Insert TreatedEntry");
			session.save(treatedEntryDetail);
			log.debug("Insert TreatedEntry DONE");
		} catch (Exception e) {
			log.error("Error during insert of TreatedEntryDetail for treatedEntryId:" + treatedEntryDetail.getTreatedEntryId(), e);
			result = false;
		}
		return result;
	}

	public TreatedEntryDetail get(int treatedEntryId) {
		return get(treatedEntryId, null);
	}

	public TreatedEntryDetail get(int treatedEntryId, Session session) {
		TreatedEntryDetail treatedEntryDetail = new TreatedEntryDetail();

		try {
			log.debug("Finding treated entries for treatedEntryId:" + treatedEntryId);

			String sql = "from TreatedEntryDetail where treatedEntryId='" + treatedEntryId + "'";

			Object obj = null;
			if (session == null) {
				obj = getHibernateTemplate().find(sql);
			} else {
				obj = session.createQuery(sql).list();
			}
			
			if (obj instanceof ArrayList) {

				ArrayList<TreatedEntryDetail> treatedEntryDetails = (ArrayList<TreatedEntryDetail>) obj;
				if (treatedEntryDetails.size() == 1) {
					treatedEntryDetail = treatedEntryDetails.get(0);
				}

			} else {
				log.debug("No treated entries detail found");
			}
			log.debug("Finding treated entries detail DONE");
		} catch (Exception e) {
			log.error("Error while finding treated entries detail ", e);
		}

		return treatedEntryDetail;

	}

	public ArrayList<TreatedEntryDetail> get(String arrivalDate) {
		return get(arrivalDate, null);
	}

	public ArrayList<TreatedEntryDetail> get(String arrivalDate, Session session) {
		ArrayList<TreatedEntryDetail> treatedEntryDetails = new ArrayList<TreatedEntryDetail>();

		try {
			log.debug("Finding treated entries for arrivalDate:" + arrivalDate);

			String sql = "from TreatedEntryDetail where arrivalDate='" + arrivalDate + "'";

			Object obj = null;
			if (session == null) {
				obj = getHibernateTemplate().find(sql);
			} else {
				obj = session.createQuery(sql).list();
			}

			if (obj instanceof ArrayList) {

				treatedEntryDetails = (ArrayList<TreatedEntryDetail>) obj;

			} else {
				log.debug("No treated entries detail found");
			}
			log.debug("Finding treated entries detail DONE");
		} catch (Exception e) {
			log.error("Error while finding treated entries detail ", e);
		}

		return treatedEntryDetails;
	}
}