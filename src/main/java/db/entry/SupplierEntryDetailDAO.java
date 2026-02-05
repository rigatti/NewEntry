package db.entry;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
public class SupplierEntryDetailDAO implements ISupplierEntryDetailDAO {


	private final SessionFactory sessionFactory;

	public SupplierEntryDetailDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	@Transactional
	public boolean save(SupplierEntryDetail detail) {
		try {
			log.debug("Saving SupplierEntryDetail for supplier: {}", detail.getSupplierCode());

			currentSession().save(detail);

			log.debug("Save SupplierEntryDetail DONE");
			return true;
		} catch (Exception e) {
			log.error("Error while saving supplierEntryDetail: {} (ID: {})",
					detail.getSupplierCode(), detail.getSupplierEntryId(), e);
			return false;
		}
	}
}