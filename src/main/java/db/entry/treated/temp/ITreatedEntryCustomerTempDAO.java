package db.entry.treated.temp;

import java.util.List;

public interface ITreatedEntryCustomerTempDAO {

	public List<TreatedEntryCustomerTemp> getAllForEntryId(int entryId);
	public List<TreatedEntryCustomerTemp> getAll();
	public boolean save(TreatedEntryCustomerTemp treatedEntryCustomerTemp);
	public boolean deleteAllByEntryId(int entryId);
}