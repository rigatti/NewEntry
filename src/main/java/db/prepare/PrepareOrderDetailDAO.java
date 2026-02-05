package db.prepare;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Slf4j
public class PrepareOrderDetailDAO implements IPrepareOrderDetailDAO {

	private final SessionFactory sessionFactory;

	public PrepareOrderDetailDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Transactional(readOnly = true)
	public String getCustomerCode(int orderNumber, String orderLetter,  
									String customerOrderCode, int customerOrderCodeNumber, int feesType) {


		log.debug("Recherche du code client pour la commande : {}{}", orderNumber, orderLetter);

		try {
			// Projection HQL pour ne récupérer que le champ nécessaire
			String hql = "select pod.customerCode from PrepareOrderDetail pod where " +
					"pod.orderNumber = :orderNumber and " +
					"pod.orderLetter = :orderLetter and " +
					"pod.customerOrderCode = :customerOrderCode and " +
					"pod.customerOrderCodeNumber = :customerOrderCodeNumber and " +
					"pod.feesType = :feesType";

			return sessionFactory.getCurrentSession()
					.createQuery(hql, String.class) // On spécifie qu'on attend un String
					.setParameter("orderNumber", orderNumber)
					.setParameter("orderLetter", orderLetter)
					.setParameter("customerOrderCode", customerOrderCode)
					.setParameter("customerOrderCodeNumber", customerOrderCodeNumber)
					.setParameter("feesType", feesType)
					.uniqueResultOptional() // Gère proprement 0 ou 1 résultat
					.orElseGet(() -> {
						log.debug("Aucun détail de commande trouvé");
						return "";
					});

		} catch (org.hibernate.NonUniqueResultException e) {
			log.error("Plusieurs codes clients trouvés pour les critères donnés !");
			return "";
		} catch (Exception e) {
			log.error("Erreur lors de la récupération du code client", e);
			return "";
		}
	}

}