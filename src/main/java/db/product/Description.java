package db.product;

public class Description  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String productCode = "";
	private String language = "";
	private String description = "";

	public Description() {}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
}