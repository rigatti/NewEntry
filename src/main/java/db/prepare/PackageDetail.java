package db.prepare;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackageDetail  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int orderNumber;
	private String orderLetter;
	private String customerOrderCode;
	private int customerOrderCodeNumber;
	private int feesType;
	private String productCode;
	private int fromPackageNumber;
	private int numberOfUnit;
	private String unit;
	private int numberOfProduct;
	private String supplierCode;

	public PackageDetail() {
	}

	

}