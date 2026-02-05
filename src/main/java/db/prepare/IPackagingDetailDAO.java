package db.prepare;

public interface IPackagingDetailDAO {

	public PackagingDetail get(int orderNumber, String orderLetter, String customerOrderCode, int customerOrderCodeNumber, int feesType, int fromPackageNumber);
}