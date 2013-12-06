package city.Restaurant5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import activityLog.ActivityLog;
import activityLog.ActivityTag;
import test.mock.EventLog;
import test.mock.LoggedEvent;
//import tomtesting.interfaces.Check;
import tomtesting.interfaces.Restaurant5Customer;
import tomtesting.interfaces.Restaurant5Market;
import tomtesting.interfaces.Restaurant5Waiter;
import tomtesting.interfaces.Restaurant5Cashier;
import Role.Role;
import city.PersonAgent;
import city.gui.Restaurant5.Restaurant5CookGui;
//import Restaurant5Check;


/**
 * Restaurant Market Agent
 */

public class Restaurant5CashierRole extends Role implements Restaurant5Cashier{
	
	String roleName = "Restaurant5CashierRole";
	
	public List<Restaurant5Check> checksforwaiter = Collections.synchronizedList( new ArrayList<Restaurant5Check>());
	public List<Restaurant5Check> checksforcustomer = Collections.synchronizedList(new ArrayList<Restaurant5Check>());
	public List<checkformarket> checksformarket = Collections.synchronizedList(new ArrayList<checkformarket>());
	
	public EventLog log = new EventLog();
	
	
	Timer timerforcalculating = new Timer();
	private String name;
	public Restaurant5CookGui CookGui = null;
	int calculatingtime = 1;
	boolean calculating = false;
	boolean donecalculating = false;
	boolean makingcheck = false;
	boolean donemakingcheck = false;
	boolean checkforwaiter = false;
	public boolean checkforcustomer = false;
	boolean paymarket = false;
	
	PersonAgent person;
	int currentmoney = 100;
    Restaurant5Check currentcheckforwaiter;
    Restaurant5Check currentcheckforcustomer;
    Restaurant5Menu menu = new Restaurant5Menu();
    
    ActivityTag tag = ActivityTag.RESTAURANT5CASHIER;
     
	
	public Restaurant5CashierRole(String name, PersonAgent person) {
		super();
		building = "rest5";
		this.name = name;
		this.person = person;
	}

	// Messages
	//After receiving message from the cook, start packing supplies
	public void msgReceviedCheckFromCustomer(Restaurant5Check checkfromcustomer ) {
		
		log.add(new LoggedEvent("Received ReadyToPay"));
		log("Received check from " + checkfromcustomer.customer.getName());
		checksforcustomer.add(checkfromcustomer);
		log("new check added to the list");
		checkforcustomer = true;
		person.stateChanged();
	}
	
	public void msgMakeCheckForWaiter(Restaurant5Customer customer, String choice, int table , Restaurant5Waiter waiter) {
		
		log.add(new LoggedEvent("Received request from waiter"));
		log("Received request for check from " + waiter.getName());
		int total = menu.m.get(choice);
		Restaurant5Check newcheck = new Restaurant5Check(customer, total, table);
		newcheck.setwaiter(waiter);
		checksforwaiter.add(newcheck);
		log("new check added to the list");
		checkforwaiter = true; 
		person.stateChanged();
	
	}
	
	public void msgReceivedCheckFromCook(int total, Restaurant5Market market) {
		
		log.add(new LoggedEvent("Received request from cook"));
		if(currentmoney >= total)
		{
			Do("I paid $" + total + " for cook's order");
			currentmoney -= total;
			Do("Now I'm left with $" + currentmoney);
			checksformarket.add(new checkformarket(market, total));
			paymarket = true;
		}
		else
		{
			Do("I'm don't have enough money to pay for that!");
		}
		
		
		person.stateChanged();
	}



	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	
	public boolean pickAndExecuteAnAction() {	
		
		try{
			
		if(paymarket == true) {
			paymarket = false;
			checkformarket currentcheckformarket = checksformarket.remove(0);
			currentcheckformarket.market.msgReceivedCheckFromCashier(currentcheckformarket.amount);
			return true;
		}
		
	
		//if done cooking order, tell the waiter that the food is ready
		if(donemakingcheck == true) {
			donemakingcheck = false;
			tellWaiterCheckIsReady(currentcheckforwaiter);
			return true;
		}
		
		
		if(donecalculating == true) {
			
			donecalculating = false;
			returnMoneyToCustomer(currentcheckforcustomer);	
			return true;
		}
		
		if(!checksforwaiter.isEmpty() && makingcheck == false && checkforwaiter == true) {
			makingcheck = true;
			checkforwaiter = false;
			currentcheckforwaiter = checksforwaiter.remove(0);
		    makingCheck(currentcheckforwaiter);
		     return true;
		}
		
		//pick up and order and start cooking
		if(!checksforcustomer.isEmpty() && calculating == false && checkforcustomer == true) {
			calculating = true;
			checkforcustomer = false;
			currentcheckforcustomer = checksforcustomer.remove(0);
			if(!checksforcustomer.isEmpty())
			{
				checkforcustomer = true;
			}
		    calculatingCheck(currentcheckforcustomer);
		     return true;
		}
		
		
		
		
		
		return false; 
		}catch(Exception e){
			return true;
		}
	}

	// Actions
	//utilities

	public void setGui(Restaurant5CookGui gui) {
		CookGui = gui;
	}

	public Restaurant5CookGui getGui() {
		return CookGui;
	}
	
	public void calculatingCheck(Restaurant5Check currentcheck) {
		// calculates subtotal
		 int subtotal = currentcheck.amountcustomerpaid - currentcheck.total; 
		 //log("subtotal calculated " + subtotal);
		 currentcheck.setsubtotal(subtotal);
			timerforcalculating.schedule(new TimerTask() {
			//Object cookie = 1;
			public void run() {
			    calculating = false;
			    donecalculating = true;   
			    log("done calculating");
			    person.stateChanged();
			}
			},
			calculatingtime * 1000);//how long to wait before running task

	}
	
	public void makingCheck(Restaurant5Check currentcheck) {
		// calculates subtotal
		 log("total is $" + currentcheck.total);
			timerforcalculating.schedule(new TimerTask() {
			//Object cookie = 1;
			public void run() {
			    makingcheck = false;
			    donemakingcheck = true;   
			    log("done making check");
			    person.stateChanged();
			}
			},
			calculatingtime * 1000);//how long to wait before running task

	}
	
	
	public void returnMoneyToCustomer(Restaurant5Check customercheck) {
		
		
		log("Here is your subtotal $" + customercheck.subtotal + " customer: " + customercheck.customer.getName());
		if(customercheck.subtotal >= 0) {
		customercheck.customer.msgReceivedMoneyFromCashier(customercheck.subtotal);
		}
		else {
		log("customer has to pay back later");
		int paybackmoney = customercheck.subtotal *= -1; 
		customercheck.customer.msgDontHaveMoneyPayBackLater(paybackmoney);
		//customercheck.customer.msgDontHaveMoneyWashDishes();
		}
	
	}
	
	public void tellWaiterCheckIsReady(Restaurant5Check check) {
		log("check is ready");
		check.waiter.msgCheckIsReady(check);
	}

	/*
	public void msgMakeCheckForWaiter(Customer customer, String choice, int table, Waiter waiter) {
		// TODO Auto-generated method stub
		
	}
	*/
	//supply class for market
	
	
	class checkformarket {
		Restaurant5Market market;
		int amount;
		checkformarket(Restaurant5Market market, int amount) {
			this.market = market;
			this.amount = amount;
		}
		
	}


	private void log(String msg){
		print(msg);
        ActivityLog.getInstance().logActivity(tag, msg, name);
        log.add(new LoggedEvent(msg));
	}

	@Override
	public String getRoleName() {
		return roleName;
	}
	


}

