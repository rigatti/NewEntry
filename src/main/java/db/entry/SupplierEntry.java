package db.entry;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SupplierEntry implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int supplierEntryId;
	private String supplierCode;
	private String orderNumbers;
	private String checked;
	private String responsible;
	private String entryDate;
	private String entryTime;
	private String closed;

	public SupplierEntry() {}

}
