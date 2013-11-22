package Role;

import test.mock.EventLog;
import test.mock.LoggedEvent;

public class BankCustomerRole extends Role{

	public enum state {arrived, wihdraw, openaccount, deposit, leave, getloan, paybackloan, openaccountsuccessful, depositintoaccountsuccessful, withdrawfromaccountsuccessful, getloansuccessful};
	public int bankaccountnumber;
	state bankcustomerstate;
	BankTellerRole mybankteller;
	double deposit;
	double withdrawal;
	double loan;
	double paybackloan;
	public double amountofcustomermoney;
	//public int customeraccountnumber;
	PersonAgent person;
	public EventLog log = new EventLog();
	
	
	public BankCustomerRole(double setamountofcustomermoney, PersonAgent setperson)
	{
		
		bankcustomerstate = state.arrived;
		this.amountofcustomermoney = setamountofcustomermoney;
		this.person = setperson;
		stateChanged();
	
	}
	
	
	public void msgAssignMeBankTeller(BankTellerRole assignbankteller)
	{

		mybankteller = assignbankteller;
		stateChanged();
	
	}
	
	public void msgWithDrawFund(double withdrawal)
	{
		
		bankcustomerstate = state.wihdraw;
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
		

		if(bankcustomerstate == state.openaccount)
		{
			mybankteller.msgOpenAccount();
			return true;
		}
		
		if(bankcustomerstate == state.deposit)
		{
			mybankteller.msgDepositIntoAccount(this.deposit);
			return true;
		}
		
		if(bankcustomerstate == state.wihdraw)
		{
			mybankteller.msgWithdrawFromAccount(this.withdrawal);
			return true;
		}
		
		if(bankcustomerstate == state.getloan)
		{
			mybankteller.msgGetLoan(this.loan);
			return true;		
		}
		
		if(bankcustomerstate == state.paybackloan)
		{
			mybankteller.msgGetLoan(this.paybackloan);
			return true;
		}
		
		if(bankcustomerstate == state.openaccountsuccessful)
		{
			log.add(new LoggedEvent("receivedaccountnumber"));
			person.msgSetBankAccountNumber(this.bankaccountnumber);
			return true;	
		}
		
		if(bankcustomerstate == state.depositintoaccountsuccessful)
		{
			log.add(new LoggedEvent("successfullydeposittedintoaccount"));
			this.amountofcustomermoney -= this.deposit;
			person.msgBalanceAfterDepositingIntoAccount(this.amountofcustomermoney);
			return true;	
		}
		
		if(bankcustomerstate == state.withdrawfromaccountsuccessful)
		{
			log.add(new LoggedEvent("successfullywithdrewfromaccount"));
			this.amountofcustomermoney += this.withdrawal;
			person.msgBalanceAfterWithdrawingFromAccount(this.amountofcustomermoney);
			return true;	
		}
		
		if(bankcustomerstate == state.getloansuccessful)
		{
			this.amountofcustomermoney += this.loan;
			person.msgBalanceAfterGetitngLoanFromAccount(this.amountofcustomermoney);
			return true;	
		}
		
		
		return false;
	}
	
	
}
