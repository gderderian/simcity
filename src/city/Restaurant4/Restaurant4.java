package city.Restaurant4;

import java.util.*;

import justinetesting.interfaces.Cashier4;
import city.PersonAgent;
import city.gui.ControlPanel;

public class Restaurant4 {
	
	String name;
	HostRole4 host;
	CashierRole4 cashier;
	CookRole4 cook;
	CustomerRole4 customer;
	List<WaiterRole4> waiters;
	SharedOrders4 orders= new SharedOrders4();
	boolean isOpen= true;
	
	public Restaurant4(){
		waiters = Collections.synchronizedList(new ArrayList<WaiterRole4>());
	}
	
	public void setHost(HostRole4 h){
		host = h;
	}
	
	public HostRole4 getHost(){
		if(!isOpen){
			return null;
		}
		return host;
	}
	
	public CustomerRole4 getNewCustomerRole(PersonAgent p){
		customer = new CustomerRole4(p.getName(), p);
		return customer;
	}
	
	public void addWaiters(WaiterRole4 w){
		waiters.add(w);
		host.addWaiter(w);
		cook.addWaiter(w);
		w.setCook(cook);
		w.setCashier(cashier);
		w.setHost(host);
		if(w instanceof SharedDataWaiterRole4){
			w.setOrders(orders);
		}
	}
	
	public void setCook(CookRole4 c){
		cook = c;
		c.setOrders(orders);
		orders.setConsumer(c);
	}
	
	public void setCashier(CashierRole4 c){
		cashier = c;
	}

	public Cashier4 getCashier(){
		return cashier;
	}
	
	public int getWaiterListSize() {
		return waiters.size();
	}
	
	public boolean isOpen(){
		return isOpen;
	}
	
	public void close(){
		if(isOpen){
			isOpen= false;
			cook.closeRestaurant();
			System.out.println("REST 4 IS NOW CLOSED");
		} else{
			isOpen= true;
			for(WaiterRole4 w : waiters){
				w.msgBackInBusiness();
				w.p.msgBackToWork();
			}
			host.msgBackInBusiness();
			host.p.msgBackToWork();
			cashier.msgBackInBusiness();
			cashier.p.msgBackToWork();
			cook.p.msgBackToWork();
			System.out.println("REST 4 IS NOW OPEN");
		}
	}
	
	public void emptyInventory(){
		cook.msgEmptyInventory();
	}
	
	public void fireHost(){
		host.msgYoureFired();
	}
}