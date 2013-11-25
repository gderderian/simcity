package Role;

import java.util.concurrent.Semaphore;

import restaurant1.Restaurant1CashierRole;
import restaurant1.Restaurant1CookRole;
import restaurant1.Restaurant1CustomerRole;
import restaurant1.Restaurant1HostRole;
import restaurant1.Restaurant1WaiterRole;
import agent.StringUtil;
import city.PersonAgent;
import city.Restaurant2.Restaurant2CashierRole;
import city.Restaurant2.Restaurant2CookRole;
import city.Restaurant2.Restaurant2CustomerRole;
import city.Restaurant2.Restaurant2HostRole;
import city.Restaurant2.Restaurant2WaiterRole;
import city.gui.CityGui;
import city.gui.Gui;
import city.gui.restaurant2.Restaurant2AnimationPanel;
import city.gui.restaurant2.Restaurant2CookGui;
import city.gui.restaurant2.Restaurant2CustomerGui;
import city.gui.restaurant2.Restaurant2WaiterGui;

public abstract class Role {
	
	//Is this state change necessary if the role has no thread  ?

	public boolean isActive;
	public boolean inUse;
	
	protected String building;
	protected Gui gui;

    protected Role() {
    	isActive = false;
    	inUse = false;
    }
    
    
    /**
     * Agents must implement this scheduler to perform any actions appropriate for the
     * current state.  Will be called whenever a state change has occurred,
     * and will be called repeated as long as it returns true.
     *
     * @return true iff some action was executed that might have changed the
     *         state.
     */
    public abstract boolean pickAndExecuteAnAction();

    /**
     * Return agent name for messages.  Default is to return java instance
     * name.
     */
    protected String getName() {
        return StringUtil.shortName(this);
    }
    
    public String getBuilding() {
    	return building;
    }
    
    public Gui getGui(){
    	return gui;
    }

    /**
     * The simulated action code
     */
    protected void Do(String msg) {
        print(msg, null);
    }

    /**
     * Print message
     */
    protected void print(String msg) {
        print(msg, null);
    }

    /**
     * Print message with exception stack trace
     */
    protected void print(String msg, Throwable e) {
        StringBuffer sb = new StringBuffer();
        sb.append(getName());
        sb.append(": ");
        sb.append(msg);
        sb.append("\n");
        if (e != null) {
            sb.append(StringUtil.stackTraceString(e));
        }
        System.out.print(sb.toString());
    }
    
    /**
     * This function sets the role to active or not for use with the PersonAgent's scheduler
     */
    
    public void setActive(){
    	isActive = true;
    }
    
    public void setInactive(){
    	isActive = false;
    }
    
    public boolean isInUse(){
    	return inUse;
    }
    
	public static Role getNewRole(String type, PersonAgent p, CityGui cityGui){
		if(type.equals("Restaurant2 Waiter")){
			//Creates role, gui for role
			//Adds role to gui and gui to role
			//Adds role to correct animation panel
			Restaurant2WaiterRole role = new Restaurant2WaiterRole(p.getName(), p);
			Restaurant2WaiterGui gui = new Restaurant2WaiterGui(role, p.getName(), cityGui, 1);
			role.setGui(gui);
			return role;
		}
		else if(type.equals("Restaurant2 Host")){
			Restaurant2HostRole role = new Restaurant2HostRole(p.getName(), p);
			return role;
		}
		else if(type.equals("Restaurant2 Cook")){
			Restaurant2CookRole role = new Restaurant2CookRole(p.getName(), p);
			Restaurant2CookGui gui = new Restaurant2CookGui(role);
			role.setGui(gui);
			return role;
		}
		else if(type.equals("Restaurant2 Cashier")){
			Restaurant2CashierRole role = new Restaurant2CashierRole(p.getName(), p);
			return role;
		}
		else if(type.equals("Restaurant2 Customer")){
			Restaurant2CustomerRole role = new Restaurant2CustomerRole(p.getName(), p);
			//Restaurant2CustomerGui gui = new Restaurant2CustomerGui(role, p.getName(), 1);
			return role;
		}
		//else if(type.equals("Bank Manager")) return new BankManagerRole();
		else if(type.equals("Restaurant1 Customer")){
			Restaurant1CustomerRole role = new Restaurant1CustomerRole(p.getName(), p);
			return role;
		}
		else if(type.equals("Restaurant1 Waiter")){
			Restaurant1WaiterRole role = new Restaurant1WaiterRole(p.getName(), p);
			return role;
		}
		else if(type.equals("Restaurant1 Cook")){
			Restaurant1CookRole role = new Restaurant1CookRole(p.getName(), p);
			return role;
		}
		else if(type.equals("Restaurant1 Host")){
			Restaurant1HostRole role = new Restaurant1HostRole(p.getName(), p);
			return role;
		}
		else if(type.equals("Restaurant1 Cashier")){
			Restaurant1CashierRole role = new Restaurant1CashierRole(p.getName(), p);
			return role;
		}
		else return null;
	}
    
}
