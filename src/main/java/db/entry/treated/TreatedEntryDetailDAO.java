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
public class TreatedEntryDetailDAO implements ITreatedEntryDetailDAO {

	private SessionFactory sessionFactory;

	public TreatedEntryDetailDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	@Transactional
	public boolean insert(TreatedEntryDetail detail) {
		try {
			log.debug("Inserting TreatedEntryDetail for ID: {}", detail.getTreatedEntryId());
			currentSession().save(detail);
			return true;
		} catch (Exception e) {
			log.error("Error during insert of TreatedEntryDetail for ID: {}", detail.getTreatedEntryId(), e);
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public TreatedEntryDetail get(int treatedEntryId) {
		log.debug("Finding TreatedEntryDetail for ID: {}", treatedEntryId);
		try {
			String hql = "from TreatedEntryDetail where treatedEntryId = :id";
			return currentSession()
					.createQuery(hql, TreatedEntryDetail.class)
					.setParameter("id", treatedEntryId)
					.uniqueResultOptional()
					.orElseGet(TreatedEntryDetail::new); // Retourne un objet vide si non trouvé
		} catch (Exception e) {
			log.error("Error while finding TreatedEntryDetail for ID: {}", treatedEntryId, e);
			return new TreatedEntryDetail();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<TreatedEntryDetail> get(String arrivalDate) {
		log.debug("Finding TreatedEntryDetails for date: {}", arrivalDate);
		try {
			String hql = "from TreatedEntryDetail where arrivalDate = :date";
			return currentSession()
					.createQuery(hql, TreatedEntryDetail.class)
					.setParameter("date", arrivalDate)
					.getResultList();
		} catch (Exception e) {
			log.error("Error while finding TreatedEntryDetails for date: {}", arrivalDate, e);
			return new ArrayList<>();
		}
	}
}