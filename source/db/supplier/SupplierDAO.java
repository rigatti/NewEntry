package db.supplier;

import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SupplierDAO extends HibernateDaoSupport implements ISupplierDAO {

	private static final Category log = Logger.getLogger(SupplierDAO.class);
	
	public List<Supplier> getAll() {
		List<Supplier> suppliers = null;
		try {
			log.debug("Finding all suppliers");
			
			Object obj = getHibernateTemplate().find("from Supplier ORDER BY DescriptionFournisseur ASC");
			
			if (obj instanceof List) {

				suppliers = (List<Supplier>) obj;
				
			}
		} catch (Exception e) {
			log.error("Error while finding suppliers", e);
		}

		return suppliers;
	}

	public Supplier getByCode(String supplierCode) {
		Supplier supplier = new Supplier(supplierCode);
		
		try {
			log.debug("Finding Suppliers from " + supplierCode);
			
			String sql = "from Supplier where supplierCode='" + supplierCode + "'";
			//Object obj = getSession().createQuery("from Supplier where supplierCode='" + supplierCode + "'").list();
			Object obj = getHibernateTemplate().find(sql);
			
			if (obj instanceof List) {

				List<Supplier> listOfSupplier = (List<Supplier>) obj;
				
				// List size should be 1
				if (listOfSupplier.size() == 1) {
					supplier = listOfSupplier.get(0);
				} else {
					log.error("List of Suppliers is bigger than 1 : " + sql);
				}
			} else {
				log.debug("No supplier found for the code:" + supplierCode);
			}
		} catch (Exception e) {
			log.error("Error while finding suppliers", e);
		}
		
		return supplier;
	}

}