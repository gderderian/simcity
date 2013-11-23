package restaurant1.interfaces;

import restaurant1.Restaurant1CashierRole;

public interface Restaurant1Market {

	public abstract void msgHereIsPayment(Restaurant1CashierRole c, double money);
	
	public abstract void msgCannotPayBill(Restaurant1CashierRole c, double total);
	
}
