package db.product;

public interface IDescriptionDAO {
	public boolean insert(Description description);
	public boolean updateProductCode(String oldProductCode, String newProductCode);
	public boolean deleteProductCode(String productCode);
	
}