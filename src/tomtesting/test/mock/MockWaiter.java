package tomtesting.test.mock;

import java.util.List;

import restaurant.Check;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter {
	
	
	public MockWaiter(String name) {
		super(name);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getMaitreDName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getWaitingCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtLobby() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGiveOrder(String order, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAssignMeCustomer(Customer customer, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToOrder(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToPay(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCheckIsReady(Check check) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("received message check is ready"));
		
	}

	@Override
	public void msgcustomerleft(Customer customerleft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCustomerIsGone(Customer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFoodIsReady(String order, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFoodIsOut(String order, int assignedtablenumber) {
		// TODO Auto-generated method stub
		
	}

	
	
}
