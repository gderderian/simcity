package Role;
import java.util.concurrent.Semaphore;

import activityLog.ActivityLog;
import activityLog.ActivityTag;

import test.mock.EventLog;
import test.mock.LoggedEvent;
import city.Bank;
import city.account;
import city.gui.Bank.BankTellerRoleGui;
import Role.BankCustomerRole;
import city.PersonAgent;

public class BankTellerRole extends Role {

	String roleName = "BankTellerRole";

	public BankCustomerRole currentcustomer;//since bank teller serves one customer at a time. no list is necessary
	//BankTellerRole banktellerrole;
	public int currentcustomeraccountnumber;
	String name;
	public double deposit;
	public double loan;
	public double withdrawal;
	double paybackloan;
	public BankManagerRole bankmanager;
	public Semaphore atBankStation = new Semaphore(0,true);
	enum state {doingnothing, openaccount, depositintoaccount, withdrawfromaccount, getloan, paybackloan, customerleft, gotobanktellerstation};
	state banktellerstate;
	public BankTellerRoleGui gui;
	public EventLog log = new EventLog();

	ActivityTag tag = ActivityTag.BANKTELLER;

	PersonAgent person;
	int stationnumber;


	public BankTellerRole(BankManagerRole assignbankmanager)
	{
		super();

		//this.banktellerrole = assignbanktellerrole;
		this.bankmanager = assignbankmanager;
	}

	public void msgGoToBankTellerStation(int banktellerstationnumber)
	{
		stationnumber = banktellerstationnumber;
		banktellerstate = state.gotobanktellerstation;
		person.stateChanged();

	}

	public void msgAssignMeCustomer(BankCustomerRole customer)
	{
		currentcustomer = customer;
		currentcustomeraccountnumber = currentcustomer.bankaccountnumber;
		person.stateChanged();
	}

	public void msgOpenAccount() 
	{
		Do("customer wants to open an account");
		log.add(new LoggedEvent("msgOpenAccount"));
		banktellerstate = state.openaccount;
		gui.bankTellerOccupied = true;
		person.stateChanged();
	}

	public void msgDepositIntoAccount(double deposit)
	{
		Do("customer wants to deposit into an account");
		log.add(new LoggedEvent("msgDepositIntoAccount"));
		this.deposit = deposit;
		banktellerstate = state.depositintoaccount;
		person.stateChanged();
	}

	public void msgWithdrawFromAccount(double withdrawal)
	{
		Do("customer wants to withdraw from his account");
		log.add(new LoggedEvent("msgWithdrawFromAccount"));
		this.withdrawal = withdrawal;
		banktellerstate = state.withdrawfromaccount;
		person.stateChanged();
	}

	public void msgGetLoan(double loan)
	{
		log.add(new LoggedEvent("msgGetLoan"));
		Do("customer wants to get loan");
		this.loan = loan;
		banktellerstate = state.getloan;
		person.stateChanged();
	}

	public void msgPayBackLoan(double paybackloan)
	{

		Do("customer wants to pay back his loan");
		this.paybackloan = paybackloan;
		banktellerstate = state.paybackloan;
		person.stateChanged();
	}

	public void msgBankCustomerLeaving()
	{
		log.add(new LoggedEvent("msgBankCustomerLeaving"));
		banktellerstate = state.customerleft;
		person.stateChanged();
	}


	public boolean pickAndExecuteAnAction() {

		if(banktellerstate == state.gotobanktellerstation)
		{
			guiGoToBankTellerStation(stationnumber);
			banktellerstate = state.doingnothing;
			return true;
		}


		if(banktellerstate == state.openaccount)
		{
			Do("customer is opening account");
			bankmanager.bank.accounts.add(new account(currentcustomer, bankmanager.bank.uniqueaccountnumber));
			currentcustomeraccountnumber = bankmanager.bank.uniqueaccountnumber;
			currentcustomer.msgOpenAccountDone(currentcustomeraccountnumber);
			bankmanager.bank.uniqueaccountnumber++;
			banktellerstate = state.doingnothing;
			return true;
		}

		if(banktellerstate == state.depositintoaccount)
		{

			synchronized(bankmanager.bank.accounts)
			{

				for(account findaccount: bankmanager.bank.accounts)
				{
					if(findaccount.accountnumber == currentcustomeraccountnumber)
					{        
						//System.out.println("accout number = "+currentcustomeraccountnumber);
						//System.out.println("amount to deposit ="+this.deposit);
						log.add(new LoggedEvent("deposit!"));
						findaccount.balance += this.deposit;
						currentcustomer.msgDepositIntoAccountDone(this.deposit);
						break;
					}
				}

			}

			banktellerstate = state.doingnothing;
			return true;


		}

		if(banktellerstate == state.withdrawfromaccount)
		{

			synchronized(bankmanager.bank.accounts)
			{

				for(account findaccount: bankmanager.bank.accounts)
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


			}
			banktellerstate = state.doingnothing;
			return true;
		}


		if(banktellerstate == state.getloan)
		{

			synchronized(bankmanager.bank.accounts)
			{

				for(account findaccount: bankmanager.bank.accounts)
				{
					if(findaccount.accountnumber == currentcustomeraccountnumber)
					{        

						if(findaccount.loan + loan > 50)
						{

							currentcustomer.msgCannotGetLoan(loan);
						}
						else
						{
							findaccount.loan += this.loan;
							currentcustomer.msgLoanBorrowed(loan);
						}


					}
				}

			}
			banktellerstate = state.doingnothing;
			return true;

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

			synchronized(bankmanager.bank.accounts)
			{

				for(account findaccount: bankmanager.bank.accounts)
				{
					if(findaccount.accountnumber == currentcustomeraccountnumber)
					{        



						findaccount.loan -= paybackloan;
						currentcustomer.msgLoanPaidBack(paybackloan, findaccount.loan);


						// this is more advanced loan system.
						double oldestloanamount;
						double subtotal;
						int i = 0;
						//60, loan 1 = 20, loan 2 30;
						do
						{
							//Do("i'm in the do while");
							oldestloanamount = findaccount.loans.get(i).loanamount;
							subtotal = oldestloanamount - paybackloan;
							if(subtotal <= 0)
							{
								currentcustomer.msgLoanPaid(findaccount.loans.get(i).loanamount,findaccount.loans.get(i).lendtime, findaccount.loans.get(i).interestrate);
								findaccount.loans.remove(i);

							}
							subtotal *= -1;
							paybackloan = subtotal;        
							i++;
						}while(paybackloan != 0 || findaccount.loans.size() != 0);

					}
				}
				return true;

			}

		}


		if(banktellerstate == state.customerleft)
		{
			bankmanager.msgCustomerLeft(currentcustomer, this);
			this.currentcustomer = null;
			banktellerstate = state.doingnothing;
			return true;

		}


		return false;
	}


	public void setGui(BankTellerRoleGui setgui) {

		this.gui = setgui;
	}

	public String getName() {
		return this.name;
	}

	public void setPerson(PersonAgent setperson) {
		this.person = setperson;
		this.name = person.getName();
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

	@Override
	public String getRoleName() {
		return roleName;
	}

	private void log(String msg){
		print(msg);
		ActivityLog.getInstance().logActivity(tag, msg, name);
	}



}