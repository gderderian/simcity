package city.Restaurant2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.Restaurant2.Order;

public class OrderSpindle {
	List<Order> orders;
	private static OrderSpindle instance = new OrderSpindle();
	
	public OrderSpindle(){
		orders = Collections.synchronizedList(new ArrayList<Order>());
	}
	
	public static OrderSpindle getInstance(){
		return instance;
	}
	
	public void addOrder(Order o){
		orders.add(o);
	}
	
	public Order takeOffOrder(){
		Order o = orders.get(0);
		orders.remove(0);
		return o;
	}
	
	public boolean isEmpty(){
		if(orders.isEmpty()) return true;
		else return false;
	}
}
