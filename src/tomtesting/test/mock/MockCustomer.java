package tomtesting.test.mock;


import restaurant.Check;
import restaurant.CustomerAgent.AgentState;
import restaurant.gui.CustomerGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Teryun Lee
 *
 */
public class MockCustomer extends Mock implements Customer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	
	 */
	
	public Cashier cashier;
	int currentmoney;

	public MockCustomer(String name) {
		super(name);
		currentmoney = 6;

	}
	

	@Override
	public String getCustomerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSitAtTable(int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTakeOrder(Waiter waiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood(Waiter waiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWaiter(Waiter waiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFoodIsOut(Waiter waiter, String order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReceivedCheckFromWaiter(Check checkfromwaiter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReceivedMoneyFromCashier(int moneyleftfromeating) {
		log.add(new LoggedEvent("Received HereIsYourChange from cashier"));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDontHaveMoneyPayBackLater(int paybackmoney) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Don't have enough money. payback later"));
	}

	@Override
	public void msgDontHaveMoneyWashDishes() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRestaurantFullLeave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getHungerLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getOrderingLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getLookingAtCheckTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int gettablenumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Waiter getwaiter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AgentState getwaiterstate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHungerLevel(int hungerLevel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(CustomerGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CustomerGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getxcoordinate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getycoordinate() {
		// TODO Auto-generated method stub
		return 0;
	}

}
