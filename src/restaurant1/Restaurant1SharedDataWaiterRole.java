package restaurant1;

import restaurant1.Restaurant1WaiterRole.customerState;
import city.PersonAgent;

public class Restaurant1SharedDataWaiterRole extends Restaurant1WaiterRole {
	
	Restaurant1OrderWheel orderWheel;

	public Restaurant1SharedDataWaiterRole(String name, PersonAgent p) {
		super(name, p);
		
		orderWheel = Restaurant1OrderWheel.getInstance();
	}

	void sendOrderToCook(MyCustomer c) {
		Restaurant1Order order = new Restaurant1Order(this, c.choice, c.table);
		log("Adding " + c.c.getName() + "'s order of " + c.choice + " to our order wheel!");
		
		orderWheel.addOrder(order);
		
		c.s = customerState.orderSentToCook;
	}

}
