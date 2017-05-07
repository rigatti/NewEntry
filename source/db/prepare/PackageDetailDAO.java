package db.prepare;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class PackageDetailDAO extends HibernateDaoSupport implements IPackageDetailDAO{

	private static final Category log = Logger.getLogger(PackageDetailDAO.class);
	
	public ArrayList<PackageDetail> get(String productCode, StringTokenizer orderNumbers) {

		ArrayList<PackageDetail> packageDetails = new ArrayList<PackageDetail>();

		String sql = "from PackageDetail where ";

		if (orderNumbers.countTokens() > 0) {
			sql += "(";
		}

		int index = 0;
		while (orderNumbers.hasMoreElements()) {
			
			if (index != 0) {
				sql += " or ";
			}
			sql += "orderNumber=" + orderNumbers.nextElement();
			index++; 
		}
		if (index > 0) {
			sql += ")";
		}
		sql += " and productCode='" + productCode + "'";

		try {
			log.debug("Finding PackageDetail");

			Object obj = getHibernateTemplate().find(sql);
			
			if (obj instanceof List) {

				packageDetails = (ArrayList<PackageDetail>) obj;
				
				if (packageDetails.size() == 0) {
					log.debug("No packageDetail found:" + sql);
				}

			} else {
				log.debug("No packageDetail found" + sql);
			}
		} catch (Exception e) {
			log.error("Error while finding PackageDetail " + sql, e);
		}
		
		return packageDetails;
	}

}