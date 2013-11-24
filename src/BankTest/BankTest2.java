package BankTest;

import astar.AStarTraversal;
import junit.framework.TestCase;
import city.Bank;
import city.account;
import Role.BankCustomerRole;
import Role.BankManagerRole;
import Role.BankTellerRole;
import city.PersonAgent;

// this test is for two customers and two bank tellers I'm verifying whether the customers are being 
//assigned to the correct bank tellers. 

public class BankTest2 extends TestCase {
        
        Bank bank;
        BankManagerRole bankmanager;
        BankTellerRole bankteller;
        BankTellerRole bankteller2;
        BankCustomerRole bankcustomer;
        BankCustomerRole bankcustomer2;
        PersonAgent person1;
        PersonAgent person2;
        PersonAgent person3;
        PersonAgent person4;
        PersonAgent person5;
        AStarTraversal aStarTraversal;
        
        public void setUp() throws Exception{
                super.setUp();
                bank = new Bank();
                
                person1 = new PersonAgent("bob", aStarTraversal, null);
                person2 = new PersonAgent("tom", aStarTraversal, null);
                person3 = new PersonAgent("manaager", aStarTraversal, null);
                person4 = new PersonAgent("tom", aStarTraversal, null);
                person5 = new PersonAgent("manaager", aStarTraversal, null);
                
                
                bankmanager = new BankManagerRole(bank);
                bankmanager.setPerson(person1);
                bankcustomer = new BankCustomerRole(50, person2);
                bankcustomer2 = new BankCustomerRole(50, person3);
                bankteller = new BankTellerRole(bankmanager);
                bankteller.setPerson(person4);
                bankteller2 = new BankTellerRole(bankmanager);
                bankteller2.setPerson(person5);
        }        

        public void testOneNormalCustomerScenario()
        {
                //intitial set up
                bankmanager.msgBankTellerArrivedAtBank(bankteller);
                bankmanager.msgBankTellerArrivedAtBank(bankteller2);
                assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsCheck is called. Instead, the Cashier's event log reads: "
                        + bankteller.log.toString(), 0, bankteller.log.size());
                assertTrue(bankmanager.pickAndExecuteAnAction());
                assertTrue(" " + bankmanager.log.getLastLoggedEvent().toString(), bankmanager.log.containsString("bankstationassigned"));
                assertTrue(bankmanager.pickAndExecuteAnAction());
                assertTrue(" " + bankmanager.log.getLastLoggedEvent().toString(), bankmanager.log.containsString("bankstationassigned")); 
                assertEquals("bank should have 2 banktellers in it.",bankmanager.banktellers.size(), 2);        
                //bankmanager.pickAndExecuteAnAction();
                
               
                bankmanager.msgCustomerArrivedAtBank(bankcustomer);
                bankmanager.msgCustomerArrivedAtBank(bankcustomer2);
                assertEquals("bank should have 2 bankcustomers in it.",bankmanager.customers.size(), 2);        
                assertTrue(bankmanager.pickAndExecuteAnAction());
                assertTrue(" " + bankmanager.log.getLastLoggedEvent().toString(), bankmanager.log.containsString("banktellerassigned"));
                assertEquals(bankteller.currentcustomer, bankcustomer);        
             
                bankmanager.pickAndExecuteAnAction();
                assertTrue(" " + bankmanager.log.getLastLoggedEvent().toString(), bankmanager.log.containsString("banktellerassigned"));
                assertEquals(bankteller2.currentcustomer, bankcustomer2);        
                
                
        
                
        }//end one normal customer scenario


}