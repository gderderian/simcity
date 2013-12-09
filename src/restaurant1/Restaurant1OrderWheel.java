package restaurant1;

import java.util.*;

public class Restaurant1OrderWheel {
	
	static Restaurant1OrderWheel instance = new Restaurant1OrderWheel();
	
	List<Restaurant1Order> orders;
	
	Restaurant1OrderWheel() {
		orders = Collections.synchronizedList(new ArrayList<Restaurant1Order>());
	}
	
	static Restaurant1OrderWheel getInstance() {
		if(instance == null)
			instance = new Restaurant1OrderWheel();
		
		return instance;
	}
	
	public void addOrder(Restaurant1Order o) {
		synchronized(orders) {
			orders.add(o);
		}
	}
	
	public Restaurant1Order getOrder() {
		if(orders.isEmpty()) {
			return null;
		}
		else {
			return orders.remove(0);
		}
	}
}
