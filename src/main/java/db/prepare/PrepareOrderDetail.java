package db.prepare;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrepareOrderDetail  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private int orderNumber;
	private String orderLetter;	
	private String customerOrderCode;
	private int customerOrderCodeNumber;
	private int feesType;
	private String customerCode;

	public PrepareOrderDetail(){}

}