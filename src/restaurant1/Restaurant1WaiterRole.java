package restaurant1;

import Role.Role;
import activityLog.ActivityLog;
import activityLog.ActivityTag;
import agent.Agent;
import restaurant1.gui.Restaurant1WaiterGui;
import restaurant1.interfaces.Restaurant1Waiter;
import test.mock.LoggedEvent;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.PersonAgent;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public abstract class Restaurant1WaiterRole extends Role implements Restaurant1Waiter {
	
	String roleName = "Restaurant1WaiterRole";
	
	public List<MyCustomer> customers = new ArrayList<MyCustomer>();
	
	protected static enum customerState { waiting, seated, readyToOrder, 
		askedForOrder, ordered, orderSentToCook, orderOut, foodReady, served, checkReady, checkGiven, finished, 
		leftRestaurant };
		
	protected Restaurant1HostRole host;
	protected Restaurant1CookRole cook;
	
	protected Restaurant1CashierRole cashier;
	
	protected enum breakState { none, wantABreak, askedForBreak, onBreak, doneWithBreak };
	breakState breakStatus = breakState.none;

	protected String name;
	
	PersonAgent person;
	
	protected Semaphore atDestination = new Semaphore(0, true);
	
	protected enum waiterState { working, onBreak };
	protected waiterState state = waiterState.working;
	
	protected enum waiterEvent { none, backToWork, takeABreak };
	protected waiterEvent event = waiterEvent.none;
	
	public Restaurant1WaiterGui waiterGui = null;
	
	ActivityTag tag = ActivityTag.RESTAURANT1WAITER;

	public Restaurant1WaiterRole(String name, PersonAgent p) {
		super();
		building = "rest1";

		this.state = waiterState.working;
		this.name = name;
		person = p;
	}

	public String getName() {
		return name;
	}
	
	public void setHost(Restaurant1HostRole h) {
		this.host = h;
	}
	
	public void setCook(Restaurant1CookRole c) {
		this.cook = c;
	}
	
	public void setCashier(Restaurant1CashierRole c) {
		this.cashier = c;
	}
	
	// Messages
	
	public void msgPleaseSeatCustomer(Restaurant1HostRole h, Restaurant1CustomerRole c, int table) {
		
		this.host = h;
		
		for(MyCustomer mc : customers) {
			if(mc.c == c) {
				mc.s = customerState.waiting;
				mc.table = table;
				person.stateChanged();
				return;
			}
		}
		customers.add(new MyCustomer(c, table, customerState.waiting));
		person.stateChanged();
	}

	public void msgImReadyToOrder(Restaurant1CustomerRole c) {
		for(MyCustomer mc : customers) {
			if(mc.c == c) {
				mc.s = customerState.readyToOrder;
			}
		person.stateChanged();
		}
	}
	
	public void msgHereIsMyChoice(Restaurant1CustomerRole c, String choice) {
		for(MyCustomer mc : customers) {
			if(mc.c == c) {
				mc.s = customerState.ordered;
				mc.choice = choice;
				log("One " + choice + ", coming right up!");
			}
			person.stateChanged();
		}
	}
	
	public void msgHereIsCheck(Restaurant1Check c) {
		for(MyCustomer mc : customers) {
			if(mc.c == c.cust) {
				mc.check = c;
				mc.s = customerState.checkReady;
			}
		}
		person.stateChanged();
	}
	
	public void msgOutOf(String choice, int table) {
		for(MyCustomer mc : customers) {
			if(mc.table == table) {
				mc.s = customerState.orderOut;
			}
			person.stateChanged();
		}
	}
	
	public void msgOrderDone(String choice, int table, int orderNumber) {
		for(MyCustomer mc : customers) {
			if(mc.table == table) {
				mc.s = customerState.foodReady;
				mc.orderNumber = orderNumber;
			}
			person.stateChanged();
		}
	}
	
	public void msgImFinished(Restaurant1CustomerRole c) {
		for(MyCustomer mc : customers) {
			if(mc.c == c) {
				mc.s = customerState.finished;
			}
		}
		person.stateChanged();
	}
	
	public void msgIWantABreak() {
		breakStatus = breakState.wantABreak;
		person.stateChanged();
	}
	
	public void msgSorryNoBreakNow() {
		log("No break?! This is inhumane!");
		breakStatus = breakState.none;
		person.stateChanged();
	}
	
	public void msgFinishUpAndTakeABreak() {
		event = waiterEvent.takeABreak;
		person.stateChanged();
	}
	
	public void msgBreakIsFinished() {
		breakStatus = breakState.doneWithBreak;
		person.stateChanged();
	}
	
	public void msgAtDestination() {
		atDestination.release();
		person.stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		
		try {
			
			if(breakStatus == breakState.doneWithBreak) {
				finishBreak();
			}
			
			if(breakStatus == breakState.wantABreak) {
				askForBreak();
			}
		
			for(MyCustomer mc : customers) {
				if(mc.s == customerState.finished) {
					tellHostCustomerIsDone(mc);
					return true;
				}
			}
			for(MyCustomer mc : customers) {
				if(mc.s == customerState.checkReady) {
					getCheckFromCashier(mc);
				}
			}
			for(MyCustomer mc : customers) {
				if(mc.s == customerState.foodReady) {
					bringFoodToCustomer(mc);
					return true;
				}
			}
			for(MyCustomer mc : customers) {
				if(mc.s == customerState.waiting) {
					seatCustomer(mc);
					return true;
				}
			}
			for(MyCustomer mc : customers) {
				if(mc.s == customerState.readyToOrder) {
					takeOrder(mc);
					return true;
				}
			}
			for(MyCustomer mc : customers) {
				if(mc.s == customerState.orderOut) {
					removeChoice(mc);
					askToReorder(mc);
					return true;
				}
			}
			for(MyCustomer mc : customers) {
				if(mc.s == customerState.ordered) {
					sendOrderToCook(mc);
					return true;
				}
			}
			
			if(event == waiterEvent.takeABreak && state == waiterState.working && allCustomersDone()) {
				state = waiterState.onBreak;
				takeABreak();
				//return true;
			}
			
			if(event == waiterEvent.backToWork && state == waiterState.onBreak) {
				state = waiterState.working;
				event = waiterEvent.none;
				return true;
			}
	
			DoGoToHome();
			
		} catch(ConcurrentModificationException e) {
			
			return false;
			
		}
		
		return false;
	}

	// Actions

	private void seatCustomer(MyCustomer c) {
		
		waiterGui.DoGoToCustomer(c.c);
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		c.c.msgFollowMe(this, new Restaurant1Menu());
		
		DoSeatCustomer(c.c, c.table);
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		log("Welcome to Restaurant V2.1! Here is your seat.");
		
		c.s = customerState.seated;
	}
	
	private void takeOrder(MyCustomer c) {
		waiterGui.DoGoToTable(c.table);
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		c.s = customerState.askedForOrder;
		log("Taking order from: " + c.c.getName());
		c.c.msgWhatDoYouWant();
	}

	private void removeChoice(MyCustomer c) {		
		log("Sorry, we're out of " + c.choice + ".");
		c.c.msgRemoveFromMenu(c.choice);
	}
	
	private void askToReorder(MyCustomer c) {
		waiterGui.DoGoToTable(c.table);
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		c.s = customerState.askedForOrder;
		
		log("Taking order from: " + c.c.getName());
		c.c.msgPleaseReorder();
	}
	
	abstract void sendOrderToCook(MyCustomer c); /*{
		log("Sending " + c.c.getName() + "'s order of " + c.choice + " to cook wirelessly. Isn't technology great?");
		
		c.s = customerState.orderSentToCook;
		cook.msgHereIsOrder(this, c.choice, c.table);
	}*/
	
	private void bringFoodToCustomer(MyCustomer c) {
		log("Getting food from cook.");
		waiterGui.DoGoToCook();
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		cook.msgPickedUpOrder(c.orderNumber);
		
		waiterGui.deliveringFood(c.choice);
		waiterGui.DoGoToTable(c.table);
		
		log("Bringing food to table " + c.table);
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		log("Here is your " + c.choice + ".");
		c.s = customerState.served;
		waiterGui.foodDelivered();
		c.c.msgHereIsYourFood(c.choice);

		cashier.msgProduceCheck(this, c.c, c.choice);
		log("Cashier, can you prepare a check for this customer?");
	}
	
	private void tellHostCustomerIsDone(MyCustomer c) {
		c.s = customerState.leftRestaurant;
		host.msgTableIsFree(c.table, this);
		log("Table " + c.table + " is free!");
		c.table = 0; // Customer is no longer at one of the 4 tables
	}
	
	private void askForBreak() {
		log("Could I please have a break?!");
		host.msgIWantABreak(this);
		breakStatus = breakState.askedForBreak;
	}
	
	private void takeABreak() {
		waiterGui.DoGoToBreakZone();
		breakStatus = breakState.onBreak;
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waiterGui.msgBreakStarted();
	}
	
	private void finishBreak() {
		waiterGui.msgBreakFinished();
		event = waiterEvent.backToWork;
		breakStatus = breakState.none;
		log("Alright, break time is over. Back to work!");
		host.msgImDoneWithMyBreak(this);
	}
	
	private void getCheckFromCashier(MyCustomer c) {
		waiterGui.DoGoToCashier();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		giveCustomerCheck(c);
	}
	
	private void giveCustomerCheck(MyCustomer c) {
		waiterGui.DoGoToTable(c.table);
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
		
		log("Here is your check!");
		c.c.msgHereIsYourBill(c.check);
		c.s = customerState.checkGiven;
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(Restaurant1CustomerRole customer, int table) {
		log("Seating " + customer + " at " + table);
		waiterGui.DoBringToTable(customer, table);
	}
	
	private void DoGoToHome() {
		waiterGui.DoGoHome();
	}

	//utilities

	private boolean allCustomersDone() {
		for(MyCustomer c : customers) {
			if(c.s != customerState.finished && c.s != customerState.leftRestaurant) {
				return false;
			}
		}
		return true;
	}
	
	public void setGui(Restaurant1WaiterGui gui) {
		waiterGui = gui;
	}

	public Restaurant1WaiterGui getGui() {
		return waiterGui;
	}

	public boolean wantsToTakeBreak() {
		return event == waiterEvent.takeABreak || breakStatus == breakState.wantABreak;
	}
	
	public boolean isOnBreak() {
		return state == waiterState.onBreak;
	}
	
	class MyCustomer {
		Restaurant1CustomerRole c;
		int table;
		int orderNumber;
		customerState s;
		String choice;
		
		Restaurant1Check check;
		
		MyCustomer(Restaurant1CustomerRole c, int table, customerState state) {
			this.c = c;
			this.table = table;
			this.s = state;
		}
	}
	
	protected void log(String msg){
		print(msg);
        ActivityLog.getInstance().logActivity(tag, msg, name, false);
	}

	@Override
	public String getRoleName() {
		return roleName;
	}
	
	public PersonAgent getPerson(){
		return person;
	}
}