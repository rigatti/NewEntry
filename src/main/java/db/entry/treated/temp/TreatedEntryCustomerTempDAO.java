package db.entry.treated.temp;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class TreatedEntryCustomerTempDAO implements ITreatedEntryCustomerTempDAO {

	private final SessionFactory sessionFactory;

	public TreatedEntryCustomerTempDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Transactional
	public boolean save(TreatedEntryCustomerTemp treatedEntryCustomerTemp) {
		
		boolean result = true;
		
		try {
			log.debug("Saving TreatedEntryCustomerTemp");

			currentSession().save(treatedEntryCustomerTemp);
			
			log.debug("Save TreatedEntryCustomerTemp finished");
		} catch (Exception e) {
			log.error("Error while saving treatedEntryCustomerTemp id:" + treatedEntryCustomerTemp.getTreatedEntryTempId(), e);
			result = false;
		}

		return result;
	}

	@Transactional(readOnly = true)
	public List<TreatedEntryCustomerTemp> getAll() {
		log.debug("Finding all TreatedEntryCustomerTemp");
		try {
			return sessionFactory.getCurrentSession()
					.createQuery("from TreatedEntryCustomerTemp", TreatedEntryCustomerTemp.class)
					.getResultList();
		} catch (Exception e) {
			log.error("Error while fetching all TreatedEntryCustomerTemp", e);
			return new ArrayList<>();
		}
	}

	@Transactional(readOnly = true)
	public List<TreatedEntryCustomerTemp> getAllForEntryId(int entryId) {
		log.debug("Finding TreatedEntryCustomerTemp for entryId: {}", entryId);
		try {
			return currentSession()
					.createQuery("from TreatedEntryCustomerTemp where treatedEntryTempId = :entryId", TreatedEntryCustomerTemp.class)
					.setParameter("entryId", entryId)
					.getResultList();
		} catch (Exception e) {
			log.error("Error while finding TreatedEntryCustomerTemp for entryId: {}", entryId, e);
			return new ArrayList<>();
		}
	}

	@Transactional
	public boolean deleteAllByEntryId(int entryId) {
		log.info("Deleting TreatedEntryCustomerTemp by entryId: {}", entryId);
		try {
			// Utilisation d'un Bulk Delete HQL (plus performant qu'une boucle delete)
			int deletedCount = currentSession()
					.createQuery("delete from TreatedEntryCustomerTemp where treatedEntryTempId = :entryId")
					.setParameter("entryId", entryId)
					.executeUpdate();

			log.info("Delete DONE. Rows affected: {}", deletedCount);
			return true;
		} catch (Exception e) {
			log.error("Error during deleting TreatedEntryCustomerTemp by entryId: {}", entryId, e);
			return false;
		}
	}
}