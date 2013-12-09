package city.Restaurant3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.Restaurant3.CookRole3.Order;

public class OrderSpindle3 {
	
	List<Order> spindleOrders;
	
	public OrderSpindle3(){
		spindleOrders = Collections.synchronizedList(new ArrayList<Order>());
	}
	
	public void newOrder(Order newOrder){
		spindleOrders.add(newOrder);
	}
	
	public Order removeOrder(){
		Order firstOrder = spindleOrders.get(0);
		spindleOrders.remove(firstOrder);
		return firstOrder;
	}
	
	public boolean isSpindleEmpty(){
		if(spindleOrders.isEmpty()){
			return true;
		} else {
			return false;
		}
	}
	
}
