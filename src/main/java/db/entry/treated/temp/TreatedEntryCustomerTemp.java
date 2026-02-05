package db.entry.treated.temp;

public class TreatedEntryCustomerTemp  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int treatedEntryTempId;
	private String customerCode;
	private String customerOrderCode;
	private int customerOrderCodeNumber;
	private int packagingFromBasket;
	private int packagingToBasket;
	private int customerNumberOfUnitRequested;
	private String airportCode;
	private String flight;
	private String lta;

	public TreatedEntryCustomerTemp() {}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public int getCustomerNumberOfUnitRequested() {
		return customerNumberOfUnitRequested;
	}

	public void setCustomerNumberOfUnitRequested(int customerNumberOfUnitRequested) {
		this.customerNumberOfUnitRequested = customerNumberOfUnitRequested;
	}

	public String getCustomerOrderCode() {
		return customerOrderCode;
	}

	public void setCustomerOrderCode(String customerOrderCode) {
		this.customerOrderCode = customerOrderCode;
	}

	public int getCustomerOrderCodeNumber() {
		return customerOrderCodeNumber;
	}

	public void setCustomerOrderCodeNumber(int customerOrderCodeNumber) {
		this.customerOrderCodeNumber = customerOrderCodeNumber;
	}

	public int getTreatedEntryTempId() {
		return treatedEntryTempId;
	}

	public void setTreatedEntryTempId(int treatedEntryTempId) {
		this.treatedEntryTempId = treatedEntryTempId;
	}

	public int getPackagingFromBasket() {
		return packagingFromBasket;
	}

	public void setPackagingFromBasket(int packagingFromBasket) {
		this.packagingFromBasket = packagingFromBasket;
	}

	public int getPackagingToBasket() {
		return packagingToBasket;
	}

	public void setPackagingToBasket(int packagingToBasket) {
		this.packagingToBasket = packagingToBasket;
	}

	public String getAirportCode() {
		return airportCode;
	}

	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	public String getFlight() {
		return flight;
	}

	public void setFlight(String flight) {
		this.flight = flight;
	}

	public String getLta() {
		return lta;
	}

	public void setLta(String lta) {
		this.lta = lta;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}