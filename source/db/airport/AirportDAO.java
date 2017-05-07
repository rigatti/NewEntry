package db.airport;

import java.util.ArrayList;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class AirportDAO extends HibernateDaoSupport {

	private static final Category log = Logger.getLogger(Airport.class);

	public boolean save(Airport obj) {
		boolean result = true;
		try {
			log.debug("Prepare to save the Airport");
			
			// avec transaction
			Session session = getSession();
			Transaction transaction = session.beginTransaction();
			
			session.save(obj);
			
			transaction.commit();
			
			log.debug("Transaction submitted");
			
		} catch (Exception e) {
			log.error("Error while saving given airport", e);
			result = false;
		}
		return result;
	}
	
	public ArrayList<Airport> find(String destinationCode, String airportCode) {
		ArrayList<Airport> airports = new ArrayList<Airport>();
		
		try {
			log.debug("Finding Airports from " + destinationCode + ", " + airportCode);
			
			airports = (ArrayList<Airport>) getHibernateTemplate().find("from Airport where destinationCode=? and airportCode=?", new Object[]{destinationCode, airportCode});
			
		} catch (Exception e) {
			log.error("Error while finding airports", e);
		}
		
		return airports;
	}

}