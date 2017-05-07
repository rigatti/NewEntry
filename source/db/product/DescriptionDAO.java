package db.product;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class DescriptionDAO extends HibernateDaoSupport implements IDescriptionDAO{

	private static final Category log = Logger.getLogger(DescriptionDAO.class);
	
	public boolean insert(Description descriptoin) {
		return insert(descriptoin, null);
	}

	public boolean insert(Description description, Session session) {
		boolean result = true;

		log.debug("Insert new Descriptoin - " + description.getProductCode());

		try {
			if (session == null) {
				getHibernateTemplate().save(description);
			} else {
				session.save(description);
			}
		} catch (Exception e) {
			result = false;
			log.error("Error while saving Description (" + description.getProductCode() + ")", e);
		}

		log.debug("Save Description done");

		return result;
	}

	public boolean updateProductCode(String oldProductCode, String newProductCode, Session session) {
		boolean result = true;

		String sql = "update Description set productCode='" + newProductCode + "' where productCode='" + oldProductCode + "'"; 

		log.debug("Update Description - " + sql);

		try {
			session.createQuery(sql).executeUpdate();
		} catch (Exception e) {
			result = false;
			log.error("Error while update Description", e);
		}

		log.debug("Update Description done");

		return result;
	}

	public boolean deleteProductCode(String productCode, Session session) {
		boolean result = true;

		String sql = "delete from Description where productCode='" + productCode + "'"; 

		log.debug("Delete Description - " + sql);

		try {
			session.createQuery(sql).executeUpdate();
		} catch (Exception e) {
			result = false;
			log.error("Error while deleting Description", e);
		}

		log.debug("Delete Description done");

		return result;
	}
}