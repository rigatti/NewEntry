package db.airport;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Airport implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String destinationCode;
    private String airportCode;
    private String description;

    public Airport() {
    }

    public Airport(String destinationCode, String airportCode) {
        this.destinationCode = destinationCode;
        this.airportCode = airportCode;
    }
}