package city;

import interfaces.Person;

import java.util.*;

public class MarketOrder {
	
	public List<OrderItem> orders = new ArrayList<OrderItem>();
	
	public String destination;
	
	Person recipient; //Should usually be a CookRole - may need to change later

	public MarketOrder(List<OrderItem> orders, String dest, Person p) {
		this.orders = orders;
		this.destination = dest;
		this.recipient = p;
	}
	
	public MarketOrder(String dest, Person p){
		destination = dest;
		recipient = p;
	}
	
	public MarketOrder(List<OrderItem> orders, Person p){
		this.orders = orders;
		recipient = p;
	}
	
	public void addOrder(OrderItem o){
		orders.add(o);
	}
	
	public Person getRecipient(){
		return recipient;
	}
	
	public List<OrderItem> getOrders() {
		return orders;
	}

}