package db.supplier.order;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SupplierOrderDetailDAO extends HibernateDaoSupport implements ISupplierOrderDetailDAO {

	private static final Category log = Logger.getLogger(SupplierOrderDetailDAO.class);

	public ArrayList<SupplierOrderDetail> get(String supplierCode, int orderNumber, String productCode) {
		ArrayList<SupplierOrderDetail> sods = new ArrayList<SupplierOrderDetail>();

		try {
			log.debug("Finding SupplierOrderDetail by SupplierCode:" + supplierCode + " number:" + orderNumber + " productCode:" + productCode);

			String sql = "from SupplierOrderDetail where" +
					" orderNumber=" + orderNumber + " and";
			
			if (StringUtils.equals(supplierCode, "01")) {
				sql += " (supplierCode='01' or supplierCode='')";
			} else {
				sql += " supplierCode='" + supplierCode + "'";
			}

			if (StringUtils.isNotEmpty(productCode)) {
				sql += " and productCode='" + productCode + "'";
			}

			Object obj = getHibernateTemplate().find(sql);
			
			if (obj instanceof ArrayList) {

				sods = (ArrayList<SupplierOrderDetail>) obj;
				
			} else {
				log.debug("No supplierOrderDetail found");
			}
		} catch (Exception e) {
			log.error("Error while finding SupplierOrderDetail", e);
		}
		
		return sods;
	}

	public ArrayList<SupplierOrderDetail> get(String supplierCode, int orderNumber) {
		return get(supplierCode, orderNumber, null);
	}
}