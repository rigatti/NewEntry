package db.entry;

public class SupplierEntryQuantity implements java.io.Serializable {
	private int supplierEntryId;
	private String productCode;
	private int numberGets;
	private int numberTransferedToCustomer;
	private int numberTransferedToStock;

	public SupplierEntryQuantity() {}

	public SupplierEntryQuantity(int supplierEntryId, String productCode) {
		super();
		this.supplierEntryId = supplierEntryId;
		this.productCode = productCode;
	}

	public int getSupplierEntryId() {
		return supplierEntryId;
	}
	public void setSupplierEntryId(int supplierEntryId) {
		this.supplierEntryId = supplierEntryId;
	}
	public int getNumberGets() {
		return numberGets;
	}
	public void setNumberGets(int numberGets) {
		this.numberGets = numberGets;
	}
	public int getNumberTransferedToCustomer() {
		return numberTransferedToCustomer;
	}
	public void setNumberTransferedToCustomer(int numberTransferedToCustomer) {
		this.numberTransferedToCustomer = numberTransferedToCustomer;
	}
	public int getNumberTransferedToStock() {
		return numberTransferedToStock;
	}
	public void setNumberTransferedToStock(int numberTransferedToStock) {
		this.numberTransferedToStock = numberTransferedToStock;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
}
