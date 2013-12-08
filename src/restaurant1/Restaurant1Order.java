package restaurant1;

import restaurant1.Restaurant1CookRole.orderState;

public class Restaurant1Order {
	
	Restaurant1WaiterRole w;
	String choice;
	int table;
	orderState s;
	int orderNumber;

	Restaurant1Order(Restaurant1WaiterRole w, String choice, int table, orderState s, int number) {
		this.w = w;
		this.choice = choice;
		this.table = table;
		this.s = s;
		this.orderNumber = number;
	}
}
