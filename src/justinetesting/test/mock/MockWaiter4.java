package justinetesting.test.mock;

import city.Restaurant4.CustomerRole4;
import justinetesting.interfaces.Waiter4;

public class MockWaiter4 extends Mock4 implements Waiter4 {
	public EventLog4 log= new EventLog4();
	
	public MockWaiter4(String name) {
		super(name);
	}
	
	@Override
	public void msgHereIsBill(double amount, CustomerRole4 c){
		log.add(new LoggedEvent4("Received msgHereIsBill from cashier. Total = " + amount));
		c.msgHereIsBill(amount);
		System.out.println("waiter: Received msgHereIsBill from cashier. Total = " + amount);
	}
	
}
