package db.entry.treated;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockEntry  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
  	private int id;
  	private String productCode;
  	private String supplierCode;
  	private int numberOfProducts;
  	private int treatedEntryDetailDestinationId;

}