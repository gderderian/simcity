package city;

import java.util.ArrayList;
import java.util.List;

import Role.BankCustomerRole;


public class account {


	public double balance;
	public int accountnumber;
	BankCustomerRole customer;
	public double loan;
	public double interestrate;
	public List<loan> loans; 
	double totalloan;

	public account(BankCustomerRole customer, int assignaccountnumber)
	{
		this.customer = customer;
		this.accountnumber = assignaccountnumber;
		this.balance = 0;
		this.loan = 0;
		this.interestrate = 1.01;
		totalloan = 0;
		loans = new ArrayList<loan>();

	}
	
	public void addloan(double loanamount)
	{
		loans.add(new loan(loanamount));
	}
	
	public void raiseinterestrateonloan()
	{
		for(int i = 0; i < loans.size(); i++)
		{
			loans.get(i).addinterestonloan();
		}	
	}
	
	public double calculatetotalloan()
	{
		for(int i = 0; i < loans.size(); i++)
		{
			totalloan += loans.get(i).loanamount;
		}
		
		return totalloan;
		
		
	}
	
	
	
	public class loan {
		public double loanamount;
		public double lendtime;
		public double interestrate;
		
		public loan(double setloanamount)
		{
			loanamount = setloanamount;
			lendtime = 0;
			interestrate = 1.01;	
		}
		
		public void addinterestonloan()
		{
			loanamount *= interestrate;
			interestrate *= .02;
			lendtime++;
		}
		
	}
	
	
	
}
