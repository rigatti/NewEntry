package db.entry.treated;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
public class StockEntryDAO implements IStockEntryDAO {

	private final SessionFactory sessionFactory;

	public StockEntryDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Transactional
	public boolean insert(StockEntry stockEntry) {
		log.debug("Insert new StockEntry - " + stockEntry.getProductCode());

		try {
			currentSession().persist(stockEntry);
			log.debug("Save StockEntry done");
			return true;

		} catch (Exception e) {
			log.error(
					"Error while saving StockEntry (" + stockEntry.getProductCode() + ")",
					e
			);
			return false;
		}
	}

	@Transactional
	public boolean update(StockEntry stockEntry) {
		log.debug("Update Conditioning");

		try {
			currentSession().merge(stockEntry);
			log.debug("Update StockEntry done");
			return true;

		} catch (Exception e) {
			log.error(
					"Error while updating StockEntry (" + stockEntry.getProductCode() + ")",
					e
			);
			return false;
		}
	}

//
//	@Override
//	public List<Conditioning> get(String productCode, String supplierCode, Product.Unit unit) {
//		try {
//			StringBuilder hql = new StringBuilder(
//					"from Conditioning c where c.productCode = :productCode"
//			);
//
//			if (supplierCode != null) {
//				hql.append(" and c.supplierCode = :supplier");
//			}
//
//			if (unit != null) {
//				hql.append(" and c.unit = :unit and c.numberOfUnit = :nb");
//			}
//
//			var query = currentSession().createQuery(hql.toString(), Conditioning.class)
//					.setParameter("productCode", productCode);
//
//			if (supplierCode != null) {
//				query.setParameter("supplier", supplierCode);
//			}
//
//			if (unit != null) {
//				query.setParameter("unit", unit.getConditionnement());
//				query.setParameter("nb", unit.getNumber());
//			}
//
//			return query.getResultList();
//
//		} catch (Exception e) {
//			log.error("Error while finding product(" + productCode + ")", e);
//			return List.of();
//		}
//	}

	@Transactional(readOnly = true)
	public StockEntry get(int treatedEntryDetailDestinationId) {
		StockEntry stockEntry = new StockEntry();

		try {
			log.debug("Finding stock entries for id :" + treatedEntryDetailDestinationId);

			StringBuilder hql = new StringBuilder(
					"from StockEntry where " +
							"treatedEntryDetailDestinationId= :treatedEntryDetailDestinationId"
			);

			var query = currentSession().createQuery(hql.toString(), StockEntry.class)
					.setParameter("treatedEntryDetailDestinationId", treatedEntryDetailDestinationId);

			stockEntry = query.getSingleResult();

			log.debug("Finding supplier returns DONE");

		} catch (Exception e) {
			log.error("Error while finding supplier returns", e);
		}

		return stockEntry ;
	}

	@Transactional
	public boolean updateProductCode(String oldProductCode, String newProductCode) {

		boolean result = true;

		try {
			String hql = "UPDATE StockEntry s SET s.productCode = :newCode WHERE s.productCode = :oldCode";

			currentSession().createQuery(hql)
					.setParameter("newCode", newProductCode)
					.setParameter("oldCode", oldProductCode)
					.executeUpdate(); //

		} catch (Exception e) {
			result = false;
			log.error("Error while update StockEntry", e);
		}

		log.debug("Update StockEntry done");

		return result;
	}
}