package restaurant1;

import restaurant1.Restaurant1WaiterRole.customerState;
import city.PersonAgent;

public class Restaurant1NormalWaiterRole extends Restaurant1WaiterRole {

	public Restaurant1NormalWaiterRole(String name, PersonAgent p) {
		super(name, p);
	}

	void sendOrderToCook(MyCustomer c) {
		log("Sending " + c.c.getName() + "'s order of " + c.choice + " to cook wirelessly. Isn't technology great?");
		
		c.s = customerState.orderSentToCook;
		cook.msgHereIsOrder(this, c.choice, c.table);	
	}

}
