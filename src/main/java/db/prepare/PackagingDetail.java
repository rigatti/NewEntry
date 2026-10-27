package db.prepare;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackagingDetail  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private int orderNumber;
	private String orderLetter;
	private String customerOrderCode;
	private int customerOrderCodeNumber;
	private int feesType;

	private int fromPackageNumber;
	private int toPackageNumber;
	private int fridge;

	public PackagingDetail() {
	}

}