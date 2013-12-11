package restaurant1;

import Role.Role;
import activityLog.ActivityLog;
import activityLog.ActivityTag;
import agent.Agent;
import test.mock.LoggedEvent;

import java.util.*;

import city.PersonAgent;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class Restaurant1HostRole extends Role {
	
	String roleName = "Restaurant1HostRole";
	
	static final int NTABLES = 4;//a global for the number of tables.
	public List<MyCustomer> customers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	
	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	
	public Collection<Table> tables = Collections.synchronizedCollection(new ArrayList<Table>(NTABLES));
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private String name;
	
	PersonAgent person;
	
	private int numberOfWorkingWaiters = 0; //To make sure there's always at least 1 waiter working
	
	private int waitingSpot = 0; //Keeps track of where new customers should wait.
	
	ActivityTag tag = ActivityTag.RESTAURANT1HOST;

	public Restaurant1HostRole(String name, PersonAgent p) {
		super();
		building = "rest1";

		this.name = name;
		person = p;
		// make some tables
		synchronized(tables) {
			for (int i = 1; i <= NTABLES; i++) {
				tables.add(new Table(i));
			}
		}
	}

	public String getName() {
		return name;
	}
	
	public void addWaiter(Restaurant1WaiterRole w) {
		waiters.add(new MyWaiter(w));
		numberOfWorkingWaiters++;
		person.stateChanged();
	}
	
	// Messages
	
	public void msgYoureFired(){
		person.msgImFired();
	}

	public void msgImHungry(Restaurant1CustomerRole c) {
		synchronized(customers) {
			for(MyCustomer mc : customers) {
				if(mc.c == c) {
					mc.waiting = true;
					mc.toldRestaurantIsFull = false;
					log("Welcome to Restaurant V2.2, " + c.getName() + "!");
					person.stateChanged();
					return;
				}
			}
		}
		customers.add(new MyCustomer(c));
		log("Welcome to Restaurant V2.2, " + c.getName() + "!");
		person.stateChanged();
	}
	
	public void msgIWantABreak(Restaurant1WaiterRole w) {
		synchronized(waiters) {
			for(MyWaiter mw : waiters) {
				if(mw.w == w) {
					mw.iWantABreak = true;
				}
			}
		}
		person.stateChanged();
	}
	
	public void msgImDoneWithMyBreak(Restaurant1WaiterRole w) {
		synchronized(waiters) {
			for(MyWaiter mw : waiters) {
				if(mw.w == w) {
					mw.iWantABreak = false;
					mw.state = waiterState.working;
					numberOfWorkingWaiters++;
				}
			}
		}
		person.stateChanged();
	}
	
	public void msgImLeaving(Restaurant1CustomerRole c) {
		synchronized(customers) {
			for(MyCustomer mc : customers) {
				if(mc.c == c) {
					mc.waiting = false;
					waitingSpot--;
				}
			}
		}
		person.stateChanged();
	}
	
	public void msgTableIsFree(int table, Restaurant1WaiterRole w) {
		synchronized(tables) {
			for (Table t : tables) {
				if (t.tableNumber == table) {
					t.occupied = false;
				}
			}
		}
		synchronized(waiters) {
			for (MyWaiter mw : waiters) {
				if(mw.w == w) {
					mw.numCustomers--;
				}
			}
		}
		person.stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		synchronized(waiters) {
			for(MyWaiter w : waiters) {
				if (w.iWantABreak) {
					giveWaiterABreak(w);
				}
			}		
		}
		
		synchronized(tables) {
			for (Table table : tables) {
				if (!table.occupied) {
					synchronized(customers) {
						for(MyCustomer mc : customers) {
							if (mc.waiting) {
								if(!waiters.isEmpty()) {
									seatCustomer(mc, table);

									return true;//return true to the abstract agent to reinvoke the scheduler.
								}
							}
						}
					}
				}
			}
		}

		synchronized(customers) {
			for(MyCustomer mc : customers) {
				if(mc.waiting && !mc.toldRestaurantIsFull && !waiters.isEmpty()) {
					tellCustomerRestaurantIsFull(mc);
				}
			}
		}
		
		return false;
	}

	// Actions
	
	private void seatCustomer(MyCustomer mc, Table table) {
		MyWaiter leastBusyWaiter = waiters.get(0);
		synchronized(waiters) {
			for(int i = 0; i < waiters.size(); i++) {
				if(waiters.get(i).state == waiterState.onBreak) {
					leastBusyWaiter = waiters.get(i+1);
				}
				else { break; }
			}
		}
		
		// This loop finds the waiter that is currently dealing with the least number of customers
		synchronized(waiters) {
			for(MyWaiter mw : waiters) {
				if(mw.numCustomers < leastBusyWaiter.numCustomers && mw.state != waiterState.onBreak) {
					leastBusyWaiter = mw;
				}
			}
		}

		leastBusyWaiter.w.msgPleaseSeatCustomer(this, mc.c, table.tableNumber);
		log(leastBusyWaiter.w.getName() + ", could you please seat customer " + mc.c.getName() + "?");
		leastBusyWaiter.numCustomers++; // Assigned a new customer to the least busy waiter
		mc.waiting = false;
		table.occupied = true;
	}
	
	private void giveWaiterABreak(MyWaiter w) {
		w.iWantABreak = false;
		if(waiters.size() == 1 || numberOfWorkingWaiters == 1) {
			log("Sorry, there's no one else to cover for you!");
			w.w.msgSorryNoBreakNow();
			return;
		}
				
		w.w.msgFinishUpAndTakeABreak();
		log("Alright, " + w.w.getName() + ", finish up and take a break.");
		w.state = waiterState.onBreak;
		numberOfWorkingWaiters--;
	}
	
	private void tellCustomerRestaurantIsFull(MyCustomer c) {
		log("The restaurant is currently full. Feel free to wait for an opening or leave!");
		c.toldRestaurantIsFull = true;
		c.c.msgRestaurantFull(waitingSpot);
		waitingSpot = (waitingSpot + 1) % 15;
	}

	// The animation DoXYZ() routines

	//utilities

	private class Table {
		int tableNumber;
		boolean occupied;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}
		
		public String toString() {
			return "table " + tableNumber;
		}
	}	
	
	private class MyWaiter {
		Restaurant1WaiterRole w;
		int numCustomers;
		boolean iWantABreak;
		waiterState state;
		
		MyWaiter(Restaurant1WaiterRole w) {
			this.w = w;
			numCustomers = 0; // Waiters start out with no customers
			state = waiterState.working;
		}
	}
	
	private enum waiterState { working, onBreak }; //Waiter is either working or taking a break
	
	private class MyCustomer {
		Restaurant1CustomerRole c;
		boolean waiting;
		boolean toldRestaurantIsFull; 
		
		MyCustomer(Restaurant1CustomerRole c) {
			this.c = c;
			this.waiting = true;
			this.toldRestaurantIsFull = false; //This gives customer a chance to leave if restaurant is full
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

	@Override
	public PersonAgent getPerson() {
		return person;
	}
}