package db.entry;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Strings;
import org.belex.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Slf4j
public class SupplierEntryDAO implements ISupplierEntryDAO {

	private final SessionFactory sessionFactory;

	public SupplierEntryDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	@Transactional(readOnly = true)
	public SupplierEntry getBySupplierEntryId(int supplierEntryId) {
		log.debug("Finding SupplierEntry by id: {}", supplierEntryId);

		return currentSession()
				.createQuery(
						"from SupplierEntry where supplierEntryId = :id",
						SupplierEntry.class
				)
				.setParameter("id", supplierEntryId)
				.uniqueResult();
	}

	@Override
	@Transactional(readOnly = true)
	public List<SupplierEntry> getSuppliersByDate(String rawDate) {
		log.debug("Finding SupplierEntry by date: {}", rawDate);
		String date = rawDate;

		if ( ! Strings.CI.contains(rawDate, "-")) {
			date = Util.formatDate(rawDate, "yyyyMMdd", "yyyy-MM-dd");
		}

		return currentSession()
				.createQuery(
						"from SupplierEntry where entryDate = :date",
						SupplierEntry.class
				)
				.setParameter("date", date)
				.list();
	}

	@Override
	@Transactional(readOnly = true)
	public SupplierEntry getSupplierByDate(String supplierCode, String date) {
		log.debug("Finding SupplierEntry: {} {}", supplierCode, date);

		return currentSession()
				.createQuery(
						"from SupplierEntry " +
								"where supplierCode = :supplierCode " +
								"and entryDate = :date",
						SupplierEntry.class
				)
				.setParameter("supplierCode", supplierCode)
				.setParameter("date", date)
				.uniqueResult();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean exists(SupplierEntry supplierEntry) {
		log.debug("Checking existence of SupplierEntry");

		Long count = currentSession()
				.createQuery(
						"select count(se) from SupplierEntry se " +
								"where se.supplierCode = :supplierCode " +
								"and se.orderNumbers = :orderNumbers " +
								"and se.entryDate = :entryDate " +
								"and se.entryTime = :entryTime",
						Long.class
				)
				.setParameter("supplierCode", supplierEntry.getSupplierCode())
				.setParameter("orderNumbers", supplierEntry.getOrderNumbers())
				.setParameter("entryDate", supplierEntry.getEntryDate())
				.setParameter("entryTime", supplierEntry.getEntryTime())
				.uniqueResult();

		return count != null && count > 0;
	}

	@Override
	@Transactional
	public boolean save(SupplierEntry supplierEntry) {
		try {
			log.debug("Saving SupplierEntry");
			currentSession().persist(supplierEntry);
			return true;
		} catch (Exception e) {
			log.error("Error while saving SupplierEntry {}", supplierEntry.getSupplierCode(), e);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean update(SupplierEntry supplierEntry) {
		try {
			log.debug("Updating SupplierEntry {}", supplierEntry.getSupplierEntryId());
			currentSession().merge(supplierEntry);
			return true;
		} catch (Exception e) {
			log.error("Error while updating SupplierEntry {}", supplierEntry.getSupplierCode(), e);
			return false;
		}
	}
}
