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
		
				
<<<<<<< HEAD
		public void msgAssignMeCustomer(BankCustomerRole customer)
=======
		void msgAssignMeCustomer(BankCustomerRole customer)
>>>>>>> my agent files
		{
			currentcustomer = customer;
			currentcustomeraccountnumber = currentcustomer.bankaccountnumber;
		}

<<<<<<< HEAD
		public void msgOpenAccount() 
=======
		void msgOpenAccount() 
>>>>>>> my agent files
		{
			banktellerstate = banktellerstate.openaccount;
			stateChanged();
		}

<<<<<<< HEAD
		public void msgDepositIntoAccount(double deposit)
=======
		void msgDepositIntoAccount(double deposit)
>>>>>>> my agent files
		{
			this.deposit = deposit;
			banktellerstate = banktellerstate.depositintoaccount;
			stateChanged();
		}

<<<<<<< HEAD
		public void msgWithdrawFromAccount(double withdrawal)
=======
		void msgWithdrawFromAccount(double withdrawal)
>>>>>>> my agent files
		{
			this.withdrawal = withdrawal;
			banktellerstate = banktellerstate.withdrawfromaccount;
			stateChanged();
		}

<<<<<<< HEAD
		public void msgGetLoan(double loan)
=======
		void msgGetLoan(double loan)
>>>>>>> my agent files
		{
			this.loan = loan;
			banktellerstate = banktellerstate.getloan;
			stateChanged();
		}


<<<<<<< HEAD
	protected boolean pickAndExecuteAnAction() {
=======
	protected boolean pickedAndExecuteAnAction() {
>>>>>>> my agent files
		
		
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
<<<<<<< HEAD
					currentcustomer.msgOpenAccountDone();
=======
					currentcustomer.msgDeositIntoAccountDone();
>>>>>>> my agent files
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
