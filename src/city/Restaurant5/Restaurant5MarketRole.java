package city.Restaurant5;

import restaurant.CustomerAgent.AgentEvent;
import restaurant.gui.CookGui;
import tomtesting.interfaces.Restaurant5Cook;
import tomtesting.interfaces.Restaurant5Market;
import restaurant.test.mock.EventLog;
import Role.Role;
import agent.Agent;
import test.mock.EventLog;
import test.mock.LoggedEvent;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Restaurant Market Agent
 */

public class Restaurant5MarketRole extends Role implements Restaurant5Market{
	
	public List<supplyorders> supplyorders = Collections.synchronizedList(new ArrayList<supplyorders>());
	
	Timer timerforcooking = new Timer();
	private String name;
	private String type;
	public Restaurant5CookGui CookGui = null;
	int packingtimeforchicken = 3;
	int packingtimeforburrito = 3;
	int packingtimeforpizza = 3;
	boolean packing = false;
	boolean donepacking = false;
	boolean foodisout = false;
    supplyorders currentorder;
	
	Map<String, Integer> inventoryofsupplies = new HashMap<String, Integer>(); // (name of food, amount)

	public Object log = new EventLog();
	
	public Restaurant5MarketRole(String type) {
		super();
		this.name = type;
		this.type = type;
		//inventoryofsupplies.put(this.type, 0);
		inventoryofsupplies.put("chicken", 3);
		inventoryofsupplies.put("burrito", 3);
		inventoryofsupplies.put("pizza", 3);
		
	}

	// Messages
	//After receiving message from the cook, start packing supplies
	public void msgReceviedOrderFromCook(Restaurant5Cook cook, String order) {
		
		Do(" " + name +": Received order: " + order + " from cook");
		supplyorders.add( new supplyorders(cook,order));
		print("supply order added to the list");
		stateChanged();
	}
	
	public void msgReceivedCheckFromCashier(int total) {
		
		Do("Received $" + total + " from cashier");
		stateChanged();
		
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	
	
	protected boolean pickAndExecuteAnAction() {		
		
		//if done cooking order, tell the waiter that the food is ready
		if(donepacking == true) {
			donepacking = false;
			tellCookOrderIsReady(currentorder.cook, currentorder.order);	
			return true;
		}
		
		//pick up and order and start cooking
		if(!supplyorders.isEmpty() && packing == false) {
			packing = true;
			currentorder = supplyorders.remove(0);
		    packingOrder(currentorder);
		     return true;
		}
		
		return false; 
	}

	// Actions
	//utilities

	public void setGui(Restaurant5CookGui gui) {
		CookGui = gui;
	}

	public Restaurant5CookGui getGui() {
		return CookGui;
	}
	
	public void packingOrder(supplyorders currentorder) {
		//each order has different cooking time
	
	String order = currentorder.order;
	
	//This is the problem	
	
	if(inventoryofsupplies.get(order) == 0)
	{
			Do(" " + name + ": " + order + " is out in " + this.name);
			
			currentorder.cook.msgSupplyIsOut(currentorder.order, this);
			packing = false;
	}
	else
		{
		
			if(order == "chicken") {
			
			inventoryofsupplies.put("chicken", inventoryofsupplies.get("chicken") - 3);	
			//print("" + inventoryofsupplies.get("chicken"));
			timerforcooking.schedule(new TimerTask() {
			//Object cookie = 1;
			public void run() {
			    packing = false;
			    donepacking = true;
			    print("done packing");
			    stateChanged();
			}
			},
			packingtimeforchicken * 1000);//how long to wait before running task
		
		}
		
		else if(order == "pizza") {
			
			inventoryofsupplies.put("pizza", inventoryofsupplies.get("pizza") - 3);	
			//print("" + inventoryofsupplies.get("pizza"));
			timerforcooking.schedule(new TimerTask() {
				//Object cookie = 1;
				public void run() {
				    packing = false;
				    donepacking = true;
				    print("done packing");
				    stateChanged();
				}
				},
				packingtimeforpizza * 1000);//how long to wait before running task
			
		}
		
		else if(order == "burrito") {
			
			inventoryofsupplies.put("burrito", inventoryofsupplies.get("burrito") - 3);	
			//print("" + inventoryofsupplies.get("burrito"));
			timerforcooking.schedule(new TimerTask() {
				//Object cookie = 1;
				public void run() {
				    packing = false;
				    donepacking = true;
				    print("done packing");
				    stateChanged();
				}
				},
				packingtimeforburrito * 1000);//how long to wait before running tas	

		}
			
		}
	}
	
	public void tellCookOrderIsReady(Restaurant5Cook cook, String order) {
		Do(" " + name + ": cook, your order " + order + " is ready");
		cook.msgReceivedSuppliesFromMarket(order, this);
	}
	
	String returntypeofmarket() {
		return this.type; 
	}
	
	public void depletemarketsupply() {
		inventoryofsupplies.put("chicken", 0);
		inventoryofsupplies.put("burrito", 0);
		inventoryofsupplies.put("pizza", 0);
	}
	
	//supply class for market
	
	public static class supplyorders {
		
		String order;
		Restaurant5Cook cook;
		
		public supplyorders(Restaurant5Cook cook, String order)
		{
			this.cook = cook;
			this.order = order;
		}
		
	}





}

