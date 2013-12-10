package interfaces;

import city.MarketOrder;

public interface MarketManager {
	
	public void msgHereIsOrder(MarketOrder o);
	
	public void msgOrderPicked(MarketOrder o);
	
	public void msgFinishedDelivery(MarketOrder o);

	public String getMarketName();

}
