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
		System.out.println("THE COOK SHOULD NOT BE NULL: " + cook);
		w.setCook(cook);
		w.setCashier(cashier);
		w.setHost(host);
	}
	
	public void setCook(CookRole4 c){
		cook = c;
	}
	
	public void setCashier(CashierRole4 c){
		cashier = c;
	}

	public int getWaiterListSize() {
		return waiters.size();
	}
	
}