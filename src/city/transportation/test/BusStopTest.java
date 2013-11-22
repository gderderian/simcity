package city.transportation.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import city.PersonAgent;
import city.transportation.BusAgent;
import city.transportation.BusAgent.BusEvent;
import city.transportation.BusAgent.BusState;
import city.transportation.BusStopAgent;
import city.transportation.mock.MockBusStop;

public class BusStopTest extends TestCase {
	//instantiated in setUp()
	List<PersonAgent> people;
	BusStopAgent stop;
	BusAgent bus1;
	BusAgent bus2;
	BusAgent bus3;

	public void setUp() throws Exception {
		super.setUp();
		people = new ArrayList<PersonAgent>();
		for(int i = 0; i < 10; i++) { //Adds 10 people to the list.
			people.add(new PersonAgent("person", null));
		}
		bus1 = new BusAgent();
		bus2 = new BusAgent();
		bus3 = new BusAgent();
		
		stop = new BusStopAgent();
		
	}	

	/* This tests the cashier receiving a single check request from a waiter which is then paid off by a customer */
	public void testBusDriving() {		
		
		//Preconditions
		assertTrue(stop.peopleWaiting.size() == 0);
		assertTrue(stop.buses.size() == 0);
		
		//10 people start waiting for bus
		for(int i = 0; i < 10; i++) {
			stop.msgWaitingForBus(people.get(i));
		}
		
		//Postconditions
		assertTrue(stop.peopleWaiting.size() == 10);
		assertTrue(stop.buses.size() == 0);
		
		//Scheduler
		stop.pickAndExecuteAnAction();
		
		//Nothing should have changed
		assertTrue(stop.peopleWaiting.size() == 10);
		assertTrue(stop.buses.size() == 0);
		
		//Message - bus can pick up 4 people
		stop.msgICanPickUp(bus1, 4);
		assertTrue(stop.buses.size() == 1);
		
		//Scheduler - should send 4 passengers to the bus
		stop.pickAndExecuteAnAction();

		//Postconditions
		assertTrue(stop.peopleWaiting.size() == 6);
		assertTrue(stop.buses.size() == 0);
		
		//Message - bus can pick up 10 people (only 6 in list)
		stop.msgICanPickUp(bus2, 10);
		assertTrue(stop.buses.size() == 1);
		
		//Scheduler - should send 4 passengers to the bus
		stop.pickAndExecuteAnAction();

		//Postconditions
		assertTrue(stop.peopleWaiting.size() == 0);
		assertTrue(stop.buses.size() == 0);
		
		//10 people start waiting for bus
		for(int i = 0; i < 10; i++) {
			stop.msgWaitingForBus(people.get(i));
		}
		
		//Messages from 2 buses
		stop.msgICanPickUp(bus3, 6);
		stop.msgICanPickUp(bus1, 5);
		assertTrue(stop.buses.size() == 2);
		
		//2 calls of scheduler to send passengers to 2 buses
		stop.pickAndExecuteAnAction();
		stop.pickAndExecuteAnAction();
		
		//Postconditions
		assertTrue(stop.peopleWaiting.size() == 0); //no one left waiting
		assertTrue(stop.buses.size() == 0);
		
		//Message from bus
		stop.msgICanPickUp(bus2, 4);
		assertTrue(stop.buses.size() == 1);
		
		//Scheduler - should not send any passengers
		stop.pickAndExecuteAnAction();
		
		//Postconditions
		assertTrue(stop.peopleWaiting.size() == 0);
		assertTrue(stop.buses.size() == 0);
	}
}
