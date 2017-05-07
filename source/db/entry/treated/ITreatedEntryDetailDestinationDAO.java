package db.entry.treated;

import java.util.ArrayList;

import org.hibernate.Session;

public interface ITreatedEntryDetailDestinationDAO {
	public boolean insert(TreatedEntryDetailDestination treatedEntryDetailDestination, Session session);
	public boolean insert(TreatedEntryDetailDestination treatedEntryDetailDestination);
	public boolean update(TreatedEntryDetailDestination treatedEntryDetailDestination, Session session);
	public boolean update(TreatedEntryDetailDestination treatedEntryDetailDestination);
	public ArrayList<TreatedEntryDetailDestination> getAll(int treatedEntryDetailId, Session session);
	public ArrayList<TreatedEntryDetailDestination> getAll(int treatedEntryDetailId);
	public TreatedEntryDetailDestination get(int id);
}