package BankTest;

import junit.framework.TestCase;
import test.mock.EventLog;
import city.Bank;
import city.account;
import Role.BankCustomerRole;
import Role.BankTellerRole;
import Role.PersonAgent;
import Role.BankManagerRole;

public class BankTest1 extends TestCase {
	
	BankManagerRole bankmanager;
	BankTellerRole bankteller;
	BankCustomerRole bankcustomer;
	PersonAgent person1;
	PersonAgent person2;
	Bank bank;
	
	public EventLog log = new EventLog();
	
	public void setUp() throws Exception{
		
		super.setUp();	
		person1 = new PersonAgent();
		person2 = new PersonAgent();
		bank = new Bank();
		bankmanager = new BankManagerRole(bank);
		bankcustomer = new BankCustomerRole(50, person2);
		bankteller = new BankTellerRole(bankmanager);
	
	}	


	public void testOneNormalCustomerScenario()
	{
		//intitial set up
		bankmanager.msgBankTellerArrivedAtBank(bankteller);
		bankmanager.msgCustomerArrivedAtBank(bankcustomer);
		assertEquals("bank should have 1 banktellers in it.",bankmanager.banktellers.size(), 1);
		assertEquals("bank should have 1 customer in it", bankmanager.customers.size(), 1);
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsCheck is called. Instead, the Cashier's event log reads: "
				+ bankteller.log.toString(), 0, bankteller.log.size());
		assertTrue("", bankmanager.pickAndExecuteAnAction());
		assertTrue("Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: " 
				+ bankmanager.log.getLastLoggedEvent().toString(), bankmanager.log.containsString("banktellerassigned"));
		assertEquals("verify if the customer is assigned to correct bank teller", bankteller.currentcustomer, bankcustomer);
		//bankteller.msgAssignMeCustomer(bankcustomer);
		bankteller.msgOpenAccount();
		assertTrue("Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: " 
				+ bankteller.log.getLastLoggedEvent().toString(), bankteller.log.containsString("msgOpenAccount"));
		
		assertTrue("", bankteller.pickAndExecuteAnAction());
		
		assertEquals("bank should have 1 account in it",bank.accounts.size(),1);
		
		assertEquals("first bank account should have account number 1",bank.accounts.get(0).accountnumber,0);
		
		//bankcustomer.pickAndExecuteAnAction();
		assertTrue(" " + bankcustomer.log.getLastLoggedEvent().toString(), bankcustomer.log.containsString("msgOpenAccountDone"));
		assertEquals("", bankcustomer.bankaccountnumber, 0);
		bankcustomer.pickAndExecuteAnAction();
		assertTrue(" " + bankcustomer.log.getLastLoggedEvent().toString(), bankcustomer.log.containsString("receivedaccountnumber"));
		
		
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
		
		//bankcustomer.msgDepositIntoAccount(50);
		assertTrue(" " + bankcustomer.log.getLastLoggedEvent().toString(), bankcustomer.log.containsString("msgDepositIntoAccountDone"));
		bankcustomer.pickAndExecuteAnAction();
		assertTrue(" " + bankcustomer.log.getLastLoggedEvent().toString(), bankcustomer.log.containsString("successfullydeposittedintoaccount"));
		assertEquals(bankcustomer.amountofcustomermoney, 0.0);
		
		
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
		
		assertTrue(" " + bankcustomer.log.getLastLoggedEvent().toString(), bankcustomer.log.containsString("msgHereIsYourWithdrawal"));
		bankcustomer.pickAndExecuteAnAction();
		assertTrue(" " + bankcustomer.log.getLastLoggedEvent().toString(), bankcustomer.log.containsString("successfullywithdrewfromaccount"));
		assertEquals(bankcustomer.amountofcustomermoney, 20.0);
		
		
		bankteller.msgGetLoan(30);
		assertTrue("Cashier should have logged \"Received ReadyToPay\" but didn't. His log reads instead: " 
				+ bankteller.log.getLastLoggedEvent().toString(), bankteller.log.containsString("msgGetLoan"));
		assertEquals("", bankteller.loan,30.0);
		assertTrue("", bankteller.pickAndExecuteAnAction());
		
		for(account findaccount: bank.accounts)
		{
			if(findaccount.accountnumber == bankteller.currentcustomeraccountnumber)
			{
				assertEquals(findaccount.loan, 30.0);
			}
		}
		
		
		bankteller.msgBankCustomerLeaving();
		assertTrue("" + bankteller.log.getLastLoggedEvent().toString(), bankteller.log.containsString("msgBankCustomerLeaving"));
		assertTrue("", bankteller.pickAndExecuteAnAction());
		
		assertTrue(" " + bankmanager.log.getLastLoggedEvent().toString(), bankmanager.log.containsString("msgCustomerLeft"));
		
		assertTrue("", bankmanager.pickAndExecuteAnAction());
		//assertTrue(" " + bankmanager.log.getLastLoggedEvent().toString(), bankmanager.log.containsString("inif"));
		assertTrue(" " + bankmanager.log.getLastLoggedEvent().toString(), bankmanager.log.containsString("customerremoved"));
		//assertEquals(bank.customers.get(0), bankteller.currentcustomer);
		
		assertEquals(bankmanager.customers.size(), 0);
		
		
		
		//bank.accounts.get(0).balance = 50;
		//assertEquals("bank account with account number 0 should have $50 in it",bank.accounts.get(0).balance,50);
		
		
		
		
		

	}//end one normal customer scenario


}
