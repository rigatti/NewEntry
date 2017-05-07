package db.prepare;


public interface IPrepareOrderDAO {

	public PrepareOrder get(int orderNumber, String orderLetter);

}