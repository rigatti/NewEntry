package db.product;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;


public interface IProductDAO {
	public String getDescription(String productCode);
	public ArrayList<Product> getProductsByDescription(String description, String searchOnSupplier);
	public Product getProductByCode(String code, boolean logError);
	public boolean insert(Product product, Session session);
	public int getMaxId();
	public boolean update(Product product, Session session);
	public boolean delete(Product product, Session session);
	public List<?> getProductsToExport();
	public List<?> getProductsToExport(String productCode);
	public List<?> getProductsToExport(String productCode, boolean mandatoryPlane);
	public List<?> getProductsToExportByReference(String productReference);
}