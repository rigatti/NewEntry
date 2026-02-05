package db.product;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
public class DescriptionDAO implements IDescriptionDAO {

	private final SessionFactory sessionFactory;

	public DescriptionDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	@Transactional
	public boolean insert(Description description) {
		log.debug("Insert new Description - " + description.getProductCode());

		try {
			currentSession().persist(description);
			log.debug("Save Description done");
			return true;

		} catch (Exception e) {
			log.error(
					"Error while saving Description (" + description.getProductCode() + ")",
					e
			);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean updateProductCode(String oldProductCode, String newProductCode) {
		log.debug("Update Description productCode from " + oldProductCode + " to " + newProductCode);

		try {
			currentSession()
					.createQuery(
							"update Description d set d.productCode = :newCode where d.productCode = :oldCode"
					)
					.setParameter("newCode", newProductCode)
					.setParameter("oldCode", oldProductCode)
					.executeUpdate();

			log.debug("Update Description done");
			return true;

		} catch (Exception e) {
			log.error("Error while updating Description", e);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean deleteProductCode(String productCode) {
		log.debug("Delete Description for productCode=" + productCode);

		try {
			currentSession()
					.createQuery(
							"delete from Description d where d.productCode = :code"
					)
					.setParameter("code", productCode)
					.executeUpdate();

			log.debug("Delete Description done");
			return true;

		} catch (Exception e) {
			log.error("Error while deleting Description", e);
			return false;
		}
	}
}
