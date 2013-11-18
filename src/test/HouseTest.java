package test;

import Role.HomeOwnerRole;
import Role.LandlordRole;
import city.Apartment;
import city.HouseAgent;
import city.PersonAgent;
import junit.framework.TestCase;


public class HouseTest extends TestCase {
	HouseAgent house;
	Apartment apartment;
	LandlordRole landlord;
	HomeOwnerRole homeowner;
	PersonAgent person1;
	PersonAgent person2;
	
	public void setUp() throws Exception{
		super.setUp();	
		
		house= new HouseAgent();
		apartment= new Apartment();
		person1= new PersonAgent("Landlord");
		person2= new PersonAgent("Homeowner");
		person1.addRole(landlord);
		person2.addRole(homeowner);
	}	
	
	public void testOneRentCollection(){
		
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
