package justinetesting.interfaces;

import city.MarketOrder;
import city.transportation.TruckAgent;

public interface Cook4 {

	public abstract void msgHereIsOrder(Waiter4 w, String choice, Customer4 c);
	
	public abstract void msgPickedUpFood(Customer4 c);
	
	public void msgHereIsYourOrder(TruckAgent t, MarketOrder mo);
	
	
	
}
