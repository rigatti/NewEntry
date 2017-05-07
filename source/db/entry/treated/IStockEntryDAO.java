package db.entry.treated;

import org.hibernate.Session;

public interface IStockEntryDAO {

	public StockEntry get(int treatedEntryDetailDestinationId, Session session);
	public boolean update(StockEntry stockEntry, Session session);
	public boolean insert(StockEntry stockEntry, Session session);
	public boolean updateProductCode(String oldProductCode, String newProductCode, Session session);
}