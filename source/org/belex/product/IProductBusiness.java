package org.belex.product;

import java.util.Vector;

public interface IProductBusiness {
	Vector<Product> getProducts(String searchValue, String searchExactMatch, String searchType, String searchOnSupplier);
}
