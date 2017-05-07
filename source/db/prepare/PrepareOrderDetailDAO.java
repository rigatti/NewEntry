package db.prepare;

import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class PrepareOrderDetailDAO extends HibernateDaoSupport implements IPrepareOrderDetailDAO {

	private static final Category log = Logger.getLogger(PrepareOrderDetailDAO.class);
	
	public String getCustomerCode(int orderNumber, String orderLetter,  
									String customerOrderCode, int customerOrderCodeNumber, int feesType) {

		String customerCode = "";

		try {
			log.debug("Finding Order");

			String sql = "from PrepareOrderDetail where" +
							" orderNumber=" + orderNumber + 
							" and orderLetter='" + orderLetter + "'" +
							" and customerOrderCode='" + customerOrderCode + "'" +
							" and customerOrderCodeNumber=" + customerOrderCodeNumber +
							" and feesType=" + feesType;
			
			Object obj = getHibernateTemplate().find(sql);
			
			if (obj instanceof List) {

				List<PrepareOrderDetail> orderDetails = (List<PrepareOrderDetail>) obj;
				if (orderDetails.size() > 0) {
					// List size should be 1
					if (orderDetails.size() == 1) {
						customerCode = orderDetails.get(0).getCustomerCode();
					} else {
						log.error("List of order detail is bigger than 1 : " + sql);
					}
				} else {
					log.debug("No order detail found");
				}
			} else {
				log.debug("No order detail found");
			}
		} catch (Exception e) {
			log.error("Error while finding Order Detail", e);
		}
		
		return customerCode;
	}

}