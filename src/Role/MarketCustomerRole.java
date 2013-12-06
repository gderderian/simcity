package Role;

import city.PersonAgent;
import Role.Role;

public class MarketCustomerRole extends Role {
	
	String roleName = "MarketCustomerRole";

	// Constructor
	MarketCustomerRole(PersonAgent p){
		person = p;
	}
	
	// Data
	PersonAgent person; // GUI and entrance/exit purposes
	
	// Messages
	/*
	public void msgHereIsYourOrder(MarketOrder o){
	 
		// Handled within PersonAgent directly
		
	}
	*/
	
	// Scheduler
	public boolean pickAndExecuteAnAction() {
		return false;
	}

	@Override
	public String getRoleName() {
		return roleName;
	}
	
	// Actions
	// All relevant actions handled within master PersonAgent

}