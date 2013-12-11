package city.Restaurant4;

import justinetesting.interfaces.Waiter4;
import city.PersonAgent;
import city.Restaurant4.CookRole4.Order;

public class SharedDataWaiterRole4 extends WaiterRole4 implements Waiter4{
	SharedOrders4 orders;
	int id= 0;
	
	public SharedDataWaiterRole4(String name, PersonAgent p){
		super(name, p);
	}
	
	public void setOrders(SharedOrders4 orders){
		this.orders= orders;
	}

	@Override
	protected void sendOrderToCook(MyCustomer c){
		waiterGui.doGoToCook();
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Order o= new Order(this, c.choice, c.c, "pending", id++);
		orders.addOrder(o);
		c.s= customerState.none;
		p.stateChanged();
	}
}
