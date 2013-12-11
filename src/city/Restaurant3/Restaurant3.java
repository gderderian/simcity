package city.Restaurant3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import city.PersonAgent;
import city.Restaurant3.CashierRole3;
import city.Restaurant3.CookRole3;
import city.Restaurant3.CustomerRole3;
import city.Restaurant3.HostRole3;
import city.Restaurant3.WaiterRole3;

/**
 * Restaurant3 Base Class
 */
public class Restaurant3 {
	
	String name;
	HostRole3 host;
	CashierRole3 cashier;
	CookRole3 cook;
	CustomerRole3 customer;
	List<WaiterRole3> waiters;
	private boolean isOpen;
	
	public Restaurant3(){
		waiters = Collections.synchronizedList(new ArrayList<WaiterRole3>());
	}
	
	public void setHost(HostRole3 h){
		host = h;
	}
	
	public HostRole3 getHost(){
		return host;
	}
	
	public CustomerRole3 getNewCustomerRole(PersonAgent p){
		customer = new CustomerRole3("Customer", 50, 50, p); // name, startX, startY, personAgent reference
		return customer;
	}
	
	public void addWaiters(WaiterRole3 w){
		//waiters.add(w);
		//if (cook == null || cashier == null || host == null || w == null){
			host.addWaiter(w);
			w.setCook(cook);
			w.setCashier(cashier);
		//}
	}
	
	public void setCook(CookRole3 c){
		cook = c;
	}
	
	public void setCashier(CashierRole3 c){
		cashier = c;
	}
	
	public CashierRole3 getCashier(){
		return cashier;
	}
	
	public boolean isOpen(){
		return isOpen;
	}
	
}