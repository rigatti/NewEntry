package db.entry;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupplierEntryQuantity implements java.io.Serializable {
	private int supplierEntryId;
	private String productCode;
	private int numberGets;
	private int numberTransferedToCustomer;
	private int numberTransferedToStock;

	public SupplierEntryQuantity() {}

	public SupplierEntryQuantity(int supplierEntryId, String productCode) {
		super();
		this.supplierEntryId = supplierEntryId;
		this.productCode = productCode;
	}

}
