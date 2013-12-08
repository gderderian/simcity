package restaurant1;

import city.PersonAgent;

public class Restaurant1SharedDataWaiterRole extends Restaurant1WaiterRole {

	public Restaurant1SharedDataWaiterRole(String name, PersonAgent p) {
		super(name, p);
	}

	void sendOrderToCook(MyCustomer c) {
		log("Sending " + c.c.getName() + "'s order of " + c.choice + " to cook wirelessly. Isn't technology great?");
		
		c.s = customerState.orderSentToCook;
		cook.msgHereIsOrder(this, c.choice, c.table);		
	}

}
