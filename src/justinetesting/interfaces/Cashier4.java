package justinetesting.interfaces;

import interfaces.MarketManager;

public interface Cashier4 {

	public abstract void msgImStealingEveryCent();
	
	public abstract void msgComputeBill(Waiter4 w, Customer4 c, String choice);
	
	public abstract void msgHereIsMoney(Customer4 c, double amount);
	
	public abstract void msgHereIsBill(MarketManager m, double amount);
	
}
