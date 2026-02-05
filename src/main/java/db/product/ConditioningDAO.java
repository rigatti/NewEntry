package db.product;

import lombok.extern.slf4j.Slf4j;
import org.belex.product.Product.Unit;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Slf4j
public class ConditioningDAO implements IConditioningDAO {

	private final SessionFactory sessionFactory;

	public ConditioningDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	/* =========================================================
	 * READ
	 * ========================================================= */

	@Override
	@Transactional(readOnly = true)
	public List<Conditioning> getProductsByEan(String ean, String searchOnSupplier) {
		try {
			StringBuilder hql = new StringBuilder(
					"from Conditioning c where upper(c.ean) = :ean"
			);

			if (searchOnSupplier != null && !searchOnSupplier.isBlank()) {
				hql.append(" and c.supplierCode = :supplier");
			}

			var query = currentSession().createQuery(hql.toString(), Conditioning.class)
					.setParameter("ean", ean.toUpperCase());

			if (searchOnSupplier != null && !searchOnSupplier.isBlank()) {
				query.setParameter("supplier", searchOnSupplier);
			}

			return query.getResultList();

		} catch (Exception e) {
			log.error("Error while finding product conditioning by ean", e);
			return List.of();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Conditioning> getProductsByCode(
			String productCode,
			boolean searchExactMatch,
			String searchOnSupplier) {

		try {
			StringBuilder hql = new StringBuilder("from Conditioning c where ");

			if (searchExactMatch) {
				hql.append("upper(c.codeArticle) = :code");
			} else {
				hql.append("upper(c.codeArticle) like :code");
			}

			if (searchOnSupplier != null && !searchOnSupplier.isBlank()) {
				hql.append(" and c.CodeFournisseur = :supplier");
			}

			var query = currentSession().createQuery(hql.toString(), Conditioning.class);

			query.setParameter(
					"code",
					searchExactMatch
							? productCode.toUpperCase()
							: "%" + productCode.toUpperCase() + "%"
			);

			if (searchOnSupplier != null && !searchOnSupplier.isBlank()) {
				query.setParameter("supplier", searchOnSupplier);
			}

			return query.getResultList();

		} catch (Exception e) {
			log.error("Error while finding product conditioning by product code", e);
			return List.of();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Conditioning> get(String productCode, String supplierCode) {
		return get(productCode, supplierCode, null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Conditioning> get(String productCode, String supplierCode, Unit unit) {
		try {
			StringBuilder hql = new StringBuilder(
					"from Conditioning c where c.productCode = :productCode"
			);

			if (supplierCode != null) {
				hql.append(" and c.supplierCode = :supplier");
			}

			if (unit != null) {
				hql.append(" and c.unit = :unit and c.numberOfUnit = :nb");
			}

			var query = currentSession().createQuery(hql.toString(), Conditioning.class)
					.setParameter("productCode", productCode);

			if (supplierCode != null) {
				query.setParameter("supplier", supplierCode);
			}

			if (unit != null) {
				query.setParameter("unit", unit.getConditionnement());
				query.setParameter("nb", unit.getNumber());
			}

			return query.getResultList();

		} catch (Exception e) {
			log.error("Error while finding product(" + productCode + ")", e);
			return List.of();
		}
	}

	/* =========================================================
	 * WRITE
	 * ========================================================= */

	@Override
	@Transactional
	public boolean insert(Conditioning conditioning) {
		log.debug("Insert new Conditioning - " + conditioning.getProductCode());

		try {
			conditioning.setDateLastModification(String.valueOf(LocalDateTime.now()));
			currentSession().persist(conditioning);
			log.debug("Save Conditioning done");
			return true;

		} catch (Exception e) {
			log.error(
					"Error while saving Conditioning (" + conditioning.getProductCode() + ")",
					e
			);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean update(Conditioning conditioning) {
		log.debug("Update Conditioning");

		try {
			conditioning.setDateLastModification(String.valueOf(LocalDateTime.now()));
			currentSession().merge(conditioning);
			log.debug("Update Conditioning done");
			return true;

		} catch (Exception e) {
			log.error(
					"Error while updating Conditioning (" + conditioning.getProductCode() + ")",
					e
			);
			return false;
		}
	}

	/* =========================================================
	 * UTILS
	 * ========================================================= */

	@Override
	@Transactional(readOnly = true)
	public int getNextPriority(String productCode, String supplierCode) {
		try {
			Integer max = currentSession()
					.createQuery(
							"select max(c.priority) from Conditioning c " +
									"where c.productCode = :productCode and c.supplierCode = :supplier",
							Integer.class
					)
					.setParameter("productCode", productCode)
					.setParameter("supplier", supplierCode)
					.uniqueResult();

			return (max != null ? max + 1 : 1);

		} catch (Exception e) {
			log.error(
					"Error while getting MAX priority in Conditioning (" +
							supplierCode + " - " + productCode + ")",
					e
			);
			return 1;
		}
	}

	@Override
	@Transactional
	public boolean updateProductCode(String oldProductCode, String newProductCode) {
		log.debug("Update Conditioning productCode from " + oldProductCode + " to " + newProductCode);

		try {
			currentSession()
					.createQuery(
							"update Conditioning c " +
									"set c.productCode = :newCode, c.dateLastModification = :now " +
									"where c.productCode = :oldCode"
					)
					.setParameter("newCode", newProductCode)
					.setParameter("oldCode", oldProductCode)
					.setParameter("now", LocalDateTime.now())
					.executeUpdate();

			log.debug("Update Conditioning done");
			return true;

		} catch (Exception e) {
			log.error("Error while update Conditioning", e);
			return false;
		}
	}

	@Override
	@Transactional
	public boolean deleteProductCode(String productCode) {
		log.debug("Delete Conditioning productCode=" + productCode);

		try {
			currentSession()
					.createQuery(
							"delete from Conditioning c where c.productCode = :code"
					)
					.setParameter("code", productCode)
					.executeUpdate();

			log.debug("Delete Conditioning done");
			return true;

		} catch (Exception e) {
			log.error("Error while deleting Conditioning", e);
			return false;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Conditioning getUnitScaleForTopPriority(String productCode) {
		try {
			return currentSession()
					.createQuery(
							"from Conditioning c where c.productCode = :code order by c.priority asc",
							Conditioning.class
					)
					.setParameter("code", productCode)
					.setMaxResults(1)
					.uniqueResult();

		} catch (Exception e) {
			log.error("Error while finding unit scale for product(" + productCode + ")", e);
			return null;
		}
	}
}
