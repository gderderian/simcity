package city.transportation.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import city.PersonAgent;
import city.transportation.BusAgent;
import city.transportation.BusAgent.BusEvent;
import city.transportation.BusAgent.BusState;
import city.transportation.BusStopAgent;
import city.transportation.mock.MockBus;
import city.transportation.mock.MockBusStop;
import city.transportation.mock.MockTransportationPerson;

public class BusStopTest extends TestCase {
	//instantiated in setUp()
	List<MockTransportationPerson> people;
	BusStopAgent stop;
	MockBus bus;

	public void setUp() throws Exception {
		super.setUp();
		people = new ArrayList<MockTransportationPerson>();
		for(int i = 0; i < 5; i++) { //Adds 10 people to the list.
			people.add(new MockTransportationPerson("person" + Integer.toString(i)));
		}
		bus = new MockBus();
		
		stop = new BusStopAgent(0);
		stop.thisIsATest(); //Turns off activity log(used in city control panel)
		
	}	

	/* This tests a single bus checking in at one bus stop twice, picking up some people, and then trying to pick up more when the bus stop is empty */
	public void testBusDriving() {		
		//Preconditions
				assertTrue(stop.peopleWaiting.size() == 0);
				assertTrue(stop.buses.size() == 0);
				
				//5 people start waiting for bus
				for(int i = 0; i < 5; i++) {
					stop.msgWaitingForBus(people.get(i));
				}
				
				//Postconditions
				assertTrue(stop.peopleWaiting.size() == 5); //Now 5 people are waiting for bus
				assertTrue(stop.buses.size() == 0);
				
				//Scheduler
				stop.pickAndExecuteAnAction();
				
				//Nothing should have changed
				assertTrue(stop.peopleWaiting.size() == 5);
				assertTrue(stop.buses.size() == 0);
				
				//Message - bus can pick up 5 people
				stop.msgICanPickUp(bus, 5);
				assertTrue(stop.buses.size() == 1); //One bus at stop
				
				//Scheduler - should tell all 5 people that their bus has arrived
				stop.pickAndExecuteAnAction();
				
				//All 5 people should have gotten message from bus stop - confirm here
				for(int i = 0; i < 5; i++) {
					assertTrue(people.get(i).log.getLastLoggedEvent().getMessage() == "Got message: Bus is here");
				}
				
				//Make sure bus received list of passengers
				assertTrue(bus.log.getLastLoggedEvent().getMessage() == "Received list of boarding passengers from bus stop");

				//Postconditions
				assertTrue(stop.peopleWaiting.size() == 0);
				assertTrue(stop.buses.size() == 0);
				
				//Message - bus wants to pick up 5 more people, but bus stop is empty
				stop.msgICanPickUp(bus, 5);
				
				//Scheduler - should message bus that no people are boarding
				stop.pickAndExecuteAnAction();
				
				//Make sure bus receives correct message
				assertTrue(bus.log.getLastLoggedEvent().getMessage() == "No one boarding from this bus stop");
				
	}
}
