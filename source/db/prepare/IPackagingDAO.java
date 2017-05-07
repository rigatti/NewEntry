package db.prepare;

public interface IPackagingDAO {

	public Packaging get(int orderNumber, String orderLetter, String customerOrderCode, int customerOrderNumber);
	public Packaging get(int orderNumber, String orderLetter, String customerOrderCode, int customerOrderNumber, int feesType);


}