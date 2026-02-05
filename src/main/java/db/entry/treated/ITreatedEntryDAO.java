package db.entry.treated;

import java.util.List;

public interface ITreatedEntryDAO {

	//public TreatedEntry test();
	public boolean insert(TreatedEntry treatedEntry);
	public List<TreatedEntry> get(String supplierCode, String arrivalDate);
	public List<TreatedEntry> getByDateRange(String supplierCode, String arrivalStartDate, String arrivalEndDate);
	public List<TreatedEntry> get(String productCode, String ean, String lot, String validityDate);
	public TreatedEntry getUnique(String supplierCode, String arrivalDate, String productCode);
	public TreatedEntry get(int treatedEntryId);
	public boolean updateProductCode(String oldProductCode, String newProductCode);
	
	public boolean cleanDb(String maxDate, ITreatedEntryDetailDAO treatedEntryDetailDAO, ITreatedEntryDetailDestinationDAO treatedEntryDetailDestinationDAO);
}