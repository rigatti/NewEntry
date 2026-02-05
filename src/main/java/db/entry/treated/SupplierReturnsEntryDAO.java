package db.entry.treated;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Slf4j
public class SupplierReturnsEntryDAO implements ISupplierReturnsEntryDAO {

	private final SessionFactory sessionFactory;

	public SupplierReturnsEntryDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	@Transactional
	public boolean insert(SupplierReturnsEntry supplierReturnsEntry) {

		try {
			log.debug("Inserting SupplierReturnsEntry for product: {}", supplierReturnsEntry.getProductCode());
			currentSession().save(supplierReturnsEntry);
			return true;
		} catch (Exception e) {
			log.error("Error during insert of SupplierReturnsEntry: {}", supplierReturnsEntry.getProductCode(), e);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean update(SupplierReturnsEntry supplierReturnsEntry) {
		try {
			log.debug("Updating SupplierReturnsEntry for product: {}", supplierReturnsEntry.getProductCode());
			currentSession().update(supplierReturnsEntry);
			return true;
		} catch (Exception e) {
			log.error("Error during update of SupplierReturnsEntry: {}", supplierReturnsEntry.getProductCode(), e);
			return false;
		}
	}

	@Transactional(readOnly = true)
	public SupplierReturnsEntry get(int treatedEntryDetailDestinationId) {

		log.debug("Finding SupplierReturnsEntry for id: {}", treatedEntryDetailDestinationId);

		String hql = "from SupplierReturnsEntry where treatedEntryDetailDestinationId = :id";

		return currentSession()
				.createQuery(hql, SupplierReturnsEntry.class)
				.setParameter("id", treatedEntryDetailDestinationId)
				.uniqueResultOptional() // Java 8+ Optional support
				.orElseGet(() -> {
					log.debug("No entry found for id: {}", treatedEntryDetailDestinationId);
					return new SupplierReturnsEntry();
				});
	}

	@Transactional
	public boolean updateProductCode(String oldProductCode, String newProductCode) {

		log.debug("Updating product code from {} to {}", oldProductCode, newProductCode);

		String hql = "UPDATE SupplierReturnsEntry SET productCode = :newCode WHERE productCode = :oldCode";

		try {
			int updated = currentSession().createQuery(hql)
					.setParameter("newCode", newProductCode)
					.setParameter("oldCode", oldProductCode)
					.executeUpdate();
			return updated > 0;
		} catch (Exception e) {
			log.error("Error updating product code", e);
			return false;
		}

	}
}