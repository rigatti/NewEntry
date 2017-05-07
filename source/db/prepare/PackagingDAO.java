package db.prepare;

import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class PackagingDAO extends HibernateDaoSupport implements IPackagingDAO{

	private static final Category log = Logger.getLogger(PackagingDAO.class);

	public Packaging get(int orderNumber, String orderLetter, String customerOrderCode, int customerOrderCodeNumber) {
		return get(orderNumber, orderLetter, customerOrderCode, customerOrderCodeNumber, -1);
	}
	
	public Packaging get(int orderNumber, String orderLetter, String customerOrderCode, int customerOrderCodeNumber, int feesType) {

		Packaging packaging = new Packaging();

		try {
			log.debug("Finding Packaging");

			String sql = "from Packaging where" +
							" orderNumber=" + orderNumber + 
							" and orderLetter='" + orderLetter + "'" +
							" and customerOrderCode='" + customerOrderCode + "'" + 
							" and customerOrderCodeNumber=" + customerOrderCodeNumber;
			if (feesType > 0) {
				sql += " and feesType=" + feesType;
			}
			
			Object obj = getHibernateTemplate().find(sql);
			
			if (obj instanceof List) {

				List<Packaging> listOfPackaging = (List<Packaging>) obj;
				
				if (listOfPackaging.size() > 0) {

					if (listOfPackaging.size() == 1) {
						packaging = listOfPackaging.get(0);
					} else {
						log.error("List of packaging is bigger than 1 NEED TO ADD FEESTYPE IN REQUEST but feestype = " + feesType +  " : " + sql);
						packaging = listOfPackaging.get(0);
					}
				} else {
					log.debug("No packaging found" + sql);
				}
			} else {
				log.debug("No packaging found" + sql);
			}
		} catch (Exception e) {
			log.error("Error while finding packaging", e);
		}
		
		return packaging;
	}

}