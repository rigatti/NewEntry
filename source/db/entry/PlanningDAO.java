package db.entry;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class PlanningDAO extends HibernateDaoSupport implements IPlanningDAO {

	private static final Category log = Logger.getLogger(PlanningDAO.class);
	
	public List<Planning> getByDate(String date) {
		return getByDateAndSupplier(date, null);
	}

	public ArrayList<Planning> getByDateAndSupplier(String date, String supplierCode) {
		ArrayList<Planning> plannings = new ArrayList<Planning>();

		String sql = "from Planning " +
						"group by orderNumber, supplierCode, plannedDate, creationNumber, plannedMode " +
						"having plannedDate=CONVERT(smalldatetime, '" + date + "', 113) ";
						//"having plannedDate=CONVERT(smalldatetime, '" + Util.formatDate(date, "yyyyMMdd", "dd.MM.yyyy") + "', 104) ";

						if (StringUtils.isNotBlank(supplierCode)) {
							sql += " and supplierCode='" + supplierCode + "' ";
						}
						//"having plannedDate='" + Util.formatDate(date, "yyyyMMdd", "dd/MM/yyyy") + "' " +
						sql += "order by supplierCode";
		try {
			log.debug("Finding Planning");
	
			Object obj = getHibernateTemplate().find(sql);
			
			if (obj instanceof List) {

				plannings = (ArrayList<Planning>) obj;

			} else {

				log.debug("No planning found");

			}
		} catch (Exception e) {
			log.error("Error while finding Planning SQL:" + sql, e);
		}
		
		return plannings;
	}

}