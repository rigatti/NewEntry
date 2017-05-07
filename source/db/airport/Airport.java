package db.airport;

public class Airport implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String destinationCode;
    private String airportCode;
    private String description;

    public Airport() {
    }

    /** full constructor */
    public Airport(String destinationCode, String airportCode) {
        this.destinationCode = destinationCode;
        this.airportCode = airportCode;
    }


	public String getdestinationCode() {
		return destinationCode;
	}


	public void setdestinationCode(String destinationCode) {
		this.destinationCode = destinationCode;
	}


	public String getairportCode() {
		return airportCode;
	}


	public void setairportCode(String airportCode) {
		this.airportCode = airportCode;
	}


	public String getdescription() {
		return description;
	}


	public void setdescription(String description) {
		this.description = description;
	}
}