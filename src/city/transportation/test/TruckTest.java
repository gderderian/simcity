package city.transportation.test;

import junit.framework.TestCase;
import city.PersonAgent;
import city.transportation.CarAgent;
import city.transportation.TruckAgent;

public class TruckTest extends TestCase {
	//instantiated in setUp()
	PersonAgent recipient;
	TruckAgent truck;
	//Market - need market code for this.


	public void setUp() throws Exception {
		super.setUp();
		recipient = new PersonAgent("test", null);
		truck = new TruckAgent();
	}	

	/* This tests the cashier receiving a single check request from a waiter which is then paid off by a customer */
	public void testTruckDelivery() {		
		assertTrue(truck.capacity == 0);
	}
}