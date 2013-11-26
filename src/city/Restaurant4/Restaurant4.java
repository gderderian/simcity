package city.Restaurant4;

import java.util.*;

import city.PersonAgent;

public class Restaurant4 {
	
	String name;
	HostRole4 host;
	CashierRole4 cashier;
	CookRole4 cook;
	CustomerRole4 customer;
	List<WaiterRole4> waiters;
	
	public Restaurant4(){
		waiters = Collections.synchronizedList(new ArrayList<WaiterRole4>());
	}
	
	public void setHost(HostRole4 h){
		host = h;
	}
	
	public HostRole4 getHost(){
		return host;
	}
	
	public CustomerRole4 getNewCustomerRole(PersonAgent p){
		customer = new CustomerRole4(p.getName(), p);
		return customer;
	}
	
	public void addWaiters(WaiterRole4 w){
		waiters.add(w);
		host.addWaiter(w);
		w.setCook(cook);
		w.setCashier(cashier);
	}
	
	public void setCook(CookRole4 c){
		cook = c;
		System.out.println("THE cook SHOULD NOT BE NULL: " + cook);
	}
	
	public void setCashier(CashierRole4 c){
		cashier = c;
	}

	public int getWaiterListSize() {
		return waiters.size();
	}
	
}