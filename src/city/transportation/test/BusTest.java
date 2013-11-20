package city.transportation.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import city.PersonAgent;
import city.transportation.BusAgent;
import city.transportation.BusStopAgent;
import city.transportation.CarAgent;

public class BusTest extends TestCase {
	//instantiated in setUp()
	List<PersonAgent> people;
	BusAgent bus;
	
	BusStopAgent stop1;
	BusStopAgent stop2;
	BusStopAgent stop3;
	BusStopAgent stop4;

	public void setUp() throws Exception {
		super.setUp();
		people = new ArrayList<PersonAgent>();
		for(int i = 0; i < 12; i++) { //Adds 11 people to the list - 1 more than bus's capacity;
			people.add(new PersonAgent("person", null));
		}
		bus = new BusAgent();
		
		stop1 = new BusStopAgent();
		stop2 = new BusStopAgent();
		stop3 = new BusStopAgent();
		stop4 = new BusStopAgent();
	}	

	/* This tests the cashier receiving a single check request from a waiter which is then paid off by a customer */
	public void testBusDriving() {		
		assertTrue(bus.capacity == 10);
	}
}
