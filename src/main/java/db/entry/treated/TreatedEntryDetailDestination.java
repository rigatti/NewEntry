package db.entry.treated;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreatedEntryDetailDestination  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private int treatedEntryDetailId;
	private String allocationDate= "";

	private int stockFlag;
	private int supplierReturnsFlag;

	private String customerCode = "";
	private int numberOfUnit;
	private int packagingFromBasket;
	private int packagingToBasket;
	private String airportCode = "";
	private String flight = "";
	private String lta = "";

	private String substitutionOfProduct = "";
	private String substitutionOfProductDescr = "";
	private String substitutionOfUnitConditionnement = "";
	private int substitutionOfNumberOfUnit;
	private int substitutionOfNumberOfProduct;
	private String substitutionOfCustomerOrderCode = "";
	private int substitutionOfCustomerOrderNumber;
	private String substitutionOfSupplierCode = "";
	private String substitutionOfSupplierOrderLetter = "";
	private int substitutionOfSupplierOrderNumber;

	
	public TreatedEntryDetailDestination() {}

}