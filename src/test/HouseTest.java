package test;

import java.util.*;

import junit.framework.TestCase;
import test.mock.MockPerson;
import city.Apartment;
import city.Food;
import city.House;


public class HouseTest extends TestCase {
	House house; //Don't need a mock for this because it is not threaded (not an "Agent")
	Apartment apartment;
	MockPerson person1;
	private Timer cook= new Timer();
	
	@Override
	public void setUp() throws Exception{
		super.setUp();	
		
		house= new House("House");
		apartment= new Apartment("Apartment", 1);
		person1= new MockPerson("person1");
		house.setOwner(person1);
		apartment.setOwner(person1);
	}	
	
	
	//Includes putting away groceries because we have to make sure there is something to cook
	public void testOneNormativeCooking(){
		//Preconditions for the test
		assertEquals(
				"House should have an empty event log before the House's boughtGroceries is called. Instead, the House's event log reads: "
						+ house.log.toString(), 0, house.log.size());
		assertEquals(
				"Person1 should have an empty event log before the House's boughtGroceries is called for the first time. Instead, Person1's event log reads: "
						+ person1.log.toString(), 0, person1.log.size());
		
		
		//Part 1, check if there is food in the fridge before attempting to cook anything
		house.checkFridge("Eggs");
		
		//Check postconditions for part 1 and preconditions for part 2
		assertTrue(
				"House should have logged \"Recieved checkFridge from person, checking if there is any Eggs in the fridge.\" but didn't. His log reads instead: "
					+ house.log.getLastLoggedEvent().toString(), house.log.containsString("Recieved checkFridge from person, checking if there is any Eggs in the fridge."));
		assertTrue(
				"Person1 should have logged \"Recieved msgDontHaveItem from house, I dont have any Eggs in my fridge.\" but didn't. His log reads instead: "
					+ person1.log.getLastLoggedEvent().toString(), person1.log.containsString("Recieved msgDontHaveItem from house, I dont have any Eggs in my fridge."));
					
		
		//Part 2, put food in the fridge		
		List<Food> groceries= new ArrayList<Food>();
		Food food= new Food("Eggs");
		groceries.add(food);
		house.boughtGroceries(groceries);
		
		//Check postconditions for part 2 and preconditions for part 3
		assertEquals(
				"House should have 1 item in the fridge. It doesn't.", house.fridge.currentAmount, 1);
		assertTrue(
				"House should have logged \"Received boughtGroceries from person, fridge should now have 1 items.\", but didn't. His log reads instead: "
						+ house.log.getLastLoggedEvent().toString(), house.log.containsString("Received boughtGroceries from person, fridge should now have 1 items."));
		assertTrue(
				"Person1 should not have logged anything new, should still have \"Recieved msgDontHaveItem from house, I dont have any Eggs in my fridge.\" but didn't. His log reads instead: "
					+ person1.log.getLastLoggedEvent().toString(), person1.log.containsString("Recieved msgDontHaveItem from house, I dont have any Eggs in my fridge."));
		
		
		//Part 4, check again that there is food in the fridge, now there should be
		house.checkFridge("Eggs");
		
		//Check postconditions for part 1 and preconditions for part 2
		assertTrue(
				"House should have logged \"Recieved checkFridge from person, checking if there is any Eggs in the fridge.\" but didn't. His log reads instead: "
					+ house.log.getLastLoggedEvent().toString(), house.log.containsString("Recieved checkFridge from person, checking if there is any Eggs in the fridge."));
		assertTrue(
				"Person1 should have logged \"Recieved msgItemInStock from house, I have at least one Eggs in my fridge.\" but didn't. His log reads instead: "
					+ person1.log.getLastLoggedEvent().toString(), person1.log.containsString("Recieved msgItemInStock from house, I have at least one Eggs in my fridge."));
		
		
		//Part 3, attempt to cook food
		house.cookFood("Eggs");
		
		//Check postconditions for part 3 after the cooking timer goes up
		cook.schedule(new TimerTask() {
			@Override public void run() {
				assertTrue(
						"House should have logged \"Cooking Eggs.\", but didn't. His log reads instead: "
								+ house.log.getLastLoggedEvent().toString(), house.log.containsString("Cooking Eggs."));
				assertTrue(
						"Person1 should have logged \"Recieved msgFoodDone from house, Eggs is done cooking now.\", but didn't. His log reads instead: "
								+ person1.log.getLastLoggedEvent().toString(), person1.log.containsString("Recieved msgFoodDone from house, Eggs is done cooking now."));
			
			}}, food.cookTime);
	}
	
	
	public void testTwoApplianceBroken(){
		//Preconditions for test
		assertEquals(
				"Apartment should have an empty event log before the House's boughtGroceries is called. Instead, the House's event log reads: "
						+ apartment.log.toString(), 0, apartment.log.size());
		assertEquals(
				"Person1 should have an empty event log before the House's boughtGroceries is called for the first time. Instead, Person1's event log reads: "
						+ person1.log.toString(), 0, person1.log.size());
		
		
		//Part 1, break appliance (has to be done manually because in runtime there is only a 1/10 chance it will break)
		apartment.cookingAppliances.get(0).isBroken= true;
		apartment.owner.msgImBroken("Microwave"); // Microwave is always the first in the list of appliances, its safe to hardcode this
		
		//Check postconditions for part 2
		assertTrue(
				"Person1 should have logged \"Recieved msgImBroken from house, Microwave is the broken appliance.\", but didn't. His log reads instead: "
						+ person1.log.getLastLoggedEvent().toString(), person1.log.containsString("Recieved msgImBroken from house, Microwave is the broken appliance."));
		assertTrue(
				"The Microwave should now be broken.", apartment.cookingAppliances.get(0).isBroken);
		
		
		//Part 2, have the owner alert the apartment that the appliance was fixed
		apartment.fixedAppliance("Microwave");
		
		//Check postconditions for part 2
		assertTrue(
				"Apartment should have logged \"Recieved fixedAppliance from person, Microwave is now fixed.\", but didn't. Instead its log reads: "
						+ apartment.log.getLastLoggedEvent().toString(), apartment.log.containsString("Recieved fixedAppliance from person, Microwave is now fixed."));
		assertFalse(
				"The Microwave should now be fixed.", apartment.cookingAppliances.get(0).isBroken);
	}
}
