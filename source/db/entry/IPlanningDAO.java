package db.entry;

import java.util.ArrayList;
import java.util.List;

public interface IPlanningDAO  {

	public List<Planning> getByDate(String date);
	public ArrayList<Planning> getByDateAndSupplier(String date, String supplierCode);

}