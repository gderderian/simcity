package city.transportation.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import city.PersonAgent;
import city.transportation.BusAgent;
import city.transportation.BusAgent.BusEvent;
import city.transportation.BusAgent.BusState;
import city.transportation.mock.MockBusStop;

public class BusTest extends TestCase {
	//instantiated in setUp()
	List<PersonAgent> people;
	BusAgent bus;
	
	MockBusStop stop1;
	MockBusStop stop2;
	MockBusStop stop3;
	MockBusStop stop4;

	public void setUp() throws Exception {
		super.setUp();
		people = new ArrayList<PersonAgent>();
		for(int i = 0; i < 10; i++) { //Adds 10 people to the list.
			people.add(new PersonAgent("person", null));
		}
		bus = new BusAgent(null);
		
		stop1 = new MockBusStop();
		stop2 = new MockBusStop();
		stop3 = new MockBusStop();
		stop4 = new MockBusStop();
		
		//Give bus reference to the 4 stops
		bus.busStops.add(stop1);
		bus.busStops.add(stop2);
		bus.busStops.add(stop3);
		bus.busStops.add(stop4);
	}	

	/* This tests the cashier receiving a single check request from a waiter which is then paid off by a customer */
	public void testBusDriving() {		
		
		//Preconditions
		assertTrue(bus.capacity == 10);
		assertTrue(bus.currentStop == 3);
		assertEquals(bus.passengers.size(), 0);
		assertTrue(bus.event == BusEvent.arrivedAtStop);
		assertTrue(bus.state == BusState.driving);
		
		//Scheduler
		bus.pickAndExecuteAnAction();
		
		//Postconditions
		assertTrue(bus.currentStop == 0);
		assertEquals(bus.passengers.size(), 0);
		assertTrue(bus.event == BusEvent.arrivedAtStop);
		assertTrue(bus.state == BusState.atStop);

		try { //Wait for bus to complete its time waiting at stop
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Postconditions
		assertTrue(bus.event == BusEvent.pickingUpPassengers);

		//Scheduler
		bus.pickAndExecuteAnAction();
		
		//Postconditions
		assertTrue(bus.state == BusState.pickingUpPassengers);
		
		//Message
		bus.msgPeopleBoarding(people); //Message to be sent by current bus stop
		
		//Postconditions
		assertEquals(bus.passengers.size(), 10);
		assertTrue(bus.event == BusEvent.boarded);
		assertTrue(bus.state == BusState.pickingUpPassengers);

		//Scheduler
		bus.pickAndExecuteAnAction();

		//Postconditions
		assertEquals(bus.passengers.size(), 10);
		assertTrue(bus.state == BusState.askingForFare);
		
		//Paying fare from first 9 passengers
		for(int i = 0; i < 9; i++) {
			bus.msgHereIsFare(people.get(i), 3.00);
		}
		assertEquals(bus.passengers.size(), 10);
		assertTrue(bus.state == BusState.askingForFare);
		assertTrue(bus.event == BusEvent.boarded);
		
		//Last passenger paying fare
		bus.msgHereIsFare(people.get(9), 3.00);
		assertEquals(bus.passengers.size(), 10);
		assertTrue(bus.state == BusState.askingForFare);
		assertTrue(bus.event == BusEvent.everyonePaid);
		
		//Scheduler
		bus.pickAndExecuteAnAction();

		//Postconditions
		assertEquals(bus.passengers.size(), 10);
		assertTrue(bus.event == BusEvent.arrivedAtStop);
		assertTrue(bus.state == BusState.driving);

		//Scheduler
		bus.pickAndExecuteAnAction();
		
		//Postconditions
		assertTrue(bus.currentStop == 1);
		assertEquals(bus.passengers.size(), 10);
		assertTrue(bus.event == BusEvent.arrivedAtStop);
		assertTrue(bus.state == BusState.atStop);
		
		//First 5 passengers want to get off
		for(int i = 0; i < 5; i++) {
			bus.msgImGettingOff(people.get(i));
		}
		
		try { //Wait for bus to complete its time waiting at stop
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 0; i < 6; i++) //Call scheduler 6 times - 5 people getting off + 1 state change
			bus.pickAndExecuteAnAction();

		//Postconditions
		assertTrue(bus.currentStop == 1);
		assertEquals(bus.passengers.size(), 5);
		assertTrue(bus.event == BusEvent.pickingUpPassengers);
		assertTrue(bus.state == BusState.pickingUpPassengers);
		
		//Message
		bus.msgPeopleBoarding(null);

		//Postconditions
		assertEquals(bus.passengers.size(), 5);
		assertTrue(bus.event == BusEvent.boarded);
		assertTrue(bus.state == BusState.pickingUpPassengers);

		//Scheduler
		bus.pickAndExecuteAnAction();

		//Postconditions
		assertEquals(bus.passengers.size(), 5);
		assertTrue(bus.state == BusState.askingForFare);
		assertTrue(bus.event == BusEvent.everyonePaid);
		
		//Scheduler
		bus.pickAndExecuteAnAction();
		
		//Postconditions
		assertEquals(bus.passengers.size(), 5);
		assertTrue(bus.event == BusEvent.arrivedAtStop);
		assertTrue(bus.state == BusState.driving);
		
		//Scheduler
		bus.pickAndExecuteAnAction();
		
		//Postconditions
		assertTrue(bus.state == BusState.atStop);
		assertTrue(bus.event == BusEvent.arrivedAtStop);
		
		//First 5 passengers want to get off
		for(int i = 5; i < 10; i++) {
			bus.msgImGettingOff(people.get(i));
		}
		try { //Wait for bus to complete its time waiting at stop
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(int i = 0; i < 6; i++) //Call scheduler 6 times - 5 people getting off + 1 state change
			bus.pickAndExecuteAnAction();

		//Postconditions
		assertTrue(bus.currentStop == 2);
		assertEquals(bus.passengers.size(), 0);
		assertTrue(bus.event == BusEvent.pickingUpPassengers);
		assertTrue(bus.state == BusState.pickingUpPassengers);
		
		//Everyone has unloaded and bus is ready to pick up new passengers!
	}
}
