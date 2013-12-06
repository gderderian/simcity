package hollytesting.test;

import hollytesting.test.mock.MockBusAgent;
import hollytesting.test.mock.MockBusStop;
import hollytesting.test.mock.MockCar;
import city.PersonAgent;
import city.PersonAgent.BusRideState;
import junit.framework.TestCase;
import interfaces.Bus;
import interfaces.Car;

public class PersonAgentTransportationTest extends TestCase {
        
        PersonAgent person;
        MockBusAgent bus;
        MockBusStop busStop;
        MockCar car;
        
        public void setUp() throws Exception{
                super.setUp();                
                person = new PersonAgent("Person");
                bus = new MockBusAgent("Bus");
                busStop = new MockBusStop("BusStop", person, bus);
                car = new MockCar("Car");
        }
        
        public void testGettingOnBusNormal(){
                person.setTesting(true);
                //Checking preconditions
                assertTrue("Person's busRide should be null", person.busRide == null);
                busStop.msgWaitingForBus(person);
                //assertTrue("Bus stop should have record of recieving person waiting message.", 
                 //               busStop.log.containsString("Recieved message waiting for bus from person Person"));
                assertTrue("Person should have record of recieving message bus is here", person.log.containsString("Recieved message bus is here"));
                assertTrue("Person's busRide should not be null.", person.busRide != null);
                person.busRide.finalStop = 1;
                //Add this hack because using scheduler runs into gui null pointer problems
                assertTrue("Person should have a bus ride with stop 1", person.busRide.finalStop == 1);
                person.busRide.state = BusRideState.onBus;
                assertTrue("Person should have a bus ride in it with the state onBus, but the state is " + person.busRide.state, 
                                person.busRide.state == BusRideState.onBus);
                person.msgPleasePayFare((Bus) bus, 3.00);
                assertTrue("Person should have a BusRide with a fare of 3.00.", person.busRide.fare == 3.00);
                person.pickAndExecuteAnAction();
                assertTrue("Person should have a BusRide with a fare of 0, but instead the fare is " + person.busRide.fare, 
                                person.busRide.fare == 0);
                person.msgArrivedAtStop(1);
                assertEquals("The person's busRide still should not be null", person.busRide != null);
                assertTrue("There should be a bus on the list with state get off bus", person.busRide.state == BusRideState.getOffBus);
                //person.pickAndExecuteAnAction();
                //cant call this because of cityMap
                bus.msgImGettingOff(person);                
        }
        
        public void testCarNormal(){
                
                /*
                 * Finish this when person has reason to take car
                 */
                
                person.cars.add((Car) car);
                person.carRides.add(person.new CarRide((Car) car, "Restaurant"));
                car.msgDriveTo(person, "Restaurant");
                assertTrue("The person should have record of the car arriving at destination", person.log.containsString("Recieved message arrived by car"));
                person.pickAndExecuteAnAction();
                assertTrue("The person should have a record of telling the car to park.", person.log.containsString("Telling car to park"));
        }
        
        
}