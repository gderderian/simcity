package tomtesting.interfaces;
import city.Restaurant5.Restaurant5Check;
//import restaurant.CustomerAgent;
//import restaurant.WaiterAgent;
import tomtesting.interfaces.Restaurant5Customer;

public interface Restaurant5Cashier {

	//public CashierAgent(String name);

	// Messages
	//After receiving message from the cook, start packing supplies
public void msgReceviedCheckFromCustomer(Restaurant5Check checkfromcustomer );
	
public void msgMakeCheckForWaiter(Restaurant5Customer customer, String choice, int table , Restaurant5Waiter waiter);
	
	
}
