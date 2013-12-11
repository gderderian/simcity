package interfaces;

import city.MarketOrder;

public interface MarketWorker {
	
	public void msgPrepareOrder(MarketOrder o, MarketManager recipientManager);

	public void releaseSemaphore();

}