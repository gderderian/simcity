package city.transportation.test;

import junit.framework.TestCase;
import city.transportation.CarAgent;
import city.transportation.CarAgent.CarEvent;
import city.transportation.CarAgent.CarState;
import city.transportation.mock.MockTransportationPerson;

public class CarTest extends TestCase {
	//instantiated in setUp()
	MockTransportationPerson owner;
	CarAgent car;
	String destination;

	public void setUp() throws Exception {
		super.setUp();
		owner = new MockTransportationPerson("owner");
		car = new CarAgent(null, null);
		car.thisIsATest(); //Disables activity log capability(used in city control panel)
		destination = "TEST DESTINATION";
	}	

	/* This tests a CarAgent in a scenario with a person driving their car from one place to another */
	public void testCarDriving() {		
		assertTrue(car.capacity == 1); //Checks that car has appropriate capacity
		
		//Preconditions
		assertTrue(car.event == CarEvent.none);
		assertTrue(car.state == CarState.parked);
		assertTrue(car.destination == null);
		assertTrue(car.owner == null);
		
		//Message
		car.msgPickMeUp(owner, null);
		
		//Postconditions
		assertTrue(car.event == CarEvent.drivingToOwner);
		
		//Scheduler
		car.pickAndExecuteAnAction();		
		car.pickAndExecuteAnAction();
		
		//Postconditions
		assertTrue(car.event == CarEvent.arrivingAtOwner);
		assertTrue(owner.log.getLastLoggedEvent().getMessage().equals("Got message: Car is picking me up")); //Owner received correct message
		assertTrue(car.state == CarState.atOwner);
		
		//Message
		car.msgDriveTo(owner, destination);
		
		//Postconditions
		assertTrue(car.event == CarEvent.drivingToDestination);
		assertTrue(car.state == CarState.atOwner);
		assertTrue(car.destination == destination);
		assertTrue(car.owner == owner);
		
		//Run scheduler
		car.pickAndExecuteAnAction();
		
		//Postconditions
		assertTrue(car.event == CarEvent.arrivingAtDestination);
		assertTrue(car.state == CarState.driving);
		
		//Run scheduler
		car.pickAndExecuteAnAction();
		
		//Postconditions
		assertTrue(car.state == CarState.arrived);
		assertTrue(owner.log.containsString("Got message: Car arrived")); //Confirm owner was sent the correct message
		
		//Message
		car.msgParkCar(owner);

		//Postconditions
		assertTrue(car.event == CarEvent.parking);
		assertTrue(car.state == CarState.arrived);
		assertTrue(car.destination == null); //Reset car's destination to null

		//Run scheduler
		car.pickAndExecuteAnAction();
		
		//Posconditions
		assertTrue(car.event == CarEvent.none);
		assertTrue(car.state == CarState.parked);
		assertTrue(car.destination == null);
		assertTrue(car.owner == owner);
	}
	
}