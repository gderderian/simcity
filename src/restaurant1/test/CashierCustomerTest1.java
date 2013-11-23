package restaurant1.test;

import restaurant1.Restaurant1CashierRole;
import restaurant1.Restaurant1Check;
import restaurant1.Restaurant1CashierRole.MarketBill;
import restaurant1.Restaurant1CashierRole.MyCheck;
import restaurant1.Restaurant1CashierRole.checkState;
import restaurant1.test.mock.Restaurant1MockCustomer;
import restaurant1.test.mock.Restaurant1MockMarket;
import restaurant1.test.mock.Restaurant1MockWaiter;

import junit.framework.*;

/* A class to test a simple normative scenario between cashier and waiter/customer */

public class CashierCustomerTest1 extends TestCase
{
	//instantiated in setUp()
	Restaurant1CashierRole cashier;
	Restaurant1MockCustomer customer;
	Restaurant1MockWaiter waiter;
	String orderFood;
	
	public void setUp() throws Exception {
		super.setUp();		
		cashier = new Restaurant1CashierRole("cashier");
		waiter = new Restaurant1MockWaiter("waiter");
		customer = new Restaurant1MockCustomer("customer");
		orderFood = "steak";
	}	
	
	/* This tests the cashier receiving a single check request from a waiter which is then paid off by a customer */
	public void testOneCustomerCheck() {		
		
		//Preconditions
		assertTrue(cashier.checks.isEmpty()); //Cashier should have no bills
		assertEquals(cashier.money, 1000.0); //Cashier starts with $1000
		assertEquals(customer.money, 100.0); //Customer starts with $100
		assertEquals(cashier.stateChange.availablePermits(), 1); //stateChange semaphore should have 1 permit to start with
		
		//Send message
		cashier.msgProduceCheck(waiter, customer, orderFood); //A request for the cashier to produce a check
		
		//Postconditions of first message
		assertTrue(cashier.checks.size() == 1); //Cashier should now have a single check
		Restaurant1Check check = cashier.checks.get(0).c; 
		assertEquals(check.cust, customer); //Check should have the correct customer
		assertEquals(check.choice, orderFood); //Check should have correct choice
		assertEquals(check.amount, 15.99); //Check should be created with correct amount of money owed
		assertEquals(cashier.stateChange.availablePermits(), 2); //stateChange semaphore should have 2 permits
		
		//Scheduler call
		assertTrue(cashier.pickAndExecuteAnAction()); //scheduler should call an action to give bill to waiter
		
		//Postconditions of scheduler call
		assertEquals(cashier.stateChange.availablePermits(), 2); //Still should have 2 permits
		assertTrue(waiter.log.containsString("Received check from cashier.")); //Correct message call in waiter
		assertTrue(cashier.checks.size() == 1); //No more checks added
		
		//Message from customer to pay bill
		cashier.msgPayBill(check, customer.money);
		
		//Postconditions of message
		assertEquals(cashier.stateChange.availablePermits(), 3); //An extra permit from message call
		assertTrue(cashier.checks.size() == 1); //No checks added
		MyCheck mc = cashier.checks.get(0);
		assertEquals(mc.amountPaid, customer.money); //Amount paid is all of customer's money
		assertTrue(mc.state == checkState.fullyPaid); //Check has been paid
		
		//Scheduler
		assertTrue(cashier.pickAndExecuteAnAction());
		
		//Postconditions of scheduler
		assertTrue(mc.state == checkState.finished); //Check exchange is finished
		assertTrue(customer.log.containsString("Received change from cashier. Leaving restaurant.")); //Change was given to customer
		assertEquals(cashier.money, 1000.0 + check.amount); //Cashier received correct amount of money from check payment
		
		assertFalse(cashier.pickAndExecuteAnAction()); //No more actions to be called by cashier's scheduler!
	}
}
