package db.airport;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class AirportDAO implements IAirportDAO{

	private final SessionFactory sessionFactory;

	public AirportDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Transactional
	@Override
	public boolean save(Airport airport) {
		boolean result = true;
		try {
			log.debug("Prepare to save the Airport");
			
			currentSession().save(airport);
			
		} catch (Exception e) {
			log.error("Error while saving given airport", e);
			result = false;
		}
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Airport> find(String destinationCode, String airportCode) {
		List<Airport> airports = new ArrayList<Airport>();
		
		try {
			log.debug("Finding Airports from " + destinationCode + ", " + airportCode);

			StringBuilder hql = new StringBuilder("from Airport where destinationCode=:destinationCode and airportCode=:airportCode");

			var query = currentSession().createQuery(hql.toString(), Airport.class);

			query.setParameter("destinationCode", destinationCode);
			query.setParameter("airportCode", airportCode);

			return query.getResultList();

		} catch (Exception e) {
			log.error("Error while finding airports", e);
		}

		return airports;
	}

}