package city.Restaurant3;

import city.PersonAgent;

public class WaiterRole3Normal extends WaiterRole3 {

	public WaiterRole3Normal(String name, int startX, int startY, PersonAgent p) {
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
		myCook.hereIsOrder(choice, this, c.tableNum);
	}
	
}
