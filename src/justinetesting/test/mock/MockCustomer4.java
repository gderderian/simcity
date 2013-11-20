package justinetesting.test.mock;

import city.Restaurant4.WaiterRole4.Menu;
import justinetesting.interfaces.Customer4;
import justinetesting.interfaces.Waiter4;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 */
public class MockCustomer4 extends Mock4 implements Customer4 {
	public EventLog4 log= new EventLog4();

	public MockCustomer4(String name) {
		super(name);

	}

	@Override
	public void msgHereIsChange(boolean paid){
		log.add(new LoggedEvent4("Received msgHereIsChange from cashier. Paid = " + paid));
		System.out.println(name + ": Received msgHereIsChange from cashier. Paid = " + paid);
	}

	@Override
	public void msgHereIsBill(double amount){
		log.add(new LoggedEvent4("Received msgHereIsBill from waiter. Amount = " + amount));
		System.out.println(name + ": Received msgHereIsBill from waiter. Amount = " + amount);
	}

	@Override
	public Waiter4 getWaiter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPositionInLine(int spot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSitAtTable(int xTable, int yTable, Waiter4 waiter, Menu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRestaurantFull() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatDoWant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatDoWant(String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsFood(String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRestClosed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		
	}
	
}
