package justinetesting.test.mock;

import justinetesting.interfaces.Market4;

public class MockMarket4 extends Mock4 implements Market4 {
	public EventLog4 log= new EventLog4();
	
	public MockMarket4(String name){
		super(name);
	}
	
	@Override
	public void msgHereIsMoney(double amount){
		log.add(new LoggedEvent4("Received msgHereIsMoney from cashier. Amount: " + amount));
		System.out.println(this.name +  ": Received msgHereIsMoney from cashier. Amount: " + amount);
	}
}
