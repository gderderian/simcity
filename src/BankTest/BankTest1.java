package BankTest;



import junit.framework.TestCase;
import test.mock.EventLog;
import city.BankAgent;
import city.account;
import Role.BankCustomerRole;
import Role.BankTellerRole;


public class BankTest1 extends TestCase {
	
	BankAgent bank;
	BankTellerRole bankteller;
	BankCustomerRole bankcustomer;
	public EventLog log = new EventLog();
	
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
		bank.msgCustomerArrivedAtBank(bankcustomer);
		assertEquals("bank should have 1 banktellers in it.",bank.banktellers.size(), 1);
		assertEquals("bank should have 1 customer in it", bank.customers.size(), 1);
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsCheck is called. Instead, the Cashier's event log reads: "
				+ bankteller.log.toString(), 0, bankteller.log.size());
		assertTrue("", bank.pickAndExecuteAnAction());
		assertTrue("Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: " 
				+ bank.log.getLastLoggedEvent().toString(), bank.log.containsString("msgOpenAccount"));
		
		
	
		bankteller.msgAssignMeCustomer(bankcustomer);
		bankteller.msgOpenAccount();
		assertTrue("Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: " 
				+ bankteller.log.getLastLoggedEvent().toString(), bankteller.log.containsString("msgOpenAccount"));
		
		assertTrue("", bankteller.pickAndExecuteAnAction());
		
		assertEquals("bank should have 1 account in it",bank.accounts.size(),1);
		
		assertEquals("first bank account should have account number 1",bank.accounts.get(0).accountnumber,0);
		
		
		
		
		
		//bankteller.currentcustomeraccountnumber = 0;
		assertEquals("bank teller should have curentcustomeraccountnumber 0", bankteller.currentcustomeraccountnumber,0);
		bankteller.msgDepositIntoAccount(50);
		assertTrue("Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: " 
				+ bankteller.log.getLastLoggedEvent().toString(), bankteller.log.containsString("msgDepositIntoAccount"));
		//assertEquals("", bankteller.deposit, 50);
		
		assertEquals("bank should have 1 account in it",bank.accounts.size(),1);
		assertTrue("", bankteller.pickAndExecuteAnAction());
		
		assertEquals("first bank account should have account number 1",bank.accounts.get(0).accountnumber,0);
		
		assertTrue("Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: " 
				+ bankteller.log.getLastLoggedEvent().toString(), bankteller.log.containsString("deposit!"));
		//assertEquals("", bankteller.deposit, 50);
	
		for(account findaccount: bank.accounts)
		{
			if(findaccount.accountnumber == bankteller.currentcustomeraccountnumber)
			{
				System.out.println(findaccount.balance);
				assertEquals(findaccount.balance, 50.0);
			}
		}
		
		bankteller.msgWithdrawFromAccount(20);
		assertTrue("Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: " 
				+ bankteller.log.getLastLoggedEvent().toString(), bankteller.log.containsString("msgWithdrawFromAccount"));
		
		assertEquals("", bankteller.withdrawal,20.0);
		assertTrue("", bankteller.pickAndExecuteAnAction());
		
		for(account findaccount: bank.accounts)
		{
			if(findaccount.accountnumber == bankteller.currentcustomeraccountnumber)
			{
				assertEquals(findaccount.balance, 30.0);
			}
		}
		
		
		
		
		
		
		
		
		//bank.accounts.get(0).balance = 50;
		//assertEquals("bank account with account number 0 should have $50 in it",bank.accounts.get(0).balance,50);
		
		
		
		
		

	}//end one normal customer scenario


}
