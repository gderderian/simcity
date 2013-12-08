package city.Restaurant2;

import city.PersonAgent;

public class Restaurant2WaiterRoleSharedData extends Restaurant2WaiterRole{
	
	public Restaurant2WaiterRoleSharedData(String n, PersonAgent p) {
		super(n, p);
	}
	
	void SendOrderToCook(MyCustomer c){
		waiterGui.DoGoToSpindle();
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		OrderSpindle.getInstance().addOrder(new Order(this, c.choice, c.table));
		print("Adding an order to the spindle");
	}

}