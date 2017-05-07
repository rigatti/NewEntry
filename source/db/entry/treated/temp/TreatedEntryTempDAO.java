package db.entry.treated.temp;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class TreatedEntryTempDAO extends HibernateDaoSupport implements ITreatedEntryTempDAO {

	private static final Category log = Logger.getLogger(TreatedEntryTempDAO.class);

	public ArrayList<TreatedEntryTemp> getBySupplierCode(String supplierCode) {
		return getBySupplierCodeAndDate(supplierCode, null, null);
	}
	
	public ArrayList<TreatedEntryTemp> getBySupplierCode(String supplierCode, Session session) {
		return getBySupplierCodeAndDate(supplierCode, null, session);
	}
	
	public ArrayList<TreatedEntryTemp> getBySupplierCodeAndDate(String supplierCode, String date) {
		return getBySupplierCodeAndDate(supplierCode, date, null);
	}

	public ArrayList<TreatedEntryTemp> getBySupplierCodeAndDate(String supplierCode, String date, Session session) {
		ArrayList<TreatedEntryTemp> treatedEntryTemps = new ArrayList<TreatedEntryTemp>();
		
		try {
			log.debug("Finding TreatedEntryTemp:" + supplierCode);

			String sql = "from TreatedEntryTemp where supplierCode='" + supplierCode + "'";
			if (date != null) {
				sql += " and arrivalDate='" + date + "'";
			}
			
			Object obj = null;
			if (session == null) {
				obj = getHibernateTemplate().find(sql);
			} else {
				obj = session.createQuery(sql).list();
			}

			if (obj instanceof List) {

				treatedEntryTemps = (ArrayList<TreatedEntryTemp>) obj;

			} else {
				log.debug("No treatedEntryTemp found");
			}
		} catch (Exception e) {
			log.error("Error while finding TreatedEntryTemp:" + supplierCode , e);
		}
		
		return treatedEntryTemps;
		
	}
	
	private TreatedEntryTemp getById(int id, Session session) {
		TreatedEntryTemp treatedEntryTemp = null;
		
		try {
			log.debug("Finding TreatedEntryTemp:" + id);

			String sql = "from TreatedEntryTemp where ID=" + id ;
			
			Object obj = null;
			if (session == null) {
				obj = getHibernateTemplate().find(sql);
			} else {
				obj = session.createQuery(sql).list();
			}

			if (obj instanceof List) {

				treatedEntryTemp = ((List<TreatedEntryTemp>) obj).get(0);

			} else {
				log.debug("No treatedEntryTemp found");
			}
		} catch (Exception e) {
			log.error("Error while finding TreatedEntryTemp:" + id , e);
		}
		
		return treatedEntryTemp;
		
	}

	public ArrayList<TreatedEntryTemp> get(String supplierCode, String date, String productCode, Session session) {
		ArrayList<TreatedEntryTemp> treatedEntryTemps = new ArrayList<TreatedEntryTemp>();
		
		try {
			log.debug("Finding TreatedEntryTemp:" + supplierCode);

			String sql = "from TreatedEntryTemp where supplierCode='" + supplierCode + "'";
			
			if (date != null) {
				sql += " and arrivalDate='" + date + "'";
			}

			if (productCode != null) {
				sql += " and productCode='" + productCode + "'";
			}

			Object obj = null;
			if (session == null) {
				obj = getHibernateTemplate().find(sql);
			} else {
				obj = session.createQuery(sql).list();
			}

			if (obj instanceof List) {

				treatedEntryTemps = (ArrayList<TreatedEntryTemp>) obj;

			} else {
				log.debug("No treatedEntryTemp found");
			}
		} catch (Exception e) {
			log.error("Error while finding TreatedEntryTemp:" + supplierCode , e);
		}
		
		return treatedEntryTemps;
		
	}

	public TreatedEntryTemp getByProductCodeAndUnit(String productCode, int numberOfUnit, String unit) {
		return getBySupplierCodeProductCodeAndUnit(null, productCode, numberOfUnit, unit);
	}
	public TreatedEntryTemp getBySupplierCodeProductCodeAndUnit(String supplierCode, String productCode, int numberOfUnit, String unit) {
		return getByDateSupplierCodeProductCodeAndUnit(null, null, productCode, numberOfUnit, unit);
	}
	public TreatedEntryTemp getByDateSupplierCodeProductCodeAndUnit(String date, String supplierCode, String productCode, int numberOfUnit, String unit) {
		TreatedEntryTemp treatedEntryTemp = new TreatedEntryTemp();

		try {
			log.debug("Finding TreatedEntryTemp");

			String sql = "from TreatedEntryTemp where " +
									"productCode='" + productCode + "'" +
									" and numberOfUnit=" + numberOfUnit + 
									" and unitConditionnement='" + unit + "'";
			if (date != null) {
				sql += "and arrivalDate='" + date + "'";
			}
			if (supplierCode != null) {
				sql += "and supplierCode='" + supplierCode + "'";
			}

			Object obj = getHibernateTemplate().find(sql);
			
			if (obj instanceof List) {

				List<TreatedEntryTemp> listOfTreatedEntryTemp = (List<TreatedEntryTemp>) obj;
				
				if (listOfTreatedEntryTemp.size() > 0) {
					// List size should be 1
					if (listOfTreatedEntryTemp.size() == 1) {
						treatedEntryTemp = listOfTreatedEntryTemp.get(0);
					} else {
						treatedEntryTemp = null;
						log.error("List of TreatedEntryTemp is bigger than 1 : " + sql);
					}
				} else {
					treatedEntryTemp = null;
					log.debug("No treatedEntryTemp found:" + productCode);
				}
			} else {
				treatedEntryTemp = null;
				log.debug("No treatedEntryTemp found" + productCode);
			}
			log.debug("Finding TreatedEntryTemp DONE");
		} catch (Exception e) {
			log.error("Error while finding TreatedEntryTemp:" + productCode, e);
		}
		
		return treatedEntryTemp;
	}
	
	public boolean save(TreatedEntryTemp treatedEntryTemp, Session session) {
		boolean result = true;

		try {
			log.debug("Saving treatedEntryTemp");
			session.save(treatedEntryTemp);
			log.debug("Saving treatedEntryTemp DONE");
		} catch (Exception e) {
			log.error("Error while saving treatedEntryTemp: " + treatedEntryTemp.getProductCode(), e);
			 result = false;
		}
		
		return result;
	}

	public boolean update(TreatedEntryTemp treatedEntryTemp) {
		boolean result = true;
		
		try {
			log.info("Update treatedEntryTemp:" + treatedEntryTemp.getId() + " product:" + treatedEntryTemp.getProductCode() + " nbr:" + treatedEntryTemp.getNumberOfProduct());
			getHibernateTemplate().update(treatedEntryTemp);
			log.info("Saving treatedEntryTemp DONE");
		} catch (Exception e) {
			log.error("Error while updating treatedEntryTemp: " + treatedEntryTemp.getProductCode(), e);
			 result = false;
		}
		//getHibernateTemplate().update(treatedEntryTemp);
		
		return result;
		
		//sql = "update TreatedEntryTemp set numberOfProduct='" + (storedNumberOfProduct + newNumberOfProduct) + "', " +
		//								  "numberOfProductToTransfer='" + nbrToTransfer + "', " +
		//								  "numberOfProductReceived='" + (nbrReceived + storedNbrReceived) + "', " +
		//								  "ean='" + unit.getEan() + "'" +
		//
		//			"where productCode='" + arrival.getEntry().getProduct().getProductCode() + "' " +
		//				  "and numberOfUnit='" + unit.getNumber() + "' " +
		//				  "and unitConditionnement='" + unit.getConditionnement() + "' ";

	}

	public boolean delete(TreatedEntryTemp treatedEntryTemp, Session session) {
		boolean result = true;

		try {

			log.info("Delete treatedEntryTemp:" + treatedEntryTemp.getId());
			session.delete(treatedEntryTemp);
			log.info("Delete treatedEntryTemp DONE");

		} catch (Exception e) {
			log.error("Error while deleting treatedEntryTemp: " + treatedEntryTemp.getProductCode(), e);
			 result = false;
		}
		
		return result;
	}

	public boolean deleteAll(String supplierCode, String arrivalDate, Session session, ITreatedEntryCustomerTempDAO tecDAO) {
		boolean result = true;
		try {
			log.debug("DeleteAll treatedEntryTemp supplier:" + supplierCode + "for a date:" + arrivalDate);
			ArrayList<TreatedEntryTemp> treatedEntryTemps = getBySupplierCodeAndDate(supplierCode, arrivalDate);
			for (TreatedEntryTemp tet : treatedEntryTemps) {
				tecDAO.deleteAllByEntryId(tet.getId(), session);
				if (session == null) {
					getHibernateTemplate().delete(tet);
				} else {
					session.delete(tet);
				}
			}
			log.debug("DeleteAll treatedEntryTemp supplier:" + supplierCode + "for a date:" + arrivalDate + " DONE");
		} catch (Exception e) {
			log.error("Error while deleting treatedEntryTemp", e);
		}
		return result;
	}
	
//	 remove all entries from db without corresponding foreign keys
	public boolean cleanDb(Session session, ITreatedEntryCustomerTempDAO treatedEntryCustomerTempDAO) {
		boolean result = true;
		try {
			log.debug("Clean DB - START");
			
			ArrayList<TreatedEntryCustomerTemp> tects = treatedEntryCustomerTempDAO.getAll();
			
			for (TreatedEntryCustomerTemp tect : tects) {
				TreatedEntryTemp tet = getById(tect.getTreatedEntryTempId(), session);
				if (tet == null) {
					treatedEntryCustomerTempDAO.deleteAllByEntryId(tect.getTreatedEntryTempId(), session);
				}
			}
			log.debug("Clean DB - END");
		} catch (Exception e) {
			log.error("Error while deleting treatedEntryTemp", e);
		}
		return result;
	}
/*
	public boolean deleteEntryIdBySupplier(String supplierCode, int entryId) {
		boolean result = true;
		try {
			log.debug("Delete treatedEntryTemp id(" + entryId + ") by supplier:" + supplierCode);
			ArrayList<TreatedEntryTemp> treatedEntryTemps = getBySupplierCode(supplierCode);
			if (treatedEntryTemps.size() > entryId) {
				getHibernateTemplate().delete(treatedEntryTemps.get(entryId));
			}
			log.debug("Delete treatedEntryTemp id(" + entryId + ") by supplier:" + supplierCode + " DONE");
		} catch (Exception e) {
			log.error("Error while deleting treatedEntryTemp", e);
		}
		return result;
	}
*/
/*
	public boolean deleteAllBySupplier(String supplierCode, Session session, ITreatedEntryCustomerTempDAO tecDAO) {
		boolean result = true;

		ArrayList<TreatedEntryTemp> treatedEntryTemps = getBySupplierCode(supplierCode, session);
		
		for (TreatedEntryTemp tet : treatedEntryTemps) {
			try {
				log.debug("deleteAllBySupplier:" + supplierCode);

				tecDAO.deleteAllByEntryId(tet.getId(), session);

				if (session == null) {
					getHibernateTemplate().delete(tet);
				} else {
					session.delete(tet);
				}

				log.debug("deleteAllBySupplier DONE");
			} catch (Exception e) {
				log.error("While deleteAllBySupplier (" +  supplierCode + "):", e);
				result = false;
			}
		}

		if (treatedEntryTemps.size() == 0) {
			log.debug("No TreatedEntryTemps founded for supplier:" + supplierCode);
		}

		return result;
	}
*/
	public boolean updateProductCode(String oldProductCode, String newProductCode, Session session) {
		boolean result = true;

		String sql = "update TreatedEntryTemp set productCode='" + newProductCode + "' where productCode='" + oldProductCode + "'"; 

		log.debug("Update treated entries temp - " + sql);

		try {
			session.createQuery(sql).executeUpdate();
		} catch (Exception e) {
			result = false;
			log.error("Error while update treated entries temp", e);
		}

		log.debug("Update treated entries temp done");

		return result;
		
	}
}