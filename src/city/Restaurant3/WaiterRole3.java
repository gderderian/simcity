package restaurant;

import agent.Agent;
import restaurant.gui.WaiterGui;
import restaurant.test.mock.EventLog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.Semaphore;
import javax.swing.Timer;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

/**
 * Restaurant Waiter Agent
 */
public class WaiterAgent extends Agent implements Waiter {
	
	static final int DEFAULT_BREAK_TIME = 15000;
	
	public List<MyCustomer> myCustomers; // Uses try/catch
	public HostAgent myHost;
	public CookAgent myCook;
	private String name;
	public CashierAgent myCashier;
	private boolean onBreak;
	public int homeX = 230;
	public int homeY = 230;
	
	private WaiterGui waiterGui;
	private Semaphore isAnimating = new Semaphore(0,true);
	Timer breakTimer;
	
	public EventLog log;
	
	public enum AgentState
	{DoingNothing, wantBreak, onBreak};
	private AgentState state = AgentState.DoingNothing;

	public enum AgentEvent
	{none, breakRequested, breakApproved, breakRejected};
	AgentEvent event = AgentEvent.none;
	
	public WaiterAgent(String name, int startX, int startY) {
		super();
		this.name = name;
		myCustomers = new ArrayList<MyCustomer>();
		onBreak = false;
		
		log = new EventLog();
		
		breakTimer = new Timer(DEFAULT_BREAK_TIME,
				new ActionListener() { public void actionPerformed(ActionEvent evt) {
					Do("Returning from break");
					waiterGui.returnFromBreak();
					notifyHostReturnedFromBreak();
					state = AgentState.DoingNothing;
					event = AgentEvent.none;
					onBreak = true;
					stateChanged();
		      }
		});
		
		homeX = startX;
		homeY = startY;
		
	}
	
	// Messages
	public void doneEating(Customer c) {
		Do("Received message from customer " + c.getCustomerName() + " that they are done eating.");
		try {
			for (MyCustomer cust : myCustomers) {
				if (cust.customer.equals(c)){
					cust.state = CustomerState.Done;
				}
			}
		} catch (ConcurrentModificationException doneEatingComod) {
			stateChanged();
		}
		stateChanged();
	}

	public void hereIsFood(int tableNum, String choice) {
		Do("Received message from cook that " + choice + " is now ready for table #" + tableNum + ".");
		try {
			for (MyCustomer cust : myCustomers) {
				if (cust.tableNum == tableNum){
					cust.state = CustomerState.FoodReady;
				}
			}
		} catch (ConcurrentModificationException hereIsFoodComod) {
			stateChanged();
		}	
		stateChanged();
	}

	public void msgSeatCustomer(Customer c, int tableNum, HostAgent h, int customerX, int customerY) {
		Do("Received message to seat customer " + c.getCustomerName() + " at table #" + tableNum + ".");
		myHost = h;
		MyCustomer customer = new MyCustomer(customerX, customerY);
		customer.customer = c;
		customer.tableNum = tableNum;
		myCustomers.add(customer);
		stateChanged();
	}
	
	public void readyToOrder(Customer c) {
		Do("Customer " + c.getName() + " is now ready to order.");
		try {
			for (MyCustomer cust : myCustomers) {
				if (cust.customer.equals(c)){
					cust.state = CustomerState.ReadyToOrder;
				}
			}
		} catch (ConcurrentModificationException readyToOrderComod) {
			stateChanged();
		}
		stateChanged();
	}
	
	public void hereIsMyChoice(String choice, Customer c) {
		Do("Received choice " + choice + " from customer " + c.getCustomerName() + ".");
		try {
			for (MyCustomer cust : myCustomers) {
				if (cust.customer.equals(c)){
					cust.choice = choice;
					cust.state = CustomerState.OrderedWaiting;
				}
			}
		} catch (ConcurrentModificationException hereIsMyChoiceComod) {
			stateChanged();
		}
		stateChanged();
	}
	
	public void ImDone(Customer c) {
		Do("Customer " + c.getCustomerName() + " has finished eating.");
		try {
			for (MyCustomer cust : myCustomers) {
				if (cust.customer.equals(c)){
					cust.state = CustomerState.Done;
				}
			}
		} catch (ConcurrentModificationException ImDoneComod) {
			stateChanged();
		}
		stateChanged();
	}
	
	public void needNewChoice(int tableNum, String choice) {
		Do("Customer at table #" + tableNum + " has to make a new food choice other than " + choice + ".");
		try {
			for (MyCustomer cust : myCustomers) {
				if (cust.tableNum == tableNum){
					cust.state = CustomerState.NeedNewChoice;
				}
			}
		} catch (ConcurrentModificationException needNewChoiceComod) {
			stateChanged();
		}
		stateChanged();
	}
	
	public void breakApproved(){
		Do("My break was approved by the host.");
		state = AgentState.onBreak;
		event = AgentEvent.breakApproved;
		onBreak = true;
		stateChanged();
	}
	
	public void breakRejected(){
		Do("My break was rejected by the host.");
		onBreak = false;
		state = AgentState.wantBreak;
		event = AgentEvent.breakRejected;
		stateChanged();
	}
	
	public void requestBreak(){
		Do("Requesting break from host.");
		state = AgentState.wantBreak;
		event = AgentEvent.none;
		stateChanged();
	}
	
	public void hereIsCheck(Customer c, double checkAmount){
		Do("Accepting check from customer " + c.getCustomerName() + " in the amount of $" + checkAmount + ".");
		try {
			for (MyCustomer cust : myCustomers) {
				if (cust.customer.equals(c)){
					cust.payAmount = checkAmount;
					cust.state = CustomerState.needCheckDelivered;
				}
			}
		} catch (ConcurrentModificationException hereIsCheckComod) {
			stateChanged();
		}
		stateChanged();
	}
	
	public void readyForCheck(Customer c){
		Do("Customer " + c.getCustomerName() + " is ready for and wants their check.");
		try {
			for (MyCustomer cust : myCustomers) {
				if (cust.customer.equals(c)){
					cust.state = CustomerState.wantCheck;
				}
			}
		} catch (ConcurrentModificationException readyForCheckComod) {
			stateChanged();
		}
		stateChanged();
	}

	// Scheduler
	protected boolean pickAndExecuteAnAction() {
		try {
			if (state == AgentState.wantBreak && event == AgentEvent.none){
				requestBreakFromHost();
				return true;
			}
			if (state == AgentState.onBreak && event == AgentEvent.breakApproved){
				beginBreak();
				return true;
			}
			if (state == AgentState.wantBreak && event == AgentEvent.breakRejected){
				processBreakRejection();
				return true;
			}
			for (MyCustomer c : myCustomers) {
				if (c.state == CustomerState.Waiting){
					seatCustomer(c);
					return true;
				}
			}
			for (MyCustomer c : myCustomers) {
				if (c.state == CustomerState.ReadyToOrder){
					takeOrder(c, c.tableNum);
					return true;
				}
			}
			for (MyCustomer c : myCustomers) {
				if (c.state == CustomerState.OrderedWaiting){
					sendToKitchen(c, c.choice);
					return true;
				}
			}
			for (MyCustomer c : myCustomers) {
				if (c.state == CustomerState.FoodReady){
					deliverOrder(c, c.choice);
					return true;
				}
			}
			for (MyCustomer c : myCustomers) {
				if (c.state == CustomerState.Done){
					goodbyeCustomer(c);
					return true;
				}
			}
			for (MyCustomer c : myCustomers) {
				if (c.state == CustomerState.NeedNewChoice){
					repickFood(c);
					return true;
				}
			}
			for (MyCustomer c : myCustomers) {
				if (c.state == CustomerState.wantCheck){
					Do("About to call request check function");
					requestCheckForCustomer(c);
					return true;
				}
			}
			for (MyCustomer c : myCustomers) {
				if (c.state == CustomerState.needCheckDelivered){
					deliverCheck(c);
					return true;
				}
			}
		} catch (ConcurrentModificationException c) {
			return false;
		}
		goHome();
		return false;
	}

	// Actions
	private void takeOrder(MyCustomer c, int tableNum){
		Do("Going to take order from customer " + c.customer.getName() + " at table #" + tableNum + ".");
		waiterGui.setDestination(c.customer.getGui().getX(), c.customer.getGui().getY());
		waiterGui.beginAnimate();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		c.customer.msgWhatDoYouWant();
		c.state = CustomerState.Ordering;
	}

	private void sendToKitchen(MyCustomer c, String choice){
		Do("Sending order " + choice + " from customer " + c.customer.getName() + " to kitchen.");
		c.state = CustomerState.WaitingForFood;
		waiterGui.setDestination(225, 390);
		waiterGui.beginAnimate();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		myCook.hereIsOrder(choice, this, c.tableNum);
	}

	public void seatCustomer(MyCustomer c){

		Do("Seating customer " + c.customer.getName() + ".");
		
		waiterGui.setDestination(c.pickupX + 20, c.pickupY + 20);
		waiterGui.beginAnimate();
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		c.customer.msgSitAtTable(new Menu(), this);
		
		int destX = 0, destY = 0;
		
		for (Table t : myHost.getTables()) {
			if (c.tableNum == t.tableNumber){
				destX = t.tableX;
				destY = t.tableY;
			}
		}
		
		c.customer.getGui().setDestination(destX, destY);
		
		waiterGui.setDestination(destX, destY);
		waiterGui.beginAnimate();
		
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		c.state = CustomerState.Seated;
		
	}
	
	public void deliverOrder(MyCustomer c, String choice){

		Do("Delivering order " + choice + " to customer " + c.customer.getName() + ".");
		
		waiterGui.setDestination(225, 390);
		waiterGui.beginAnimate();
		
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
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

		myCook.pickedUpFood(carryText);
		
		waiterGui.setCarryText(carryText);
		waiterGui.setDestination(c.customer.getGui().getX(), c.customer.getGui().getY());
		waiterGui.beginAnimate();
		
		try {
			isAnimating.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		c.customer.hereIsOrder(choice);
		c.state = CustomerState.Eating;
		waiterGui.setCarryText("");
		
	}
	
	public void goodbyeCustomer(MyCustomer c){
		Do("Removing customer " + c.customer.getName() + "from my lists and saying goodbye.");
		myCustomers.remove(c);
		myHost.decrementCustomer(this);
		c.customer.getHost().msgLeavingTable(c.customer);
	}
	
	private void goHome(){
		Do("Going back to home position as there are no tasks for me to do right now.");
		waiterGui.setDestination(homeX, homeY);
	}
	
	private void repickFood(MyCustomer c){
		Menu newMenu = new Menu();
		newMenu.removeItem(c.choice);
		Do("Telling customer " + c.customer.getCustomerName() + " they need to repick an item because their previous choice is not in stock (according to the cook).");
		c.customer.repickFood(newMenu);
		c.state = CustomerState.Ordering;
	}
	
	private void deliverCheck(MyCustomer c){
		double needToPay = 0;
		needToPay = c.payAmount;
		Do("Delivering check to customer " + c.customer.getCustomerName() + " of $" + needToPay + ".");
		c.customer.hereIsCheck(needToPay);
		c.state = CustomerState.payingCheck;
	}
	
	private void requestCheckForCustomer(MyCustomer c){
		Do("Requesting check from cashier.");
		myCashier.calculateCheck(this, c.customer, c.choice);
		c.state = CustomerState.waitingForCheck;
	}
	
	private void requestBreakFromHost(){
		Do("Requesting break from host.");
		myHost.wantBreak(this);
		event = AgentEvent.breakRequested;
		state = AgentState.wantBreak;
		waiterGui.requestedBreak();
	}
	
	private void processBreakRejection(){
		Do("Processing break rejection.");
		waiterGui.breakRejected();
		state = AgentState.DoingNothing;
		event = AgentEvent.none;
	}
	
	private void beginBreak(){
		Do("Beginning my break.");
		waiterGui.breakApproved();
		breakTimer.setRepeats(false);
		breakTimer.restart();
		breakTimer.start();
		event = AgentEvent.none;
	}
	
	// Misc. Utilities
	public enum CustomerState // Goes along with MyCustomer below
	{Waiting, Seated, ReadyToOrder, Ordering, OrderedWaiting, WaitingForFood, FoodReady, Eating, Done, NeedNewChoice, wantCheck, waitingForCheck, payingCheck, needCheckDelivered};
	
	public class MyCustomer {
		Customer customer;
		int tableNum;
		String choice;
		CustomerState state;
		double payAmount;
		int pickupX;
		int pickupY;
	
		MyCustomer(){
			state = CustomerState.Waiting;
			payAmount = 0;
		}
		
		MyCustomer(int X, int Y){
			state = CustomerState.Waiting;
			payAmount = 0;
			pickupX = X;
			pickupY = Y;
		}
		
	}
	
	public void releaseSemaphore(){
		isAnimating.release();
	}
	
	public boolean hasCustomer(Customer c){
		for (MyCustomer cust : myCustomers) {
			if (cust.customer.equals(c)){
				return true;
			}
		}
		return false;
	}
	
	public void notifyHostReturnedFromBreak(){
		myHost.returnedFromBreak(this);
	}
	
	// Accessors
	public String getName() {
		return name;
	}
	
	public Menu getMenu(){
		return new Menu();
	}
	
	public void setGui(WaiterGui g){
		waiterGui = g;
	}
	
	public void setHost(HostAgent h){
		myHost = h;
	}
	
	public int getNumCustomers() {
		return myCustomers.size();
	}
	
	public void setCook(CookAgent cook){
		myCook = cook;
	}
	
	public void setCashier(CashierAgent cashier){
		myCashier = cashier;
	}
	
	public WaiterGui getGui() {
		return waiterGui;
	}
	
	public boolean isOnBreak() {
		return onBreak;
	}
	
}