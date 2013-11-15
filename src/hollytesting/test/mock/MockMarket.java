package hollytesting.test.mock;

import java.util.HashMap;

import hollytesting.interfaces.Restaurant2Cook;
import hollytesting.interfaces.Restaurant2Market;

public class MockMarket extends Mock implements Restaurant2Market{

	public MockMarket(String name) {
		super(name);
	}

	public void msgHereIsPayment(double payment) {
		log.add(new LoggedEvent("Received payment from cashier of "+ payment));
	}

	@Override
	public void msgPlaceFoodOrder(Restaurant2Cook cook,
			HashMap<String, Integer> order) {
		// TODO Auto-generated method stub
		
	}

}
