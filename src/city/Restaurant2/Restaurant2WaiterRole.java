package city.Restaurant2;

import interfaces.Restaurant2Cook;
import interfaces.Restaurant2Customer;
import interfaces.Restaurant2Waiter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Semaphore;

import test.mock.EventLog;
import test.mock.LoggedEvent;
import city.Menu;
import city.gui.restaurant2.Restaurant2WaiterGui;
import Role.Role;

public class Restaurant2WaiterRole extends Role implements Restaurant2Waiter {
	
	public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public enum CustomerState {waiting, prompted, proceed, seated, askedToOrder, askedForOrder, ordering, ordered, doneOrdering, 
		 reorder, hasFood, needsCheck, hasCheck, done, gone};
	List<Order> orders = new ArrayList<Order>();
	enum OrderState {pending, done};
	Restaurant2Cook cook;
	Restaurant2HostRole host;
	Restaurant2CashierRole cashier;
	private String name;
	public EventLog log = new EventLog();
	
	private int waiterNum;
	
	Timer timer = new Timer();
	
	enum WaiterState {noBreak, yesBreak, onBreak, requestBreak, breakRequested, deniedBreak};
	WaiterState state = WaiterState.noBreak;
	
	boolean atStand;
	
	boolean okForBreak;
	
	String outOf;
	
	boolean deliveringFood;
	
	private Semaphore atDest = new Semaphore(0,true);
	
	private Restaurant2WaiterGui gui;
	
	public Restaurant2WaiterRole(String n){
		super();
		
		name = n;
		
		atStand = true;
		
		deliveringFood = false;
		
		okForBreak = false;
	}
	
	public void setCook(Restaurant2Cook c){
		cook = c;
	}
	
	public void setCashier(Restaurant2CashierRole c){
		cashier = c;
	}
	
	public void setHost(Restaurant2HostRole h){
		host = h;
	}
	
	public String getName(){
		return name;
	}
	
	public void setGui(Restaurant2WaiterGui g){
		gui = g;
	}
	
	public void setWaiterNum(int n){
		waiterNum = n;
	}
	
	
	//MESSAGES
	public void msgPleaseSeatCustomer(Restaurant2Customer c, int table){
		boolean returningCustomer = false;
		print("Recieved msg seat customer");
		synchronized(customers){
			for(MyCustomer mc : customers){
				if(mc.c == c){
					mc.s = CustomerState.waiting;
					mc.table = table;
					returningCustomer = true;
				}
			}
		}
		if(!returningCustomer){
			customers.add(new MyCustomer(c, table));
		}
		stateChanged();
	}
	
	public void msgReadyToBeSeated(Restaurant2Customer c){
		synchronized(customers){
			for(MyCustomer mc : customers){
				if(mc.c == c){
					mc.s = CustomerState.proceed;
				}
			}
		}
		stateChanged();
	}
	
	public void msgLeavingNoMoney(Restaurant2Customer c){
		synchronized(customers){
			for(MyCustomer mc : customers){
				if(mc.c == c){
					mc.s = CustomerState.done;
				}
			}
		}
		stateChanged();
	}
	
	public void msgReadyToOrder(Restaurant2Customer cust){
		print("recieved msg ready to order");
		synchronized(customers){
			for(MyCustomer mc : customers){
				if(mc.c == cust){
					mc.s = CustomerState.askedToOrder;
					//print(mc.c.getName() + " has asked to order.");
				}
			}
		}
		stateChanged();
	}
	
	public void msgHereIsMyChoice(Restaurant2Customer cust, String ch){
		print("Sending order");
		synchronized(customers){
			for(MyCustomer mc : customers){
				if(mc.c == cust){
					mc.s = CustomerState.ordered;
					mc.choice = ch;
				}
			}
		}
		stateChanged();
	}

	public void msgOrderIsReady(String choice, int table, Restaurant2Cook c){
		print("Recieved msg OrderIsReady");
		orders.add(new Order(choice, table));
		stateChanged();
	}
	
	public void msgDoneEatingNowLeaving(Restaurant2Customer cust){
		synchronized(customers){
			for(MyCustomer mc : customers){
				if(mc.c == cust){
					mc.s = CustomerState.done;
					stateChanged();
				}
			}
		}
	}
	
	public void msgPermissionToTakeBreak(boolean takeBreak){
		if(takeBreak){
			state = WaiterState.yesBreak;
			print("Reieved msg can take break.");
		}
		else{
			state = WaiterState.deniedBreak;
			print("Recieved msg can NOT take break");
		}
		stateChanged();
	}
	
	//Don't need additional state here now, may need it later when customer can decide to leave.
	public void msgOutOfFood(String food, int table){
		print("Recieved msgOutOfFood");
		outOf = food;
		for(MyCustomer mc : customers){
			if(mc.table == table){
				mc.s = CustomerState.reorder;
			}
		}
		stateChanged();
	}
	
	public void msgAtDest() {//from animation
		atDest.release();// = true;
		stateChanged();
	}
	
	public void msgBreakRequested(){
		state = WaiterState.requestBreak;
		stateChanged();
	}
	
	public void msgBreakOver(){
		print("Coming back from break.");
		state = WaiterState.noBreak;
		host.msgBackFromBreak(this);
		stateChanged();
	}
	
	public void msgHereIsCheck(Restaurant2Customer customer, String food, double p){
		print("Recieved check for customer " + customer.getName());
		synchronized(customers){
			for(MyCustomer mc : customers){
				if(mc.c == customer){
					mc.price = p;
				}
			}
		}
		stateChanged();
	}
	
	public void msgGetCheck(Restaurant2Customer c){
		print("Recieved msg get check");
		synchronized(customers){
			for(MyCustomer mc : customers){
				if(mc.c == c){
					mc.s = CustomerState.needsCheck;
				}
			}
		}
		stateChanged();
	}
	
	//SCHEDULER
	public boolean pickAndExecuteAnAction() {
		if(state == WaiterState.requestBreak){
			requestBreak();
			state = WaiterState.breakRequested;
			return true;
		}
		if(state == WaiterState.deniedBreak){
			resetGui();
			state = WaiterState.noBreak;
			return true;
		}
		if(customers.isEmpty()){
			return true;
		}
		synchronized(customers){
			for(MyCustomer c : customers){
				if(c.s == CustomerState.waiting){
					if(atStand){
						PromptCustomer(c);
						c.s = CustomerState.prompted;
						return true;
					}
					else
						return true;
				}
			}
		}
		synchronized(customers){
			for(MyCustomer c : customers){
				if(c.s == CustomerState.proceed){
					if(atStand){
						SeatCustomer(c);
						c.s = CustomerState.seated;
						return true;
					}
					else
						return true;
				}
			}
		}
		synchronized(customers){
			for(MyCustomer c : customers){
				if(c.s == CustomerState.askedToOrder){
					TakeOrder(c);
					c.s = CustomerState.askedForOrder;
					return true;
				}
			}
		}
		synchronized(customers){
			for(MyCustomer c : customers){
				if(c.s == CustomerState.ordered){
					SendOrderToCook(c);
					c.s = CustomerState.doneOrdering;
					return true;
				}
			}
		}
		synchronized(customers){
			for(MyCustomer c : customers){
				if(c.s == CustomerState.reorder){
					print("Requesting reorder from customer");
					Reorder(c);
					c.s = CustomerState.ordering;
					return true;
				}
			}
		}
		synchronized(customers){
			for(MyCustomer c : customers){
				if(c.s == CustomerState.done){
					FreeTable(c.table);
					return true;
				}
			}
		}
		synchronized(customers){
			for(MyCustomer c : customers){
				if(c.s == CustomerState.needsCheck){
					sendCheck(c);
					c.s = CustomerState.hasCheck;
					return true;
				}
			}
		}
		if (!orders.isEmpty()){	// && deliveringFood != true
			for(Order o : orders){
				if(o.s != OrderState.done){
					DeliverFood(o);
					return true;
				}
			}
		}
		synchronized(customers){
			for(MyCustomer c : customers){
				if(c.s != CustomerState.gone){
					return true;
				}
			}
			if(state == WaiterState.yesBreak){
				print("Taking break.");
				takeBreak();
				state = WaiterState.onBreak;
				return true;
			}
		}
		if(state != WaiterState.onBreak){
			goToHome();
		}
		return false;
	}
	
	//ACTIONS
	
	void resetGui(){
		gui.setDeniedBreak();
	}
	
	void PromptCustomer(MyCustomer mc){
		Menu m = new Menu();
		//Do("Seating customer at table " + mc.table);
		mc.c.msgFollowMeToTable(this, m, mc.table, waiterNum);
		log.add(new LoggedEvent("Prompting customer to follow me to table"));
	}
	
	void SeatCustomer(MyCustomer mc){
		log.add(new LoggedEvent("Seating customer"));
		DoSeatCustomer(mc.c, mc.table);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gui.DoLeaveCustomer();
	}
	
	private void DoSeatCustomer(Restaurant2Customer customer, int table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		print("Seating " + customer + " at table " + table);
		gui.DoGoToTable(customer, table);
	}
	
	void TakeOrder(MyCustomer mc){
		Do("Taking order of customer at table " + mc.table);
		gui.DoGoToTable(mc.c, mc.table);	//animation
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mc.c.msgWhatDoYouWant();
		gui.DoLeaveCustomer();
	}
	
	void DeliverFood(Order o){
		Do("Delivering food to customer at table " + o.table);
		gui.DoGoToKitchen();//animation
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cook.msgGotFood();
		gui.DoDeliverFood(o.choice, o.table);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gui.DoLeaveCustomer();
		for(MyCustomer mc : customers){
			if(mc.table == o.table){
				mc.s = CustomerState.hasFood;
				mc.c.msgHereIsYourFood(o.choice);
				o.s = OrderState.done;
				gui.setDelivering(false);
				cashier.msgGenerateCheck(o.choice, mc.c, this);
			}
		}
	}
	
	void SendOrderToCook(MyCustomer c){
		cook.msgHereIsOrder(this, c.choice, c.table);
	}
	
	void sendCheck(MyCustomer c){
		gui.DoGoToTable(c.c, c.table);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		print("Reached table");
		goToHome();
		c.c.msgHereIsYourCheck(c.choice, c.price, cashier);
		//goToHome();
	}
	
	void FreeTable(int table){
		host.msgTableIsFree(table);
		for(MyCustomer mc : customers){
			if (mc.table == table){
				mc.s = CustomerState.gone;
			}
		}
	}
	
	void Reorder(MyCustomer c){
		Do("Taking REorder of customer at table " + c.table);
		gui.DoGoToTable(c.c, c.table);	//animation
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c.c.msgPleaseReorder(outOf);
		//gui.DoLeaveCustomer();
	}
	
	void goToHome(){
		gui.DoLeaveCustomer();
	}
	
	void requestBreak(){
		host.msgRequestBreak(this);
	}
	
	void takeBreak(){
		gui.DoTakeBreak();
	}
	
	//WAITER CLASSES
	
	public class MyCustomer{
		Restaurant2Customer c;
		int table;
		String choice;
		double price;
		public CustomerState s;
		
		MyCustomer(Restaurant2Customer cust, int t){
			c = cust;
			table = t;
			choice = null;
			s = CustomerState.waiting;
			price = 0;
		}
		
		public MyCustomer(Restaurant2Customer cust) {
			c = cust;
			table = -1;
			choice = null;
			s = null;
		}

	}
	
	private class Order{
		String choice;
		int table;
		OrderState s;
		
		Order(String c, int t){
			choice = c;
			table = t;
			s = OrderState.pending;
		}

	}

	public void setAtStand(boolean b) {
		atStand = b;
	}
	
	public Restaurant2WaiterGui getGui(){
		return gui;
	}
	

}
