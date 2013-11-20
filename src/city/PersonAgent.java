package city;

import interfaces.Restaurant2Customer;
import interfaces.Restaurant2Host;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

import city.gui.PersonGui;
import city.transportation.BusStopAgent;
import city.transportation.CarAgent;
import Role.Role;
import agent.Agent;
import astar.AStarNode;
import astar.AStarTraversal;
import astar.Position;

public class PersonAgent extends Agent{
	
	//DATA
	String name;
	
	public List<String> events = Collections.synchronizedList(new ArrayList<String>());
	public List<String> foodsToEat = new ArrayList<String>();
	
	List<Role> roles;
	enum PersonState {idle, hungry, choosingFood, destinationSet, payRent};
	PersonState state;
	HouseAgent house;
	List<Restaurant> restaurants;
	Restaurant recentlyVisitedRestaurant; 	//so the person won’t go there twice in a row
	CarAgent car;
	String destination;
	enum TransportationState{takingCar, takingBus, walking, chooseTransport};
	TransportationState transportationState;
	CityMap cityMap;
	BusStopAgent busStop;
	//List<MyMeals> meals;
	enum FoodState {cooking, done};
	//List<MyAppliances> appliancesToFix;
	enum ApplianceState {broken, beingFixed, Fixed};
	PersonAgent landlord;
	//List<Order> recievedOrders;   //orders the person has gotten that they need to deal with
	//List<MarketAgent> markets;
	List<String> groceryList;
	//List<Bills> billsToPay;
	double takeHome; 		//some amount to take out of every paycheck and put in wallet
	double wallet;
	double moneyToDeposit;
	//BankAgent bank;
	//BankerRole bankTeller;
	enum BankState {none, deposit, withdraw, loan};   //so we know what the person is doing at the bank
	BankState bankState;
	Boolean firstTimeAtBank = true;	//determines whether person needs to create account
	int accountNumber;
	
	Semaphore atDestination = new Semaphore(0, true);
	AStarTraversal aStar;
    Position currentPosition; 
    Position originalPosition;
    
	PersonGui gui;
	

	public PersonAgent(String n, AStarTraversal aStarTraversal){
		super();
		
		name = n;
		this.aStar = aStarTraversal;
		currentPosition = new Position(40, 35);
        currentPosition.moveInto(aStar.getGrid());
        originalPosition = currentPosition;//save this for moving into
        
        cityMap = new CityMap();
		
		//populate foods list -- need to make sure this matches up with market
		foodsToEat.add("Chicken");
		foodsToEat.add("Steak");
		foodsToEat.add("Salad");
		foodsToEat.add("Pizza");
		
		msgImHungry();
		
	}
	
	public void msgAtDestination() {
		atDestination.release();
	}
	
	public void setGui(PersonGui g){
		gui = g;
	}
	
	public void addRole(Role r){
		roles.add(r);
	}
	
	//MESSAGES
	public void msgImHungry(){	//sent from GUI ?
		events.add("GotHungry");
		stateChanged();
	}
	
	public void msgImBroken(String type) {
		// TODO Auto-generated method stub
		
	}

	public void msgItemInStock(String type) {
		// TODO Auto-generated method stub
		
	}

	public void msgDontHaveItem(String type) {
		// TODO Auto-generated method stub
		
	}

	public void msgFoodDone(String type) {
		// TODO Auto-generated method stub
		
	}
	
	public void msgRentDue(double rate) {
		// TODO Auto-generated method stub
		
	}

	public void msgFixed(String string) {
		// TODO Auto-generated method stub
		
	}
	
	
	//SCHEDULER
	protected boolean pickAndExecuteAnAction() {
		
		//Uncomment this and create people named a, b, c, and d to see basic animation.
		//movementTest();
		
		moveTo(40,25);
		
		synchronized(events){
			for(String e : events){
				if(e.equals("GotHungry")){
					Eat();
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	//ACTIONS
	
	public void Eat(){	//hacked for now so that it randomly picks eating at home or going out
		synchronized(events){
			for(String e : events){
				if(e.equals("GotHungry")){
					events.remove(e);
					break;
				}
			}
		}
		Random rand = new Random();
		int x = rand.nextInt(1);
		if(x == 1){
			int y = rand.nextInt(foodsToEat.size());
			String food = foodsToEat.get(y);
			//house.msgCheckFridge(food);
			print("I'm going to eat " + food);
		}
		else{
			goToRestaurant();
		}
	}
	
	public void movementTest() {
		if(name.equals("a"))
		moveTo(40, 25);
		
		if(name.equals("b"))
			moveTo(39, 23);
		
		if(name.equals("c"))
			moveTo(40, 21);
		
		if(name.equals("d"))
			moveTo(39, 15);
	}
	
	public void goToRestaurant(){
		print("Going to go to a restaurant");
		Restaurant restaurant2 = new Restaurant();
		//restaurant2.host.msgIWantFood(restaurant2.customer);
		gui.goToRestaurant(2);
	}
	
	void moveTo(int x, int y) {
		Position p = new Position(x, y);
		guiMoveFromCurrentPositionTo(p);
	}
	
	void DoGoTo(String location) {
		int x = cityMap.getX(location);
		int y = cityMap.getY(location);
		
		gui.moveTo(x * 20 - 20, y * 20 - 20);
	    
	    //Give animation time to move to square.
	    try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void guiMoveFromCurrentPositionTo(Position to){
		//System.out.println("[Gaut] " + guiWaiter.getName() + " moving from " + currentPosition.toString() + " to " + to.toString());

		AStarNode aStarNode = (AStarNode)aStar.generalSearch(currentPosition, to);
		List<Position> path = aStarNode.getPath();
		Boolean firstStep   = true;
		Boolean gotPermit   = true;

		for (Position tmpPath: path) {
		    //The first node in the path is the current node. So skip it.
		    if (firstStep) {
			firstStep   = false;
			continue;
		    }

		    //Try and get lock for the next step.
		    int attempts    = 1;
		    gotPermit       = new Position(tmpPath.getX(), tmpPath.getY()).moveInto(aStar.getGrid());

		    //Did not get lock. Lets make n attempts.
		    while (!gotPermit && attempts < 3) {
			//System.out.println("[Gaut] " + guiWaiter.getName() + " got NO permit for " + tmpPath.toString() + " on attempt " + attempts);

			//Wait for 1sec and try again to get lock.
			try { Thread.sleep(1000); }
			catch (Exception e){}

			gotPermit   = new Position(tmpPath.getX(), tmpPath.getY()).moveInto(aStar.getGrid());
			attempts ++;
		    }

		    //Did not get lock after trying n attempts. So recalculating path.            
		    if (!gotPermit) {
			//System.out.println("[Gaut] " + guiWaiter.getName() + " No Luck even after " + attempts + " attempts! Lets recalculate");
			guiMoveFromCurrentPositionTo(to);
			break;
		    }

		    //Got the required lock. Lets move.
		    //System.out.println("[Gaut] " + guiWaiter.getName() + " got permit for " + tmpPath.toString());
		    currentPosition.release(aStar.getGrid());
		    currentPosition = new Position(tmpPath.getX(), tmpPath.getY ());
		    print("Moving to " + currentPosition.getX() + ", " + currentPosition.getY());
		    gui.moveTo(currentPosition.getX() * 20 - 20, currentPosition.getY() * 20 - 20);
		    
		    //Give animation time to move to square.
		    try {
				atDestination.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*
		boolean pathTaken = false;
		while (!pathTaken) {
		    pathTaken = true;
		    //print("A* search from " + currentPosition + "to "+to);
		    AStarNode a = (AStarNode)aStar.generalSearch(currentPosition,to);
		    if (a == null) {//generally won't happen. A* will run out of space first.
			System.out.println("no path found. What should we do?");
			break; //dw for now
		    }
		    //dw coming. Get the table position for table 4 from the gui
		    //now we have a path. We should try to move there
		    List<Position> ps = a.getPath();
		    Do("Moving to position " + to + " via " + ps);
		    for (int i=1; i<ps.size();i++){//i=0 is where we are
			//we will try to move to each position from where we are.
			//this should work unless someone has moved into our way
			//during our calculation. This could easily happen. If it
			//does we need to recompute another A* on the fly.
			Position next = ps.get(i);
			if (next.moveInto(aStar.getGrid())){
			    //tell the layout gui
			    guiWaiter.move(next.getX(),next.getY());
			    currentPosition.release(aStar.getGrid());
			    currentPosition = next;
			}
			else {
			    System.out.println("going to break out path-moving");
			    pathTaken = false;
			    break;
			}
		    }
		}
		*/
	    }
	
	
	//CLASSES
	class Restaurant{
		Restaurant2Host host;	//HACK for testing: TODO: fix this
		Restaurant2Customer customer;	//HACK for testing: TODO: fix this
		
		public Restaurant(){
			//nothing yet
		}
		
		public void setHost(Restaurant2Host h){
			host = h;
		}
		
		public void setCustomer(Restaurant2Customer c){
			customer = c;
		}
	}

}
