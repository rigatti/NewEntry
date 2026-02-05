package db.prepare;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
public class PackagingDAO implements IPackagingDAO {

	private final SessionFactory sessionFactory;

	public PackagingDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Transactional(readOnly = true)
	public Packaging get(int orderNumber, String orderLetter, String customerOrderCode, int customerOrderCodeNumber) {
		return get(orderNumber, orderLetter, customerOrderCode, customerOrderCodeNumber, -1);
	}

	@Transactional(readOnly = true)
	public Packaging get(int orderNumber, String orderLetter, String customerOrderCode, int customerOrderCodeNumber, int feesType) {
		log.debug("Finding Packaging for order: {}{}", orderNumber, orderLetter);

		try {
			StringBuilder hql = new StringBuilder(
					"from Packaging where orderNumber = :orderNumber " +
							"and orderLetter = :orderLetter " +
							"and customerOrderCode = :customerOrderCode " +
							"and customerOrderCodeNumber = :customerOrderCodeNumber"
			);

			if (feesType > 0) {
				hql.append(" and feesType = :feesType");
			}

			Query<Packaging> query = currentSession()
					.createQuery(hql.toString(), Packaging.class)
					.setParameter("orderNumber", orderNumber)
					.setParameter("orderLetter", orderLetter)
					.setParameter("customerOrderCode", customerOrderCode)
					.setParameter("customerOrderCodeNumber", customerOrderCodeNumber);

			if (feesType > 0) {
				query.setParameter("feesType", feesType);
			}

			// Gestion propre du résultat unique ou vide
			return query.uniqueResultOptional()
					.orElseGet(() -> {
						log.debug("No packaging found for given parameters");
						return new Packaging();
					});

		} catch (org.hibernate.NonUniqueResultException e) {
			log.error("Multiple packagings found for search criteria! FeesType was: {}", feesType);
			// Fallback pour garder le comportement d'origine (récupérer le premier)
			return sessionFactory.getCurrentSession()
					.createQuery("from Packaging where orderNumber = :orderNumber", Packaging.class)
					.setParameter("orderNumber", orderNumber)
					.setMaxResults(1)
					.uniqueResult();
		} catch (Exception e) {
			log.error("Error while finding packaging", e);
			return new Packaging();
		}
	}
}