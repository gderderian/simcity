package restaurant1;

import restaurant1.interfaces.Restaurant1Customer;

public class Restaurant1Check {
	public Restaurant1Customer cust;
	public Restaurant1CashierRole cashier;
	public String choice;
	public double amount;
	
	Restaurant1Check(Restaurant1CashierRole cashier, Restaurant1Customer cust, String choice) {
		this.cashier = cashier;
		this.cust = cust;
		this.choice = choice;
	}
	
	public Restaurant1Customer getCust() {
		return cust;
	}
}
