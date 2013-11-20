package city.Restaurant4;

import Role.Role;

import java.util.*;

import justinetesting.interfaces.Waiter4;
import justinetesting.test.mock.EventLog4;
import justinetesting.test.mock.LoggedEvent4;


public class CashierRole4 extends Role {
	WaiterRole4 waiter;
	String name;
	Timer cook= new Timer();
	private List<Bill> bills= Collections.synchronizedList(new ArrayList<Bill>());
	public List<MarketBill> marketBills= Collections.synchronizedList(new ArrayList<MarketBill>());
	enum billState {pending, sent, paid, payNextTime, done};
	enum marketBillState{none, pending, paid}
	public EventLog4 log= new EventLog4();
	double cash= 100.00;
	

	public CashierRole4(String name) {
		super();
		this.name= name;
	}

	public String getName(){
		return name;
	}
	
	
	// MESSAGES 
	public void msgImStealingEveryCent(){
		print("One of the waiters robbed us!");
		cash= 0.0;
	}
	
	public void msgComputeBill(Waiter4 w, CustomerRole4 c, String choice){
		log.add(new LoggedEvent4("Received msgComputeBill from waiter"));
		Bill b= new Bill(w, c, choice);
		getBills().add(b);
		stateChanged();
	}
	
	public void msgHereIsMoney(CustomerRole4 customerRole4, double amount){
		log.add(new LoggedEvent4("Received msgHereIsMoney from customer. Amount = " + amount));
		Bill b= find(customerRole4);
		if(b.getAmount() <= amount){
			cash += amount;
			b.bs= billState.paid;
		}
		else{
			b.bs= billState.payNextTime;
		}
		print("The restaruant now has: $" + cash);
		stateChanged();
	}
	
	public void msgHereIsBill(MarketRole4 marketRole4, double amount){
		log.add(new LoggedEvent4("Received msgHereIsBill from market"));
		marketBills.add(new MarketBill(marketRole4, amount));
		stateChanged();
	}
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		synchronized(marketBills){
			for(MarketBill mb : marketBills){
				if(mb.mbs == marketBillState.pending){
					print("I should send the market their money for this delivery now.");
					sendMoney(mb);
					return true;
				}
			}
		}
		synchronized(bills){
			for(Bill bill : getBills()){
				if(bill.bs == billState.pending){
					print("I should give the waiter this bill, it's ready for the customer");
					sendCheck(bill);
					return true;
				}
			}
		}
		synchronized(bills){
			for(Bill bill : getBills()){
				if(bill.bs == billState.paid || bill.bs == billState.payNextTime){
					print("This customer is all set to go now!");
					tellCustomer(bill);
					return true;
				}
			}
		}
		return false;
	}

	
	// ACTIONS
	private void goToBank(){
		print("Seems like there isn't enough money to pay this market bill, I'll have to go to the bank and withdraw more");
		cash += 500;
	}
	
	private void sendCheck(Bill b){
		b.w.msgHereIsBill(b.getAmount(), b.c);
		b.bs= billState.sent;
	}
	
	private Bill find(CustomerRole4 customerRole4){
		Bill temp= getBills().get(0);  // initialize to the first customer to be updated in loop below
		for(Bill bill : getBills()){
			if( customerRole4 == bill.c && bill.bs != billState.done){
				return bill;
			}
		}
		return temp;
	}
	
	private void tellCustomer(Bill b){
		if(b.bs == billState.paid){
			print("The customer had enough money to pay!");
			b.c.msgHereIsChange(true);
		}
		else if(b.bs == billState.payNextTime){
			print("The customer didn't have enought money this time, make sure to pay next time");
			b.c.msgHereIsChange(false);
		}
		b.bs= billState.done;
		stateChanged();
	}
	
	private void sendMoney(MarketBill mb){
		if(cash - mb.amount >= 0){
			cash -= mb.amount;
			mb.m.msgHereIsMoney(mb.amount);
		}
		else{
			goToBank();
			cash -= mb.amount;
			mb.m.msgHereIsMoney(mb.amount);
		}
			print("The restaruant now has: $" + cash);
			marketBills.remove(mb);
			stateChanged();
	}

	public List<Bill> getBills() {
		return bills;
	}


	// CLASSES
	public static class Bill{
		Waiter4 w;
		public CustomerRole4 c;
		String choice;
		private double amount;
		billState bs;
		
			
		public Bill(Waiter4 w, CustomerRole4 c2, String choice){
			this.w= w;
			this.c= c2;
			this.choice= choice;
			bs= billState.pending;
			if(choice == "Steak"){
				setAmount(15.99);
			}
			else if(choice == "Chicken"){
				setAmount(10.99);
			}
			else if(choice == "Salad"){
				setAmount(5.99);
			}
			else if(choice == "Pizza"){
				setAmount(8.99);
			}
		}

		public double getAmount() {
			return amount;
		}


		public void setAmount(double amount) {
			this.amount = amount;
		}
	}

	public class MarketBill{
		public MarketRole4 m;
		public double amount;
		marketBillState mbs= marketBillState.none;
		
		MarketBill(MarketRole4 marketRole4, double amount){
			this.m= marketRole4;
			this.amount= amount;
			mbs= marketBillState.pending;
		}
	}
	
}