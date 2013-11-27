package hollytesting.test;

import hollytesting.test.mock.MockHouse;
import hollytesting.test.mock.MockLandlord;
import city.House;
import city.PersonAgent;
import city.PersonAgent.FoodState;
import city.gui.House.HouseAnimationPanel;
import junit.framework.TestCase;

public class PersonAgentHousingTest extends TestCase {
        
        PersonAgent person;
        MockHouse house;
        MockLandlord landlord;
        
        public void setUp() throws Exception{
                super.setUp();                
                person = new PersonAgent("Person");
                house = new MockHouse("House", person);
                landlord = new MockLandlord("Landlord");
                person.house = house;
        }
        
        public void testGettingFoodNormal(){
                
        	//TODO fix this for the test
                //Check the fridge in the house for chicken
                assertEquals("Persons meals list shouldn't have any meals in it", person.meals.size(), 0);
                house.checkFridge("Chicken");
                assertTrue("The person should have a meal in it with the state initial", person.meals.get(0).state == FoodState.initial);
                assertTrue("The person should have a meal in it with the type 'Chicken'", person.meals.get(0).type.equals("Chicken"));
                person.pickAndExecuteAnAction();
                assertTrue("Person should have an event logged 'Cooking meal'", person.log.containsString("Cooking meal"));
                house.cookFood("Chicken");
                assertTrue("Person should have logged an event 'Recieved message food is done'", person.log.containsString("Recieved message food is done"));
                assertTrue("Person should have a meal with the state done", person.meals.get(0).state == FoodState.done);
                person.pickAndExecuteAnAction();
                assertTrue("Person should have logged an event 'Eating meal'", person.log.containsString("Eating meal"));
                
        }
        
        public void testPayRentNormal(){
                
        		person.landlord = landlord;
        		assertTrue("The person should have greater than 10 dollars in their wallet", person.wallet > 10.00);
                assertEquals("Person should have no bills in their billsToPay list", person.billsToPay.size(), 0);
                person.msgRentDue(landlord, 10.00);
                assertEquals("Person should have one bill in their billsToPay list", person.billsToPay.size(), 1);
                assertTrue("The person should have a bill in it where the payTo field is to the landlord", person.billsToPay.get(0).landlord == person.landlord);
                person.pickAndExecuteAnAction();
                assertTrue("The person should have logged that they're paying a bill", person.log.containsString("Paying bill"));
                assertTrue("The person should have logged that they're paying their rent", person.log.containsString("The bill I'm paying is my rent"));
                
        }
        
}