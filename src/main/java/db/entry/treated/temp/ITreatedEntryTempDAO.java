package db.entry.treated.temp;

import java.util.List;

public interface ITreatedEntryTempDAO {

	public List<TreatedEntryTemp> getBySupplierCode(String supplierCode);

	public List<TreatedEntryTemp> getBySupplierCodeAndDate(String supplierCode, String date);

	public List<TreatedEntryTemp> get(String supplierCode, String date, String productCode);

	public TreatedEntryTemp getByProductCodeAndUnit(String productCode, int numberOfUnit, String unit);
	public TreatedEntryTemp getBySupplierCodeProductCodeAndUnit(String supplierCode, String productCode, int numberOfUnit, String unit);
	public TreatedEntryTemp getByDateSupplierCodeProductCodeAndUnit(String date, String supplierCode, String productCode, int numberOfUnit, String unit);
	
	public boolean update(TreatedEntryTemp treatedEntryTemp);
	public boolean updateProductCode(String oldProductCode, String newProductCode);
	
	public boolean save(TreatedEntryTemp treatedEntryTemp);
	
	public boolean delete(TreatedEntryTemp treatedEntryTemp);
	public boolean deleteAll(String supplierCode, String arrivalDate, ITreatedEntryCustomerTempDAO tecDAO);
	//public boolean deleteEntryIdBySupplier(String supplierCode, int entryId);
	//public boolean deleteAllBySupplier(String supplierCode, ITreatedEntryCustomerTempDAO tecDAO);
	
	public boolean cleanDb(ITreatedEntryCustomerTempDAO tecDAO);
}