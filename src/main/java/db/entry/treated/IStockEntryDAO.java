package db.entry.treated;

public interface IStockEntryDAO {

	public StockEntry get(int treatedEntryDetailDestinationId);
	public boolean update(StockEntry stockEntry);
	public boolean insert(StockEntry stockEntry);
	public boolean updateProductCode(String oldProductCode, String newProductCode);
}