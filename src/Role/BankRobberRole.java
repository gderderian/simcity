package Role;

import java.util.concurrent.Semaphore;

import test.mock.EventLog;
import city.PersonAgent;
import city.gui.Gui;
import city.gui.Bank.BankRobberRoleGui;

public class BankRobberRole extends Role {

	String roleName = "BankRobberRole";

    public enum state {arrived, waiting, inprogress, gotobankteller, leave, gotobankchamber};
    public int bankaccountnumber;
    state bankrobberstate;
    BankTellerRole mybankteller;
    double deposit;
    double withdrawal;
    double loan;
    double paybackloan;
    public double amountofcustomermoney;
    int stationnumber;
    //public int customeraccountnumber;
    public Semaphore atBankChamber = new Semaphore(0,true);
    public Semaphore atBankLobby = new Semaphore(0,true);
    
    public BankRobberRoleGui gui;
    PersonAgent person;
    String name;
    BankManagerRole bankmanager;
    public EventLog log = new EventLog();
    
    
    public BankRobberRole(double setamountofcustomermoney)
    {
    		building = "bank1";
            bankrobberstate = state.arrived;
            this.amountofcustomermoney = setamountofcustomermoney;
            bankaccountnumber = 0;
    
    } 
    
    public void msgAssignMeBankTeller(BankTellerRole assignbankteller)
    {
 
            mybankteller = assignbankteller;
            stationnumber = mybankteller.stationnumber;
            bankrobberstate = state.gotobankteller; 
            person.stateChanged();      
    }
    public void msgGoToBankChamber()
    {
    		bankrobberstate = state.gotobankchamber;
    		person.stateChanged();
    	 	
    }
    public void msgHereIsYourMoney(double stolenmoney)
    {
            
            amountofcustomermoney += stolenmoney;
    		bankrobberstate = state.leave;
            person.stateChanged();
            
    }
    
    

    public boolean pickAndExecuteAnAction() 
    {
    
    		if(bankrobberstate == state.gotobankchamber)
    		{
    			
    				Do("I'm going to bank chamber station");
    				guiGoToBankChamber();
    				bankrobberstate = state.inprogress;			
    				return true;
    		}
    	
    	
            if(bankrobberstate == state.leave)
            {
            		Do("i'm opening an account");
                    mybankteller.msgOpenAccount();
                    bankrobberstate = state.waiting;
                    guiLeaveBank();
                    return true;
            }
            
           
            
            return false;
    }


    protected void Do(String string) {
		// TODO Auto-generated method stub
		
	}

	public Gui getGui() {
       
    	return this.gui;
    	
    }
    
    public void setGui(BankRobberRoleGui setGui)
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
    
    public void guiGoToBankChamber()
	{
		gui.goToBankChamber();
    	try {
			atBankChamber.acquire();
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

	@Override
	public PersonAgent getPerson() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
}
