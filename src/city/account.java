package city;


public class account {


	double balance;
	int accountnumber;
	BankCustomerRole customer;
	double loan;

	public account(BankCustomerRole customer, int assignaccountnumber)
	{
		this.customer = customer;
		this.accountnumber = assignaccountnumber;
		this.balance = 0;
		this.loan = 0;

	}

	
	
	
}
