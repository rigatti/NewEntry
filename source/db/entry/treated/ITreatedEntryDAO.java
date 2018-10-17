package db.entry.treated;

import java.util.ArrayList;

import org.hibernate.Session;

public interface ITreatedEntryDAO {

	//public TreatedEntry test();
	public boolean insert(TreatedEntry treatedEntry, Session session);
	public ArrayList<TreatedEntry> get(String supplierCode, String arrivalDate, Session session);
	public ArrayList<TreatedEntry> get(String supplierCode, String arrivalDate);
	public ArrayList<TreatedEntry> getByDateRange(String supplierCode, String arrivalStartDate, String arrivalEndDate);
	public ArrayList<TreatedEntry> get(String productCode, String ean, String lot, String validityDate);
	public TreatedEntry getUnique(String supplierCode, String arrivalDate, String productCode);
	public TreatedEntry getUnique(String supplierCode, String arrivalDate, String productCode, Session session);
	public TreatedEntry get(int treatedEntryId, Session session);
	public TreatedEntry get(int treatedEntryId);
	public boolean updateProductCode(String oldProductCode, String newProductCode, Session session);
	
	public boolean cleanDb(String maxDate, Session session, ITreatedEntryDetailDAO treatedEntryDetailDAO, ITreatedEntryDetailDestinationDAO treatedEntryDetailDestinationDAO);
}