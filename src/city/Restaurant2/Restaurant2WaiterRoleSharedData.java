package city.Restaurant2;

import city.PersonAgent;

public class Restaurant2WaiterRoleSharedData extends Restaurant2WaiterRole{
	
	public Restaurant2WaiterRoleSharedData(String n, PersonAgent p) {
		super(n, p);
	}
	
	void SendOrderToCook(MyCustomer c){
		OrderSpindle.getInstance().addOrder(new Order(this, c.choice, c.table));
	}

}
