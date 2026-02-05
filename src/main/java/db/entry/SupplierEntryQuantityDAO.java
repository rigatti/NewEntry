package db.entry;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
public class SupplierEntryQuantityDAO implements ISupplierEntryQuantityDAO {

	private final SessionFactory sessionFactory;

	public SupplierEntryQuantityDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}


	@Override
	@Transactional
	public boolean save(SupplierEntryQuantity supplierEntryQuantity) {
		try {
			log.debug("Saving SupplierEntryQuantity for product: {}", supplierEntryQuantity.getProductCode());
			currentSession().save(supplierEntryQuantity);
			return true;
		} catch (Exception e) {
			log.error("Error while saving supplierEntryQuantity: {} ({})",
					supplierEntryQuantity.getProductCode(), supplierEntryQuantity.getSupplierEntryId(), e);
			return false;
		}
	}


	@Override
	@Transactional
	public boolean update(SupplierEntryQuantity supplierEntryQuantity) {
		try {
			log.debug("Updating SupplierEntryQuantity");
			currentSession().update(supplierEntryQuantity);
			return true;
		} catch (Exception e) {
			log.error("Error while updating supplierEntryQuantity: {} ({})",
					supplierEntryQuantity.getProductCode(), supplierEntryQuantity.getSupplierEntryId(), e);
			return false;
		}
	}
	@Override
	@Transactional(readOnly = true)
	public SupplierEntryQuantity getUnique(int supplierEntryId, String productCode) {
		log.debug("Finding unique SupplierEntryQuantity: {} - {}", supplierEntryId, productCode);

		try {
			String hql = "from SupplierEntryQuantity where productCode = :productCode " +
					"and supplierEntryId = :supplierEntryId";

			return currentSession()
					.createQuery(hql, SupplierEntryQuantity.class)
					.setParameter("productCode", productCode)
					.setParameter("supplierEntryId", supplierEntryId)
					.uniqueResultOptional() // Hibernate 5.2+ : évite les casts et gère proprement le null
					.orElse(null);

		} catch (Exception e) {
			log.error("Error while finding SupplierEntryQuantity for ID: {} and product: {}",
					supplierEntryId, productCode, e);
			return null;
		}
	}
	
}