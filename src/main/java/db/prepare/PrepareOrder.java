package db.prepare;

public class PrepareOrder  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int orderNumber;
	private String orderLetter;
	private String airportCode;
	private String flightNumber;
	private String ltaNumber;
	private int fret;
	private String flightDate;

	public PrepareOrder(){}
	
	public String getAirportCode() {
		return airportCode;
	}

	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public int getFret() {
		return fret;
	}

	public void setFret(int fret) {
		this.fret = fret;
	}

	public String getLtaNumber() {
		return ltaNumber;
	}

	public void setLtaNumber(String ltaNumber) {
		this.ltaNumber = ltaNumber;
	}

	public String getOrderLetter() {
		return orderLetter;
	}

	public void setOrderLetter(String orderLetter) {
		this.orderLetter = orderLetter;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(String flightDate) {
		this.flightDate = flightDate;
	}
}