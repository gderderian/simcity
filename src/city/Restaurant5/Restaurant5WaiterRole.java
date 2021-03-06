package city.Restaurant5;

import Role.Role;
import activityLog.ActivityLog;
import activityLog.ActivityTag;
import agent.Agent;





import city.PersonAgent;
import city.gui.Restaurant5.Restaurant5WaiterGui;
import test.mock.LoggedEvent;
import tomtesting.interfaces.Restaurant5Customer;
import tomtesting.interfaces.Restaurant5Market;
import tomtesting.interfaces.Restaurant5Waiter;
import tomtesting.interfaces.Restaurant5Cashier;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public abstract class Restaurant5WaiterRole extends Role implements Restaurant5Waiter {
	
	String roleName = "Restaurant5WaiterRole";
	
	static final int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<Restaurant5CustomerRole> waitingCustomers
	= Collections.synchronizedList(new ArrayList<Restaurant5CustomerRole>());

	public List<mycustomer> customers = Collections.synchronizedList(new ArrayList<mycustomer>());
	public List<orders> orders = Collections.synchronizedList(new ArrayList<orders>());
	public List<Restaurant5Check> checks = Collections.synchronizedList(new ArrayList<Restaurant5Check>());
	//public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	public enum AgentState
	{DoingNothing, SeatingCustomer, BackInTheLobby, BringingFood, BringingCheck, FoodIsOut};
	public enum customerstate {Waiting, Seated, ReadyToOrder, Ordered, WaitingForFood, FoodReady, Reorder, Eating, ReadyToPay, WaitingForCheck, CheckReady,GoingToCashier, Leaving, LeavingWithoutEating};
	public AgentState state = AgentState.DoingNothing;//The start state
	//used to be private

	private String name;
	private Restaurant5HostRole host;
	protected Restaurant5CookRole cook;
	private Restaurant5Cashier cashier;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atTable1 = new Semaphore(0,true);
	protected Semaphore atLobby = new Semaphore(0,true);
	public Semaphore atKitchen = new Semaphore(0, true);
	protected boolean atlobbycurrently = true;
	public String currentfood; 
	public Restaurant5Check currentcheck;
	public boolean foodout = false;
	PersonAgent person;

	ActivityTag tag = ActivityTag.RESTAURANT5WAITER;

	Restaurant5Menu menu = new Restaurant5Menu();
	Restaurant5 restaurant5 = new Restaurant5();
	OrderSpindle5 orderspindle;

	public Restaurant5WaiterGui waiterGui = null;

	public int xCoordinate;
	public int yCoordinate;

	public Restaurant5WaiterRole(String name, PersonAgent person) {
		super();
		building = "rest5";

		this.name = name;
		this.person = person;

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

	public void msgAtTable() {

		atTable.release();
		person.stateChanged();
	}

	public void msgAtLobby() {

		atlobbycurrently = true;
		atLobby.release();
		person.stateChanged();
	}

	public void msgAssignMeCustomer(Restaurant5Customer customer, int table)
	{
		customer.setWaiter(this);
		Do("Assign me customer and seat him at table " + table  );
		Do(" customer " + customer);
		Do("person " + person);
		customers.add(new mycustomer(customer, table));
		Do("customers size " + customers.size());
		person.stateChanged();
	}

	public void msgReadyToOrder(Restaurant5Customer customer)
	{
		synchronized(customers){

			log("customer " + customer.getName() + " is ready to order");
			for(mycustomer findcustomer: customers) {
				if(findcustomer.customer == customer) {
					//log("searching customer");
					findcustomer.state = customerstate.ReadyToOrder; 
					person.stateChanged();
					return; 
				}
			}

		}

	}


	public void msgReadyToPay(Restaurant5Customer customer)
	{
		synchronized(customers){

			log("customer " + customer.getName() + " is ready to pay");
			for(mycustomer findcustomer: customers) {
				if(findcustomer.customer == customer) {
					//log("searching customer");
					findcustomer.state = customerstate.ReadyToPay; 
					person.stateChanged();
					return; 
				}
			}

		}
	}


	public void msgGiveOrder(String order, int table)
	{
		log("Received order! " + order);
		orders.add(new orders(order, table));
		log("order added to the list");
		//This is the new customer order system
		
		synchronized(customers)
		{

			for(mycustomer customer : customers){
				if(customer.table == table)
				{
					customer.state = customerstate.Ordered;
					if(customer.state == customerstate.Ordered){
						//log("Ordered.");
					}
					customer.choice = order;
					person.stateChanged();
					break;
					//return;
				}	
			}

		}
		//person.stateChanged();
		
	}

	public void msgFoodIsOut(String order, int table) {
		log("msgFoodIsOut");

		synchronized(customers)
		{

			for(mycustomer tellcustomerfoodisout: customers) {
				if(tellcustomerfoodisout.table == table)
				{ 
					tellcustomerfoodisout.state = customerstate.Reorder;
					person.stateChanged();
				}

			}

		}


	}


	public void msgFoodIsReady(String order, int table) {

		log("msgfoodisready");
		synchronized(customers){

			for(mycustomer givefoodtocustomer: customers) {
				if(givefoodtocustomer.table == table)
				{ 
					Do("customer who is waiting for food found!");
					givefoodtocustomer.state = customerstate.FoodReady;
					Do("customer id " + givefoodtocustomer.customer + " table " + givefoodtocustomer.table + " state " + givefoodtocustomer.state);
					person.stateChanged();
				}

			}

		}
	}

	public void msgCheckIsReady(Restaurant5Check check) {

		log("msgcheckisready");

		synchronized(customers){

			for(mycustomer givechecktocustomer: customers) {
				if(givechecktocustomer.table == check.assignedtable)
				{ 
					givechecktocustomer.state = customerstate.CheckReady;
					checks.add(check);
					person.stateChanged();
				}

			}

		}


	}


	public void msgcustomerleft(Restaurant5Customer customerleft)
	{

		synchronized(customers){
			log("customer " + customerleft.getName() + " is leaving without eating");
			for(mycustomer findcustomer: customers) {
				if(findcustomer.customer == customerleft) {
					//log("searching customer");
					findcustomer.state = customerstate.LeavingWithoutEating; 
					person.stateChanged();
					return; 
				}
			}

		}
	}

	public void msgCustomerIsGone(Restaurant5Customer customer) {
		log("removing customer");
		synchronized(customers){

			for(mycustomer removecustomer: customers) {
				if(removecustomer.customer == customer)
				{
					Do("set customer state to leaving!");
					removecustomer.state = customerstate.Leaving;
					person.stateChanged();
				}
			}

		}

	}

	public boolean pickAndExecuteAnAction() {
		
		Do("I'm in the waiter scheduler!!!");
		try
		{

			if(!customers.isEmpty())
			{

				for (mycustomer customer : customers) {

					Do("!!!!!!!!!!   customer size " + customers.size());
					Do("<<<<<<<<<<<  current customer id " + customers.get(0).customer + " state " + customers.get(0).state);


					if(customer.state == customerstate.Waiting)
					{
						if(atlobbycurrently == true) {
							atlobbycurrently = false;
							customer.state = customerstate.Seated;
							seatCustomer(customer);
							return true;

						}
					}
				
					if(customer.state == customerstate.ReadyToOrder)
					{

						if(customer.waiterattable == false) {	

							atlobbycurrently = false;
							DoGoToTable(customer.table);
							log("moving to table " + customer.table);

							try {

								log("" + atTable.availablePermits());
								atTable.acquire();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							log("at table");
							TakeOrder(customer);
							customer.state = customerstate.WaitingForFood;
							waiterGui.atTable = false;

							return true;
						}

					}
		
					if(customer.state == customerstate.Ordered)
					{
						foodout = false;
						log("bring order to cook");
						customer.state = customerstate.WaitingForFood;
						BringOrderToCook(customer);
						return true;
					}
			
					if(customer.state == customerstate.FoodReady /*&& atlobbycurrently == true*/)
					{	
						log("bring food!!!");
						String order = customer.choice ;
						currentfood = order;
						BringFoodToCustomer(order, customer.table);	
						return true; 
					}

					if(customer.state == customerstate.Reorder && atlobbycurrently == true)
					{
						foodout = true; 
						log("food is out");
						TellCustomerFoodIsOut(customer.choice, customer.table);
						return true;

					}
				
					if(customer.state == customerstate.ReadyToPay)
					{
						if(customer.waiterattable == false) {	
							int table = customer.table;
							String choice = customer.choice;

							cashier.msgMakeCheckForWaiter(customer.customer,choice, table, this);
							customer.state = customerstate.WaitingForCheck;
						
							return true;	

						}
					}
		

					if(customer.state == customerstate.CheckReady && atlobbycurrently == true)
					{
						//log("bring check!!!");
						for(Restaurant5Check findcheck: checks)
						{
							if(findcheck.customer == customer)
							{
								BringCheckToCustomer(currentcheck, customer.table);
								checks.remove(findcheck);
								return true;
							}

						}

					}
				
					if(customer.state == customerstate.CheckReady)
					{
						if(customer.waiterattable == false) {	


							//cashier.msgMakeCheckForWaiter(customer.customer, this);
							atlobbycurrently = false;
							DoGoToTable(customer.table);

							try {

								//log("" + atTable.availablePermits());
								atTable.acquire();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						
							GiveCheckToCustomer(customer);
							customer.state = customerstate.GoingToCashier;

							return true;	

						}
					}

					if(customer.state == customerstate.GoingToCashier)
					{
						//waiterGui.gotohomeposition();
						GoBackAfterCustomerGotCheck();
						customer.state = customerstate.Leaving;
						return true;

					}
			
					if(customer.state == customerstate.LeavingWithoutEating)
					{
						foodout = false;
						//log("customer left so follow him");
						atlobbycurrently = true;	
						Dogobacktolobby();
						customers.remove(customer);
						host.msgCustomerleftTable(customer.customer);
						log("" + state);
						return true;
					}
			
					if(customer.state == customerstate.Leaving)
					{
						//log("" + customer.customer.getName() + "removed");
						host.msgCustomerleftTable(customer.customer);
						customers.remove(customer);
						return true;
					}				

					waiterGui.gotohomeposition();
				}


			}



			return false;
		}catch(ConcurrentModificationException e) {

			return false;
		}
	}

	// Actions

	private void seatCustomer(mycustomer customer) {

		customer.customer.msgSitAtTable(customer.table);
		DoSeatCustomer((Restaurant5CustomerRole)customer.customer, customer.table);
		//Do("i have attable permit " + atTable.availablePermits());
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		Dogobacktolobby();
		try {
			atLobby.acquire();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}		

	}

	private void DoSeatCustomer(Restaurant5CustomerRole customer, int table) {
	
		log("Seating " + customer + " at " + table);
		waiterGui.DoBringToTable(customer, table); 
	}

	private void DoGoToTable(int table) {
		log("Going to table:" + table);
		waiterGui.DoGoToTable(table);
	}

	private void TakeOrder(mycustomer customer) {
		Do("i'm taking order");
		customer.customer.msgTakeOrder(this);
	}

	private void AskCashierForCustomerCheck(mycustomer customer) {
		Do("I'm asking the cahsier for " + customer.customer.getName() + "'s check");


	}

	private void GiveCheckToCustomer(mycustomer customer) {	
		Restaurant5Check customercheck = new Restaurant5Check(customer.customer, menu.m.get(customer.choice), customer.table);
		customer.customer.msgReceivedCheckFromWaiter(customercheck);
	}


	public abstract void BringOrderToCook(mycustomer customer);
	
	
	/*
		Dogotokitchen();
		try {
			atKitchen.acquire();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}

		cook.msgReceviedOrderFromWaiter(this, customer.choice, customer.table);


		Dogobacktolobby();
		atlobbycurrently=false;
		//log("NUMBER OF PERMITS = " + atLobby.availablePermits());
		try {
			atLobby.acquire();
			//atLobby.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		atlobbycurrently = true;

	
	*/
	private void GoBackAfterCustomerGotCheck()
	{
		Dogobacktolobby();
		atlobbycurrently=false;
		//log("NUMBER OF PERMITS = " + atLobby.availablePermits());
		try {
			atLobby.acquire();
			//atLobby.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.gotohomeposition();
		atlobbycurrently = true;
	}

	// this part is tricky
	private void TellCustomerFoodIsOut(String order, int table) {
		log("Tell customer that the food is out");
		//DoGoToTable(table);


		for (mycustomer customer : customers) {	
			if(customer.table == table) {
				//log("find customer " + customer.customer.getName());
				this.state = AgentState.FoodIsOut;
				DoGoToTable(table);
				try {
					//atTable.acquire();
					atTable.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				customer.customer.msgFoodIsOut(this, customer.choice);
				atlobbycurrently = false;

				//return;

			}
		}



	}

	public void BringFoodToCustomer(String order, int table) {

		log("bringing " + order + " to table " + table);


		for (mycustomer customer : customers) {

			if(customer.table == table) {

				//log("bring to table:" + table);

				Dogotokitchen();
				try {
					atKitchen.acquire();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}

				cook.msgPickedUpFoodFromTheKitchen(this, customer.choice, customer.table);
				this.state = AgentState.BringingFood;
				//Do("1. I have "+atTable.availablePermits() +" permits");
				DoGoToTable(table);
				//Do("2. I have "+atTable.availablePermits() +" permits");
				try {
					atTable.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Do("3. I have "+atTable.availablePermits() +" permits");
				customer.customer.msgHereIsYourFood(this);
				customer.state = customerstate.Eating; 
				Dogobacktolobby();
				try {
					atLobby.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				atlobbycurrently = true;
			}
		}


	}

	public void BringCheckToCustomer(Restaurant5Check returncustomercheck, int table) {
		log("bringing check: " + returncustomercheck.total + " to table " + table);

		for (mycustomer customer : customers) {

			if(customer.table == table) {

				//log("bring to table:" + table);
				this.state = AgentState.BringingCheck;
				DoGoToTable(table);
				try {

					atTable.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				customer.customer.msgReceivedCheckFromWaiter(returncustomercheck);
				customer.state = customerstate.GoingToCashier; 
				atlobbycurrently = true;
				waiterGui.gotohomeposition();

			}
		}


	}


	protected void Dogobacktolobby(){

		//log("going back to lobby");
		waiterGui.DoLeaveCustomer();
		state = AgentState.DoingNothing;
		host.msgWaiterBackInLobby(this);

	}

	protected void Dogotokitchen() {
		waiterGui.GoToKitchen();

	}

	public void GoOnBreak() {
		host.msgWaiterWantBreak(this);

	}

	public void ComeBackFromBreak() {
		host.msgWaiterComeBackFromBreak(this);

	}

	private void removecustomer(Restaurant5Customer customer) {

		for(mycustomer removecustomer: customers) {
			if(removecustomer.customer == customer)
			{
				Do("set customer state to leaving!");
				removecustomer.state = customerstate.Leaving;
			}
		}
	}



	public AgentState getWaiterState() {
		return state; 
	}


	public void setGui(Restaurant5WaiterGui gui) {
		waiterGui = gui;
	}

	public Restaurant5WaiterGui getGui() {
		return waiterGui;
	}


	//mycustomer class to store all of waiters customers
	public static class mycustomer {
		Restaurant5Customer customer;
		int table;
		String choice;
		customerstate state = customerstate.Waiting;
		boolean waiterattable;

		public mycustomer(Restaurant5Customer customer, int table)
		{
			this.customer = customer;
			this.table = table;
		}

		public int returntablenumber()
		{
			return table;
		}

	}

	// order class for all of waiters orders
	private static class orders {
		String foodname;
		int table;

		public orders(String foodname, int table)
		{
			this.foodname = foodname;
			this.table = table;
		}

	}

	private void log(String msg){
		print(msg);
		ActivityLog.getInstance().logActivity(tag, msg, name, false);
	}

	public void setCook(Restaurant5CookRole setcook) {
		this.cook = setcook;

	}

	public void setCashier(Restaurant5CashierRole setcashier) {
		this.cashier = setcashier;

	}

	public void setHost(Restaurant5HostRole sethost) {
		this.host = sethost;

	}

	@Override
	public String getRoleName() {
		return roleName;
	}

	public void setOrderSpindle(OrderSpindle5 setorderspindle) {
		orderspindle = setorderspindle;
	}
	
	public PersonAgent getPerson(){
		return person;
	}

}


