package city.Restaurant3;

import Role.Role;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import test.mock.LoggedEvent;
import activityLog.ActivityLog;
import activityLog.ActivityTag;
import city.PersonAgent;
import city.gui.Restaurant3.CustomerGui3;

import java.util.concurrent.Semaphore;
import java.util.Random;


/**
 * Restaurant customer agent.
 */
public class CustomerRole3 extends Role {
	
	String roleName = "Restaurant3CustomerRole";
	
	static final int DEFAULT_HUNGER_LEVEL = 3500;
	static final int DEFAULT_SIT_TIME = 5000;
	static final int DEFAULT_CHOOSE_TIME = 5000;
	static final int ORDER_ATTEMPT_THRESHOLD = 3;
	
	private String name;
	private String choice;
	private int hungerLevel = DEFAULT_HUNGER_LEVEL;
	Timer eatingTimer;
	Timer choosingTimer;
	private CustomerGui3 customerGui;
	private double money;
	private double needToPay;
	private int orderAttempts;
	
	private WaiterRole3 assignedWaiter;
	private Menu3 myMenu;
	private HostRole3 host;
	public CashierRole3 cashier;
	private boolean madeStayDecision = false;
	int homeX;
	int homeY;

	public test.mock.EventLog evtLog;

	public enum AgentState
	{DoingNothing, WaitingForSeat, BeingSeated, Seated, Ordering, WaitingForFood, Eating, Leaving, Choosing, CalledWaiter, RequestedCheck, Paying, restaurantFull, CantPay};
	private AgentState state = AgentState.DoingNothing;

	public enum AgentEvent 
	{none, gotHungry, followHost, begunEating, doneEating, doneLeaving, doneChoosing, seated, wantWaiter, receivedCheck, notPaid};
	AgentEvent event = AgentEvent.none;
	
	private Semaphore isAnimating = new Semaphore(0,true);

	PersonAgent person;
	
	ActivityTag tag = ActivityTag.RESTAURANT3CUSTOMER;
	
	public CustomerRole3(String name, int startX, int startY, PersonAgent p){
		
		super();
		this.name = name;
		choice = "";
		needToPay = 0;
		orderAttempts = 0;

		evtLog = new test.mock.EventLog();
		
		// Hack to set amount of money based on customer's name
		if (name.equals("reallycheap")){
			money = 1.00; // Can't afford anything
		} else if (name.equals("cheap")) {
			money = 2.55;
		} else if (name.equals("somemoney")) {
			money = 7.00;
		} else if (name.equals("lotsofmoney")) {
			money = 17.00;
		} else if (name.equals("tonsofmoney")) {
			money = 25.00;
		} else { // Default $15.00
			money = 15.00;
		}
		
		choosingTimer = new Timer(DEFAULT_CHOOSE_TIME,
				new ActionListener() { public void actionPerformed(ActionEvent evt) {
					choice = pickRandomItemWithinCost();
					event = AgentEvent.wantWaiter;
					person.stateChanged();
		      }
		});
		eatingTimer = new Timer(DEFAULT_HUNGER_LEVEL,
				new ActionListener() { public void actionPerformed(ActionEvent evt) {
					state = AgentState.DoingNothing;
					event = AgentEvent.doneEating;
					person.stateChanged();
		      }
		});
		
		homeX = startX;
		homeY = startY;
		
		person = p;
		
	}

	// Messages
	public void gotHungry() {
		log("I'm hungry.");
		event = AgentEvent.gotHungry;
		person.stateChanged();
	}

	public void msgSitAtTable(Menu3 m, WaiterRole3 w) {
		log("Received msgSitAtTable.");
		myMenu = m;
		assignedWaiter = w;
		event = AgentEvent.followHost;
		person.stateChanged();
	}
	
	public void msgWhatDoYouWant() {
		log("Received msgWhatDoYouWant.");
		event = AgentEvent.doneChoosing;
		person.stateChanged();
	}
	
	public void hereIsOrder(String choice) {
		log("Received food choice " + choice + ".");
		state = AgentState.Eating;
		person.stateChanged();
	}
	
	public void msgAnimationFinishedGoToSeat() {
		person.stateChanged();
	}
	
	public void msgAnimationFinishedLeaveRestaurant() {
		log("Done leaving restaurant.");
		event = AgentEvent.doneLeaving;
		person.stateChanged();
	}
	
	public void repickFood(Menu3 newMenu) {
		log("Need to repick my food choice.");
		myMenu = newMenu;
		state = AgentState.BeingSeated;
		event = AgentEvent.seated;
		person.stateChanged();
	}
	
	public void dispenseChange(double newMoney) {
		money = newMoney;
		log("Received change back, my new money amount is $" + newMoney + ".");
		person.stateChanged();
	}
	
	public void hereIsCheck(double amountDue) {
		log("Customer needs to pay $" + amountDue);
		needToPay = amountDue;
		event = AgentEvent.receivedCheck;
		person.stateChanged();
	}
	
	public void goToCorner() {
		log("I ordered something that I now can't afford to pay for. I'm going in the corner and will stay there forever.");
		state = AgentState.CantPay;
		event = AgentEvent.notPaid;
		person.stateChanged();
	}
	
	public void restaurantFull(){
		log("The host says the restaurant is full. I need to decide whether to stay or leave.");
		//if (madeStayDecision == false) {
			state = AgentState.restaurantFull;
		//}
		person.stateChanged();
	}
	
	// Scheduler
	public boolean pickAndExecuteAnAction() {
		
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry){
			state = AgentState.WaitingForSeat;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.DoingNothing && event == AgentEvent.followHost){ // was state check for waitingforseat
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		if (state == AgentState.BeingSeated && event == AgentEvent.seated){
			state = AgentState.Choosing;
			beginChoosing();
			return true;
		}
		if (state == AgentState.Choosing && event == AgentEvent.wantWaiter){
			tellWaiterReady();
			return true;
		}
		if (state == AgentState.CalledWaiter && event == AgentEvent.doneChoosing){
			sendChoiceToWaiter();
			state = AgentState.WaitingForFood;
			return true;
		}
		if (state == AgentState.Eating && event == AgentEvent.doneChoosing){
			beginEating();
			event = AgentEvent.begunEating;
			return true;
		}		
		if (state == AgentState.DoingNothing && event == AgentEvent.doneEating){
			state = AgentState.Leaving;
			sendReadyForCheck();
			return true;
		}
		if (state == AgentState.RequestedCheck && event == AgentEvent.receivedCheck){
			leaveRestaurant();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			returnToCity();
			state = AgentState.DoingNothing;
			event = AgentEvent.none;
			return true;
		}
		if (state == AgentState.Paying && event == AgentEvent.none){
			sendPayment();
			state = AgentState.Leaving;
			event = AgentEvent.doneLeaving;
			return true;
		}
		if (state == AgentState.WaitingForFood && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			event = AgentEvent.none;
			return true;
		}
		if (state == AgentState.CantPay && event == AgentEvent.notPaid){
			shame();
			state = AgentState.DoingNothing;
			event = AgentEvent.none;
			return true;
		}
		if (state == AgentState.restaurantFull && event == AgentEvent.gotHungry){
			determineIfStay();
			event = AgentEvent.none;
			return true;
		}
		return false;
	}

	// Actions
	private void returnToCity(){
		person.setRoleInactive(this);
		person.setGuiVisible();
	}
	
	private void tellWaiterReady(){
		log("Telling waiter that I'm ready to order.");
		assignedWaiter.readyToOrder(this);
		state = AgentState.CalledWaiter;
	}
	
	private void sendReadyForCheck(){
		log("Telling waiter I want my check because I'm ready to leave.");
		assignedWaiter.readyForCheck(this);
		state = AgentState.RequestedCheck;
	}
	
	private void sendChoiceToWaiter(){
		
		// Customer can't afford anything on menu within their price range because choice was set to blank
		if (choice.equals("")) { // Customer cannot afford any items on the menu. Make them leave.
			log("I can't afford anything that's on the menu given to me with the money I currently have ($" + money + "). I'm leaving!");
			leaveAbruptly();
			return;
		}
		
		// Customer leaves if there is nothing for them to order
		if (orderAttempts > ORDER_ATTEMPT_THRESHOLD){ // Nothing is left on the menu for the customer to order. Make them leave.
			log("There's nothing for me to order from the restaurant anymore. I'm leaving!");
			leaveAbruptly();
			return;
		}
		
		assignedWaiter.hereIsMyChoice(choice, this);
		orderAttempts++;
		
		String carryText = "";
		switch(choice){
		case "Chicken":
			carryText = "CHK";
			break;
		case "Mac & Cheese":
			carryText = "M&C";
			break;
		case "French Fries":
			carryText = "FRF";
			break;
		case "Pizza":
			carryText = "PZA";
			break;
		case "Pasta":
			carryText = "PST";
			break;
		case "Cobbler":
			carryText = "CBL";
			break;
		}
		customerGui.setCarryText(carryText + "?");
		
		log("Sending food choice " + choice + " to waiter.");
	}
	
	private void beginChoosing(){
		choosingTimer.setRepeats(false);
		choosingTimer.restart();
		choosingTimer.start();
	}
	
	public void goToRestaurant() {
		log("Going to restaurant and telling host that I'm hungry. I currently have $" + money + ".");
		customerGui.setDestination(homeX, homeY);
		customerGui.beginAnimate();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		host.msgIWantFood(this, homeX, homeY);
	}

	private void SitDown() {
		log("Going to sit down.");
		customerGui.beginAnimate();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		event = AgentEvent.seated;
	}
	
	private void beginEating() {
		log("Beginning to eat food.");
		String carryText = "";
		switch(choice){
		case "Chicken":
			carryText = "CHK";
			break;
		case "Mac & Cheese":
			carryText = "M&C";
			break;
		case "French Fries":
			carryText = "FRF";
			break;
		case "Pizza":
			carryText = "PZA";
			break;
		case "Pasta":
			carryText = "PST";
			break;
		case "Cobbler":
			carryText = "CBL";
			break;
		}
		customerGui.setCarryText(carryText);
		eatingTimer.setRepeats(false);
		eatingTimer.restart();
		eatingTimer.start();
		log("Beginning to eat food.");
	}

	private void leaveRestaurant() {
		log("Leaving restaurant.");
		customerGui.setCarryText("");
		customerGui.DoExitRestaurant();
		assignedWaiter.ImDone(this);
		orderAttempts = 0;
		state = AgentState.Paying;
		event = AgentEvent.none;
	}
	
	private void leaveAbruptly() {
		log("Leaving restaurant.");
		customerGui.setCarryText("");
		customerGui.DoExitRestaurant();
		assignedWaiter.ImDone(this);
		orderAttempts = 0;
		event = AgentEvent.doneLeaving;
	}
	
	private void sendPayment(){
		log("Sending my payment of $" + money + " to cashier.");
		cashier.acceptPayment(this, money);
	}
	
	private void determineIfStay(){
		Random random = new Random();
		
		boolean willStay = true;
		
		if (madeStayDecision == false){
			willStay = random.nextBoolean();
		}
		
	    if (willStay == true){
	    	state = AgentState.WaitingForSeat;
	    	event = AgentEvent.gotHungry;
	    	//host.msgIWantFood(this);
	    	log("The restaurant is currently full according to the host, but I'll continue to stay and wait.");
	    	madeStayDecision = true;
	    } else {
	    	log("The restaurant is currently full according to the host and I don't want to wait so I'm leaving.");
	    	state = AgentState.DoingNothing;
	    	event = AgentEvent.none;
	    	customerGui.resetNotHungry();
	    	host.imLeaving(this);
	    	madeStayDecision = true;
	    }
		
	}

	// Accessors
	public String getName() {
		return name;
	}
	
	public HostRole3 getHost() {
		return host;
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

	public void setGui(CustomerGui3 g) {
		customerGui = g;
	}

	public CustomerGui3 getGui() {
		return customerGui;
	}
	
	public void setHost(HostRole3 host) {
		this.host = host;
	}

	public String getCustomerName() {
		return name;
	}
	
	public void assignWaiter(WaiterRole3 w) {
		assignedWaiter = w;
	}
	
	public void setCashier(CashierRole3 c) {
		cashier = c;
	}
	
	public double getNeedToPay() {
		return needToPay;
	}

	public void shame() {
		customerGui.goInCorner();
	}
	
	// Misc. Utilities
	public String pickRandomItem() {
		return myMenu.pickRandomItem();
	}
	
	public String pickRandomItemWithinCost() {
		if (name.equals("Chicken")){
			return "Chicken";
		} else if (name.equals("Mac & Cheese")) {
			return "Mac & Cheese";
		} else if (name.equals("French Fries")) {
			return "French Fries";
		} else if (name.equals("Pizza")) {
			return "Pizza";
		} else if (name.equals("Pasta")) {
			return "Pasta";
		} else if (name.equals("Cobbler")) {
			return "Cobbler";
		} else {
			return myMenu.pickRandomItemWithinCost(money);
		}
	}
	
	public void releaseSemaphore(){
		isAnimating.release();
	}
	
	private void log(String msg){
		print(msg);
        ActivityLog.getInstance().logActivity(tag, msg, name);
	}

	@Override
	public String getRoleName() {
		return roleName;
	}
	
}