package city.Restaurant2;

import interfaces.MarketManager;
import interfaces.Restaurant2Cashier;
import interfaces.Restaurant2Customer;
import interfaces.Restaurant2Market;
import interfaces.Restaurant2Waiter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import activityLog.ActivityLog;
import activityLog.ActivityTag;
import city.PersonAgent;
import test.mock.EventLog;
import test.mock.LoggedEvent;
import Role.Role;

public class Restaurant2CashierRole extends Role implements Restaurant2Cashier {
	
	public enum CheckState {initial, unpaid, pending, processing, paid};
	public enum MarketBillState {pending, paid, processing};
	//unpaid means waiting for customer, pending means customer has given money and is waiting for change
	public List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());
	public List<MarketBill> marketBills = new ArrayList<MarketBill>();
	Map<String, Double> options = new HashMap<String, Double>();
	String name;
	
	public EventLog log = new EventLog();
	
	PersonAgent person;
	
	String roleName = "Restaurant2CashierRole";
	
	double restaurantMoney;
	
	ActivityTag tag = ActivityTag.RESTAURANT2CASHIER;
	
	private boolean test = false;
	
	public Restaurant2CashierRole(String n, PersonAgent p){
		super();
		building = "rest2";
		
		name = n;
		person = p;
		
		options.put("Steak", 15.99);
		options.put("Chicken", 10.99);
		options.put("Salad", 5.99);
		options.put("Pizza", 8.99);
		
		restaurantMoney = 100;
	}
	/*
	public void setPerson(PersonAgent p){
		person = p;
	}
*/
	
	//MESSAGES
	public void msgGenerateCheck(String food, Restaurant2Customer c, Restaurant2Waiter w){
		print("Generating check");
		boolean returningCustomer = false;
		for(Check ch : checks){
			if(ch.customer == c){
				ch.amount += options.get(food);
				ch.waiter = w;
				returningCustomer = true;
			}
		}
		if(!returningCustomer){
			checks.add(new Check(food, c, CheckState.initial, w));
		}
		person.stateChanged();
	}
	
	public void msgHereIsPayment(Restaurant2Customer c, double amount){
		print("Recieved payment of " + amount + " from customer " + c.getName());
		log.add(new LoggedEvent("Recieved payment of " + amount + " from customer " + c.getName()));
		for(Check ch : checks){
			if(ch.customer == c){
				ch.cs = CheckState.pending;
				ch.addPayment(amount);
				log.add(new LoggedEvent("Changed state of check to pending."));
				person.stateChanged();
				return;
			}
		}
	}
	
	public void msgChargeForOrder(double total, MarketManager m){
		print("Recieved msgChargeForOrder from market");
		marketBills.add(new MarketBill(m, total));
		person.stateChanged();
	}
	
	public void clearChecks(){		//ONLY for testing purposes
		checks.clear();
	}
	
	
	//SCHEDULER
	public boolean pickAndExecuteAnAction() {
		synchronized(checks){
			for(Check c : checks){
				if(c.cs == CheckState.initial){
					sendCheckToWaiter(c);
					c.cs = CheckState.unpaid;
					return true;
				}
			}
		}
		synchronized(checks){
			for(Check c : checks){
				if(c.cs == CheckState.pending){
					processCheck(c);
					c.cs = CheckState.processing;
					return true;
				}
			}
		}
		try{
			for(MarketBill b : marketBills){
				if(b.ms == MarketBillState.pending){
					payMarketBill(b);
					b.ms = MarketBillState.processing;
					return true;
				}
			}
		}
		catch(Exception e){
			return true;
		}
		
		return false;
	}
	
	//ACTIONS
	private void sendCheckToWaiter(Check c){
		c.waiter.msgHereIsCheck(c.customer, c.food, c.amount);
	}
	
	private void processCheck(Check c){
		log("Processing check");
		if(c.overdrawn != true){
			c.customer.msgHereIsYourChange(c.change);
			restaurantMoney += c.amount;
		}
		else{
			c.customer.msgHereIsYourChange(c.change);
			log(c.customer.getName() + " did not have enough to pay. Please pay the remaining blanace of " + c.amount + " next time you come in.");
		}
	}
	
	private void payMarketBill(MarketBill b){
		b.ms = MarketBillState.paid;
		print("Paying market " + b.amount + " for food shipment");
		b.marketManager.msgAcceptPayment(b.amount);
	}
	
	
	//CLASSES
	public class Check{
		public String food;
		public Restaurant2Customer customer;
		public double amount;
		public double payment;
		public double change;
		public CheckState cs;
		public boolean overdrawn;
		public Restaurant2Waiter waiter;
		
		public Check(String f, Restaurant2Customer c, CheckState state, Restaurant2Waiter w){
			food = f;
			customer = c;
			waiter = w;
			cs = state;
			overdrawn = false;
			amount = options.get(f);
		}
		
		public void addPayment(double pay){
			payment = pay;
			if(payment >= amount){
				change = payment - amount;
				payment = 0;
				overdrawn = false;
			}
			else{
				overdrawn = true;
				amount = amount - payment;
				payment = 0;
			}
		}
		
	}
	
	public class MarketBill{
		public double amount;
		public MarketManager marketManager;
		public MarketBillState ms;
		
		public MarketBill(MarketManager m, double a){
			marketManager = m;
			amount = a;
			ms = MarketBillState.pending;
		}
		
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String getRoleName() {
		return roleName;
	}

	@Override
	public PersonAgent getPerson() {
		return person;
	}

	public void msgGoHome() {
		person.leaveWork();
	}
	
	private void log(String msg){
		print(msg);
		if(!test){
			ActivityLog.getInstance().logActivity(tag, msg, name, true);
		}
		log.add(new LoggedEvent(msg));
	}
	
	public void setTesting(boolean t){
		test = t;
	}

}
