package db.entry.treated.temp;

import java.util.ArrayList;

import org.hibernate.Session;

public interface ITreatedEntryCustomerTempDAO {

	public ArrayList<TreatedEntryCustomerTemp> getAllForEntryId(int entryId);
	public ArrayList<TreatedEntryCustomerTemp> getAll();
	public boolean save(TreatedEntryCustomerTemp treatedEntryCustomerTemp, Session session);
	public boolean deleteAllByEntryId(int entryId, Session session);
}