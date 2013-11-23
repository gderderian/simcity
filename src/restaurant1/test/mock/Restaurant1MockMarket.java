package restaurant1.test.mock;

import restaurant1.Restaurant1CashierRole;
import restaurant1.interfaces.Restaurant1Market;

public class Restaurant1MockMarket extends Restaurant1Mock implements Restaurant1Market {
	public Restaurant1CashierRole cashier;
	
	public double money = 1000.0;
	
	public Restaurant1EventLog log;

	public Restaurant1MockMarket(String name) {
		super(name);
		log = new Restaurant1EventLog();
	}
	
	public void msgHereIsPayment(Restaurant1CashierRole c, double money) {
		log.add(new Restaurant1LoggedEvent("Received payment for order."));
		this.money += money;
	}
	
	public void msgCannotPayBill(Restaurant1CashierRole c, double total) {
		log.add(new Restaurant1LoggedEvent("Cannot pay for order."));
	}
}
 