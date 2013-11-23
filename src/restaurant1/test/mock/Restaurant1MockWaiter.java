package restaurant1.test.mock;

import restaurant1.Restaurant1CashierRole;
import restaurant1.Restaurant1Check;
import restaurant1.interfaces.Restaurant1Waiter;

public class Restaurant1MockWaiter extends Restaurant1Mock implements Restaurant1Waiter {
	Restaurant1CashierRole cashier;
	
	public Restaurant1EventLog log;

	public Restaurant1MockWaiter(String name) {
		super(name);
		log = new Restaurant1EventLog();
	}
	
	public void msgHereIsCheck(Restaurant1Check c) {
        log.add(new Restaurant1LoggedEvent("Received check from cashier."));
	}
}
 