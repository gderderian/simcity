package restaurant1;

import Role.MarketManagerRole;
import Role.Role;
import activityLog.ActivityLog;
import activityLog.ActivityTag;
import agent.Agent;
import restaurant1.Restaurant1Order.orderState;
import restaurant1.gui.Restaurant1CookGui;
import test.mock.LoggedEvent;

import java.util.*;
import java.util.concurrent.Semaphore;

import city.MarketOrder;
import city.OrderItem;
import city.PersonAgent;
import city.transportation.TruckAgent;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class Restaurant1CookRole extends Role {
	
	String roleName = "Restaurant1CookRole";
	
	public List<Restaurant1Order> orders = Collections.synchronizedList(new ArrayList<Restaurant1Order>());

	private MarketManagerRole market;
	
	int orderCount = 0;
	Restaurant1OrderWheel orderWheel;
	
	private enum foodOrderingState { notYetOrdered, ordered };
	
	boolean restaurantOpening = true; // A bool to deal with the initial inventory check when restaurant opens.
	
	boolean needToReorder = false;
	
	private String name;
	
	PersonAgent person;
	
	Timer timer = new Timer();
	
	Timer timer2 = new Timer();
	
	private Map<String, Food> foods = Collections.synchronizedMap(new HashMap<String, Food>());

	private Semaphore atDestination = new Semaphore(0, true); // For gui movements
	
	public Restaurant1CookGui cookGui = null;
	
	ActivityTag tag = ActivityTag.RESTAURANT1COOK;
	
	public Restaurant1CookRole(String name, PersonAgent p) {
		super();
		building = "rest1";
		
		person = p;
		
				// usage: new Food(String type, int cookTime, int amount, int low, int capacity);
		foods.put("steak", new Food("steak", 6, 5, 5, 8));
		foods.put("fish", new Food("fish", 4, 5, 5, 8));
		foods.put("chicken", new Food("chicken", 3, 5, 5, 8));
		
		this.name = name;
		
		orderWheel = Restaurant1OrderWheel.getInstance();
	}

	public String getName() {
		return name;
	}
	
	public void setGui(Restaurant1CookGui gui) {
		cookGui = gui;
	}
	
	public void setOrderWheel(Restaurant1OrderWheel wheel) {
		orderWheel = wheel;
	}

	// Messages
	public void msgHereIsOrder(Restaurant1Order o) {
		o.setNumber(orderCount++);
		orders.add(o);
		person.stateChanged();
	}
	
	public void msgFoodDoneCooking(Restaurant1Order o) {
		o.s = orderState.cooked;
		person.stateChanged();
	}
	
	public void msgPickedUpOrder(int orderNumber) {
		synchronized(orders) {
			for(Restaurant1Order o : orders) {
				if(o.orderNumber == orderNumber) {
					o.s = orderState.pickedUp;
				}
			}
		}
		person.stateChanged();
	}
	
	public void msgHereIsYourOrder(MarketOrder o) {
		List<OrderItem> orderItems = o.getOrders();
		for(int i = 0; i < orderItems.size(); i++) {
				OrderItem tempOrder = orderItems.get(i);
				String type = tempOrder.getName();
				int amount = tempOrder.getQuantity();
				
				Food tempFood = foods.get(type);
				tempFood.state = foodOrderingState.notYetOrdered;
				log("Received delivery of " + amount + " units of " + type);
				tempFood.amount += amount;
		}
	}
	
	public void msgHereIsBill(MarketManagerRole m, double amount) {
		log("Received a bill! What do I do?!");
	}
	
	public void msgRecheckInventory() {
		Food temp = foods.get("steak");
		temp.amount = temp.low - 1;
		
		temp = foods.get("fish");
		temp.amount = temp.low - 1;
		
		temp = foods.get("chicken");
		temp.amount = temp.low - 1;
		
		restaurantOpening = true;
		person.stateChanged();
	}
	
	public void msgAtDestination() {
		atDestination.release();
		person.stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if(restaurantOpening) {
			initialInventoryCheck();
			checkOrderWheel();
			return true;
		}
		
		if(needToReorder) {
			//reorderFood();
			needToReorder = false;
			return true;
		}
		
		synchronized(orders) {
			for(Restaurant1Order o : orders) {
				if(o.s == orderState.pickedUp) {
					finishIt(o);
					return true;
				}
			}
		}

		synchronized(orders) {
			for(Restaurant1Order o : orders) {
				if(o.s == orderState.cooked) {
					plateIt(o);
					return true;
				}
			}
		}

		synchronized(orders) {
			for(Restaurant1Order o : orders) {
				if(o.s == orderState.pending) {
					cookIt(o);
					return true;
				}
			}
		}
		
		DoGoToHome();

		return false;
	}

	// Actions

	private void cookIt(final Restaurant1Order o) {
		//Animation
		//DoCooking(o) 

		
		Food thisFood = foods.get(o.choice);
		
		DoGoToFridge();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(thisFood.amount == 0) {
			log("We're all out of " + o.choice + "!");
			
			o.w.msgOutOf(o.choice, o.table);
			
			o.s = orderState.finished;
			
			if(thisFood.state != foodOrderingState.ordered) {
				orderMoreFood();
			}

			return;
		}
		
		if(thisFood.amount <= thisFood.low) {
			orderMoreFood();
		}
		

		cookGui.msgNewOrder(o.choice, o.orderNumber);
		
		log("Cooking up an order of " + o.choice + "!");
		
		DoGoToGrill();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		o.s = orderState.cooking;
		thisFood.amount--;
		cookGui.msgOrderCooking(o.orderNumber);
		
		int cookTime = thisFood.cookingTime * 1000;
				
		timer.schedule(new TimerTask() {
							public void run() {
								 msgFoodDoneCooking(o);
							}
						}, cookTime	);
	}

	private void plateIt(Restaurant1Order o) {
		DoGoToGrill();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cookGui.msgOrderBeingCarried(o.orderNumber);
		
		o.s = orderState.finished;
		log(o.choice + " done cooking, time to plate it!");
		
		DoGoToCounter();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		cookGui.msgOrderWaiting(o.orderNumber);
		o.w.msgOrderDone(o.choice, o.table, o.orderNumber);
	}
	
	private void finishIt(Restaurant1Order o) {
		cookGui.msgOrderPickedUp(o.orderNumber);
		o.s = orderState.finished;
	}
	
	private void initialInventoryCheck() {
		log("Checking initial inventory levels.");
		Food steak = foods.get("steak");
		Food chicken = foods.get("chicken");
		Food fish = foods.get("fish");
		
		if((steak.amount < steak.low) || (chicken.amount < chicken.low) || (fish.amount < fish.low)) {
			orderMoreFood();
		} else {
			log("All foods are in stock! We're ready to go!");
		}
		restaurantOpening = false;
	}
	
	private void orderMoreFood() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<OrderItem> orderList = Collections.synchronizedList(new ArrayList<OrderItem>());
		
		Food temp = foods.get("steak");
		if(temp.amount < temp.low && temp.state == foodOrderingState.notYetOrdered) {
			orderList.add(new OrderItem(temp.type, temp.capacity - temp.amount));
			temp.state = foodOrderingState.ordered;
		}
		
		temp = foods.get("chicken");
		if(temp.amount < temp.low && temp.state == foodOrderingState.notYetOrdered) {
			orderList.add(new OrderItem(temp.type, temp.capacity - temp.amount));
			temp.state = foodOrderingState.ordered;
		}
		
		temp = foods.get("fish");
		if(temp.amount < temp.low && temp.state == foodOrderingState.notYetOrdered) {
			orderList.add(new OrderItem(temp.type, temp.capacity - temp.amount));
			temp.state = foodOrderingState.ordered;
		}
		
		if(orderList.isEmpty()) {
			return;
		}
		
		MarketOrder newOrder = new MarketOrder(orderList, "rest1", this.person);
		
		log("Sending order for more food to the market!");
		market.msgHereIsTruckOrder(newOrder);
	}

	private void checkOrderWheel() {
		Restaurant1Order temp = orderWheel.getOrder();
		
		if(temp != null) {
			temp.setNumber(orderCount++);
			orders.add(temp);
			person.stateChanged();
		}
		
		timer2.schedule(new TimerTask() {
			public void run() {
				checkOrderWheel();
			}
		}, 5000	);
	}
	
	private void DoGoToHome() {
		cookGui.DoGoToHome();
	}
	
	private void DoGoToFridge() {
		cookGui.DoGoToFridge();
	}
	
	private void DoGoToGrill() {
		cookGui.DoGoToGrill();
	}
	
	private void DoGoToCounter() {
		cookGui.DoGoToCounter();
	}
	
	public void addMarket(MarketManagerRole m) {
		market = m;
	}
	
	public void foodShortage() {
		foods.get("steak").amount = 0;
		initialInventoryCheck();
	}
	
	private class Food {
		String type;
		int cookingTime;
		int amount;
		int low;
		int capacity;
		foodOrderingState state;
		
		Food(String type, int cookingTime, int amount, int low, int capacity) {
			this.type = type;
			this.cookingTime = cookingTime;
			this.amount = amount;
			this.low = low;
			this.capacity = capacity;
			state = foodOrderingState.notYetOrdered;
		}
	}
	
	private void log(String msg){
		print(msg);
        ActivityLog.getInstance().logActivity(tag, msg, name, false);
	}

	@Override
	public String getRoleName() {
		return roleName;
	}

	@Override
	public PersonAgent getPerson() {
		return person;
	}
	
}

