package city.Restaurant5;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.gui.Restaurant5.Restaurant5CustomerGui;
import tomtesting.interfaces.Restaurant5Cashier;
import tomtesting.interfaces.Restaurant5Host;
import tomtesting.interfaces.Restaurant5Customer;
import tomtesting.interfaces.Restaurant5Waiter;

//import tomtesting.test.mock.EventLog;
//import restaurant.test.mock.LoggedEvent;
import test.mock.EventLog;
import test.mock.LoggedEvent;
import Role.Role;
import agent.Agent;

/**
 * Restaurant customer agent.
 */

// this is the new updated file////
public class Restaurant5CustomerRole extends Role implements Restaurant5Customer {
	
	
	public String name;
	private int hungerLevel = 10;// determines length of meal
	private int orderingtime = 5;
	private int lookingatchecktime = 3;
	Timer timer = new Timer();
	Timer timerforordering = new Timer();
	private Restaurant5CustomerGui customerGui;
	public int xcoordinate;
	public int ycoordinate;

	public String choice;
	public boolean eating = false; // this is for the graphic
	public boolean readytoorder = false; //this is for the graphic
	public boolean ordered = false; // this is for the graphic
	public boolean reordered = false; //this is for the graphic
	public boolean readyforcheck = false; //this is for the graphic
	public Semaphore atLobby = new Semaphore(0,true);
	public Semaphore atWashingDishes = new Semaphore(0,true);
	public int xcoordinateofwaitingspot;
	public int ycoordinateofwaitingspot;
	
	
	// agent correspondents
	private Restaurant5Host host;
	private Restaurant5Waiter waiter;
	private Restaurant5Cashier cashier;
	String Customersorder = ""; 
	public boolean goingtocashier = false;
	int table;
	int currentmoney;
	int paybackmoney;
	Restaurant5Check mycheck;
	Restaurant5Menu menu = new Restaurant5Menu();
	private boolean scumbagnexttime = false; //this is for non-normative scenario
	PersonAgent person;
	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, WaitingInWaitingArea, BeingSeated, Seated, WaitingForWaiter, WaitingForFood, Eating, DoneEating, Paying, LookingAtCheck, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state

	public enum AgentEvent 
	{none, gotHungry, goToWaitingArea, followHost, seated, lookingAtMenu, readyToOrder, waitingForWaiter, ordering, reordering, doneOrdering, gotFood, doneEating, readyToPay, waitingToPay, receivedCheck, goToCashier, donePaying, washDishes, doneLeaving, doneLeavingWithoutEating, noMoneyLeave};
	AgentEvent event = AgentEvent.none;

	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public Restaurant5CustomerRole(String name, PersonAgent person){
		super();
		building = "rest5";
		
		
		if(name.equals("cheap"))
		{
			this.currentmoney = 2;
		}
		else if(name.equals("beggar"))
		{
			this.currentmoney = 0;
		}
		else if(name.equals("scumbag"))
		{
			this.currentmoney = 1;
		}
		else
		{
			this.currentmoney = 6;
		}
		this.name = name;
		this.person = person;
		
	}

	/**
	 * hack to establish connection to Host agent.
	 */
	public void setHost(Restaurant5Host host) {
		this.host = host;
	}
	
	public void setWaiter(Restaurant5Waiter waiter) {
		this.waiter = waiter;
	}
	
	public void setCashier(Restaurant5Cashier cashier) {
		this.cashier = cashier;
	}

	public String getCustomerName() {
		return name;
	}
	
	public int getxcooridnate() {
		return this.xcoordinate;
	}
	
	public int getycooridnate() {
		return this.ycoordinate;
	}
	
	// Messages

	public void gotHungry() {
		print("I'm hungry");
		event = AgentEvent.gotHungry;
		person.stateChanged();
	}
	
	public void msgGoToWaitingArea(int xcoordinateofwaitingspot, int ycoordinateofwaitingspot) {
		//print("I'm going to waiting spot");
		event = AgentEvent.goToWaitingArea;
		this.xcoordinateofwaitingspot = xcoordinateofwaitingspot;
		this.ycoordinateofwaitingspot = ycoordinateofwaitingspot;
		person.stateChanged();
	}

	public void msgSitAtTable(int table) {
		print("Received msgSitAtTable");
		event = AgentEvent.followHost;
		this.table = table;
		person.stateChanged();
		
	}
	
	public void msgSetHomePos(int x, int y) {
		xcoordinate = x;
		ycoordinate = y;
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		person.stateChanged();
	}
	
	public void msgTakeOrder(Restaurant5Waiter waiter) {
		event = AgentEvent.ordering;
		person.stateChanged();
	}
	
	public void msgHereIsYourFood(Restaurant5Waiter waiter) {
		event = AgentEvent.gotFood;
		person.stateChanged();
	}
	
	public void msgFoodIsOut(Restaurant5Waiter waiter, String order) {
		event = AgentEvent.reordering;
		print("waiter state" + state);
		person.stateChanged();
	}
	
	public void msgReceivedCheckFromWaiter(Restaurant5Check checkfromwaiter) {
		mycheck = new Restaurant5Check(checkfromwaiter.customer, checkfromwaiter.total, checkfromwaiter.assignedtable);
		mycheck.amountcustomerpaid = currentmoney;
		event = AgentEvent.receivedCheck;
		print("total: $" + mycheck.total + " table: " + mycheck.assignedtable);		
		print("received check from the waiter");
		person.stateChanged();
	}
	
	public void msgReceivedMoneyFromCashier(int moneyleftfromeating) {
		
		
		currentmoney = moneyleftfromeating;
		currentmoney -= paybackmoney;
		paybackmoney = 0;
		state = AgentState.DoingNothing;
		if(currentmoney > 0)
		{
			event = AgentEvent.doneLeaving;
		}
		person.stateChanged();
		
	}
	
	public void msgDontHaveMoneyPayBackLater( int paybackmoney) {
		currentmoney = 0;
		this.paybackmoney = paybackmoney;
		print("has to pay back this much: $" + this.paybackmoney);
		event = AgentEvent.doneLeaving;
		person.stateChanged();
	}
	
	public void msgDontHaveMoneyWashDishes() {
		event = AgentEvent.washDishes;
		person.stateChanged();
	}
	
	public void msgRestaurantFullLeave() {
		print("restaurant is full so I'm leaving");
		state = AgentState.DoingNothing;
		person.stateChanged();
	}
 	
	public void msgAnimationFinishedLeaveRestaurant() {
		event = AgentEvent.doneLeaving;
		person.stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
		//customer gets hungry and goes to restaurant
		
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ) {
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			
			return true;
		}
		
		if(state == AgentState.WaitingInRestaurant && event == AgentEvent.goToWaitingArea) {
			//Do("im in the if st");
			//state = AgentState.WaitingInWaitingArea;
			GoToWaitingArea();
			return true;
		}
		
		//customer is being seated
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followHost ){
			state = AgentState.BeingSeated;
			SitDown();
		    return true;
		}
		//customer looks at the menu
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Seated;
			LookingAtMenu();
			return true;
		}
		//customer calls the waiter
	    if(state == AgentState.Seated && event == AgentEvent.readyToOrder)
	    {	
	    	state = AgentState.WaitingForWaiter;
	    	CallWaiter();
	    	readytoorder = true;
	    	order();
	    	//start here
	    	return true;
	    }
	    
	    if(state == AgentState.WaitingForFood && event == AgentEvent.reordering)
	    {
	    	print("reordering");
	    	readytoorder = false;
	    	ordered = true;
	    	reorder(this.choice, this.table);
	    	//TellOrder(this.choice, this.table);
	    	return true;
	    }
	    
	    //customer tells his/her order
	    if(state == AgentState.WaitingForWaiter && event == AgentEvent.ordering)
	    {
	    	state = AgentState.WaitingForFood;
	    	readytoorder = false;
	    	ordered = true;
	    	print("" + this.name + "i'm telling order");
	    	TellOrder(Customersorder, table);
	    	return true;
	    	
	    }
	    //customer is eating
	    if(state == AgentState.WaitingForFood && event == AgentEvent.gotFood) {
	    	ordered = false;
	    	state = AgentState.Eating;
	    	EatFood();
	    	return true;
	    }
	    
	    //the original code from customer eating to leaving
	    /*
	     if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.Leaving;
			leaveTable();
			return true;
		}
		
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			waiter.msgCustomerIsGone(this);
			return true;
		}
	    */
	    
	    // this is the new code where I implement waiter getting check for the customer
	    //customer is leaving
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.DoneEating;
			callWaiterForCheck();
			return true;
		}
		
		 if(state == AgentState.DoneEating && event == AgentEvent.receivedCheck) {
			 state = AgentState.LookingAtCheck;
			 lookingAtCheck();
			 return true;
		    
		 }
		 
		 if(state == AgentState.LookingAtCheck && event == AgentEvent.goToCashier) {
			 state = AgentState.Leaving;
		     leaveTable();
			 return true;
			 
		 }
		 
		 if(state == AgentState.Leaving && event == AgentEvent.washDishes) {
			 
			 
			 
			 return true;
		 }
		   
		//customer state is changed to doingnothing
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving) {
			state = AgentState.DoingNothing;
			payCashier();
			//waiter.msgCustomerIsGone(this);
			//leaveTableWithoutEating();
			return true;
		}
		
		if (state == AgentState.Leaving && event == AgentEvent.doneLeavingWithoutEating) {
			state = AgentState.DoingNothing;
			//waiter.msgCustomerIsGone(this);
			leaveTableWithoutEating();
			return true;
		}
		
		if (state == AgentState.Seated && event == AgentEvent.noMoneyLeave) {
			state = AgentState.Leaving;
			//waiter.msgCustomerIsGone(this);
			leaveTableNoMoney();
			return true;
		}
		
		
		
		return false;
	}

	// Actions

	private void goToRestaurant() {
		Do("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
		customerGui.gotohomeposition();
		if(this.name.equals("scumbag") && scumbagnexttime == true)
			currentmoney = 10;
	}
	
	private void GoToWaitingArea() {
		//Do("i'm telling the graphics to wait");
		customerGui.DoGoToWait(this.xcoordinateofwaitingspot, this.ycoordinateofwaitingspot);
	}

	private void SitDown() {
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat(1,table);//hack; only one table
	}
	
	private void LookingAtMenu() {
		Do("Looking at menu");
		timerforordering.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
			//print("ordering, cookie=" + cookie);
			event = AgentEvent.readyToOrder;
			if(menu.m.get("chicken") > currentmoney && menu.m.get("burrito") > currentmoney && menu.m.get("pizza") >currentmoney && !name.equals("scumbag"))
			{
				//state = AgentState.Leaving;
				event = AgentEvent.noMoneyLeave;
			}
			
			person.stateChanged();
			}
			
		},
		getOrderingLevel() * 1000);
		
	}
	
	private void CallWaiter() {
		Do("Call Waiter!");
		event = AgentEvent.waitingForWaiter;
		waiter.msgReadyToOrder(this);
	}
	
	private void callWaiterForCheck() {
		Do("Call waiter for check!");
		event = AgentEvent.readyToPay;
		readyforcheck = true; 
		waiter.msgReadyToPay(this);
	}
	
	private void order() {
		Restaurant5Menu menu = new Restaurant5Menu();
		
		if(this.name.equals("cheap"))
		{
			print("i'm cheap so I'm getting the cheapest food: chicken");
			Customersorder = "chicken";
			choice = Customersorder;
		}
		else
		{
			Random r = new Random();
			int i = r.nextInt(3); 
			if(i == 0)
			{
				Customersorder = "chicken"; /*menu.m.get("chicken");*/
				print("" + name + " got chicken");
			}
			else if(i == 1)
			{
				Customersorder = "burrito"; /*menu.m.get("burrito");*/
				print("" + name + " got burrito");
			}
			else if(i == 2)
			{
				Customersorder = "pizza"; /*menu.m.get("pizza");*/
				print("" + name + " got pizza");
			}
			choice = Customersorder;
		}
	}
	


	private void reorder(String previousorder, int table) {

//    	CallWaiter();
    	//readytoorder = true;
    	//start here
		
		print("reorder");
		if(previousorder == "chicken")
		{
			menu.m.remove("chicken");
			print("chicken removed");
			
		}
		
		else if(previousorder == "burrito")
		{
			menu.m.remove("burrito");
			print("burrito removed");
			
		}
		
		else if(previousorder == "pizza")
		{
			menu.m.remove("pizza");
			print("pizza removed"); 
			
		}
		
		
    	Random r = new Random();
    	//int i = r.nextInt(menu.m.size()); 
    	//do while loop
    	//print("this is hash map size" + menu.m.size());
    	
    	if(menu.m.size() == 0)
    	{
    		//print("im in the if statement where the customer leaves");
        	state = AgentState.Leaving;
        	event = AgentEvent.doneLeavingWithoutEating;
        	ordered = false;
        	waiter.msgcustomerleft(this);
        	
    	}
    	else if(name.equals("cheap") && previousorder == "chicken")
    	{
    		Do("cheap is leaving without eating");
    		state = AgentState.Leaving;
        	event = AgentEvent.doneLeavingWithoutEating;
        	ordered = false;
        	waiter.msgcustomerleft(this);
    	}
    	else
    	{
    	
    	
    	for (String key : menu.m.keySet()) {
    		System.out.println("Key = " + key);
    		
    		if(key == "chicken")
    		{
    			Customersorder = "chicken";
    		}
    		
    		else if(key == "burrito")
    		{
    			Customersorder = "burrito";
    		}
    		
    		else if(key == "pizza")
    		{
    			Customersorder = "pizza";
    		}
    		
    	}
 
    		choice = Customersorder;
    		TellOrder(choice, table);
    	
    	}
    	
    	
    	
	}
	
	private void TellOrder(String order, int table) {
		Do("Giving order");
		event = AgentEvent.doneOrdering;
	    waiter.msgGiveOrder(order, table);
	}
	

	private void EatFood() {
		Do("Eating Food");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		eating = true;
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				eating = false;
				print("Done eating" /*cookie=" + cookie*/);
				event = AgentEvent.doneEating;
				person.stateChanged();
			}
		},
		getHungerLevel() * 1000);//how long to wait before running task
	}
	 
	private void lookingAtCheck() {
		Do("Looking at check");
		readyforcheck = false;
		print("mycheck: amount to pay $" + mycheck.total);
		timerforordering.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
			//print("ordering, cookie=" + cookie);
			event = AgentEvent.goToCashier;
			person.stateChanged();
			}
			
		},
		getLookingAtCheckTime() * 1000);
		
	}
	
	

	private void leaveTable() {
		//Do("Leave table function");
		readyforcheck = false;
		waiter.msgCustomerIsGone(this);
		goingtocashier = true;
		customerGui.DoExitRestaurant();
		try {
			atLobby.acquire();
			//atLobby.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(paybackmoney > 0) {
			
			//currentmoney -= paybackmoney;
			//mycheck.amountcustomerpaid += paybackmoney;
			print("I'm paying the cahsier back from last time so " + "current total: $" + mycheck.total + " plus payback money: $" + paybackmoney);
			//paybackmoney = 0;
			
		}
		/*
		cashier.msgReceviedCheckFromCustomer(mycheck);
		if(this.name.equals( "scumbag"))
		{
			scumbagnexttime = true;
		}
		*/
	}
	
	private void payCashier() {
		if(!this.name.equals("beggar"))
		cashier.msgReceviedCheckFromCustomer(mycheck);
		if(this.name.equals( "scumbag"))
		{
			scumbagnexttime = true;
		}
	}
	
	
	private void leaveTableWithoutEating() {
		
		//waiter.msgCustomerIsGone(this);
		customerGui.DoExitRestaurant();
		menu.m.put("chicken", 2);
		menu.m.put("burrito", 3);
		menu.m.put("pizza", 4);
		try {
			atLobby.acquire();
			//atLobby.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiter.msgcustomerleft(this);
		
	}
	
	private void leaveTableNoMoney() {
		
		print(" I don't have enough money");
		customerGui.DoExitRestaurant();
		try {
			atLobby.acquire();
			//atLobby.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		event = AgentEvent.doneLeaving;
		waiter.msgCustomerIsGone(this);
			
	}
	
	
	private void washDishes() {
		customerGui.DoGoToWashDishes(100, 300);
		
		try {
			atWashingDishes.acquire();
			
		} catch(InterruptedException e) {
			
			e.printStackTrace();
		}
		
	}

	// Accessors, etc.

	public String getName() {
		return this.name;
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}
	
	public int getOrderingLevel() {
		return orderingtime;
	}
	
	public int getLookingAtCheckTime() {
		return lookingatchecktime;
	}
	
	public int gettablenumber() {
		return table;
	}
	public Restaurant5Waiter getwaiter() {
		return waiter;
	}
	public AgentState getwaiterstate() {
		return state;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(Restaurant5CustomerGui g) {
		customerGui = g;
	}

	public Restaurant5CustomerGui getGui() {
		return customerGui;
	}

	@Override
	public int getxcoordinate() {
		// TODO Auto-generated method stub
		return xcoordinate;
	}

	@Override
	public int getycoordinate() {
		// TODO Auto-generated method stub
		return ycoordinate;
	}

	


	

	
}

