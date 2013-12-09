package restaurant1;

import restaurant1.*;

public class Restaurant1Order {
	
	public enum orderState { pending, cooking, cooked, pickedUp, finished };
	
	Restaurant1WaiterRole w;
	String choice;
	int table;
	orderState s;
	int orderNumber;

	Restaurant1Order(Restaurant1WaiterRole w, String choice, int table) {
		this.w = w;
		this.choice = choice;
		this.table = table;
		this.s = orderState.pending;
		this.orderNumber = -1;
	}
	
	public void setNumber(int num) {
		this.orderNumber = num;
	}
}
