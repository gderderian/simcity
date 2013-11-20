package Role;
import test.mock.EventLog;
import test.mock.LoggedEvent;
import city.BankAgent;
import city.account;
import Role.BankCustomerRole;
public class BankTellerRole extends Role {
	

		BankCustomerRole currentcustomer;//since bank teller serves one customer at a time. no list is necessary
		//BankTellerRole banktellerrole;
		public int currentcustomeraccountnumber;
		String name;
		public double deposit;
		double loan;
		public double withdrawal;
		double paybackloan;
		public BankAgent bank;
		enum state {openaccount, depositintoaccount, withdrawfromaccount, getloan, paybackloan};
		state banktellerstate;
		public EventLog log = new EventLog();
		
		
		public BankTellerRole(/*BankTellerRole assignbanktellerrole,*/ BankAgent assignbank)
		{
			super();
			//this.banktellerrole = assignbanktellerrole;
			this.bank = assignbank;
		
		}
		
				
		public void msgAssignMeCustomer(BankCustomerRole customer)
		{
			currentcustomer = customer;
			currentcustomeraccountnumber = currentcustomer.bankaccountnumber;
			stateChanged();
		}

		public void msgOpenAccount() 
		{
			
			log.add(new LoggedEvent("msgOpenAccount"));
			banktellerstate = state.openaccount;
			stateChanged();
		}

		public void msgDepositIntoAccount(double deposit)
		{
			log.add(new LoggedEvent("msgDepositIntoAccount"));
			this.deposit = deposit;
			banktellerstate = state.depositintoaccount;
			stateChanged();
		}

		public void msgWithdrawFromAccount(double withdrawal)
		{
			log.add(new LoggedEvent("msgWithdrawFromAccount"));
			this.withdrawal = withdrawal;
			banktellerstate = state.withdrawfromaccount;
			stateChanged();
		}

		public void msgGetLoan(double loan)
		{
			this.loan = loan;
			banktellerstate = state.getloan;
			stateChanged();
		}
		
		public void msgPayBackLoan(double paybackloan)
		{
			this.paybackloan = paybackloan;
			banktellerstate = state.paybackloan;
			stateChanged();
		}


	public boolean pickAndExecuteAnAction() {
		
		
		if(banktellerstate == state.openaccount)
		{
		    bank.accounts.add(new account(currentcustomer, BankAgent.uniqueaccountnumber));
		    currentcustomeraccountnumber = BankAgent.uniqueaccountnumber;
		    BankAgent.uniqueaccountnumber++;
			currentcustomer.msgOpenAccountDone();
			
			return true;
		}

		if(banktellerstate == state.depositintoaccount)
		{
			for(account findaccount: bank.accounts)
			{
				if(findaccount.accountnumber == currentcustomeraccountnumber)
				{	
					//System.out.println("accout number = "+currentcustomeraccountnumber);
					//System.out.println("amount to deposit ="+this.deposit);
					log.add(new LoggedEvent("deposit!"));
					findaccount.balance += this.deposit;
					currentcustomer.msgDepositIntoAccountDone();
					break;
				}
			}
			return true;
		}

		if(banktellerstate == state.withdrawfromaccount)
		{
			for(account findaccount: bank.accounts)
			{
				if(findaccount.accountnumber == currentcustomeraccountnumber)
				{	
					if(!(findaccount.balance < withdrawal))
					{
						findaccount.balance -= this.withdrawal;
						currentcustomer.msgHereIsYourWithdrawal(withdrawal);
					break;
					}
					else
					{
						currentcustomer.msgWithdrawalFailed();
					}
				}
			}
			return true;
		}


		if(banktellerstate == state.getloan)
		{
			for(account findaccount: bank.accounts)
			{
				if(findaccount.accountnumber == currentcustomeraccountnumber)
				{	
				
					if(findaccount.loan + loan < 50)
					{
						currentcustomer.msgCannotGetLoan(loan);
					}
					else
					{
						findaccount.loan += this.loan;
						currentcustomer.msgLoanBorrowed(loan);
					}
					break;	
			
				}
			}
			
			/*
			for(account findaccount: bank.accounts)
			{
	
				if(findaccount.accountnumber == currentcustomeraccountnumber)
				{	
					if(!(findaccount.calculatetotalloan() > 60))
					{
						findaccount.addloan(loan);
						currentcustomer.msgLoanBorrowed(loan);
						break;
					}
					else
					{
						currentcustomer.msgCannotGetLoan(loan);
					}
				}
				
			}
			*/
				
		}
		
		
		if(banktellerstate == state.paybackloan)
		{
			for(account findaccount: bank.accounts)
			{
				if(findaccount.accountnumber == currentcustomeraccountnumber)
				{	
					double oldestloanamount;
					double subtotal;
					
					//60, loan 1 = 20, loan 2 30;
					do
					{
						
						oldestloanamount = findaccount.loans.get(0).loanamount;
						subtotal = oldestloanamount - paybackloan;
						if(subtotal <= 0)
						{
							findaccount.loans.remove(0);
							currentcustomer.msgLoanPaid(findaccount.loans.get(0).loanamount,findaccount.loans.get(0).lendtime, findaccount.loans.get(0).interestrate);
							
						}
						subtotal *= -1;
						paybackloan = subtotal;	
							
					}while(paybackloan == 0 || findaccount.loans.size() == 0);
			
				}
			}
			
		}
		
		
		
		return false;
}

	
	
	
	
	
	
}
