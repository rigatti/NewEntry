package db.prepare;

import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class PackagingDetailDAO extends HibernateDaoSupport implements IPackagingDetailDAO{

	private static final Category log = Logger.getLogger(PackagingDetailDAO.class);
	
	public PackagingDetail get(int orderNumber, String orderLetter, String customerOrderCode, int customerOrderCodeNumber, int feesType, int fromPackageNumber) {
		PackagingDetail packagingDetail = new PackagingDetail();

		try {
			log.debug("Finding PackagingDetail");

			String sql = "from PackagingDetail where ";
			
			sql += "orderNumber=" + orderNumber + " and ";
			sql += "orderLetter='" + orderLetter + "' and ";
			sql += "customerOrderCode='" + customerOrderCode + "' and ";
			sql += "customerOrderCodeNumber=" + customerOrderCodeNumber + " and ";
			sql += "feesType=" + feesType + " and ";
			sql += "fromPackageNumber=" + fromPackageNumber;
			
			Object obj = getHibernateTemplate().find(sql);
			
			if (obj instanceof List) {

				List<PackagingDetail> listOfPackagingDetail = (List<PackagingDetail>) obj;
				if (listOfPackagingDetail.size() > 0) {
					// List size should be 1
					if (listOfPackagingDetail.size() == 1) {
						packagingDetail = listOfPackagingDetail.get(0);
					} else {
						log.error("List of packagingDetail is bigger than 1 : " + sql);
					}
				} else {
					log.debug("No packagingDetail found");
				}
			} else {
				log.debug("No packagingDetail found");
			}
		} catch (Exception e) {
			log.error("Error while finding packagingDetail", e);
		}
		
		return packagingDetail;
	}

}