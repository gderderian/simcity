package justinetesting.interfaces;

import city.Restaurant4.WaiterRole4.Menu;

/**
 * A sample Customer interface built to unit test a CashierAgent.
 *
 */
public interface Customer4 {
	
	public abstract Waiter4 getWaiter();
	
	public abstract void gotHungry();
	
	public abstract void msgPositionInLine(int spot);
	
	public abstract void msgSitAtTable(int xTable, int yTable, Waiter4 waiter, Menu menu);
	
	public abstract void msgRestaurantFull();
	
	public abstract void msgAnimationFinishedGoToSeat();
	
	public abstract void msgWhatDoWant();
	
	public abstract void msgWhatDoWant(String choice);
	
	public abstract void msgHereIsFood(String choice);
	
	public abstract void msgHereIsChange(boolean paid);

	public abstract void msgHereIsBill(double amount);
	
	public abstract void msgRestClosed();
	
	public abstract void msgAnimationFinishedLeaveRestaurant();

}