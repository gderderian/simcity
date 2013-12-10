package city.Restaurant2;

import interfaces.Restaurant2Cook;
import interfaces.Restaurant2Waiter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import activityLog.ActivityLog;
import activityLog.ActivityTag;
import city.CityMap;
import city.MarketOrder;
import city.OrderItem;
import city.PersonAgent;
import city.gui.restaurant2.Restaurant2CookGui;
import Role.Role;
import city.Restaurant2.Order;
import city.Restaurant4.CookRole4.marketState;

public class Restaurant2CookRole extends Role implements Restaurant2Cook{
	
	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	enum OrderState {pending, cooking, done, sent};
	enum FoodOrderState {Ordered, notOrdered};
	Timer timer = new Timer();
	javax.swing.Timer spindleTimer;
	Map<String, Food> foods = new HashMap<String, Food>();
	String name;
	List<ShipmentOrder> shipmentOrders = Collections.synchronizedList(new ArrayList<ShipmentOrder>());
	enum ShipmentState {pending, sent, arrived, processed};
	boolean startCheck;
	
	CityMap cityMap;
	
	String roleName = "Restaurant2CookRole";
	
	private Semaphore atDestination = new Semaphore(0,true);
	
	OrderSpindle spindle;
	
	PersonAgent person;
	boolean test = false;
	
	Restaurant2CookGui cookGui;
	ActivityTag tag = ActivityTag.RESTAURANT2COOK;
		
	public Restaurant2CookRole(String n, PersonAgent p){
		super();
		building = "rest2";
		
		person = p;
		
		cityMap = p.getCityMap();
		
		name = n;
		foods.put("Chicken", new Food("Chicken", 10, 3));
		foods.put("Steak", new Food("Steak", 20, 1));
		foods.put("Pizza", new Food("Pizza", 12, 5));
		foods.put("Salad", new Food("Salad", 5, 1));
		//each food starts off with low inventory
		
		startCheck = true;

		spindle = OrderSpindle.getInstance();
		
		spindleTimer = new javax.swing.Timer(2000,
				new ActionListener() { public void actionPerformed(ActionEvent event) {
					checkSpindle();
					spindleTimer.restart();
		      }
		});
	}
	
	public void setGui(Restaurant2CookGui g){
		cookGui = g;
		gui = g;
	}
	
	public void atDest(){ //from gui
		atDestination.release();
	}
	
	//MESSAGES
	
	public void msgHereIsOrder(Restaurant2Waiter w, String choice, int table){
		log("Recieved order msg.");
		orders.add(new Order(w, choice, table));
		person.stateChanged();
	}
	
	public void msgFoodDone(Order o){ //msg sent from the timer when it finishes
		log("Food is done cooking.");
		o.setState(OrderState.done);
		person.stateChanged();
	}
	
	//TODO fix this in V2
	@Override
	public void msgFailedOrder(HashMap<String, Integer> failedOrder){
	//	shipmentOrders.add(new ShipmentOrder(failedOrder, ShipmentState.pending));
		person.stateChanged();
	}
	
	public void msgHereIsYourOrder(MarketOrder goodOrder){
		log("Recieved msg here is shipment");
		shipmentOrders.add(new ShipmentOrder(goodOrder, ShipmentState.arrived));
		person.stateChanged();
	}
	
	public void msgGotFood(){
		cookGui.setFoodDone(false);
	}
	
	//SCHEDULER

	public boolean pickAndExecuteAnAction() {
		if(startCheck){
			checkInventory();
			checkSpindle();
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
		synchronized(shipmentOrders){
			for(ShipmentOrder s : shipmentOrders){
				if(s.ss == ShipmentState.pending){
					sendShipmentOrder(s);
					s.ss = ShipmentState.sent;
					return true;
				}
			}
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
	
	private void checkSpindle(){
		//log("Checking the spindle");
		if(!spindle.isEmpty()){
			orders.add(spindle.takeOffOrder());
			log("Found an order on the spindle, I'm adding it to my list");
		}
	}
	
	private void PlateIt(Order o){
		Do("Plating food " + o.choice);
	//	DoPlating(o); //Animation method
		o.getWaiter().msgOrderIsReady(o.getChoice(), o.getTable(), this);
		o.setState(OrderState.sent);
	}
	
	private void CookIt(final Order o){
		Do("Cooking food " + o.choice);
		cookGui.doStartCooking();
		try{
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		cookGui.doCookFood(); //Animation method
		try{
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cookGui.setFoodCooking(true);
		
		timer.schedule(new TimerTask() {
			public void run() {
				msgFoodDone(o);
				cookGui.doPlateFood();
				try{
					atDestination.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				cookGui.setFoodDone(true);
				cookGui.goHome();
			}
		},
		//foods.get((o.getChoice()).cookTime);
		5000);
	}
	
	private void checkInventory() {
		MarketOrder newOrder = new MarketOrder("rest2", person);
		for(Map.Entry<String, Food> e : foods.entrySet()){
			if((e.getValue().inventory <= e.getValue().lowPoint) && e.getValue().os == FoodOrderState.notOrdered){
				newOrder.addOrder(new OrderItem(e.getKey(), e.getValue().capacity - e.getValue().inventory));
				e.getValue().os = FoodOrderState.Ordered;
			}
		}
		if(newOrder.orders.size() != 0){
			sendShipmentOrder(new ShipmentOrder(newOrder, ShipmentState.sent));
		}
	}
	
	private void checkFood(Order o) {
		Food temp = foods.get(o.choice);
		if(temp.inventory == 0){
			o.setState(OrderState.sent);
			o.w.msgOutOfFood(o.choice, o.table);
			log("We're out of " + o.choice);
		}
		else{
			foods.get(o.choice).inventory--;
			o.setState(OrderState.cooking);
			CookIt(o);
		}
		checkInventory();
	}
	
	//TODO change this to market order
	private void sendShipmentOrder(ShipmentOrder s){
		log("Sending shipment order to market the market of size " + s.order.orders.size());
		boolean isOpen = true;
		for(int i = 0; i < 3; i++){
			isOpen = cityMap.msgMarketHereIsTruckOrder(1, s.order);
			if(isOpen){
				s.ss = ShipmentState.sent;
				return;
			}
			else{
				log("Uh oh, looks like the market I wanted to order from isn't open! I'll try a different one");
			}
		}
		
		log("Looks like all of the markets are closed, I guess I won't order my food right now...");
		
	}
	
	private void recieveShipment(ShipmentOrder s){
		for(OrderItem o : s.order.orders){
			for(Map.Entry<String, Food> e : foods.entrySet()){
				if(o.name.equals(e.getKey())){
					e.getValue().addToInventory(o.quantity);
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
	
	class ShipmentOrder{
		MarketOrder order;
		ShipmentState ss;
		
		ShipmentOrder(MarketOrder o, ShipmentState state){
			order = o;
			ss = state;
		}
	}
	
	public String getName() {
		return name;
	}
	
	private void log(String msg){
		print(msg);
		if(!test)
			ActivityLog.getInstance().logActivity(tag, msg, name, false);
	}

	public void setTesting(boolean b) {
		test = b;
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
