package granttesting.test;

import java.util.ArrayList;

import Role.MarketCustomerRole;
import Role.MarketManagerRole;
import Role.MarketManagerRole.myMarketWorker;
import Role.MarketManagerRole.orderState;
import Role.MarketWorkerRole;
import Role.MarketWorkerRole.orderPickState;
import junit.framework.TestCase;
import city.Market;
import city.MarketOrder;
import city.OrderItem;
import city.PersonAgent;
import city.Restaurant3.CookRole3;

public class MarketTest extends TestCase{
        
    PersonAgent person;
    PersonAgent person2;
    PersonAgent person3;
    PersonAgent person4;
    CookRole3 restaurantCook;
    MarketManagerRole marketMgr;
    MarketWorkerRole marketWorker;
    MarketCustomerRole marketCustomer;
    myMarketWorker myMarketWorker;
    public ArrayList<OrderItem> testOrderItems;
    
    public void setUp() throws Exception{
            
    	super.setUp();
        person = new PersonAgent("Person");
        person2 = new PersonAgent("Person2");
        person3 = new PersonAgent("Person3");
        person4 = new PersonAgent("Person4");
        restaurantCook = new CookRole3("MockCook", person4);
        Market mkt1 = new Market("Vons");
        marketMgr = new MarketManagerRole("MarketManager", person, mkt1);
        marketMgr.setTesting();
        mkt1.setManager(marketMgr);
        marketWorker = new MarketWorkerRole(person2);
        marketWorker.setTest();
        marketCustomer = new MarketCustomerRole("Person3", person3);
        testOrderItems = new ArrayList<OrderItem>();
      
    }
    
    public void testNormativeOrderWorkerAssignment(){
            
    	// MarketManager shouldn't be keeping track of any orders right now
    	assertEquals("MarketManager orderList should be empty", marketMgr.myOrders.size(), 0);
    	
    	// Try to submit order to the MarketManager of 5 pieces of chicken
    	OrderItem testItem = new OrderItem("Chicken", 5);
    	testOrderItems.add(testItem);
    	MarketOrder mOrder = new MarketOrder(testOrderItems, person);
    	marketMgr.msgHereIsOrder(mOrder);
    	
    	// MarketManager should now have one order that it needs to process
    	assertEquals("MarketManager orderList should now have one order", marketMgr.myOrders.size(), 1);
    
    	// Add new worker who will be picking the order
        marketMgr.addWorker(marketWorker);
    	
    	// There should be one worker now added to assign the order picking process to
    	assertEquals("One marketWorker should now exist in the manager, is" + marketMgr.myWorkers.size(), marketMgr.myWorkers.size(), 1);
    	
    	// Process the first order that was just submitted by calling MarketManager's pickAndExecuteAnAction()
    	marketMgr.pickAndExecuteAnAction();
    	
    	// Order should now be in assignedToWorker state
    	assertTrue("First order in the list should now be assigned to a worker", marketMgr.myOrders.get(0).state == orderState.assignedToWorker);
    	
    }
    
    public void testNormativeOrderProcessing(){
        
    	// MarketManager shouldn't be keeping track of any orders right now
    	assertEquals("MarketManager orderList should be empty", marketMgr.myOrders.size(), 0);
    	
    	// Try to submit order to the MarketManager of 5 pieces of chicken
    	OrderItem testItem = new OrderItem("Chicken", 5);
    	testOrderItems.add(testItem);
    	marketMgr.msgHereIsOrder(new MarketOrder(testOrderItems, person));
    	
    	// MarketManager should now have one order that it needs to process
    	assertEquals("MarketManager orderList should now have one order", marketMgr.myOrders.size(), 1);
    
    	// Add new worker who will be picking the order
        marketMgr.addWorker(marketWorker);
    	
    	// There should be one worker now added to assign the order picking process to
    	assertEquals("One marketWorker should now exist in the manager, is" + marketMgr.myWorkers.size(), marketMgr.myWorkers.size(), 1);
    	
    	// Process the first order that was just submitted by calling MarketManager's pickAndExecuteAnAction()
    	marketMgr.pickAndExecuteAnAction();
    	
    	// Order should now be in assignedToWorker state
    	assertTrue("First order in the list should now be assigned to a worker", marketMgr.myOrders.get(0).state == orderState.assignedToWorker);
    	
    	// Market order should now be within the worker's pickOrder list
    	assertEquals("MarketWorker should have one order in their possession to pick", marketWorker.pickOrders.size(), 1);
    	
    	// Worker should now begin to do process their order
    	
    	// Worker should now have set their order status to pending because they've just picked their order and it's not fully ready
    	assertTrue("Order should be pending, order actually is " + marketWorker.pickOrders.get(0).state, marketWorker.pickOrders.get(0).state == orderPickState.pending);
    	
    	// Worker should now have one order left because they told their manager that it's ready to go!
    	assertEquals("MarketManager orderList should now have no orders", marketMgr.myOrders.size(), 1);
    	
    	
    }  

}