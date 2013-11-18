package city;
/*
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import agent.Agent;
import city.account;
import Role.BankTellerRole;
import Role.BankCustomerRole;

public class BankAgent extends Agent{

	public enum banktellerstate {free, busy};
	public enum bankstate {createaccount, depositintoaccount, withdrawfromaccount, getloan};
	public enum customerstate {waiting, beingserved, leaving};
	String name;
	public static int uniqueaccountnumber = 0;
	public Semaphore accessingaccount = new Semaphore(0,true);

	public List<mybankteller> banktellers = new ArrayList<mybankteller>();
	public List<mycustomer> customers = new ArrayList<mycustomer>();
	public List<account> accounts = new ArrayList<account>();
	bankstate state;

<<<<<<< HEAD
*/
=======
	public BankAgent(String name)
	{
		super();
		this.name = name;

>>>>>>> new update

	}

	public void msgCustomerArrivedAtBank(BankCustomerRole newcustomer)
	{
		customers.add(new mycustomer(newcustomer));
		stateChanged();
	}

	public void msgBankTellerArrivedAtBank(BankTellerRole newbankteller)
	{
		banktellers.add(new mybankteller(newbankteller, this));
		stateChanged();
	}




	/*
public void msgCreateNewAccount(BankCustomerRole customer)
{
	accounts.add(new account(customer, uniqueaccountnumber));
	uniqueaccountnumber++;
	state = bankstate.createaccount;
	stateChanged();	
}
<<<<<<< HEAD
*/
/*
public void msgCustomerLeft(BankCustomerRole leavingcustomer)
{
	customers.remove(leavingcustomer);
	stateChanged();
}
=======
	 */

	public void msgCustomerLeft(BankCustomerRole leavingcustomer)
	{
		customers.remove(leavingcustomer);
		stateChanged();
	}
>>>>>>> new update


	public void msgBankTellerFree(BankTellerRole bankteller)
	{
		for(mybankteller freebankteller: banktellers)
		{
			if(freebankteller.bankteller == bankteller)
			{
				freebankteller.state = banktellerstate.free;
				break;
			}
		}
		stateChanged();
	}


	//Scheduler
	//interest rate implementation

	@Override
	protected boolean pickAndExecuteAnAction() {


		for(mycustomer customer: customers)
		{
			if(customer.state == customerstate.waiting)
			{
				for(mybankteller bankteller: banktellers)
				{
					if(bankteller.state == banktellerstate.free)
					{
						bankteller.bankteller.msgAssignMeCustomer(customer.customer);
						bankteller.state = banktellerstate.busy;
						break;
					}
				}

				return true;	
			}



			if(customer.state == customerstate.leaving)
			{
				customers.remove(customer);
				return true;
			}

		}

		return false;

	}



	class mybankteller {

		BankTellerRole bankteller;
		banktellerstate state;

		public mybankteller(BankTellerRole bt, BankAgent bank)
		{
			bankteller = bt;
			bankteller.bank = bank;
			state = banktellerstate.free;
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


}
<<<<<<< HEAD
<<<<<<< HEAD
*/
=======







>>>>>>> new update
=======
*/
>>>>>>> my agent files
