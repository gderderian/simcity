package city.Restaurant4;

import activityLog.ActivityLog;
import activityLog.ActivityTag;
import city.PersonAgent;
import city.Restaurant4.HostRole4;
import city.Restaurant4.WaiterRole4;
import city.Restaurant4.WaiterRole4.Menu;
import city.gui.restaurant4.CustomerGui4;
import justinetesting.interfaces.Cashier4;
import justinetesting.interfaces.Customer4;
import justinetesting.interfaces.Host4;
import justinetesting.interfaces.Waiter4;
import Role.Role;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

import test.mock.LoggedEvent;

/**
 * Restaurant customer agent.
 */
public class CustomerRole4 extends Role implements Customer4 {
	
	String roleName = "Restaurant4CustomerRole";
	
	private String name;
	private String choice;
	PersonAgent p;
	private Menu menu;
	private int hungerLevel = 10;        // determines length of meal
	private int xDest;
	private int yDest;
	private double amountOwed;
	private double cashOnHand;
	private static final int randSelector= 4;
	private static final int timeToEat= 750;
	private static final int dimensions= 20;
	Timer timer = new Timer();
	private CustomerGui4 customerGui;
	private boolean payNextTime= false;
	private boolean  closed= false;
	private double amountOwedNextTime= 0;
	double amount= 0;

	// agent correspondents
	private Host4 host;
	private Waiter4 waiter;
	private Cashier4 cashier;
	
	// Finite State Machine requires both states and events
	public enum AgentState {none, waitingAtRest, decidingToStay, beingSeated, askingToOrder, deciding, eating, needBill, paying, gone};
	AgentState state= AgentState.none;
	public enum AgentEvent {none, gotHungry, fullRest, followHost, atTable, askedToOrder, reOrder, gotFood, doneEating, gotBill, gotChange, doneLeaving};
	AgentEvent event= AgentEvent.none;
	
	ActivityTag tag = ActivityTag.RESTAURANT4CUSTOMER;
	
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param cookGui  reference to the customergui so the customer can send it messages
	 */
	public CustomerRole4(String name, PersonAgent p){
		super();
		building = "rest4";
		this.name = name;
		this.p= p;
		if(name.equals("cheapestItem")){
			cashOnHand= 5.99;
		}
		else if(name.equals("noMoneyL") || name.equals("noMoneyS")){
			cashOnHand= 0;
		}
		else{	
			cashOnHand= 40.00;
		}
		amountOwed= 0;
	}

	/**
	 * hack to establish connection to other agents.
	 */
	public void setHost(Host4 host) {
		this.host = host;
	}
	
	public void setWaiter(Waiter4 waiter){
		this.waiter= waiter;
	}

	public void setCashier(Cashier4 cashier){
		this.cashier= cashier;
	}
	
	public Waiter4 getWaiter(){
		return waiter;
	}
	
	public String getCustomerName() {
		return name;
	}
	
	public String getState(){
		return state.toString();
	}
	
	public String getChoice(){
		return choice;
	}
	
	public void setGuiActive() {
		//goToRestaurant();
		customerGui.setPresent(true);
	}
	
	
	// MESSAGES
	public void gotHungry() {//from animation
		event= AgentEvent.gotHungry;
		p.stateChanged();
	}

	public void msgPositionInLine(int spot){
		customerGui.setxPos(dimensions + (spot * (dimensions + 5)));
		customerGui.setyPos(40);
		customerGui.setxDestination(dimensions + (spot * (dimensions + 5)));
		customerGui.setyDestination(40);
	}
	
	public void msgSitAtTable(int xTable, int yTable, Waiter4 waiter, Menu menu) {
		log("Time to sit down!");
		xDest= xTable;
		yDest= yTable;
		this.waiter= waiter;
		this.menu= menu;
		state= AgentState.waitingAtRest;
		event= AgentEvent.followHost;
		p.stateChanged();
	}
	
	public void msgRestaurantFull(){
		event= AgentEvent.fullRest;
		p.stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event= AgentEvent.atTable;
		p.stateChanged();
	}
	
	public void msgWhatDoWant(){
		event= AgentEvent.askedToOrder;
		p.stateChanged();
	}
	
	public void msgWhatDoWant(String choice){
		state= AgentState.askingToOrder;
		event= AgentEvent.reOrder;
		p.stateChanged();
	}
	
	public void msgHereIsFood(String choice){
		if(choice.equals(this.choice)){
			log("Yay, I got what I ordered!");
			
		}
		else{
			log("This is NOT what I asked for, I'm giving this place 0 stars on Yelp!");
		}
		state= AgentState.deciding;
		event= AgentEvent.gotFood;
		p.stateChanged();
	}
	
	public void msgHereIsBill(double amount){
		amountOwed= amount;
		event= AgentEvent.gotBill;
		p.stateChanged();
	}
	
	public void msgHereIsChange(boolean finished){
		if(!finished){
			payNextTime= true;
			amountOwedNextTime= amountOwed;
		}
		event= AgentEvent.gotChange;
		p.stateChanged();
	}
	
	public void msgRestClosed(){
		closed= true;
	}
	
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event= AgentEvent.doneLeaving;
		state= AgentState.none;
		p.stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
		if(closed && state != AgentState.none){
			leaveTable();
			state= AgentState.none;
			return false;
		}
		if(state == AgentState.none && event == AgentEvent.gotHungry){
			log("I'm hungry, maybe I should go to a restaurant.");
			state= AgentState.waitingAtRest;
			goToRestaurant();
			return true;
		}
		if(state == AgentState.waitingAtRest && event == AgentEvent.fullRest){
			log("The restaurant is full right now, should I wait it out?");
			state= AgentState.decidingToStay;
			decideIfStaying();
			return true;
		}
		if(state == AgentState.waitingAtRest && event == AgentEvent.followHost){
			log("I'm so glad im finally being seated!");
			state= AgentState.beingSeated;
			sitDown();
			return true;
		}
		if(state == AgentState.beingSeated && event == AgentEvent.atTable){
			log("I'm ready to order now!");
			state= AgentState.askingToOrder;
			askToOrder();
			return true;
		}
		if(state == AgentState.askingToOrder && event == AgentEvent.askedToOrder){
			log("Hmm... everything sounds so good, what should I choose?!");
			state= AgentState.deciding;
			order();
			return true;
		}
		if(state == AgentState.askingToOrder && event == AgentEvent.reOrder){
			log("Okay, let me pick something else...");
			state= AgentState.deciding;
			reOrder();
			return true;
		}
		if(state == AgentState.deciding && event == AgentEvent.gotFood){
			log("That looks delicious, I can't wait to eat it!");
			state= AgentState.eating;
			eatFood();
			return true;
		}
		if(state == AgentState.eating && event == AgentEvent.doneEating){
			log("I'm ready to pay for this delicious meal!");
			state= AgentState.needBill;
			askForBill();
			return true;
		}
		if(state == AgentState.needBill && event == AgentEvent.gotBill){
			log("I should take my money to the cashier.");
			state= AgentState.paying;
			payBill();
			leaveTable();
			return true;
		}
		if(state == AgentState.paying && event == AgentEvent.gotChange){
			log("Goodbye!");
			state= AgentState.gone;
			return true;
		}
		return false;
	}

	// ACTIONS
	private void goToRestaurant() {
		Do("Going to restaurant");
		host.msgIWantFood(this);//send our instance, so he can respond to us
	}

	private void decideIfStaying(){
		Random rand = new Random();
		int num= rand.nextInt(randSelector);
		if(num == 0){
			log("I don't have time to wait for a table to free up, maybe next time I'll stay.");
			host.msgLeavingRest(this);
			customerGui.DoExitRestaurant();
			state= AgentState.gone;
			event= AgentEvent.none;
			p.stateChanged();
			return;
		}
		log("The line isn't too long, I'll wait until a table is avaliable");
		state= AgentState.waitingAtRest;
		event= AgentEvent.gotHungry;
	}
	
	private void sitDown() {
		Do("Being seated. Going to table");
		customerGui.DoGoToSeat(1, xDest, yDest);
	}

	private void askToOrder(){
		waiter.msgReadyToOrder(this);
	}
	
	private void order(){
		if(name.equals("Eggs")){
			choice= "Eggs";
		}
		else if(name.equals("Waffels")){
			choice= "Waffles";
		}
		else if(name.equals("Pancakes")){
			choice= "Pancakes";
		}
		else if(name.equals("Bacon")){
			choice= "Bacon";
		}
		else if(name.equals("cheapestItem")){
			choice= "Bacon";
		}
		else if(name.equals("noMoneyL")){
			leaveTable();
			return;
		}
		else{
			log("Not named after food :( I guess I'll just pick something randomly");
			Random rand = new Random();
			int num= rand.nextInt(randSelector);
			choice = menu.select(num);
		}
		waiter.msgHereIsChoice(this, choice);
	}
	
	private void reOrder(){
		if(name.equals("cheapestItem")){
			leaveTable();
			return;
		}
		String newChoice= choice;
		while(choice == newChoice){
			Random rand = new Random();
			int num= rand.nextInt(randSelector);
			newChoice = menu.select(num);
		}
		choice= newChoice;
		log("MY NEW CHOICE THAT I CHOOSE TO ORDER IS: " + choice);
		waiter.msgHereIsChoice(this, newChoice);
	}
	
	private void eatFood() {
		Do("Eating Food");
		timer.schedule(new TimerTask() {
			public void run() {
				event= AgentEvent.doneEating;
				p.stateChanged();
			}
		},
		getHungerLevel() * timeToEat); //how long to wait before running task
	}

	private void askForBill(){
		waiter.msgReadyForBill(this);
	}
	
	private void payBill(){
		//double amount= 0;
		if(payNextTime){
			goToBank();
			amount += amountOwedNextTime;
		}
		amount += amountOwed;
		if( cashOnHand - amount >= 0){
			cashOnHand -= amount;
		}
		else{
			amount= 0;
			amountOwedNextTime= 0;
			payNextTime= false;
		}
		log("Now I have $" + cashOnHand);
		waiter.getCashier().msgHereIsMoney(this, amount); 
	}
	
	private void goToBank(){
		cashOnHand += 20.00;
	}
	
	private void leaveTable() {
		Do("Leaving.");
		waiter.msgDoneEating(this);
		customerGui.DoExitRestaurant();
		p.setGuiVisible();
		p.setRoleInactive(this);
	}

	// Accessors, etc.
	public String getName() {
		return name;
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}

	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui4 g) {
		customerGui = g;
	}

	public CustomerGui4 getGui() {
		return customerGui;
	}
	
	private void log(String msg){
		print(msg);
        ActivityLog.getInstance().logActivity(tag, msg, name);
	}

	@Override
	protected String getRoleName() {
		return roleName;
	}
}

