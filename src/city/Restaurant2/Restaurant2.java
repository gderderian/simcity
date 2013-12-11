package city.Restaurant2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.PersonAgent;

public class Restaurant2 {
	
	static Restaurant2 instance = new Restaurant2();
	
	String name;
	Restaurant2HostRole host;
	Restaurant2CashierRole cashier;
	Restaurant2CookRole cook;
	Restaurant2CustomerRole customer;
	List<Restaurant2WaiterRole> waiters;
	
	private boolean isOpen;
	
	public Restaurant2(){
		waiters = Collections.synchronizedList(new ArrayList<Restaurant2WaiterRole>());
		isOpen = true;
	}
	
	public static Restaurant2 getInstance(){
		return instance;
	}
	
	public void setHost(Restaurant2HostRole h){
		host = h;
	}
	
	public Restaurant2HostRole getHost(){
		if(!isOpen)
			return null;
		return host;
	}
	
	public Restaurant2CashierRole getCashier(){
		return cashier;
	}
	
	public Restaurant2CustomerRole getNewCustomerRole(PersonAgent p){
		customer = new Restaurant2CustomerRole(p);
		return customer;
	}
	
	public void addWaiters(Restaurant2WaiterRole w){
		//waiters.add(w);
		host.addWaiters(w);
		w.setCook(cook);
		w.setCashier(cashier);
	}
	
	public void setCook(Restaurant2CookRole c){
		cook = c;
	}
	
	public void setCashier(Restaurant2CashierRole c){
		cashier = c;
	}
	
	public void closeRestaurant(){
		isOpen = false;
		/*
		host.msgGoHome();
		cashier.msgGoHome();
		cook.msgGoHome();
		for(Restaurant2WaiterRole w : waiters){
			w.msgGoHome();
		}
		*/
	}
	
	public void openRestaurant(){
		isOpen = true;
	}
	
	public boolean isOpen(){
		return isOpen;
	}

	public void setInventoryLow() {
		cook.setInventoryLow();
	}
	
}
