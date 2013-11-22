package hollytesting.test;

import hollytesting.test.mock.MockBusAgent;
import hollytesting.test.mock.MockBusStop;
import city.PersonAgent;
import city.PersonAgent.BusRideState;
import city.Restaurant2.Restaurant2CookRole;
import city.Restaurant2.Restaurant2CustomerRole;
import city.Restaurant2.Restaurant2HostRole;
import city.Restaurant2.Restaurant2WaiterRole;
import city.gui.PersonGui;
import city.transportation.BusAgent;
import city.transportation.BusStopAgent;
import junit.framework.TestCase;

public class PersonAgentTransportationtTest extends TestCase {
	
	PersonAgent person;
	MockBusAgent bus;
	MockBusStop busStop;
	
	public void setUp() throws Exception{
		super.setUp();		
		person = new PersonAgent("Person");
		bus = new MockBusAgent("Bus");
		busStop = new MockBusStop("BusStop", person, bus);
	}
	
	public void testGettingOnBusNormal(){
		
		//Checking preconditions
		assertEquals("Person's list of bus rides shouldn't have anything in it", person.busRides.size(), 0);
		busStop.msgWaitingForBus(person);
		assertTrue("Bus stop should have record of recieving person waiting message.", 
				busStop.log.containsString("Recieved message waiting for bus from person Person"));
		assertTrue("Person should have record of recieving message bus is here", person.log.containsString("Recieved message bus is here"));
		assertEquals("Person's list of busRides should have one BusRide in it.", person.busRides.size(), 1);
		//Hack -- full bus capabilities not yet running in person
		person.busRides.get(0).busStop = 1;
		person.pickAndExecuteAnAction();
		assertTrue("Person should have a bus ride in it with the state onBus, but the state is " + person.busRides.get(0).state, 
				person.busRides.get(0).state == BusRideState.onBus);
		assertTrue("Bus should have record of getting the list of people boarding the bus.", bus.log.containsString("Recieved list of people boarding the bus."));
		person.msgPleasePayFare(bus, 3.00);
		assertTrue("Person should have a BusRide with a fare of 3.00.", person.busRides.get(0).fare == 3.00);
		person.pickAndExecuteAnAction();
		assertTrue("Person should have a BusRide with a fare of 0, but instead the fare is " + person.busRides.get(0).fare, 
				person.busRides.get(0).fare == 0);
		person.msgArrivedAtStop(1);
		assertTrue("Person should have a BusRide with state getOffBus", person.busRides.get(0).state == BusRideState.getOffBus);
		assertEquals("There should still only be one bus ride in the list.", person.busRides.size(), 1);
		person.pickAndExecuteAnAction();
		assertEquals("Person should have no more bus rides in their list.", person.busRides.size(), 0);
		
	}
	
	
}
