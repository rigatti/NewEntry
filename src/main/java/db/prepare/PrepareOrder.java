package db.prepare;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PrepareOrder  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Include
	private int orderNumber;
	@EqualsAndHashCode.Include
	private String orderLetter;
	private String airportCode;
	private String flightNumber;
	private String ltaNumber;
	private int fret;
	private String flightDate;

	public PrepareOrder(){}
	

}