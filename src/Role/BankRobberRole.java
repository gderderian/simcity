package Role;

import java.util.concurrent.Semaphore;

import test.mock.EventLog;
import test.mock.LoggedEvent;
import Role.BankCustomerRole.state;
import city.PersonAgent;
import city.gui.Gui;
import city.gui.Bank.BankCustomerRoleGui;

public class BankRobberRole {

	String roleName = "BankCustomerRole";

    public enum state {arrived, waiting, inprogress, gotobankteller, leave};
    public int bankaccountnumber;
    state bankcustomerstate;
    BankTellerRole mybankteller;
    double deposit;
    double withdrawal;
    double loan;
    double paybackloan;
    public double amountofcustomermoney;
    int stationnumber;
    //public int customeraccountnumber;
    public Semaphore atBankStation = new Semaphore(0,true);
    public Semaphore atBankLobby = new Semaphore(0,true);
    
    public BankCustomerRoleGui gui;
    PersonAgent person;
    String name;
    BankManagerRole bankmanager;
    public EventLog log = new EventLog();
    
    
    public BankRobberRole(double setamountofcustomermoney)
    {
    		String building = "bank1";
            bankcustomerstate = state.arrived;
            this.amountofcustomermoney = setamountofcustomermoney;
            bankaccountnumber = 0;
    
    } 
    
    public void msgAssignMeBankTeller(BankTellerRole assignbankteller)
    {
 
            mybankteller = assignbankteller;
            stationnumber = mybankteller.stationnumber;
            bankcustomerstate = state.gotobankteller; 
            person.stateChanged();      
    }
    
    public void msgHereIsYourMoney(double stolenmoney)
    {
            
            amountofcustomermoney += stolenmoney;
    		bankcustomerstate = state.leave;
            person.stateChanged();
            
    }
    
    

    public boolean pickAndExecuteAnAction() 
    {
    
    	
    		Do("!!!!!!!!!!!! I'm in Robber customer scheduler !!!!!!!");
            
    		if(bankcustomerstate == state.gotobankteller)
    		{
    			
    				Do("I'm going to bank teller station");
    				guiGoToBankTellerStation(stationnumber);
    				bankcustomerstate = state.waiting;			
    				return true;
    		}
    	
    	
            if(bankcustomerstate == state.leave)
            {
            		Do("i'm opening an account");
                    mybankteller.msgOpenAccount();
                    bankcustomerstate = state.waiting;
                    guiLeaveBank();
                    return true;
            }
            
           
            
            return false;
    }


    private void Do(String string) {
		// TODO Auto-generated method stub
		
	}

	public Gui getGui() {
       
    	return this.gui;
    	
    }
    
    public void setGui(BankCustomerRoleGui setGui)
    {
    	this.gui = setGui;
    }
    
    public String getName() {
    	return this.name;
    }
    
    public void setPerson(PersonAgent person)
    {
    	this.person = person;
    	this.name = person.getName();
    	
    }
    
    public void setManager(BankManagerRole bankmanager)
    {
    	this.bankmanager = bankmanager;
    }
    
    public void guiGoToBankTellerStation(int stationnumber)
	{
		gui.goToBankTellerStation(stationnumber);
    	try {
			atBankStation.acquire();
			//atLobby.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
    
    public void guiLeaveBank()
    {
    	gui.leaveBank();
    	try {
			atBankLobby.acquire();
			//atLobby.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    

	public void setGuiActive() {
		//customerGui.DoEnterRestaurant();
		gui.setPresent(true);
	}

	public String getRoleName() {
		return roleName;
	}
	
	
	
	
	
	
}
