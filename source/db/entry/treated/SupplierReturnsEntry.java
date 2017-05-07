package db.entry.treated;

public class SupplierReturnsEntry  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
  	private int id;
  	private String productCode;
  	private String supplierCode;
  	private int numberOfProducts;
  	private int treatedEntryDetailDestinationId;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNumberOfProducts() {
		return numberOfProducts;
	}
	public void setNumberOfProducts(int numberOfProducts) {
		this.numberOfProducts = numberOfProducts;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public int getTreatedEntryDetailDestinationId() {
		return treatedEntryDetailDestinationId;
	}
	public void setTreatedEntryDetailDestinationId(
			int treatedEntryDetailDestinationId) {
		this.treatedEntryDetailDestinationId = treatedEntryDetailDestinationId;
	}
}