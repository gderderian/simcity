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

public class BusStopTest2 extends TestCase {
	//instantiated in setUp()
	List<MockTransportationPerson> people;
	BusStopAgent stop;
	MockBus bus1;
	MockBus bus2;
	MockBus bus3;

	public void setUp() throws Exception {
		super.setUp();
		people = new ArrayList<MockTransportationPerson>();
		for(int i = 0; i < 10; i++) { //Adds 10 people to the list.
			people.add(new MockTransportationPerson("person" + Integer.toString(i)));
		}
		bus1 = new MockBus();
		bus2 = new MockBus();
		bus3 = new MockBus();
		
		stop = new BusStopAgent(0);
		stop.thisIsATest(); //Turns off activity log(used in city control panel)
		
	}	

	/* This tests a BusStopAgent with three buses checking in, all picking up different numbers of people */
	public void testBusDriving() {		
		
		//Preconditions
		assertTrue(stop.peopleWaiting.size() == 0);
		assertTrue(stop.buses.size() == 0); //0 buses at stop
		
		//10 people start waiting for bus
		for(int i = 0; i < 10; i++) {
			stop.msgWaitingForBus(people.get(i));
		}
		
		//Postconditions
		assertTrue(stop.peopleWaiting.size() == 10);
		assertTrue(stop.buses.size() == 0); //0 buses
		
		//Scheduler
		stop.pickAndExecuteAnAction();
		
		//Nothing should have changed
		assertTrue(stop.peopleWaiting.size() == 10);
		assertTrue(stop.buses.size() == 0); //No buses at stop yet
		
		//Message - bus can pick up 4 people
		stop.msgICanPickUp(bus1, 4);
		assertTrue(stop.buses.size() == 1); //One bus at stop
		
		//Scheduler - should tell first 4 people that their bus has arrived
		stop.pickAndExecuteAnAction();
		
		//First 4 people should have gotten message from bus - confirm here
		for(int i = 0; i < 4; i++) {
			assertTrue(people.get(i).log.getLastLoggedEvent().getMessage() == "Got message: Bus is here");
		}
		//Other 6 should not have gotten message - confirm here
		for(int i = 4; i < 10; i++) {
			assertTrue(people.get(i).log.size() == 0); //No messages for these 6
		}
		
		//Also, bus1 should have gotten message with list of passengers
		assertTrue(bus1.log.getLastLoggedEvent().getMessage() == "Received list of boarding passengers from bus stop");

		//Postconditions
		assertTrue(stop.peopleWaiting.size() == 6);
		assertTrue(stop.buses.size() == 0); //No buses at stop
		
		//Message - bus can pick up 10 people (there are only 6 at stop)
		stop.msgICanPickUp(bus2, 10);
		assertTrue(stop.buses.size() == 1); //One bus at stop
		
		//Scheduler - should message remaining 6 people that their bus has arrived
		stop.pickAndExecuteAnAction();
		
		//Confirm messages were received
		for(int i = 4; i < 10; i++) {
			assertTrue(people.get(i).log.getLastLoggedEvent().getMessage() == "Got message: Bus is here");
		}
		
		//Also, bus2 should have gotten message with list of passengers
		assertTrue(bus2.log.getLastLoggedEvent().getMessage() == "Received list of boarding passengers from bus stop");

		//Postconditions
		assertTrue(stop.peopleWaiting.size() == 0); //No people left waiting at stop
		assertTrue(stop.buses.size() == 0);
		
		//10 more people start waiting for bus
		for(int i = 0; i < 10; i++) {
			stop.msgWaitingForBus(people.get(i));
		}
		
		//Messages from 2 buses
		stop.msgICanPickUp(bus3, 6);
		stop.msgICanPickUp(bus1, 5);
		assertTrue(stop.buses.size() == 2); //2 buses at stop
		
		//2 calls of scheduler to send passengers to 2 buses
		stop.pickAndExecuteAnAction();
		stop.pickAndExecuteAnAction();
		
		//Confirm buses received messages
		assertTrue(bus3.log.getLastLoggedEvent().getMessage() == "Received list of boarding passengers from bus stop");
		assertTrue(bus1.log.getLastLoggedEvent().getMessage() == "Received list of boarding passengers from bus stop");
		
		//Postconditions
		assertTrue(stop.peopleWaiting.size() == 0); //no one left waiting
		assertTrue(stop.buses.size() == 0);
		
		//Message from bus while stop is empty
		stop.msgICanPickUp(bus2, 4);
		assertTrue(stop.buses.size() == 1); //One bus at stop
		
		//Scheduler - should not send any passengers
		stop.pickAndExecuteAnAction();
		
		//Confirm that bus2 received no new passengers
		assertTrue(bus2.log.getLastLoggedEvent().getMessage() == "No one boarding from this bus stop");
		
		//Postconditions
		assertTrue(stop.peopleWaiting.size() == 0); //Still no people waiting
		assertTrue(stop.buses.size() == 0); //No buses at stop
	}
}
