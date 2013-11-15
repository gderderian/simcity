package interfaces;

import java.util.HashMap;

import test.mock.EventLog;

public interface Restaurant2Market {
	
	EventLog log = new EventLog();
	
	public void msgHereIsPayment(double payment);
	
	public void msgPlaceFoodOrder(Restaurant2Cook cook, HashMap<String, Integer> order);
	
}
