package Role;

import java.util.concurrent.Semaphore;

import city.gui.Gui;
import city.gui.Bank.BankCustomerRoleGui;
import test.mock.EventLog;
import test.mock.LoggedEvent;
import city.PersonAgent;

public class BankCustomerRole extends Role{

        public enum state {arrived, waiting, inprogress, gotobankteller, openaccount, withdraw, deposit, leave, getloan, paybackloan, openaccountsuccessful, depositintoaccountsuccessful, withdrawfromaccountsuccessful, getloansuccessful};
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
        
        
        public BankCustomerRole(double setamountofcustomermoney/*, PersonAgent setperson*/)
        {
                
                bankcustomerstate = state.arrived;
                this.amountofcustomermoney = setamountofcustomermoney;
                bankaccountnumber = 0;
                //this.person = setperson;
                //this.name = setperson.getName();
                //stateChanged();
        
        } 
        
        public void msgAssignMeBankTeller(BankTellerRole assignbankteller)
        {
     
                mybankteller = assignbankteller;
                stationnumber = mybankteller.stationnumber;
                bankcustomerstate = state.gotobankteller; 
                Do("i'm in the message of customer");
                Do("current customer state is : " + bankcustomerstate);
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
        
        public void msgLeaveBank()
        {
                //animation state change
                bankcustomerstate = state.leave;
                person.stateChanged();
                
        }

        public void msgOpenAccountDone(int setcustomeraccountnumber) 
        {
                log.add(new LoggedEvent("msgOpenAccountDone"));
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

        public void msgWithdrawalFailed() 
        {
                // TODO Auto-generated method stub
                
        }

        public void msgCannotGetLoan(double loan) 
        {
                // TODO Auto-generated method stub
                
        }

        public void msgLoanBorrowed(double loan) 
        {
        	//log.add(new LoggedEvent("msgLoanBorrowed"));  
        	 amountofcustomermoney = loan;
                
        }
        
        public void msgLoanPaidBack(double amountofloanpaidback, double amountofremainingloan)
        {
        	Do("msgLoanPaidBack");
        	log.add(new LoggedEvent("msgLoanPaidBack"));
                
        }
        
        public void msgLoanPaid(double loanamount, double lendtime,double interestrate) {
                Do("Successfully paid off loan of:" + loanamount + " lendtime: " + lendtime +" days" + " interestrate: " + interestrate);
                log.add(new LoggedEvent("msgLoanPaidBack"));
        }
        

        public boolean pickAndExecuteAnAction() 
        {
        	
        		Do(" I'm in the bank customer scheduler");
                
        		if(bankcustomerstate == state.gotobankteller)
        		{
        			
        			Do("gui is going to bank teller station");
        			guiGoToBankTellerStation(stationnumber);
        			bankcustomerstate = state.waiting;
        			if(bankaccountnumber == 0)
        			{
        				bankcustomerstate = state.openaccount;
        			}
        			else if(amountofcustomermoney >= 50)
        			{
        				bankcustomerstate = state.deposit;
        			}
        			
        					
        			return true;
        		}
        	
        	
                if(bankcustomerstate == state.openaccount)
                {
                		Do("i'm opening account");
                        mybankteller.msgOpenAccount();
                        bankcustomerstate = state.waiting;
                        return true;
                }
                
                if(bankcustomerstate == state.deposit)
                {
                        mybankteller.msgDepositIntoAccount(this.deposit);
                        bankcustomerstate = state.waiting;
                        return true;
                }
                
                if(bankcustomerstate == state.withdraw)
                {
                        mybankteller.msgWithdrawFromAccount(this.withdrawal);
                        bankcustomerstate = state.waiting;
                        return true;
                }
                
                if(bankcustomerstate == state.getloan)
                {
                        mybankteller.msgGetLoan(this.loan);
                        bankcustomerstate = state.waiting;
                        return true;                
                }
                
                if(bankcustomerstate == state.paybackloan)
                {
                        mybankteller.msgGetLoan(this.paybackloan);
                        bankcustomerstate = state.waiting;
                        return true;
                }
                
                if(bankcustomerstate == state.openaccountsuccessful)
                {
                        log.add(new LoggedEvent("receivedaccountnumber"));
                        person.msgSetBankAccountNumber(this.bankaccountnumber);
                        bankcustomerstate = state.waiting;
                        return true;        
                }
                
                if(bankcustomerstate == state.depositintoaccountsuccessful)
                {
                        log.add(new LoggedEvent("successfullydeposittedintoaccount"));
                        this.amountofcustomermoney -= this.deposit;
                        person.msgBalanceAfterDepositingIntoAccount(this.amountofcustomermoney);
                        bankcustomerstate = state.waiting;
                        return true;        
                }
                
                if(bankcustomerstate == state.withdrawfromaccountsuccessful)
                {
                        log.add(new LoggedEvent("successfullywithdrewfromaccount"));
                        this.amountofcustomermoney += this.withdrawal;
                        person.msgBalanceAfterWithdrawingFromAccount(this.amountofcustomermoney);
                        bankcustomerstate = state.waiting;
                        return true;        
                }
                
                if(bankcustomerstate == state.getloansuccessful)
                {
                        this.amountofcustomermoney += this.loan;
                        person.msgBalanceAfterGetitngLoanFromAccount(this.amountofcustomermoney);
                        bankcustomerstate = state.waiting;
                        return true;        
                }
                
                if(bankcustomerstate == state.leave)
                {
                        mybankteller.msgBankCustomerLeaving();
                        guiLeaveBank();
                        bankcustomerstate = state.waiting;
                        return true;
                }
                
              //gui.gotohomeposition();
                
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
        
        
}