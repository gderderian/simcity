package city.Restaurant2;

import interfaces.Restaurant2Customer;
import interfaces.Restaurant2Host;
import interfaces.Restaurant2Waiter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import test.mock.EventLog;
import test.mock.LoggedEvent;
import Role.Role;

public class Restaurant2HostRole extends Role implements Restaurant2Host{
	
	static final int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	
	public enum CustomerState {hungry, seated, tablesFull};
	enum WaiterState {normal, breakRequested, onBreak, assessing};
	public EventLog log = new EventLog();
	
	public List<MyWaiter> waiters = Collections.synchronizedList(new LinkedList<MyWaiter>());

	private String name;
		
	Semaphore guard = new Semaphore(1);
	
	private int waiterNum;
	PersonAgent person;
	
	Restaurant2 restaurant;

	public Restaurant2HostRole(String name, PersonAgent p) {
		super();
		
		waiterNum = 1;
		person = p;

		this.name = name;
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public void setRestaurant(Restaurant2 r){
		restaurant = r;
	}
	
	/*
	public void setPerson(PersonAgent p){
		person = p;
	}
	*/
	//stateChanged?
	public void addWaiters(Restaurant2Waiter w){
		waiters.add(new MyWaiter(w));
		person.stateChanged();
	}
	
	
	// Messages

	public void msgIWantFood(Restaurant2Customer cust) {
		boolean alreadyExists = false;
		print("Customer is hungry");
		log.add(new LoggedEvent("Customer is hungry"));
		synchronized(customers){
			for(MyCustomer mc : customers){
				if(mc.c == cust){
					mc.cs = CustomerState.hungry;
					alreadyExists = true;
				}
			}
		}
		if(!alreadyExists){
			customers.add(new MyCustomer(cust));
			print("Adding customer");
		}
		int tcount = 0;
		synchronized(tables){
			for(Table t : tables){
				if(t.isOccupied()){
					tcount++;
				}
			}
		}
		if(tcount == 4){
			synchronized(customers){
				for(MyCustomer mc : customers){
					if(mc.c == cust){
						mc.cs = CustomerState.tablesFull;
					}
				}
			}
		}
		person.stateChanged();
	}
	
	public void msgNoTablesLeaving(Restaurant2Customer c){
		synchronized(customers){
			for(MyCustomer mc : customers){
				if(mc.c == c){
					mc.cs = CustomerState.seated;
				}
			}
		}
		person.stateChanged();
	}
	
	public void msgIllStay(Restaurant2Customer c){
		synchronized(customers){
			for(MyCustomer mc : customers){
				if(mc.c == c){
					mc.cs = CustomerState.hungry;
				}
			}
		}
		person.stateChanged();
	}

	public void msgTableIsFree(int table) {
		synchronized(tables){
			for(Table t : tables){
				if (t.tableNumber == table){
					t.setUnoccupied();
					person.stateChanged();
				}
			}
		}
	}
	
	public void msgRequestBreak(Restaurant2Waiter w){
		synchronized(waiters){
			for(MyWaiter mw : waiters){
				if (mw.w == w){
					mw.ws = WaiterState.breakRequested;
					person.stateChanged();
				}
			}
		}
	}
	
	public void msgBackFromBreak(Restaurant2Waiter w){
		synchronized(waiters){
			for(MyWaiter mw : waiters){
				if(mw.w == w){
					mw.ws = WaiterState.normal;
				}
			}
		}
	}
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		print("Inside host state pick and execute");
		try{
			for (Table table : tables) {
				if (!table.isOccupied()) {
					if(!waiters.isEmpty()){
						for(MyCustomer mc : customers){
							if(mc.cs == CustomerState.hungry){
								print("going to seat customer");
								seatCustomer(mc, table);//the action
								return true;//return true to the abstract agent to reinvoke the scheduler.
							}
						}
					}
				}
			}
		}
		catch(ConcurrentModificationException e){
			return true;
		}
		
		try{
			for(MyCustomer mc : customers){
				if(mc.cs == CustomerState.tablesFull){
					NotifyTablesFull(mc);
				}
			}
		}
		catch(ConcurrentModificationException e){
			return true;
		}
		
		if(!waiters.isEmpty()){
			try{
				for(MyWaiter mw : waiters){
					if (mw.ws == WaiterState.breakRequested){
						mw.ws = WaiterState.assessing;
						assessBreakRequest(mw);
						return true;
					}
				}
			}
			catch(ConcurrentModificationException e){
				return true;
			}

		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void seatCustomer(MyCustomer customer, Table table) {
		print("Seating customer.");
		log.add(new LoggedEvent("Now seating customer"));
		if(waiters.size() != 0){
			MyWaiter w = waiters.get(waiterNum-1);
			if(w.ws == WaiterState.breakRequested || w.ws == WaiterState.onBreak){
				if(waiterNum == waiters.size()){
					waiterNum = 1;
				}
				else{
					waiterNum++;
				}
				w = waiters.get(waiterNum-1);
			}
			print("Current waiter: " + w.w.getName());
			w.w.msgPleaseSeatCustomer(customer.c, table.tableNumber);
			customer.cs = CustomerState.seated;
			table.setOccupant(customer.c);
			if(waiterNum == waiters.size()){
				waiterNum = 1;
			}
			else{
				waiterNum++;
			}
		}	
	}
	
	private void NotifyTablesFull(MyCustomer mc){
		print(mc.c.getName() + ", tables are all full.");
		mc.c.msgTablesAreFull();
	}
	
	private void assessBreakRequest(MyWaiter waiter){
		for(MyWaiter w : waiters){
			System.out.println(w.w.getName() + ": " + w.ws);
		}
		if(waiters.size() <= 1){	//if only one waiter, request denied
			print("Waiter " + waiter.w.getName() + " can NOT take a break.");
			waiter.w.msgPermissionToTakeBreak(false);
			waiter.ws = WaiterState.normal;
			return;
		}
		synchronized(waiters){
			for(MyWaiter w : waiters){	//if another waiter has already requested a break or is on break, request denied
				if(w != waiter && (w.ws == WaiterState.breakRequested || w.ws == WaiterState.assessing || w.ws == WaiterState.onBreak)){
					print("Waiter " + waiter.w.getName() + " can NOT take a break.");
					waiter.w.msgPermissionToTakeBreak(false);
					waiter.ws = WaiterState.normal;
					return;
				}
			}
		}
		print("Waiter " + waiter.w.getName() + " can take a break.");
		waiter.w.msgPermissionToTakeBreak(true);
		waiter.ws = WaiterState.onBreak;
		return;
	}

	//utilities

	private class MyWaiter{
		Restaurant2Waiter w;
		WaiterState ws;
		
		public MyWaiter(Restaurant2Waiter waiter){
			w = waiter;
			ws = WaiterState.normal;
		}
		
	}
	
	public class MyCustomer{
		Restaurant2Customer c;
		public CustomerState cs;
		
		public MyCustomer(Restaurant2Customer customer){
			c = customer;
			cs = CustomerState.hungry;
		}
	}

	private class Table {
		Restaurant2Customer occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(Restaurant2Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
	
}
