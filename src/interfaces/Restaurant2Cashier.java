package interfaces;

import interfaces.Restaurant2Customer;
import interfaces.Restaurant2Market;
import interfaces.Restaurant2Waiter;

public interface Restaurant2Cashier {
	
	public void msgGenerateCheck(String food, Restaurant2Customer c, Restaurant2Waiter w);
	
	public void msgHereIsPayment(Restaurant2Customer c, double amount);
	
	public void msgChargeForOrder(double total, Restaurant2Market m);

}
