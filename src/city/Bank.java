package city;

import java.util.ArrayList;
import java.util.List;
import Role.PersonAgent;

import Role.BankManagerRole;

public class Bank {

	BankManagerRole bankmanager;
	public List<account> accounts;
	public static int uniqueaccountnumber = 0;
	
	public Bank() {
		
		accounts = new ArrayList<account>();
		bankmanager = new BankManagerRole(this);
	}
	
	public BankManagerRole getBankManager()
	{
		return bankmanager;
	}
	
	
}
