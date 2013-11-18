package justinetesting.interfaces;

public interface Host4 {

	public abstract void msgIWantFood(Customer4 c);
	
	public abstract void msgSeatingCustomer(Customer4 c);
	
	public abstract void msgLeavingRest(Customer4 c);
	
	public void msgTableAvaliable(Customer4 c);
	
	public void msgWantToGoOnBreak(Waiter4 w);
	
	public void msgReadyToWork(Waiter4 w);
	
	public void msgRestClosed();
	
}
