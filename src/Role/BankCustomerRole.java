package Role;

import java.util.Random;
import java.util.concurrent.Semaphore;

import activityLog.ActivityLog;
import activityLog.ActivityTag;
import city.gui.Gui;
import city.gui.Bank.BankCustomerRoleGui;
import test.mock.EventLog;
import test.mock.LoggedEvent;
import city.PersonAgent;

public class BankCustomerRole extends Role{
	
	String roleName = "BankCustomerRole";

        public enum state {arrived, waiting, inprogress, gotobankteller, atstation, openaccount, withdraw, deposit, leave, getloan, paybackloan, openaccountsuccessful, depositintoaccountsuccessful, withdrawfromaccountsuccessful, getloansuccessful, withdrawalfailed};
        public int bankaccountnumber;
        state bankcustomerstate;
        BankTellerRole mybankteller;
        double deposit;
        double withdrawal;
        double loan;
        double paybackloan;
        double failedwithdrawal;
        public double amountofcustomermoney;
        int stationnumber;
        boolean needloan;
        //public int customeraccountnumber;
        public Semaphore atBankStation = new Semaphore(0,true);
        public Semaphore atBankLobby = new Semaphore(0,true);
        
        public BankCustomerRoleGui gui;
        PersonAgent person;
        String name;
        BankManagerRole bankmanager;
        public EventLog log = new EventLog();
        
        ActivityTag tag = ActivityTag.BANKCUSTOMER;
        
        
        public BankCustomerRole(double setamountofcustomermoney/*, PersonAgent setperson*/)
        {
        		building = "bank1";
                bankcustomerstate = state.arrived;
                this.amountofcustomermoney = setamountofcustomermoney;
                //bankaccountnumber = 1;//this should be 0
                needloan = true;
                //this is for testing purpose
                //this.amountofcustomermoney = 40; // this should not be set to anything
                //this.person = setperson;
                //this.name = setperson.getName();
                //stateChanged();
        
        }
        
        public void msgGoToWaitingArea(int xc, int yc) {
        	
        	gui.setWaitingPosition(xc, yc);
        	
        }
        
        public void msgAssignMeBankTeller(BankTellerRole assignbankteller)
        {
     
                mybankteller = assignbankteller;
                stationnumber = mybankteller.stationnumber;
                bankcustomerstate = state.gotobankteller; 
                person.stateChanged();      
        }
        
        public void msgWithDrawFund(double withdrawal)
        {
                
                bankcustomerstate = state.withdraw;
                this.withdrawal = withdrawal;
                person.stateChanged();
                
        }
        
        public void msgOpenAccount()
        {
        	
        		//Do("msgOpenAccount");
                //log.add(new LoggedEvent("msgOpenAccount")
                bankcustomerstate = state.openaccount;
                person.stateChanged();
                
        }
        
        public void msgDepositIntoAccount(double deposit)
        {
                
                bankcustomerstate = state.deposit;
                this.deposit = deposit;
                person.stateChanged();
                
        }
        
        public void msgGetLoan(double loan)
        {
                
                bankcustomerstate = state.getloan;
                this.loan = loan;
                person.stateChanged();
                
        }
        
        public void msgPayLoan( double paybackloan)
        {
                
                bankcustomerstate = state.paybackloan;
                this.paybackloan = paybackloan;
                person.stateChanged();        
        }
        

        public void msgOpenAccountDone(int setcustomeraccountnumber) 
        {
                log.add(new LoggedEvent("msgOpenAccountDone"));
                log("Msg open account done");
                bankcustomerstate = state.openaccountsuccessful;
                this.bankaccountnumber = setcustomeraccountnumber;
                person.stateChanged();
                
        }
        
        public void msgDepositIntoAccountDone(double setdeposit) 
        {
                log.add(new LoggedEvent("msgDepositIntoAccountDone"));
                bankcustomerstate = state.depositintoaccountsuccessful;
                this.deposit = setdeposit;
                person.stateChanged();

        }

        public void msgHereIsYourWithdrawal(double setwithdrawal) 
        {
                log("I withdrew $" + setwithdrawal + " from my account");
        		log.add(new LoggedEvent("msgHereIsYourWithdrawal"));
                bankcustomerstate = state.withdrawfromaccountsuccessful;
                this.withdrawal = setwithdrawal;
                person.stateChanged();
                
        }
        
        public void msgHereIsYourLoan(double setloan)
        {
                bankcustomerstate = state.getloansuccessful;
                this.loan = setloan;
                person.stateChanged();
        }

        public void msgWithdrawalFailed(double failedwithdrawal) 
        {
                log("failed to withdraw amount " + failedwithdrawal);
        		bankcustomerstate = state.withdrawalfailed;
                this.failedwithdrawal = failedwithdrawal;
                person.stateChanged();
                
        }

        public void msgCannotGetLoan(double loan) 
        {
            log("Failed to get loan ");
            
            bankcustomerstate = state.leave;
            person.stateChanged();       	
        }

        public void msgLoanBorrowed(double loan) 
        {
        	//log.add(new LoggedEvent("msgLoanBorrowed"));  
        	 amountofcustomermoney = loan;
                
        }
        
        public void msgLoanPaidBack(double amountofloanpaidback, double amountofremainingloan)
        {
        	log("loan paid back");
        	gui.paybackloan = false;
        	log.add(new LoggedEvent("msgLoanPaidBack"));
        	bankcustomerstate = state.leave;
            person.stateChanged();      
                
        }
        
        public void msgLoanPaid(double loanamount, double lendtime,double interestrate) {
                Do("Successfully paid off loan of: " + loanamount + " lendtime: " + lendtime +" days" + " interestrate: " + interestrate);
                log.add(new LoggedEvent("msgLoanPaidBack"));
        }
        

        public boolean pickAndExecuteAnAction() 
        {
        
        	
        		//log("!!!!!!!!!!!! I'm in customer scheduler !!!!!!!");
                
        		log("state " + bankcustomerstate);
        		
        		 
        		log("amount of customer money $" + amountofcustomermoney);
        		
        		if(bankcustomerstate == state.gotobankteller)
        		{
        			
        			log("I'm going to bank teller station");
        			guiGoToBankTellerStation(stationnumber);
        			bankcustomerstate = state.atstation;
        			return true;
        		}
        		
        		if(bankcustomerstate == state.atstation && bankaccountnumber == 0)
        		{
        	
        				 mybankteller.msgOpenAccount();
                         bankcustomerstate = state.waiting;
                         gui.openaccount = true;
                         return true;
        		}
        		
        		if(bankcustomerstate == state.atstation && amountofcustomermoney >= 50)
        		{	
        	
        				mybankteller.msgDepositIntoAccount(amountofcustomermoney/2);
        				bankcustomerstate = state.waiting;
        				gui.deposit = true;
        				return true;
        		}     			
        			
        		
        		if(bankcustomerstate == state.atstation && amountofcustomermoney <= 50)
        		{
        				mybankteller.msgWithdrawFromAccount(amountofcustomermoney); 
        				bankcustomerstate = state.waiting;
        				gui.withdraw = true;
        				return true;   			
        		}
        		
        		if(bankcustomerstate == state.atstation && needloan == true)
        		{
        				double amountofloanrequested = amountofcustomermoney * 2;
        				mybankteller.msgGetLoan(amountofloanrequested);
        				bankcustomerstate = state.waiting;
        				gui.loan = true;
        				return true;   			
        		}
        		
        		
        		if(bankcustomerstate == state.withdrawalfailed)
        		{
        			Random r = new Random();
        			int i = r.nextInt(2); 
        			if(i == 0)
        			{
        				this.failedwithdrawal /= 2;
        				log("Since my request for withdrawal failed. I'm requesting $" + this.failedwithdrawal);
            			mybankteller.msgWithdrawFromAccount(amountofcustomermoney);
        				bankcustomerstate = state.waiting;
        				gui.withdraw = true;
        				return true;   		
        			
        			
        			}
        			else if(i == 1)
        			{
        				gui.withdraw = false;
        				gui.loan = true;
        				mybankteller.msgGetLoan(failedwithdrawal);
        				bankcustomerstate = state.waiting;
        				
        				return true;  	
        				
        			}
        				
        				
        		}
        		
        		/*
                if(bankcustomerstate == state.openaccount)
                {
                		Do("i'm opening an account");
                        mybankteller.msgOpenAccount();
                        bankcustomerstate = state.waiting;
                        gui.openaccount = true;
                        return true;
                }
                
                if(bankcustomerstate == state.deposit)
                {
                		Do("I'm depositing into my account");
                        mybankteller.msgDepositIntoAccount(this.deposit);
                        bankcustomerstate = state.waiting;
                        gui.deposit = true;
                        return true;
                }
                
                if(bankcustomerstate == state.withdraw)
                {
                		Do("I'm withdrawing from my account");
                        mybankteller.msgWithdrawFromAccount(this.withdrawal);
                        bankcustomerstate = state.waiting;
                        gui.withdraw = true;
                        return true;
                }
                
                if(bankcustomerstate == state.getloan)
                {
                       	Do("I'm getting loan");
                		mybankteller.msgGetLoan(this.loan);
                        bankcustomerstate = state.waiting;
                        return true;                
                }
                */
                if(bankcustomerstate == state.paybackloan)
                {
                		
                		if(this.paybackloan <= amountofcustomermoney)
                		{
                		log("I'm paying back my loan of $" + this.paybackloan);
                		gui.withdraw = false;
                		gui.paybackloan = true;
                		amountofcustomermoney -= this.paybackloan;
                        mybankteller.msgPayBackLoan(this.paybackloan);
                        bankcustomerstate = state.waiting;
                        return true;
                              
                		}
                		else
                		{
                			log("I don't have enough money to payback my loan so I'm leaving");
                            mybankteller.msgBankCustomerLeaving();
                            guiLeaveBank();
                            gui.withdraw = false;
                            gui.setPresent(false);
                        	person.setRoleInactive(this);
                		}
                }
               
                if(bankcustomerstate == state.openaccountsuccessful)
                {
                       	log("I recevied my account number: " + this.bankaccountnumber);
                		log.add(new LoggedEvent("receivedaccountnumber"));
                        person.msgSetBankAccountNumber(this.bankaccountnumber);
                        gui.openaccount = false;
                        /*
                        if(needloan == true)
                        {
                        	Do("!!!!!!!!!!!!!!! I requested loan");
                        	double amountofloanrequested = amountofcustomermoney * 2;
            				mybankteller.msgGetLoan(amountofloanrequested);
            				gui.deposit = true;
            				//return true;   			
                        		
                        }
                        */
                        if(amountofcustomermoney >= 50)
                        {
                        	gui.deposit = true;
                        	Do(" !!!!!!!!!!! amount of customer money" + amountofcustomermoney);
                        	bankcustomerstate = state.waiting;
                        	double amounttodeposit = amountofcustomermoney/2;
                        	amountofcustomermoney -= amounttodeposit;
                        	mybankteller.msgDepositIntoAccount(amounttodeposit);
                        }
                        
                        else
                        {
                        
                        	mybankteller.msgBankCustomerLeaving();
                        	log("My wallet : " + amountofcustomermoney);
                        	guiLeaveBank();
                        	gui.setPresent(false);
                        	person.setRoleInactive(this);
                            //bankcustomerstate = state.waiting;
                    
                        }
                        //return true;        
                 }                
                if(bankcustomerstate == state.depositintoaccountsuccessful)
                {
                	    
                		this.amountofcustomermoney -= this.deposit;
                		gui.deposit = false;
                		log("I successfully deposited money into my account");
                		log("My wallet after depositing into my account : " + amountofcustomermoney);
                		log.add(new LoggedEvent("successfullydeposittedintoaccount"));
                		mybankteller.msgBankCustomerLeaving();
                		person.msgBalanceAfterDepositingIntoAccount(this.amountofcustomermoney);
                        guiLeaveBank();
                    	gui.setPresent(false);
                    	person.setRoleInactive(this);
                          
                }
                
                if(bankcustomerstate == state.withdrawfromaccountsuccessful)
                {
                		
                        log.add(new LoggedEvent("successfullywithdrewfromaccount"));
                        this.amountofcustomermoney += this.withdrawal;
                        gui.withdraw = false;
                        gui.money = true;
                        log("After withdrawing from my account I now have $" + this.amountofcustomermoney);
                        person.msgBalanceAfterWithdrawingFromAccount(this.amountofcustomermoney);
                        mybankteller.msgBankCustomerLeaving();
                        guiLeaveBank();
                    	gui.setPresent(false);
                    	person.setRoleInactive(this);
                        
                }
                
                if(bankcustomerstate == state.getloansuccessful)
                {
                		log("I received a loan of $" + this.loan);
                		log("Now I have $" + this.loan + " in my wallet");
                        this.amountofcustomermoney += this.loan;
                        gui.loan = false;
                        gui.money = true;
                        person.msgBalanceAfterGetitngLoanFromAccount(this.amountofcustomermoney);
                        mybankteller.msgBankCustomerLeaving();
                        guiLeaveBank();
                    	gui.setPresent(false);
                    	person.setRoleInactive(this);
                          
                }
               
                if(bankcustomerstate == state.leave)
                {
                		Do("I'm leaving");
                        mybankteller.msgBankCustomerLeaving();
                        guiLeaveBank();
                        gui.setPresent(false);
                    	person.setRoleInactive(this);
                }
               
        
                
                return false;
        
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
        	this.bankaccountnumber = person.bankaccountnumber;
        	//this.bankaccountnumber = 1;
        	this.amountofcustomermoney = person.wallet;
        	this.amountofcustomermoney = 60;
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
        	
        	Do("I'm leaving bank");
        	gui.leaveBank();
        	try {
    			atBankLobby.acquire();
    			//atLobby.acquire();
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        	Do("I left bank!");
        	
        }
        
    	private void log(String msg){
    		print(msg);
    		ActivityLog.getInstance().logActivity(tag, msg, name, false);
    		log.add(new LoggedEvent(msg));
    	}
        

    	public void setGuiActive() {
    		//customerGui.DoEnterRestaurant();
    		gui.setPresent(true);
    	}

		@Override
		public String getRoleName() {
			return roleName;
		}
        
        
}