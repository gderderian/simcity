package city;

import Role.BankCustomerRole;


public class account {


	public double balance;
	public int accountnumber;
	BankCustomerRole customer;
	public double loan;

	public account(BankCustomerRole customer, int assignaccountnumber)
	{
		this.customer = customer;
		this.accountnumber = assignaccountnumber;
		this.balance = 0;
		this.loan = 0;

	}

	
	
	
}
