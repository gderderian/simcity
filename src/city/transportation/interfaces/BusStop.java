package city.transportation.interfaces;

import city.PersonAgent;
import city.transportation.BusAgent;

public interface BusStop {

	public void msgWaitingForBus(PersonAgent p);
	
	public void msgICanPickUp(BusAgent b, int people);
	
}
