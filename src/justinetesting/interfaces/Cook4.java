package justinetesting.interfaces;

public interface Cook4 {

	public abstract void msgHereIsOrder(Waiter4 w, String choice, Customer4 c);
	
	public abstract void msgPickedUpFood(Customer4 c);
	
	public abstract void msgHereIsDelivery(int st, int ch, int s, int p, boolean successful);
	
	public abstract void msgOutOfItem(Market4 m, String type);
	
}
