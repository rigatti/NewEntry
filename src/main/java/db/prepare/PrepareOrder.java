package db.prepare;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
	

}