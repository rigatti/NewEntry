package org.belex.product;

import db.product.IProductDAO;
import org.belex.arrival.Arrival;

import java.util.Vector;

public interface IProductBusiness {
	Vector<Product> getProducts(String searchValue, String searchExactMatch, String searchType, String searchOnSupplier);
	Arrival newProduct(Product product, Arrival arrival);
	void updateProductUnit(Arrival arrival);
	String createProduct(String description, String ean, Product.Unit unit, String supplierCode, String alternateProductCode);
	void setProductDAO(IProductDAO pDao);
	Product.Unit fillSelectedUnit(Arrival arrival);
    void updateProductCode(String oldCode, String newCode);
	void deleteProductCode(String productCode);
}
