package city.Restaurant2;

import interfaces.Restaurant2Cook;
import interfaces.Restaurant2Customer;
import interfaces.Restaurant2Waiter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Semaphore;

import activityLog.ActivityLog;
import activityLog.ActivityTag;
import test.mock.EventLog;
import test.mock.LoggedEvent;
import city.Menu;
import city.PersonAgent;
import city.gui.restaurant2.Restaurant2WaiterGui;
import Role.Role;

public abstract class Restaurant2WaiterRole extends Role implements Restaurant2Waiter {
	
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
	
	String roleName = "Restaurant2WaiterRole";
	
	Timer timer = new Timer();
	
	enum WaiterState {noBreak, yesBreak, onBreak, requestBreak, breakRequested, deniedBreak};
	WaiterState state = WaiterState.noBreak;
	
	boolean atStand;
	PersonAgent person;
	
	boolean okForBreak;
	
	String outOf;
	
	boolean deliveringFood;
	
	protected Semaphore atDest = new Semaphore(0,true);
	private Semaphore AtStand = new Semaphore(0, true);
	
	protected Restaurant2WaiterGui waiterGui;
	ActivityTag tag = ActivityTag.RESTAURANT2WAITER;
	
	boolean test = false;
	
	public Restaurant2WaiterRole(String n, PersonAgent p){
		super();
		building = "rest2";
		
		name = n;
		person = p;
		
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
		System.out.println("Setting the waiter gui");
		waiterGui = g;
		gui = g;
	}
	
	public void setWaiterNum(int n){
		waiterNum = n;
	}
	
	/*
	public void setPerson(PersonAgent p){
		person = p;
	}*/
	
	
	//MESSAGES
	public void msgPleaseSeatCustomer(Restaurant2Customer c, int table, Restaurant2HostRole h){
		boolean returningCustomer = false;
		log("Received msg seat customer");
		host = h;
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
		person.stateChanged();
	}
	
	public void msgReadyToBeSeated(Restaurant2Customer c){
		synchronized(customers){
			for(MyCustomer mc : customers){
				if(mc.c == c){
					mc.s = CustomerState.proceed;
				}
			}
		}
		person.stateChanged();
	}
	
	public void msgLeavingNoMoney(Restaurant2Customer c){
		synchronized(customers){
			for(MyCustomer mc : customers){
				if(mc.c == c){
					mc.s = CustomerState.done;
				}
			}
		}
		person.stateChanged();
	}
	
	public void msgReadyToOrder(Restaurant2Customer cust){
		log("recieved msg ready to order");
		synchronized(customers){
			for(MyCustomer mc : customers){
				if(mc.c == cust){
					mc.s = CustomerState.askedToOrder;
					//log(mc.c.getName() + " has asked to order.");
				}
			}
		}
		person.stateChanged();
	}
	
	public void msgHereIsMyChoice(Restaurant2Customer cust, String ch){
		log("Sending order");
		synchronized(customers){
			for(MyCustomer mc : customers){
				if(mc.c == cust){
					mc.s = CustomerState.ordered;
					mc.choice = ch;
				}
			}
		}
		person.stateChanged();
	}

	public void msgOrderIsReady(String choice, int table, Restaurant2Cook c){
		log("Recieved msg OrderIsReady");
		orders.add(new Order(choice, table));
		person.stateChanged();
	}
	
	public void msgDoneEatingNowLeaving(Restaurant2Customer cust){
		synchronized(customers){
			for(MyCustomer mc : customers){
				if(mc.c == cust){
					mc.s = CustomerState.done;
					person.stateChanged();
				}
			}
		}
	}
	
	public void msgPermissionToTakeBreak(boolean takeBreak){
		if(takeBreak){
			state = WaiterState.yesBreak;
			log("Reieved msg can take break.");
		}
		else{
			state = WaiterState.deniedBreak;
			log("Recieved msg can NOT take break");
		}
		person.stateChanged();
	}
	
	//Don't need additional state here now, may need it later when customer can decide to leave.
	public void msgOutOfFood(String food, int table){
		log("Recieved msgOutOfFood");
		outOf = food;
		for(MyCustomer mc : customers){
			if(mc.table == table){
				mc.s = CustomerState.reorder;
			}
		}
		person.stateChanged();
	}
	
	public void msgAtDest() {//from animation
		atDest.release();// = true;
		person.stateChanged();
	}
	
	public void msgAtStand(){
		AtStand.release();
		person.stateChanged();
	}
	
	public void msgBreakRequested(){
		state = WaiterState.requestBreak;
		person.stateChanged();
	}
	
	public void msgBreakOver(){
		log("Coming back from break.");
		state = WaiterState.noBreak;
		host.msgBackFromBreak(this);
		person.stateChanged();
	}
	
	public void msgHereIsCheck(Restaurant2Customer customer, String food, double p){
		log("Recieved check for customer " + customer.getName());
		synchronized(customers){
			for(MyCustomer mc : customers){
				if(mc.c == customer){
					mc.price = p;
				}
			}
		}
		person.stateChanged();
	}
	
	public void msgGetCheck(Restaurant2Customer c){
		log("Recieved msg get check");
		synchronized(customers){
			for(MyCustomer mc : customers){
				if(mc.c == c){
					mc.s = CustomerState.needsCheck;
				}
			}
		}
		person.stateChanged();
	}
	
	//SCHEDULER
	public boolean pickAndExecuteAnAction() {
		//log("Waiter scheduler");
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
		//Scheduler will automatically be called when new customer is added.
		/*if(customers.isEmpty()){
			return true;
		}*/
		synchronized(customers){
			for(MyCustomer c : customers){
				if(c.s == CustomerState.waiting){
					//if(atStand){
						PromptCustomer(c);
						c.s = CustomerState.prompted;
						return true;
					//}
					//else
					//	return true;
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
					//else
					//	return true;
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
					log("Requesting reorder from customer");
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
				log("Taking break.");
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
		waiterGui.setDeniedBreak();
	}
	
	void PromptCustomer(MyCustomer mc){
		/*
		try {
			AtStand.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		log("Prompting customer");
		Menu m = new Menu();
		//Do("Seating customer at table " + mc.table);
		mc.c.msgFollowMeToTable(this, m, mc.table, waiterNum);
		log.add(new LoggedEvent("Prompting customer to follow me to table"));
	}
	
	void SeatCustomer(MyCustomer mc){
		/*try {
			AtStand.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		log("Seating customer");
		DoSeatCustomer(mc.c, mc.table);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waiterGui.DoLeaveCustomer();
	}
	
	private void DoSeatCustomer(Restaurant2Customer customer, int table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		log("Seating " + customer + " at table " + table);
		waiterGui.DoGoToTable(customer, table);
	}
	
	void TakeOrder(MyCustomer mc){
		Do("Taking order of customer at table " + mc.table);
		waiterGui.DoGoToTable(mc.c, mc.table);	//animation
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mc.c.msgWhatDoYouWant();
		waiterGui.DoLeaveCustomer();
	}
	
	void DeliverFood(Order o){
		Do("Delivering food to customer at table " + o.table);
		waiterGui.DoGoToKitchen();//animation
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cook.msgGotFood();
		waiterGui.DoDeliverFood(o.choice, o.table);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waiterGui.DoLeaveCustomer();
		for(MyCustomer mc : customers){
			if(mc.table == o.table){
				mc.s = CustomerState.hasFood;
				mc.c.msgHereIsYourFood(o.choice);
				o.s = OrderState.done;
				waiterGui.setDelivering(false);
				cashier.msgGenerateCheck(o.choice, mc.c, this);
			}
		}
	}
	
	abstract void SendOrderToCook(MyCustomer c);
	
	void sendCheck(MyCustomer c){
		waiterGui.DoGoToTable(c.c, c.table);
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log("Reached table");
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
		waiterGui.DoGoToTable(c.c, c.table);	//animation
		try {
			atDest.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c.c.msgPleaseReorder(outOf);
		//gui.DoLeaveCustomer();
	}
	
	void goToHome(){
		waiterGui.DoLeaveCustomer();
	}
	
	void requestBreak(){
		host.msgRequestBreak(this);
	}
	
	void takeBreak(){
		waiterGui.DoTakeBreak();
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
		person.stateChanged();
	}
	
	public Restaurant2WaiterGui getGui(){
		return waiterGui;
	}
	
	private void log(String msg){
		print(msg);
		if(!test)
			ActivityLog.getInstance().logActivity(tag, msg, name);
        log.add(new LoggedEvent(msg));
	}

	public void setTesting(boolean b) {
		test = b;
	}

	@Override
	public String getRoleName() {
		return roleName;
	}
	

}
