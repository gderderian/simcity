package Role;
import city.BankAgent;
import city.account;
import Role.BankCustomerRole;
public class BankTellerRole extends Role {
	

		BankCustomerRole currentcustomer;//since bank teller serves one customer at a time. no list is necessary
		BankTellerRole banktellerrole;
		int currentcustomeraccountnumber;
		String name;
		double deposit;
		double loan;
		double withdrawal;
		public BankAgent bank;
		enum state {openaccount, depositintoaccount, withdrawfromaccount, getloan};
		state banktellerstate;
		
		
		public BankTellerRole(BankTellerRole assignbanktellerrole, BankAgent assignbank)
		{
			super();
			this.banktellerrole = assignbanktellerrole;
			this.bank = assignbank;
		}
		
				
		public void msgAssignMeCustomer(BankCustomerRole customer)
		{
			currentcustomer = customer;
			currentcustomeraccountnumber = currentcustomer.bankaccountnumber;
		}

		public void msgOpenAccount() 
		{
			banktellerstate = banktellerstate.openaccount;
			stateChanged();
		}

		public void msgDepositIntoAccount(double deposit)
		{
			this.deposit = deposit;
			banktellerstate = banktellerstate.depositintoaccount;
			stateChanged();
		}

		public void msgWithdrawFromAccount(double withdrawal)
		{
			this.withdrawal = withdrawal;
			banktellerstate = banktellerstate.withdrawfromaccount;
			stateChanged();
		}

		public void msgGetLoan(double loan)
		{
			this.loan = loan;
			banktellerstate = banktellerstate.getloan;
			stateChanged();
		}


	protected boolean pickAndExecuteAnAction() {
		
		
		if(banktellerstate == state.openaccount)
		{
		    bank.accounts.add(new account(currentcustomer, bank.uniqueaccountnumber));
			bank.uniqueaccountnumber++;
			currentcustomer.msgOpenAccountDone();
			return true;
		}

		if(banktellerstate == state.depositintoaccount)
		{
			for(account findaccount: bank.accounts)
			{
				if(findaccount.accountnumber == currentcustomeraccountnumber)
				{	
					findaccount.balance += this.deposit;
					currentcustomer.msgOpenAccountDone();
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

		}
		return false;
}

	
	
	
	
	
	
}
