package db.customer.order;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@Slf4j
public class CustomerOrderDetailDAO implements ICustomerOrderDetailDAO {

	private final SessionFactory sessionFactory;

	public CustomerOrderDetailDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}


	@Override
	@Transactional(readOnly = true)
	public List<CustomerOrderDetail> get(Set<Integer> orderNumbers) {
		if (orderNumbers == null || orderNumbers.isEmpty()) return Collections.emptyList();

		log.debug("Finding CustomerOrderDetail by orderNumbers: {}", orderNumbers);
		try {
			// Utilisation de la clause IN (plus performant et propre)
			String hql = "from CustomerOrderDetail where orderNumber in (:orderNumbers)";
			return currentSession().createQuery(hql, CustomerOrderDetail.class)
					.setParameterList("orderNumbers", orderNumbers)
					.getResultList();
		} catch (Exception e) {
			log.error("Error finding CustomerOrderDetail by set", e);
			return new ArrayList<>();
		}
	}


	@Override
	@Transactional(readOnly = true)
	public List<CustomerOrderDetail> getByProductCode(String productCode, StringTokenizer orderNumbers) {
		List<String> orders = new ArrayList<>();
		while (orderNumbers.hasMoreElements()) orders.add(orderNumbers.nextToken());

		if (orders.isEmpty()) return Collections.emptyList();

		log.debug("Finding CustomerOrderDetail for product: {}", productCode);
		try {
			String hql = "from CustomerOrderDetail where orderNumber in (:orders) and productCode = :productCode";
			return currentSession().createQuery(hql, CustomerOrderDetail.class)
					.setParameterList("orders", orders)
					.setParameter("productCode", productCode)
					.getResultList();
		} catch (Exception e) {
			log.error("Error in getByProductCode", e);
			return new ArrayList<>();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getLettersByOrderNumber(int orderNumber) {
		log.debug("Finding order letters for order: {}", orderNumber);
		try {
			String hql = "select distinct orderLetter from CustomerOrderDetail where orderNumber = :num";
			return currentSession().createQuery(hql, String.class)
					.setParameter("num", orderNumber)
					.getResultList();
		} catch (Exception e) {
			log.error("Error getting letters for order: {}", orderNumber, e);
			return new ArrayList<>();
		}
	}


	@Override
	@Transactional(readOnly = true)
	public List<CustomerOrderDetail> get(String productCode, String supplierOrderNumbers, String supplierOrderLetters,
										 String customerCodeOrder, int customerCodeOrderNumber) {
		log.debug("Finding CustomerOrderDetail for product: {}", productCode);
		try {
			String hql = "from CustomerOrderDetail where productCode = :pc and orderNumber = :on " +
					"and orderLetter = :ol and customerOrderCode = :coc and customerOrderCodeNumber = :cocn";

			return currentSession().createQuery(hql, CustomerOrderDetail.class)
					.setParameter("pc", productCode)
					.setParameter("on", supplierOrderNumbers)
					.setParameter("ol", supplierOrderLetters)
					.setParameter("coc", customerCodeOrder)
					.setParameter("cocn", customerCodeOrderNumber)
					.getResultList();
		} catch (Exception e) {
			log.error("Error in complex get", e);
			return new ArrayList<>();
		}
	}


	@Override
	@Transactional
	public boolean update(CustomerOrderDetail sod) {
		try {
			log.debug("Updating CustomerOrderDetail ID: {}", sod.getId());
			currentSession().update(sod);
			return true;
		} catch (Exception e) {
			log.error("Error updating CustomerOrderDetail: {}", sod.getId(), e);
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<CustomerOrderDetail> get(int orderNumber, String orderLetter) {
		log.debug("Finding CustomerOrderDetail - Num: {}, Letter: {}", orderNumber, orderLetter);
		try {
			StringBuilder hql = new StringBuilder("from CustomerOrderDetail where orderNumber = :num");
			if (orderLetter != null) hql.append(" and orderLetter = :letter");

			var query = currentSession().createQuery(hql.toString(), CustomerOrderDetail.class)
					.setParameter("num", orderNumber);

			if (orderLetter != null) query.setParameter("letter", orderLetter);

			return query.getResultList();
		} catch (Exception e) {
			log.error("Error in get by number/letter", e);
			return new ArrayList<>();
		}
	}
}