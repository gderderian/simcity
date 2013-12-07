package city;

import test.mock.LoggedEvent;
import interfaces.Bus;
import interfaces.Car;
import interfaces.HouseInterface;
import interfaces.Landlord;
import interfaces.Person;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

import test.mock.EventLog;
import city.PersonTask.State;
import city.PersonTask.TaskType;
import city.PersonTask.Transportation;
import city.gui.PersonGui;
import city.gui.House.HomeOwnerGui;
import city.gui.restaurant2.Restaurant2CustomerGui;
import city.transportation.BusAgent;
import city.transportation.BusStopAgent;
import city.transportation.CarAgent;
import city.transportation.TruckAgent;
import Role.BankCustomerRole;
import Role.BankTellerRole;
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
	public List<PersonTask> tasks = Collections.synchronizedList(new ArrayList<PersonTask>());
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
	public Car car;
	enum TransportationState{takingCar, takingBus, walking, chooseTransport};
	TransportationState transportationState;
	CityMap cityMap;
	BusAgent bus;
	public BusRide busRide;	//only need one because will only be doing one bus ride at a time
	public enum BusRideState {initial, waiting, busIsHere, onBus, done, paidFare, getOffBus};
	public CarRide carRide;
	public enum CarRideState {initial, arrived, pickingMeUp, inCar};

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

	public boolean busTest = false;

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
	boolean callonce = false; //this is tom's little hack

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
		busRide = new BusRide(5);

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
		busRide = new BusRide(5);

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
	public void msgImHungry(){
		synchronized(tasks){
			tasks.add(new PersonTask(TaskType.gotHungry));
		}
		log("Recieved msgImHungry");
		stateChanged();
	}

	//TODO fix this

	public void msgTimeUpdate(int t){

		timeOfDay = t;

		if(t > 4000 && t < 7020 && (name.equals("waiter") || name.equals("waiter1") || name.equals("waiter3") || name.equals("waiter4")
				|| name.equals("waiter5"))){
			synchronized(tasks){
				tasks.add(new PersonTask(TaskType.goToWork));
			}
			log("Its time for me to go to work");
			stateChanged();
		}
		else if(t > 19000 && t < 21000 && (name.equals("rest2Test") ||/* name.equals("rest1Test") || */name.equals("rest4Test")
				|| name.equals("rest5Test") || name.equals("rest3Test") || name.equals("joe"))){
			synchronized(tasks){
				tasks.add(new PersonTask(TaskType.gotHungry));
			}
			log("It's time for me to eat something");
			stateChanged();
		}
		else if(t > 5000 && t < 7000 && name.equals("rest1Test")) {
			synchronized(tasks){
				tasks.add(new PersonTask(TaskType.gotHungry));
			}
			log("It's time for me to eat something");
			stateChanged();
		}

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
		synchronized(tasks){
			tasks.add(new PersonTask(TaskType.goToMarket));
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
		// TODO Auto-generated method stub
		//This is a non-norm, will fill in later
		log("Recieved message fridge full");
	}

	public void msgSpaceInFridge(int spaceLeft) {
		// TODO Auto-generated method stub
		//Not sure what to do with this one - also non-norm, will assume for now that there is definitely space in fridge?
	}

	//TODO finish this function
	public void msgApplianceBrokeCantCook() {
		synchronized(meals){
			for(MyMeal m : meals){

			}
		}

	}

	//Messages from bus/bus stop
	public void msgArrivedAtStop(int stop, Position p) {
		if(busRide.finalStop == stop){
			busRide.state = BusRideState.getOffBus;
			busRide.busPos = p;
			log("Arrived at the correct bus stop, I can get off!");
		}
		stateChanged();
	}

	public void msgPleasePayFare(Bus b, double fare) {
		busRide.addFare(fare);
		log("Added fare to bus ride to pay");
		stateChanged();
	}

	public void msgBusIsHere(Bus b, Position p) { //Sent from bus stop
		log("Recieved message bus is here");
		busRide.bus = b;
		busRide.busPos = p;
		busRide.state = BusRideState.busIsHere;
		stateChanged();
	}

	//Messages from car
	public void msgImPickingYouUp(Car car, Position p) { 
		log.add(new LoggedEvent("Received message ImPickingYouUp from car"));
		carRide.state = CarRideState.pickingMeUp;
		carRide.carLocation = p;
		stateChanged();
	}

	public void msgArrived(Car car, Position p) {
		log.add(new LoggedEvent("Recieved message arrived by car"));
		log("Thanks for the ride!");
		carRide.state = CarRideState.arrived;
		carRide.carLocation = p;
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
		this.car = car;
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
		
		if(name.equals("bankCustomerTest") && callonce == false) {
			goToBank(new PersonTask(TaskType.goToBank));
			callonce = true;
		}

		//ROLES - i.e. job or customer
		boolean anytrue = false;
		synchronized(roles){
			for(Role r : roles){
				if(r.isActive){
					anytrue = r.pickAndExecuteAnAction() || anytrue; // Changed by Grant
				}
			}
			if(anytrue){
				return anytrue;
			} 
		}
		synchronized(tasks){
			for(PersonTask t : tasks){
				if(t.state == State.arrived){
					reachedDestination(t);
					t.state = State.processing;
					return true;
				}
			}
		}
		synchronized(tasks){
			for(PersonTask t : tasks){
				if(t.type == TaskType.goToWork && t.state == State.initial){
					goToWork(t);
					t.state = State.processing;
					return true;
				}
			}
		}
		synchronized(tasks){
			for(PersonTask t : tasks){
				if(t.type == TaskType.doneWithWork && t.state == State.initial){
					leaveWork();
					t.state = State.processing;
					return true;
				}
			}
		}
		if(busRide.finalStop != 5){
			if(busRide.fare != 0){
				payBusFare(busRide);
				return true;
			}
		}
		if(busRide.finalStop != 5){
			if(busRide.state == BusRideState.busIsHere){
				getOnBus();
				return true;
			}
		}
		if(busRide.finalStop != 5){
			if(busRide.state == BusRideState.getOffBus){
				getOffBus();
				return true;
			}
		}
		//CarRide actions
		if(carRide != null) {
			if(carRide.state == CarRideState.pickingMeUp){
				tellCarWhereToDrive(carRide);
				return true;
			}
		}
		if(carRide != null) {
			if(carRide.state == CarRideState.arrived){
				getOutOfCar(carRide);
				return true;
			}
		}

		//Person getting hungry
		synchronized(tasks){
			for(PersonTask t : tasks){
				if(t.type == TaskType.gotHungry && t.state == State.initial){
					eat(t);
					t.state = State.processing;
					return true;
				}
			}
		}
		//Go grocery shopping
		synchronized(tasks){
			for(PersonTask t : tasks){
				if(t.type == TaskType.goToMarket && t.state == State.initial){
					goGroceryShopping(t);
					t.state = State.processing;
					return true;
				}
			}
		}
		//Go to bank
		synchronized(tasks){
			for(PersonTask t: tasks){
				if(t.type == TaskType.goToBank && t.state == State.initial){
				goToBank(t);
				t.state = State.processing;
				return true;
				}
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
		/*
		synchronized(roles){
			for(Role r : roles){
				if(r.isActive)
					anyActive = true;
			}
			if(!atHome && !anyActive && !busTest){
				if(house != null)
					goHome();
			}
		}
		 */
		return false;
	}


	//ACTIONS
	public void goHome(){
		if(!atHome){
			log("Going home");
			if(house != null){
				DoGoTo(house.getName(), null);
				house.getAnimationPanel().addGui(homeGui);
				homeGui.goToBed();
			}	
			atHome= true;
		}
	}

	@SuppressWarnings("unused")
	public void reachedDestination(PersonTask task){
		log("I've reached my destination, now I'm going to go inside!");
		log("My tas right now is " + task.type.toString());
		Role role = null;
		synchronized(roles){
			if(task.role != null){
				log("The role name is " + task.role);
				for(Role r : roles){
					if(r.getRoleName().equals(task.role)){
						r.setActive();
						role = r;
						break;
					}
				}
			}
		}
		//This is if the person is going to the restaurant to eat
		if(task.location.contains("rest") && task.type == TaskType.gotHungry){
			String[] restNum = task.location.split("rest");
			if(role != null){
				cityMap.msgHostHungryAtRestaurant(Integer.parseInt(restNum[1]), role);
				role.getGui().setPresent(true);
			}
			else{
				log("Looks like I don't have a role for this task. I can't go into the building.");
			}
		}
		else if(task.type == TaskType.goToWork){
			myJob.startJob();
		}
		else if(task.type == TaskType.goToBank){
			log.add(new LoggedEvent("Decided to go to the bank"));
			if(role != null){
				cityMap.bank.getBankManager().msgCustomerArrivedAtBank((BankCustomerRole) role);
				((BankCustomerRole)role).setGuiActive();
			}
			else{
				log("Couldn't find the role for task " + task.type.toString());
			}
		}
	}

	public void goToWork(PersonTask task){
		log("Going to work");
		task.location = myJob.location;
		//Role in the task here should be null because role-related things are taken care of in the Job class

		if(car != null){	//if the person has a car, he/she will take it
			takeCar(myJob.location);
			task.transportation = Transportation.car;
			task.state = State.inTransit;
		}
		else{
			DoGoTo(myJob.location, task);
		}
		//This needs to be moved into the reachedDestination() function
		//myJob.startJob();
	}

	//TODO ...
	public void leaveWork(){
		//Need to make the gui step outside the building, and then the person can do whatever the next thing is on their list
		myJob.endJob();
	}

	public void eat(PersonTask task){	//hacked for now so that it randomly picks eating at home or going out
		task.state = State.processing;
		Random rand = new Random();
		/*If the person needs to go to work, they will eat at home
		 * This would need to be updated with the Schedule update
		 */
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
			goToRestaurant(task);
		}
	}

	/*
	 * Left this function alone for the most part, hopefully is still usable with Tom's tests
	 * Will need to change this later (maybe once testing is complete) to fit update
	 */
	public void goToBank(PersonTask task){
		if(name.equals("bankCustomerTest")){
			print("Going to go to the bank");
			String bankName = null;
			Role role = null;
			synchronized(roles){
				for(Role r : roles){
					if(r instanceof BankCustomerRole) {
						r.setActive();
						role = (BankCustomerRole) r;
						bankName = role.getBuilding();
						task.location = bankName;
						task.role = r.getRoleName();
						//task.role = r;
						log("Set BankCustomerRole active");
					}
				}
			}
			if(car != null){	//Extremely hack-y TODO fix this
				String destination = bankName;
				takeCar(destination);
				task.state = State.inTransit;
			}
			else{
				//This is walking
				DoGoTo(bankName, task);
			}
			//Moved this to arrived at destination function
			//log.add(new LoggedEvent("Decided to go to the bank"));
			//cityMap.bank.getBankManager().msgCustomerArrivedAtBank((BankCustomerRole) role);
			//((BankCustomerRole)role).setGuiActive();		
		}
		synchronized(bankEvents){
			//TODO finish this
			//bank = cityMap.getClosestBank();
		}
	}

	public void goToRestaurant(PersonTask task){
		//Testing/scenario hacks

		if(name.contains("rest")){	//if it's a restaurant test
			String[] restNumTest = name.split("rest");
			String[] restNum = restNumTest[1].split("Test");
			String num = restNum[0];
			log("Going to go to Restaurant " + num);
			task.location = "rest" + num;
			task.role = "Restaurant" + num + "CustomerRole";
			log("The role is called " + task.role);
			if(car != null){
				print("Car is not empty!");
				String destination = task.location;
				takeCar(destination);
			}
			else{
				DoGoTo(task.location, task);
			}
			/*
			if(name.equals("rest5Test")){
				print("Going to go to a restaurant 5");
				String restName = null;
				Role role = null;
				synchronized(roles){
					for(Role r : roles){
						if(r instanceof Restaurant5CustomerRole) {
							r.setActive();
							role = (Restaurant5CustomerRole) r;
							restName = role.getBuilding();
							restName = "rest5";
							log.add(new LoggedEvent("Decided to go to a restaurant5"));
	                        ((Restaurant5CustomerRole) role).setHost(cityMap.restaurant5.getHost());
	                        ((Restaurant5CustomerRole) role).setCashier(cityMap.restaurant5.getCashier());
	                        if(task.transportation == Transportation.walking) {
	        					reachedDestination(task);
	        				}
	                        log("Set CustomerRole5 active");
						}
					}
				}
			}
			 */

		}
		else{
			//Generalized function so we can get rid of the hacks

			//Get the location and set the role in the task
			String location = cityMap.getClosestPlaceFromHere(house.getName(), "rest");
			task.location = location;
			String[] restNum = location.split("rest");
			log("The number of the restaurant I am going to is " + restNum[0]);
			String roleName = "Restaurant" + restNum[0] + "CustomerRole";
			task.role = roleName;

			if(car != null){	//if the person has a car, he/she will take it
				takeCar(location);
				task.transportation = Transportation.car;
				task.state = State.inTransit;
			}
			else{
				//This is walking
				DoGoTo(location, task);
			}
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
						synchronized(tasks){
							tasks.add(new PersonTask(TaskType.goToBank));
							//Eventually want to make this so there are different types of goToBank TaskTypes
							//i.e. for this TaskType.goToBankWithdrawal or something
							return;
						}
					}
				}
			}
		}
	}


	public void getOnBus(){
		gui.moveTo(busRide.busPos.getX() * 30 + 120, busRide.busPos.getY() * 30 + 60);
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gui.setInvisible();
		busRide.state = BusRideState.onBus;
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

	public void getOffBus(){
		log("Getting off the bus");
		
		int busX = busRide.busPos.getX();
		int busY = busRide.busPos.getY();
		gui.teleport(busX * 30 + 120, busY * 30 + 60);
		gui.setVisible();
		
		busRide.state = BusRideState.done;
		busRide.bus.msgImGettingOff(this);
		
		String thisStop = "stop" + Integer.toString(busRide.finalStop);
		
		int x = cityMap.getX(thisStop);
		int y = cityMap.getY(thisStop);
		
		gui.moveTo(x * 30 + 120, y * 30 + 60);
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		currentPosition.release(aStar.getGrid());
		currentPosition = new Position(x, y);
		currentPosition.moveInto(aStar.getGrid());
		
		print("Now, go to final destination!");

		PersonTask temp = null;
		synchronized(tasks){
			for(PersonTask t : tasks){
				if(t.location.equals(busRide.destination)){
					temp = t;
				}
			}
		}

		DoGoTo(busRide.destination, temp);
	}

	public void tellCarWhereToDrive(CarRide ride) {
		gui.moveTo(ride.carLocation.getX() * 30 + 120, ride.carLocation.getY() * 30 + 60);
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gui.setInvisible();
		ride.state = CarRideState.inCar;
		ride.car.msgDriveTo(this, ride.destination);
		log.add(new LoggedEvent("Telling car to go to " + ride.destination));
	}
	
	public void getOutOfCar(CarRide ride){
		int carX = ride.carLocation.getX();
		int carY = ride.carLocation.getY();
		gui.teleport(carX * 30 + 120, carY * 30 + 60);
		gui.setVisible();
		ride.car.msgParkCar(this);
		
		log.add(new LoggedEvent("Telling car to park"));

		int x = cityMap.getX(ride.destination);
		int y = cityMap.getY(ride.destination);
		gui.moveTo(x * 30 + 120, y * 30 + 60);
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		currentPosition.release(aStar.getGrid());
		currentPosition = new Position(x, y);
		currentPosition.moveInto(aStar.getGrid());
		gui.setInvisible();
		
		//Will need to pass in the current task when this get used regularly
		PersonTask task = null;
		synchronized(tasks){
			for(PersonTask t : tasks){
				if(t.location.equals(ride.destination)){
					task = t;
				}
			}
		}
		reachedDestination(task);

		carRide = null;
	}

	public void notifyHouseFixed(MyAppliance a){
		house.fixedAppliance(a.type);
		appliancesToFix.remove(a);	//no longer needed on this list
	}

	public void goGroceryShopping(PersonTask task){
		log("I'm headed out to the market to buy food now.");

		String location = cityMap.getClosestPlaceFromHere(house.getName(), "mark");
		task.location = location;
		task.role = "MarketCustomerRole";

		if(car != null){
			DoGoTo(location, task);
		}
		else{
			takeCar(location);
		}
		task.state = State.inTransit;

		/*
		 * This will be moved to reachedDestination() function
		MarketOrder o = new MarketOrder(groceryList.get(0), this);
		cityMap.market.mktManager.msgHereIsOrder(o);
		 */
	}

	public void takeCar(String destination){
		log.add(new LoggedEvent("Taking car to destination: " + destination));
		CarRide ride = new CarRide((Car) car, destination);
		carRide = ride;
		ride.car.msgPickMeUp(this, currentPosition);
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

	void DoGoTo(String location, PersonTask task) {
		if(test)
			return;

		if(task != null){
			task.state = State.inTransit;
		}

		atHome= false;
		if(house != null)
			house.getAnimationPanel().notInHouse(homeGui);

		gui.setVisible();
		int x = cityMap.getX(location);
		int y = cityMap.getY(location);

		Position p = new Position(x, y);
		if(currentPosition.distance(p) > 25) {	// || name.equals("BusTest")
			if(task != null){
				task.transportation = Transportation.bus;
			}
			int startingBusStop = cityMap.getClosestBusStop(currentPosition);
			int busStopToGetOffAt = cityMap.getClosestBusStop(location);
			busRide.finalStop = busStopToGetOffAt;
			busRide.initialStop = startingBusStop;
			busRide.destination = location;
			DoGoTo("stop" + Integer.toString(startingBusStop), task);
			busRide.busStopAgent = cityMap.getBusStop(startingBusStop);
			busRide.busStopAgent.msgWaitingForBus(this);
			gui.setVisible(); /*Person will stand outside bus stop*/
			return;
		}
		if(task != null){
			if(task.transportation != Transportation.bus && task.transportation != Transportation.car)
				task.transportation = Transportation.walking;
		}
		moveTo(x, y);
		if(task != null)
			task.state = State.arrived;
		if(task.transportation == Transportation.walking){
			reachedDestination(task);
		}
		gui.setInvisible();
		return;
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

	public void addCar(Car c) {
		car = c;
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
		public Position busPos;
		public double fare;
		public BusRideState state;
		public int finalStop;
		public int initialStop;
		public BusStopAgent busStopAgent;
		public String destination;

		public BusRide(int stop){
			fare = 0;
			state = BusRideState.initial;
			finalStop = stop;
		}

		public void addFare(double f){
			fare = f;
		}
	}

	public class CarRide{
		public Car car;
		public String destination;
		public CarRideState state;
		public Position carLocation;

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
		log("Yay, I got my order back!");
		List<OrderItem> o= order.orders;
		Food f= new Food(o.get(0).type);
		List<Food> groceries= new ArrayList<Food>();
		groceries.add(f);
		house.boughtGroceries(groceries);
	}

}