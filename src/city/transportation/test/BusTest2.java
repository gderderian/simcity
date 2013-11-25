package city.transportation.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import city.PersonAgent;
import city.transportation.BusAgent;
import city.transportation.BusAgent.BusEvent;
import city.transportation.BusAgent.BusState;
import city.transportation.mock.MockBusStop;
import city.transportation.mock.MockTransportationPerson;
import interfaces.Person;

public class BusTest2 extends TestCase {
	//instantiated in setUp()
	List<Person> people;
	BusAgent bus;
	
	MockBusStop stop1;
	MockBusStop stop2;
	MockBusStop stop3;
	MockBusStop stop4;

	public void setUp() throws Exception {
		super.setUp();
		people = new ArrayList<Person>();
		for(int i = 0; i < 10; i++) { //Adds 10 people to the list.
			people.add(new MockTransportationPerson("person" + Integer.toString(i)));
		}
		bus = new BusAgent(null);
		
		bus.money = 0; //Take away all of bus's money for testing purposes
		
		stop1 = new MockBusStop(0);
		stop2 = new MockBusStop(1);
		stop3 = new MockBusStop(2);
		stop4 = new MockBusStop(3);
		
		//Give bus reference to the 4 stops
		bus.busStops.add(stop1);
		bus.busStops.add(stop2);
		bus.busStops.add(stop3);
		bus.busStops.add(stop4);
	}	

	/* This is a simpler BusAgent test to make sure bus doesn't exceed its capacity of passengers */
	public void testBusDriving() {		
		
		//Preconditions
		assertTrue(bus.capacity == 10); //Making sure capacity is set correctly
		assertTrue(bus.currentStop == 3); //Starts out at 3rd stop so that bus can immediately go to stop #0
		assertEquals(bus.passengers.size(), 0); //So far, no passengers
		assertTrue(bus.event == BusEvent.none);
		assertTrue(bus.state == BusState.driving);
		
		//Scheduler
		bus.pickAndExecuteAnAction();
		bus.pickAndExecuteAnAction(); //stateChanged is called twice
		
		//Postconditions
		assertTrue(bus.event == BusEvent.arrivedAtStop);
		assertTrue(bus.state == BusState.atStop);
		assertTrue(bus.currentStop == 0); //Now at stop 0
		assertEquals(bus.passengers.size(), 0); //Still no passengers

		try { //Wait for bus to complete its time waiting at stop (~2.5 seconds)
			Thread.sleep(3000);
		} catch (InterruptedException e) {
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
		
		//Bus stop should now send a list of 10 people to board the bus
		
		//Postconditions
		assertEquals(bus.passengers.size(), 10); //Now, there should be 10 passengers in the bus
		assertTrue(bus.event == BusEvent.boarded);
		assertTrue(bus.state == BusState.pickingUpPassengers);

		//Scheduler
		bus.pickAndExecuteAnAction();

		//Postconditions
		assertEquals(bus.passengers.size(), 10);
		assertTrue(bus.state == BusState.askingForFare); //Now asking all passengers to pay fare
		
		//Check to make sure all passengers received the message to pay fare
		for(int i = 0; i < bus.passengers.size(); i++) { 
			MockTransportationPerson mtp = (MockTransportationPerson) bus.passengers.get(i).p;
			assertTrue(mtp.log.getLastLoggedEvent().getMessage() == "Got message: Please pay fare");
		}
		
		//No money paid yet
		assertTrue(bus.money == 0); 
		
		//Paying fare from first 9 passengers
		for(int i = 0; i < 9; i++) {
			bus.msgHereIsFare(people.get(i), 3.00);
		}
		assertEquals(bus.passengers.size(), 10);
		assertTrue(bus.state == BusState.askingForFare); //State/event should not change until everyone pays!
		assertTrue(bus.event == BusEvent.boarded);
		assertEquals(bus.money, 9 * 3.00); //money should now be 3.00 per person who paid
		
		//Last passenger paying fare - now event changes
		bus.msgHereIsFare(people.get(9), 3.00);
		assertEquals(bus.passengers.size(), 10);
		assertTrue(bus.state == BusState.askingForFare);
		assertTrue(bus.event == BusEvent.everyonePaid);
		assertEquals(bus.money, 10 * 3.00); //money should now be 3.00 for each of the 10 people
		
		//Scheduler
		bus.pickAndExecuteAnAction();

		//Postconditions
		assertEquals(bus.passengers.size(), 10);
		assertTrue(bus.event == BusEvent.arrivedAtStop);
		assertTrue(bus.state == BusState.driving);

		//Scheduler
		bus.pickAndExecuteAnAction();
		
		//Postconditions
		assertTrue(bus.currentStop == 1); //Now at stop #1
		assertEquals(bus.passengers.size(), 10);
		assertTrue(bus.event == BusEvent.arrivedAtStop);
		assertTrue(bus.state == BusState.atStop);
		
		//Bus should have messaged everyone that bus arrived at stop - confirm here!
		for(int i = 0; i < bus.passengers.size(); i++) { 
			MockTransportationPerson mtp = (MockTransportationPerson) bus.passengers.get(i).p;
			assertTrue(mtp.log.getLastLoggedEvent().getMessage() == "Got message: Arrived at stop");
		}
		
		try { //Wait for bus to complete its time waiting at stop
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bus.pickAndExecuteAnAction();

		//Postconditions
		assertTrue(bus.currentStop == 1); //Still at stop #1 - still have to pick up new passengers
		assertEquals(bus.passengers.size(), 10); //5 passengers left
		assertTrue(bus.event == BusEvent.pickingUpPassengers);
		assertTrue(bus.state == BusState.pickingUpPassengers);
		
		//Message
		bus.msgPeopleBoarding(null); //no new passengers ready to board

		//Postconditions
		assertEquals(bus.passengers.size(), 10);
		assertTrue(bus.event == BusEvent.boarded);
		assertTrue(bus.state == BusState.pickingUpPassengers);

		//Scheduler
		bus.pickAndExecuteAnAction();

		//Postconditions
		assertEquals(bus.passengers.size(), 10);
		assertTrue(bus.state == BusState.askingForFare);
		assertTrue(bus.event == BusEvent.everyonePaid);
		
		//Scheduler
		bus.pickAndExecuteAnAction(); //Asks for fare from all new passengers, but there are none
		
		//Postconditions
		assertEquals(bus.passengers.size(), 10); //Still 10 passengers
		assertTrue(bus.event == BusEvent.arrivedAtStop); //Bus arrives at next stop, ready to continue!
	}
}