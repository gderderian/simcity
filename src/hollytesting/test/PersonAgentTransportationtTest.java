package hollytesting.test;

import city.PersonAgent;
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
	BusAgent bus;
	BusStopAgent busStop;
	
	public void setUp() throws Exception{
		super.setUp();		
		person = new PersonAgent("Person");
		bus = new BusAgent();
		busStop = new BusStopAgent();
	}
	
	public void testGettingOnBusNormal(){
		
		//Checking preconditions
		assertEquals("Person's list of bus rides shouldn't have anything in it", person.busRides.size(), 0);
		busStop.msgWaitingForBus(person);
		assertTrue("Person should have record of recieving message bus is here", person.log.containsString("Recieved message bus is here"));
		person.pickAndExecuteAnAction();
		
	}
	
	
}
