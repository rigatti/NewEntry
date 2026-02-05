package db.entry;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
@Slf4j
public class PlanningDAO implements IPlanningDAO {

	private final SessionFactory sessionFactory;

	public PlanningDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	public List<Planning> getByDate(String date) {
		return getByDateAndSupplier(date, null);
	}
//
//		log.debug("Finding SupplierEntry by id: {}", supplierEntryId);
//
//		return currentSession()
//				.createQuery(
//						"from SupplierEntry where supplierEntryId = :id",
//						SupplierEntry.class
//	)
//				.setParameter("id", supplierEntryId)
//				.uniqueResult();
//}
	@Transactional(readOnly = true)
	public List<Planning> getByDateAndSupplier(String date, String supplierCode) {

		log.debug("Finding Planning by date: {} and supplier {}", date, supplierCode);

		try {
			StringBuilder hql = new StringBuilder("from Planning " +
					"group by orderNumber, supplierCode, plannedDate, creationNumber, plannedMode " +
					"having plannedDate=CONVERT(smalldatetime, :date, 113) ");


			if (StringUtils.isNotBlank(supplierCode)) {
				hql.append(" and supplierCode= :supplierCode  ");
			}
			hql.append("order by supplierCode");

			var query = currentSession().createQuery(hql.toString(), Planning.class);

			query.setParameter("date", date);

			if (StringUtils.isNotBlank(supplierCode)) {
				query.setParameter("supplierCode", supplierCode);
			}

			return query.getResultList();

		} catch (Exception e) {
			log.error("Error while finding Planning by date and/or supplier code", e);
			return List.of();
		}
	}

}