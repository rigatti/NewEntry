package db.product;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Slf4j
public class ProductDAO implements IProductDAO {

	private final SessionFactory sessionFactory;

	public ProductDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	@Transactional(readOnly = true)
	public String getDescription(String productCode) {
		try {
			log.debug("Finding product description from " + productCode);

			return currentSession()
					.createQuery(
							"select p.description from Product p where p.productCode = :code",
							String.class
					)
					.setParameter("code", productCode)
					.uniqueResult();

		} catch (Exception e) {
			log.error("Error while finding product description", e);
			return null;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Product> getProductsByDescription(String description, String searchOnSupplier) {
		try {
			log.debug("Finding products by description");

			return currentSession()
					.createQuery(
							"from Product p where upper(p.description) like :descr",
							Product.class
					)
					.setParameter("descr", "%" + description.toUpperCase() + "%")
					.getResultList();

		} catch (Exception e) {
			log.error("Error while finding products by description", e);
			return List.of();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Product getProductByCode(String code, boolean logError) {
		try {
			return currentSession()
					.createQuery(
							"from Product p where p.productCode = :code",
							Product.class
					)
					.setParameter("code", code)
					.uniqueResult();

		} catch (Exception e) {
			if (logError) {
				log.error("Error while finding product by code: " + code, e);
			}
			return null;
		}
	}

	@Transactional
	@Override
	public boolean insert(Product product) {
		try {
			currentSession().persist(product);
			return true;
		} catch (Exception e) {
			log.error("Error while inserting product: " + product.getProductCode(), e);
			return false;
		}
	}

	@Transactional
	@Override
	public boolean update(Product product) {
		try {
			currentSession().merge(product);
			return true;
		} catch (Exception e) {
			log.error("Error while updating product: " + product.getProductCode(), e);
			return false;
		}
	}

	@Transactional
	@Override
	public boolean delete(Product product) {
		try {
			currentSession().remove(product);
			return true;
		} catch (Exception e) {
			log.error("Error while deleting product: " + product.getProductCode(), e);
			return false;
		}
	}

	@Override
	public int getMaxId() {
		try {
			Integer max = currentSession()
					.createQuery("select max(p.tempId) from Product p", Integer.class)
					.uniqueResult();

			return (max != null ? max + 1 : 1);

		} catch (Exception e) {
			log.error("Error while getting MAX tempId in product", e);
			return 1;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<?> getProductsToExport(String editionType) {
		return getProductsToExport(editionType, false);
	}

	@Override
	@Transactional(readOnly = true)
	public List<?> getProductsToExport(String editionType, boolean mandatoryPlane) {
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("""
                FROM Conditioning c, Product p, EditionType t, FamilyLevel1 fl1, FamilyLevel3 fl3
                WHERE p.productCode NOT LIKE '~%'
                  AND p.productCode NOT LIKE '%TBD'
                  AND c.dateLastModification > CURRENT_DATE - 185
                  AND p.productCode = c.productCode
                  AND p.followedProduct = '0'
                  AND t.id = c.editionTypeId
                  AND t.description = :editionType
                  AND fl3.id = p.familyCode
                  AND fl3.idFamilyLevel1 = fl1.id
            """);

			if (mandatoryPlane) {
				hql.append(" AND p.AvionObligatoire != 1 ");
			}

			hql.append(" ORDER BY p.productCode ASC ");

			return currentSession()
					.createQuery(hql.toString())
					.setParameter("editionType", editionType)
					.getResultList();

		} catch (Exception e) {
			log.error("Error while finding products to export", e);
			return List.of();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<?> getProductsToExportByReference(String productReference) {
		try {
			return currentSession()
					.createQuery(
							"""
                            FROM Conditioning c, Product p, EditionType t, FamilyLevel1 fl1, FamilyLevel3 fl3
                            WHERE p.productCode NOT LIKE '~%'
                              AND p.productCode NOT LIKE '%TBD'
                              AND c.dateLastModification > CURRENT_DATE - 185
                              AND p.productCode = c.productCode
                              AND p.productCode = :ref
                              AND p.followedProduct = '0'
                              AND t.id = c.editionTypeId
                              AND fl3.id = p.familyCode
                              AND fl3.idFamilyLevel1 = fl1.id
                            """
					)
					.setParameter("ref", productReference)
					.getResultList();

		} catch (Exception e) {
			log.error("Error while finding products to export by reference", e);
			return List.of();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<?> getProductsToExport() {
		try {
			return currentSession()
					.createQuery(
							"""
                            FROM Conditioning c, Product p, EditionType t, FamilyLevel1 fl1, FamilyLevel3 fl3
                            WHERE p.productCode NOT LIKE '~%'
                              AND p.productCode NOT LIKE '%TBD'
                              AND c.dateLastModification > CURRENT_DATE - 185
                              AND p.productCode = c.productCode
                              AND p.followedProduct = '0'
                              AND t.id = c.editionTypeId
                              AND fl3.id = p.familyCode
                              AND fl3.idFamilyLevel1 = fl1.id
                            ORDER BY p.productCode ASC
                            """
					)
					.getResultList();

		} catch (Exception e) {
			log.error("Error while finding products to export", e);
			return List.of();
		}
	}
}
