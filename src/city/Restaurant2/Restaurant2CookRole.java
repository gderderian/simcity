package city.Restaurant2;

import interfaces.Restaurant2Cook;
import interfaces.Restaurant2Market;
import interfaces.Restaurant2Waiter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import city.OrderItem;
import city.gui.restaurant2.Restaurant2CookGui;
import Role.Role;

public class Restaurant2CookRole extends Role implements Restaurant2Cook {
	
	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	enum OrderState {pending, cooking, done, sent};
	enum FoodOrderState {Ordered, notOrdered};
	Timer timer = new Timer();
	Map<String, Food> foods = new HashMap<String, Food>();
	String name;
	List<ShipmentOrder> shipmentOrders = Collections.synchronizedList(new ArrayList<ShipmentOrder>());
	enum ShipmentState {pending, sent, arrived, processed};
	int marketNumber;
	List<Restaurant2Market> markets = new ArrayList<Restaurant2Market>();
	boolean startCheck;
	
	private Semaphore atDestination = new Semaphore(0,true);
	
	Restaurant2CookGui gui;
		
	public Restaurant2CookRole(String n){
		super();
		
		marketNumber = 0;
		
		name = n;
		foods.put("Chicken", new Food("Chicken", 10, 3));
		foods.put("Steak", new Food("Steak", 20, 1));
		foods.put("Pizza", new Food("Pizza", 12, 5));
		foods.put("Salad", new Food("Salad", 5, 1));
		//each food starts off with low inventory
		
		startCheck = true;
	}
	
	public void addRestaurant2Market(Restaurant2Market m){
		markets.add(m);
	}
	
	public void setGui(Restaurant2CookGui g){
		gui = g;
	}
	
	public void atDest(){ //from gui
		atDestination.release();
	}
	
	//MESSAGES
	
	public void msgHereIsOrder(Restaurant2Waiter w, String choice, int table){
		print("Recieved order msg.");
		orders.add(new Order(w, choice, table));
		stateChanged();
	}
	
	public void msgFoodDone(Order o){ //msg sent from the timer when it finishes
		print("Food is done cooking.");
		o.setState(OrderState.done);
		stateChanged();
	}
	
	public void msgFailedOrder(HashMap<String, Integer> failedOrder){
		shipmentOrders.add(new ShipmentOrder(failedOrder, ShipmentState.pending));
		stateChanged();
	}
	
	public void msgHereIsShipment(HashMap<String, Integer> goodOrder){
		print("Recieved msg here is shipment");
		shipmentOrders.add(new ShipmentOrder(goodOrder, ShipmentState.arrived));
		stateChanged();
	}
	
	public void msgOutOfAllFood(Restaurant2Market m){
		for(Restaurant2Market ma : markets){
			if(ma == m){
				markets.remove(ma);
			}
		}
	}
	
	public void msgGotFood(){
		gui.setFoodDone(false);
	}
	
	//SCHEDULER

	public boolean pickAndExecuteAnAction() {
		if(startCheck){
			checkInventory();
		}
		synchronized(orders){
			for(Order o : orders){
				if(o.s == OrderState.pending){
					checkFood(o);
					return true;
				}
			}
		}
		synchronized(orders){
			for(Order o : orders){
				if (o.s == OrderState.done){
					PlateIt(o);
					return true;
				}
			}
		}
		try{
			for(ShipmentOrder s : shipmentOrders){
				if(s.ss == ShipmentState.pending){
					sendShipmentOrder(s);
					s.ss = ShipmentState.sent;
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e){
			return true;
		}
		try{
			for(ShipmentOrder s : shipmentOrders){
				if(s.ss == ShipmentState.arrived){
					recieveShipment(s);
					s.ss = ShipmentState.processed;
					return true;
				}
			}
		}
		catch(ConcurrentModificationException e){
			return true;
		}
		return false;
	}
	
	
	//ACTIONS
	
	private void PlateIt(Order o){
		Do("Plating food " + o.choice);
	//	DoPlating(o); //Animation method
		o.getWaiter().msgOrderIsReady(o.getChoice(), o.getTable(), this);
		o.setState(OrderState.sent);
	}
	
	private void CookIt(final Order o){
		Do("Cooking food " + o.choice);
		gui.doStartCooking();
		try{
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		gui.doCookFood(); //Animation method
		try{
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		gui.setFoodCooking(true);
		
		timer.schedule(new TimerTask() {
			public void run() {
				msgFoodDone(o);
				gui.doPlateFood();
				try{
					atDestination.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				gui.setFoodDone(true);
				gui.goHome();
			}
		},
		//foods.get((o.getChoice()).cookTime);
		5000);
	}
	
	private void checkInventory() {
		if(markets.isEmpty()){
			return;
		}
		HashMap<String, Integer> newOrder = new HashMap<String, Integer>();
		for(Map.Entry<String, Food> e : foods.entrySet()){
			if((e.getValue().inventory <= e.getValue().lowPoint) && e.getValue().os == FoodOrderState.notOrdered){
				newOrder.put(e.getValue().type, e.getValue().capacity - e.getValue().inventory);
				e.getValue().os = FoodOrderState.Ordered;
			}
		}
		if(!newOrder.isEmpty()){
			sendShipmentOrder(new ShipmentOrder(newOrder, ShipmentState.sent));
		}
	}
	
	private void checkFood(Order o) {
		//if(markets.isEmpty()){
		//	o.w.msgOutOfFood();
		//}
		Food temp = foods.get(o.choice);
		if(temp.inventory == 0){
			o.setState(OrderState.sent);
			o.w.msgOutOfFood(o.choice, o.table);
			print("We're out of " + o.choice);
		}
		else{
			foods.get(o.choice).inventory--;
			o.setState(OrderState.cooking);
			CookIt(o);
		}
		checkInventory();
	}
	
	private void sendShipmentOrder(ShipmentOrder s){
		print("Sending shipment order to market " + (marketNumber + 1) + " of size " + s.order.size());
		Restaurant2Market m = markets.get(marketNumber);
		m.msgPlaceFoodOrder(this, s.order);
		if(marketNumber == (markets.size()-1)){
			marketNumber = 0;
		}
		else{
			marketNumber++;
		}
	}
	
	private void recieveShipment(ShipmentOrder s){
		for(Map.Entry<String, Integer> entry : s.order.entrySet()){
			for(Map.Entry<String, Food> e : foods.entrySet()){
				if(entry.getKey() == e.getKey()){
					e.getValue().addToInventory(entry.getValue());
				}
			}
		}
	}

	//Cook classes
	
	private class Food{
		String type;
		int cookTime;
		int inventory;
		int lowPoint;
		int capacity;
		FoodOrderState os;
		//List<Restaurant2Market> outOf = new ArrayList<Restaurant2Market>();
		
		Food(String t, int time, int i){
			type = t;
			cookTime = time*1000;
			inventory = i;
			lowPoint = 3;
			capacity = 7;
			os = FoodOrderState.notOrdered;
		}
		public void addToInventory(int i){
			inventory = inventory + i;
		}
		
	}
	
	public class Order{
		Restaurant2Waiter w;
		String choice;
		int table;
		OrderState s;
		
		Order(Restaurant2Waiter wa, String c, int t){
			w = wa;
			choice = c;
			table = t;
			s = OrderState.pending;
		}
		
		void setState(OrderState state){
			s = state;
		}
		
		Restaurant2Waiter getWaiter(){
			return w;
		}
		
		String getChoice(){
			return choice;
		}
		
		int getTable(){
			return table;
		}
	}
	
	class ShipmentOrder{
		HashMap<String, Integer> order;
		ShipmentState ss;
		
		ShipmentOrder(HashMap<String, Integer> o, ShipmentState state){
			order = o;
			ss = state;
		}
	}
	
	public String getName() {
		return name;
	}


}
