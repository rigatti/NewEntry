package db.supplier;

public class Supplier implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String supplierCode;
	private String description;
	
	public Supplier(){}
	
	public Supplier(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
}