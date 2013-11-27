package city.Restaurant5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.PersonAgent;
import city.Restaurant4.WaiterRole4;

public class Restaurant5 {
	
	String name;
	Restaurant5HostRole host;
	Restaurant5CashierRole cashier;
	Restaurant5CookRole cook;
	Restaurant5CustomerRole customer;
	List<Restaurant5WaiterRole> waiters;
	
	public Restaurant5(){
		waiters = Collections.synchronizedList(new ArrayList<Restaurant5WaiterRole>());
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

	public void setCook(Restaurant5CookRole c) {
		cook = c;
		
	}

	public void setCashier(Restaurant5CashierRole c) {
		cashier = c;
		
	}

	public void addWaiters(Restaurant5WaiterRole w) {
		waiters.add(w);
		host.addwaiter(w);
		System.out.println("THE COOK SHOULD NOT BE NULL: " + host);
		w.setCook(cook);
		w.setCashier(cashier);
		w.setHost(host);
	}
}
