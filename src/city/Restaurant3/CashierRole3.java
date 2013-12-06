package city.Restaurant3;

import Role.Role;
import activityLog.ActivityLog;
import activityLog.ActivityTag;
import agent.Agent;

import java.util.*;

import city.PersonAgent;
import test.mock.EventLog;
import test.mock.LoggedEvent;

/**
 * Restaurant Cashier Agent
 */
public class CashierRole3 extends Role {
	
	// Variable Declarations
	private String name;
	public List<Check> myChecks;
	private double checkAmount;
	public EventLog log;
	public double myMoney;
	
	PersonAgent person;
	String roleName = "Restaurant3CashierRole";
	
	ActivityTag tag = ActivityTag.RESTAURANT3CASHIER;
	
	public CashierRole3(String name, PersonAgent p) {
		super();
		this.name = name;
		myChecks = Collections.synchronizedList(new ArrayList<Check>());
		checkAmount = 0;
		myMoney = 10000.0;
		log = new EventLog();
		person = p;
	}
	
	// Messages
	public void calculateCheck(WaiterRole3 w, CustomerRole3 customer, String choice){
		Do("Calculating check for customer " + customer.getCustomerName() + " who ordered " + choice + ".");
		Check newCheck = new Check(w, customer, choice); // Add in new check to be calculated for this customer
		myChecks.add(newCheck);
		person.stateChanged();
	}
	
	public void acceptPayment(CustomerRole3 c, double amountPaid){
		Do("Accepting payment of $" + amountPaid + " from customer " + c.getCustomerName() + ".");
		// Lookup check to mark it as paid
		if (!myChecks.isEmpty()) {
			synchronized(myChecks){
				for (Check check : myChecks) {
					if (!check.type.equals(checkType.marketCheck)) {
						if (check.customer.equals(c)){
							processCustomerPayment(c, amountPaid, check); // Process this check with action below
						}
					}
				}
			}
		}
		person.stateChanged();
	}
	
	public void acceptMarketBill(MarketRole3 market, double amountDue){
		// Create new check that needs to be paid
		Do("Creating check for market " + market.getName() + " based on incoming bill/message.");
		//Check newCheck = new Check(market, (float)amountDue); // Add in new check to be calculated for this market
		//myChecks.add(newCheck);
		person.stateChanged();
	}

	// Scheduler
	public boolean pickAndExecuteAnAction() {
		if (!myChecks.isEmpty()) {
			synchronized(myChecks){
			for (Check check : myChecks) {
					if (check.getStatus() == checkStatus.pending && check.getType() == checkType.customerCheck) {
						processCheckToWaiter(check);
						return true;
					}
				}
			}
			synchronized(myChecks){
				for (Check check : myChecks) {
					if (check.getStatus() == checkStatus.pending && check.getType() == checkType.marketCheck) {
						payMarket(check);
						return true;
					}
				}
			}
		}
		return false;
	}

	// Actions
	public void processCheckToWaiter(Check c){ // Mark check as calculated and send back to waiter
		Do("Processing check and sending it back to waiter.");
		Menu3 myMenu = new Menu3();
		checkAmount = myMenu.getPriceofItem(c.choice);
		c.amount = checkAmount;
		c.status = checkStatus.calculated;
		WaiterRole3 w = c.waiter;
		w.hereIsCheck(c.customer, c.amount);
	}
	
	public void processCustomerPayment(CustomerRole3 customer, double amountPaid, Check c){
		Do("Processing payment of $" + amountPaid + " from customer " + customer.getCustomerName() + ".");
		if (amountPaid == c.amount){ // Customer paid exact amount
			c.status = checkStatus.paid;
			myMoney = myMoney + c.amount;
		} else if (amountPaid > c.amount){ // Customer paid more than their order, dispense the difference to them in change
			customer.dispenseChange(amountPaid - c.amount);
			myMoney = myMoney + c.amount; // Double-test
			c.status = checkStatus.paid;
		} else if (amountPaid < c.amount){ // Customer cannot afford to pay for what they ordered! Send them a shame command.
			customer.goToCorner();
			myMoney = myMoney + amountPaid;
		}
	}
	
	public void payMarket(Check c){ // Complete processing of check
		Do("Completing payment of marketCheck by sending money back to market.");
		//Market m = c.getMarket();
		//m.acceptCashierPayment(this, c.amount);
		c.status = checkStatus.paid;
	}
	
	// Misc. utilities
	public enum checkStatus {pending, calculated, paid}; // Used in conjunction with Check class below
	public enum checkType {customerCheck, marketCheck}; // Used in conjunction with Check class below
	
	public class Check {
		
		public CustomerRole3 customer;
		public WaiterRole3 waiter;
		//public Market market;
		public double amount;
		public checkStatus status;
		public String choice;
		public checkType type;
		
		public Check(float checkAmount){
			amount = checkAmount;
			type = checkType.customerCheck;
		}
		
		public Check(WaiterRole3 w, CustomerRole3 customer2, String ch){
			customer = customer2;
			waiter = w;
			choice = ch;
			status = checkStatus.pending;
			type = checkType.customerCheck;
		}
		
		public Check(WaiterRole3 w, CustomerRole3 c, String ch, float checkAmount){
			customer = c;
			waiter = w;
			choice = ch;
			status = checkStatus.pending;
			amount = checkAmount;
			type = checkType.customerCheck;
		}
		
		//public Check(Market market2, float checkAmount){
			//market = market2;
			//status = checkStatus.pending;
			//amount = checkAmount;
			//type = checkType.marketCheck;
		//}
		
		public Check(WaiterRole3 w, CustomerRole3 c){
			customer = c;
			waiter = w;
			status = checkStatus.pending;
			type = checkType.customerCheck;
		}
		
		public void setCustomer(CustomerRole3 c){
			customer = c;
		}
		
		public void setWaiter(WaiterRole3 w){
			waiter = w;
		}
		
		public checkStatus getStatus(){
			return status;
		}
		
		public checkType getType(){
			return type;
		}
		
		//public Market3 getMarket(){
		//	return market;
		//}
		
	}
	
	// Accessors
	public String getName() {
		return name;
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