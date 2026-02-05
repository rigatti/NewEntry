package db.airport;

import java.util.List;

public interface IAirportDAO {
	public boolean save(Airport airport);
	public List<Airport> find(String destinationCode, String airportCode);
}