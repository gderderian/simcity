package city;

import java.util.*;

public class MarketOrder {
	
	public List<OrderItem> orders = new ArrayList<OrderItem>();
	
	String destination;
	
	PersonAgent recipient; //Should usually be a CookRole - may need to change later

	MarketOrder(List<OrderItem> orders, String dest, PersonAgent p) {
		this.orders = orders;
		this.destination = dest;
		this.recipient = p;
	}
	
	MarketOrder(String dest, PersonAgent p){
		destination = dest;
		recipient = p;
	}
	
	public void addOrder(OrderItem o){
		orders.add(o);
	}
	
	public PersonAgent getRecipient(){
		return recipient;
	}

}