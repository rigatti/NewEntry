package db.entry;

import java.util.List;

public interface IPlanningDAO  {

	public List<Planning> getByDate(String date);
	public List<Planning> getByDateAndSupplier(String date, String supplierCode);

}