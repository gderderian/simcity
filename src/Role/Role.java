package Role;

import agent.StringUtil;
import city.PersonAgent;
import city.gui.Gui;

public abstract class Role {
	
	//Is this state change necessary if the role has no thread  ?

	public boolean isActive;
	public boolean inUse;
	
	protected String building;
	protected Gui gui;
	protected double money;

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
    
    public abstract String getRoleName();
    
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
    
    public void setActive(double m){
    	isActive = true;
    	money = m;
    }
    
    public void setInactive(){
    	isActive = false;
    	getPerson().setWallet(money);
    }
    
    public boolean isInUse(){
    	return inUse;
    }
    
    public boolean isActive() {
    	return isActive;
    }

	public void setBuilding(String location) {
		building = location;		
	}
    
    public abstract PersonAgent getPerson();
        
}
