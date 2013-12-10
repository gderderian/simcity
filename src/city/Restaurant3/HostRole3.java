package city.Restaurant3;

import Role.Role;
import java.util.*;
import activityLog.ActivityLog;
import activityLog.ActivityTag;
import city.PersonAgent;
import city.gui.Restaurant3.WaiterGui3;

/**
 * Restaurant Host Agent
 */
public class HostRole3 extends Role {
	
	String roleName = "Restaurant3HostRole";
	
	static final int NTABLES = 4;
	
	//public List<CustomerRole3> waitingCustomers;
	public List<MyCustomer> waitingCustomers;
	
	//public List<MyWaiter> myWaiters;
	public List<MyWaiter> myWaiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	
	// public Collection<Table3> tables;
	public List<Table3> tables = Collections.synchronizedList(new ArrayList<Table3>());

	private String name;
	public WaiterGui3 hostGui = null;
	String carryingOrderText = "";
	
	PersonAgent person;
	
	ActivityTag tag = ActivityTag.RESTAURANT3HOST;
	
	public HostRole3(String name, PersonAgent p) {
		
		super();
		this.name = name;
		
		// myWaiters = new ArrayList<MyWaiter>();
		waitingCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
		
		// Generate all new tables
		//tables = new ArrayList<Table3>(NTABLES);
		int tableRoot = (int)Math.sqrt(NTABLES);
		int startingCoord = 150;
		int tableDistance = 125;
		
		for (int i = 0; i < tableRoot; i++) {
			for (int j = 0; j < tableRoot; j++){
				int tableNum = tableRoot * i + j + 1;
				int tableX = startingCoord + i*tableDistance;
				int tableY = startingCoord + j*tableDistance;
				tables.add(new Table3(tableNum, tableX, tableY));
			}
		}
		
		person = p;
		
		
	}
	
	// Messages
	public void msgIWantFood(CustomerRole3 cust, int locX, int locY) {
		log("Received message msgIWantFood from customer " + cust.getCustomerName() + ".");
		waitingCustomers.add(new MyCustomer(cust, locX, locY));
		person.stateChanged();
	}

	public void msgLeavingTable(CustomerRole3 customer) {
		log("Received message msgLeavingTable3 from customer " + customer.getCustomerName() + ".");
		synchronized(tables){
			for (Table3 table : tables) {
				if (table.getOccupant() == customer) {
					table.setUnoccupied();
					person.stateChanged();
				}
			}
		}
	}
	
	public void wantBreak(WaiterRole3 w){
		log("Received request to go on break from waiter " + w.getName() + ".");
		synchronized(myWaiters){
			for (MyWaiter waiter : myWaiters) {
				if (waiter.waiter.equals(w)){
					waiter.state = WaiterState.wantBreak;
				}
			}
		}
		person.stateChanged();
	}
	
	public void decrementCustomer(WaiterRole3 w){
		log("Received notification one customer left the restaurant.");
		synchronized(myWaiters){
			for (MyWaiter waiter : myWaiters) {
				if (waiter.waiter.equals(w)){
					waiter.numCustomers--;
				}
			}
		}
		person.stateChanged();
	}
	
	public void returnedFromBreak(WaiterRole3 w){
		log("Notified that waiter " + w.getName() + " has now returned from break.");
		synchronized(myWaiters){
			for (MyWaiter waiter : myWaiters) {
				if (waiter.waiter.equals(w)){
					waiter.state = WaiterState.none;
				}
			}
		}
		person.stateChanged();
	}
	
	public void imLeaving(CustomerRole3 c){
		synchronized(waitingCustomers){
			for (MyCustomer customer : waitingCustomers) {
				if (customer.customer.equals(c)){
					waitingCustomers.remove(customer);
					return;
				}
			}
		}
		person.stateChanged();
	}

	// Scheduler
	public boolean pickAndExecuteAnAction() {
		
		
		System.out.println("Host customer size: " + waitingCustomers.size() + " - Waiters: " + myWaiters.size());
		// System.out.println("Status: " + waitingCustomers.get(0).state);
		
		if (!waitingCustomers.isEmpty() && checkAllTable3sOccupied() == true) { // Ask customer if they want to stay when full
			try {
				if (waitingCustomers.get(0).state.equals(CustomerState.none)){ // If they haven't been notified restaurant is full, notify them
						waitingCustomers.get(0).customer.restaurantFull();
						waitingCustomers.get(0).state = CustomerState.notifiedFull;
						return true;
				}
			} catch(IndexOutOfBoundsException e){
				return true;
			}
			//waitingCustomers.remove(0);
			return true;
		}
		synchronized(tables){
			for (Table3 table : tables) {
				if (!table.isOccupied()) {
					if (!waitingCustomers.isEmpty()) {
						seatCustomer(waitingCustomers.get(0).customer, table, waitingCustomers.get(0).locX, waitingCustomers.get(0).locY);
						return true;
					}
				}
			}
		}
		synchronized(myWaiters){
			for (MyWaiter waiter : myWaiters) {
				if (waiter.state == WaiterState.wantBreak){
					processBreakRequest(waiter);
					return true;
				}
			}
		}
		return false;
	}

	// Actions
	private void seatCustomer(CustomerRole3 customer, Table3 table, int X, int Y) {
		// Find waiter and notify them
		log("Seating customer " + customer.getCustomerName() + " at table #" + table.tableNumber + ".");
		if (myWaiters.size() != 0) {
			int init_cust = myWaiters.get(0).numCustomers;
			MyWaiter w_selected = null;
			synchronized(myWaiters){
				for (MyWaiter w : myWaiters){
					if (w.numCustomers <= init_cust && w.isOnBreak() == false){
						init_cust = w.numCustomers;
						w_selected = w;
					}
				}
			}
			w_selected.waiter.msgSeatCustomer(customer, table.tableNumber, this, X, Y);
			w_selected.numCustomers++;
			table.setOccupant(customer);
			
			synchronized(waitingCustomers){
				for (MyCustomer cust : waitingCustomers) {
					if (cust.customer.equals(customer)){
						waitingCustomers.remove(cust);
						return;
					}
				}
			}
			
			
		}
	}
	
	public void processBreakRequest(MyWaiter w){
		int onBreakNow = getNumWaitersOnBreak();
		if (myWaiters.size() <= 1 || (onBreakNow == myWaiters.size() - 1)){ // One waiter also always has to be left!
			log("Rejecting request for waiter " + w.name + " to go on break.");
			w.waiter.breakRejected();
			w.state = WaiterState.none;
		} else {
			log("Approving request for waiter " + w.name + " to go on break.");
			w.waiter.breakApproved();
			w.state = WaiterState.onBreak;
		}
	}
	
	// Accessors
	public String getName() {
		return name;
	}
	
	public void addWaiter(WaiterRole3 w){
		MyWaiter waiter = new MyWaiter();
		waiter.waiter = w;
		waiter.name = w.getName();
		myWaiters.add(waiter);
	}
	
	public void setGui(WaiterGui3 gui) {
		hostGui = gui;
	}

	public WaiterGui3 getGui() {
		return hostGui;
	}
	
    public void setCarryText(String carryText){
    	carryingOrderText = carryText;
    }
    
    public Collection<Table3> getTables(){
    	return tables;
    }
    
	// Misc. Utilities
	public enum WaiterState // Goes along with MyWaiter below
	{none, wantBreak, onBreak};
	
	class MyWaiter {
		WaiterRole3 waiter;
		String name;
		int numCustomers;
		WaiterState state;
	
		MyWaiter(){
			state = WaiterState.none;
			numCustomers = 0;
		}
		
		public boolean isOnBreak(){
			if (state == WaiterState.onBreak){
				return true;
			}
			return false;
		}
		
	}
	
	public int getNumWaitersOnBreak() {
		int onBreakNow = 0;
		synchronized(myWaiters){
			for (MyWaiter w : myWaiters){
				if (w.state == WaiterState.onBreak){
					onBreakNow++;
				}
			}
		}
		return onBreakNow;
	}
	
	public boolean checkAllTable3sOccupied() {
		int totalOccupied = 0;
		synchronized(tables){
			for (Table3 table : tables) {
				if (table.isOccupied()) {
					totalOccupied++;
				}
			}
		}
		if (totalOccupied == tables.size()){
			return true; // All tables are occupied
		} else {
			return false; // There are still free tables
		}
	}
	
	public enum CustomerState // Goes along with MyCustomer below
	{none, notifiedFull};
	
	class MyCustomer {
		CustomerRole3 customer;
		CustomerState state;
		int locX;
		int locY;
		
		MyCustomer(CustomerRole3 c, int X, int Y){
			customer = c;
			state = CustomerState.none;
			locX = X;
			locY = Y;
		}
		
	}
	
	private void log(String msg){
		print(msg);
        ActivityLog.getInstance().logActivity(tag, msg, name, false);
	}

	@Override
	public String getRoleName() {
		return roleName;
	}
	
}