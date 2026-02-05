package db.entry.treated;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class TreatedEntryDetailDestinationDAO implements ITreatedEntryDetailDestinationDAO {

	private final SessionFactory sessionFactory;

	public TreatedEntryDetailDestinationDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	@Transactional
	public boolean insert(TreatedEntryDetailDestination destination) {
		try {
			log.debug("Inserting TreatedEntryDetailDestination for detailId: {}", destination.getTreatedEntryDetailId());
			currentSession().save(destination);
			return true;
		} catch (Exception e) {
			log.error("Error during insert of TreatedEntryDetailDestination: {}", destination.getTreatedEntryDetailId(), e);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean update(TreatedEntryDetailDestination destination) {
		try {
			log.debug("Updating TreatedEntryDetailDestination: {}", destination.getId());
			currentSession().update(destination);
			return true;
		} catch (Exception e) {
			log.error("Error during update of TreatedEntryDetailDestination: {}", destination.getId(), e);
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<TreatedEntryDetailDestination> getAll(int entryDetailId) {
		log.debug("Finding destinations for detailId: {}", entryDetailId);
		try {
			String hql = "from TreatedEntryDetailDestination where treatedEntryDetailId = :detailId";
			return currentSession()
					.createQuery(hql, TreatedEntryDetailDestination.class)
					.setParameter("detailId", entryDetailId)
					.getResultList();
		} catch (Exception e) {
			log.error("Error while finding destinations for detailId: {}", entryDetailId, e);
			return new ArrayList<>();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public TreatedEntryDetailDestination get(int id) {
		log.debug("Finding destination by ID: {}", id);
		// Utilisation de session.get() pour une recherche par Clé Primaire
		TreatedEntryDetailDestination tedd = currentSession()
				.get(TreatedEntryDetailDestination.class, id);

		return (tedd != null) ? tedd : new TreatedEntryDetailDestination();
	}
}