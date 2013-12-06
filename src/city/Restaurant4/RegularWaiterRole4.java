package city.Restaurant4;

import justinetesting.interfaces.Waiter4;
import city.PersonAgent;

public class RegularWaiterRole4 extends WaiterRole4 implements Waiter4{
	public RegularWaiterRole4(String name, PersonAgent p){
		super(name, p);
	}
	
	@Override
	protected void sendOrderToCook(MyCustomer c){
		waiterGui.doGoToCook();
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cook.msgHereIsOrder(this, c.choice, c.c);
		c.s= customerState.none;
		p.stateChanged();
	}
}
