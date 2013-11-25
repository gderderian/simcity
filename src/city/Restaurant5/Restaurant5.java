package city.Restaurant5;

import java.util.List;

import city.PersonAgent;

public class Restaurant5 {
	
	String name;
	Restaurant5HostRole host;
	Restaurant5CashierRole cashier;
	Restaurant5CookRole cook;
	Restaurant5CustomerRole customer;
	List<Restaurant5WaiterRole> waiters;
	
	public Restaurant5(){
		//nothing here
	}
	
	public void setHost(Restaurant5HostRole h){
		host = h;
	}
	
	public Restaurant5HostRole getHost(){
		return host;
	}
	
	public Restaurant5CustomerRole getNewCustomerRole(PersonAgent p){
		customer = new Restaurant5CustomerRole("",p);
		return customer;
	}
}
