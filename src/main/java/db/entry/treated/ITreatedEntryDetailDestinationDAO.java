package db.entry.treated;

import java.util.List;

public interface ITreatedEntryDetailDestinationDAO {
	public boolean insert(TreatedEntryDetailDestination treatedEntryDetailDestination);
	public boolean update(TreatedEntryDetailDestination treatedEntryDetailDestination);
	public List<TreatedEntryDetailDestination> getAll(int treatedEntryDetailId);
	public TreatedEntryDetailDestination get(int id);
}