package restaurant1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.PersonAgent;
import city.Restaurant2.Restaurant2CashierRole;
import city.Restaurant2.Restaurant2CookRole;
import city.Restaurant2.Restaurant2CustomerRole;
import city.Restaurant2.Restaurant2HostRole;
import city.Restaurant2.Restaurant2WaiterRole;

public class Restaurant1 {

	String name;
	Restaurant1HostRole host;
	Restaurant1CashierRole cashier;
	Restaurant1CookRole cook;
	Restaurant1CustomerRole customer;
	List<Restaurant1WaiterRole> waiters;
	
	public boolean closed = false;

	public Restaurant1() {
		waiters = Collections.synchronizedList(new ArrayList<Restaurant1WaiterRole>());
	}

	public void setHost(Restaurant1HostRole h){
		host = h;
	}

	public Restaurant1HostRole getHost(){
		return host;
	}

	public Restaurant1CookRole getCook() {
		return cook;
	}

	public Restaurant1CustomerRole getNewCustomerRole(PersonAgent p){
		customer = new Restaurant1CustomerRole(name, p);
		return customer;
	}

	public void addWaiter(Restaurant1WaiterRole w){
		waiters.add(w);
		host.addWaiter(w);
		w.setCook(cook);
		w.setCashier(cashier);
	}

	public void setCook(Restaurant1CookRole c){
		cook = c;
	}

	public void setCashier(Restaurant1CashierRole c){
		cashier = c;
	}

	public int getWaiterListSize() {
		return waiters.size();
	}
	
	public boolean isOpen() {
		return !closed;
	}
	
	public void closeRestaurant() {
		closed = true;
		
		//Message workers to go home?
	}
}
