package hollytesting.interfaces;

public interface Restaurant2Host {
	
	public void msgIWantFood(Restaurant2Customer cust);
	
	public void msgNoTablesLeaving(Restaurant2Customer c);
	
	public void msgIllStay(Restaurant2Customer c);

	public void msgTableIsFree(int table);
	
	public void msgRequestBreak(Restaurant2Waiter w);
	
	public void msgBackFromBreak(Restaurant2Waiter w);

}
