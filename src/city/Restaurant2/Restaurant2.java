package city.Restaurant2;

import java.util.List;

public class Restaurant2 {
	
	String name;
	Restaurant2HostRole host;
	Restaurant2CashierRole cashier;
	Restaurant2CookRole cook;
	List<Restaurant2WaiterRole> waiters;
	
	public Restaurant2(){
		host = new Restaurant2HostRole("Restaurant 2 Host");
	}
}
