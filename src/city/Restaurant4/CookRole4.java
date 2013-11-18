package city.Restaurant4;

import Role.Role;

import java.util.*;

import justinetesting.interfaces.Cook4;
import justinetesting.interfaces.Customer4;
import justinetesting.interfaces.Market4;
import justinetesting.interfaces.Waiter4;
import city.gui.restaurant4.CookGui4;

public class CookRole4 extends Role implements Cook4 {
	String name;
	WaiterRole4 waiter;
	ArrayList<Waiter4> waiters= new ArrayList<Waiter4>();
	Timer cook= new Timer();
	Order o= new Order();
	List<Order> orders= Collections.synchronizedList(new ArrayList<Order>());;
	ArrayList<Food> foods;
	ArrayList<MyMarket> markets;
	public enum orderState {none, pending, cooking, outOfItem, done, finished};
	public enum marketState{none, checkForRestock, ready, ordered, fulfilled, partiallyFullfilled, allMarketsOut};
	static final int steakTime= 1500;
	static final int chickenTime= 1200;
	static final int saladTime= 1000;
	static final int pizzaTime= 1250;
	static final int randSelector= 3;
	public int id= 0;
	Map<String, Integer> delivery= new HashMap<String, Integer>();
	boolean successful;
	
	// Implement cook gui
	public CookGui4 cookGui = null;

	public CookRole4(String name) {
		super();
		this.name= name;
		foods= new ArrayList<Food>();
		markets= new ArrayList<MyMarket>();
		foods.add(new Food("Steak"));
		foods.add(new Food("Chicken"));
		foods.add(new Food("Salad"));
		foods.add(new Food("Pizza"));
		delivery.put("Steak", 0);
		delivery.put("Chicken", 0);
		delivery.put("Salad", 0);
		delivery.put("Pizza", 0);
	}

	public String getName(){
		return name;
	}
	
	public void addMarket(MarketRole4 m){
		markets.add(new MyMarket(m));
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
		stateChanged();
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
	
	public void msgHereIsDelivery(int st, int ch, int s, int p, boolean successful){
		print("Recieved delivery, let's check it out!");
		print("STEAK: " + st + "  CHICKEN: " + ch + "  SALAD: " + s + "  PIZZA: " + p);
		delivery.put("Steak", st);
		delivery.put("Chicken", ch);
		delivery.put("Salad", s);
		delivery.put("Pizza", p);
		this.successful= successful;
		o.ms= marketState.fulfilled;
		stateChanged();
	}
	
	public void msgOutOfItem(Market4 m, String type){
		for(MyMarket market : markets){
			if(m == market.m){
				market.outOf(type);
			}
		}
	}
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		if(orders != null){
			synchronized(orders){
				for(Order order : orders){
					if(order.s == orderState.done){
						print("Order up!");
						plateIt(order);
						return true;
					}
				}
			}
			synchronized(orders){
				for(Order order : orders){
					if(order.s == orderState.pending){
						print("I should cook this food.");
						order.s= orderState.cooking;
						cookIt(order);
						return true;
					}
				}
			}
		}
		if(o.ms == marketState.checkForRestock){
			print("Looking through inventory to calcualte order to send to market");
			calculateOrder();
			return true;
		}
		if(o.ms == marketState.ready || o.ms == marketState.partiallyFullfilled){
			print("Sending order to the market now.");
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
		print("COULDNT FIND THE RIGHT ORDER, WHOOPS");
		return -1;
	}
	
	private void plateIt(Order o){
		cookGui.doPlating(o.choice, find(o), o.id);
		o.w.msgOrderDone(o.choice, o.c);
		o.s= orderState.finished;
		stateChanged();
	}
	
	private void cookIt(final Order o){
		int cookingTime= 0;
		for(Food food : foods){
			if(food.type == o.choice){	 
				if(food.getAmount() == 0){
					o.w.msgOutOfFood(food.type, o.c);
					o.s= orderState.outOfItem;
					this.o.ms= marketState.checkForRestock;
					stateChanged();
					return;
				}
				else {
					cookGui.doCooking(o.choice, find(o), o.id);
					food.decrementAmount();
					cookingTime= food.getTime();	
					cook.schedule(new TimerTask() {
						@Override public void run() {
							o.s= orderState.done;
							stateChanged();
						}}, cookingTime);
					return;
				}
			}
		}
	}

	public void sendOrder(){
		Random rand = new Random();
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
		}
		o.ms= marketState.ordered;
		stateChanged();
	}
	
	public void calculateOrder(){
		for(Food food : foods){
			if(food.type == "Steak"){
				if(food.currAmount <= food.low){
					print("Steak is low, I need to restock!");
					int st= food.capacity - food.currAmount;
					o.add("Steak", st);
				}
			}
			if(food.type == "Chicken"){
				if(food.currAmount <= food.low){
					print("Chicken is low, I need to restock!");
					int ch= food.capacity - food.currAmount;
					o.add("Chicken", ch);
				}
			}
			if(food.type == "Salad"){
				if(food.currAmount <= food.low){
					print("Salad is low, I need to restock!");
					int s= food.capacity - food.currAmount;
					o.add("Salad", s);
				}
			}
			if(food.type == "Pizza"){
				if(food.currAmount <= food.low){
					print("Pizza is low, I need to restock!");
					int p= food.capacity - food.currAmount;
					o.add("Pizza", p);
				}
			}
		}
	}
	
	public void restock(){
		for(Food food : foods){
			food.currAmount += delivery.get(food.type);
			print("Restocked " + food.type + ": " + food.currAmount);
			for(Waiter4 w : waiters){
				w.msgRestocked(food.type);
			}
		}
		if(successful){
			print("Oh good, I got everything I needed!");
			o.steak= 0;
			o.chicken= 0;
			o.salad= 0;
			o.pizza= 0;
			delivery.put("Steak", 0);
			delivery.put("Chicken", 0);
			delivery.put("Salad", 0);
			delivery.put("Pizza", 0);
			o.ms= marketState.none;
		}
		else{
			print("Oh no, I'm missing a few items! I should ask another market this time");
			if(delivery.get("Steak") < o.steak){
				o.add("Steak", (o.steak - delivery.get("Steak")));
			}
			else if(delivery.get("Chicken") < o.chicken){
				o.add("Chicken", (o.chicken - delivery.get("Chicken")));
			}
			else if(delivery.get("Salad") < o.salad){
				o.add("Salad", (o.salad - delivery.get("Salad")));
			}
			else if(delivery.get("Pizza") < o.pizza){
				o.add("Pizza", (o.pizza - delivery.get("Pizza")));
			}
			o.ms= marketState.partiallyFullfilled;
		}
		stateChanged();
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
		int steak=0;
		int chicken=0;
		int salad=0;
		int pizza=0;
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
			steak= 0;
			chicken= 0;
			salad= 0;
			pizza= 0;
			ms= marketState.none;
		}

		public void add(String type, int amount){
			ms= marketState.ready;
			if(type == "Steak"){
				steak= amount;
			}
			else if(type == "Chicken"){
				chicken= amount;
			}
			else if(type == "Salad"){
				salad= amount;
			}
			else if(type == "Pizza"){
				pizza= amount;
			}
		}
	}
	
	public class Food{
		String type;
		private int cookingTime;
		private int currAmount;
		private final int capacity= 5;
		private final int low= 2;
		
		Food(String type){
			this.type= type;
			currAmount= 3;
			if(type == "Steak"){
				cookingTime= steakTime;
			}
			else if(type == "Chicken"){
				cookingTime= chickenTime;
			}
			else if(type == "Salad"){
				cookingTime= saladTime;
			}
			else if(type == "Pizza"){
				cookingTime= pizzaTime;
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
	
	public class MyMarket{
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
	}
}