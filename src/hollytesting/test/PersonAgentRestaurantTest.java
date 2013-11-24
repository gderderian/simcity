package hollytesting.test;

import test.mock.EventLog;
import city.CityMap;
import city.PersonAgent;
import city.Restaurant2.Restaurant2;
import city.Restaurant2.Restaurant2CookRole;
import city.Restaurant2.Restaurant2CustomerRole;
import city.Restaurant2.Restaurant2CustomerRole.AgentEvent;
import city.Restaurant2.Restaurant2HostRole;
import city.Restaurant2.Restaurant2WaiterRole;
import city.gui.PersonGui;
import junit.framework.TestCase;
import city.Restaurant2.Restaurant2HostRole.CustomerState;

public class PersonAgentRestaurantTest extends TestCase{

        PersonAgent person;        //this will be the customer
        PersonAgent hostPerson;
        PersonAgent waiterPerson;
        PersonAgent cookPerson;
        Restaurant2HostRole host;
        Restaurant2CustomerRole customer;
        Restaurant2WaiterRole waiter;
        Restaurant2CookRole cook;
        public EventLog log;
        PersonGui gui;
        CityMap cityMap = new CityMap();
        Restaurant2 rest2 = new Restaurant2();
        
        public void setUp() throws Exception{
                super.setUp();                
                person = new PersonAgent("Person");
                hostPerson = new PersonAgent("hostPerson");
                host = new Restaurant2HostRole("Host", hostPerson);        
                waiterPerson = new PersonAgent("waiterPerson");
                waiter = new Restaurant2WaiterRole("Waiter", waiterPerson);
                cookPerson = new PersonAgent("cookPerson");
                cook = new Restaurant2CookRole("Cook", cookPerson);
                gui = new PersonGui(person);
                cityMap.setRestaurant2(rest2);
        }
        
        public void testPersonNormalRestaurant(){
                
                hostPerson.addRole(host, true);
                waiterPerson.addRole(waiter, true);
                cookPerson.addRole(cook, true);
                
                person.setGui(gui);
                assertEquals("Person should zero roles in the Roles list, but it doesn't", person.roles.size(), 0);
                
                //Set the person to hungry so that they try to eat
                person.msgImHungry();
                
                assertTrue("Person should have logged an event that they recieved the 'I'm hungry' message, but it reads instead " +
                                person.log.getLastLoggedEvent().toString(), person.log.containsString("Recieved message Im Hungry"));
                assertTrue("Person's scheduler should have returned true to deal with the hungry message, but it didn't", person.pickAndExecuteAnAction());
                assertTrue("Person should have logged an event that they are going to a restaurant, but instead it reads " + 
                                person.log.getLastLoggedEvent().toString(), person.log.containsString("Decided to go to a restaurant"));
                
                //Add customer role
                customer = new Restaurant2CustomerRole("Customer", person);
                person.addRole(customer, true);
                assertEquals("Person should have one role in their roles list.", person.roles.size(), 1);
                
                //Pre-test the host conditions
                host.addWaiters(waiter);
                assertEquals("The host should have no customers in its list yet, but it does", host.customers.size(), 0);
                assertEquals("The host should have one waiter in its waiters list, but it doesn't.", host.waiters.size(), 1);
                
                //Pre-test waiter conditions
                assertEquals("The waiter should not have customers in its list yet, but it does.", waiter.customers.size(), 0);
                
                //Message the host that the customer wants food
                host.msgIWantFood(customer);
                //customer.state = AgentState.WaitingInRestaurant;        //This normally gets changed in I'm hungry message
                assertTrue(host.log.containsString("Customer is hungry"));
                assertEquals("Host should have a person in its list", host.customers.size(), 1);
                assertTrue("Host should have a customer whose state is hungry", host.customers.get(0).cs == CustomerState.hungry);
                
                //Test that normal scenario is running properly
                assertTrue("Host person's scheduler should return true because it's dealing with the host role's scheduler",
                                hostPerson.pickAndExecuteAnAction());
                assertEquals("Host should have one person on its list, but doesn't.", host.customers.size(), 1);
                assertTrue("The host should have logged an event to say that it is seating the customer, but instead it says " + 
                                host.log.getLastLoggedEvent().toString(), host.log.containsString("Now seating customer"));
                assertEquals("The waiter should contain one customer in its list, but it doesn't.", waiter.customers.size(), 1);
                waiter.pickAndExecuteAnAction();
                assertTrue("The waiter's event log should contain a log saying that he's prompting the customer to sit, but instead it reads " + 
                                waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Prompting customer to follow me to table"));
                assertTrue("The customer's event log should contain an event that he recieved the follow me message, but instead it says " + 
                                customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Recieved message follow waiter to table"));
                assertEquals("The customer's waiter should be the same as the waiter who sent the message, but it's not.", waiter, customer.waiter);
                assertTrue(customer.event == AgentEvent.followWaiter);
                
                //This code doesnt work because there's gui involved - need to either use mocks or not test for now
/*                customer.pickAndExecuteAnAction();
                assertTrue("The customer's event log should state that it's ready to be seated, but instead it says " + 
                                customer.log.getLastLoggedEvent().toString(), customer.log.containsString("I'm ready to be seated"));
                assertTrue("The waiter's log should state that it's seating the customer, but instead it says " + 
                                waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Seating customer"));
                assertTrue("The customer's log should state that it's sitting down at table 1, but instead it reads " + 
                                customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Sitting down at table 1"));
                assertTrue("The waiter should contain a customer who's state is 'ready to orer', but it doesn't.", waiter.customers.get(0).s == CustomerState.askedToOrder);
                assertTrue(customer.log.containsString("Recieved message what do you want."));
                
                //Message waiter so customer doesn't have to choose
                waiter.msgHereIsMyChoice(customer, "Chicken");
                */
                
        }
        
}