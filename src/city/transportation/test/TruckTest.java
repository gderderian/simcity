package city.transportation.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import city.MarketOrder;
import city.OrderItem;
import city.PersonAgent;
import city.transportation.CarAgent;
import city.transportation.TruckAgent;
import city.transportation.mock.MockMarketManager;
import city.transportation.mock.MockTransportationPerson;

public class TruckTest extends TestCase {
	//instantiated in setUp()
	MockMarketManager market;
	TruckAgent truck;
	MockTransportationPerson recipient;
	
	List<OrderItem> items;
	MarketOrder order;
	String destination;


	public void setUp() throws Exception {
		super.setUp();
		recipient = new MockTransportationPerson("recipient");
		market = new MockMarketManager();
		truck = new TruckAgent(market, null);
		truck.thisIsATest(); //Disables activity log for test (used in city control panel)
		
		items = new ArrayList<OrderItem>();
		items.add(new OrderItem("item1", 3));
		items.add(new OrderItem("item2", 5));
		destination = "TEST DESTINATION";
		order = new MarketOrder(items, destination, recipient);
		//Created a full market order with items, destination, and recipient
	}	

	/* This tests the truck receiving a single delivery from the market and delivering it */
	public void testTruckDelivery() {		
		//Preconditions
		assertTrue(truck.capacity == 0); //No driver/passengers in delivery truck
		assertEquals(truck.orders.size(), 0); //No orders yet
		
		//Message
		truck.msgPleaseDeliver(order);
		
		//Postconditions
		assertTrue(truck.orders.size() == 1); //Now, 1 order for truck
		
		//Scheduler call - should cause truck to deliver order
		truck.pickAndExecuteAnAction(); 
		
		//Recipient should have gotten an order delivery message - confirm
		assertTrue(recipient.log.getLastLoggedEvent().getMessage() == "Received order from market truck");
		
		//Postconditions
		assertTrue(truck.orders.size() == 1); //Order still exists - must tell market it's finished
		
		//Scheduler call - should let market know order is done
		truck.pickAndExecuteAnAction();
		
		//Market should receive a confirmation message - confirm
		assertTrue(market.log.getLastLoggedEvent().getMessage() == "Truck has finished delivery");
		
		//Postcondition
		assertTrue(truck.orders.size() == 0); //Truck is now finished with its delivery
		
	}
}