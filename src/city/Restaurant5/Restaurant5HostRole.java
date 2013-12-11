package city.Restaurant5;

import Role.Role;
import activityLog.ActivityLog;
import activityLog.ActivityTag;
import agent.Agent;
//import restaurant.CustomerAgent.AgentState;


import test.mock.LoggedEvent;
import tomtesting.interfaces.Restaurant5Host;
import tomtesting.interfaces.Restaurant5Customer;
import tomtesting.interfaces.Restaurant5Waiter;

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
public class Restaurant5HostRole extends Role implements Restaurant5Host {
	
	String roleName = "Restaurant5HostRole";
	
	static final int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	
	//with List semantics.
	public List<Restaurant5Customer> waitingCustomers = Collections.synchronizedList(new ArrayList<Restaurant5Customer>());
	
	//list of waiter agents
	public List<mywaiter> waiters = Collections.synchronizedList(new ArrayList<mywaiter>());
	public List<mywaiter> mywaiters = Collections.synchronizedList(new ArrayList<mywaiter>());
	public Collection<Table> tables;
	public List<WaitingSpot> waitingspots;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	
	PersonAgent person;
	boolean alltableoccupied = true;

	private String name;
    private int currentwaiter = 0;
	//public HostGui hostGui = null;
	private int numberoftablesfilled = 0;
	Restaurant5Customer assigncustomer;
	Restaurant5CustomerRole assigncustomertowaitingarea;
	int xcoordinateofwaitingspot = 25;
	int ycoordinateofwaitingspot = 170;
	int occupiedtablecounter = 0;
	
	ActivityTag tag = ActivityTag.RESTAURANT5HOST;
	
	public Restaurant5HostRole(String name, PersonAgent person) {
		super();
		building = "rest5";

		this.name = name;
		this.person = person;
		// make some tables
		tables = new ArrayList<Table>(NTABLES);
		int table1xcoordinate = 200;
		int table1ycoordinate = 250;
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix, table1xcoordinate, table1ycoordinate));//how you add to a collections
			table1xcoordinate = table1xcoordinate + 60;//I'm setting the xcoordinates and ycoordinates of other tables
		}
		waitingspots = new ArrayList<WaitingSpot>(5);
		
		for(int x = 1; x <= 5; x++) {
			waitingspots.add(new WaitingSpot(x, xcoordinateofwaitingspot, ycoordinateofwaitingspot));
		    ycoordinateofwaitingspot = ycoordinateofwaitingspot - 40;
		}
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection getTables() {
		return tables;
	}
	
	public int[] getxcoordinatesTables() {
		int [] xcoordinatestables = new int[NTABLES];
		Iterator<Table> it = this.tables.iterator();
		int i = 0;
		
		while(it.hasNext())
		{
			Table table = it.next();
			xcoordinatestables[i] = (table.gettablexcoordinate());
			i++;
		}
		return xcoordinatestables;
	}
	
	public int[] getycoordinatesTables() {
		int [] ycoordinatestables = new int[NTABLES];
		Iterator<Table> it = this.tables.iterator();
		int i = 0;
		
		while(it.hasNext())
		{
			Table table = it.next();
			ycoordinatestables[i] = (table.gettableycoordinate());
			i++;
		}
		return ycoordinatestables;
	}
	
	public void addwaiter(Restaurant5Waiter w)
	{
		mywaiters.add(new mywaiter(w));
	}
	
	// Messages

	public void msgIWantFood(Restaurant5Customer cust) {
		
		Do("arrived at restaurant");
		waitingCustomers.add(cust);
		person.stateChanged();
	}
	
	public void msgTableIsEmpty(Table T) {
		T.setUnoccupied();
	}

	public void msgCustomerleftTable(Restaurant5Customer cust) {
		cust = (Restaurant5CustomerRole) cust;
		Do("table empty!");
		
		synchronized(tables)
		{
		
		for (Table table : tables) {
			if (table.getOccupant() == cust) {
				log(cust + " leaving " + table);
				table.setUnoccupied();
				log("table " + table + " is set uoccupied");
				person.stateChanged();
			}
		}
		
		}
	}
	
	public void msgWaiterWantBreak(Restaurant5Waiter w) {
		
		if(mywaiters.size() > 1) {
			
		synchronized(mywaiters)
		{
			
		for(mywaiter waiter : mywaiters) {
			if(waiter.waiter == w)
			{
				waiter.onbreak = true;
			}
		
		}
		
		}
		
		}
		else {
			log("there's only one waiter, you cannot put him on break!");
		}
		
	}
	
	public void msgWaiterComeBackFromBreak(Restaurant5Waiter w) {
		
		synchronized(mywaiters)
		{
		
		for(mywaiter waiter : mywaiters) {
			if(waiter.waiter == w)
			{
				waiter.onbreak = false;
			}
		}
		
		}
		
	}
	
	
	public void msgWaiterBackInLobby(Restaurant5Waiter w)
	{
		synchronized(mywaiters)
		{
		
		for(mywaiter waiter : mywaiters) {
			if(waiter.waiter == w)
			{
				waiter.busy = false;
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
		synchronized(tables) 
		{
		
		for (Table table : tables) {
			if (!table.isOccupied()) {
				
				if (!waitingCustomers.isEmpty()) {
					synchronized(mywaiters)
					{
						
					for(mywaiter waiter : mywaiters) {
						if(waitingCustomers.isEmpty())
							break;
						if(waiter.busy == false && waiter.onbreak == false)
						{
							waiter.busy = true;
							if(!mywaiters.isEmpty()) {
						    assigncustomer = waitingCustomers.remove(0);	
							assignwaiter(assigncustomer, waiter.waiter, table);
							//why is this called twice?????
							occupiedtablecounter += 1;
							//DoSeatCustomerAtWaitingArea(assigncustomer, xcoordinateofwaitingspot, ycoordinateofwaitingspot);
							return true;
							}
						}
					}
					
					}
					
					}
					
					
					//return true;//return true to the abstract agent to reinvoke the scheduler.
				}
			}
		
			/*
			if(occupiedtablecounter == 4)
			{
			if(!waitingCustomers.isEmpty())
			{
			assigncustomertowaitingarea = waitingCustomers.remove(0);
			//assignwaiter(assigncustomer, waiter.waiter, table);
			synchronized(waitingspots)
			{
				
			for(WaitingSpot waitingspot : waitingspots) {
				if(waitingspot.occupied == false)
				{
					Do(assigncustomer.getName() + " is seating in the waiting spot");
					DoSeatCustomerAtWaitingArea(assigncustomertowaitingarea, waitingspot.xcoordinate, waitingspot.ycoordinate);
					waitingspot.occupied = true;
					break;
				}
	
			}
			
			}
			
			}
			}
			*/
		
		
		}
		return false;
		
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	

	// Actions
	// The animation DoXYZ() routines
	private void DoSeatCustomer(Restaurant5Customer customer, Table table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		log("Seating " + customer + " at " + table);
		//hostGui.DoBringToTable(customer, table.gettablexcoordinate(), table.gettableycoordinate()); 
	}
	
	private void DoSeatCustomerAtWaitingArea(Restaurant5CustomerRole customer, int xcoordinateofwaitingspot, int ycoordinateofwaitingspot) {
		Do("message customer to waiting area");
		customer.msgGoToWaitingArea(xcoordinateofwaitingspot, ycoordinateofwaitingspot);
		
	}
	
	private void Dogobacktolobby(){
		
		log("back in lobby");
		//hostGui.DoLeaveCustomer();
	}

	//utilities
	/*
	public void setGui(Restaurant5HostGui gui) {
		//hostGui = gui;
	}
	*/

	/*
	public HostGui getGui() {
		return hostGui;
	}
	*/
	
	public void assignwaiter(Restaurant5Customer customer, Restaurant5Waiter waiter, Table table)
	{
		table.setOccupant(customer);
		waiter.msgAssignMeCustomer(customer,table.gettablenumber());
	}
	
	public void tellcustomerrestaurantisfull() {
		for(Restaurant5Customer customer : waitingCustomers)
		{
			customer.msgRestaurantFullLeave();
			waitingCustomers.remove(customer);
		}
	}
	
	//table class
	public class WaitingSpot {
		Restaurant5Customer customer;
		int seatnumber;
		int xcoordinate;
		int ycoordinate;
		boolean occupied;
		
		WaitingSpot(int seatnumber, int xcoordinate, int ycoordinate) {
			this.seatnumber = seatnumber;
			this.xcoordinate = xcoordinate;
			this.ycoordinate = ycoordinate;
			occupied = false;
		}
		
		void setCustomer(Restaurant5Customer customer) {
			this.customer = customer;
			occupied = true;
			
		}
		
		void setUnoccupied() {
			occupied = false;
		}
		
	}
	
	
	
	public class Table {
		Restaurant5Customer occupiedBy;
		int tableNumber;
		int tablexcoordinate;
		int tableycoordinate;

		Table(int tableNumber, int newtablexcoordinate, int newtableycoordinate) {
			this.tableNumber = tableNumber;
			this.tablexcoordinate = newtablexcoordinate;
			this.tableycoordinate = newtableycoordinate;
		}

		void setOccupant(Restaurant5Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Restaurant5Customer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
		
		void setcoordinates(int xcoordinate, int ycoordinate)
		{
			tablexcoordinate = xcoordinate;
			tableycoordinate = ycoordinate;
			
		}
		int gettablexcoordinate()
		{
			return tablexcoordinate;
		}
		int gettableycoordinate()
		{
			return tableycoordinate;
		}
		int gettablenumber()
		{
			return tableNumber;
		}
	}
	
	//waiter class for host's list of waiters
	public class mywaiter {
		Restaurant5Waiter waiter;
		boolean busy;
		boolean backinlobby;
		boolean onbreak;
		
		mywaiter(Restaurant5Waiter w) {
			waiter = w; 
			busy = false;
		}
	
	
	}

	@Override
	public void msgTableIsEmpty(tomtesting.interfaces.Restaurant5Host.Table T) {
		// TODO Auto-generated method stub
		
	}






	//@Override
	//public void msgTableIsEmpty(restaurant.interfaces.Host.Table T) {
		// TODO Auto-generated method stub
		
	//}

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

