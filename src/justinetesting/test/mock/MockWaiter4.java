package justinetesting.test.mock;

import city.Restaurant4.CashierRole4;
import justinetesting.interfaces.Customer4;
import justinetesting.interfaces.Waiter4;

public class MockWaiter4 extends Mock4 implements Waiter4 {
	public EventLog4 log= new EventLog4();
	
	public MockWaiter4(String name) {
		super(name);
	}
	
	@Override
	public void msgHereIsBill(double amount, Customer4 c){
		log.add(new LoggedEvent4("Received msgHereIsBill from cashier. Total = " + amount));
		c.msgHereIsBill(amount);
		System.out.println("waiter: Received msgHereIsBill from cashier. Total = " + amount);
	}

	@Override
	public void msgNumber(int num) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPleaseSeatCustomer(Customer4 c, int tableNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToOrder(Customer4 c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsChoice(Customer4 c, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfFood(String choice, Customer4 c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRestocked(String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderDone(String choice, Customer4 c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneEating(Customer4 c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWantBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBreakApproved() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBreakDenied() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyForBill(Customer4 c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAllMarketsOut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCook() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtEntrance() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtHome() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CashierRole4 getCashier() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
