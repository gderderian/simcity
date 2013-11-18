package justinetesting.interfaces;

import city.Restaurant4.CustomerRole4;


public interface Waiter4 {

	public abstract void msgNumber(int num);
	
	public abstract void msgPleaseSeatCustomer(Customer4 c, int tableNum);
	
	public abstract void msgReadyToOrder(Customer4 c);
	
	public abstract void msgHereIsChoice(Customer4 c, String choice);
	
	public abstract void msgOutOfFood(String choice, Customer4 c);
	
	public abstract void msgRestocked(String choice);
	
	public abstract void msgOrderDone(String choice, Customer4 c);
	
	public abstract void msgDoneEating(Customer4 c);
	
	public abstract void msgWantBreak();
	
	public abstract void msgBreakApproved();
	
	public abstract void msgBreakDenied();
	
	public abstract void msgReadyForBill(Customer4 c);
	
	public abstract void msgHereIsBill(double amount, Customer4 c);

	public abstract void msgAllMarketsOut();
	
	public abstract void msgAtTable();
	
	public abstract void msgAtCook();
	
	public abstract void msgAtEntrance();
	
	public abstract void msgAtHome();
	
}
