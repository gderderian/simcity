package city.Restaurant3;

import city.PersonAgent;

/**
 * Restauarant3 WaiterRole with Shared Data Operation (adding order to spindle for cook to see upon new orders)
 */
public class WaiterRole3Shared extends WaiterRole3 {

	public WaiterRole3Shared(String name, int startX, int startY, PersonAgent p) {
		super(name, startX, startY, p);
	}

	void sendToKitchen(MyCustomer c, String choice){
		//log("Sending order " + choice + " from customer " + c.customer.getName() + " to kitchen.");
		c.state = CustomerState.WaitingForFood;
		waiterGui.setDestination(225, 390);
		waiterGui.beginAnimate();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Order o = new Order();
		o.foodItem = choice;
		o.requestingWaiter = this;
		o.recipTable = c.tableNum;
		OrderSpindle3.returnSpindleInstance().newOrder(o);
	}
	
}
