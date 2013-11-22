package hollytesting.test.mock;

import interfaces.Restaurant2Cook;

import java.util.HashMap;

import hollytesting.interfaces.Restaurant2Market;
import test.mock.*;

public class MockMarket extends Mock implements Restaurant2Market{

	public MockMarket(String name) {
		super(name);
	}

	public void msgHereIsPayment(double payment) {
		log.add(new LoggedEvent("Received payment from cashier of "+ payment));
	}

	public void msgPlaceFoodOrder(Restaurant2Cook cook,
			HashMap<String, Integer> order) {
		log.add(new LoggedEvent("Placed food order"));
	}

}
