package test;



import java.util.Timer;
import java.util.TimerTask;

import junit.framework.TestCase;
import test.mock.MockPerson;
import Role.LandlordRole;
import city.Apartment;
import city.PersonAgent;


public class LandlordTest extends TestCase {
	Apartment apartment; //Don't need a mock for this because it is not threaded
	Apartment apartment2;
	LandlordRole landlord;
	PersonAgent person;
	MockPerson person1;
	MockPerson person2;
	Timer fixAppliance= new Timer();
	
	@Override
	public void setUp() throws Exception{
		super.setUp();	
		
		person= new PersonAgent("person");
		apartment= new Apartment("Apartment2", 1);
		apartment2= new Apartment("Apartment1", 2);
		landlord= new LandlordRole("Landlord", person);
		person1= new MockPerson("person1");
		person2= new MockPerson("person2");
		landlord.test(true);
		landlord.addTenant(person1);
		apartment.setOwner(person1);
		apartment2.setOwner(person2);
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
		landlord.msgCollectRent();
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
		
		
		//Part 2, run the landlord's scheduler so it will fix the appliance and run the rest of the conditions after the timer to account for the timer in landlordRole
		fixAppliance.schedule(new TimerTask() {
			@Override public void run() {
					//Postconditions for part 2 to be checked after the landlord's fix timer goes off
				assertTrue(
						"Person1 should have logged \"Recieved msgFixed from landlord, Oven is now fixed.\" but didn't. His log reads instead: "
								+ person1.log.getLastLoggedEvent().toString(), person1.log.containsString("Recieved msgFixed from landlord, Oven is now fixed."));
				assertEquals(
						"The tenant should have 0 broken appliances. It doesn't.", landlord.tenants.get(0).needsMaintenance.size(), 0);

		
				//Scenario is finished, the scheduler should now return false
				assertFalse(
						"Landlord's scheduler should have returned false (no actions left to do), but didn't.", landlord.pickAndExecuteAnAction());
			
			}}, 4000);
		

	}
	
	
	//The landlord will have to collect rent from multiple tenants and fix one of their appliances
	public void testThreeMultipleTenants(){
		//Set up the test by adding a second tenant
		landlord.addTenant(person2);
		
		//Preconditions for test
		assertEquals(
				"Landlord should have 2 tenants. It doesn't.", landlord.tenants.size(), 2);
		assertEquals(
				"The first tenant should have 0 broken appliances. It doesn't.", landlord.tenants.get(0).needsMaintenance.size(), 0);
		assertEquals(
				"The second tenant should have 0 broken appliances. It doesn't.", landlord.tenants.get(1).needsMaintenance.size(), 0);
		assertEquals(
				"The first tenant should have 0 outstanding payments due. It doesn't.", landlord.tenants.get(0).numOutstandingPayments, 0);
		assertEquals(
				"The second tenant should have 0 outstanding payments due. It doesn't.", landlord.tenants.get(1).numOutstandingPayments, 0);
		assertEquals(
				"Landlord should have an empty event log before the Landlords's msgEndOfDay is called. Instead, the Landlord's event log reads: "
						+ landlord.log.toString(), 0, landlord.log.size()); 
		assertEquals(
				"The first tenant should have an empty event log before the Landlord's scheduler is called for the first time. Instead, the first tenant's event log reads: "
						+ person1.log.toString(), 0, person1.log.size());
		assertEquals(
				"The second tenant should have an empty event log before the Landlord's scheduler is called for the fisrt time. Instead, the second tenant's event log reads: "
						+ person2.log.toString(), 0, person2.log.size());
		
		
		//Part 1, alert the landlord rent is due for all tenants
		landlord.msgCollectRent();
		
		//Check postconditions for part 1 and preconditions for part 2
		assertTrue(
				"Landlord should have logged \"Recieved msgEndOfDay, all tenants now should have rent due\" but didn't. His log reads instead: "
						+ landlord.log.getLastLoggedEvent().toString(), landlord.log.containsString("Recieved msgEndOfDay, all tenants now should have rent due"));
		assertEquals(
				"The first tenant should have 1 outstanding payment due. It doesn't.", landlord.tenants.get(0).numOutstandingPayments, 1);
		assertEquals(
				"The second tenant should have 1 outstanding payment due. It doesn't.", landlord.tenants.get(1).numOutstandingPayments, 1);
		assertEquals(
				"The first tenant should still have an empty event log before the Landlord's scheduler is called for the first time. Instead, the first tenant's event log reads: "
						+ person1.log.toString(), 0, person1.log.size());
		assertEquals(
				"The second tenant should still have an empty event log before the Landlord's scheduler is called for the fisrt time. Instead, the second tenant's event log reads: "
						+ person2.log.toString(), 0, person2.log.size());
		
		
		//Part 2, alert the landlord that an appliance broke
		landlord.msgFixAppliance(person2, "Microwave");
		
		//Check postconditions for part 2 and preconditions for part 3
		assertTrue(
				"Landlord should have logged \"Recieved msgFixAppliance from tenant, tenant should now have Microwave in needsMaintenance\" but didn't. His log reads instead: "
						+ landlord.log.getLastLoggedEvent().toString(), landlord.log.containsString("Recieved msgFixAppliance from tenant, tenant should now have Microwave in needsMaintenance"));
		assertEquals(
				"The second tenant should have 1 broken appliance. It doesn't.", landlord.tenants.get(1).needsMaintenance.size(), 1);
		assertEquals(
				"The first tenant should still have an empty event log before the Landlord's scheduler is called for the first time. Instead, the first tenant's event log reads: "
						+ person1.log.toString(), 0, person1.log.size());
		assertEquals(
				"The second tenant should still have an empty event log before the Landlord's scheduler is called for the fisrt time. Instead, the second tenant's event log reads: "
						+ person2.log.toString(), 0, person2.log.size());
		
		
		//Part 3, call the landlord's scheduler so he can then alert the first tenant his rent is due
		landlord.pickAndExecuteAnAction();
		
		
		//Check postconditions for part 3 and preconditions for part 4
		assertTrue(
				"Person1 should have logged \"Recieved msgRentDue from landlord, I owe $10.0.\" but didn't. His log reads instead: "
						+ person1.log.getLastLoggedEvent().toString(), person1.log.containsString("Recieved msgRentDue from landlord, I owe $10.0."));
		assertEquals(
				"The first tenant should still have 1 outstanding payment due. It doesn't.", landlord.tenants.get(0).numOutstandingPayments, 1);
		
		
		//Part 4, call the landlord's scheduler again so he can alert the second tenant his rent is due
		landlord.pickAndExecuteAnAction();
		
		//Check postconditions for part 4 and preconditions for part 5	
		assertTrue(
				"Person2 should have logged \"Recieved msgRentDue from landlord, I owe $10.0.\" but didn't. His log reads instead: "
						+ person2.log.getLastLoggedEvent().toString(), person2.log.containsString("Recieved msgRentDue from landlord, I owe $10.0."));
		assertEquals(
				"The second tenant should still have 1 outstanding payment due. It doesn't.", landlord.tenants.get(1).numOutstandingPayments, 1);
		assertEquals(
				"The second tenant should still have 1 broken appliance. It doesn't.", landlord.tenants.get(1).needsMaintenance.size(), 1);
		
		
		//Part 5, the tenants pay their rent
		landlord.msgHereIsMyRent(person1, 10.00);
		landlord.msgHereIsMyRent(person2, 10.00);
		
		//Check postconditions for part 5 and preconditions for part 6
		assertTrue(
				"Landlord should have logged \"Recieved msgHereIsMyRent from tenant, tenant should now have no outstanding payments due\" but didn't. His log reads instead: "
						+ landlord.log.getLastLoggedEvent().toString(), landlord.log.containsString("Recieved msgHereIsMyRent from tenant, tenant should now have no outstanding payments due"));
		assertEquals(
				"The first tenant should have 0 outstanding payments due. It doesn't.", landlord.tenants.get(0).numOutstandingPayments, 0);
		assertEquals(
				"The second tenant should have 0 outstanding payments due. It doesn't.", landlord.tenants.get(1).numOutstandingPayments, 0);
		
		
		//Part 6, call the landlord's scheduler to fix the second tenant's appliance, check the rest of the conditions after the fix appliance timer
		landlord.pickAndExecuteAnAction();
		fixAppliance.schedule(new TimerTask() {
			@Override public void run() {	
				landlord.pickAndExecuteAnAction();
				//Check postconditions for part 6
				assertTrue(
						"Person2 should have logged \"Recieved msgFixed from landlord, Microwave is now fixed.\" but didn't. His log reads instead: "
								+ person2.log.getLastLoggedEvent().toString(), person2.log.containsString("Recieved msgFixed from landlord, Microwave is now fixed."));
				assertEquals(
						"The tenant should have 0 broken appliances. It doesn't.", landlord.tenants.get(1).needsMaintenance.size(), 0);
		
		
				//Scenario is finished, the scheduler should now return false
				assertFalse(
						"Landlord's scheduler should have returned false (no actions left to do), but didn't.", landlord.pickAndExecuteAnAction());
			
			}}, 4000);
		

	}
}
