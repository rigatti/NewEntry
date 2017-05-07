package db.product;

import java.util.ArrayList;
import java.util.List;

import org.belex.product.Product.Unit;
import org.hibernate.Session;

public interface IConditioningDAO {
	public ArrayList<Conditioning> getProductsByEan(String ean, String searchOnSupplier);
	public ArrayList<Conditioning> getProductsByCode(String productCode, boolean searchExactMatch, String searchOnSupplier);
	public ArrayList<Conditioning> get(String productCode, String supplierCode, Unit unit);
	public ArrayList<Conditioning> get(String productCode, String supplierCode);
	public boolean update(Conditioning conditioning);
	public boolean updateProductCode(String oldProductCode, String newProductCode, Session session);
	public boolean deleteProductCode(String productCode, Session session);
	public boolean insert(Conditioning conditioning);
	public boolean insert(Conditioning conditioning, Session session);
	public int getNextPriority(String productCode, String supplierCode);
	public Conditioning getUnitScaleForTopPriority(String productCode);
}