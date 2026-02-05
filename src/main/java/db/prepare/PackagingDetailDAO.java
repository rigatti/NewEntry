package db.prepare;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
public class PackagingDetailDAO implements IPackagingDetailDAO {

	private final SessionFactory sessionFactory;

	public PackagingDetailDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	@Transactional(readOnly = true)
	public PackagingDetail get(int orderNumber, String orderLetter, String customerOrderCode, int customerOrderCodeNumber, int feesType, int fromPackageNumber) {

		log.debug("Finding PackagingDetail for order: {}{} and package: {}",
				orderNumber, orderLetter, fromPackageNumber);

		try {
			String hql = "from PackagingDetail where orderNumber = :orderNumber " +
					"and orderLetter = :orderLetter " +
					"and customerOrderCode = :customerOrderCode " +
					"and customerOrderCodeNumber = :customerOrderCodeNumber " +
					"and feesType = :feesType " +
					"and fromPackageNumber = :fromPackageNumber";

			return sessionFactory.getCurrentSession()
					.createQuery(hql, PackagingDetail.class)
					.setParameter("orderNumber", orderNumber)
					.setParameter("orderLetter", orderLetter)
					.setParameter("customerOrderCode", customerOrderCode)
					.setParameter("customerOrderCodeNumber", customerOrderCodeNumber)
					.setParameter("feesType", feesType)
					.setParameter("fromPackageNumber", fromPackageNumber)
					.uniqueResultOptional() // Gère proprement 0 ou 1 résultat
					.orElseGet(() -> {
						log.debug("No packagingDetail found");
						return new PackagingDetail();
					});

		} catch (org.hibernate.NonUniqueResultException e) {
			log.error("Multiple packagingDetail found for the given criteria!");
			return new PackagingDetail();
		} catch (Exception e) {
			log.error("Error while finding packagingDetail", e);
			return new PackagingDetail();
		}
	}
}