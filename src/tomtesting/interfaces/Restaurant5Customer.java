package tomtesting.interfaces;

import city.Restaurant5.Restaurant5Check;
//import restaurant.CashierAgent;
//import restaurant.Check;
//import restaurant.HostAgent;
//import restaurant.WaiterAgent;
//import restaurant.CustomerAgent.AgentEvent;
//import restaurant.CustomerAgent.AgentState;
import restaurant.gui.CustomerGui;

public interface Restaurant5Customer {
	//public CustomerAgent(String name);
	
	
	public String getCustomerName();
	
	//messages

	public void gotHungry();

	public void msgSitAtTable(int table);
	
	public void msgAnimationFinishedGoToSeat();
	
	public void msgTakeOrder(Restaurant5Waiter waiter);
	
	public void msgHereIsYourFood(Restaurant5Waiter waiter);
	public void setWaiter(Restaurant5Waiter waiter);

	public void msgFoodIsOut(Restaurant5Waiter waiter, String order);
	
	public void msgReceivedCheckFromWaiter(Restaurant5Check checkfromwaiter);
	
	public void msgReceivedMoneyFromCashier(int moneyleftfromeating);
	
	public void msgDontHaveMoneyPayBackLater( int paybackmoney);
		
	public void msgDontHaveMoneyWashDishes();
	
	public void msgRestaurantFullLeave();
	
	public void msgAnimationFinishedLeaveRestaurant();
	
	public String getName();
	
	public int getHungerLevel();
	
	public int getOrderingLevel();
	
	public int getLookingAtCheckTime();
	
	public int gettablenumber();
	
	public Restaurant5Waiter getwaiter();
	
	public AgentState getwaiterstate();

	public void setHungerLevel(int hungerLevel);

	public String toString();

	public void setGui(CustomerGui g);

	public CustomerGui getGui();

	public int getxcoordinate();
	
	public int getycoordinate();
	
	
	
	

	
}
