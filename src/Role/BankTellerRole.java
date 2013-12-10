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
		log("Customer wants to deposit into an account");
		log.add(new LoggedEvent("msgDepositIntoAccount"));
		this.deposit = deposit;
		banktellerevent = event.depositintoaccount;
		banktellerstate = state.servingcustomer;
		person.stateChanged();
	}

	public void msgWithdrawFromAccount(double withdrawal)
	{
		log("Customer wants to withdraw from account");
		log.add(new LoggedEvent("msgWithdrawFromAccount"));
		this.withdrawal = withdrawal/2;
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

		Do("Customer wants to pay back his loan");
		this.paybackloan = paybackloan;
		banktellerevent = event.paybackloan;
		banktellerstate = state.servingcustomer;
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
		if(bankmanager.bank.accounts.size() > 0)
			{
			for(account displayaccount: bankmanager.bank.accounts )
			{
				log("Acccount " + displayaccount.accountnumber + " balance :" + displayaccount.balance + " loan : " + displayaccount.loan);
			}
		}
		
		log("!!!!!!!!!!!!!!!!!!!! banktellerrole scheduler  state " + banktellerstate + "   event " + banktellerevent);
		
		if(banktellerstate == state.arrived && banktellerevent == event.gotobanktellerstation)
		{
			guiGoToBankTellerStation(stationnumber);
			log("I have arrived at station " + stationnumber);
			banktellerstate = state.atstation;
			return true;
		}
		
		if(banktellerstate == state.servingcustomer && banktellerevent == event.openaccount)
		{
			
			log("customer is opening account");
			boolean unique;
			int i;
			//boolean once = false;
			
			do {
					Random r = new Random();
        			i = r.nextInt(50); 
					unique = true;
					//if(once == false)
					//{
						//i = 1;
						//once = true;
					//}
					
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
			
			int setaccountnumber = i;
			
			bankmanager.bank.accounts.add(new account(currentcustomer, i));
			currentcustomeraccountnumber = i;
			//bankmanager.bank.uniqueaccountnumber++;
			banktellerstate = state.waitingforresponse;
			gui.approved = true;
			timer.schedule(new TimerTask() {
				
				public void run() {
					currentcustomer.msgOpenAccountDone(currentcustomeraccountnumber);
					gui.approved = false;
					//person.stateChanged();
				}
					},
				1 * 1000);
			
		   return true;
		}
		
		/*
		if(banktellerstate == state.servingcustomer && banktellerevent == event.openaccount)
		{
			log("customer is opening account");
			bankmanager.bank.accounts.add(new account(currentcustomer, bankmanager.bank.uniqueaccountnumber));
			currentcustomeraccountnumber = bankmanager.bank.uniqueaccountnumber;
			bankmanager.bank.uniqueaccountnumber++;
			banktellerstate = state.waitingforresponse;
			gui.approved = true;
			timer.schedule(new TimerTask() {
				
				public void run() {
					currentcustomer.msgOpenAccountDone(currentcustomeraccountnumber);
					gui.approved = false;
					//person.stateChanged();
				}
					},
				3 * 1000);
			
		   return true;
		}
		*/

		if(banktellerstate == state.servingcustomer && banktellerevent == event.depositintoaccount)
		{
			
			Do("!!!!!!!!!!!!! I'm in the if statement");
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
						gui.approved = true;
						timer.schedule(new TimerTask() {
							
							public void run() {
							log("!!!!!!!! deposit" + deposit);
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
						else if(!(findaccount.balance < withdrawal))
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
								log("loan failed");
								currentcustomer.msgCannotGetLoan(loan);
								gui.denied = false;
								}
									},
								1 * 1000);
							
							
						}
						else
						{
							findaccount.loan += this.loan;
							gui.approved = true;
							timer.schedule(new TimerTask() {
								
								public void run() {
								log("loan approved");
								currentcustomer.msgHereIsYourLoan(loan);	
								}
									},
								1 * 1000);
								
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

				for(final account findaccount: bankmanager.bank.accounts)
				{
					if(findaccount.accountnumber == currentcustomeraccountnumber)
					{        

						
						findaccount.loan -= paybackloan;
						gui.approved = true;
						timer.schedule(new TimerTask() {
							
							public void run() {
							log("loan approved");
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
			
			Do("!!!!!!!!!!!!!!!!!!!!! i'm in if customer left statement");
			//bankmanager.msgCustomerLeft(currentcustomer, this);
			bankmanager.msgBankTellerFree(this);
			this.currentcustomer = null;
			gui.approved = false;
			gui.denied = false;
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
		ActivityLog.getInstance().logActivity(tag, msg, name, false);
	}

	@Override
	public PersonAgent getPerson() {
		return person;
	}



}