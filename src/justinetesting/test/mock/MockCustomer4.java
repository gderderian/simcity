package justinetesting.test.mock;

import justinetesting.interfaces.Customer4;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 */
public class MockCustomer4 extends Mock4 implements Customer4 {
	public EventLog4 log= new EventLog4();

	public MockCustomer4(String name) {
		super(name);

	}

	@Override
	public void msgHereIsChange(boolean paid){
		log.add(new LoggedEvent4("Received msgHereIsChange from cashier. Paid = " + paid));
		System.out.println(name + ": Received msgHereIsChange from cashier. Paid = " + paid);
	}

	@Override
	public void msgHereIsBill(double amount){
		log.add(new LoggedEvent4("Received msgHereIsBill from waiter. Amount = " + amount));
		System.out.println(name + ": Received msgHereIsBill from waiter. Amount = " + amount);
	}
	
}
