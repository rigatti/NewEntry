package db.prepare;

import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class PrepareOrderDAO extends HibernateDaoSupport implements IPrepareOrderDAO {

	private static final Category log = Logger.getLogger(PrepareOrderDAO.class);
	
	public PrepareOrder get(int orderNumber, String orderLetter) {

		PrepareOrder order = new PrepareOrder();

		try {
			log.debug("Finding Order");

			String sql = "from PrepareOrder where" +
							" orderNumber=" + orderNumber + 
							" and orderLetter='" + orderLetter + "'";
			
			Object obj = getHibernateTemplate().find(sql);
			
			if (obj instanceof List) {

				List<PrepareOrder> listOfOrder = (List<PrepareOrder>) obj;
				if (listOfOrder.size() > 0) {
					// List size should be 1
					if (listOfOrder.size() == 1) {
						order = listOfOrder.get(0);
					} else {
						log.error("List of order is bigger than 1 : " + sql);
					}
				} else {
					log.debug("No order found");
				}
			} else {
				log.debug("No order found");
			}
		} catch (Exception e) {
			log.error("Error while finding Order", e);
		}
		
		return order;
	}

}