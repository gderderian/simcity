package Role;

import interfaces.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import activityLog.ActivityLog;
import activityLog.ActivityTag;
import city.Bank;
//import restaurant.BankAgent.bankstate;
import city.account;
import city.gui.Bank.BankManagerRoleGui;
import Role.BankManagerRole.customerstate;
import Role.BankTellerRole;
import Role.BankCustomerRole;
import Role.Role;
import test.mock.EventLog;
import test.mock.LoggedEvent;
import city.PersonAgent;

public class BankManagerRole extends Role{
	
	String roleName = "BankManagerRole";

	public enum banktellerstate {arrived, free, busy};
	//public enum bankstate {createaccount, depositintoaccount, withdrawfromaccount, getloan, calculateloan, customerleft};
	public enum bankmanagerstate {doingnothing, assignbanktellertostation, calculateloan, customerleft, endoftheday, bankrobberarrived};
	public enum customerstate {waiting, beingserved, leaving};
	public String name;
	public Semaphore accessingaccount = new Semaphore(0,true);
	public Semaphore atBankStation = new Semaphore(0,true);

	public List<mybankteller> banktellers = new ArrayList<mybankteller>();
	public List<mycustomer> customers = new ArrayList<mycustomer>();

	bankmanagerstate state;
	public Bank bank;
	BankCustomerRole leavingcustomer;
	BankTellerRole freebankteller;
	PersonAgent person;
	public EventLog log = new EventLog();
	BankRobberRole bankrobber;
	
	ActivityTag tag = ActivityTag.BANKMANAGER;



	public BankManagerRole(Bank setbank)
	{
		super();
		this.bank = setbank;
		building = "bank1";
		

	}

	public void msgCustomerArrivedAtBank(BankCustomerRole newcustomer)
	{
		log("new customer arrived");
		customers.add(new mycustomer(newcustomer));
		person.stateChanged();

	}

	public void msgBankTellerArrivedAtBank(BankTellerRole newbankteller)
	{
		log("new bankteller arrived");
		banktellers.add(new mybankteller(newbankteller, this));
		log("" + banktellers.size());
		person.stateChanged();
	}
	
	public void msgEndOfTheDay()
	{
		state = bankmanagerstate.endoftheday;
		person.stateChanged();	
	}
	
	
	public void msgBankRobberArrived(BankRobberRole setbankrobber) {
		
		bankrobber = setbankrobber;
		state = bankmanagerstate.bankrobberarrived;
		person.stateChanged();
		
	}

	public void msgCalculateLoan() {
		state = bankmanagerstate.calculateloan;
		person.stateChanged();

	}


	public void msgCustomerLeft(BankCustomerRole leavingcustomer, BankTellerRole bankteller)
	{
		log.add(new LoggedEvent("msgCustomerLeft"));
		Do("customer just left");
		this.leavingcustomer = leavingcustomer;
		synchronized(customers)
		{

			for(mycustomer customer: customers)
			{
				if(customer.customer == this.leavingcustomer)
				{
					
					customers.remove(customer);
					log.add(new LoggedEvent("customerremoved"));

				}

			}

		}
		this.freebankteller = bankteller;
		state = bankmanagerstate.customerleft;
		person.stateChanged();
	}


	public void msgBankTellerFree(BankTellerRole bankteller)
	{
		synchronized(banktellers)
		{

			for(mybankteller freebankteller: banktellers)
			{

				if(freebankteller.bankteller == bankteller)
				{
					log.add(new LoggedEvent("msgBankTellerFree"));
					Do("bankteller is free");
					freebankteller.state = banktellerstate.free;
					break;
				}
			}
			person.stateChanged();
		}
	}


	//Scheduler
	//interest rate implementation


	public boolean pickAndExecuteAnAction() {

		
		log("!!!!!!!!!!!!!!!!!!!!!! size of banktelelrs" + banktellers.size());
		
		if(state == bankmanagerstate.bankrobberarrived)
		{
			
			
			Do("????????????????????????????  Bank Robber arrived ");
			bankrobber.msgGoToBankChamber();	
			
			
			
			
			return false;
		}
		
		
		//Do("im in the scheduler");
		//Do("" + banktellers.size());

		//Do("!!!!!!!!!!!!!!! bank manager gui " + gui);
		synchronized(banktellers)
		{

			for(mybankteller newbankteller: banktellers)
			{
				if(newbankteller.state == banktellerstate.arrived)
				{
					synchronized(bank.bankstations)
					{

						for(Bank.bankstation findfreebankstation : bank.bankstations)
						{

							if(!findfreebankstation.isOccupied())
							{								
								Do("assign bankteller  to station " + findfreebankstation.stationnumber);
								log.add(new LoggedEvent("bankstationassigned"));
								findfreebankstation.setBankTeller(newbankteller.bankteller);
								newbankteller.setBankStationNumber(findfreebankstation.stationnumber);
								//animation stuff
								newbankteller.bankteller.msgGoToBankTellerStation(findfreebankstation.stationnumber);
								newbankteller.state = banktellerstate.free;
								return true;
							}

						}

					}

				}

			}

		}


		synchronized(customers)
		{

			for(mycustomer customer: customers)
			{
				if(customer.state == customerstate.waiting)
				{
					synchronized(banktellers)
					{

						for(mybankteller bankteller: banktellers)
						{
							if(bankteller.state == banktellerstate.free)
							{
								log.add(new LoggedEvent("banktellerassigned"));
								bankteller.bankteller.msgAssignMeCustomer(customer.customer);
								Do("assign bankteller to customer: " + customer.customer.person.getName());
								customer.customer.msgAssignMeBankTeller(bankteller.bankteller);
								//new line
								customers.remove(customer);
								customer.state = customerstate.beingserved;
								bankteller.state = banktellerstate.busy;
								//customer.customer.msgOpenAccount();
								return true;
							}
							else
							{
								customer.customer.msgGoToWaitingArea(160, 500);
					
								
							}
						}

					}


				}
				/*
                        if(customer.state == customerstate.leaving)
                        {
                                customers.remove(customer);
                                return true;
                        }
				 */

			}
		}

		if(state == bankmanagerstate.endoftheday)
		{
			//this is a very simple loan calculation system with some limits

			synchronized(bank.accounts)
			{

				for(account findaccountwithloan: bank.accounts)
				{
					if(findaccountwithloan.loan > 0)
					{
						
						findaccountwithloan.loan *= findaccountwithloan.interestrate;
						findaccountwithloan.interestrate *= .05;
					}
				}

			}
			
			synchronized(banktellers)
			{
				
			
			for(mybankteller bankteller: banktellers)
			{
			
				
				
			}
			
			
			
			}
			
			
			
			
			//this is my new design for loan system
			/*
			synchronized(bank.accounts)
			{

				for(account findaccountwithloan: bank.accounts)
				{
					if(findaccountwithloan.loans.size() !=0)
					{
												
						findaccountwithloan.raiseinterestrateonloan();
					}

				}

			}
			 */
			state = bankmanagerstate.doingnothing;

			return true;
		}
		
		if(state == bankmanagerstate.customerleft)
		{
			/*
			synchronized(customers)
			{

				for(mycustomer leavingcustomer: customers)
				{
					if(leavingcustomer.customer == this.leavingcustomer)
					{
						
						customers.remove(leavingcustomer);
						log.add(new LoggedEvent("customerremoved"));
						//return true;

					}

				}

			}
			*/
			synchronized(banktellers)
			{

				for(mybankteller freebankteller: banktellers)
				{
					if(freebankteller.bankteller == this.freebankteller)
					{

						freebankteller.state = banktellerstate.free;
						log.add(new LoggedEvent("banktellerfree"));
						//return true;
					}
				}

			}

			return true;

		}



		return false;

	}



	class mybankteller {

		BankTellerRole bankteller;
		banktellerstate state;
		int bankstationnumber;

		public mybankteller(BankTellerRole bt, BankManagerRole bm)
		{
			bankteller = bt;
			bankteller.bankmanager = bm;
			state = banktellerstate.arrived;

		}

		public void setBankStationNumber(int setbankstationnumber)
		{
			this.bankstationnumber = setbankstationnumber;
		}

		public int getBankStationNumber()
		{
			return this.bankstationnumber;
		}

	}

	class mycustomer {

		BankCustomerRole customer;
		customerstate state;

		public mycustomer(BankCustomerRole c)
		{
			this.customer = c;
			state = customerstate.waiting;
		}

	}



	//actions



	public void setPerson(PersonAgent person)
	{
		this.person = person;
		this.name = person.getName();
	}

	public void setGui(BankManagerRoleGui bankmanagerGui) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getRoleName() {
		return roleName;
	}
	
	private void log(String msg){
		print(msg);
		ActivityLog.getInstance().logActivity(tag, msg, name, false);
		log.add(new LoggedEvent(msg));
	}

	@Override
	public PersonAgent getPerson() {
		return person;
	}



}


