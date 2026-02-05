package db.entry.treated;

import java.util.List;

public interface ITreatedEntryDetailDAO {

	//public TreatedEntry test();
	public boolean insert(TreatedEntryDetail treatedEntryDetail);
	public List<TreatedEntryDetail> get(String arrivalDate);
	public TreatedEntryDetail get(int treatedEntryId);
}