package BankTest;

import astar.AStarTraversal;
import junit.framework.TestCase;
import city.Bank;
import city.CityMap;
import city.House;
import city.account;
import Role.BankCustomerRole;
import Role.BankManagerRole;
import Role.BankTellerRole;
import city.PersonAgent;

// this test is for two customers and two bank tellers I'm verifying whether the customers are being 
//assigned to the correct bank tellers. 

public class BankTest3 extends TestCase {
        
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
        House house = new House("house1");
        CityMap citymap = new CityMap();
        
        public void setUp() throws Exception{
                super.setUp();
                bank = new Bank();
                
                
                person1 = new PersonAgent("bob", aStarTraversal, citymap, house);
                person2 = new PersonAgent("tom", aStarTraversal, citymap, house);
                person3 = new PersonAgent("manaager", aStarTraversal, citymap, house);
                person4 = new PersonAgent("tom", aStarTraversal, citymap, house);
                person5 = new PersonAgent("manaager", aStarTraversal, citymap, house);
                
                bankmanager = new BankManagerRole(bank);
                bankmanager.setPerson(person1);
                bankcustomer = new BankCustomerRole(50);
                bankcustomer.setPerson(person2);
                bankcustomer.bankaccountnumber = 0;
                
                //create an account with account number 0 and loan $20
                bankmanager.bank.accounts.add(new account(bankcustomer, 0));
                bank.accounts.get(0).loan = 20;
                
                bank.accounts.get(0).addloan(10);
                bank.accounts.get(0).loans.get(0).loanamount = 10;
                
                bankteller = new BankTellerRole(bankmanager);
                bankteller.setPerson(person4);
          
        }        

        public void testOneNormalCustomerScenario()
        {
                //intitial set up
                bankmanager.msgBankTellerArrivedAtBank(bankteller);
                assertEquals("BankTellerRole should have an empty event log: "
                        + bankteller.log.toString(), 0, bankteller.log.size());
                assertEquals("BankCustomerRole should have an empty event log: "
                        + bankcustomer.log.toString(), 0, bankcustomer.log.size());
                
                assertEquals("bank account wiht account number 0 should have one loan of $10",bank.accounts.get(0).loans.get(0).loanamount, 10.0);
                
                assertTrue(bankmanager.pickAndExecuteAnAction());
                assertTrue(" " + bankmanager.log.getLastLoggedEvent().toString(), bankmanager.log.containsString("bankstationassigned")); 
                assertEquals("bank should have 1 bankteller in it.",bankmanager.banktellers.size(), 1);        
               
                bankmanager.msgCustomerArrivedAtBank(bankcustomer);
                assertEquals("bank should have 1 bankcustomers in it.",bankmanager.customers.size(), 1);        
                assertTrue(bankmanager.pickAndExecuteAnAction());
                assertTrue(" " + bankmanager.log.getLastLoggedEvent().toString(), bankmanager.log.containsString("banktellerassigned"));
                assertEquals(bankteller.currentcustomer, bankcustomer);        
                bankteller.msgPayBackLoan(10);
                
                bankteller.pickAndExecuteAnAction();
                assertEquals("the loan is paid off there should be no more loans in the account",bank.accounts.get(0).loans.size(), 0);
                assertTrue(" " + bankcustomer.log.getLastLoggedEvent().toString(), bankcustomer.log.containsString("msgLoanPaidBack"));
                
                    
        
                
        }//end one normal customer scenario


}