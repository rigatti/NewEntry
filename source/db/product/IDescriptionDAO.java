package db.product;

import org.hibernate.Session;


public interface IDescriptionDAO {
	public boolean insert(Description description);
	public boolean insert(Description description, Session session);
	public boolean updateProductCode(String oldProductCode, String newProductCode, Session session);
	public boolean deleteProductCode(String productCode, Session session);
	
}