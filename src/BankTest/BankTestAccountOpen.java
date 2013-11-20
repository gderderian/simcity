package BankTest;

import junit.framework.TestCase;
import city.BankAgent;
import city.account;
import Role.BankCustomerRole;
import Role.BankTellerRole;

public class BankTestAccountOpen extends TestCase {
	
	BankAgent bank;
	BankTellerRole bankteller;
	BankCustomerRole bankcustomer;
	
	
	public void setUp() throws Exception{
		super.setUp();		
		bank = new BankAgent("bank");
		bankcustomer = new BankCustomerRole();
		bankteller = new BankTellerRole(bank);
	}	

	public void testOneNormalCustomerScenario()
	{
		//intitial set up
		bank.msgBankTellerArrivedAtBank(bankteller);
		assertEquals("bank should have 1 banktellers in it.",bank.banktellers.size(), 1);	
		bankteller.msgAssignMeCustomer(bankcustomer);
		bankteller.msgOpenAccount();
			
		
		
		
	}//end one normal customer scenario


}
