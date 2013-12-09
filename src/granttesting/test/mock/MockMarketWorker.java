package granttesting.test.mock;

import Role.MarketManager;
import test.mock.EventLog;
import test.mock.Mock;
import city.MarketOrder;
import interfaces.MarketWorker;

public class MockMarketWorker extends Mock implements MarketWorker {

	public EventLog log;
	
	public MockMarketWorker(String name) {
		super("");
		log = new EventLog();
	}

	@Override
	public void msgPrepareOrder(MarketOrder o, MarketManager recipientManager) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void releaseSemaphore() {
		// TODO Auto-generated method stub
		
	}
	
}