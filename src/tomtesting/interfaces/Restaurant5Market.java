package tomtesting.interfaces;


public interface Restaurant5Market {

	//public MarketAgent(String type);
	
	// Messages
	//After receiving message from the cook, start packing supplies
	public void msgReceviedOrderFromCook(Restaurant5Cook cook, String order);

	public void msgReceivedCheckFromCashier(int amount);
	
}
