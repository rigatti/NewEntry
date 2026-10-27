package db.entry.treated.temp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}