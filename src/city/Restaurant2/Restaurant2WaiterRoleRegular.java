package city.Restaurant2;

import city.PersonAgent;

/*
 * This is the waiter class that will deliver orders to the cook by messaging him the order
 */
public class Restaurant2WaiterRoleRegular extends Restaurant2WaiterRole {

	public Restaurant2WaiterRoleRegular(String n, PersonAgent p) {
		super(n, p);
	}

	void SendOrderToCook(MyCustomer c){
		cook.msgHereIsOrder(this, c.choice, c.table);
	}
	
}
