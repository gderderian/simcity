package tomtesting.interfaces;

import java.util.List;

import city.Restaurant5.Restaurant5Check;
//import restaurant.Check;

//import restaurant.WaiterAgent.orders;

public interface Restaurant5Waiter {
	//public WaiterAgent(String name, Host host, Cook cook, Cashier cashier);
	
	
	
	int xCoordinate = 0;
	int yCoordinate = 0;

	public String getMaitreDName();
	
	public String getName();

	public List getWaitingCustomers();
	
	public void msgAtTable();

	public void msgAtLobby();
	
	public void msgGiveOrder(String order, int table);
	
	public void msgAssignMeCustomer(Restaurant5Customer customer, int table);
	
	public void msgReadyToOrder(Restaurant5Customer customer);
	
	public void msgReadyToPay(Restaurant5Customer customer);
	
	public void msgCheckIsReady(Restaurant5Check check);
	
	public void msgcustomerleft(Restaurant5Customer customerleft);
	
	public void msgCustomerIsGone(Restaurant5Customer customer);

	public void msgFoodIsReady(String order, int table);

	public void msgFoodIsOut(String order, int assignedtablenumber);

}
