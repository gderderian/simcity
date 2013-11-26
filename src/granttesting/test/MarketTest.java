package granttesting.test;

import java.util.ArrayList;

import Role.MarketManager;
import Role.MarketManager.myMarketOrder;
import granttesting.test.mock.MockCook;
import granttesting.test.mock.MockMarketWorker;
import junit.framework.TestCase;
import city.MarketOrder;
import city.OrderItem;
import city.PersonAgent;

public class MarketTest extends TestCase{
        
    PersonAgent person;
    MockCook restaurantCook;
    MarketManager marketMgr;
    MockMarketWorker marketWorker;
    public ArrayList<OrderItem> testOrderItems;
    
    public void setUp() throws Exception{

            super.setUp();                
            person = new PersonAgent("Person");
            restaurantCook = new MockCook("MockCook");
            marketMgr = new MarketManager("MarketManager", person);
            marketWorker = new MockMarketWorker("MarketWorker");
            
            testOrderItems = new ArrayList<OrderItem>();
            
    }
    
    public void testNormativeMarketOrder(){
            
    	// MarketManager shouldn't be keeping track of any orders right now
    	assertEquals("MarketManager orderList should be empty", marketMgr.myOrders.size(), 0);
    	
    	// Try to submit order to the MarketManager of 5 chickens
    	OrderItem testItem = new OrderItem("Chicken", 5);
    	marketMgr.msgHereIsOrder(new MarketOrder(testOrderItems, person));
    	
    	
    }  

}