package db.entry.treated;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLta() {
		return lta;
	}

	public void setLta(String lta) {
		this.lta = lta;
	}

	public int getTreatedEntryDetailId() {
		return treatedEntryDetailId;
	}

	public void setTreatedEntryDetailId(int treatedEntryDetailId) {
		this.treatedEntryDetailId = treatedEntryDetailId;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public int getNumberOfUnit() {
		return numberOfUnit;
	}

	public void setNumberOfUnit(int numberOfUnit) {
		this.numberOfUnit = numberOfUnit;
	}

	public String getSubstitutionOfCustomerOrderCode() {
		return substitutionOfCustomerOrderCode;
	}

	public void setSubstitutionOfCustomerOrderCode(
			String substitutionOfCustomerOrderCode) {
		this.substitutionOfCustomerOrderCode = substitutionOfCustomerOrderCode;
	}

	public int getSubstitutionOfCustomerOrderNumber() {
		return substitutionOfCustomerOrderNumber;
	}

	public void setSubstitutionOfCustomerOrderNumber(
			int substitutionOfCustomerOrderNumber) {
		this.substitutionOfCustomerOrderNumber = substitutionOfCustomerOrderNumber;
	}

	public int getSubstitutionOfNumberOfProduct() {
		return substitutionOfNumberOfProduct;
	}

	public void setSubstitutionOfNumberOfProduct(int substitutionOfNumberOfProduct) {
		this.substitutionOfNumberOfProduct = substitutionOfNumberOfProduct;
	}

	public int getSubstitutionOfNumberOfUnit() {
		return substitutionOfNumberOfUnit;
	}

	public void setSubstitutionOfNumberOfUnit(int substitutionOfNumberOfUnit) {
		this.substitutionOfNumberOfUnit = substitutionOfNumberOfUnit;
	}

	public String getSubstitutionOfProduct() {
		return substitutionOfProduct;
	}

	public void setSubstitutionOfProduct(String substitutionOfProduct) {
		this.substitutionOfProduct = substitutionOfProduct;
	}

	public String getSubstitutionOfProductDescr() {
		return substitutionOfProductDescr;
	}

	public void setSubstitutionOfProductDescr(String substitutionOfProductDescr) {
		this.substitutionOfProductDescr = substitutionOfProductDescr;
	}

	public String getSubstitutionOfSupplierCode() {
		return substitutionOfSupplierCode;
	}

	public void setSubstitutionOfSupplierCode(String substitutionOfSupplierCode) {
		this.substitutionOfSupplierCode = substitutionOfSupplierCode;
	}

	public String getSubstitutionOfSupplierOrderLetter() {
		return substitutionOfSupplierOrderLetter;
	}

	public void setSubstitutionOfSupplierOrderLetter(
			String substitutionOfSupplierOrderLetter) {
		this.substitutionOfSupplierOrderLetter = substitutionOfSupplierOrderLetter;
	}

	public int getSubstitutionOfSupplierOrderNumber() {
		return substitutionOfSupplierOrderNumber;
	}

	public void setSubstitutionOfSupplierOrderNumber(
			int substitutionOfSupplierOrderNumber) {
		this.substitutionOfSupplierOrderNumber = substitutionOfSupplierOrderNumber;
	}

	public String getSubstitutionOfUnitConditionnement() {
		return substitutionOfUnitConditionnement;
	}

	public void setSubstitutionOfUnitConditionnement(
			String substitutionOfUnitConditionnement) {
		this.substitutionOfUnitConditionnement = substitutionOfUnitConditionnement;
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

	public String getAllocationDate() {
		return allocationDate;
	}

	public void setAllocationDate(String allocationDate) {
		this.allocationDate = allocationDate;
	}

	public int getStockFlag() {
		return stockFlag;
	}

	public void setStockFlag(int stockFlag) {
		this.stockFlag = stockFlag;
	}

	public int getSupplierReturnsFlag() {
		return supplierReturnsFlag;
	}

	public void setSupplierReturnsFlag(int supplierReturnsFlag) {
		this.supplierReturnsFlag = supplierReturnsFlag;
	}

}