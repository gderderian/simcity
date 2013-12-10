package interfaces;

import Role.MarketManagerRole;
import city.MarketOrder;

public interface MarketWorker {
	
	public void msgPrepareOrder(MarketOrder o, MarketManagerRole recipientManager);

	public void releaseSemaphore();

}