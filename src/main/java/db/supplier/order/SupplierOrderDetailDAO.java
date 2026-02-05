package db.supplier.order;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Repository
@Slf4j
public class SupplierOrderDetailDAO implements ISupplierOrderDetailDAO {


	private final SessionFactory sessionFactory;

	public SupplierOrderDetailDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}


	@Override
	@Transactional(readOnly = true)
	public List<SupplierOrderDetail> get(String supplierCode, int orderNumber, String productCode) {
		log.debug("Finding SupplierOrderDetail - Supplier: {}, Order: {}, Product: {}",
				supplierCode, orderNumber, productCode);

		try {
			StringBuilder hql = new StringBuilder("from SupplierOrderDetail where orderNumber = :orderNumber");

			// Gestion de la condition spécifique au code fournisseur '01'
			if (StringUtils.equals(supplierCode, "01")) {
				hql.append(" and (supplierCode = '01' or supplierCode = '' or supplierCode is null)");
			} else {
				hql.append(" and supplierCode = :supplierCode");
			}

			if (StringUtils.isNotEmpty(productCode)) {
				hql.append(" and productCode = :productCode");
			}

			Query<SupplierOrderDetail> query = sessionFactory.getCurrentSession()
					.createQuery(hql.toString(), SupplierOrderDetail.class)
					.setParameter("orderNumber", orderNumber);

			if (!StringUtils.equals(supplierCode, "01")) {
				query.setParameter("supplierCode", supplierCode);
			}

			if (StringUtils.isNotEmpty(productCode)) {
				query.setParameter("productCode", productCode);
			}

			return query.getResultList();

		} catch (Exception e) {
			log.error("Error while finding SupplierOrderDetail for order: {}", orderNumber, e);
			return new ArrayList<>();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<SupplierOrderDetail> get(String supplierCode, int orderNumber) {
		return get(supplierCode, orderNumber, null);
	}
}