package Role;

import interfaces.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;







import city.Bank;
//import restaurant.BankAgent.bankstate;
import city.account;
import city.gui.Bank.BankManagerRoleGui;
import Role.BankTellerRole;
import Role.BankCustomerRole;
import Role.Role;
import test.mock.EventLog;
import test.mock.LoggedEvent;
import city.PersonAgent;

public class BankManagerRole extends Role{

        public enum banktellerstate {arrived, free, busy};
        //public enum bankstate {createaccount, depositintoaccount, withdrawfromaccount, getloan, calculateloan, customerleft};
        public enum bankmanagerstate {doingnothing, assignbanktellertostation, calculateloan, customerleft};
        public enum customerstate {waiting, beingserved, leaving};
        String name;
        public Semaphore accessingaccount = new Semaphore(0,true);
        public Semaphore atBankStation = new Semaphore(0,true);
        
        public List<mybankteller> banktellers = new ArrayList<mybankteller>();
        public List<mycustomer> customers = new ArrayList<mycustomer>();
        
        bankmanagerstate state;
        public Bank bank;
        BankCustomerRole leavingcustomer;
        BankTellerRole freebankteller;
        PersonAgent person;
        public EventLog log = new EventLog();
        


        public BankManagerRole(Bank setbank)
        {
                super();
                this.bank = setbank;
                //this.name = name;

        }

        public void msgCustomerArrivedAtBank(BankCustomerRole newcustomer)
        {
                Do("new customer arrived");
        		customers.add(new mycustomer(newcustomer));
                person.stateChanged();
        		
        }

        public void msgBankTellerArrivedAtBank(BankTellerRole newbankteller)
        {
                Do("new bankteller arrived");
        		banktellers.add(new mybankteller(newbankteller, this));
        		Do("" + banktellers.size());
                person.stateChanged();
        }

        public void msgCalculateLoan() {
                state = bankmanagerstate.calculateloan;
                person.stateChanged();
                
        }


public void msgCustomerLeft(BankCustomerRole leavingcustomer, BankTellerRole bankteller)
{
        log.add(new LoggedEvent("msgCustomerLeft"));
        this.leavingcustomer = leavingcustomer;
        this.freebankteller = bankteller;
        state = bankmanagerstate.customerleft;
        person.stateChanged();
}


public void msgBankTellerFree(BankTellerRole bankteller)
{
                
                for(mybankteller freebankteller: banktellers)
                {
                	
                        if(freebankteller.bankteller == bankteller)
                        {
                                log.add(new LoggedEvent("msgBankTellerFree"));
                                freebankteller.state = banktellerstate.free;
                                break;
                        }
                }
                person.stateChanged();
}


        //Scheduler
        //interest rate implementation


public boolean pickAndExecuteAnAction() {

					
					Do("im in the scheduler");
					Do("" + banktellers.size());
					for(mybankteller newbankteller: banktellers)
					{
						if(newbankteller.state == banktellerstate.arrived)
						{
							
							for(Bank.bankstation findfreebankstation : bank.bankstations)
							{
				
								if(!findfreebankstation.isOccupied())
								{
									Do("assign bankteller  to station");
									log.add(new LoggedEvent("bankteller assigned to station " + findfreebankstation.stationnumber));
									findfreebankstation.setBankTeller(newbankteller.bankteller);
									newbankteller.setBankStationNumber(findfreebankstation.stationnumber);
									//animation stuff
									newbankteller.bankteller.msgGoToBankTellerStation(findfreebankstation.stationnumber);
									//
									newbankteller.state = banktellerstate.free;
									return true;
								}
							
							}
						
						}
						
					}
		
                for(mycustomer customer: customers)
                {
                        if(customer.state == customerstate.waiting)
                        {
                                for(mybankteller bankteller: banktellers)
                                {
                                        if(bankteller.state == banktellerstate.free)
                                        {
                                                log.add(new LoggedEvent("banktellerassigned"));
                                                Do("assign customer to bank teller");
                                                bankteller.bankteller.msgAssignMeCustomer(customer.customer);
                                                customer.customer.msgAssignMeBankTeller(bankteller.bankteller);
                                                Do("assign bankteller to customer:" + customer.customer.mybankteller);
                                                //animation stuff
                                                
                                                //
                                                customer.state = customerstate.beingserved;
                                                bankteller.state = banktellerstate.busy;
                                                //customer.gui.goToBankTellerStation(bankteller.bankstationnumber);
                                                return true;
                                        }
                                }

                        
                        }
                        /*
                        if(customer.state == customerstate.leaving)
                        {
                                customers.remove(customer);
                                return true;
                        }
                        */

                }
                
                if(state == bankmanagerstate.calculateloan)
                {
                        //this is a very simple loan calculation system with some limits
                        for(account findaccountwithloan: bank.accounts)
                        {
                                if(findaccountwithloan.loan > 0)
                                {
                                        findaccountwithloan.loan *= findaccountwithloan.interestrate;
                                        findaccountwithloan.interestrate *= .05;
                                }
                        }
                        
                        //this is my new design for loan system
                        for(account findaccountwithloan: bank.accounts)
                        {
                                if(findaccountwithloan.loans.size() !=0)
                                {
                                        findaccountwithloan.raiseinterestrateonloan();
                                }
                                
                        }
                        
                        
                        state = bankmanagerstate.doingnothing;
                        
                        return true;
                }
                
                if(state == bankmanagerstate.customerleft)
                {
                
                        for(mycustomer leavingcustomer: customers)
                        {
                                if(leavingcustomer.customer == this.leavingcustomer)
                                {
                                        customers.remove(leavingcustomer);
                                        log.add(new LoggedEvent("customerremoved"));
                                        return true;
                                        
                                }
                                
                        }
                        
                        for(mybankteller freebankteller: banktellers)
                        {
                                if(freebankteller.bankteller == this.freebankteller)
                                {
                                        freebankteller.state = banktellerstate.free;
                                        log.add(new LoggedEvent("banktellerfree"));
                                
                                }
                        }
                        
                        return true;
                        
                }

                
                
                return false;

        }



        class mybankteller {

                BankTellerRole bankteller;
                banktellerstate state;
                int bankstationnumber;

                public mybankteller(BankTellerRole bt, BankManagerRole bm)
                {
                        bankteller = bt;
                        bankteller.bankmanager = bm;
                        state = banktellerstate.arrived;
                   
                }
                
                public void setBankStationNumber(int setbankstationnumber)
                {
                	this.bankstationnumber = setbankstationnumber;
                }
                
                public int getBankStationNumber()
                {
                	return this.bankstationnumber;
                }

        }

        class mycustomer {

                BankCustomerRole customer;
                customerstate state;

                public mycustomer(BankCustomerRole c)
                {
                        this.customer = c;
                        state = customerstate.waiting;
                }

        }
        
        
        
        //actions
        
       

        public void setPerson(PersonAgent person)
        {
                this.person = person;
        }
        
        public void setGui(BankManagerRoleGui bankmanagerGui) {
                // TODO Auto-generated method stub
                
        }
        
        
       
}


