package city.Restaurant4;

import Role.MarketManager;
import Role.Role;

import java.util.*;

import test.mock.LoggedEvent;
import activityLog.ActivityLog;
import activityLog.ActivityTag;
import justinetesting.interfaces.Cook4;
import justinetesting.interfaces.Customer4;
import justinetesting.interfaces.Waiter4;
import city.MarketOrder;
import city.OrderItem;
import city.PersonAgent;
import city.gui.restaurant4.CookGui4;

public class CookRole4 extends Role implements Cook4 {
	String name;
	WaiterRole4 waiter;
	PersonAgent p;
	ArrayList<Waiter4> waiters= new ArrayList<Waiter4>();
	Timer cook= new Timer();
	Order o= new Order();
	List<Order> orders= Collections.synchronizedList(new ArrayList<Order>());;
	ArrayList<Food> foods;
	//ArrayList<MyMarket> markets;
	MarketManager market;
	public enum orderState {none, pending, cooking, outOfItem, done, finished};
	public enum marketState{none, checkForRestock, ready, ordered, fulfilled, partiallyFullfilled, allMarketsOut};
	static final int eggTime= 1500;
	static final int waffelTime= 1200;
	static final int pancakeTime= 1000;
	static final int baconTime= 1250;
	static final int randSelector= 3;
	public int id= 0;
	Map<String, Integer> delivery= new HashMap<String, Integer>();
	boolean successful;
	
	ActivityTag tag = ActivityTag.RESTAURANT4COOK;
	
	// Implement cook gui
	public CookGui4 cookGui = null;

	public CookRole4(String name, PersonAgent p) {
		super();
		building = "rest4";
		this.name= name;
		this.p= p;
		foods= new ArrayList<Food>();
		//markets= new ArrayList<MyMarket>();
		foods.add(new Food("Eggs"));
		foods.add(new Food("Waffels"));
		foods.add(new Food("Pancakes"));
		foods.add(new Food("Bacon"));
		delivery.put("Eggs", 0);
		delivery.put("Waffels", 0);
		delivery.put("Pancakes", 0);
		delivery.put("Bacon", 0);
	}

	public String getName(){
		return name;
	}
	
	public void addMarket(MarketManager m){
		//markets.add(new MyMarket(m));
		market= m;
	}
	
	// MESSAGES 
	public void msgHereIsOrder(Waiter4 w, String choice, Customer4 c){
		Order o= new Order(w, choice, c, "pending", id++);
		orders.add(o);
		boolean newWaiter= true;
		for(Waiter4 wait : waiters){
			if(wait == w){
				newWaiter= false;
			}
		}
		if(newWaiter){
			waiters.add(w);
		}
		p.stateChanged();
	}

	public void msgPickedUpFood(Customer4 c){
		for(Order o : orders){
			if(o.c.equals(c)){
				//cookGui.itemPickedUp(o.choice, find(o));
				cookGui.itemPickedUp(o.id);
				orders.remove(o);
			}
		}
	}
	
	/*public void msgHereIsDelivery(int e, int w, int p, int b, boolean successful){
		log("Recieved delivery, let's check it out!");
		log("EGGS: " + e + "  WAFFELS: " + w + "  PANCAKES: " + p + "  BACON: " + b);
		delivery.put("Eggs", e);
		delivery.put("Waffels", w);
		delivery.put("Pancakes", p);
		delivery.put("Bacon", b);
		this.successful= successful;
		o.ms= marketState.fulfilled;
		this.p.stateChanged();
	} */
	 
	public void msgHereIsYourOrder(MarketOrder mo){
		List<OrderItem> order = mo.getOrders();
		for(int i=0; i<order.size(); i++){
			if(order.get(i).type.equals("Eggs")){
				delivery.put("Eggs", 1);
			} else if (order.get(i).type.equals("Waffels")){
				delivery.put("Waffels", 1);
			} else if(order.get(i).type.equals("Pancakes")){
				delivery.put("Pancakes", 1);
			} else if(order.get(i).type.equals("Bacon")){
				delivery.put("Bacon", 1);
			}
		}
		successful= true;
		o.ms= marketState.fulfilled;
		this.p.stateChanged();
	}
	
	/*public void msgOutOfItem(Market4 m, String type){
		for(MyMarket market : markets){
			if(m == market.m){
				market.outOf(type);
			}
		}
	}*/
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if(orders != null){
			synchronized(orders){ 
				for(Order order : orders){
					if(order.s == orderState.done){
						log("Order up!");
						plateIt(order);
						return true;
					}
				}
			}
			synchronized(orders){
				for(Order order : orders){
					if(order.s == orderState.pending){
						log("I should cook this food.");
						order.s= orderState.cooking;
						cookIt(order);
						return true;
					}
				}
			}
		}
		if(o.ms == marketState.checkForRestock){
			log("Looking through inventory to calcualte order to send to market");
			calculateOrder();
			return true;
		}
		if(o.ms == marketState.ready || o.ms == marketState.partiallyFullfilled){
			log("Sending order to the market now.");
			sendOrder();
			return true;
		}
		if(o.ms == marketState.fulfilled){
			restock();
			return true;
		}
		if(o.ms == marketState.allMarketsOut){
			closeRestaurant();
		}
		return false;
	}

	
	// ACTIONS
	private int find(Order o){
		for(int i=0; i<orders.size(); i++){
			if(orders.get(i).equals(o)){
				return i;
			}
		}
		log("COULDNT FIND THE RIGHT ORDER, WHOOPS");
		return -1;
	}
	
	private void plateIt(Order o){
		cookGui.doPlating(o.choice, find(o), o.id);
		o.w.msgOrderDone(o.choice, o.c);
		o.s= orderState.finished;
		p.stateChanged();
	}
	
	private void cookIt(final Order o){
		int cookingTime= 0;
		for(Food food : foods){
			if(food.type == o.choice){	 
				if(food.getAmount() == 0){
					o.w.msgOutOfFood(food.type, o.c);
					o.s= orderState.outOfItem;
					this.o.ms= marketState.checkForRestock;
					p.stateChanged();
					return;
				}
				else {
					cookGui.doCooking(o.choice, find(o), o.id);
					food.decrementAmount();
					cookingTime= food.getTime();	
					cook.schedule(new TimerTask() {
						@Override public void run() {
							o.s= orderState.done;
							p.stateChanged();
						}}, cookingTime);
					return;
				}
			}
		}
	}

	public void sendOrder(){
		List<OrderItem> orders= new ArrayList<OrderItem>();
		OrderItem eggs= new OrderItem("Eggs", o.eggs);
		OrderItem waffels= new OrderItem("Waffels", o.waffels);
		OrderItem pancakes= new OrderItem("Pancakes", o.pancakes);
		OrderItem bacon= new OrderItem("Bacon", o.bacon);
		
		orders.add(eggs);
		orders.add(waffels);
		orders.add(pancakes);
		orders.add(bacon);
		
		MarketOrder order= new MarketOrder(orders, "Restaruant4", p);
		market.msgHereIsOrder(order);
		/*Random rand = new Random();
		int num= rand.nextInt(randSelector);
		if(num == 0 && !markets.get(0).out){
			markets.get(0).m.msgHereIsOrder(this, o.steak, o.chicken, o.salad, o.pizza);
		}
		else if(num == 1 && !markets.get(1).out){
			markets.get(1).m.msgHereIsOrder(this, o.steak, o.chicken, o.salad, o.pizza);
		}
		else if(num == 2 && !markets.get(2).out){
			markets.get(2).m.msgHereIsOrder(this, o.steak, o.chicken, o.salad, o.pizza);
		}
		else{
			o.ms= marketState.allMarketsOut;
			return;
		}*/
		o.ms= marketState.ordered;
		p.stateChanged();
	}
	
	public void calculateOrder(){
		for(Food food : foods){
			if(food.type == "Eggs"){
				if(food.currAmount <= food.low){
					log("Eggs are low, I need to restock!");
					int e= food.capacity - food.currAmount;
					o.add("Eggs", e);
				}
			}
			if(food.type == "Waffels"){
				if(food.currAmount <= food.low){
					log("Waffels are low, I need to restock!");
					int w= food.capacity - food.currAmount;
					o.add("Waffels", w);
				}
			}
			if(food.type == "Pancakes"){
				if(food.currAmount <= food.low){
					log("Pancakes are low, I need to restock!");
					int p= food.capacity - food.currAmount;
					o.add("Pancakes", p);
				}
			}
			if(food.type == "Bacon"){
				if(food.currAmount <= food.low){
					log("Bacon is low, I need to restock!");
					int b= food.capacity - food.currAmount;
					o.add("Bacon", b);
				}
			}
		}
	}
	
	public void restock(){
		for(Food food : foods){
			food.currAmount += delivery.get(food.type);
			log("Restocked " + food.type + ": " + food.currAmount);
			for(Waiter4 w : waiters){
				w.msgRestocked(food.type);
			}
		}
		if(successful){
			log("Oh good, I got everything I needed!");
			o.eggs= 0;
			o.waffels= 0;
			o.pancakes= 0;
			o.bacon= 0;
			delivery.put("Eggs", 0);
			delivery.put("Waffels", 0);
			delivery.put("Pancakes", 0);
			delivery.put("Bacon", 0);
			o.ms= marketState.none;
		}
		else{
			log("Oh no, I'm missing a few items! I should ask another market this time");
			if(delivery.get("Eggs") < o.eggs){
				o.add("Eggs", (o.eggs - delivery.get("Eggs")));
			}
			else if(delivery.get("Waffels") < o.waffels){
				o.add("Waffels", (o.waffels - delivery.get("Waffels")));
			}
			else if(delivery.get("Pancakes") < o.pancakes){
				o.add("Pancakes", (o.pancakes - delivery.get("Pancakes")));
			}
			else if(delivery.get("Bacon") < o.bacon){
				o.add("Bacon", (o.bacon - delivery.get("Bacon")));
			}
			o.ms= marketState.partiallyFullfilled;
		}
		
		p.stateChanged();
	}
	
	public void closeRestaurant(){
		for(Waiter4 wait : waiters){
			wait.msgAllMarketsOut();
		}
	}
	
	
	// UTILITIES
	public void setGui(CookGui4 gui) {
		cookGui = gui;
	}

	public CookGui4 getGui() {
		return cookGui;
	}

	
	// CLASSES
	public static class Order{
		Waiter4 w;
		String choice;
		Customer4 c;
		orderState s;
		marketState ms;
		int eggs=0;
		int waffels=0;
		int pancakes=0;
		int bacon=0;
		int id;
		
		
		Order(Waiter4 w2, String choice, Customer4 c2, String state, int id){
			this.w= w2;
			this.choice= choice;
			this.c= c2;
			if(state == "pending"){
				s= orderState.pending;
			}
			else{
				s= orderState.none;
			}	
		}
		
		Order(){
			eggs= 0;
			waffels= 0;
			pancakes= 0;
			bacon= 0;
			ms= marketState.none;
		}

		public void add(String type, int amount){
			ms= marketState.ready;
			if(type == "Steak"){
				eggs= amount;
			}
			else if(type == "Chicken"){
				waffels= amount;
			}
			else if(type == "Salad"){
				pancakes= amount;
			}
			else if(type == "Pizza"){
				bacon= amount;
			}
		}
	}
	
	public class Food{
		String type;
		private int cookingTime;
		private int currAmount;
		private final int capacity= 15;
		private final int low= 3;
		
		Food(String type){
			this.type= type;
			currAmount= 10;
			if(type == "Eggs"){
				cookingTime= eggTime;
			}
			else if(type == "Waffels"){
				cookingTime= waffelTime;
			}
			else if(type == "Pancakes"){
				cookingTime= pancakeTime;
			}
			else if(type == "Bacon"){
				cookingTime= baconTime;
			}
		}
		
		public int getTime(){
			return cookingTime;
		}
		
		public int getAmount(){
			return currAmount;
		}
		
		public void decrementAmount(){
			currAmount--;
		}
	}	
	
	/*public class MyMarket{
		boolean steak= true;
		boolean chicken= true;
		boolean salad= true;
		boolean pizza= true;
		boolean out= false;
		MarketRole4 m;
		
		MyMarket(MarketRole4 m){
			this.m= m;
		}
		
		public void outOf(String type){
			if(type.equals("Steak")){
				steak= false;
			}
			else if(type.equals("Chicken")){
				chicken= false;
			}
			else if(type.equals("Salad")){
				salad= false;
			}
			else if(type.equals("Pizza")){
				pizza= false;
			}
			
			if(!steak && !chicken && !salad && !pizza){
				out= true;
			}
		}
	}*/
	
	private void log(String msg){
		print(msg);
        ActivityLog.getInstance().logActivity(tag, msg, name);
	}
}