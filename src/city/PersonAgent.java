package city;

import interfaces.Bus;
import interfaces.Car;
import interfaces.HouseInterface;
import interfaces.Landlord;
import interfaces.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurant1.Restaurant1CookRole;
import test.mock.EventLog;
import test.mock.LoggedEvent;
import Role.BankCustomerRole;
import Role.BankTellerRole;
import Role.LandlordRole;
import Role.MarketCustomerRole;
import Role.Role;
import activityLog.ActivityLog;
import activityLog.ActivityTag;
import agent.Agent;
import astar.AStarNode;
import astar.AStarTraversal;
import astar.Position;
import city.PersonTask.State;
import city.PersonTask.TaskType;
import city.PersonTask.Transportation;
import city.Restaurant2.Restaurant2CookRole;
import city.Restaurant3.CookRole3;
import city.Restaurant4.CookRole4;
import city.Restaurant5.Restaurant5CookRole;
import city.gui.CityClock;
import city.gui.PersonGui;
import city.gui.House.HomeOwnerGui;
import city.transportation.BusAgent;
import city.transportation.BusStopAgent;
import city.transportation.TruckAgent;

public class PersonAgent extends Agent implements Person{

	//DATA
	String name;
	public List<PersonTask> tasks = Collections.synchronizedList(new ArrayList<PersonTask>());
	public PersonSchedule schedule = new PersonSchedule();
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
	Timer eatMeal= new Timer();

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
	public int bankaccountnumber;
	double moneyToDeposit;

	//Bank
	Bank bank = new Bank();
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
	CityClock clock;
	int currentHour;

	//Testing
	public EventLog log = new EventLog();
	public boolean goToRestaurantTest = false;
	public boolean test = false;
	public boolean busTest = false;

	//Job
	public Job myJob = null;
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
			if(house.getName().contains("apart1")){
				currentPosition = new Position(map.getX("apart1"), map.getY("apart1"));
			}
			else if(house.getName().contains("apart2")){
				currentPosition = new Position(map.getX("apart2"), map.getY("apart2"));
			}
			else{
				currentPosition = new Position(map.getX(house.getName()), map.getY(house.getName()));
			}
		} else {
			currentPosition = new Position(20, 18);
		}

		wallet = 100;
		bankaccountnumber = 0;

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
		
		//populate fridge with a few foods to start
		if(house != null){
			log("My house should be stocked with a minimal amount of food");
			List<Food> groceries= new ArrayList<Food>();
			Food chicken= new Food("Chicken");
			Food steak= new Food("Steak");
			Food salad= new Food("Salad");
			Food pizza= new Food("Pizza");
			groceries.add(chicken);
			groceries.add(steak);
			groceries.add(salad);
			groceries.add(pizza);
			house.boughtGroceries(groceries);
		}

		currentHour = 0;

	}

	/*
	 * Constructor without astar traversal for testing purposes 
	 */

	public PersonAgent(String n){
		super();

		name = n;

		wallet = 100;
		bankaccountnumber = 0;
		busRide = new BusRide(5);

		//populate foods list -- need to make sure this matches up with market
		foodsToEat.add("Chicken");
		foodsToEat.add("Steak");
		foodsToEat.add("Salad");
		foodsToEat.add("Pizza");

		currentHour = 0;

	}

	public void setCityMap(CityMap c){	//for JUnit testing
		cityMap = c;
	}

	public CityMap getCityMap(){
		return cityMap;
	}
	
	public void setWallet(double amount){
		wallet = amount;
	}

	public String getName(){
		return name;
	}

	public HouseInterface getHouse(){
		return house;
	}

	public void setGoToRestaurant(){	//for testing purposes
		goToRestaurantTest = true;
	}

	public void msgAtDestination() {
		atDestination.release();
	}

	public void setGui(PersonGui g){
		gui = g;
		goHome(); // each person initially starts in their house
	}

	public void addRole(Role r, boolean active){
		roles.add(r);
		if(active){
			r.setActive(wallet);
		}
	}

	public void setRoleActive(Role r){
		synchronized(roles){
			for(Role role : roles){
				if(role == r){
					role.setActive(wallet);
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
		synchronized(tasks){
			/*for(PersonTask task : tasks){
			  		if(task.role.equals(r.getRoleName())){
			 			tasks.remove(task);
			 		}
			 }*/

			//This new way might not work every time so if it messes your code up just put it back to the comment out code above
			/*int taskSize= tasks.size();
			for(int i=0; i<taskSize; i++){
				if(tasks.get(0).role.equals(r.getRoleName())){
					tasks.remove(tasks.get(0));
				}
			}*/
			for(PersonTask task : tasks){
				log("Role name: " + r.getRoleName() + "   task role name: " + task.role);
				if(task.role.equals(r.getRoleName())){
					//tasks.remove(task);
					//this is called in reachedDestination
				}
			}
		}
		stateChanged();
	}

	public void addFirstJob(Role r, String location, int startTime){
		myJob = new Job(r, location);
		if(startTime != -1)
			myJob.workStartTime = startTime;
		r.setBuilding(location);
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

	//Takes a string argument and creates a new PersonTask which is added onto the current day's schedule
	public void addTask(String task){
		PersonTask t = new PersonTask(task);
		stateChanged();
		//Do we need this stateChanged()?
	}

	public void setClock(CityClock c){
		clock = c;
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
	public void msgTimeUpdate(int t, int hour, long minute, String am_pm){
		
		//if it's the last hour in the day, the tasks in the schedule for the day get transferred over to the next day
		if(hour == 23 && hour != currentHour){
			currentHour = hour;
			schedule.transferTodaysTasksToTomorrow(clock.getDayOfWeekNum());
		}
		if(!(myJob == null)){
			if(hour == myJob.workStartTime && minute < 15 && am_pm.equals("am") && myJob != null){
				if(!schedule.isTaskAlreadyScheduled(TaskType.goToWork, clock.getDayOfWeekNum())){
					PersonTask task = new PersonTask(TaskType.goToWork);
					schedule.addTaskToDay(clock.getDayOfWeekNum(), task);
					log("It's time for me to go to work!");
				}
			}
		}
		
		//Old way of doing jobs
		/*
		if(hour == 1 && minute < 15 && am_pm.equals("am") && myJob != null){
			if(myJob.role.getRoleName().contains("Rest")){
				if(!schedule.isTaskAlreadyScheduled(TaskType.goToWork, clock.getDayOfWeekNum())){
					PersonTask task = new PersonTask(TaskType.goToWork);
					schedule.addTaskToDay(clock.getDayOfWeekNum(), task);
					log("It's time for me to go to work!");
				}
			}
		}
		if(hour == 3 && minute < 15 && am_pm.equals("am") && myJob != null){
			if(!myJob.role.getRoleName().contains("Rest")){
				if(!schedule.isTaskAlreadyScheduled(TaskType.goToWork, clock.getDayOfWeekNum())){
					PersonTask task = new PersonTask(TaskType.goToWork);
					schedule.addTaskToDay(clock.getDayOfWeekNum(), task);
					log("It's time for me to go to work!");
				}
			}
		}*/
		
		//For paying rent
		if(hour == 2 && minute >= 15 && minute < 30 && am_pm.equals("am") && house.getName().contains("apart")){
			currentHour = hour;
			if(house.getName().contains("apart1")){
				msgRentDue(house.getLandlord(), 10.0);
			}
		}
		if(hour == 6 && minute < 15 && am_pm.equals("am") && (name.equals("rest1Test") || name.equals("rest2Test") || name.equals("rest4Test")
				|| name.equals("rest5Test") || name.equals("rest3Test") || name.equals("joe") || name.equals("brokenApplianceTest"))){
			if(!schedule.isTaskAlreadyScheduled(TaskType.goToWork, clock.getDayOfWeekNum())){
				PersonTask task = new PersonTask(TaskType.gotHungry);
				schedule.addTaskToDay(clock.getDayOfWeekNum(), task);
				log("I'm getting hungry.");
			}
		}
		else if(hour == 6 && minute >= 15 && minute < 30 && am_pm.equals("am") && (name.equals("bankCustomerTest"))){
			if(!schedule.isTaskAlreadyScheduled(TaskType.goToBank, clock.getDayOfWeekNum())){
				PersonTask task = new PersonTask(TaskType.goToBank);
				schedule.addTaskToDay(clock.getDayOfWeekNum(), task);
				log("I need to go to the bank");
			}
		} 
		else if(hour == 6 && minute >= 30 && minute < 45 && am_pm.equals("am") && (name.equals("bankCustomerTest1"))){

			wallet = 40;
			if(!schedule.isTaskAlreadyScheduled(TaskType.goToBank, clock.getDayOfWeekNum())){
				PersonTask task = new PersonTask(TaskType.goToBank);
				schedule.addTaskToDay(clock.getDayOfWeekNum(), task);
				log("It's time for me to go to bank.");
			}

		}
		else if(hour == 7 && minute < 15 && (name.equals("marketClient"))){
			if(!schedule.isTaskAlreadyScheduled(TaskType.goToMarket, clock.getDayOfWeekNum())){
				PersonTask task = new PersonTask(TaskType.goToMarket);
				task.role = "MarketCustomer";
				schedule.addTaskToDay(clock.getDayOfWeekNum(), task);
				log("It's time for me to buy something from the market.");
			}
		}
		
		/*Adds got hungry task
		 * Right now this is only for the test person
		 * */
		/*
		else if(hour == 3 && minute >= 40){
			currentHour = hour;
			PersonTask newTask = new PersonTask(TaskType.gotHungry);
			schedule.addTaskToDay(clock.getDayOfWeekNum(), newTask);
			log("Adding got hungry task");
		}
		*/
		else if(hour == 12 && minute >= 15 && minute < 30 && am_pm.equals("pm") && myJob == null){
			if(!schedule.isTaskAlreadyScheduled(TaskType.goToMarket, clock.getDayOfWeekNum())){
				PersonTask task = new PersonTask(TaskType.goToMarket);
				schedule.addTaskToDay(clock.getDayOfWeekNum(), task);
				log("It's time for me to buy something from the market.");
			}
		}
		stateChanged();
	}
	//From house
	public void msgImBroken(String type) {
		log("Oh no, my " + type + " broke!");
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
		log("YES, my food is done cooking!");
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

	public void msgApplianceBrokeCantCook(String food) {
		log("Oh no, my appliance broke, I'll have to try to make something else.");
		synchronized(meals){
			for(MyMeal m : meals){
				if(m.type == food){
					m.state = FoodState.done;
				}
			}
		}
		synchronized(tasks){
			tasks.add(new PersonTask(TaskType.gotHungry));
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
		log("Recieved message ImPickingYouUp from car");
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
		log("Yes! My " + appliance + " was fixed!");
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
		log("Oh, looks like its time for me to pay rent!");
		billsToPay.add(new Bill("rent", rate, r));
		stateChanged();
	}

	public void msgHereIsYourOrder(Car car){		//order for a car
		this.car = car;
		stateChanged();
	}

	public void msgHereIsYourOrder(TruckAgent t, MarketOrder order){ //Order for the cook role from a truck agent
		synchronized(roles) {
			for(Role r : roles) {
				if(r.getRoleName().contains("Cook") && r.isActive()) {
					if(r instanceof Restaurant1CookRole) {
						((Restaurant1CookRole) r).msgHereIsYourOrder(order);
					} else if(r instanceof Restaurant2CookRole) {
						((Restaurant2CookRole) r).msgHereIsYourOrder(order);
					} else if(r instanceof CookRole3) {
						//((CookRole3 r).msgHereIsYourOrder(order);
					} else if(r instanceof CookRole4) {
						((CookRole4) r).msgHereIsYourOrder(order);
					} else if(r instanceof Restaurant5CookRole) {
						//((Restaurant5CookRole) r).msgHereIsYourOrder(order);
					}
				}
			}
		}
	}

	//Bank
	public void msgSetBankAccountNumber(double num){
		accountNumber = num;
		log("I have a bank account now :" + accountNumber);
	}

	public void msgBalanceAfterDepositingIntoAccount(double balance){
		wallet = balance;
		log("My balance after depositing into my account:" + wallet);
	}

	public void msgBalanceAfterWithdrawingFromAccount(double balance){
		wallet = balance;
		log("My balance after withdrawing from my account:" + wallet);
	}

	public void msgBalanceAfterGetitngLoanFromAccount(double balance, double loan) {
		wallet = balance;
		loan = loan;
		log("My balance after getting loan from the bank:" + wallet + " and loan :" + loan);
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
		//ROLES - i.e. job or customer
				
		boolean anytrue = false;
		synchronized(roles){
			for(Role r : roles){
				if(r.isActive){
					anytrue = r.pickAndExecuteAnAction() || anytrue; // Changed by Grant
					return anytrue;
				}
			}
			//if(anytrue){
			//	return anytrue;
			//}

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
					goToMarket(t);
					t.state = State.processing;
					return true;
				}
			}
		}
		//Go to bank
		synchronized(tasks){
			for(PersonTask t: tasks){
				if(t.type == TaskType.goToBank && t.state == State.initial) {
					Do("I'm calling go to bank function");
					goToBank(t);
					t.state = State.processing;
					return true;
				}
			}
		}
		/*
		synchronized(tasks){
			boolean taskExists = false;
			for(PersonTask t : tasks){
				if(t.type == TaskType.goToBank){
					taskExists = true;
				}
			}
		}*/
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
		//go home if there is nothing else to do
		synchronized(tasks){
			if(tasks.isEmpty()){
				//log("Tasks is empty");
				List<PersonTask> dayTasks = schedule.getDayTasks(clock.getDayOfWeekNum());
				if(dayTasks.isEmpty()){
					//log("No more tasks in schedule");
					if(!atHome){
						if(house != null){
							goHome();
							return true;
						}
					}
				}
				else{
					for(PersonTask t : dayTasks){
						log("Task is " + t.type.toString());
					}
					tasks.add(dayTasks.get(0));
					log("Adding a new task " + dayTasks.get(0).type.toString());
					schedule.removeTaskFromDay(clock.getDayOfWeekNum(), dayTasks.get(0));
					return true;
				}
			}
		}
		return false;
	}


	//ACTIONS
	private boolean doesRoleListContain(String type){
		log("Checking if I'm a landlord");
		synchronized(roles){
			for(Role role : roles){
				if(role.getRoleName().contains("Landlord")){
					log("I think I'm a landlord...");
					//if(role.isActive){
					return true;
					//log("I'm going to go ahead and collect rent now, I'm not doing anything else...");
					//((LandlordRole)role).msgCollectRent();
					//}
				}
			}
		}
		return false;
	}

	public void goHome(){
		if(!atHome){
			//log("Going home");
			if(house != null){
				String location;
				if(house.getName().contains("apart1")){
					location = "apart1";
				}
				else if(house.getName().contains("apart2")){
					location = "apart2";
				}
				else{
					location = house.getName();
				}

				DoGoTo(location, new PersonTask(TaskType.goHome));
				//homeGui.goToBed();
			}	
			atHome= true;
		}
	}

	public void reachedDestination(PersonTask task){

		log("I've reached my destination, now I'm going to go inside!");
		log("My task right now is " + task.type.toString());
		Role role = null;
		synchronized(roles){
			if(task.role != null){
				log("The role name is " + task.role);
				for(Role r : roles){
					if(r.getRoleName().equals(task.role)){
						r.setActive(wallet);
						role = r;
						break;
					}
				}
			}
		}
		boolean isOpen;
		//This is if the person is going to the restaurant to eat
		if(task.location != null && task.location.contains("rest") && task.type == TaskType.gotHungry){
			String[] restNum = task.location.split("rest");
			if(role != null){
				isOpen= cityMap.msgHostHungryAtRestaurant(Integer.parseInt(restNum[1]), role);
				if(isOpen){
					role.getGui().setPresent(true);
				} else{
					role.setInactive();
					log("Oh no, the restaurant I want to go to is closed today!");
				}	
			}
			else{
				log("Looks like I don't have a role for this task. I can't go into the building.");
			}
		}
		else if(task.type == TaskType.goToWork){
			System.out.println("Starting job in 735 of personagent");
			myJob.startJob();
		}
 
		else if(task.type == TaskType.goToBank){
			log.add(new LoggedEvent("Decided to go to the bank"));
			if(role != null){
				if(cityMap.isBankOpen()){
					cityMap.bank.getBankManager().msgCustomerArrivedAtBank((BankCustomerRole) role);
					((BankCustomerRole)role).setGuiActive();
					isOpen= true;
				} else{
					role.setInactive();
					log("Oh no, the bank I want to go to is closed today!");
				}
			}
			else{
				log("Couldn't find the role for task " + task.type.toString());
			}
		}
		else if(task.type == TaskType.goToMarket){

			log("I should give the market manager my order!!!!!!!!!!!!!!!!!!!!!");
			String[] markNum = task.location.split("mark");
				//isOpen= cityMap.msgHostHungryAtRestaurant(Integer.parseInt(restNum[1]), role);
				
			if(role != null){
				//cityMap.mark1.getMarketManager().msgCustomerArrivedToMarket((MarketCustomerRole) role);
				isOpen= cityMap.msgMarketManagerArrivedToMarket(Integer.parseInt(markNum[1]), role);
				if(isOpen){
					((MarketCustomerRole)role).setGuiActive();
					role.getGui().setPresent(true);
				
					OrderItem oItem = new OrderItem("Chicken", 3);
					List<OrderItem> oItemList = new ArrayList<OrderItem>();
					oItemList.add(oItem);

					MarketOrder o = new MarketOrder(oItemList, this);
					log("Current order size in personagent pre-send is:" + o.orders.size());
					cityMap.msgMarketManagerHereIsOrder(Integer.parseInt(markNum[1]), o);
				} else{
					role.setInactive();
					log("Oh no, the market I want to go to is closed today!");
				}
			} else{
				log("Couldn't find the role for task " + task.type.toString());
			}

		}
		else if(task.type == TaskType.goToApartment){
			tasks.add(task);
			for(Role r : roles){
				if(r.getRoleName().contains("Landlord")){
					r.setActive(wallet);
					role = r;
					task.role= "LandlordRole";
					break;
				}
			}
			if(role != null){
				((LandlordRole)role).setGuiActive();
				role.getGui().setPresent(true);
				log("Time to work");
			}
		}
		else if(task.type == TaskType.goHome) {
			house.getAnimationPanel().addGui(homeGui);
			tasks.remove(task);
		}

		tasks.remove(task);
	}

	public void goToWork(PersonTask task){
		log("Going to work");

		task.location = myJob.location;
		System.out.println("Going to work as a " + task.role + " at " + task.location);
		//Role in the task here should be null because role-related things are taken care of in the Job class

		if(car != null){	//if the person has a car, he/she will take it
			takeCar(myJob.location);
			task.transportation = Transportation.car;
			task.state = State.inTransit;
		}
		else{
			DoGoTo(myJob.location, task);
		}
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
			if(!atHome){
				goHome();
			}
			homeGui.goToFridge();         
			try{
				atDestination.acquire();
			} catch (InterruptedException e){}
			int y = rand.nextInt(foodsToEat.size());
			String food = foodsToEat.get(y);
			house.checkFridge(food);
			//groceryList.add(food);
			/*homeGui.goToExit(); 
			try{
				atDestination.acquire();
			} catch (InterruptedException e){}
			 */
			//MarketOrder o= new MarketOrder(food, this);
			//log("IS THE MARKET MANAGER NULL? " + cityMap.market.mktManager);
			//cityMap.market.mktManager.msgHereIsOrder(o);

			// DoGoTo("mark1", PersonTask(TaskType.goToMarket));


		}
		else if(name.equals("brokenApplianceTest") || name.equals("Jess")){
			List<Food> groceries= new ArrayList<Food>();
			Food chicken= new Food("Chicken");
			groceries.add(chicken);

			if(!atHome){
				goHome();
			}
			homeGui.goToFridge();         
			try{
				atDestination.acquire();
			} catch (InterruptedException e){}
			house.boughtGroceries(groceries);
			house.checkFridge("Chicken");
		}
		//Else if they don't have to go to work, they will go to a restaurant
		else{
			int y = rand.nextInt(2);
			if(y == 0){
				goToRestaurant(task);
			}
			else{
				String food = foodsToEat.get(y);
				house.checkFridge(food);
				log("I'm going to eat " + food + " in my house.");
				log.add(new LoggedEvent("Decided to eat something from my house."));
			}
		}
	}

	/*
	 * Left this function alone for the most part, hopefully is still usable with Tom's tests
	 * Will need to change this later (maybe once testing is complete) to fit update
	 */
	public void goToBank(PersonTask task){
		//if(name.equals("bankCustomerTest")){
		print("Going to go to the bank");
		String bankName = null;
		Role role = null;
		synchronized(roles){
			for(Role r : roles){
				if(r instanceof BankCustomerRole) {
					//r.setActive();
					//This is hack for non norm
					//if(name.equals("bankCustomerTest1"))
					//((BankCustomerRole) r).amountofcustomermoney = 40;
					//This is hack for non norm
					if(name.equals("bankCustomerTest1")) {
						
					((BankCustomerRole) r).amountofcustomermoney = 40;
					((BankCustomerRole) r).bankaccountnumber = 1;
					}
					
					if(name.equals("bankCustomerTest1")) {
						
						((BankCustomerRole) r).amountofcustomermoney = 40;
						((BankCustomerRole) r).bankaccountnumber = 1;
					}
						
				
					r.setActive(wallet);
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
		//}
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
			String temp= Character.toString(location.charAt(4));
			int num= Integer.parseInt(temp);
			//String[] restNum = location.split("rest");  //This was not returning a valid number
			log("The number of the restaurant I am going to is " + num);
			//log("The number of the restaurant I am going to is " + restNum[0]);
			//String roleName = "Restaurant" + restNum[0] + "CustomerRole";
			String roleName = "Restaurant" + num + "CustomerRole";
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
		house.getLandlord().msgFixAppliance(this, a.type);
		a.state = ApplianceState.beingFixed;
	}

	public void payBills(){
		log.add(new LoggedEvent("Paying bill"));
		log("Paying bills");
		synchronized(billsToPay){
			for(Bill b : billsToPay){
				if(b.landlord == house.getLandlord()){
					if(wallet > b.amount){
						log.add(new LoggedEvent("The bill I'm paying is my rent"));
						house.getLandlord().msgHereIsMyRent(this, b.amount);
						wallet -= b.amount;
						billsToPay.remove(b);
						return;
					}
					else{
						synchronized(tasks){
							//tasks.add(new PersonTask(TaskType.goToBank));
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
		if(atHome){
			homeGui.goToExit();
			gui.setVisible();
			house.getAnimationPanel().notInHouse(homeGui);
			atHome = false;
		}
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
				if(t.location != null){
					if(t.location.equals(ride.destination)){
						task = t;
					}
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

	public void goToMarket(PersonTask task){

		log("I'm headed out to the market NOW!!!!!!!!!!!!!!!!!");
		if(atHome){
			log("At home, going to exit of house");
			homeGui.goToExit(); 
			atHome = false;
			try{
				atDestination.acquire();
			} catch (InterruptedException e){}
		}
		//String location = cityMap.getClosestPlaceFromHere(house.getName(), "mark");
		String location;
		Random rand = new Random();
		int num= rand.nextInt(3);
		if(num == 0)
			location= "mark1";
		else if(num == 1)
			location= "mark2";
		else
			location = "mark3";

		location = "mark1";

		// task.location = location;

		// Hack for testing
		task.location = location; 

		task.role = "MarketCustomerRole";

		if(car == null){
			log("location: " + location);
			DoGoTo(location, task);
		}
		else{
			takeCar(location);
		}
		task.state = State.inTransit;

		/*
		 * This was moved to reachedDestination() function
		MarketOrder o = new MarketOrder(groceryList.get(0), this);
		cityMap.market.mktManager.msgHereIsOrder(o);
		 */

	}

	public void takeCar(String destination){
		log("Taking car to destination " + destination);
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
		Food temp= new Food(meal.type);
		if(temp.appliance.equals("Stove")){
			homeGui.goToStove();
		} else if(temp.appliance.equals("Microwave")){
			homeGui.goToMicrowave();
		} else if(temp.appliance.equals("Oven")){
			homeGui.goToOven();
		}
		try{
			atDestination.acquire();
		} catch (InterruptedException e){}
		house.cookFood(meal.type);
		meal.state = FoodState.cooking;
	}

	public void eatMeal(final MyMeal m){
		log.add(new LoggedEvent("Eating meal"));
		log("My food is done cooking, eating my meal now");
		homeGui.goToTable();
		try{
			atDestination.acquire();
		} catch (InterruptedException e){}
		meals.remove(m);
		eatMeal.schedule(new TimerTask() {
			@Override public void run() {
				log("That meal was fabulous! I'm a GREAT cook, why don't people come visit me and try my cooking??");
				return;
			}}, 4000);
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

	public void DoGoTo(String location, PersonTask task) {

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
		int myX = currentPosition.getX();
		int myY = currentPosition.getY();

		if((Math.abs(myX - x) > 15) || Math.abs(myY - y) > 12) {
			if(!(x > 18 && myX > 18) && !(x < 3 && myX < 3) && !(y < 3 && myY < 3) && !(y > 15 && myY > 15)){	// || name.equals("BusTest")
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
		}

		if(task != null){
			if(task.transportation != Transportation.bus && task.transportation != Transportation.car)
				task.transportation = Transportation.walking;
		}

		moveTo(x, y);
		if(task != null) {
			task.state = State.arrived;
		}

		if((task != null) && (task.transportation == Transportation.walking)){
			reachedDestination(task);
		}

		gui.setInvisible();
		return;
	}

	void guiMoveFromCurrentPositionTo(Position to){
		//System.out.println("[Gaut] " + guiWaiter.getName() + " moving from " + currentPosition.toString() + " to " + to.toString());

		AStarNode aStarNode = (AStarNode)aStar.generalSearch(currentPosition, to);

		//If a path is not found, sleep for .5 seconds and then try again.
		while(aStarNode == null) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			aStarNode = (AStarNode)aStar.generalSearch(currentPosition, to);
		}

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

	public void setBank(Bank bank)
	{
		this.bank = bank;
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
			location = l;
			workStartTime = -1;
			workEndTime = -1;
			leaveForWork = -1;
		}

		public void startJob(){
			role.setActive(wallet);
			System.out.println("Setting role active" + role.getRoleName());
			workState = WorkState.atWork;
			System.out.println("at work, about to check for null");
			if(role.getGui() != null){
				System.out.println("NOT NULL!!!!");
				System.out.println("at work");
				role.getGui().setPresent(true);
			}
			if(role instanceof BankTellerRole) {
				log("Bank teller is at the bank");
				//bank.getBankManager().msgBankTellerArrivedAtBank((BankTellerRole) findrole);
				//this.setRoleActive(findrole);
				cityMap.msgArrivedAtBank(role);
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
			ActivityLog.getInstance().logActivity(tag, msg, name, true);
		}
		log.add(new LoggedEvent(msg));
	}

	public void setTesting(boolean t){
		test = true;
	}

	public void msgHereIsYourOrder(MarketOrder order) {
		log("Yay, I got my order back!");
		List<OrderItem> o = order.orders;

		log("Final: " + order.orders.size());

		Food f = new Food(o.get(0).name);

		List<Food> groceries = new ArrayList<Food>();
		groceries.add(f);
		house.boughtGroceries(groceries);
	}

}