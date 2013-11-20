package tomtesting.interfaces;


import java.util.Collection;
import java.util.List;
//import restaurant.HostAgent.Table;

public interface Restaurant5Host {

	//public HostAgent(String name);
	public class Table {}
	
	public String getMaitreDName();
	
	public String getName();
	
	public List getWaitingCustomers();
	
	public Collection getTables();
	
	public int[] getxcoordinatesTables();
	
	public int[] getycoordinatesTables();
	
	public void addwaiter(Restaurant5Waiter w);
	// Messages

	public void msgIWantFood(Restaurant5Customer cust);

	public void msgTableIsEmpty(/*restaurant.HostAgent.Table T*/Restaurant5Host.Table T);

	public void msgCustomerleftTable(Restaurant5Customer cust);

	public void msgWaiterWantBreak(Restaurant5Waiter w);

	public void msgWaiterComeBackFromBreak(Restaurant5Waiter w);
	
	public void msgWaiterBackInLobby(Restaurant5Waiter w);

	
}
