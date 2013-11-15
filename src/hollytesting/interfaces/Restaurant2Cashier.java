package hollytesting.interfaces;

import hollytesting.interfaces.Restaurant2Customer;
import hollytesting.interfaces.Restaurant2Market;
import hollytesting.interfaces.Restaurant2Waiter;

public interface Restaurant2Cashier {
	
	public void msgGenerateCheck(String food, Restaurant2Customer c, Restaurant2Waiter w);
	
	public void msgHereIsPayment(Restaurant2Customer c, double amount);
	
	public void msgChargeForOrder(double total, Restaurant2Market m);

}
