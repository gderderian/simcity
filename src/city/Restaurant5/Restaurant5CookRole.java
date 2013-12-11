package city.Restaurant5;
import Role.Role;
import city.CityMap;
import city.House;
import city.MarketOrder;
import city.OrderItem;
import city.PersonAgent;
import city.gui.Restaurant5.Restaurant5CookGui;
import tomtesting.interfaces.Restaurant5Cook;
import tomtesting.interfaces.Restaurant5Market;
import tomtesting.interfaces.Restaurant5Waiter;
import activityLog.ActivityLog;
import activityLog.ActivityTag;
import astar.AStarTraversal;
import interfaces.MarketManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

//import restaurant.MarketAgent;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class Restaurant5CookRole extends Role implements Restaurant5Cook{
	
	String roleName = "Restaurant5CookRole";
	
	public List<cookingorder> cookingorders = Collections.synchronizedList(new ArrayList<cookingorder>());
	public List<mymarket> markets = Collections.synchronizedList(new ArrayList<mymarket>()); 
	public List<checkfrommarket> checksfrommarket = Collections.synchronizedList(new ArrayList<checkfrommarket>());
	public List<finishedorder> finishedorders = Collections.synchronizedList(new ArrayList<finishedorder>()); 
	public enum finishedorderstate {pending, pickedupbywaiter};
	public enum cookingorderstate {pending, cooking, donecooking, waiting, pickedupbywaiter};
	public enum cookstate {doingnothing, cooking, plating};
	
	Timer timerforcooking = new Timer();
	private String name;
	public cookstate state;
	public Restaurant5CookGui CookGui = null;
	int cookingtimeforchicken = 9;
	int cookingtimeforburrito = 9;
	int cookingtimeforpizza = 9;
	public boolean cooking = false;
	public boolean donecooking = false;
	boolean foodisout = false;
	//public cookingorder currentorder;
	String sendorderchickentomarket2;
	String sendorderburritotomarket2;
	String sendorderpizzatomarket2;
	String sendordertomarket3;
	
	boolean callchickenmarket = false;
	boolean callburritomarket = false;
	boolean callpizzamarket = false;
	
	boolean tellcashiertopayforsupplies = false;
	
	PersonAgent person;
	
	Map<String, Integer> inventoryoffood = new HashMap<String, Integer>();
	Restaurant5MarketRole market1;
	Restaurant5MarketRole market2;
	Restaurant5MarketRole market3;
	Restaurant5CashierRole cashier;
	OrderSpindle5 orderspindle;
	MarketManager marketmanager;
	
	ActivityTag tag = ActivityTag.RESTAURANT5COOK;
	
	
	public Restaurant5CookRole(String name, PersonAgent person) {
		super();
		building = "rest5";
		this.name = name;
		this.person = person;
		this.state = cookstate.doingnothing;
		inventoryoffood.put("chicken", 0);
		inventoryoffood.put("burrito", 0);
		inventoryoffood.put("pizza", 0);
		
		
		
		
		AStarTraversal aStarTraversal = null;
		
		CityMap citymap = new CityMap();
		House house = new House("house1");
				
	}

	public String getName() {
		return name;
	}


	// Messages
	//After receiving message from the waiter start cooking
	/*
	public void msgHereIsYourOrder(MarketOrder mo){ 
		List<OrderItem> order = mo.getOrders();
		for(int i=0; i<order.size(); i++){
			if(order.get(i).type.equals("chicken")){
				delivery.put("Eggs", 1);
			} else if (order.get(i).type.equals("burrito")){
				delivery.put("Waffels", 1);
			} else if(order.get(i).type.equals("pizza")){
				delivery.put("Pancakes", 1);
			}
		}
		successful= true;
		o.ms= marketState.fulfilled;
		this.p.stateChanged();
	}
	*/
	
	
	
	
	
	public void msgReceviedOrderFromWaiter(Restaurant5Waiter waiter, String order, int table) {
		
		log("Received order: " + order + " from waiter: " + waiter.getName() + " for table " + table);
		cookingorders.add( new cookingorder(waiter, order, table));
		log("order added to the cooking list");
		person.stateChanged();
		
	}
	
	public void msgDoneCooking(cookingorder setcookingorder) {
		
		synchronized(cookingorders)
		{
		
		for(cookingorder findcookingorder: cookingorders)
		{
			if(findcookingorder == setcookingorder)
			{
				findcookingorder.state = cookingorderstate.donecooking;
				person.stateChanged();
			}
			
		}
		
		}
		
	}
	
	public void msgPickedUpFoodFromTheKitchen(Restaurant5WaiterRole waiter, String order, int table) {
		
		synchronized(cookingorders)
		{
		
		for(cookingorder checkcookingorder: cookingorders)
		{
			if(checkcookingorder.waiter == waiter && checkcookingorder.order.equals(order) && checkcookingorder.assignedtablenumber == table)
			{
				checkcookingorder.state = cookingorderstate.pickedupbywaiter;
				person.stateChanged();
			}
		
		}
		
		}
		
		
		
	}
	
	
	public void msgReceivedSuppliesFromMarket(String order, Restaurant5Market market) {
		
		log("" + order +" is restocked!");
		if(order.equals("chicken"))
		{
			callchickenmarket = false;
			
		}
		else if(order.equals("burrito"))
		{
			callburritomarket = false;
		}
		else if(order.equals("pizza"))
		{
			callpizzamarket = false;
		}
		inventoryoffood.put(order, 3);
		log("current amount of " + order + ": " + inventoryoffood.get(order));
		checksfrommarket.add( new checkfrommarket(order, market));
		tellcashiertopayforsupplies = true;
		person.stateChanged();
	}
	
	
		
	public void msgSupplyIsOut(String order, Restaurant5Market market) {
		//log("supply of " + order + " is out!");
		if(order.equals("chicken"))
		{
			callchickenmarket = false;
		}
		else if(order.equals("burrito"))
		{
			callburritomarket = false;
		}
		else if(order.equals("pizza"))
		{
			callpizzamarket = false;
		}
		for(mymarket findmarket : markets) {
			if(findmarket.market == market)
			{
				findmarket.suppliesoffood.put(order, false);
			}
		}
		person.stateChanged();
	}
	

	public void msgDepleteCookSupply() {
		
		inventoryoffood.put("chicken", 0);
		inventoryoffood.put("burrito", 0);
		inventoryoffood.put("pizza", 0);
		person.stateChanged();
		
	}
	
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	
	
	public boolean pickAndExecuteAnAction() {		
		
	
		/*
		for(finishedorder findfinishedorder: finishedorders) {
			if(findfinishedorder.state == finishedorderstate.pickedupbywaiter)
			{
				finishedorders.remove(findfinishedorder);
				return true;
			}
			
		}
		*/
		
		if(tellcashiertopayforsupplies == true)
		{
			
			checkfrommarket currentcheckfrommarket;
			currentcheckfrommarket = checksfrommarket.remove(0);
			cashier.msgReceivedCheckFromCook(currentcheckfrommarket.ordertotal, currentcheckfrommarket.market);
			tellcashiertopayforsupplies = false;
			return true;
		}
		
	
		if(inventoryoffood.get("chicken") == 0 && callchickenmarket == false)
		{
			Do("current amount of chicken: " + inventoryoffood.get("chicken") + ", order chicken from the market");
			//create
			for(mymarket findmarket: markets)
			{
				if(findmarket.suppliesoffood.get("chicken") == true)
				{
					log("Ordering chicken from the market");
					List<OrderItem> orders= new ArrayList<OrderItem>();
					OrderItem chicken= new OrderItem("chicken", 5);
					orders.add(chicken);
					MarketOrder order= new MarketOrder(orders, "rest5", person);
					person.getCityMap().msgMarketHereIsTruckOrder(5, order);
					//findmarket.market.msgReceviedOrderFromCook(this,"chicken");
					callchickenmarket = true;
					return true;
				}
				
			}
			
		}
		
		if(inventoryoffood.get("burrito") == 0 && callburritomarket == false)
		{
			Do("current amount of burrito: " + inventoryoffood.get("burrito") + ", order burrito from the market");
			for(mymarket findmarket: markets)
			{
				if(findmarket.suppliesoffood.get("burrito") == true)
				{
					log("Ordering burrito from the marekt");
					List<OrderItem> orders= new ArrayList<OrderItem>();
					OrderItem burrito= new OrderItem("burrito", 5);
					orders.add(burrito);
					MarketOrder order= new MarketOrder(orders, "rest5", person);
					person.getCityMap().msgMarketHereIsTruckOrder(5, order);
					//findmarket.market.msgReceviedOrderFromCook(this,"burrito");
					callburritomarket = true;
					return true;
				}
				
			}
			
			
		}
		
		if(inventoryoffood.get("pizza") == 0 && callpizzamarket == false)
		{
			Do("current amount of pizza: " + inventoryoffood.get("pizza") + ", order pizza from the market");
			for(mymarket findmarket: markets)
			{
				if(findmarket.suppliesoffood.get("pizza") == true)
				{
					
					List<OrderItem> orders= new ArrayList<OrderItem>();
					OrderItem pizza = new OrderItem("pizza", 5);
					orders.add(pizza);
					MarketOrder order= new MarketOrder(orders, "rest5", person);
					person.getCityMap().msgMarketHereIsTruckOrder(5, order);
					//findmarket.market.msgReceviedOrderFromCook(this,"pizza");
					callpizzamarket = true;
					return true;
				}
				
			}
			
		}
		
		
		if(!cookingorders.isEmpty())
		{
			
		synchronized(cookingorders)
		{
			
			
		for(cookingorder checkcookingorder : cookingorders)
		{
			
			if(checkcookingorder.state == cookingorderstate.pickedupbywaiter)
			{
				cookingorders.remove(checkcookingorder);
				CookGui.msgfoodpickedup(checkcookingorder);
				return true;
			}
			
			if(checkcookingorder.state == cookingorderstate.donecooking)
			{
				this.state = cookstate.plating;
				checkcookingorder.state = cookingorderstate.waiting;
				tellWaiterFoodIsReady(checkcookingorder.waiter, checkcookingorder.order, checkcookingorder.assignedtablenumber);
				CookGui.msgOrderGuiPending(checkcookingorder.order, checkcookingorder.waiter);
				return true;
			}
			if(checkcookingorder.state == cookingorderstate.pending)
			{
				this.state = cookstate.cooking;
				checkcookingorder.state = cookingorderstate.cooking;
				CookGui.msgAddOrderGui(checkcookingorder.order, checkcookingorder.waiter);
				cookingOrder(checkcookingorder);
				return true;
			}
			
			
	
			
		}
		
		}

		}
		
		
		return false; 
	}

	// Actions

	public void setGui(Restaurant5CookGui gui) {
		CookGui = gui;
	}

	public Restaurant5CookGui getGui() {
		return CookGui;
	}
	
	private void cookingOrder(final cookingorder currentorder) {
		//each order has different cooking time
	
	//String order = currentorder.order;
	
	if(inventoryoffood.get(currentorder.order) == 0)
	{
			log("food is out");
			currentorder.waiter.msgFoodIsOut(currentorder.order, currentorder.assignedtablenumber);
			cooking = false;
	}
	else
	{
	
			String order = currentorder.order;
			//this.state = cookstate.cooking;
			if(order == "chicken") {
			
			inventoryoffood.put("chicken", inventoryoffood.get("chicken") - 1);	
			log("inventory of chicken " + inventoryoffood.get("chicken"));
			 //currentorder.state = cookingorderstate.pending;
			timerforcooking.schedule(new TimerTask() {
			//Object cookie = 1;
			public void run() {
			    log("done cooking");
			    msgDoneCooking(currentorder);
			    person.stateChanged();
			}
			},
			cookingtimeforchicken * 1000);//how long to wait before running task
		
		}
		
		else if(order == "pizza") {
			
			inventoryoffood.put("pizza", inventoryoffood.get("pizza") - 1);	
			log("inventory of pizza " + inventoryoffood.get("pizza"));
			// currentorder.state = cookingorderstate.pending;
			timerforcooking.schedule(new TimerTask() {
				//Object cookie = 1;
				public void run() {
				   
				    log("done cooking");
				    msgDoneCooking(currentorder);
				    person.stateChanged();
				}
				},
				cookingtimeforpizza * 1000);//how long to wait before running task
			
		}
		
		else if(order == "burrito") {
			
			inventoryoffood.put("burrito", inventoryoffood.get("burrito") - 1);	
			log("inventory of burrito " + inventoryoffood.get("burrito"));
			//currentorder.state = cookingorderstate.pending;
			timerforcooking.schedule(new TimerTask() {
				//Object cookie = 1;
				public void run() {
				   // cooking = false;
				    //donecooking = true;
				    log("done cooking");
				    msgDoneCooking(currentorder);
				    person.stateChanged();
				}
				},
				cookingtimeforburrito * 1000);//how long to wait before running tas	

		}
	//}
	}
	}
	
	public void tellWaiterFoodIsReady(Restaurant5Waiter waiter, String order, int table) {
		log("waiter: " + waiter.getName() + " bring " + order + " to table: " + table);
		waiter.msgFoodIsReady(order, table);
	}
	
	public void addMarket(Restaurant5MarketRole market) {
		markets.add(new mymarket(market));
	}
	
	public void addCashier(Restaurant5CashierRole cashier) {
		this.cashier = cashier;
	}
	
	public void setOrderSpindle(OrderSpindle5 orderspindle) {
		this.orderspindle = orderspindle;
	}
	
	
	//order class for cook
	public static class cookingorder {
		public Restaurant5Waiter waiter;
		public String order;
		int assignedtablenumber;
		boolean donecooking = false;
		public cookingorderstate state;
		public cookingorder(Restaurant5Waiter waiter, String order, int assignedtablenumber)
		{
			this.waiter = waiter;
			this.order = order;
			this.assignedtablenumber = assignedtablenumber;
			state = cookingorderstate.pending;
		}
		
	}
	
	public class finishedorder {
		Restaurant5WaiterRole waiter;
		public String order;
		int assignedtablenumber;
		public finishedorderstate state;
		
		public finishedorder(Restaurant5WaiterRole waiter, String order , int assignedtablenumber)
		{
			this.waiter = waiter;
			this.order = order;
			this.assignedtablenumber = assignedtablenumber;
			state = finishedorderstate.pending;
		}
		
	}
	
	public class checkfrommarket {
		Restaurant5Market market;
		String order;
		int ordertotal;
		Restaurant5Menu menu = new Restaurant5Menu();
		
		public checkfrommarket(String order , Restaurant5Market market) {
			this.order = order;
			this.market = market;
			ordertotal = menu.m.get(this.order) * 3;
		}
		
	}
	
	//mymarket class for cook
	private static class mymarket 	{
		Restaurant5Market market;
		Map<String, Boolean> suppliesoffood; 
		public mymarket(Restaurant5Market market)
		{
			this.market = market;
			suppliesoffood = new HashMap<String, Boolean>();
			suppliesoffood.put("chicken", true);
			suppliesoffood.put("pizza", true);
			suppliesoffood.put("burrito", true);
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

