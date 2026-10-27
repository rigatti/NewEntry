package db.entry;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierEntryDetail {

	private int id;
	private int supplierEntryId;
	private String supplierCode;
	private int ligneNumber;
	private int orderNumber;
	private String date;
	private String productCode;
	private String ean;
	private String unit;
	private int numberGets;
	private int numberOfUnit;
	private String checked;
	private String updated;
	private String lot;

	public SupplierEntryDetail() {}

	public SupplierEntryDetail(int supplierEntryId) {
		super();
		this.supplierEntryId = supplierEntryId;
	}

}
