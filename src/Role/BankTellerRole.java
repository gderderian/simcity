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
		void msgAssignMeCustomer(BankCustomerRole customer)
=======
		public void msgAssignMeCustomer(BankCustomerRole customer)
>>>>>>> ae741fab47ec37fd55057b894a94a7702040c30d
		{
			currentcustomer = customer;
			currentcustomeraccountnumber = currentcustomer.bankaccountnumber;
		}

<<<<<<< HEAD
		void msgOpenAccount() 
=======
		public void msgOpenAccount() 
>>>>>>> ae741fab47ec37fd55057b894a94a7702040c30d
		{
			banktellerstate = banktellerstate.openaccount;
			stateChanged();
		}

<<<<<<< HEAD
		void msgDepositIntoAccount(double deposit)
=======
		public void msgDepositIntoAccount(double deposit)
>>>>>>> ae741fab47ec37fd55057b894a94a7702040c30d
		{
			this.deposit = deposit;
			banktellerstate = banktellerstate.depositintoaccount;
			stateChanged();
		}

<<<<<<< HEAD
		void msgWithdrawFromAccount(double withdrawal)
=======
		public void msgWithdrawFromAccount(double withdrawal)
>>>>>>> ae741fab47ec37fd55057b894a94a7702040c30d
		{
			this.withdrawal = withdrawal;
			banktellerstate = banktellerstate.withdrawfromaccount;
			stateChanged();
		}

<<<<<<< HEAD
		void msgGetLoan(double loan)
=======
		public void msgGetLoan(double loan)
>>>>>>> ae741fab47ec37fd55057b894a94a7702040c30d
		{
			this.loan = loan;
			banktellerstate = banktellerstate.getloan;
			stateChanged();
		}


<<<<<<< HEAD
	protected boolean pickedAndExecuteAnAction() {
=======
	protected boolean pickAndExecuteAnAction() {
>>>>>>> ae741fab47ec37fd55057b894a94a7702040c30d
		
		
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
					currentcustomer.msgDeositIntoAccountDone();
=======
					currentcustomer.msgOpenAccountDone();
>>>>>>> ae741fab47ec37fd55057b894a94a7702040c30d
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
