package test;

import Role.HomeOwnerRole;
import Role.LandlordRole;
import city.Apartment;
import city.House;
import city.PersonAgent;
import junit.framework.TestCase;


public class HouseTest extends TestCase {
	//Dont need a mock for this because it is not threaded
	House house;
	//Dont need a mock for this because it is not threaded
	Apartment apartment;
	//Dont need a mock for this because it is not threaded unless associated with a person agent
	LandlordRole landlord;
	//MockPerson person1;
	
	//PersonAgent person1;
	PersonAgent person2;
	
	public void setUp() throws Exception{
		super.setUp();	
		
		house= new House();
		apartment= new Apartment();
		landlord= new LandlordRole();
		person2= new PersonAgent("Homeowner", null);
	}	
	
	public void testOneRentCollection(){
		
		
		//This tells the landlord it is time to collect rent
		landlord.msgEndOfDay();
		
		
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
