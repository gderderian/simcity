package city.Restaurant4;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.Restaurant4.CookRole4.Order;

public class SharedOrders4 {
	List<Order> orders;
	public Semaphore monitor= new Semaphore(1);
	public CookRole4 consumer;
	
	public SharedOrders4(){
		orders= Collections.synchronizedList(new ArrayList<Order>());
	}
	
	public void setConsumer(CookRole4 c){
		consumer= c;
	}
	
	public boolean addOrder(Order o){
		if(monitor.tryAcquire()){
			synchronized(orders){
				System.out.println("ADDING ORDER TO REVOLVING STAND");
				orders.add(o);
				consumer.msgReadyForConsumption();
			}
			monitor.release();
			return true;
		}
		return false;
	}
	
	public Order fillOrder(){
		if(orders.size() > 0 && monitor.tryAcquire()){
			Order o= orders.get(0);
			synchronized(orders){
				System.out.println("REMOVING ORDER FROM REVOLVING STAND");
				orders.remove(0);
				monitor.release();
			}
			return o;
		}
		return null;
	}
}
