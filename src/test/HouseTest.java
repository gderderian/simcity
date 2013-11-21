package test;

import test.mock.MockPerson;
import Role.LandlordRole;
import city.Apartment;
import city.House;
import junit.framework.TestCase;


public class HouseTest extends TestCase {
	House house; //Don't need a mock for this because it is not threaded (not an "Agent")
	Apartment apartment; //Don't need a mock for this because it is not threaded
	LandlordRole landlord; //Don't need a mock for this because it is not threaded unless associated with a person agent
	MockPerson person1;
	
	public void setUp() throws Exception{
		super.setUp();	
		
		house= new House();
		apartment= new Apartment();
		landlord= new LandlordRole();
		person1= new MockPerson("person1");
		landlord.addTenant(person1);
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
				"MockPerson should have an empty event log before the Landlord's scheduler is called for the first time. Instead, the MockPerson's event log reads: "
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
		
	}
	
	public void testThreeNormativeCooking(){
		
	}
	
	public void testFourApplianceFull(){
		
	}
	
	public void testFiveOutOfFood(){
		
	}
	
	public void testSixPutAwayGroceries(){
		
	}
}
