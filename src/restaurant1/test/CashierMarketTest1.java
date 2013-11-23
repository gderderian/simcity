package restaurant1.test;

import restaurant1.Restaurant1CashierRole;
import restaurant1.Restaurant1CashierRole.MarketBill;
import restaurant1.test.mock.Restaurant1MockCustomer;
import restaurant1.test.mock.Restaurant1MockMarket;
import restaurant1.test.mock.Restaurant1MockWaiter;

import junit.framework.*;

/* A class to test a simple normative scenario between cashier and market */

public class CashierMarketTest1 extends TestCase
{
	//instantiated in setUp()
	Restaurant1CashierRole cashier;
	Restaurant1MockMarket market;
	double billAmount;
	
	public void setUp() throws Exception {
		super.setUp();		
		cashier = new Restaurant1CashierRole("cashier");
		market = new Restaurant1MockMarket("mockMarket");
		
		billAmount = 50.0;
	}	
	/* This tests the cashier receiving a single bill and paying off the bill to the market */
	public void testOneMarketBill() {		
		
		//Preconditions
		assertTrue(cashier.marketBills.isEmpty()); //Cashier should have no bills
		assertEquals(cashier.money, 1000.0); //Cashier starts with $1000
		assertEquals(market.money, 1000.0); //Market starts with $1000
		assertEquals(cashier.stateChange.availablePermits(), 1); //stateChange semaphore should have 1 permit to start with
		
		//Send message
		cashier.msgYouOwe(market, billAmount); //A $50 bill for the cashier.
		
		//Postconditions of first message
		assertTrue(cashier.marketBills.size() == 1); //Cashier should now have a single bill
		MarketBill bill = cashier.marketBills.get(0); 
		assertEquals(bill.m, market); //Bill should be associated with the market
		assertEquals(bill.amountOwed, billAmount);
		assertEquals(cashier.stateChange.availablePermits(), 2); //stateChange semaphore should have 2 permits
		
		//Scheduler call
		assertTrue(cashier.pickAndExecuteAnAction()); //scheduler should call an action to pay the bill
		
		//Final postconditions
		assertEquals(cashier.marketBills.size(), 0); //No bills left to pay
		assertEquals(cashier.stateChange.availablePermits(), 2); //Still should have 2 permits
		assertTrue(market.log.containsString("Received payment for order.")); //Log should have a record of this message call
		assertEquals(cashier.money, 1000.0 - billAmount); //Cashier's money goes down by $50
		assertEquals(market.money, 1000.0 + billAmount); //Market's money goes up by $50
	
		assertFalse(cashier.pickAndExecuteAnAction()); //No more actions to be called by cashier's scheduler!
	}
}
