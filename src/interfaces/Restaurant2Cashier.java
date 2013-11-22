package interfaces;

//import interfaces.Restaurant2Market; TODO add market capabilities

public interface Restaurant2Cashier {
	
	public void msgGenerateCheck(String food, Restaurant2Customer c, Restaurant2Waiter w);
	
	public void msgHereIsPayment(Restaurant2Customer c, double amount);
	
	//public void msgChargeForOrder(double total, Restaurant2Market m);

}
