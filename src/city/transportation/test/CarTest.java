package city.transportation.test;

import city.PersonAgent;
import city.transportation.CarAgent;
import city.transportation.CarAgent.CarEvent;
import city.transportation.CarAgent.CarState;

import junit.framework.*;

public class CarTest extends TestCase {
	//instantiated in setUp()
	PersonAgent carOwner;
	CarAgent car;
	String destination;

	public void setUp() throws Exception {
		super.setUp();
		carOwner = new PersonAgent("owner", null);
		car = new CarAgent();
		destination = "test destination";
	}	

	/* This tests the cashier receiving a single check request from a waiter which is then paid off by a customer */
	public void testCarDriving() {		
		assertTrue(car.capacity == 1); //Checks that car has appropriate capacity
		
		//Preconditions
		assertTrue(car.event == CarEvent.none);
		assertTrue(car.state == CarState.parked);
		assertTrue(car.destination == null);
		assertTrue(car.owner == null);
		
		//Message
		car.msgDriveTo(carOwner, destination);
		
		//Postconditions
		assertTrue(car.event == CarEvent.driving);
		assertTrue(car.state == CarState.parked);
		assertTrue(car.destination == destination);
		assertTrue(car.owner == carOwner);
		
		//Run scheduler
		car.pickAndExecuteAnAction();
		
		//Postconditions
		assertTrue(car.event == CarEvent.arriving);
		assertTrue(car.state == CarState.driving);
		
		//Run scheduler
		car.pickAndExecuteAnAction();
		
		//Postconditions
		assertTrue(car.state == CarState.arrived);
		
		//Message
		car.msgParkCar(carOwner);

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
		assertTrue(car.owner == carOwner);
	}
	
}