package Role;

import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.gui.Market.MarketCustomerGui;
import city.gui.Restaurant3.CookGui3;
import Role.Role;

public class MarketCustomerRole extends Role {
	
	String roleName = "MarketCustomerRole";

	private Semaphore isAnimating = new Semaphore(0,true);
	private MarketCustomerGui mktCustGui;
	
	// Constructor
	public MarketCustomerRole(String name, PersonAgent p){
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
	
	public void releaseSemaphore(){
		isAnimating.release();
	}
	
	// Scheduler
	public boolean pickAndExecuteAnAction() {
		return false;
	}

	@Override
	public String getRoleName() {
		return roleName;
	}

	public void setGuiActive() {
		// TODO Auto-generated method stub
		
	}

	public void setGui(MarketCustomerGui gui) {
		mktCustGui = gui;
	}
	
	public MarketCustomerGui getGui() {
		return mktCustGui;
	}

	public void setPerson(PersonAgent p) {
		person = p;
	}
	
	// Actions
	// All relevant actions handled within master PersonAgent

}