package hollytesting.test;

import interfaces.House;
import interfaces.Landlord;
import hollytesting.test.mock.MockHouse;
import hollytesting.test.mock.MockLandlord;
import city.PersonAgent;
import city.PersonAgent.FoodState;
import junit.framework.TestCase;

public class PersonAgentHousingTest extends TestCase {
	
	PersonAgent person;
	MockHouse house;
	MockLandlord landlord;
	
	public void setUp() throws Exception{
		super.setUp();		
		person = new PersonAgent("Person");
		house = new MockHouse("House", person);
	}
	
	public void testGettingFoodNormal(){
		
		person.setHouse((House) house);
		//Check the fridge in the house for chicken
		assertEquals("Persons meals list shouldn't have any meals in it", person.meals.size(), 0);
		house.checkFridge("Chicken");
		assertTrue("The person should have a meal in it with the state initial", person.meals.get(0).state == FoodState.initial);
		person.pickAndExecuteAnAction();
		assertTrue("Person should have an event logged 'Cooking meal'", person.log.containsString("Cooking meal"));
		house.cookFood("Chicken");
		assertTrue("Person should have logged an event 'Recieved message food is done'", person.log.containsString("Recieved message food is done"));
		assertTrue("Person should have a meal with the state done", person.meals.get(0).state == FoodState.done);
		person.pickAndExecuteAnAction();
		assertTrue("Person should have logged an event 'Eating meal'", person.log.containsString("Eating meal"));
		
	}
	
	public void testPayRentNormal(){
		
		assertEquals("Person should have no bills in their billsToPay list", person.billsToPay.size(), 0);
		person.msgRentDue((Landlord) landlord, 10.00);
		assertEquals("Person should have one bill in their billsToPay list", person.billsToPay.size(), 1);
		person.pickAndExecuteAnAction();
		
		
	}
	
}
