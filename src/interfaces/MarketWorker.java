package interfaces;

import Role.MarketManager;
import city.MarketOrder;

public interface MarketWorker {
	
	public void msgPrepareOrder(MarketOrder o, MarketManager recipientManager);

}