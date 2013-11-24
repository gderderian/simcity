package interfaces;

import city.PersonAgent;
import city.transportation.BusAgent;

public interface BusStop {

	public void msgWaitingForBus(Person p);
	
	public void msgICanPickUp(Bus b, int people);
}
