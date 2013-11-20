package tomtesting.interfaces;

//import restaurant.MarketAgent;
//import restaurant.WaiterAgent;
//import restaurant.CookAgent.cookingorders;
//import restaurant.CookAgent.mymarket;

public interface Restaurant5Cook {

	//public CookAgent(String name);
	
	public String getName();
	
	// Messages
	//After receiving message from the waiter start cooking
	public void msgReceviedOrderFromWaiter(Restaurant5Waiter waiter, String order, int table);
	
	public void msgReceivedSuppliesFromMarket(String order, Restaurant5Market market);

	public void msgSupplyIsOut(String order, Restaurant5Market market);
	

	public void msgDepleteCookSupply();
	
	
}
