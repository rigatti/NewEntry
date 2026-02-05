package db.entry.treated.temp;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class TreatedEntryTempDAO implements ITreatedEntryTempDAO {

	private final SessionFactory sessionFactory;

	public TreatedEntryTempDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public List<TreatedEntryTemp> getBySupplierCode(String supplierCode) {
		return getBySupplierCodeAndDate(supplierCode, null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TreatedEntryTemp> getBySupplierCodeAndDate(String supplierCode, String date) {

		log.debug("Finding TreatedEntryTemp for supplier: {}", supplierCode);
		try {
			StringBuilder hql = new StringBuilder("from TreatedEntryTemp where supplierCode = :supplierCode");
			if (date != null) {
				hql.append(" and arrivalDate = :date");
			}

			Query<TreatedEntryTemp> query = currentSession().createQuery(hql.toString(), TreatedEntryTemp.class);
			query.setParameter("supplierCode", supplierCode);
			if (date != null) query.setParameter("date", date);

			return query.getResultList();
		} catch (Exception e) {
			log.error("Error finding TreatedEntryTemp for supplier: {}", supplierCode, e);
			return new ArrayList<>();
		}
	}
	
//	private TreatedEntryTemp getById(int id) {
//		return currentSession().get(TreatedEntryTemp.class, id);
//	}

	@Override
	@Transactional(readOnly = true)
	public List<TreatedEntryTemp> get(String supplierCode, String date, String productCode) {
		try {
			StringBuilder hql = new StringBuilder("from TreatedEntryTemp where supplierCode = :supplierCode");
			if (date != null) hql.append(" and arrivalDate = :date");
			if (productCode != null) hql.append(" and productCode = :productCode");

			Query<TreatedEntryTemp> query = currentSession().createQuery(hql.toString(), TreatedEntryTemp.class);
			query.setParameter("supplierCode", supplierCode);
			if (date != null) query.setParameter("date", date);
			if (productCode != null) query.setParameter("productCode", productCode);

			return query.getResultList();
		} catch (Exception e) {
			log.error("Error in get method", e);
			return new ArrayList<>();
		}
	}

	@Override
	@Transactional
	public TreatedEntryTemp getByProductCodeAndUnit(String productCode, int numberOfUnit, String unit) {
		return getBySupplierCodeProductCodeAndUnit(null, productCode, numberOfUnit, unit);
	}
	@Override
	@Transactional
	public TreatedEntryTemp getBySupplierCodeProductCodeAndUnit(String supplierCode, String productCode, int numberOfUnit, String unit) {
		return getByDateSupplierCodeProductCodeAndUnit(null, null, productCode, numberOfUnit, unit);
	}


	@Override
	@Transactional(readOnly = true)
	public TreatedEntryTemp getByDateSupplierCodeProductCodeAndUnit(String date, String supplierCode, String productCode, int numberOfUnit, String unit) {
		try {
			StringBuilder hql = new StringBuilder("from TreatedEntryTemp where productCode = :productCode " +
					"and numberOfUnit = :numUnit and unitConditionnement = :unit");
			if (date != null) hql.append(" and arrivalDate = :date");
			if (supplierCode != null) hql.append(" and supplierCode = :supplierCode");

			Query<TreatedEntryTemp> query = currentSession().createQuery(hql.toString(), TreatedEntryTemp.class);
			query.setParameter("productCode", productCode);
			query.setParameter("numUnit", numberOfUnit);
			query.setParameter("unit", unit);
			if (date != null) query.setParameter("date", date);
			if (supplierCode != null) query.setParameter("supplierCode", supplierCode);

			return query.uniqueResultOptional().orElse(null);
		} catch (Exception e) {
			log.error("Error finding unique TreatedEntryTemp: {}", productCode, e);
			return null;
		}
	}


	@Override
	@Transactional
	public boolean save(TreatedEntryTemp entity) {
		try {
			currentSession().save(entity);
			return true;
		} catch (Exception e) {
			log.error("Error saving TreatedEntryTemp", e);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean update(TreatedEntryTemp entity) {
		try {
			currentSession().update(entity);
			return true;
		} catch (Exception e) {
			log.error("Error updating TreatedEntryTemp: {}", entity.getId(), e);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean delete(TreatedEntryTemp entity) {
		try {
			currentSession().delete(entity);
			return true;
		} catch (Exception e) {
			log.error("Error deleting TreatedEntryTemp: {}", entity.getId(), e);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean deleteAll(String supplierCode, String arrivalDate, ITreatedEntryCustomerTempDAO tecDAO) {
		try {
			List<TreatedEntryTemp> list = getBySupplierCodeAndDate(supplierCode, arrivalDate);
			for (TreatedEntryTemp tet : list) {
				tecDAO.deleteAllByEntryId(tet.getId());
				currentSession().delete(tet);
			}
			return true;
		} catch (Exception e) {
			log.error("Error in deleteAll", e);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean cleanDb(ITreatedEntryCustomerTempDAO tecDAO) {
		log.debug("Clean DB - START");
		try {
			// Utiliser la session passée en paramètre si présente

			List<TreatedEntryCustomerTemp> tects = tecDAO.getAll();
			for (TreatedEntryCustomerTemp tect : tects) {
				TreatedEntryTemp tet = currentSession().get(TreatedEntryTemp.class, tect.getTreatedEntryTempId());
				if (tet == null) {
					tecDAO.deleteAllByEntryId(tect.getTreatedEntryTempId());
				}
			}
			log.debug("Clean DB - END");
			return true;
		} catch (Exception e) {
			log.error("Error during cleanDb", e);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean updateProductCode(String oldProductCode, String newProductCode) {
		boolean result = true;

		String sql = "update TreatedEntryTemp set productCode='" + newProductCode + "' where productCode='" + oldProductCode + "'"; 

		log.debug("Update treated entries temp - " + sql);

		try {
			currentSession().createQuery(sql).executeUpdate();
		} catch (Exception e) {
			result = false;
			log.error("Error while update treated entries temp", e);
		}

		log.debug("Update treated entries temp done");

		return result;
		
	}
}