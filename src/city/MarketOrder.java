package city;

import java.util.*;

public class MarketOrder {
	
	List<OrderItem> orders = new ArrayList<OrderItem>();
	
	String destination;
	
	PersonAgent recipient; //Should usually be a CookRole - may need to change later

	MarketOrder(List<OrderItem> orders, String dest, PersonAgent p) {
		this.orders = orders;
		this.destination = dest;
		this.recipient = p;
	}
<<<<<<< HEAD
	
	public PersonAgent getRecipient(){
		return recipient;
	}
=======
>>>>>>> 95f9124601994057c3754bd9dd1cebc0b97917c3

}