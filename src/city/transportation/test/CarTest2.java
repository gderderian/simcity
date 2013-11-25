package city.transportation.test;

import test.mock.MockPerson;
import city.PersonAgent;
import city.transportation.CarAgent;
import city.transportation.CarAgent.CarEvent;
import city.transportation.CarAgent.CarState;
import city.transportation.mock.MockTransportationPerson;

import junit.framework.*;

public class CarTest2 extends TestCase {
	//instantiated in setUp()
	MockTransportationPerson owner;
	CarAgent car;
	String destination;
	String destination2;

	public void setUp() throws Exception {
		super.setUp();
		owner = new MockTransportationPerson("owner");
		car = new CarAgent(null);
		destination = "TEST DESTINATION";
		destination2 = "TEST DESTINATION #2";
	}	

	/* This tests a person driving their car to one destination, and then driving to a second destination */
	public void testCarDriving() {		
		assertTrue(car.capacity == 1); //Checks that car has appropriate capacity
		
		//Preconditions
		assertTrue(car.event == CarEvent.none);
		assertTrue(car.state == CarState.parked);
		assertTrue(car.destination == null);
		assertTrue(car.owner == null);
		
		//Message
		car.msgDriveTo(owner, destination);
		
		//Postconditions
		assertTrue(car.event == CarEvent.driving);
		assertTrue(car.state == CarState.parked);
		assertTrue(car.destination == destination);
		assertTrue(car.owner == owner);
		
		//Run scheduler
		car.pickAndExecuteAnAction();
		
		//Postconditions
		assertTrue(car.event == CarEvent.arriving);
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
		
		//Message
		car.msgDriveTo(owner, destination2);
		
		//Postconditions
		assertTrue(car.event == CarEvent.driving);
		assertTrue(car.state == CarState.parked);
		assertTrue(car.destination == destination2);
		assertTrue(car.owner == owner);
		
		//Run scheduler
		car.pickAndExecuteAnAction();
		
		//Postconditions
		assertTrue(car.event == CarEvent.arriving);
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