package db.product;

import java.util.List;


public interface IProductDAO {
	public String getDescription(String productCode);
	public List<Product> getProductsByDescription(String description, String searchOnSupplier);
	public Product getProductByCode(String code, boolean logError);
	public boolean insert(Product product);
	public int getMaxId();
	public boolean update(Product product);
	public boolean delete(Product product);
	public List<?> getProductsToExport();
	public List<?> getProductsToExport(String productCode);
	public List<?> getProductsToExport(String productCode, boolean mandatoryPlane);
	public List<?> getProductsToExportByReference(String productReference);
}