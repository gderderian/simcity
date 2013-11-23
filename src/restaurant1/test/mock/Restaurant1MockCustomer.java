package restaurant1.test.mock;


import restaurant1.Restaurant1CashierRole;
import restaurant1.interfaces.Restaurant1Customer;

public class Restaurant1MockCustomer extends Restaurant1Mock implements Restaurant1Customer {	
	public Restaurant1EventLog log;
	
	public Restaurant1CashierRole cashier;
	public double money;

	public Restaurant1MockCustomer(String name) {
		super(name);
		log = new Restaurant1EventLog();
		
		if(name.equals("flake")) {
			money = 0.0;
		} else {
			money = 100.0;
		}
	}

	public void msgHereIsChange(double change) {
		log.add(new Restaurant1LoggedEvent("Received change from cashier. Leaving restaurant."));
	}
	
}
