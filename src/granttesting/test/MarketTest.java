package granttesting.test;

import java.util.ArrayList;

import Role.MarketManager;
import Role.MarketManager.myMarketWorker;
import Role.MarketManager.orderState;
import Role.MarketWorker;
import Role.MarketWorker.orderPickState;
import granttesting.test.mock.MockCook;
import junit.framework.TestCase;
import city.MarketOrder;
import city.OrderItem;
import city.PersonAgent;

public class MarketTest extends TestCase{
        
    PersonAgent person;
    PersonAgent person2;
    MockCook restaurantCook;
    MarketManager marketMgr;
    MarketWorker marketWorker;
    myMarketWorker myMarketWorker;
    public ArrayList<OrderItem> testOrderItems;
    
    public void setUp() throws Exception{

            super.setUp();                
            person = new PersonAgent("Person");
            person2 = new PersonAgent("Person2");
            restaurantCook = new MockCook("MockCook");
            marketMgr = new MarketManager("MarketManager", person);
            marketWorker = new MarketWorker(person2);
         
            testOrderItems = new ArrayList<OrderItem>();
            
            
    }
    
    public void testNormativeOrderWorkerAssignment(){
            
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
    	marketWorker.pickAndExecuteAnAction();
    	
    	// Worker should now have set their order status to processing
    	assertTrue("Worker should have begun picking order", marketWorker.pickOrders.get(0).state == orderPickState.picking);
    	
    	// Worker should now see order as done and notify the manager that it's ready to go!
    	marketWorker.pickAndExecuteAnAction();
    	assertTrue("Worker should have finished picking order. Is instead " + marketWorker.pickOrders.get(0).state, marketWorker.pickOrders.get(0).state == orderPickState.done);
    	
    	
    	
    }  

}