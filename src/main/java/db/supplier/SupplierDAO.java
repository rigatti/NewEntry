package db.supplier;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Slf4j
public class SupplierDAO implements ISupplierDAO {

	private final SessionFactory sessionFactory;

	public SupplierDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Transactional(readOnly = true)
	public List<Supplier> getAll() {
		List<Supplier> suppliers = null;
		try {

			log.debug("Finding all suppliers");
			return currentSession().createQuery(
					"from Supplier ORDER BY DescriptionFournisseur ASC",
					Supplier.class
			).list();

		} catch (Exception e) {
			log.error("Error while finding suppliers", e);
		}

		return suppliers;
	}

	@Transactional(readOnly = true)
	public Supplier getByCode(String supplierCode) {
		Supplier supplier = new Supplier(supplierCode);
		
		try {
			log.debug("Finding Suppliers from " + supplierCode);

			StringBuilder hql = new StringBuilder(
					"from Supplier c where supplierCode = :supplierCode"
			);

			var query = currentSession().createQuery(hql.toString(), Supplier.class);

			query.setParameter("supplierCode", supplierCode);

			return query.getSingleResult();

		} catch (Exception e) {
			log.error("Error while finding suppliers", e);
		}
		
		return supplier;
	}

}