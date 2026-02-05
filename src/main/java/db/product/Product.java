package db.product;

public class Product implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String productCode;
	private String alternateProductCode;
	private String description;
	private String unitConditioning;
	private String followedProduct;
	private boolean planeMandatory;
	private int familyCode;
	private int sortingOrder;
	
	private int tempId;
	
	public Product(){}
	
	public Product(String productCode) {
		this.productCode = productCode;
	}
	
	public String getAlternateProductCode() {
		return alternateProductCode;
	}

	public void setAlternateProductCode(String alternateProductCode) {
		this.alternateProductCode = alternateProductCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTempId() {
		return tempId;
	}

	public void setTempId(int tempId) {
		this.tempId = tempId;
	}

	public String getUnitConditioning() {
		return unitConditioning;
	}

	public void setUnitConditioning(String unitConditioning) {
		this.unitConditioning = unitConditioning;
	}

	public String getFollowedProduct() {
		return followedProduct;
	}

	public void setFollowedProduct(String followedProduct) {
		this.followedProduct = followedProduct;
	}

	public boolean isPlaneMandatory() {
		return planeMandatory;
	}

	public void setPlaneMandatory(boolean planeMandatory) {
		this.planeMandatory = planeMandatory;
	}

	public int getFamilyCode() {
		return familyCode;
	}

	public void setFamilyCode(int familyCode) {
		this.familyCode = familyCode;
	}

	public int getSortingOrder() {
		return sortingOrder;
	}

	public void setSortingOrder(int sortingOrder) {
		this.sortingOrder = sortingOrder;
	}
}