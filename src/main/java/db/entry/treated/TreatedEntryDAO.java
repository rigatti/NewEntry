package db.entry.treated;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.belex.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Collections;
import java.util.List;

@Repository
@Slf4j
public class TreatedEntryDAO implements ITreatedEntryDAO {

	private static final int CLEAN_BATCH_SIZE = 100;

	private final SessionFactory sessionFactory;
	private final TransactionTemplate cleanBatch;

	public TreatedEntryDAO(SessionFactory sessionFactory, PlatformTransactionManager transactionManager) {
		this.sessionFactory = sessionFactory;
		this.cleanBatch = new TransactionTemplate(transactionManager);
		this.cleanBatch.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	@Transactional
	public boolean insert(TreatedEntry treatedEntry) {
		try {
			log.debug("Inserting TreatedEntry: {}", treatedEntry.getProductCode());
			currentSession().save(treatedEntry);
			return true;
		} catch (Exception e) {
			log.error("Error during insert of TreatedEntry: {}", treatedEntry.getProductCode(), e);
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public TreatedEntry get(int treatedEntryId) {
		log.debug("Finding TreatedEntry for id: {}", treatedEntryId);
		TreatedEntry entry = currentSession().get(TreatedEntry.class, treatedEntryId);
		return entry != null ? entry : new TreatedEntry();
	}

	@Override
	@Transactional(readOnly = true)
	public TreatedEntry getUnique(String supplierCode, String arrivalDate, String productCode) {
		log.debug("Finding unique TreatedEntry for supplier: {}, product: {}", supplierCode, productCode);

		String hql = "from TreatedEntry where supplierCode = :supplierCode " +
				"and arrivalDate = :arrivalDate and productCode = :productCode";

		return currentSession().createQuery(hql, TreatedEntry.class)
				.setParameter("supplierCode", supplierCode)
				.setParameter("arrivalDate", arrivalDate)
				.setParameter("productCode", productCode)
				.uniqueResultOptional()
				.orElseGet(TreatedEntry::new);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TreatedEntry> getByDateRange(String supplierCode, String arrivalStartDate, String arrivalEndDate) {
		log.debug("Finding TreatedEntry range for supplier: {}", supplierCode);

		StringBuilder hql = new StringBuilder("from TreatedEntry where supplierCode = :supplierCode");
		if (StringUtils.isNotBlank(arrivalStartDate)) hql.append(" and arrivalDate >= :startDate");
		if (StringUtils.isNotBlank(arrivalEndDate)) hql.append(" and arrivalDate <= :endDate");

		Query<TreatedEntry> query = currentSession().createQuery(hql.toString(), TreatedEntry.class);
		query.setParameter("supplierCode", supplierCode);

		if (StringUtils.isNotBlank(arrivalStartDate)) {
			String dateValidated = getValidatedDateFormat(arrivalStartDate);
			query.setParameter("startDate", dateValidated);
		}
		if (StringUtils.isNotBlank(arrivalEndDate)) {
			String dateValidated = getValidatedDateFormat(arrivalEndDate);
			query.setParameter("endDate", dateValidated);
		}

		return query.getResultList();
	}

	private static String getValidatedDateFormat(String arrivalStartDate) {
		String dateValidated = arrivalStartDate;
		if (dateValidated.contains("-")) {
			dateValidated = Util.formatDate(arrivalStartDate, "dd-MM-yyyy", "yyyyMMdd");
		}
		if (dateValidated.contains("/")) {
			dateValidated = Util.formatDate(arrivalStartDate, "dd/MM/yyyy", "yyyyMMdd");
		}
		return dateValidated;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TreatedEntry> get(String supplierCode, String arrivalDate) {
		log.debug("Finding TreatedEntry - Supplier: {}, Date: {}", supplierCode, arrivalDate);

		try {
			StringBuilder hql = new StringBuilder("from TreatedEntry where supplierCode = :supplierCode");
			if (StringUtils.isNotBlank(arrivalDate)) {
				hql.append(" and arrivalDate = :arrivalDate");
			}

			// Utilisation de l'interface Query d'Hibernate 5.2+
			Query<TreatedEntry> query = currentSession().createQuery(hql.toString(), TreatedEntry.class);

			query.setParameter("supplierCode", supplierCode);
			if (StringUtils.isNotBlank(arrivalDate)) {
				query.setParameter("arrivalDate", arrivalDate);
			}

			return query.getResultList();

		} catch (Exception e) {
			log.error("Error while finding treated entries for supplier: {}", supplierCode, e);
			return Collections.emptyList();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<TreatedEntry> get(String productCode, String ean, String lotNumber, String validityDate) {

		StringBuilder hql = new StringBuilder("from TreatedEntry where 1=1");

		if (StringUtils.isNotEmpty(productCode)) hql.append(" and productCode = :productCode");
		if (StringUtils.isNotEmpty(ean)) hql.append(" and ean = :ean");
		if (StringUtils.isNotEmpty(lotNumber)) hql.append(" and lotNumber LIKE :lotNumber");
		if (StringUtils.isNotEmpty(validityDate)) hql.append(" and validityDate = :validityDate");

		Query<TreatedEntry> query = currentSession().createQuery(hql.toString(), TreatedEntry.class);

		if (StringUtils.isNotEmpty(productCode)) query.setParameter("productCode", productCode);
		if (StringUtils.isNotEmpty(ean)) query.setParameter("ean", ean);
		if (StringUtils.isNotEmpty(lotNumber)) query.setParameter("lotNumber", "%" + lotNumber + "%");
		if (StringUtils.isNotEmpty(validityDate)) query.setParameter("validityDate", validityDate);

		return query.getResultList();
	}

	@Override
	@Transactional
	public boolean updateProductCode(String oldProductCode, String newProductCode) {
		log.debug("Updating product code from {} to {}", oldProductCode, newProductCode);
		String hql = "update TreatedEntry set productCode = :newCode where productCode = :oldCode";
		try {
			int updated = currentSession().createQuery(hql)
					.setParameter("newCode", newProductCode)
					.setParameter("oldCode", oldProductCode)
					.executeUpdate();
			return updated > 0;
		} catch (Exception e) {
			log.error("Error while update treated entries", e);
			return false;
		}
	}

	@Override
	public boolean cleanDb(String maxDate, ITreatedEntryDetailDAO treatedEntryDetailDAO, ITreatedEntryDetailDestinationDAO treatedEntryDetailDestinationDAO) {
		List<Integer> ids;
		try {
			ids = cleanBatch.execute(status -> currentSession()
					.createQuery("select e.treatedEntryId from TreatedEntry e where e.arrivalDate < :maxDate", Integer.class)
					.setParameter("maxDate", maxDate)
					.getResultList());
		} catch (Exception e) {
			log.error("Error while listing entries to clean before {}", maxDate, e);
			return false;
		}
		if (ids == null || ids.isEmpty()) {
			return true;
		}

		for (int from = 0; from < ids.size(); from += CLEAN_BATCH_SIZE) {
			List<Integer> batch = ids.subList(from, Math.min(from + CLEAN_BATCH_SIZE, ids.size()));
			try {
				cleanBatch.executeWithoutResult(status -> {
					for (Integer id : batch) {
						TreatedEntry entry = currentSession().get(TreatedEntry.class, id);
						if (entry == null) {
							continue;
						}
						treatedEntryDetailDestinationDAO.getAll(entry.getTreatedEntryId()).forEach(currentSession()::delete);
						TreatedEntryDetail detail = treatedEntryDetailDAO.get(entry.getTreatedEntryId());
						if (detail != null) {
							currentSession().delete(detail);
						}
						currentSession().delete(entry);
					}
				});
				log.info("Cleaned entries {} to {} of {} before {}", from + 1, from + batch.size(), ids.size(), maxDate);
			} catch (Exception e) {
				log.error("Error during cleanDb batch starting at {} before {}", from, maxDate, e);
				return false;
			}
		}
		return true;
	}
}