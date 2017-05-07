package db.entry.treated;

import java.util.ArrayList;

import org.hibernate.Session;

public interface ITreatedEntryDetailDAO {

	//public TreatedEntry test();
	public boolean insert(TreatedEntryDetail treatedEntryDetail, Session session);
	public ArrayList<TreatedEntryDetail> get(String arrivalDate, Session session);
	public ArrayList<TreatedEntryDetail> get(String arrivalDate);
	public TreatedEntryDetail get(int treatedEntryId, Session session);
	public TreatedEntryDetail get(int treatedEntryId);
}