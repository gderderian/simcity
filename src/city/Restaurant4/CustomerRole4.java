package city.Restaurant4;

import city.Restaurant4.HostRole4;
import city.Restaurant4.WaiterRole4;
import city.Restaurant4.WaiterRole4.Menu;
import city.gui.restaurant4.CustomerGui4;
import justinetesting.interfaces.Customer4;
import agent.Agent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

/**
 * Restaurant customer agent.
 */
public class CustomerRole4 extends Agent implements Customer4 {
	private String name;
	private String choice;
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
	private HostRole4 host;
	private WaiterRole4 waiter;
	private CashierRole4 cashier;
	
	// Finite State Machine requires both states and events
	public enum AgentState {none, waitingAtRest, decidingToStay, beingSeated, askingToOrder, deciding, eating, needBill, paying, gone};
	AgentState state= AgentState.none;
	public enum AgentEvent {none, gotHungry, fullRest, followHost, atTable, askedToOrder, reOrder, gotFood, doneEating, gotBill, gotChange, doneLeaving};
	AgentEvent event= AgentEvent.none;
	
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public CustomerRole4(String name){
		super();
		this.name = name;
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
	public void setHost(HostRole4 host) {
		this.host = host;
	}
	
	public void setWaiter(WaiterRole4 waiter){
		this.waiter= waiter;
	}

	public void setCashier(CashierRole4 cashier){
		this.cashier= cashier;
	}
	
	public WaiterRole4 getWaiter(){
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
	
	// MESSAGES
	public void gotHungry() {//from animation
		event= AgentEvent.gotHungry;
		stateChanged();
	}

	public void msgPositionInLine(int spot){
		customerGui.setxPos(dimensions + (spot * (dimensions + 5)));
		customerGui.setyPos(40);
		customerGui.setxDestination(dimensions + (spot * (dimensions + 5)));
		customerGui.setyDestination(40);
	}
	
	public void msgSitAtTable(int xTable, int yTable, WaiterRole4 waiter, Menu menu) {
		xDest= xTable;
		yDest= yTable;
		this.waiter= waiter;
		this.menu= menu;
		event= AgentEvent.followHost;
		stateChanged();
	}
	
	public void msgRestaurantFull(){
		event= AgentEvent.fullRest;
		stateChanged();
	}

	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event= AgentEvent.atTable;
		stateChanged();
	}
	
	public void msgWhatDoWant(){
		event= AgentEvent.askedToOrder;
		stateChanged();
	}
	
	public void msgWhatDoWant(String choice){
		state= AgentState.askingToOrder;
		event= AgentEvent.reOrder;
		stateChanged();
	}
	
	public void msgHereIsFood(String choice){
		if(choice.equals(this.choice)){
			print("Yay, I got what I ordered!");
			
		}
		else{
			print("This is NOT what I asked for, I'm giving this place 0 stars on Yelp!");
		}
		state= AgentState.deciding;
		event= AgentEvent.gotFood;
		stateChanged();
	}
	
	public void msgHereIsBill(double amount){
		amountOwed= amount;
		event= AgentEvent.gotBill;
		stateChanged();
	}
	
	public void msgHereIsChange(boolean finished){
		if(!finished){
			payNextTime= true;
			amountOwedNextTime= amountOwed;
		}
		event= AgentEvent.gotChange;
		stateChanged();
	}
	
	public void msgRestClosed(){
		closed= true;
	}
	
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event= AgentEvent.doneLeaving;
		state= AgentState.none;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
		if(closed && state != AgentState.none){
			leaveTable();
			state= AgentState.none;
			return false;
		}
		if(state == AgentState.none && event == AgentEvent.gotHungry){
			print("I'm hungry, maybe I should go to a restaurant.");
			state= AgentState.waitingAtRest;
			goToRestaurant();
			return true;
		}
		if(state == AgentState.waitingAtRest && event == AgentEvent.fullRest){
			print("The restaurant is full right now, should I wait it out?");
			state= AgentState.decidingToStay;
			decideIfStaying();
			return true;
		}
		if(state == AgentState.waitingAtRest && event == AgentEvent.followHost){
			print("I'm so glad im finally being seated!");
			state= AgentState.beingSeated;
			sitDown();
			return true;
		}
		if(state == AgentState.beingSeated && event == AgentEvent.atTable){
			print("I'm ready to order now!");
			state= AgentState.askingToOrder;
			askToOrder();
			return true;
		}
		if(state == AgentState.askingToOrder && event == AgentEvent.askedToOrder){
			print("Hmm... everything sounds so good, what should I choose?!");
			state= AgentState.deciding;
			order();
			return true;
		}
		if(state == AgentState.askingToOrder && event == AgentEvent.reOrder){
			print("Okay, let me pick something else...");
			state= AgentState.deciding;
			reOrder();
			return true;
		}
		if(state == AgentState.deciding && event == AgentEvent.gotFood){
			print("That looks delicious, I can't wait to eat it!");
			state= AgentState.eating;
			eatFood();
			return true;
		}
		if(state == AgentState.eating && event == AgentEvent.doneEating){
			print("I'm ready to pay for this delicious meal!");
			state= AgentState.needBill;
			askForBill();
			return true;
		}
		if(state == AgentState.needBill && event == AgentEvent.gotBill){
			print("I should take my money to the cashier.");
			state= AgentState.paying;
			payBill();
			leaveTable();
			return true;
		}
		if(state == AgentState.paying && event == AgentEvent.gotChange){
			print("Goodbye!");
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
			print("I don't have time to wait for a table to free up, maybe next time I'll stay.");
			host.msgLeavingRest(this);
			customerGui.DoExitRestaurant();
			state= AgentState.gone;
			event= AgentEvent.none;
			stateChanged();
			return;
		}
		print("The line isn't too long, I'll wait until a table is avaliable");
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
		if(name.equals("Steak")){
			choice= "Steak";
		}
		else if(name.equals("Chicken")){
			choice= "Chicken";
		}
		else if(name.equals("Salad")){
			choice= "Salad";
		}
		else if(name.equals("Pizza")){
			choice= "Pizza";
		}
		else if(name.equals("cheapestItem")){
			choice= "Salad";
		}
		else if(name.equals("noMoneyL")){
			leaveTable();
			return;
		}
		else{
			print("Not named after food :( I guess I'll just pick something randomly");
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
		print("MY NEW CHOICE THAT I CHOOSE TO ORDER IS: " + choice);
		waiter.msgHereIsChoice(this, newChoice);
	}
	
	private void eatFood() {
		Do("Eating Food");
		timer.schedule(new TimerTask() {
			public void run() {
				event= AgentEvent.doneEating;
				stateChanged();
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
		print("Now I have $" + cashOnHand );
		cashier.msgHereIsMoney(this, amount);
	}
	
	private void goToBank(){
		cashOnHand += 20.00;
	}
	
	private void leaveTable() {
		Do("Leaving.");
		waiter.msgDoneEating(this);
		customerGui.DoExitRestaurant();
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
}

