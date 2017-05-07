package db.prepare;


public interface IPrepareOrderDetailDAO {

	public String getCustomerCode(int orderNumber, String orderLetter, 
			String customerOrderCode, int customerOrderCodeNumber, int feesType);


}