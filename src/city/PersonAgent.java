package city;

import restaurant1.Restaurant1CustomerRole;
import test.mock.LoggedEvent;
import interfaces.Bus;
import interfaces.Car;
import interfaces.HouseInterface;
import interfaces.Landlord;
import interfaces.Person;
import interfaces.Restaurant2Customer;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

import test.mock.EventLog;
import city.Restaurant2.Restaurant2;
import city.Restaurant2.Restaurant2CustomerRole;
import city.Restaurant2.Restaurant2WaiterRole;
import city.Restaurant4.CustomerRole4;
import city.gui.BuildingPanel;
import city.gui.Gui;
import city.gui.PersonGui;
import city.gui.House.HomeOwnerGui;
import city.gui.restaurant2.Restaurant2CustomerGui;
import city.transportation.BusAgent;
import city.transportation.BusStopAgent;
import city.transportation.CarAgent;
import city.transportation.TruckAgent;
import Role.BankCustomerRole;
import Role.BankTellerRole;
import Role.LandlordRole;
import Role.Role;
import activityLog.ActivityLog;
import activityLog.ActivityTag;
import agent.Agent;
import astar.AStarNode;
import astar.AStarTraversal;
import astar.Position;

public class PersonAgent extends Agent implements Person{
	
	//DATA
	String name;
	public List<String> events = Collections.synchronizedList(new ArrayList<String>());
	public List<String> foodsToEat = new ArrayList<String>();
	public List<Role> roles = Collections.synchronizedList(new ArrayList<Role>());
	enum PersonState {idle, hungry, choosingFood, destinationSet, payRent};
	PersonState state;
	
	//House
	public HouseInterface house;
	public List<MyMeal> meals = Collections.synchronizedList(new ArrayList<MyMeal>());
	public enum FoodState {initial, cooking, done};
	List<MyAppliance> appliancesToFix = Collections.synchronizedList(new ArrayList<MyAppliance>());
	enum ApplianceState {broken, beingFixed, fixed};
	public Landlord landlord;
	boolean atHome= false;
	
	//Transportation
	CarAgent car;
	String destinationBuilding;
	enum TransportationState{takingCar, takingBus, walking, chooseTransport};
	TransportationState transportationState;
	CityMap cityMap;
	BusStopAgent busStop;
	int busStopToGetOffAt;
	BusAgent bus;
	public List<Car> cars = new ArrayList<Car>();
	public List<BusRide> busRides = Collections.synchronizedList(new ArrayList<BusRide>());
	public enum BusRideState {initial, waiting, busIsHere, onBus, done, paidFare, getOffBus};
	public List<CarRide> carRides = Collections.synchronizedList(new ArrayList<CarRide>());
	public enum CarRideState {initial, arrived};
	
	//Money
	public List<Bill> billsToPay = Collections.synchronizedList(new ArrayList<Bill>());
	double takeHome; 		//some amount to take out of every paycheck and put in wallet
	public double wallet;
	double moneyToDeposit;
	
	//Bank
	Bank bank;
	BankTellerRole bankTeller;
	enum BankState {none, deposit, withdraw, loan};   //so we know what the person is doing at the bank
	BankState bankState;
	Boolean firstTimeAtBank = true;	//determines whether person needs to create account
	double accountNumber;
	double accountBalance;
	List<BankEvent> bankEvents = Collections.synchronizedList(new ArrayList<BankEvent>());
	enum BankEventType {withrawal, deposit, loan, openAccount};
	
	//Other
	List<MarketOrder> recievedOrders = Collections.synchronizedList(new ArrayList<MarketOrder>());   //orders the person has gotten that they need to deal with
	List<String> groceryList = Collections.synchronizedList(new ArrayList<String>());
	int timeOfDay;
	enum TimeStatus {wakeUp, getReadyForWork, goToWork, atWork, leaveWork, nightTime};
	TimeStatus timeStatus = TimeStatus.wakeUp;
	//List<MarketAgent> markets;
	//List<Restaurant> restaurants;
	//Restaurant recentlyVisitedRestaurant; 	//so the person won't go there twice in a row
	
	//Testing
	public EventLog log = new EventLog();
	public boolean goToRestaurantTest = false;
	public boolean test = false;
	
	//Job
	public Job myJob;
	public enum WorkState {notWorking, goToWork, atWork};
	WorkState workState;
	
	String destination;
	Semaphore atDestination = new Semaphore(0, true);
	AStarTraversal aStar;
    Position currentPosition; 
    Position originalPosition;
    
	PersonGui gui;
	HomeOwnerGui homeGui;
	ActivityTag tag = ActivityTag.PERSON;

	public PersonAgent(String n, AStarTraversal aStarTraversal, CityMap map, HouseInterface h){
		super();
		
		name = n;
		this.house = h;
		this.aStar = aStarTraversal;
		homeGui= new HomeOwnerGui(this);
		
		if(house != null) {
			currentPosition = new Position(map.getX(house.getName()), map.getY(house.getName()));
		} else {
			currentPosition = new Position(20, 18);
		}
		
		wallet = 1000;
		
		if(aStar != null)
			currentPosition.moveInto(aStar.getGrid());
        originalPosition = currentPosition;//save this for moving into
        
        cityMap = map;
        		
		//populate foods list -- need to make sure this matches up with market
		foodsToEat.add("Chicken");
		foodsToEat.add("Steak");
		foodsToEat.add("Salad");
		foodsToEat.add("Pizza");
	
	}
	
	/*
	 * Constructor without astar traversal for testing purposes 
	 */
	
	public PersonAgent(String n){
		super();
		
		name = n;
		
		wallet = 1000;
		
		//populate foods list -- need to make sure this matches up with market
		foodsToEat.add("Chicken");
		foodsToEat.add("Steak");
		foodsToEat.add("Salad");
		foodsToEat.add("Pizza");
				
	}
	
	public void setCityMap(CityMap c){	//for JUnit testing
		cityMap = c;
	}
	
	public String getName(){
		return name;
	}
	
	public void setGoToRestaurant(){	//for testing purposes
		goToRestaurantTest = true;
	}
	
	public void msgAtDestination() {
		atDestination.release();
	}
	
	public void setGui(PersonGui g){
		gui = g;
	}
	
	public void addRole(Role r, boolean active){
		roles.add(r);
		if(active){
			r.setActive();
		}
	}
	
	public void setRoleActive(Role r){
		synchronized(roles){
			for(Role role : roles){
				if(role == r){
					role.setActive();
				}
			}
		}
	}
	
	public void setRoleInactive(Role r){
		synchronized(roles){
			for(Role role : roles){
				if(role == r){
					role.setInactive();
				}
			}
		}
	}
	
	public void addFirstJob(Role r, String location){
		myJob = new Job(r, location);
		roles.add(r);
	}
	
	public void changeJob(Role r, String location){
		myJob.changeJob(r, location);
	}
	
	public void setHouse(HouseInterface h){
		house = h;
		homeGui.setMainAnimationPanel(h.getAnimationPanel());
	}
	
	public void setJobLocation(String loc){
		myJob.location = loc;
	}
	
	//For testing, until we have the time functionality
	public void setWorkState(String s){
		if(s.equals("Go to work")){
			workState = WorkState.goToWork;
		}
		else if(s.equals("Not working")){
			workState = WorkState.notWorking;
		}
	}
	
	/*
	 * MESSAGES FROM HOMEOWNER ANMIATION
	 */
	 public void msgAnimationAtTable(){
         log("I'm at my table now");
 }

	 public void msgAnimationAtFridge(){
		 log("Yes! I made it to the fridge! FOOD FOOD FOOD");
	 }

	 public void msgAnimationAtStove(){
		 log("I'm at the stove, cookin' time");
	 }

	 public void msgAnimationAtOven(){
		 log("Hey oven! I'm standing near you now.");
	 }

	 public void msgAnimationAtMicrowave(){
		 log("Whaddup my main microwave, guess who's standing right next to you? ME!");
	 }

	 public void msgAnimationAtBed(){
		 log("I'm at my bed, time to go to sleep! ZZZzzzZZZzzz...");
	 }
	
	 
	/*
	 * MESSAGES
	 */
	public void msgImHungry(){	//sent from GUI ?
		synchronized(events){
			events.add("GotHungry");
		}
		log("Recieved msgImHungry");
		log.add(new LoggedEvent("Recieved message Im Hungry"));
		stateChanged();
	}
	
	//TODO this is a test hack
	
	public void msgTimeUpdate(int t){
		
		timeOfDay = t;
		
		if(t > 4000 && t < 7020 && name.equals("waiter")){
			synchronized(events){
				events.add("GoToWork");
			}
			log("Its time for me to go to work");
		}
		else if(t > 4000 && t < 7020 && name.equals("waiter1")){
			synchronized(events){
				events.add("GoToWork");
			}
			log("Its time for me to go to work");
		}
		else if(t > 17000 && t < 19000 && (name.equals("rest2Test") || name.equals("rest1Test"))){
			log("The time right now is " + t);
			synchronized(events){
				events.add("GotHungry");
			}
			log("It's time for me to eat something");
		}
		else if(t > 4000 && t < 7020 && name.equals("waiter4")){
			synchronized(events){
				events.add("GoToWork");
			}
			log("Its time for me to go to work");
		}
		else if(t > 17000 && t < 19000 && name.equals("rest4Test")){
			log("The time right now is " + t);
			synchronized(events){
				events.add("GotHungry");
			}
			log("It's time for me to eat something");
		}
		else if(t > 17000 && t < 19000 && name.equals("joe")){
			log("The time right now is " + t);
			synchronized(events){
				events.add("GotHungry");
			}
			log("It's time for me to eat something");
		}
		else if(t > 4000 && t < 7020 && (name.equals("marketManager") || name.equals("marketWorker"))){
			synchronized(events){
				events.add("GoToWork");
			}
			log("Its time for me to go to work");
		}
		
		stateChanged();
	}
	//From house
	public void msgImBroken(String type) {
		appliancesToFix.add(new MyAppliance(type));
		stateChanged();
	}
	
	public void msgItemInStock(String type) {
		log("Yes! I have " + type + " in my fridge! I can't wait to eat!");
		meals.add(new MyMeal(type));
		stateChanged();
	}

	public void msgDontHaveItem(String food) {
		log("Oh no! I don't have any " + food + " in my fridge, I'll add it to my grocery list.");
		groceryList.add(food);
		synchronized(events){
			events.add("GoGroceryShopping");
		}
		stateChanged();
	}

	public void msgFoodDone(String food) {
		log.add(new LoggedEvent("Recieved message food is done"));
		synchronized(meals){
			for(MyMeal m : meals){
				if(m.type == food){
					m.state = FoodState.done;
				}
			}
		}
		stateChanged();
	}

	public void msgFridgeFull() {
		// TODO
		//This is a non-norm, will fill in later
		log("Recieved message fridge full");
		log.add(new LoggedEvent("Recieved message fridge full"));
		stateChanged();
	}

	public void msgSpaceInFridge(int spaceLeft) {
		// TODO Auto-generated method stub
		//Not sure what to do with this one - also non-norm, will assume for now that there is definitely space in fridge?
	}

	public void msgApplianceBrokeCantCook() {
		synchronized(meals){
			for(MyMeal m : meals){
				//TODO finish this function
			}
		}
		stateChanged();
	}
	
	//Messages from bus/bus stop
	public void msgArrivedAtStop(int stop) {
		synchronized(busRides){
			for(BusRide br : busRides){
				if(busStopToGetOffAt == stop){
					br.state = BusRideState.getOffBus;
				}
			}
		}
		stateChanged();
	}
	
	public void msgPleasePayFare(Bus b, double fare) {
		synchronized(busRides){
			for(BusRide br : busRides){
				if(br.bus == b){
					br.addFare(fare);
				}
			}
		}
		stateChanged();
	}
	
	public void msgBusIsHere(Bus b) { //Sent from bus stop
		log.add(new LoggedEvent("Recieved message bus is here"));
		synchronized(events){
			events.add("BusIsHere");
		}
		BusRide busride = new BusRide(b);
		busride.state = BusRideState.busIsHere;
		busRides.add(busride);
		stateChanged();

	}
	
	public void msgArrived(Car car) { //Sent from person's car
		log.add(new LoggedEvent("Recieved message arrived by car"));
		synchronized(carRides){
			for(CarRide cr : carRides){
				if(cr.car == car){
					cr.state = CarRideState.arrived;
				}
			}
		}
		stateChanged();
	}
	
	//from landlord
	public void msgFixed(String appliance) {
		synchronized(appliancesToFix){
			for(MyAppliance a : appliancesToFix){
				if(a.type == appliance){
					a.state = ApplianceState.fixed; 
				}
			}
		}
		stateChanged();
	}
	
	public void msgRentDue(Landlord r, double rate) {
		billsToPay.add(new Bill("rent", rate, r));
		stateChanged();
	}
	
	public void msgHereIsYourOrder(Car car){		//order for a car
		cars.add(car);
		stateChanged();
	}
	
	public void msgHereIsYourOrder(TruckAgent t, MarketOrder order){		//order for groceries
		recievedOrders.add(order);
		stateChanged();
	}
	
	//Bank
	public void msgSetBankAccountNumber(double num){
		accountNumber = num;
	}
	
	public void msgBalanceAfterDepositingIntoAccount(double balance){
		accountBalance = balance;
	}
	
	public void msgBalanceAfterWithdrawingFromAccount(double balance){
		accountBalance = balance;
	}
	
	public void msgBalanceAfterGetitngLoanFromAccount(double balance) {
		accountBalance = balance;
	}
	
	/*
	 * Scheduler
	 * @see agent.Agent#pickAndExecuteAnAction()
	 * Scheduler events are order as follows:
	 * 1. Role schedulers - if a person has a role active, these actions need to be taken care of first
	 * 2. Things that need to be done immediately, i.e. paying bus fare
	 * 3. All other actions (i.e. eat food, go to bank), in order of importance/urgency
	 */
	public boolean pickAndExecuteAnAction() {
		
		/* This is only for our animation test which
		 * displays A* animation capabilities!!
		 */
		if(name == "aStarTest1") {
			DoGoTo("rest3");
			DoGoTo("mark2");
			DoGoTo("apart1");
			goHome();
			return false;
		} else if(name == "aStarTest2") {
			DoGoTo("rest1");
			DoGoTo("rest2");
			DoGoTo("bank2");
			DoGoTo("stop3");
			goHome();
			return false;
		} else if(name == "aStarTest3") {
			DoGoTo("mark3");
			DoGoTo("rest4");
			goHome();
			return false;
		}
		/* End of animation test code */
		
		//ROLES - i.e. job or customer
		boolean anytrue = false;
		synchronized(roles){
			for(Role r : roles){
				if(r != null){
				if(r.isActive){
					anytrue = r.pickAndExecuteAnAction() || anytrue; // Changed by Grant
				}
				}
			}
			if (anytrue){
				return anytrue;
			} 
		}
		synchronized(events){
			for(String e : events){
				if(e.equals("GoToWork")){
					goToWork();
					return true;
				}
			}
		}
		synchronized(events){
			for(String e : events){
				if(e.equals("WorkDone")){
					leaveWork();
					return true;
				}
			}
		}
		synchronized(busRides){
			for(BusRide br : busRides){
				if(br.fare != 0){
					payBusFare(br);
					return true;
				}
			}
		}
		synchronized(busRides){
			for(BusRide br : busRides){
				if(br.state == BusRideState.busIsHere){
					getOnBus(br);
					return true;
				}
			}
		}
		synchronized(busRides){
			for(BusRide br : busRides){
				if(br.state == BusRideState.getOffBus){
					getOffBus(br);
					return true;
				}
			}
		}
		synchronized(carRides){
			for(CarRide cr : carRides){
				if(cr.state == CarRideState.arrived){
					getOutOfCar(cr);
				}
			}
		}
		//Person getting hungry
		synchronized(events){
			for(String e : events){
				if(e.equals("GotHungry")){
					Eat();
					return true;
				}
			}
		}
		//Go grocery shopping
		synchronized(events){
			for(String e : events){
				if(e.equals("GoGroceryShopping")){
					goGroceryShopping();
					return true;
				}
			}
		}
		//Go to bank
		synchronized(events){
			for(String e : events){
				if(e.equals("GoToBank"));
				goToBank();
				return true;
			}
		}
		//Cook meal
		synchronized(meals){
			for(MyMeal m : meals){
				if(m.state == FoodState.initial){
					cookMeal(m);
					return true;
				}
			}
		}
		//Eat meal
		synchronized(meals){
			for(MyMeal m : meals){
				if(m.state == FoodState.done){
					eatMeal(m);
					return true;
				}
			}
		}
		//Deal with recieved orders
		synchronized(recievedOrders){
			if(!recievedOrders.isEmpty()){
				handleRecievedOrders();
				return true;
			}
		}
		//Pay bills
		synchronized(billsToPay){
			if(!billsToPay.isEmpty()){
				payBills();
				return true;
			}
		}
		//Notify landlord of broken appliance
		synchronized(appliancesToFix){
			for(MyAppliance a : appliancesToFix){
				if(a.state == ApplianceState.broken){
					notifyLandlordBroken(a);
					return true;
				}
			}
		}
		//Notify house that appliance is fixed
		synchronized(appliancesToFix){
			for(MyAppliance a : appliancesToFix){
				if(a.state == ApplianceState.fixed){
					notifyHouseFixed(a);
					return true;
				}
			}
		}
		boolean anyActive= false;
		synchronized(roles){
			for(Role r : roles){
				if(r != null){
				if(r.isActive)
					anyActive = true;
				}
			}
			if(!atHome && !anyActive){
				if(house != null)
					goHome();
			}
		}

		return false;
	}
	

	//ACTIONS
	public void goHome(){
		if(!atHome){
			log("Going home");
			if(house != null){
				DoGoTo(house.getName());
				house.getAnimationPanel().addGui(homeGui);
				homeGui.goToBed();
			}	
			atHome= true;
		}
	}
	
	public void goToWork(){
		log("Going to work");
		synchronized(events){
			for(String e : events){
				if(e.equals("GoToWork")){
					events.remove(e);
					break;
				}
			}
		}
		DoGoTo(myJob.location);
		//Semaphore stuff
		//TODO how to announce that the person is there for work
		myJob.startJob();
	}
	
	public void leaveWork(){
		synchronized(events){
			for(String e : events){
				if(e.equals("LeaveWork")){
					events.remove(e);
					break;
				}
			}
		}
		//Need to make the gui step outside the building, and then the person can do whatever the next thing is on their list
		myJob.endJob();
	}
	
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
		//If the person needs to go to work, they will eat at home
		if(workState == WorkState.goToWork){
			int y = rand.nextInt(foodsToEat.size());
			String food = foodsToEat.get(y);
			house.checkFridge(food);
			log("I'm going to eat " + food + " in my house.");
			log.add(new LoggedEvent("Decided to eat something from my house."));
		}
		else if(name.equals("joe")){
			homeGui.goToFridge();         
        	try{
                atDestination.acquire();
        	} catch (InterruptedException e){}
        	int y = rand.nextInt(foodsToEat.size());
			String food = foodsToEat.get(y);
			house.checkFridge(food);
			groceryList.add(food);
			homeGui.goToExit(); 
        	try{
                atDestination.acquire();
        	} catch (InterruptedException e){}
        	/*MarketOrder o= new MarketOrder(food, this);
        	log("IS THE MARKET MANAGER NULL? " + cityMap.market.mktManager);
        	cityMap.market.mktManager.msgHereIsOrder(o);
        	DoGoTo("mark1");*/
		}
		//Else if they don't have to go to work, they will go to a restaurant
		else{
			goToRestaurant();
		}
	}
	

	public void goToBank(){
		synchronized(events){
			for(String e : events){
				if(e.equals("GoToBank")){
					events.remove(e);
					break;
				}
			}
		}
		String bank;
		
		
		synchronized(bankEvents){
			//TODO finish this
			//bank = cityMap.getClosestBank();
		}
	}
	
	public void goToRestaurant(){
		if(name.equals("rest1Test")){
			print("Going to go to a restaurant");
			String restName = null;
			Role role = null;
			synchronized(roles){
				for(Role r : roles){
					if(r instanceof Restaurant1CustomerRole) {
						r.setActive();
						role = (Restaurant1CustomerRole) r;
						restName = role.getBuilding();
						log("Set Restaurant1CustomerRole active");
					}
				}
			}
			if(!cars.isEmpty()){
				String destination = restName;
				takeCar(destination);
			}
			else{
				//This is walking
				DoGoTo(restName);
			}
			log.add(new LoggedEvent("Decided to go to a restaurant"));
				((Restaurant1CustomerRole) role).setHost(cityMap.restaurant1.getHost());
				((Restaurant1CustomerRole) role).goToRestaurant();
				((Restaurant1CustomerRole)role).setGuiActive();
		}
		else if(name.equals("rest2Test")){
			log("Going to go to a restaurant");
			String restName = null;
			Role role = null;
			synchronized(roles){
				for(Role r : roles){
					if(r instanceof Restaurant2CustomerRole) {
						r.setActive();
						role = (Restaurant2CustomerRole) r;
						restName = role.getBuilding();
						log("Set Restaurant2CustomerRole active");
					}
				}
			}
			if(!cars.isEmpty()){
				String destination = restName;
				takeCar(destination);
			}
			else{
				//This is walking
				DoGoTo(restName);
			}
			log.add(new LoggedEvent("Decided to go to a restaurant"));
			cityMap.restaurant2.getHost().msgIWantFood((Restaurant2Customer) role);
			((Restaurant2CustomerRole)role).setGuiActive();
		}
		else if(name.equals("rest4Test")){
			print("Going to go to a restaurant");
			String restName = null;
			Role role = null;
			synchronized(roles){
				for(Role r : roles){
					if(r instanceof CustomerRole4) {
						r.setActive();
						role = (CustomerRole4) r;
						restName = role.getBuilding();
						log("Set CustomerRole4 active");
					}
				}
			}
			if(!cars.isEmpty()){
				String destination = restName;
				takeCar(destination);
			}
			else{
				//This is walking
				DoGoTo(restName);
			}
			log.add(new LoggedEvent("Decided to go to a restaurant"));
			cityMap.restaurant4.getHost().msgIWantFood((CustomerRole4) role);
			((CustomerRole4)role).setGuiActive();	
		}
		
	}
	
	public void notifyLandlordBroken(MyAppliance a){
		log("Telling landlord that appliance " + a.type + " is broken");
		landlord.msgFixAppliance(this, a.type);
		a.state = ApplianceState.beingFixed;
	}
	
	public void payBills(){
		log.add(new LoggedEvent("Paying bill"));
		synchronized(billsToPay){
			for(Bill b : billsToPay){
				if(b.landlord == landlord){
					if(wallet > b.amount){
						log.add(new LoggedEvent("The bill I'm paying is my rent"));
						landlord.msgHereIsMyRent(this, b.amount);
						wallet -= b.amount;
						billsToPay.remove(b);
						return;
					}
					else{
						synchronized(events){
							events.add("GoToBank");
							return;
						}
					}
				}
			}
		}
	}
	
	public void getOnBus(BusRide ride){
		gui.setInvisible();
		ride.state = BusRideState.onBus;
		log.add(new LoggedEvent("Getting on the bus"));
	}
	
	/*
	 * This is assuming the person will always have enough to pay the fare.
	 * May need to fix this later in non-norm scenario
	 */
	public void payBusFare(BusRide br){
		br.bus.msgHereIsFare(this, br.fare);
		br.state = BusRideState.paidFare;
		br.fare = 0;
		wallet -= br.fare;
	}
	
	public void getOffBus(BusRide busride){
		busride.bus.msgImGettingOff(this);
		String thisStop = "stop" + Integer.toString(busStopToGetOffAt);
		int x = cityMap.getX(thisStop);
		int y = cityMap.getY(thisStop);
		gui.teleport(x * 30 + 130, y * 30 + 70);
	    currentPosition.release(aStar.getGrid());
		currentPosition = new Position(x, y);
		currentPosition.moveInto(aStar.getGrid());
		
		print("Now, go to final destination!");
	    
		busRides.remove(busride);
		DoGoTo(destinationBuilding);		
	}
	
	public void getOutOfCar(CarRide ride){
		ride.car.msgParkCar(this);
		log.add(new LoggedEvent("Telling car to park"));
	}
	
	public void notifyHouseFixed(MyAppliance a){
		house.fixedAppliance(a.type);
		appliancesToFix.remove(a);	//no longer needed on this list
	}
	
	public void goGroceryShopping(){
		log("I'm headed out to the market to buy food now.");
		synchronized(events){
			for(String e : events){
				if(e.equals("GoGroceryShopping")){
					events.remove(e);
					break;
				}
			}
		}
		DoGoTo("mark1");
		MarketOrder o= new MarketOrder(groceryList.get(0), this);
    	log("IS THE MARKET MANAGER NULL? " + cityMap.market.mktManager);
    	cityMap.market.mktManager.msgHereIsOrder(o);
		/*
		 * TODO gui - go to market
		 * NOT walking, because there will be groceries to carry
		 */
		if(cars.isEmpty()){
			 //String market = cityMap.getClosestPlaceFromHere(house.getName(), "mark");
			 //DoGoTo(market);	//may need to force bus travel
		}
		else{
			//takeCar(market);
		}
		
	}
	
	public void takeCar(String destination){
		log.add(new LoggedEvent("Taking car to destination: " + destination));
		CarRide ride = new CarRide((Car) cars.get(0), destination);
		carRides.add(ride);
		ride.car.msgDriveTo(this, destination);
	}
	
	public void handleRecievedOrders(){
		synchronized(recievedOrders){
			for(MarketOrder o : recievedOrders){
				for(int i = 0; i < o.orders.size(); i ++){
					Food f = new Food(o.orders.get(i).type, "Stove", o.orders.get(i).quantity);
					//TODO change the appliance type
				}
			}
		}
	}
	
	public void cookMeal(MyMeal meal){
		log.add(new LoggedEvent("Cooking meal"));
		house.cookFood(meal.type);
		meal.state = FoodState.cooking;
		//TODO add gui
	}
	
	public void eatMeal(MyMeal m){
		log.add(new LoggedEvent("Eating meal"));
		//TODO make gui function
		//gui.eatMeal();
		meals.remove(m);
	}
	
	public void setGuiVisible(){
		gui.setVisible();
	}
	
	//Animation code below!
	public int getXPosition() {
		return currentPosition.getX();
	}
	
	public int getYPosition() {
		return currentPosition.getY();
	}
	
	void moveTo(int x, int y) {
		Position p = new Position(x, y);		
		guiMoveFromCurrentPositionTo(p);
	}
	
	void DoGoTo(String location) {
		if(test)
			return;
		
		atHome= false;
		house.getAnimationPanel().notInHouse(homeGui);
		
		gui.setVisible();
		int x = cityMap.getX(location);
		int y = cityMap.getY(location);

		Position p = new Position(x, y);
		
		//if(currentPosition.distance(p) < 20) {
			moveTo(x, y);
			gui.setInvisible();
			return;
		/*}
		destinationBuilding = location;
		int startingBusStop = cityMap.getClosestBusStop(currentPosition);
		int endingBusStop = cityMap.getClosestBusStop(location);
		busStopToGetOffAt = endingBusStop;
		DoGoTo("stop" + Integer.toString(startingBusStop));
		busStop = cityMap.getBusStop(startingBusStop);
		busStop.msgWaitingForBus(this);			
		gui.setVisible(); /*Person will stand outside bus stop*/
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
			try { Thread.sleep(500); }
			catch (Exception e){}

			gotPermit   = new Position(tmpPath.getX(), tmpPath.getY()).moveInto(aStar.getGrid());
			attempts ++;
		    }

		    //Did not get lock after trying n attempts. So recalculating path.            
		    if (!gotPermit) {
			//System.out.println("[Gaut] " + guiWaiter.getName() + " No Luck even after " + attempts + " attempts! Lets recalculate");
		    	path.clear(); aStarNode=null;
		    	guiMoveFromCurrentPositionTo(to);
		    	break;
		    }

		    //Got the required lock. Lets move.
		    //System.out.println("[Gaut] " + guiWaiter.getName() + " got permit for " + tmpPath.toString());
		    currentPosition.release(aStar.getGrid());
		    currentPosition = new Position(tmpPath.getX(), tmpPath.getY ());
		    //log("Moving to " + currentPosition.getX() + ", " + currentPosition.getY());
		    gui.moveTo(130 + (tmpPath.getX() * 30), 70 + (tmpPath.getY() * 30));
		    
		    //Give animation time to move to square.

		    try {
				atDestination.acquire();
			} catch (InterruptedException e) {
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
	
	public class Bill{
		public String type;
		public double amount;
		public Role payTo;
		public Landlord landlord;
		
		public Bill(String t, double a, Role r){
			type = t;
			amount = a;
			payTo = r;
		}
		
		public Bill(String t, double a, Landlord l){
			type = t;
			amount = a;
			landlord = l;
		}
		
		
	}
	
	class MyAppliance{
		String type;
		ApplianceState state;
		
		public MyAppliance(String t){
			type = t;
			state = ApplianceState.broken;
		}
		
	}
	
	public class MyMeal{
		public String type;
		public FoodState state;
		
		public MyMeal(String t){
			type = t;
			state = FoodState.initial;
		}
	}
	
	public class BusRide{
		public Bus bus;
		public double fare;
		public BusRideState state;
		
		public BusRide(Bus b){
			bus = b;
			fare = 0;
			state = BusRideState.initial;
		}
		
		//This will get used in the message recieved from the bus
		public void setBus(Bus b){
			bus = b;
		}
		
		public void addFare(double f){
			fare = f;
		}
	}
	
	public class CarRide{
		public Car car;
		public String destination;
		public CarRideState state;
		
		public CarRide(Car c, String dest){
			car = c;
			destination = dest;
			state = CarRideState.initial;
		}
	}
	
	public class BankEvent{
		public BankEventType type;
		public double amount;
		
		public BankEvent(BankEventType t, double a){
			type = t;
			amount = a;
		}
	}
	
	public class Job{
		Role role;
		String location;
		int workStartTime;
		int leaveForWork;
		int workEndTime;
		
		public Job(Role r, String l){
			role = r;
			//location = r.getBuilding();
			location= l;
			workStartTime = -1;
			workEndTime = -1;
			leaveForWork = -1;
		}
		
		public void startJob(){
			role.setActive();
			workState = WorkState.atWork;
			if(role.getGui() != null){
				role.getGui().setPresent(true);
			}
			if(role.getGui() instanceof Restaurant2CustomerGui){
				log("This is a customer gui");
			}
		}
		
		public void endJob(){
			role.setInactive();
			workState = WorkState.notWorking;
			role.getGui().setPresent(false);
		}
		
		public void changeJob(Role r, String l){
			role = r;
			location = l;
		}
		
	}
	
	private void log(String msg){
		print(msg);
		if(!test){
	        ActivityLog.getInstance().logActivity(tag, msg, name);
		}
        log.add(new LoggedEvent(msg));
	}
	
	public void setTesting(boolean t){
		test = true;
	}

	@Override
	public void msgHereIsYourOrder(MarketOrder order) {
		// TODO Auto-generated method stub
		
	}
	
}