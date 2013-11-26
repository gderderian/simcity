package granttesting.test;

import granttesting.test.mock.MockCook;
import granttesting.test.mock.MockMarketWorker;
import junit.framework.TestCase;
import city.PersonAgent;
import granttesting.test.mock.MockMarketManager;

public class MarketTest extends TestCase{
        
    PersonAgent person;
    MockCook restaurantCook;
    MockMarketManager marketManager;
    MockMarketWorker marketWorker;
    
    public void setUp() throws Exception{
            super.setUp();                
            person = new PersonAgent("Person");
            restaurantCook = new MockCook("MockCook");
            marketManager = new MockMarketManager();
            marketWorker = new MockMarketWorker("MarketWorker");
    }
    
    public void testNormativeMarketOrder(){
            
    	
    	
    }  

}