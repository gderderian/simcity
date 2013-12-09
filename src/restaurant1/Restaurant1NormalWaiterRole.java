package restaurant1;

import restaurant1.Restaurant1WaiterRole.customerState;
import city.PersonAgent;

public class Restaurant1NormalWaiterRole extends Restaurant1WaiterRole {

	public Restaurant1NormalWaiterRole(String name, PersonAgent p) {
		super(name, p);
	}

	void sendOrderToCook(MyCustomer c) {
		Restaurant1Order order = new Restaurant1Order(this, c.choice, c.table);
		log("Sending " + c.c.getName() + "'s order of " + c.choice + " to cook wirelessly. Isn't technology great?");
		
		cook.msgHereIsOrder(order);	
		c.s = customerState.orderSentToCook;
	}

}
