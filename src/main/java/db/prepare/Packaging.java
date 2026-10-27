package db.prepare;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Packaging  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int orderNumber;
	private String orderLetter;
	private String customerOrderCode;
	private float customerOrderCodeNumber;
	private int feesType;
	private int fromPackageNumber;
	private String customerCode;

	
	public Packaging() {}

}