package db.entry.treated.temp;

import java.util.ArrayList;

import org.hibernate.Session;

public interface ITreatedEntryTempDAO {

	public ArrayList<TreatedEntryTemp> getBySupplierCode(String supplierCode, Session session);
	public ArrayList<TreatedEntryTemp> getBySupplierCode(String supplierCode);

	public ArrayList<TreatedEntryTemp> getBySupplierCodeAndDate(String supplierCode, String date);
	public ArrayList<TreatedEntryTemp> getBySupplierCodeAndDate(String supplierCode, String date, Session session);
	
	public ArrayList<TreatedEntryTemp> get(String supplierCode, String date, String productCode, Session session);

	public TreatedEntryTemp getByProductCodeAndUnit(String productCode, int numberOfUnit, String unit);
	public TreatedEntryTemp getBySupplierCodeProductCodeAndUnit(String supplierCode, String productCode, int numberOfUnit, String unit);
	public TreatedEntryTemp getByDateSupplierCodeProductCodeAndUnit(String date, String supplierCode, String productCode, int numberOfUnit, String unit);
	
	public boolean update(TreatedEntryTemp treatedEntryTemp);
	public boolean updateProductCode(String oldProductCode, String newProductCode, Session session);
	
	public boolean save(TreatedEntryTemp treatedEntryTemp, Session currentSession);
	
	public boolean delete(TreatedEntryTemp treatedEntryTemp, Session session);
	public boolean deleteAll(String supplierCode, String arrivalDate, Session session, ITreatedEntryCustomerTempDAO tecDAO);
	//public boolean deleteEntryIdBySupplier(String supplierCode, int entryId);
	//public boolean deleteAllBySupplier(String supplierCode, Session session, ITreatedEntryCustomerTempDAO tecDAO);
	
	public boolean cleanDb(Session session, ITreatedEntryCustomerTempDAO tecDAO);
}