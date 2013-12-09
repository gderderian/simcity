package Role;
import java.util.Timer;
import java.util.TimerTask;
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
	enum state {arrived, atstation, servingcustomer, waitingforresponse, openaccount, depositintoaccount, withdrawfromaccount, getloan, paybackloan, customerleft, gotobanktellerstation};
	enum event {gotobanktellerstation, openaccount, depositintoaccount, withdrawfromaccount, getloan, paybackloan, customerleft};
	state banktellerstate;
	event banktellerevent;
	public BankTellerRoleGui gui;
	public EventLog log = new EventLog();
	Timer timer = new Timer();
	ActivityTag tag = ActivityTag.BANKTELLER;

	PersonAgent person;
	int stationnumber;


	public BankTellerRole(BankManagerRole assignbankmanager)
	{
		super();
		this.bankmanager = assignbankmanager;
		banktellerstate = state.arrived;
	}

	public void msgGoToBankTellerStation(int banktellerstationnumber)
	{
		stationnumber = banktellerstationnumber;
		banktellerevent = event.gotobanktellerstation;
		person.stateChanged();

	}

	public void msgAssignMeCustomer(BankCustomerRole customer)
	{
		log("assign me customer");
		currentcustomer = customer;
		currentcustomeraccountnumber = currentcustomer.bankaccountnumber;
		banktellerstate = state.servingcustomer;
		person.stateChanged();
	}

	public void msgOpenAccount() 
	{
		log("customer wants to open an account");
		log.add(new LoggedEvent("msgOpenAccount"));
		banktellerevent = event.openaccount;
		gui.bankTellerOccupied = true;
		person.stateChanged();
	}

	public void msgDepositIntoAccount(double deposit)
	{
		log("customer wants to deposit into an account");
		log.add(new LoggedEvent("msgDepositIntoAccount"));
		this.deposit = deposit;
		banktellerevent = event.depositintoaccount;
		banktellerstate = state.servingcustomer;
		person.stateChanged();
	}

	public void msgWithdrawFromAccount(double withdrawal)
	{
		Do("customer wants to withdraw from his account");
		log.add(new LoggedEvent("msgWithdrawFromAccount"));
		this.withdrawal = withdrawal;
		banktellerevent = event.withdrawfromaccount;
		banktellerstate = state.servingcustomer;
		person.stateChanged();
	}

	public void msgGetLoan(double loan)
	{
		log.add(new LoggedEvent("msgGetLoan"));
		Do("customer wants to get loan");
		this.loan = loan;
		banktellerevent = event.getloan;
		banktellerstate = state.servingcustomer;
		person.stateChanged();
	}

	public void msgPayBackLoan(double paybackloan)
	{

		Do("customer wants to pay back his loan");
		this.paybackloan = paybackloan;
		banktellerevent = event.paybackloan;
		person.stateChanged();
	}

	public void msgBankCustomerLeaving()
	{
		
		Do("!!!!!!!!!!!!!! message bankcustomerleaving recevied");
		log.add(new LoggedEvent("msgBankCustomerLeaving"));
		banktellerevent = event.customerleft;
		person.stateChanged();
	}


	public boolean pickAndExecuteAnAction() {
		//log("INSIDE BANK TELLER SCHEDULER");

		
		Do("!!!!!!!!!!!!!!!!!!!! banktellerrole scheduler  state " + banktellerstate + "   event " + banktellerevent);
		
		if(banktellerstate == state.arrived && banktellerevent == event.gotobanktellerstation)
		{
			guiGoToBankTellerStation(stationnumber);
			banktellerstate = state.atstation;
			return true;
		}
		
		if(banktellerstate == state.servingcustomer && banktellerevent == event.openaccount)
		{
			log("customer is opening account");
			bankmanager.bank.accounts.add(new account(currentcustomer, bankmanager.bank.uniqueaccountnumber));
			currentcustomeraccountnumber = bankmanager.bank.uniqueaccountnumber;
			bankmanager.bank.uniqueaccountnumber++;
			banktellerstate = state.waitingforresponse;
			timer.schedule(new TimerTask() {
				
				public void run() {
					currentcustomer.msgOpenAccountDone(currentcustomeraccountnumber);
					//person.stateChanged();
				}
					},
				2 * 1000);
			
		   return true;
		}

		if(banktellerstate == state.servingcustomer && banktellerevent == event.depositintoaccount)
		{
			synchronized(bankmanager.bank.accounts)
			{

				for(account findaccount: bankmanager.bank.accounts)
				{
					if(findaccount.accountnumber == currentcustomeraccountnumber)
					{        
						//System.out.println("accout number = "+currentcustomeraccountnumber);
						//System.out.println("amount to deposit ="+this.deposit);
						findaccount.balance += this.deposit;
						log("Current balance of account number : " + findaccount.accountnumber + " is $" + findaccount.balance);
						log.add(new LoggedEvent("deposit!"));
						timer.schedule(new TimerTask() {
							
							public void run() {
							Do("!!!!!!!! deposit" + deposit);
							currentcustomer.msgDepositIntoAccountDone(deposit);	
							}
								},
							2 * 1000);
					
						break;
					}
				}

			}

			banktellerstate = state.waitingforresponse;
			return true;


		}

		if(banktellerstate == state.servingcustomer && banktellerevent == event.withdrawfromaccount)
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
							currentcustomer.msgWithdrawalFailed(withdrawal);
						}

					}
				}


			}
			banktellerstate = state.waitingforresponse;
			return true;
		}


		if(banktellerstate == state.servingcustomer && banktellerevent == event.getloan)
		{

			synchronized(bankmanager.bank.accounts)
			{
				Do("I'm in the if statement");

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
							currentcustomer.msgHereIsYourLoan(loan);
						}


					}
				}

			}
			banktellerstate = state.waitingforresponse;
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


		if(banktellerstate == state.servingcustomer && banktellerevent == event.paybackloan)
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


		if(banktellerstate == state.waitingforresponse && banktellerevent == event.customerleft)
		{
			
			Do("!!!!!!!!!!!!!!!!!!!!! i'm in if customer left statement");
			//bankmanager.msgCustomerLeft(currentcustomer, this);
			bankmanager.msgBankTellerFree(this);
			this.currentcustomer = null;
			gui.bankTellerOccupied = false;
			banktellerstate = state.atstation;
			//return true;

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
		//Do("!!!!!!!!!  I'm done going to the bank teller station");


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