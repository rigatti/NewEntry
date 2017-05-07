package db.customer.order;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CustomerOrderDetailDAO extends HibernateDaoSupport implements ICustomerOrderDetailDAO {

	private static final Category log = Logger.getLogger(CustomerOrderDetailDAO.class);
	
	public ArrayList<CustomerOrderDetail> get(Set<Integer> orderNumbers) {
		ArrayList<CustomerOrderDetail> cods = new ArrayList<CustomerOrderDetail>();

		try {
			log.debug("Finding CustomerOrderDetail by orderNumbers:" + orderNumbers);

			String sql = "from CustomerOrderDetail where (";

			int index = 0;
			Iterator<Integer> it = orderNumbers.iterator();
			while (it.hasNext()) {

				if (index != 0) {
					sql += " or ";
				}
				sql += "orderNumber=" + it.next();
				index++; 
			}

			sql += ")";

			Object obj = getHibernateTemplate().find(sql);
			
			if (obj instanceof ArrayList) {

				cods = (ArrayList<CustomerOrderDetail>) obj;
				
			} else {
				log.debug("No customerOrderDetail found");
			}
		} catch (Exception e) {
			log.error("Error while finding CustomerOrderDetail", e);
		}
		
		return cods;
	}
	
	public ArrayList<CustomerOrderDetail> getByProductCode(String productCode, StringTokenizer orderNumbers) {
		ArrayList<CustomerOrderDetail> sods = new ArrayList<CustomerOrderDetail>();

		try {
			log.debug("Finding CustomerOrderDetail:" + productCode);

			String sql = "from CustomerOrderDetail where (";
			
			int index = 0;
			while (orderNumbers.hasMoreElements()) {
				
				if (index != 0) {
					sql += " or ";
				}
				sql += "orderNumber=" + orderNumbers.nextElement();
				index++; 
			}

			sql += ") and productCode='" + productCode + "'";

			//Object obj = getSession().createQuery(sql).list();
			Object obj = getHibernateTemplate().find(sql);
			
			if (obj instanceof ArrayList) {

				sods = (ArrayList<CustomerOrderDetail>) obj;
				
			} else {
				log.debug("No customerOrderDetail found");
			}
			log.debug("Finding CustomerOrderDetail DONE");
		} catch (Exception e) {
			log.error("Error while finding CustomerOrderDetail", e);
		}
		
		return sods;
	}

	public ArrayList<String> getLettersByOrderNumber(int orderNumber) {
		ArrayList<String> orderLetters = new ArrayList<String>();

		try {
			log.debug("Finding CustomerOrderDetail:" + orderNumber);

			String sql = "select distinct orderLetter from CustomerOrderDetail where orderNumber=" + orderNumber;

			Object obj = getHibernateTemplate().find(sql);
			
			if (obj instanceof ArrayList) {

				orderLetters = (ArrayList<String>) obj;

			} else {
				log.debug("No customerOrderDetail found");
			}
			log.debug("Finding CustomerOrderDetail DONE");
		} catch (Exception e) {
			log.error("Error while finding CustomerOrderDetail", e);
		}

		return orderLetters;
		
	}
/*
	public ArrayList<CustomerOrderDetail> getBySupplierCode(String supplierCode, Set<Integer> orderNumbers) {
		ArrayList<CustomerOrderDetail> cods = new ArrayList<CustomerOrderDetail>();

		try {
			log.debug("Finding CustomerOrderDetail by SupplierCode:" + supplierCode);

			String sql = "from CustomerOrderDetail where (";
			
			int index = 0;
			Iterator<Integer> it = orderNumbers.iterator();
			while (it.hasNext()) {
				
				if (index != 0) {
					sql += " or ";
				}
				sql += "orderNumber=" + it.next();
				index++; 
			}
			
			sql += ") and supplierCode='" + supplierCode + "'";

			Object obj = getHibernateTemplate().find(sql);
			
			if (obj instanceof ArrayList) {

				cods = (ArrayList<CustomerOrderDetail>) obj;
				
			} else {
				log.debug("No customerOrderDetail found");
			}
		} catch (Exception e) {
			log.error("Error while finding CustomerOrderDetail", e);
		}
		
		return cods;
	}
*/
	public ArrayList<CustomerOrderDetail> get(String productCode, String supplierOrderNumbers, String supplierOrderLetters, String customerCodeOrder, int customerCodeOrderNumber, Session session) {
		ArrayList<CustomerOrderDetail> cods = new ArrayList<CustomerOrderDetail>();

		try {
			log.debug("Finding CustomerOrderDetail:" + productCode);

			String sql = "from CustomerOrderDetail where " +
							"productCode='" + productCode + "' and " +
							"orderNumber='" + supplierOrderNumbers + "' and " +
							"orderLetter='" + supplierOrderLetters + "' and " +
							"customerOrderCode='" + customerCodeOrder + "' and " +
							"customerOrderCodeNumber=" + customerCodeOrderNumber;

			Object obj = session.createQuery(sql).list();
			
			if (obj != null && obj instanceof ArrayList) {

				cods = (ArrayList<CustomerOrderDetail>) obj;
				
			} else {
				log.debug("No customerOrderDetail found");
			}
		} catch (Exception e) {
			log.error("Error while finding CustomerOrderDetail:" + productCode + ", " + supplierOrderNumbers + ", " +supplierOrderLetters + ", " + customerCodeOrder + ", " + customerCodeOrderNumber, e);
		}
		
		return cods;
//	sql = "select * from CommandesLignes where CodeArticle='" + productCode + "' " +
//			" and NuméroCommande='" + supplierCommandeNumber + "' " +
//			" and CodeClientCommande='" + client.getCodeCommande()  + "' " +
//			" and NuméroCodeClientCommande='" + client.getCodeCommandeNumber() + "';";
	}
	
	public boolean update(CustomerOrderDetail sod, Session session) {
		boolean result = true;
		
		log.debug("Update CustomerOrderDetail");
		
		try {
			session.update(sod);	
		} catch (Exception e) {
			result = false;
			log.error("Error while updating CustomerOrderDetail (" + sod.getId() + ")", e);
		}

		log.debug("update CustomerOrderDetail done");

		return result;
	}
	public ArrayList<CustomerOrderDetail> get(int orderNumber, String orderLetter) {
		ArrayList<CustomerOrderDetail> cods = new ArrayList<CustomerOrderDetail>();

		try {
			log.debug("Finding CustomerOrderDetail by number:" + orderNumber + " and letter:" + orderLetter);

			String sql = "from CustomerOrderDetail where" +
					" orderNumber=" + orderNumber;

			if (orderLetter != null) {
				sql += " and orderLetter='" + orderLetter + "'";
			}

			Object obj = getHibernateTemplate().find(sql);
			
			if (obj instanceof ArrayList) {

				cods = (ArrayList<CustomerOrderDetail>) obj;
				
			} else {
				log.debug("No customerOrderDetail found");
			}
		} catch (Exception e) {
			log.error("Error while finding CustomerOrderDetail", e);
		}
		
		return cods;
	}
}