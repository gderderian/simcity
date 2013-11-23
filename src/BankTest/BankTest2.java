package BankTest;

import junit.framework.TestCase;
import city.Bank;
import city.account;
import Role.BankCustomerRole;
import Role.BankManagerRole;
import Role.BankTellerRole;
import Role.PersonAgent;

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
        
        
        public void setUp() throws Exception{
                super.setUp();                
                bankmanager = new BankManagerRole(bank);
                bankcustomer = new BankCustomerRole(50, person2);
                bankcustomer2 = new BankCustomerRole(50, person3);
                bankteller = new BankTellerRole(bankmanager);
                bankteller2 = new BankTellerRole(bankmanager);
        }        

        public void testOneNormalCustomerScenario()
        {
                //intitial set up
                bankmanager.msgBankTellerArrivedAtBank(bankteller);
                bankmanager.msgBankTellerArrivedAtBank(bankteller2);
                bankmanager.msgCustomerArrivedAtBank(bankcustomer);
                bankmanager.msgCustomerArrivedAtBank(bankcustomer2);
                assertEquals("bank should have 2 banktellers in it.",bankmanager.banktellers.size(), 2);        
                
                assertTrue("", bankmanager.pickAndExecuteAnAction());
                
                assertTrue(" " + bankmanager.log.getLastLoggedEvent().toString(), bankmanager.log.containsString("banktellerassigned"));
                assertEquals(bankteller.currentcustomer, bankcustomer);        
                System.out.println(bankcustomer);
                System.out.println(bankcustomer2);
                bankmanager.pickAndExecuteAnAction();
                assertTrue(" " + bankmanager.log.getLastLoggedEvent().toString(), bankmanager.log.containsString("banktellerassigned"));
                System.out.println("bankteller2 " +  bankteller.currentcustomer);
                assertEquals(bankteller2.currentcustomer, bankcustomer2);        
                
                
        
                
        }//end one normal customer scenario


}