package db.entry.treated;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreatedEntry  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int treatedEntryId;
	private String productCode;
	private String validityDate;
	private String ean;
	private String lotNumber;
	private String supplierCode;
	private String supplierProductCode;
	private String arrivalDate;
	private String arrivalTime;
	

	public TreatedEntry() {}

}