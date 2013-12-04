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

public class TruckTest2 extends TestCase {
	//instantiated in setUp()
	MockMarketManager market;
	TruckAgent truck;
	MockTransportationPerson recipient1;
	MockTransportationPerson recipient2;
	MockTransportationPerson recipient3;
	
	List<OrderItem> items;
	MarketOrder order1;
	MarketOrder order2;
	MarketOrder order3;
	String destination1;
	String destination2;
	String destination3;


	public void setUp() throws Exception {
		super.setUp();
		recipient1 = new MockTransportationPerson("recipient1");
		recipient2 = new MockTransportationPerson("recipient2");
		recipient3 = new MockTransportationPerson("recipient3");
		market = new MockMarketManager();
		truck = new TruckAgent(null, null);
		truck.setMarketManager(market);
		truck.thisIsATest(); //Disables activity log for test (used in city control panel)
		
		items = new ArrayList<OrderItem>();
		items.add(new OrderItem("item1", 3));
		items.add(new OrderItem("item2", 5));
		destination1 = "TEST DESTINATION 1";
		destination2 = "TEST DESTINATION 2";
		destination3 = "TEST DESTINATION 3";
		order1 = new MarketOrder(items, destination1, recipient1);
		order2 = new MarketOrder(items, destination2, recipient2);
		order3 = new MarketOrder(items, destination3, recipient3);
		//Created three market orders with distinct destinations and recipients
	}	

	/* This tests the TruckAgent receiving three deliveries from the market and delivering them all */
	public void testTruckDelivery() {		
		//Preconditions
		assertTrue(truck.capacity == 0); //No driver/passengers in delivery truck
		assertEquals(truck.orders.size(), 0); //No orders yet
		
		//Messages - giving truck 2 new orders at once
		truck.msgPleaseDeliver(order1);
		truck.msgPleaseDeliver(order2);
		
		//Postconditions
		assertTrue(truck.orders.size() == 2); //Now, 2 orders for truck
		
		//Scheduler call - should cause truck to deliver order #1
		truck.pickAndExecuteAnAction(); 
		
		//Recipient should have gotten an order delivery message - confirm
		assertTrue(recipient1.log.getLastLoggedEvent().getMessage() == "Received order from market truck");
		
		//Postconditions
		assertTrue(truck.orders.size() == 2); //Order still exists - must tell market it's finished
		
		//Scheduler call - should let market know order is done
		truck.pickAndExecuteAnAction();
		
		//Market should receive a confirmation message - confirm
		assertTrue(market.log.getLastLoggedEvent().getMessage() == "Truck has finished delivery");
		
		//Postcondition
		assertTrue(truck.orders.size() == 1); //Truck is now finished with its first delivery
		
		//Scheduler call - should cause truck to deliver order #2
		truck.pickAndExecuteAnAction(); 
		
		//Recipient should have gotten an order delivery message - confirm
		assertTrue(recipient2.log.getLastLoggedEvent().getMessage() == "Received order from market truck");
		
		//Postconditions
		assertTrue(truck.orders.size() == 1); //Order still exists - must tell market it's finished
		
		//Scheduler call - should let market know order is done
		truck.pickAndExecuteAnAction();
		
		//Market should receive a confirmation message - confirm
		assertTrue(market.log.getLastLoggedEvent().getMessage() == "Truck has finished delivery");
		
		//Postcondition
		assertTrue(truck.orders.size() == 0); //Truck is now finished with its second delivery
		
		//Message - give the truck one more order to deliver
		truck.msgPleaseDeliver(order3);
		
		//Postconditions
		assertTrue(truck.orders.size() == 1); //Now, 1 order for truck
		
		//Scheduler call - should cause truck to deliver order #1
		truck.pickAndExecuteAnAction(); 
		
		//Recipient should have gotten an order delivery message - confirm
		assertTrue(recipient3.log.getLastLoggedEvent().getMessage() == "Received order from market truck");
		
		//Postconditions
		assertTrue(truck.orders.size() == 1); //Order still exists - must tell market it's finished
		
		//Scheduler call - should let market know order is done
		truck.pickAndExecuteAnAction();
		
		//Market should receive a confirmation message - confirm
		assertTrue(market.log.getLastLoggedEvent().getMessage() == "Truck has finished delivery");
		
		//Postcondition
		assertTrue(truck.orders.size() == 0); //Truck is now finished with its third and final delivery
	}
}