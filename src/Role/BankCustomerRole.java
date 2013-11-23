package Role;

import java.util.concurrent.Semaphore;

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
        //public int customeraccountnumber;
        public Semaphore atBankStation = new Semaphore(0,true);
        public Semaphore atBankLobby = new Semaphore(0,true);
        public BankCustomerRoleGui gui;
        PersonAgent person;
        String name;
        public EventLog log = new EventLog();
        
        
        public BankCustomerRole(double setamountofcustomermoney, PersonAgent setperson)
        {
                
                bankcustomerstate = state.arrived;
                this.amountofcustomermoney = setamountofcustomermoney;
                this.person = setperson;
                //stateChanged();
        
        }
        
        
        public void msgAssignMeBankTeller(BankTellerRole assignbankteller)
        {

                mybankteller = assignbankteller;
                bankcustomerstate = state.gotobankteller;
                stateChanged();
       
        }
        
        
        public void msgWithDrawFund(double withdrawal)
        {
                
                bankcustomerstate = state.withdraw;
                this.withdrawal = withdrawal;
                stateChanged();
                
        }
        
        public void msgOpenAccount()
        {
                //log.add(new LoggedEvent("msgOpenAccount")
                bankcustomerstate = state.openaccount;
                stateChanged();
                
        }
        
        public void msgDepositIntoAccount(double deposit)
        {
                
                bankcustomerstate = state.deposit;
                this.deposit = deposit;
                stateChanged();
                
        }
        
        public void msgGetLoan(double loan)
        {
                
                bankcustomerstate = state.getloan;
                this.loan = loan;
                stateChanged();
                
        }
        
        public void msgPayLoan( double paybackloan)
        {
                
                bankcustomerstate = state.paybackloan;
                this.paybackloan = paybackloan;
                stateChanged();        
        }
        
        public void msgLeaveBank()
        {
                
                bankcustomerstate = state.leave;
                stateChanged();
                
        }

        public void msgOpenAccountDone(int setcustomeraccountnumber) 
        {
                log.add(new LoggedEvent("msgOpenAccountDone"));
                bankcustomerstate = state.openaccountsuccessful;
                this.bankaccountnumber = setcustomeraccountnumber;
                stateChanged();
                
        }
        
        public void msgDepositIntoAccountDone(double setdeposit) 
        {
                log.add(new LoggedEvent("msgDepositIntoAccountDone"));
                bankcustomerstate = state.depositintoaccountsuccessful;
                this.deposit = setdeposit;
                stateChanged();

        }

        public void msgHereIsYourWithdrawal(double setwithdrawal) 
        {
                log.add(new LoggedEvent("msgHereIsYourWithdrawal"));
                bankcustomerstate = state.withdrawfromaccountsuccessful;
                this.withdrawal = setwithdrawal;
                stateChanged();
                
        }
        
        public void msgHereIsYourLoan(double setloan)
        {
                bankcustomerstate = state.getloansuccessful;
                this.loan = setloan;
                stateChanged();
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
                // TODO Auto-generated method stub
                
        }
        
        public void msgLoanPaidBack(double amountofloanpaidback, double amountofremainingloan)
        {
                
                
        }
        
        public void msgLoanPaid(double loanamount, double lendtime,double interestrate) {
                Do("Successfully paid off loan of:" + loanamount + " lendtime: " + lendtime +" days" + " interestrate: " + interestrate);
                
        }
        

        public boolean pickAndExecuteAnAction() 
        {
                
        		/*
        		if(bankcustomerstate == state.gotobankteller)
        		{
        			gui.goToBankTellerStation(mybankteller)
        		}
        		*/
        	
                if(bankcustomerstate == state.openaccount)
                {
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
                        //gui animation of customer leaving the bank
                        return true;
                }
                
              //gui.gotohomeposition();
                
                return false;
        }


        public Object getGui() {
           
                return this.gui;
        }
        
        public void setGui(BankCustomerRoleGui setGui)
        {
        	this.gui = setGui;
        }
        
        public String getName() {
        	return this.name;
        }
        
        
        
        
}