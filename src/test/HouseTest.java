package test;

import java.util.*;

import junit.framework.TestCase;
import test.mock.MockPerson;
import Role.LandlordRole;
import city.Apartment;
import city.Food;
import city.House;


public class HouseTest extends TestCase {
	House house; //Don't need a mock for this because it is not threaded (not an "Agent")
	Apartment apartment; //Don't need a mock for this because it is not threaded
	LandlordRole landlord; //Don't need a mock for this because it is not threaded unless associated with a person agent
	MockPerson person1;
	MockPerson person2;
	
	@Override
	public void setUp() throws Exception{
		super.setUp();	
		
		house= new House();
		apartment= new Apartment();
		landlord= new LandlordRole();
		person1= new MockPerson("person1");
		person2= new MockPerson("person2");
		landlord.addTenant(person1);
		house.setOwner(person2);
	}	
	
	
	public void testOneRentCollection(){
		//Preconditions for part 1 of this test
		assertEquals(
				"Landlord should have 1 tenant. It doesn't.", landlord.tenants.size(), 1);
		assertEquals(
				"The tenant should have 0 outstanding payments due. It doesn't.", landlord.tenants.get(0).numOutstandingPayments, 0);
		assertEquals(
				"Landlord should have an empty event log before the Landlords's msgEndOfDay is called. Instead, the Landlord's event log reads: "
						+ landlord.log.toString(), 0, landlord.log.size()); 
		assertEquals(
				"Person1 should have an empty event log before the Landlord's scheduler is called for the first time. Instead, the Person1's event log reads: "
						+ person1.log.toString(), 0, person1.log.size());
		
		
		//Part 1, this tells the landlord it is time to collect rent from the tenants
		landlord.msgEndOfDay();
		landlord.pickAndExecuteAnAction(); //need to call this because landlord is not threaded, it won't happen on its own
		
		//Postconditions for part 1, preconditions for part 2
		assertTrue(
				"Landlord should have logged \"Recieved msgEndOfDay, all tenants now should have rent due\" but didn't. His log reads instead: "
						+ landlord.log.getLastLoggedEvent().toString(), landlord.log.containsString("Recieved msgEndOfDay, all tenants now should have rent due"));
		assertTrue(
				"Person1 should have logged \"Recieved msgRentDue from landlord, I owe $10.0.\" but didn't. His log reads instead: "
						+ person1.log.getLastLoggedEvent().toString(), person1.log.containsString("Recieved msgRentDue from landlord, I owe $10.0."));
		assertEquals(
				"The tenant should have 1 outstanding payment due. It doesn't.", landlord.tenants.get(0).numOutstandingPayments, 1);
	
		
		//Part 2, the tenant is paying the landlord the appropriate amount for rent
		landlord.msgHereIsMyRent(person1, 10.0);
		
		//Postconditions for part 2
		assertTrue(
				"Landlord should have logged \"Recieved msgHereIsMyRent from tenant, tenant should now have no outstanding payments due\" but didn't. His log reads instead: "
						+ landlord.log.getLastLoggedEvent().toString(), landlord.log.containsString("Recieved msgHereIsMyRent from tenant, tenant should now have no outstanding payments due"));
		assertEquals(
				"The tenant should have 0 outstanding payments due. It doesn't.", landlord.tenants.get(0).numOutstandingPayments, 0);
		
		
		//Scenario is finished, the scheduler should now return false
		assertFalse(
				"Landlord's scheduler should have returned false (no actions left to do), but didn't.", landlord.pickAndExecuteAnAction());
	}
	
	
	public void testTwoFixAppliance(){
		//Preconditions for part 1 of this test
		assertEquals(
				"Landlord should have 1 tenant. It doesn't.", landlord.tenants.size(), 1);
		assertEquals(
				"The tenant should have 0 broken appliances. It doesn't.", landlord.tenants.get(0).needsMaintenance.size(), 0);
		assertEquals(
				"Landlord should have an empty event log before the Landlords's msgFixAppliance is called. Instead, the Landlord's event log reads: "
						+ landlord.log.toString(), 0, landlord.log.size()); 
		assertEquals(
				"Person1 should have an empty event log before the Landlord's scheduler is called for the first time. Instead, the Person1's event log reads: "
						+ person1.log.toString(), 0, person1.log.size());
		
		
		//Part 1, alert the landlord an appliance broke
		landlord.msgFixAppliance(person1, "Oven");
		
		
		//Postconditions for part 1, preconditions for part 2
		assertTrue(
				"Landlord should have logged \"Recieved msgFixAppliance from tenant, tenant should now have Oven in needsMaintenance\" but didn't. His log reads instead: "
						+ landlord.log.getLastLoggedEvent().toString(), landlord.log.containsString("Recieved msgFixAppliance from tenant, tenant should now have Oven in needsMaintenance"));
		assertEquals(
				"Person1 should still have an empty event log before the Landlord's scheduler is called for the first time. Instead, the Person1's event log reads: "
						+ person1.log.toString(), 0, person1.log.size());
		assertEquals(
				"The tenant should have 1 broken appliance. It doesn't.", landlord.tenants.get(0).needsMaintenance.size(), 1);
		
		
		//Part 2, run the landlord's scheduler so it will fix the appliance
		landlord.pickAndExecuteAnAction();
		
		//Postconditions for part 2
		assertTrue(
				"Person1 should have logged \"Recieved msgFixed from landlord, Oven is now fixed.\" but didn't. His log reads instead: "
						+ person1.log.getLastLoggedEvent().toString(), person1.log.containsString("Recieved msgFixed from landlord, Oven is now fixed."));
		assertEquals(
				"The tenant should have 0 broken appliances. It doesn't.", landlord.tenants.get(0).needsMaintenance.size(), 0);
		
		
		//Scenario is finished, the scheduler should now return false
		assertFalse(
				"Landlord's scheduler should have returned false (no actions left to do), but didn't.", landlord.pickAndExecuteAnAction());
	}
	
	
	//Includes putting away groceries because we have to make sure there is something to cook
	public void testThreeNormativeCooking(){
		//Preconditions for the test
		assertEquals(
				"House should have an empty event log before the House's boughtGroceries is called. Instead, the House's event log reads: "
						+ house.log.toString(), 0, house.log.size());
		assertEquals(
				"Person2 should have an empty event log before the House's boughtGroceries is called for the first time. Instead, Person2's event log reads: "
						+ person2.log.toString(), 0, person2.log.size());
		
		
		//Part 1, check if there is food in the fridge before attempting to cook anything
		house.checkFridge("Eggs");
		
		//Check postconditions for part 1 and preconditions for part 2
		assertTrue(
				"House should have logged \"Recieved checkFridge from person, checking if there is any Eggs in the fridge.\" but didn't. His log reads instead: "
					+ house.log.getLastLoggedEvent().toString(), house.log.containsString("Recieved checkFridge from person, checking if there is any Eggs in the fridge."));
		assertTrue(
				"Person2 should have logged \"Recieved msgDontHaveItem from house, I dont have any Eggs in my fridge.\" but didn't. His log reads instead: "
					+ person2.log.getLastLoggedEvent().toString(), person2.log.containsString("Recieved msgDontHaveItem from house, I dont have any Eggs in my fridge."));
					
		
		//Part 2, put food in the fridge		
		List<Food> groceries= new ArrayList<Food>();
		Food food= new Food("Eggs", "Stove", 0);
		groceries.add(food);
		house.boughtGroceries(groceries);
		
		//Check postconditions for part 2 and preconditions for part 3
		assertEquals(
				"House should have 1 item in the fridge. It doesn't.", house.fridge.currentAmount, 1);
		assertTrue(
				"House should have logged \"Received boughtGroceries from person, fridge should now have 1 items.\", but didn't. His log reads instead: "
						+ house.log.getLastLoggedEvent().toString(), house.log.containsString("Received boughtGroceries from person, fridge should now have 1 items."));
		assertTrue(
				"Person2 should not have logged anything new, should still have \"Recieved msgDontHaveItem from house, I dont have any Eggs in my fridge.\" but didn't. His log reads instead: "
					+ person2.log.getLastLoggedEvent().toString(), person2.log.containsString("Recieved msgDontHaveItem from house, I dont have any Eggs in my fridge."));
		
		
		//Part 4, check again that there is food in the fridge, now there should be
		house.checkFridge("Eggs");
		
		//Check postconditions for part 1 and preconditions for part 2
		assertTrue(
				"House should have logged \"Recieved checkFridge from person, checking if there is any Eggs in the fridge.\" but didn't. His log reads instead: "
					+ house.log.getLastLoggedEvent().toString(), house.log.containsString("Recieved checkFridge from person, checking if there is any Eggs in the fridge."));
		assertTrue(
				"Person2 should have logged \"Recieved msgItemInStock from house, I have at least one Eggs in my fridge.\" but didn't. His log reads instead: "
					+ person2.log.getLastLoggedEvent().toString(), person2.log.containsString("Recieved msgItemInStock from house, I have at least one Eggs in my fridge."));
		
		
		//Part 3, attempt to cook food
		house.cookFood("Eggs");
		
		//Check postconditions for part 3
		assertTrue(
				"House should have logged \"Cooking Eggs.\", but didn't. His log reads instead: "
						+ house.log.getLastLoggedEvent().toString(), house.log.containsString("Cooking Eggs."));
		assertTrue(
				"Person2 should have logged \"Recieved msgFoodDone from house, Eggs is done cooking now.\", but didn't. His log reads instead: "
					+ person2.log.getLastLoggedEvent().toString(), person2.log.containsString("Recieved msgFoodDone from house, Eggs is done cooking now."));
	}
	
	
	public void testFourApplianceBroken(){
		
	}
	
	
	public void testFivePutAwayGroceries(){
		
	}
	
	
	public void testSixOutOfFood(){
		
	}
}
