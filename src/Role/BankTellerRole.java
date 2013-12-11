package Role;
import java.util.Random;
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
		//building = "bank1";
	
	}

	public void msgGoToBankTellerStation(int banktellerstationnumber)
	{
		stationnumber = banktellerstationnumber;
		banktellerevent = event.gotobanktellerstation;
		//building = "bank1";
		person.stateChanged();

	}

	public void msgAssignMeCustomer(BankCustomerRole customer)
	{
		log("Assign me customer");
		currentcustomer = customer;
		currentcustomeraccountnumber = currentcustomer.bankaccountnumber;
		banktellerstate = state.servingcustomer;
		person.stateChanged();
	}

	public void msgOpenAccount() 
	{
		log("Customer wants to open an account!");
		log.add(new LoggedEvent("msgOpenAccount"));
		banktellerevent = event.openaccount;
		banktellerstate = state.servingcustomer;
		gui.bankTellerOccupied = true;
		person.stateChanged();
	}

	public void msgDepositIntoAccount(double deposit)
	{
		log("Customer wants to deposit into an account!");
		log.add(new LoggedEvent("msgDepositIntoAccount"));
		this.deposit = deposit;
		banktellerevent = event.depositintoaccount;
		banktellerstate = state.servingcustomer;
		person.stateChanged();
	}

	public void msgWithdrawFromAccount(double withdrawal)
	{
		log("Customer wants to withdraw from account!");
		log.add(new LoggedEvent("msgWithdrawFromAccount"));
		//this.withdrawal = withdrawal/2;
		banktellerevent = event.withdrawfromaccount;
		banktellerstate = state.servingcustomer;
		person.stateChanged();
	}

	public void msgGetLoan(double loan)
	{
		log("Customer wants to get loan");
		log.add(new LoggedEvent("msgGetLoan"));
		this.loan = loan;
		banktellerevent = event.getloan;
		banktellerstate = state.servingcustomer;
		person.stateChanged();
	}

	public void msgPayBackLoan(double paybackloan)
	{
		log("Customer wants to pay back loan!");
		this.paybackloan = paybackloan;
		banktellerevent = event.paybackloan;
		banktellerstate = state.servingcustomer;
		person.stateChanged();
	}

	public void msgBankCustomerLeaving()
	{
		log("Customer left!");
		log.add(new LoggedEvent("msgBankCustomerLeaving"));
		banktellerevent = event.customerleft;
		person.stateChanged();
	}


	public boolean pickAndExecuteAnAction() {
		/*if(bankmanager.bank.accounts.size() > 0)
			{
			for(account displayaccount: bankmanager.bank.accounts )
			{
				log("Acccount " + displayaccount.accountnumber + " balance :" + displayaccount.balance + " loan : " + displayaccount.loan);
			}
		}*/
		
		if(banktellerstate == state.arrived && banktellerevent == event.gotobanktellerstation)
		{
			log("I'm going to bank station " + stationnumber);
			guiGoToBankTellerStation(stationnumber);
			log("I have arrived at station " + stationnumber);
			banktellerstate = state.atstation;
			return true;
		}
		
		Do("Bank teller state :" + banktellerstate + "   event" + banktellerevent);
		
		if(banktellerstate == state.servingcustomer && banktellerevent == event.openaccount)
		{
			
			log("Customer is opening account");
			boolean unique;
			int i;
			
			do {
					Random r = new Random();
        			i = r.nextInt(50); 
					unique = true;
				
					synchronized(bankmanager.bank.accounts)
					{
					
					for(account findaccount: bankmanager.bank.accounts)
					{
						if(findaccount.accountnumber == i || i == 0)
						{ 
							log("account already exists please pick another one");
							unique = false;
						}
						
					}
				    }
				
			}while(unique == false);
		
			bankmanager.bank.accounts.add(new account(currentcustomer, i));
			currentcustomeraccountnumber = i;
			banktellerstate = state.waitingforresponse;
			gui.approved = true;
			timer.schedule(new TimerTask() {
				
				public void run() {
					log("You have opened an account!");
					currentcustomer.msgOpenAccountDone(currentcustomeraccountnumber);
					gui.approved = false;
					//person.stateChanged();
				}
					},
				1 * 1000);
			
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
					
						findaccount.balance += this.deposit;
						log("After depositing, current balance of account number : " + findaccount.accountnumber + " is $" + findaccount.balance);
						log.add(new LoggedEvent("deposit!"));
						gui.approved = true;
						timer.schedule(new TimerTask() {
							
							public void run() {
							currentcustomer.msgDepositIntoAccountDone(deposit);
							gui.approved = false;
							}
								},
							1 * 1000);
					
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
						if(findaccount.loan > findaccount.balance)
						{
							final double amounttopayback = findaccount.loan;
							gui.denied = true;
							timer.schedule(new TimerTask() {
								
								public void run() {
								log("withdrawal failed due to excessive loan");
								currentcustomer.msgPayLoan(amounttopayback);
								gui.denied = false;
								}
									},
								1 * 1000);
						
						}
						else if(findaccount.balance > withdrawal)
						{
							
						
							findaccount.balance -= this.withdrawal;
							gui.approved = true;
							timer.schedule(new TimerTask() {
								
								public void run() {
								currentcustomer.msgHereIsYourWithdrawal(withdrawal);	
								}
									},
								1 * 1000);
							break;
						}
						else if(findaccount.balance < withdrawal && findaccount.balance != 0)
						{
							gui.approved = true;
							final double partialwithdrawal = findaccount.balance;
							findaccount.balance = 0;
							log("Your account does not exactly have what you requested but this is what you have in your account so I'm going to give it to you $" + partialwithdrawal);
							timer.schedule(new TimerTask() {
								
								public void run() {
								currentcustomer.msgHereIsYourWithdrawal(partialwithdrawal);	
								}
									},
								1 * 1000);
							break;
						}
						else
						{
							gui.denied = true;
							timer.schedule(new TimerTask() {
								
								public void run() {
								log("withdrawal failed");
								currentcustomer.msgWithdrawalFailed(withdrawal);
								gui.denied = false;
								}
									},
								1 * 1000);
						
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
				//Do("I'm in the if statement");

				for(account findaccount: bankmanager.bank.accounts)
				{
					if(findaccount.accountnumber == currentcustomeraccountnumber)
					{        

						if(findaccount.balance < findaccount.loan)
						{
							
							gui.denied = true;
							timer.schedule(new TimerTask() {
								
								public void run() {
								log("You cannot get loan");
								currentcustomer.msgCannotGetLoan(loan);
								gui.denied = false;
								}
									},
								1 * 1000);
							
							
						}
						else
						{
							bankmanager.bank.resetBankAsset(-this.loan);
							findaccount.loan += this.loan;
							final double loanadded = this.loan;
							gui.approved = true;
							timer.schedule(new TimerTask() {
								
								public void run() {
								log("loan approved");
								currentcustomer.msgHereIsYourLoan(loanadded);	
								}
									},
								1 * 1000);
								
						}


					}
				}

			}
			banktellerstate = state.waitingforresponse;
			return true;


		}


		if(banktellerstate == state.servingcustomer && banktellerevent == event.paybackloan)
		{

			synchronized(bankmanager.bank.accounts)
			{

				for(final account findaccount: bankmanager.bank.accounts)
				{
					if(findaccount.accountnumber == currentcustomeraccountnumber)
					{        

						bankmanager.bank.resetBankAsset(paybackloan);
						findaccount.loan -= paybackloan;
						gui.approved = true;
						timer.schedule(new TimerTask() {
							
							public void run() {
							log("Loan approved!");
							currentcustomer.msgLoanPaidBack(paybackloan, findaccount.loan);	
							}
								},
							1 * 1000);
						
						/*

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
						*/

					}
				}
				banktellerstate = state.waitingforresponse;
				return true;

			}

		}


		if(banktellerstate == state.waitingforresponse && banktellerevent == event.customerleft)
		{
			
			log("Manager, I'm free");
			//bankmanager.msgCustomerLeft(currentcustomer, this);
			bankmanager.msgBankTellerFree(this);
			this.currentcustomer = null;
			gui.approved = false;
			gui.denied = false;
			gui.bankTellerOccupied = false;
			banktellerstate = state.atstation;

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
		ActivityLog.getInstance().logActivity(tag, msg, name, false);
	}

	@Override
	public PersonAgent getPerson() {
		return person;
	}



}