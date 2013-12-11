package Role;

import java.util.concurrent.Semaphore;

import activityLog.ActivityLog;
import activityLog.ActivityTag;
import city.MarketOrder;
import city.PersonAgent;
import city.gui.Market.MarketCustomerGui;
import city.gui.Restaurant3.CookGui3;
import Role.Role;

public class MarketCustomerRole extends Role {
	
	String roleName = "MarketCustomerRole";

	private Semaphore isAnimating = new Semaphore(0,true);
	private MarketCustomerGui mktCustGui;
	private String name;
	ActivityTag tag = ActivityTag.MARKETCUSTOMER;
	
	// Constructor
	public MarketCustomerRole(String name, PersonAgent p){
		person = p;
	}
	
	// Data
	PersonAgent person; // GUI and entrance/exit purposes
	
	// Messages
	public void msgHereIsYourOrder(MarketOrder o){
		// Handled within PersonAgent directly
		person.stateChanged();
	}
	
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
	
	private void log(String msg){
		print(msg);
        ActivityLog.getInstance().logActivity(tag, msg, name, false);
	}

	public PersonAgent getPerson() {
		return person;
	}

}