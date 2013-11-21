package hollytesting.test;

import test.mock.EventLog;
import city.PersonAgent;
import city.Restaurant2.Restaurant2CookRole;
import city.Restaurant2.Restaurant2CustomerRole;
import city.Restaurant2.Restaurant2HostRole;
import city.Restaurant2.Restaurant2WaiterRole;
import city.gui.PersonGui;
import junit.framework.TestCase;

public class PersonAgentRestaurantTest extends TestCase{

	PersonAgent person;
	Restaurant2HostRole host;
	Restaurant2CustomerRole customer;
	Restaurant2WaiterRole waiter;
	Restaurant2CookRole cook;
	public EventLog log;
	PersonGui gui;
	
	public void setUp() throws Exception{
		super.setUp();		
		person = new PersonAgent("Person");
		host = new Restaurant2HostRole("Host");	
		customer = new Restaurant2CustomerRole("Customer");		
		waiter = new Restaurant2WaiterRole("Waiter");
		cook = new Restaurant2CookRole("Cook");
		gui = new PersonGui(person);
	}
	
	public void testPersonEnteringRestaurant(){
		
		person.setGui(gui);
		assertEquals("Person should zero roles in the Roles list, but it doesn't", person.roles.size(), 0);
		
		//Set the person to hungry so that they try to eat
		person.msgImHungry();
		
		assertTrue("Person should have logged an event that they recieved the 'I'm hungry' message, but it reads instead " +
				person.log.getLastLoggedEvent().toString(), person.log.containsString("Recieved message Im Hungry"));
		assertTrue("Person's scheduler should have returned true to deal with the hungry message, but it didn't", person.pickAndExecuteAnAction());
		assertTrue("Person should have logged an event that they are going to a restaurant, but instead it reads " + 
				person.log.getLastLoggedEvent().toString(), person.log.containsString("Decided to go to a restaurant"));
		
		
	}
	
}
