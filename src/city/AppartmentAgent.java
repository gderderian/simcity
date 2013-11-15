package city;

public class AppartmentAgent extends HouseAgent{
	PersonAgent landlord;
	int apptNum;
	
	AppartmentAgent(PersonAgent p, int num){
		super(p);
		
		apptNum= num;
	}
}
