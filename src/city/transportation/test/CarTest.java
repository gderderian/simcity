package city.transportation.test;

import city.PersonAgent;
import city.transportation.CarAgent;

import junit.framework.*;

public class CarTest extends TestCase {
	//instantiated in setUp()
	PersonAgent carOwner;
	CarAgent car;


	public void setUp() throws Exception {
		super.setUp();
		carOwner = new PersonAgent("owner", null);
		car = new CarAgent();
	}	

	/* This tests the cashier receiving a single check request from a waiter which is then paid off by a customer */
	public void testCarDriving() {		
		assertTrue(car.capacity == 1);
	}
}