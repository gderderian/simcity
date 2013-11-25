package tomtesting.test.mock;

import java.util.List;

import restaurant.Check;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiter;

public class MockMarket extends Mock implements Market {

	public MockMarket(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void msgReceviedOrderFromCook(Cook cook, String order) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("received message check is ready"));
	
	}
	
	public void msgReceivedCheckFromCashier(int amount) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("mock market received check from the cashier"));
		System.out.println(name + " received $" + amount + " from the cashier");
	}
	
}
