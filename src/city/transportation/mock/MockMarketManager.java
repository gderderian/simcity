package city.transportation.mock;

import test.mock.EventLog;
import test.mock.LoggedEvent;
import city.MarketOrder;
import interfaces.MarketManager;

public class MockMarketManager implements MarketManager {
	
	public EventLog log;
	
	public MockMarketManager() {
		log = new EventLog();
	}

	public void msgHereIsOrder(MarketOrder o) {	}

	public void msgOrderPicked(MarketOrder o) {	}

	@Override
	public void msgFinishedDelivery(MarketOrder o) {
		log.add(new LoggedEvent("Truck has finished delivery"));
		
	}

	@Override
	public String getMarketName() {
		// TODO Auto-generated method stub
		return null;
	}

}
