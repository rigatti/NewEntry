package db.product;

import org.belex.product.Product.Unit;

import java.util.List;

public interface IConditioningDAO {
	public List<Conditioning> getProductsByEan(String ean, String searchOnSupplier);
	public List<Conditioning> getProductsByCode(String productCode, boolean searchExactMatch, String searchOnSupplier);
	public List<Conditioning> get(String productCode, String supplierCode, Unit unit);
	public List<Conditioning> get(String productCode, String supplierCode);
	public boolean update(Conditioning conditioning);
	public boolean updateProductCode(String oldProductCode, String newProductCode);
	public boolean deleteProductCode(String productCode);
	public boolean insert(Conditioning conditioning);
	public int getNextPriority(String productCode, String supplierCode);
	public Conditioning getUnitScaleForTopPriority(String productCode);
}