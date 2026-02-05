package db.prepare;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
public class PrepareOrderDAO implements IPrepareOrderDAO {

	private final SessionFactory sessionFactory;

	public PrepareOrderDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	@Transactional(readOnly = true)
	public PrepareOrder get(int orderNumber, String orderLetter) {
		log.debug("Recherche de la commande : {}{}", orderNumber, orderLetter);

		try {
			String hql = "from PrepareOrder where orderNumber = :num and orderLetter = :letter";

			return currentSession()
					.createQuery(hql, PrepareOrder.class)
					.setParameter("num", orderNumber)
					.setParameter("letter", orderLetter)
					.uniqueResultOptional()
					.orElseGet(() -> {
						log.debug("Aucune commande trouvée");
						return new PrepareOrder();
					});

		} catch (org.hibernate.NonUniqueResultException e) {
			log.error("Plusieurs commandes trouvées pour le critère : {}{}", orderNumber, orderLetter);
			return new PrepareOrder();
		} catch (Exception e) {
			log.error("Erreur lors de la recherche de la commande", e);
			return new PrepareOrder();
		}
	}
}