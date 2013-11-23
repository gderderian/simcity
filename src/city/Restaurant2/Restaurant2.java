package city.Restaurant2;

import java.util.List;

public class Restaurant2 {
	
	String name;
	Restaurant2HostRole host;
	Restaurant2CashierRole cashier;
	Restaurant2CookRole cook;
	Restaurant2CustomerRole customer;
	List<Restaurant2WaiterRole> waiters;
	
	public Restaurant2(){
		host = new Restaurant2HostRole("Restaurant 2 Host");
	}
	
	public Restaurant2HostRole getHostRole(){
		return host;
	}
	
	public Restaurant2HostRole getHost(){
		return host;
	}
	
	public Restaurant2CustomerRole getNewCustomerRole(){
		customer = new Restaurant2CustomerRole();
		return customer;
	}
}
