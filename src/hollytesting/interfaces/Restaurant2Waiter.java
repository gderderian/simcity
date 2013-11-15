package hollytesting.interfaces;

import hollytesting.test.mock.EventLog;

public interface Restaurant2Waiter {
	
	EventLog log = new EventLog();
	
	void msgPleaseSeatCustomer(Restaurant2Customer c, int table);
	
	void msgReadyToBeSeated(Restaurant2Customer c);
	
	void msgLeavingNoMoney(Restaurant2Customer c);
	
	void msgReadyToOrder(Restaurant2Customer cust);
	
	void msgHereIsMyChoice(Restaurant2Customer cust, String ch);

	void msgOrderIsReady(String choice, int table, Restaurant2Cook c);
	
	void msgDoneEatingNowLeaving(Restaurant2Customer cust);
	
	void msgPermissionToTakeBreak(boolean takeBreak);
	
	//Don't need additional state here now, may need it later when customer can decide to leave.
	void msgOutOfFood(String food, int table);
	
	public void msgAtDest();
	
	public void msgBreakRequested();
	
	public void msgBreakOver();
	
	public void msgHereIsCheck(Restaurant2Customer customer, String food, double p);
	
	public void msgGetCheck(Restaurant2Customer c);
	
	public void setAtStand(boolean b);

	String getName();

}
