package db.prepare;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Repository
@Slf4j
public class PackageDetailDAO implements IPackageDetailDAO{

	private final SessionFactory sessionFactory;

	public PackageDetailDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	@Transactional(readOnly = true)
	public List<PackageDetail> get(String productCode, Collection<Integer> orderNumbers) {

		if (orderNumbers == null || orderNumbers.isEmpty()) {
			return Collections.emptyList();
		}

		try {
			log.debug("Finding PackageDetail for product: {} and orders: {}", productCode, orderNumbers);

			// Utilisation de la clause IN : beaucoup plus propre que des OR en chaîne
			String hql = "from PackageDetail where productCode = :productCode " +
					"and orderNumber in (:orders)";

			return currentSession()
					.createQuery(hql, PackageDetail.class)
					.setParameter("productCode", productCode)
					.setParameterList("orders", orderNumbers) // Méthode spécifique pour les collections
					.getResultList();

		} catch (Exception e) {
			log.error("Error while finding PackageDetail for product: {}", productCode, e);
			return new ArrayList<>();
		}
	}
}